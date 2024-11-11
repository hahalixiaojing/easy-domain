package cn.easylib.domain.base.test2.event;

import cn.easylib.domain.base.test2.entity.Person;
import cn.easylib.domain.event.BaseDomainEvent;
import lombok.Data;

@Data
public class PersonUpdateEvent extends BaseDomainEvent {
    private Long id;
    private String name;


    public static PersonUpdateEvent build(Person person) {

        PersonUpdateEvent personUpdateEvent = new PersonUpdateEvent();
        personUpdateEvent.setId(person.getId());
        personUpdateEvent.setName(person.getName());
        return personUpdateEvent;

    }
}
