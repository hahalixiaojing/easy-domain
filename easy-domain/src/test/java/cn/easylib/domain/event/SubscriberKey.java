package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.ISubscriberKey;

public enum SubscriberKey implements ISubscriberKey {

    SUB1("sub1", "订阅Sub1"),
    SUB2("sub2", "订阅Sub2"),
    SUB3("sub3", "订阅Sub3");


    private final String keyName;
    private final String description;


    SubscriberKey(String keyName, String description) {
        this.keyName = keyName;
        this.description = description;
    }

    @Override
    public String keyName() {
        return this.keyName;
    }

    @Override
    public String description() {
        return this.description;
    }
}
