package easy.domain.base;

import easy.domain.event.BaseDomainEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author lixiaojing10
 * @date 2022/3/12 4:18 下午
 */
public class DomainEventCollector {

    private final List<BaseDomainEvent> domainEventList = new ArrayList<>();

    private final List<Supplier<BaseDomainEvent>> delayGenerateEventList = new ArrayList<>();

    public void pushEvent(BaseDomainEvent baseDomainEvent) {
        this.domainEventList.add(baseDomainEvent);
    }

    public void pushDelayGenerateEvent(Supplier<BaseDomainEvent> supplier) {
        this.delayGenerateEventList.add(supplier);
    }

    public List<BaseDomainEvent> getEventList() {

        List<BaseDomainEvent> delayDomainEventList = this.delayGenerateEventList.stream().map(Supplier::get).collect(Collectors.toList());

        List<BaseDomainEvent> returnedList = new ArrayList<>(this.domainEventList);
        returnedList.addAll(delayDomainEventList);

        return returnedList;
    }

    public <T> void removeEvent(Class<T> cls) {
        domainEventList.removeIf(s -> s.getClass().equals(cls));
    }
}
