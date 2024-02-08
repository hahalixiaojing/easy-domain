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

    /**
     * 执行条件
     */
    private final IExecuteCondition condition;

    private final SubscriberDelayLevel delayLevel;


    public SubscriberInfo(ISubscriber subscriber, String alias,
                          IExecuteCondition condition,SubscriberDelayLevel delayLevel) {
        this.subscriber = subscriber;
        this.alias = alias;
        this.condition = condition;
        this.delayLevel = delayLevel;
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

    public SubscriberDelayLevel getDelayLevel() {
        return delayLevel;
    }
}
