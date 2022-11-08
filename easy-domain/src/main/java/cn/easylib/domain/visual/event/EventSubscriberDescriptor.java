package cn.easylib.domain.visual.event;

public class EventSubscriberDescriptor {
    private final String subscriberKey;
    private final String subscriberDescription;
    private final String preDependencySubscriber;

    public EventSubscriberDescriptor(String subscriberKey,
                                     String subscriberDescription,
                                     String preDependencySubscriber) {
        this.subscriberKey = subscriberKey;
        this.subscriberDescription = subscriberDescription;
        this.preDependencySubscriber = preDependencySubscriber;
    }

    public String getSubscriberKey() {
        return subscriberKey;
    }

    public String getSubscriberDescription() {
        return subscriberDescription;
    }

    public String getPreDependencySubscriber() {
        return preDependencySubscriber;
    }
}
