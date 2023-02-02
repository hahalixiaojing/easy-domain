package cn.easylib.domain.visual;

import cn.easylib.domain.base.BrokenRuleMessage;
import cn.easylib.domain.base.EntityBase;

public class MockEntity extends EntityBase<Long> {


    private String name;

    public String showName(){
        return name;
    }

    @Override
    public Boolean validate() {
        return null;
    }

    @Override
    protected BrokenRuleMessage getBrokenRuleMessages() {
        return null;
    }
}

