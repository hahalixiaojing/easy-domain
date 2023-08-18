package cn.easylib.domain.application.subscriber;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.toList;

/**
 * @author lixiaojing10
 */
public class OrderedPerformManager implements IOrderedPerformManager {

    private static final String ROOT_NAME = "_root_";

    //key= eventName
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
    public List<String> selectNextSubscribers(String eventName, String subscriberAlias) {

        return Optional.ofNullable(this.maps.get(eventName))
                .orElse(new HashSet<>())
                .stream().filter(s -> s.currentSubscriberAlias.equals(subscriberAlias))
                .map(s -> s.childSubscriberAlias).collect(toList());
    }

    @Override
    public List<String> selectRootSubscribers(String eventName) {
        return this.selectNextSubscribers(eventName, ROOT_NAME);
    }

    @Override
    public List<OrderData> selectEventSubscriberInfo(String eventName) {
        HashSet<OrderData> orderData = this.maps.get(eventName);
        if (orderData == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(orderData);
    }
    public static class OrderData {


        public OrderData(String currentSubscriberAlias,
                         String childSubscriberAlias) {

            this.currentSubscriberAlias = StringUtils.isEmpty(currentSubscriberAlias) ? ROOT_NAME : currentSubscriberAlias;
            this.childSubscriberAlias = childSubscriberAlias;
        }

        public final String currentSubscriberAlias;
        public final String childSubscriberAlias;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderData orderData = (OrderData) o;
            return currentSubscriberAlias.equals(orderData.currentSubscriberAlias)
                    && childSubscriberAlias.equals(orderData.childSubscriberAlias);
        }

        @Override
        public int hashCode() {
            return Objects.hash(currentSubscriberAlias, childSubscriberAlias);
        }
    }
}


