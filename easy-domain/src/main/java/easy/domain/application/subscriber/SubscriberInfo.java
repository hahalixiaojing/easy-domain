package easy.domain.application.subscriber;

/**
 * 事件订单对象
 *
 * @author lixiaojing
 * @date 2019/3/29 2:46 PM
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

    /**
     * 构造函数
     *
     * @param subscriber 事件订阅程序
     * @param alias      别名
     * @param condition  执行条件
     */
    public SubscriberInfo(ISubscriber subscriber, String alias, IExecuteCondition condition) {
        this.subscriber = subscriber;
        this.alias = alias;
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
}
