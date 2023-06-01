package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.*;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 基于线程池的事件任务处理器
 */
public class ThreadPoolTaskDomainEventManager extends AbstractDomainEventManager {

    private final ConcurrentHashMap<Integer, ScheduledThreadPoolExecutor> taskTheadMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> domainEventAndThreadMap = new ConcurrentHashMap<>();

    /**
     * 订阅默认执行条件
     */

    private final int maxRetryTimes;
    private final int retryDelayTime;
    private final int initThreadCount;

    public ThreadPoolTaskDomainEventManager() {
        this(60, 3, 1500,
                new OrderedPerformManager());
    }

    public ThreadPoolTaskDomainEventManager(int initThreadCount, int maxRetryTimes, int retryDelayTime,
                                            IOrderedPerformManager iOrderedPerformManager) {

        super("", iOrderedPerformManager);
        this.initThreadCount = initThreadCount;
        this.maxRetryTimes = maxRetryTimes;
        this.retryDelayTime = retryDelayTime;

        ThreadFactory threadFactory = this.createThreadFactory();

        for (int i = 0; i < this.initThreadCount; i++) {
            ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(5, threadFactory);
            this.taskTheadMap.put(i, threadPoolExecutor);
        }
    }


    public ThreadPoolTaskDomainEventManager(int initThreadCount, int maxRetryTimes, int retryDelayTime) {
        this(initThreadCount, maxRetryTimes, retryDelayTime, new OrderedPerformManager());
    }

    private ThreadFactory createThreadFactory() {
        return new ThreadFactory() {
            /**
             * 线程计数
             */
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("domain-event-thread-" + threadNumber.getAndIncrement());
                return thread;
            }
        };
    }

    @Override
    public Map<String, List<String>> allEvents() {

        return this.subscribers.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        v -> v.getValue().values().stream().map(SubscriberInfo::getAlias)
                                .collect(toList()))
                );
    }

    @Override
    public List<OrderedPerformManager.OrderData> findEventSubscriberInfo(String eventName) {
        return this.performManager.selectEventSubscriberInfo(eventName);
    }

    @Override
    public void registerDomainEvent(Class<?> domainEventType) {

        EventNameInfo eventName = this.getEventName(domainEventType);

        this.subscribers.computeIfAbsent(eventName.eventName, s -> new HashMap<>());
        this.domainEventAndThreadMap.computeIfAbsent(eventName.eventName, s -> RandomUtils.nextInt(0, this.initThreadCount));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IDomainEvent> void publishEvent(T obj) {

        EventNameInfo eventName = this.getEventName(obj.getClass());

        Map<String, SubscriberInfo> subscriberMap = this.subscribers.get(eventName.eventName);
        if (subscriberMap == null) {
            return;
        }

        //如果有执行顺序管理，先查找到根
        if (this.performManager != null) {
            List<String> rootSubscribers = this.performManager.selectRootSubscribers(eventName.eventName);
            subscriberMap = subscriberMap.entrySet()
                    .stream()
                    .filter(s -> rootSubscribers.contains(s.getKey()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        Integer pooledIndex = this.domainEventAndThreadMap.get(eventName.eventName);
        if (pooledIndex == null) {
            return;
        }
        ScheduledThreadPoolExecutor threadPoolExecutor = this.taskTheadMap.get(pooledIndex);

        for (Map.Entry<String, SubscriberInfo> entry : subscriberMap.entrySet()) {

            IDomainEventSubscriber<T> subscribedTo = (IDomainEventSubscriber<T>) entry.getValue().getSubscriber();

            if (subscribedTo != null && this.executeCheck(obj, entry.getValue().getCondition())) {

                Task<T> task = this.buildTask(
                        subscribedTo,
                        obj,
                        eventName.eventName,
                        entry.getKey(),
                        threadPoolExecutor,
                        false);
                threadPoolExecutor.schedule(task, 0, TimeUnit.MILLISECONDS);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IDomainEvent> void publishEvent(T obj, String subscriber) {
        this.publishEvent(obj, subscriber, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IDomainEvent> void publishEvent(T obj, String subscriber, boolean onlyThis) {
        EventNameInfo eventName = this.getEventName(obj.getClass());


        Map<String, SubscriberInfo> subscriberMap = this.subscribers.get(eventName.eventName);
        if (subscriberMap == null) {
            return;
        }
        Integer pooledIndex = this.domainEventAndThreadMap.get(eventName.eventName);
        if (pooledIndex == null) {
            return;
        }
        ScheduledThreadPoolExecutor threadPoolExecutor = this.taskTheadMap.get(pooledIndex);

        for (Map.Entry<String, SubscriberInfo> entry : subscriberMap.entrySet()) {

            IDomainEventSubscriber<T> subscribedTo = (IDomainEventSubscriber<T>) entry.getValue().getSubscriber();

            if (subscribedTo != null && entry.getKey().equals(subscriber) &&
                    this.executeCheck(obj, entry.getValue().getCondition())) {

                Task<T> task = this.buildTask(subscribedTo,
                        obj,
                        eventName.eventName,
                        entry.getKey(),
                        threadPoolExecutor,
                        onlyThis);
                threadPoolExecutor.schedule(task, 0, TimeUnit.MILLISECONDS);
                break;
            }
        }
    }

    private <T extends IDomainEvent> Task<T> buildTask(IDomainEventSubscriber<T> subscribedTo,
                                                       T obj,
                                                       String domainEventName,
                                                       String alias,
                                                       ScheduledThreadPoolExecutor threadPoolExecutor, boolean onlyThis) {
        return new Task<>(subscribedTo, obj, this.maxRetryTimes, this.retryDelayTime,
                threadPoolExecutor, s -> {
            if (this.performManager != null && !onlyThis) {

                List<String> nextSubscriberList = this.performManager.selectNextSubscribers(
                        domainEventName, alias
                );

                nextSubscriberList.forEach(ss -> this.publishEvent(obj, ss, false));
            }
        });
    }


    public void close() {
        for (ScheduledThreadPoolExecutor executor : this.taskTheadMap.values()) {
            executor.shutdown();
        }
    }
}
