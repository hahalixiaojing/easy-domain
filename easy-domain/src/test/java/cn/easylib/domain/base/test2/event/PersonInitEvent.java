package cn.easylib.domain.base.test2.event;

import cn.easylib.domain.base.test2.entity.Person;
import cn.easylib.domain.event.BaseDomainEvent;
import lombok.Data;

@Data
public class PersonInitEvent extends BaseDomainEvent {
    private Long id;
    private String name;

    public static PersonInitEvent build(Person person) {

        PersonInitEvent personInitEvent = new PersonInitEvent();
        personInitEvent.setId(person.getId());
        personInitEvent.setName(person.getName());
        return personInitEvent;

    }
}
