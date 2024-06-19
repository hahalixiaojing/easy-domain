package cn.easylib.domain.rule;

import cn.easylib.domain.base.IBoxValueObject;

public class DataShadowEntityObject implements IBoxValueObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
