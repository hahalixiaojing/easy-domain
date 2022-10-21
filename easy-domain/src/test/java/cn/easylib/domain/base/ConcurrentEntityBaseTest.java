package cn.easylib.domain.base;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * 领域模型实例，多次调用getNewVersion()获取并发版本号的，只递增一次
 *
 * @author lixiaojing
 * @date 2021/3/18 6:46 下午
 */
public class ConcurrentEntityBaseTest {

    @Test
    public void newVersionTest() {
        TestDomainModel testDomainModel = new TestDomainModel();
        long newVersion = testDomainModel.getNewVersion();
        Assert.assertEquals(newVersion, testDomainModel.getNewVersion());
    }

    @Test
    public void dad(){
        String testStr = "";
        String sd = Optional.ofNullable(testStr).map(s -> s + "1").orElse("sd");
        Assert.assertEquals("sd",sd);
    }



    public static class TestDomainModel extends ConcurrentEntityBase<Long> {

        @Override
        public Boolean validate() {
            return null;
        }

        @Override
        protected BrokenRuleMessage getBrokenRuleMessages() {
            return null;
        }
    }
}
