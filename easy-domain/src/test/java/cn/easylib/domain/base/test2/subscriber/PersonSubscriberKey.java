package cn.easylib.domain.base.test2.subscriber;

import cn.easylib.domain.application.subscriber.AbstractSubscriberKey;

public class PersonSubscriberKey extends AbstractSubscriberKey {

    public static final String updateScore ="updateScore";
    public static final String updateGrade ="updateGrade";

    public static final PersonSubscriberKey INSTANCE = new PersonSubscriberKey();

    @Override
    protected void populateKeys() {
        this.getKeys().put(updateScore, buildKeySetting("updateScore"));
        this.getKeys().put(updateGrade, buildKeySetting("updateScore"));
    }
}
