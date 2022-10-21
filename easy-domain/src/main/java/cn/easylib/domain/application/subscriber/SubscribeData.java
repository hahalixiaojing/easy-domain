package cn.easylib.domain.application.subscriber;

/**
 * @author lixiaojing
 */
public class SubscribeData {

    private String name;
    private String eventData;
    private String realEventName;
    private Boolean onlyThis;



    public SubscribeData(String name, String eventData,String realEventName,Boolean onlyThis) {
        this.name = name;
        this.eventData = eventData;
        this.realEventName = realEventName;
        this.onlyThis = onlyThis;
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
    public Boolean getOnlyThis() {
        return onlyThis;
    }
    public void setOnlyThis(Boolean onlyThis) {
        this.onlyThis = onlyThis;
    }
}
