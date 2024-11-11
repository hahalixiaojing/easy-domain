package cn.easylib.domain.base.test2.entity;

import cn.easylib.domain.base.*;
import cn.easylib.domain.base.test2.action.PersonAction;
import cn.easylib.domain.base.test2.boxvalueobject.PersonCopyData;
import cn.easylib.domain.base.test2.boxvalueobject.PersonInitData;
import cn.easylib.domain.base.test2.boxvalueobject.PersonUpdateData;
import cn.easylib.domain.base.test2.entity.enums.Status;
import cn.easylib.domain.base.test2.event.PersonInitEvent;
import cn.easylib.domain.base.test2.event.PersonUpdateEvent;
import cn.easylib.domain.base.test2.event.PersonUpdateStatusEvent;
import cn.easylib.domain.base.test2.rule.PersonBrokeRuleMessage;
import cn.easylib.domain.rules.EntityRule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter(AccessLevel.PROTECTED)
public class Person extends ConcurrentEntityBase<Long> implements
        ICustomValidator<Person>,
        ICopyData<PersonCopyData>,
        IEntityAction {

    private String name;
    private String age;
    private String email;
    private String phone;
    private Status status;
    private Date createdTime;
    private Date updatedTime;


    public Person(PersonInitData personInitData) {
        this.setId(personInitData.getId());
        this.setNewEntity(true);
        PersonSetter.init(this, personInitData);
        this.actionCollector.put(PersonAction.NEW);
        this.eventCollector.pushEvent(PersonInitEvent.build(this));

    }

    /**
     * 更新基础信息
     */
    public void update(PersonUpdateData personUpdateData) {
        this.copyDataCollector.initCopyData(this);
        PersonSetter.updateSet(this, personUpdateData);
        this.actionCollector.put(PersonAction.UPDATE_ACTION);
        this.eventCollector.pushEvent(PersonUpdateEvent.build(this));

    }

    /**
     * 更新状态
     */
    public void updateStatus(Status status) {
        this.copyDataCollector.initCopyData(this);
        this.setStatus(status);
        this.setUpdatedTime(new Date());
        this.allActions().put(PersonAction.UPDATE_STATUS_ACTION);
        this.eventCollector.pushEvent(PersonUpdateStatusEvent.build(this));


    }

    @Override
    protected BrokenRuleMessage getBrokenRuleMessages() {
        return PersonBrokeRuleMessage.INSTANCE;
    }

    @Override
    public Boolean validate(EntityRule<Person> rule) {
        return rule.isSatisfy(this);
    }

    @Override
    public EntityAction entityActions() {
        return PersonAction.INSTANCE;
    }

    @Override
    public PersonCopyData copy() {
        PersonCopyData personCopyData = new PersonCopyData();
        personCopyData.setId(this.getId());
        personCopyData.setName(this.getName());
        personCopyData.setAge(this.getAge());
        personCopyData.setEmail(this.getEmail());
        personCopyData.setPhone(this.getPhone());
        personCopyData.setStatus(this.getStatus());
        return personCopyData;
    }
}
