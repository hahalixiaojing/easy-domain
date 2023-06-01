package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.*;
import org.apache.commons.lang3.RandomUtils;

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
public class ThreadPoolTaskDomainEventManager implements IDomainEventManager {

    private final ConcurrentHashMap<String, Map<String, SubscriberInfo>> subscribersMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, ScheduledThreadPoolExecutor> taskTheadMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> domainEventAndThreadMap = new ConcurrentHashMap<>();

    private final IOrderedPerformManager performManager;
    /**
     * 订阅默认执行条件
     */
    private static final DefaultExecuteCondition<IDomainEvent> condition = new DefaultExecuteCondition<>();

    private final int maxRetryTimes;
    private final int retryDelayTime;
    private final int initThreadCount;

    public ThreadPoolTaskDomainEventManager() {
        this(60, 3, 1500,
                new OrderedPerformManager());
    }

    public ThreadPoolTaskDomainEventManager(int initThreadCount, int maxRetryTimes, int retryDelayTime,
                                            IOrderedPerformManager iOrderedPerformManager) {
        this.initThreadCount = initThreadCount;
        this.maxRetryTimes = maxRetryTimes;
        this.retryDelayTime = retryDelayTime;
        this.performManager = iOrderedPerformManager;

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

        return this.subscribersMap.entrySet()
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

        String domainEventName = domainEventType.getName();
        this.subscribersMap.computeIfAbsent(domainEventName, s -> new HashMap<>());
        this.domainEventAndThreadMap.computeIfAbsent(domainEventName, s -> RandomUtils.nextInt(0, this.initThreadCount));
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, String alias) {
        this.registerSubscriber(subscriber, alias, "");
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, String alias, String dependSubscriber) {
        this.registerSubscriber(subscriber, alias, condition, dependSubscriber);
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, String alias, IExecuteCondition condition) {
        this.registerSubscriber(subscriber, alias, condition, "");
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, String alias, IExecuteCondition condition,
                                   String dependSubscriber) {
        String domainEventName = subscriber.subscribedToEventType().getName();

        if (this.subscribersMap.containsKey(domainEventName)) {

            Map<String, SubscriberInfo> subscriberMap = this.subscribersMap.get(domainEventName);
            if (subscriberMap.containsKey(alias)) {

                throw new IllegalArgumentException(alias + " is duplication");
            }

            this.subscribersMap.get(domainEventName).put(
                    alias,
                    new SubscriberInfo(subscriber, alias, condition)
            );
        }

        if (this.performManager != null) {

            this.performManager.registerSubscriber(domainEventName,
                    alias,
                    dependSubscriber);
        }
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, ISubscriberKey alias) {
        this.registerSubscriber(subscriber, alias, condition);
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, ISubscriberKey alias, ISubscriberKey dependSubscriber) {
        this.registerSubscriber(subscriber, alias, condition, dependSubscriber);
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, ISubscriberKey alias, IExecuteCondition condition) {
        this.registerSubscriber(subscriber, alias, condition, null);

    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, ISubscriberKey alias, IExecuteCondition condition,
                                   ISubscriberKey dependSubscriber) {

        String domainEventName = subscriber.subscribedToEventType().getName();

        if (this.subscribersMap.containsKey(domainEventName)) {

            Map<String, SubscriberInfo> subscriberMap = this.subscribersMap.get(domainEventName);
            if (subscriberMap.containsKey(alias.keyName())) {

                throw new IllegalArgumentException(alias.keyName() + " is duplication");
            }

            this.subscribersMap.get(domainEventName).put(alias.keyName(),
                    new SubscriberInfo(subscriber,
                            alias.keyName(),
                            alias, condition)
            );
        }

        if (this.performManager != null) {
            this.performManager.registerSubscriber(
                    subscriber.subscribedToEventType().getName(),
                    alias,
                    dependSubscriber);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IDomainEvent> void publishEvent(T obj) {

        String domainEventName = obj.getClass().getName();
        Map<String, SubscriberInfo> subscriberMap = this.subscribersMap.get(domainEventName);
        if (subscriberMap == null) {
            return;
        }

        //如果有执行顺序管理，先查找到根
        if (this.performManager != null) {
            List<String> rootSubscribers = this.performManager.selectRootSubscribers(domainEventName);
            subscriberMap = subscriberMap.entrySet()
                    .stream()
                    .filter(s -> rootSubscribers.contains(s.getKey()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        Integer pooledIndex = this.domainEventAndThreadMap.get(domainEventName);
        if(pooledIndex == null){
            return;
        }
        ScheduledThreadPoolExecutor threadPoolExecutor = this.taskTheadMap.get(pooledIndex);

        for (Map.Entry<String, SubscriberInfo> entry : subscriberMap.entrySet()) {

            IDomainEventSubscriber<T> subscribedTo = (IDomainEventSubscriber<T>) entry.getValue().getSubscriber();

            if (subscribedTo != null && this.executeCheck(obj, entry.getValue().getCondition())) {

                Task<T> task = this.buildTask(
                        subscribedTo,
                        obj,
                        domainEventName,
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
        String domainEventName = obj.getClass().getName();
        Map<String, SubscriberInfo> subscriberMap = this.subscribersMap.get(domainEventName);
        if (subscriberMap == null) {
            return;
        }
        Integer pooledIndex = this.domainEventAndThreadMap.get(domainEventName);
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
                        domainEventName,
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

    private boolean executeCheck(final IDomainEvent t, IExecuteCondition iExecuteCondition) {
        try {

            return iExecuteCondition.isExecute(t);

        } catch (Exception ex) {
            return false;
        }
    }

    public void close() {
        for (ScheduledThreadPoolExecutor executor : this.taskTheadMap.values()) {
            executor.shutdown();
        }
    }
}
