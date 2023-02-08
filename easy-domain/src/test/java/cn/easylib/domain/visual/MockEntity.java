package cn.easylib.domain.visual;

import cn.easylib.domain.base.BrokenRuleMessage;
import cn.easylib.domain.base.EntityBase;

public class MockEntity extends EntityBase<Long> {


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAgeTest() {
        return ageTest;
    }

    public void setAgeTest(int ageTest) {
        this.ageTest = ageTest;
    }

    private String name;
    private Integer age;
    private int ageTest;

    private MockValueObject mockValueObject;

    public String showName() {
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public MockValueObject getMockValueObject() {
        return mockValueObject;
    }

    public void setMockValueObject(MockValueObject mockValueObject) {
        this.mockValueObject = mockValueObject;
    }
}

