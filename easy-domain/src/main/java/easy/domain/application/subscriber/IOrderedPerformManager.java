package easy.domain.application.subscriber;

import java.util.List;

/**
 * 顺序执行管理接口
 *
 * @author lixiaojing10
 * @date 2021/12/23 9:24 上午
 */
public interface IOrderedPerformManager {

    void registerSubscriber(String eventName, String currentSubscriberAlias, String parentSubscriberAlias);

    List<String> selectNextSubscribers(String eventName,String subscriberAlias);
    List<String> selectRootSubscribers(String eventName);
}
