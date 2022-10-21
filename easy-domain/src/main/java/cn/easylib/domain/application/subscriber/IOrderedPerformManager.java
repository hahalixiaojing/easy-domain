package cn.easylib.domain.application.subscriber;

import java.util.List;

/**
 * 顺序执行管理接口
 *
 * @author lixiaojing10
 */
public interface IOrderedPerformManager {

    void registerSubscriber(String eventName, String currentSubscriberAlias, String parentSubscriberAlias);

    List<String> selectNextSubscribers(String eventName,String subscriberAlias);
    List<String> selectRootSubscribers(String eventName);
}
