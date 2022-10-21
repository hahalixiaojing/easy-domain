package cn.easylib.domain.application.subscriber;

public interface ISubscriber {
    /**
     * 事件类型
     *
     * @return
     */
    Class<?> subscribedToEventType();
}
