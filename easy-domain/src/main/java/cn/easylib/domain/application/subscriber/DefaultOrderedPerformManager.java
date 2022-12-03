package cn.easylib.domain.application.subscriber;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.toList;

/**
 * @author lixiaojing10
 */
public class DefaultOrderedPerformManager implements IOrderedPerformManager {

    private static final String ROOT_NAME = "_root_";

    private final ConcurrentMap<String, HashSet<OrderData>> maps = new ConcurrentHashMap<>();

    @Override
    public void registerSubscriber(String eventName, String childSubscriberAlias, String parentSubscriberAlias) {

        if (this.maps.containsKey(eventName)) {
            this.maps.get(eventName).add(new OrderData(parentSubscriberAlias, childSubscriberAlias));
        } else {
            HashSet<OrderData> orderDataHashSet = new HashSet<>();
            orderDataHashSet.add(new OrderData(parentSubscriberAlias, childSubscriberAlias));
            this.maps.put(eventName, orderDataHashSet);
        }
    }

    @Override
    public void registerSubscriber(String eventName,
                                   ISubscriberKey childSubscriberKey,
                                   ISubscriberKey parentSubscriberKey) {

        String parentSubscriberAlias = Optional.ofNullable(parentSubscriberKey)
                .map(ISubscriberKey::keyName)
                .orElse(null);
        String childSubscriberAlias = childSubscriberKey.keyName();

        if (this.maps.containsKey(eventName)) {

            this.maps.get(eventName).add(new OrderData(
                    parentSubscriberAlias,
                    childSubscriberAlias,
                    childSubscriberKey,
                    parentSubscriberKey));
        } else {

            HashSet<OrderData> orderDataHashSet = new HashSet<>();
            orderDataHashSet.add(new OrderData(parentSubscriberAlias, childSubscriberAlias));
            this.maps.put(eventName, orderDataHashSet);
        }

    }


    @Override
    public List<String> selectNextSubscribers(String eventName, String subscriberAlias) {

        return Optional.ofNullable(this.maps.get(eventName))
                .orElse(new HashSet<>())
                .stream().filter(s -> s.parentSubscriberAlias.equals(subscriberAlias))
                .map(s -> s.childSubscriberAlias).collect(toList());
    }

    @Override
    public List<String> selectRootSubscribers(String eventName) {
        return this.selectNextSubscribers(eventName, ROOT_NAME);
    }

    static class OrderData {
        public OrderData(String parentSubscriberAlias, String childSubscriberAlias) {
            this(parentSubscriberAlias, childSubscriberAlias, null, null);

        }


        public OrderData(String parentSubscriberAlias,
                         String childSubscriberAlias,
                         ISubscriberKey currentSubscriberKey,
                         ISubscriberKey childSubscriberKey) {
            this.parentSubscriberAlias = StringUtils.isEmpty(parentSubscriberAlias) ? ROOT_NAME : parentSubscriberAlias;
            this.childSubscriberAlias = childSubscriberAlias;
            this.currentSubscriberKey = currentSubscriberKey;
            this.childSubscriberKey = childSubscriberKey;
        }

        public final String parentSubscriberAlias;
        public final String childSubscriberAlias;

        private final ISubscriberKey currentSubscriberKey;
        private final ISubscriberKey childSubscriberKey;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderData orderData = (OrderData) o;
            return parentSubscriberAlias.equals(orderData.parentSubscriberAlias)
                    && childSubscriberAlias.equals(orderData.childSubscriberAlias);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parentSubscriberAlias, childSubscriberAlias);
        }

        public ISubscriberKey getCurrentSubscriberKey() {
            return currentSubscriberKey;
        }

        public ISubscriberKey getChildSubscriberKey() {
            return childSubscriberKey;
        }
    }
}


