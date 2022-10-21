package cn.easylib.domain.application.subscriber;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author lixiaojing10
 * @date 2021/12/23 3:44 下午
 */
public class DefaultOrderedPerformManagerTest {

    /**
     * 1. 直接循环依赖检查
     * 2. 一个节点同时依赖多个前置节点
     * 3. 间接循环依赖检查
     */

    @Test
    public void registerSubscriberTest() {
        DefaultOrderedPerformManager defaultOrderedPerformManager = new DefaultOrderedPerformManager();
        defaultOrderedPerformManager.registerSubscriber("evt", "a", null);
        defaultOrderedPerformManager.registerSubscriber("evt", "b", null);
        defaultOrderedPerformManager.registerSubscriber("evt", "c", null);
        defaultOrderedPerformManager.registerSubscriber("evt", "d", "a");
        defaultOrderedPerformManager.registerSubscriber("evt", "e", "a");


        List<String> evt = defaultOrderedPerformManager.selectRootSubscribers("evt");
        Assert.assertEquals(3, evt.size());

        List<String> strings = defaultOrderedPerformManager.selectNextSubscribers("evt", "a");

        Assert.assertEquals(2, strings.size());
        Assert.assertTrue(strings.contains("d"));
        Assert.assertTrue(strings.contains("e"));
    }
}