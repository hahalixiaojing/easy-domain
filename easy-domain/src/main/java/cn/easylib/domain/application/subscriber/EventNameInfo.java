package cn.easylib.domain.application.subscriber;

/**
 * @author lixiaojing

 */
public class EventNameInfo {

    public EventNameInfo(String eventName,String shareTopicName){
        this.eventName = eventName;
        this.shareTopicName= shareTopicName;
    }

    public String eventName;
    public String shareTopicName;

    public boolean useEventName(){
        return this.shareTopicName == null || this.shareTopicName.equals("");
    }
}
