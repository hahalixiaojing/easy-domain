package easy.domain.rule;

import easy.domain.base.BrokenRuleMessage;
import easy.domain.base.EntityBase;
import easy.domain.rules.*;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * 业务规则单元测试
 *
 * @author lixiaojing
 * @date 2021/2/19 10:22 上午
 */
public class RuleTest {

    /**
     * Bool业务规则验证
     */
    @Test
    public void booleanRuleTest() {

        Data data = new Data();
        data.setBoolInfo(true);

        BooleanRule<Data> boolInfo1 = new BooleanRule<>("boolInfo", true);
        Assert.assertTrue(boolInfo1.isSatisfy(data));

        data.setBoolInfo(false);

        BooleanRule<Data> boolInfo2 = new BooleanRule<>("boolInfo", true);
        Assert.assertFalse(boolInfo2.isSatisfy(data));

    }

    /**
     * 时间业务规则验证
     */
    @Test
    public void dateShouldGreaterThanRuleTest() {

        LocalDateTime localDateTime = LocalDateTime.now().plusHours(-1);
        Instant instant = localDateTime.toInstant(OffsetDateTime.now().getOffset());

        DateShouldGreaterThanRule<Data> greaterData = new DateShouldGreaterThanRule<>("greaterData", Date.from(instant));

        Data data = new Data();
        data.setGreaterData(new Date());

        Assert.assertTrue(greaterData.isSatisfy(data));

        data.setGreaterData(Date.from(LocalDateTime.now().plusHours(-2).toInstant(OffsetDateTime.now().getOffset())));
        Assert.assertFalse(greaterData.isSatisfy(data));


    }

    /**
     * 时间业务规则验证
     */
    @Test
    public void dateShouldLessThanRuleTest() {

        LocalDateTime localDateTime = LocalDateTime.now().plusHours(-1);
        Instant instant = localDateTime.toInstant(OffsetDateTime.now().getOffset());

        DateShouldLessThanRule<Data> dataDateShouldLessThanRule = new DateShouldLessThanRule<>("greaterData", Date.from(instant));

        Data data = new Data();
        data.setGreaterData(Date.from(LocalDateTime.now().plusHours(-2).toInstant(OffsetDateTime.now().getOffset())));
        Assert.assertTrue(dataDateShouldLessThanRule.isSatisfy(data));

        data.setGreaterData(new Date());

        Assert.assertFalse(dataDateShouldLessThanRule.isSatisfy(data));
    }

    /**
     * 字符串空业务规则验证
     */
    @Test
    public void isBlankRuleTest() {

        IsBlankRule<Data> isBlankRule = new IsBlankRule<>("name");
        Data data = new Data();
        Assert.assertFalse(isBlankRule.isSatisfy(data));

        data.setName("  \r\n");
        Assert.assertFalse(isBlankRule.isSatisfy(data));

        data.setName("ok");
        Assert.assertTrue(isBlankRule.isSatisfy(data));
    }

    /**
     * 字符串长度业务规则验证
     */
    @Test
    public void maxLengthRuleTest() {
        MaxLengthRule<Data> name = new MaxLengthRule<>("name", 3);

        Data data = new Data();
        data.setName("123");
        Assert.assertTrue(name.isSatisfy(data));
        data.setName("");
        Assert.assertTrue(name.isSatisfy(data));
        data.setName("1234");
        Assert.assertFalse(name.isSatisfy(data));
    }

    /**
     * 数字相等业务规则验证
     */
    @Test
    public void numberEqualRuleTest() {

        Data data = new Data();
        data.setPrice(20.9);

        NumberEqualRule<Data, Double> price = new NumberEqualRule<>("price", 20.9);
        Assert.assertTrue(price.isSatisfy(data));

        data.setPrice(20.99);
        Assert.assertFalse(price.isSatisfy(data));
    }

    /**
     * 复合字段业务规则验证
     */
    @Test
    public void compositeFieldRuleTest() {

        Data data = new Data();
        data.setDataExtension(new DataExtension());

        NumberShouldGreaterThanRule<Data, Long> rule = new NumberShouldGreaterThanRule<>("dataExtension.extId", 0L);

        Assert.assertFalse(rule.isSatisfy(data));

        data.setDataExtension(new DataExtension(1));

        Assert.assertTrue(rule.isSatisfy(data));


    }

    static class DataExtension {
        private long extId;

        public DataExtension(long extId) {
            this.setExtId(extId);
        }

        public DataExtension() {
        }

        public long getExtId() {
            return extId;
        }

        public void setExtId(long extId) {
            this.extId = extId;
        }
    }


    static class Data extends EntityBase<Long> {

        private String name;
        private Double price;
        private Boolean boolInfo;
        private Date greaterData;
        private DataExtension dataExtension;

        public Boolean getBoolInfo() {
            return boolInfo;
        }

        public void setBoolInfo(Boolean boolInfo) {
            this.boolInfo = boolInfo;
        }

        @Override
        public Boolean validate() {
            return null;
        }

        @Override
        protected BrokenRuleMessage getBrokenRuleMessages() {
            return null;
        }

        public Date getGreaterData() {
            return greaterData;
        }

        public void setGreaterData(Date greaterData) {
            this.greaterData = greaterData;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public DataExtension getDataExtension() {
            return dataExtension;
        }

        public void setDataExtension(DataExtension dataExtension) {
            this.dataExtension = dataExtension;
        }
    }
}
