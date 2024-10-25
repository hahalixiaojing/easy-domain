package cn.easylib.domain.visual.entity;

import cn.easylib.domain.base.BrokenRuleMessage;

public class EntityTest2BrokenRuleMessage extends BrokenRuleMessage {

    public static final String testError = "testError";

    @Override
    protected void populateMessage() {

        this.getMessages().put(testError, "testError");

    }
}
