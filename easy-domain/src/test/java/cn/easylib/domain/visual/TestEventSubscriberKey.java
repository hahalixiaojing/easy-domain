package cn.easylib.domain.visual;

import cn.easylib.domain.application.subscriber.ISubscriberKey;

public enum TestEventSubscriberKey implements ISubscriberKey {
    SUB1("sub1", "sub1描述"),
    SUB2("sub2", "sub2描述"),
    SUB3("sub3", "sub3描述");

    TestEventSubscriberKey(String keyName, String description) {
        this.keyName = keyName;
        this.description = description;
    }

    private final String keyName;
    private final String description;

    @Override
    public String keyName() {
        return this.keyName;
    }

    @Override
    public String description() {
        return this.description;
    }
}
