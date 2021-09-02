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

        int count4 = collection.getAppendedItems().size();
        Assert.assertEquals(2, count4);
        Assert.assertEquals(3L, collection.getAppendedItems().get(0).longValue());
        Assert.assertEquals(4L, collection.getAppendedItems().get(1).longValue());

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

}