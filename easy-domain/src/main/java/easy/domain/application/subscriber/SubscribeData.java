package easy.domain.application.subscriber;

/**
 * @author lixiaojing10
 * @date 2021/3/16 7:48 下午
 */
public class SubscribeData {

    private String name;
    private String eventData;
    private String realEventName;



    public SubscribeData(String name, String eventData,String realEventName) {
        this.name = name;
        this.eventData = eventData;
        this.realEventName = realEventName;
    }
    public SubscribeData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }
    public String getRealEventName() {
        return realEventName;
    }
    public void setRealEventName(String realEventName) {
        this.realEventName = realEventName;
    }
}
