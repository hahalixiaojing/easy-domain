package easy.domain.base;

import org.junit.Assert;
import org.junit.Test;

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
