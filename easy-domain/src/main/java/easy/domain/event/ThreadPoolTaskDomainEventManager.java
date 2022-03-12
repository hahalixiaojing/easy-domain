package easy.domain.event;

import easy.domain.application.subscriber.*;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

/**
 * 基于线程池的事件任务处理器
 */
public class ThreadPoolTaskDomainEventManager implements IDomainEventManager {

    private final ConcurrentHashMap<String, List<SubscriberInfo>> subscribersMap = new ConcurrentHashMap<>();
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
        this(60, 3, 1500, new DefaultOrderedPerformManager());
    }

    public ThreadPoolTaskDomainEventManager(int initThreadCount, int maxRetryTimes, int retryDelayTime, IOrderedPerformManager iOrderedPerformManager) {
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
        this(initThreadCount, maxRetryTimes, retryDelayTime,new DefaultOrderedPerformManager());
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
    public void registerDomainEvent(Class<?> domainEventType) {

        String domainEventName = domainEventType.getName();
        this.subscribersMap.computeIfAbsent(domainEventName, s -> new ArrayList<>());
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
    public void registerSubscriber(ISubscriber subscriber, String alias, IExecuteCondition condition, String dependSubscriber) {
        String domainEventName = subscriber.subscribedToEventType().getName();
        if (this.subscribersMap.containsKey(domainEventName)) {
            this.subscribersMap.get(domainEventName).add(new SubscriberInfo(subscriber, alias, condition));
        }

        if (this.performManager != null) {
            this.performManager.registerSubscriber(subscriber.subscribedToEventType().getName(), alias, dependSubscriber);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IDomainEvent> void publishEvent(T obj) {

        String domainEventName = obj.getClass().getName();
        List<SubscriberInfo> subscriberInfoList = this.subscribersMap.get(domainEventName);

        //如果有执行顺序管理，先查找到根
        if (this.performManager != null) {
            List<String> rootSubscribers = this.performManager.selectRootSubscribers(domainEventName);
            subscriberInfoList = subscriberInfoList.stream().filter(s -> rootSubscribers.contains(s.getAlias())).collect(toList());
        }

        Integer pooledIndex = this.domainEventAndThreadMap.get(domainEventName);
        ScheduledThreadPoolExecutor threadPoolExecutor = this.taskTheadMap.get(pooledIndex);


        for (SubscriberInfo sub : subscriberInfoList) {
            IDomainEventSubscriber<T> subscribedTo = (IDomainEventSubscriber<T>) sub.getSubscriber();
            if (subscribedTo != null && this.executeCheck(obj, sub.getCondition())) {
                Task<T> task = new Task<>(subscribedTo, obj, this.maxRetryTimes, this.retryDelayTime, threadPoolExecutor, s -> {
                    if (this.performManager != null) {
                        List<String> nextSubscriberList = this.performManager.selectNextSubscribers(domainEventName, sub.getAlias());
                        nextSubscriberList.forEach(ss -> this.publishEvent(obj, ss, false));
                    }
                });
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
        List<SubscriberInfo> subscriberInfoList = this.subscribersMap.get(domainEventName);

        Integer pooledIndex = this.domainEventAndThreadMap.get(domainEventName);
        ScheduledThreadPoolExecutor threadPoolExecutor = this.taskTheadMap.get(pooledIndex);

        for (SubscriberInfo sub : subscriberInfoList) {
            IDomainEventSubscriber<T> subscribedTo = (IDomainEventSubscriber<T>) sub.getSubscriber();
            if (subscribedTo != null && sub.getAlias().equals(subscriber) && this.executeCheck(obj, sub.getCondition())) {
                Task<T> task = new Task<>(subscribedTo, obj, this.maxRetryTimes, this.retryDelayTime, threadPoolExecutor, s -> {
                    if (this.performManager != null && !onlyThis) {
                        List<String> nextSubscriberList = this.performManager.selectNextSubscribers(domainEventName, sub.getAlias());
                        nextSubscriberList.forEach(ss -> this.publishEvent(obj, ss, false));
                    }
                });
                threadPoolExecutor.schedule(task, 0, TimeUnit.MILLISECONDS);
                break;
            }
        }
    }

    /**
     * 订阅是否满足执行条件检查
     *
     * @param iExecuteCondition 执行检查逻辑
     * @return true=可以执行， false=不可以执行
     */
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
