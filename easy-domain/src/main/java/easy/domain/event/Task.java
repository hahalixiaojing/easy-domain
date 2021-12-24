package easy.domain.event;

import easy.domain.application.subscriber.IDomainEventSubscriber;
import easy.domain.application.subscriber.IOrderedPerformManager;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class Task<T extends IDomainEvent> implements Runnable {

    private final IDomainEventSubscriber<T> subscriber;
    private final T aDomainEvent;
    private final AtomicInteger retryTimes = new AtomicInteger(0);
    private final ScheduledThreadPoolExecutor threadPoolExecutor;
    private final int maxRetryTimes;
    private final int retryDelayTime;
    private final ITaskCallback iTaskCallback;


    public Task(IDomainEventSubscriber<T> subscriber, T aDomainEvent, int maxRetryTimes, int retryDelayTime, ScheduledThreadPoolExecutor threadPoolExecutor,ITaskCallback iTaskCallback) {
        this.subscriber = subscriber;
        this.aDomainEvent = aDomainEvent;
        this.maxRetryTimes = maxRetryTimes;
        this.retryDelayTime = retryDelayTime;
        this.threadPoolExecutor = threadPoolExecutor;
        this.iTaskCallback = iTaskCallback;
    }

    @Override
    public void run() {
        try {
            this.subscriber.handleEvent(this.aDomainEvent);
            this.iTaskCallback.execute(this);

        } catch (Exception ex) {
            int times = this.retryTimes.get();
            if (times < this.maxRetryTimes) {
                this.threadPoolExecutor.schedule(this, this.retryDelayTime, TimeUnit.MILLISECONDS);
                this.retryTimes.incrementAndGet();
            }
        }
    }
}
