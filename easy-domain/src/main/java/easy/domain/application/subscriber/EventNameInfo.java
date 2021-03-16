package easy.domain.application.subscriber;

/**
 * @author lixiaojing10
 * @date 2021/3/16 7:47 下午
 */
public class EventNameInfo {

    public EventNameInfo(String eventName,String shareTopicName){
        this.eventName = eventName;
        this.shareTopicName= shareTopicName;
    }

    public String eventName;
    public String shareTopicName;
}
