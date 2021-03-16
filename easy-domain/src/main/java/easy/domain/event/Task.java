package easy.domain.event;

import easy.domain.application.subscriber.IDomainEventSubscriber;

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


    public Task(IDomainEventSubscriber<T> subscriber, T aDomainEvent, int maxRetryTimes, int retryDelayTime, ScheduledThreadPoolExecutor threadPoolExecutor) {
        this.subscriber = subscriber;
        this.aDomainEvent = aDomainEvent;
        this.maxRetryTimes = maxRetryTimes;
        this.retryDelayTime = retryDelayTime;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public void run() {
        try {
            this.subscriber.handleEvent(this.aDomainEvent);

        } catch (Exception ex) {
            int times = this.retryTimes.get();
            if (times < this.maxRetryTimes) {
                this.threadPoolExecutor.schedule(this, this.retryDelayTime, TimeUnit.MILLISECONDS);
                this.retryTimes.incrementAndGet();
            }
        }
    }
}
