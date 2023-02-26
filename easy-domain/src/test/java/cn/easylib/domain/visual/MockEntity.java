package cn.easylib.domain.visual;

import cn.easylib.domain.base.BrokenRuleMessage;
import cn.easylib.domain.base.EntityAction;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.visual.entity.EntityActionVisual;
import cn.easylib.domain.visual.entity.EntityVisual;

@EntityVisual(description = "这个一个模拟测试类")
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

    public MockEntity() {
    }

    @EntityActionVisual(triggerEvents = MockEntityCreatedEvent.class)
    public MockEntity(String name, Integer age, int ageTest) {

        this.name = name;
        this.age = age;
        this.ageTest = ageTest;

        this.eventCollector.pushEvent(MockEntityCreatedEvent.buildEvent());
    }

    public String showName() {
        return name;
    }

    @EntityActionVisual(triggerEvents = {TestEvent.class})
    public void changeBasic(String name) {
        this.name = name;
        this.eventCollector.pushEvent(new TestEvent());
    }

    @EntityActionVisual(triggerEvents = {TestEvent.class})
    public void changeBasic2(String name,String name2) {
        this.name = name;
        this.eventCollector.pushEvent(new TestEvent());
    }

    @Override
    public Boolean validate() {
        return null;
    }

    @Override
    protected BrokenRuleMessage getBrokenRuleMessages() {
        return MockEntityBrokenRuleMessage.message;
    }

    @Override
    public EntityAction entityActions() {
        return MockEntityActions.action;
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

