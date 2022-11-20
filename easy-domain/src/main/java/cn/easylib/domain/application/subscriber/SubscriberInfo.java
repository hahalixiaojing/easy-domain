package cn.easylib.domain.application.subscriber;

/**
 * 事件订单对象
 *
 * @author lixiaojing
 */
public class SubscriberInfo {
    /**
     * 事件订阅程序
     */
    private final ISubscriber subscriber;
    /**
     * 事件订阅别名
     */
    private final String alias;

    private final ISubscriberKey subscriberKey;
    /**
     * 执行条件
     */
    private final IExecuteCondition condition;

    public SubscriberInfo(ISubscriber subscriber,
                          String alias,
                          IExecuteCondition condition) {

        this(subscriber,alias,null,condition);
    }

    public SubscriberInfo(ISubscriber subscriber, String alias,
                          ISubscriberKey subscriberKey,
                          IExecuteCondition condition) {
        this.subscriber = subscriber;
        this.alias = alias;
        this.subscriberKey = subscriberKey;
        this.condition = condition;
    }

    public ISubscriber getSubscriber() {
        return subscriber;
    }

    public String getAlias() {
        return alias;
    }

    public IExecuteCondition getCondition() {
        return condition;
    }

    public ISubscriberKey getSubscriberKey() {
        return subscriberKey;
    }
}
