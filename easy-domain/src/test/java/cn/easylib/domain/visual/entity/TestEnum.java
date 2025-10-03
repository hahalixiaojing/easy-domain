package cn.easylib.domain.visual.entity;

import cn.easylib.domain.base.IEnumValue;

public enum TestEnum implements IEnumValue<Integer, TestEnum> {
    Test_A(1, "测试枚举1"),
    Test_B(2, "测试枚举2");

    private final Integer value;
    private final String name;

    TestEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public TestEnum parse(Integer integer) {
        return null;
    }
}
