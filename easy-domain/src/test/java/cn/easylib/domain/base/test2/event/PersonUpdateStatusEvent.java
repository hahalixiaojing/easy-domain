package cn.easylib.domain.base.test2.event;

import cn.easylib.domain.base.test2.entity.Person;
import cn.easylib.domain.event.BaseDomainEvent;
import lombok.Data;

@Data
public class PersonUpdateStatusEvent extends BaseDomainEvent {

    private Long id;
    private String name;

    public static PersonUpdateStatusEvent build(Person person) {

        PersonUpdateStatusEvent personUpdateStatusEvent = new PersonUpdateStatusEvent();
        personUpdateStatusEvent.setId(person.getId());
        personUpdateStatusEvent.setName(person.getName());
        return personUpdateStatusEvent;

    }
}
