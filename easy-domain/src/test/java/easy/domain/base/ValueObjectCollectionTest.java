package easy.domain.base;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author lixiaojing10
 * @date 2021/9/2 10:05 下午
 */
public class ValueObjectCollectionTest {

    @Test
    public void initIsEmptyValueObjectCollections() {
        ValueObjectCollection<Long> collection = new ValueObjectCollection<>();

        collection.append(1L);
        collection.append(2L);

        int count = collection.getAllItems().size();
        Assert.assertEquals(2, count);

        int count2 = collection.getAppendedItems().size();
        Assert.assertEquals(2, count2);

        ArrayList<Long> longs = new ArrayList<>();
        longs.add(3L);
        longs.add(4L);
        collection.clearAndAppend(longs);

        int count3 = collection.getAllItems().size();
        Assert.assertEquals(2, count3);
        Assert.assertEquals(3L, collection.getAllItems().get(0).longValue());
        Assert.assertEquals(4L, collection.getAllItems().get(1).longValue());

        int count4 = collection.getAppendedItems().size();
        Assert.assertEquals(2, count4);
        Assert.assertEquals(3L, collection.getAppendedItems().get(0).longValue());
        Assert.assertEquals(4L, collection.getAppendedItems().get(1).longValue());

        ArrayList<Long> appendList = new ArrayList<>();
        appendList.add(7L);
        appendList.add(8L);
        collection.append(appendList);
        Assert.assertEquals(4, collection.getAppendedItems().size());
        Assert.assertEquals(8L, collection.getAppendedItems().get(collection.getAppendedItems().size() - 1).longValue());


    }

    @Test
    public void initNotEmptyValueObjectCollection() {

        ArrayList<Long> longs = new ArrayList<>();
        longs.add(3L);
        longs.add(4L);

        ValueObjectCollection<Long> collection = new ValueObjectCollection<>(longs);

        collection.append(1L);
        collection.append(2L);


        int count = collection.getAllItems().size();
        Assert.assertEquals(4, count);

        int count2 = collection.getAppendedItems().size();
        Assert.assertEquals(2, count2);


    }

    @Test
    public void testRemoveAll() {

        ArrayList<Long> longs = new ArrayList<>();
        longs.add(3L);
        longs.add(4L);
        ValueObjectCollection<Long> collection = new ValueObjectCollection<>(longs);

        collection.append(5L);

        collection.removeAll();

        collection.append(5L);


        Assert.assertEquals(2, collection.getRemovedItems().size());

        Assert.assertEquals(1, collection.getAppendedItems().size());

    }


    @Test
    public void testRemoveItems() {
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(3L);
        longs.add(4L);

        ValueObjectCollection<Long> collection = new ValueObjectCollection<>(longs);
        collection.removeItems(s ->
                s.equals(3L)
        );

        Assert.assertEquals(1, collection.getRemovedItems().size());
        Assert.assertEquals(3L, collection.getRemovedItems().get(0).longValue());
        Assert.assertEquals(1, collection.getAllItems().size());
        Assert.assertEquals(4L, collection.getAllItems().get(0).longValue());
    }
}