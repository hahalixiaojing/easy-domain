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
                    parentSubscriberKey,
                    childSubscriberKey
            ));
        } else {

            HashSet<OrderData> orderDataHashSet = new HashSet<>();
            orderDataHashSet.add(new OrderData(parentSubscriberAlias,
                    childSubscriberAlias,
                    parentSubscriberKey, childSubscriberKey));
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
        if(orderData == null){
            return  Collections.emptyList();
        }
        return new ArrayList<>(orderData);
    }

    static enum RootSubscriberKey implements ISubscriberKey {

        ROOT("ROOT", "ROOT");

        RootSubscriberKey(String keyName, String description) {
            this.keyName = keyName;
            this.description = description;
        }

        private final String keyName;
        private final String description;


        @Override
        public String keyName() {
            return this.keyName;
        }

        @Override
        public String description() {
            return this.description;
        }
    }

    public static class OrderData {
        public OrderData(String parentSubscriberAlias, String childSubscriberAlias) {
            this(parentSubscriberAlias, childSubscriberAlias, null, null);

        }


        public OrderData(String currentSubscriberAlias,
                         String childSubscriberAlias,
                         ISubscriberKey currentSubscriberKey,
                         ISubscriberKey childSubscriberKey) {

            this.currentSubscriberAlias = StringUtils.isEmpty(currentSubscriberAlias) ? ROOT_NAME : currentSubscriberAlias;
            this.childSubscriberAlias = childSubscriberAlias;
            this.currentSubscriberKey = currentSubscriberKey == null ? RootSubscriberKey.ROOT : currentSubscriberKey;
            this.childSubscriberKey = childSubscriberKey;
        }

        public final String currentSubscriberAlias;
        public final String childSubscriberAlias;

        private final ISubscriberKey currentSubscriberKey;
        private final ISubscriberKey childSubscriberKey;

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

        public ISubscriberKey getCurrentSubscriberKey() {
            return currentSubscriberKey;
        }

        public ISubscriberKey getChildSubscriberKey() {
            return childSubscriberKey;
        }
    }
}


