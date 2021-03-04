package easy.domain.rule;

import easy.domain.base.BrokenRuleMessage;
import easy.domain.base.EntityBase;
import easy.domain.rules.EntityRule;
import easy.domain.rules.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * @author lixiaojing10
 * @date 2021/2/22 10:18 上午
 */
public class EntityAndEntityRuleTest {

    @Test
    public void basicRuleTest() {

        Data data = new Data();

        EntityRule<Data> dataEntityRule = new DataEntityRule();
        Assert.assertFalse(dataEntityRule.isSatisfy(data));

    }

    @Test
    public void entityTest() {
        Data data = new Data();

        Assert.assertFalse(data.validate());
        Assert.assertEquals(2, data.getBrokenRules().size());

        data.clearBrokenRules();
        data.setStatus(1);
        data.validate();
        Assert.assertEquals(3, data.getBrokenRules().size());

        data.clearBrokenRules();
        data.setPrice(10.9);
        data.setName("张三");
        data.setStatus(0);

        Assert.assertFalse(data.validate());
        Assert.assertEquals(1, data.getBrokenRules().size());
        Assert.assertEquals("张三这个名字已经使用", data.getBrokenRules().get(0).getDescription());


    }


    static class DataEntityRule extends EntityRule<Data> {
        public DataEntityRule() {
            //基本验证
            this.isBlank("name", DataBrokenRuleMessage.NAME_EMPTY_ERROR, "");
            //自定以验证
            this.addRule(model -> model.getPrice() > 0, DataBrokenRuleMessage.PRICE_ZERO_ERROR, "");
            //在特定条件下，参与验证
            this.addRule(Data::getBoolInfo, DataBrokenRuleMessage.BOOLEAN_INFO_ERROR, "", model -> model.getStatus() == 1);
            //带参数的方式验证
            this.addParamRule(model -> new Pair(false, new Object[]{model.getName()}), DataBrokenRuleMessage.NAME_USED_ERROR, "", model -> !model.getName().isEmpty());
        }
    }

    static class DataBrokenRuleMessage extends BrokenRuleMessage {

        public static final String NAME_EMPTY_ERROR = "NAME_EMPTY_ERROR";
        public static final String NAME_USED_ERROR = "NAME_USED_ERROR";
        public static final String PRICE_ZERO_ERROR = "PRICE_ZERO_ERROR";
        public static final String BOOLEAN_INFO_ERROR = "BOOLEAN_INFO_ERROR";

        @Override
        protected void populateMessage() {

            this.getMessages().put(NAME_EMPTY_ERROR, "名字不能为空");

            this.getMessages().put(NAME_USED_ERROR, "%s这个名字已经使用");
            this.getMessages().put(PRICE_ZERO_ERROR, "价格不能是0");
            this.getMessages().put(BOOLEAN_INFO_ERROR, "must be TRUE");

        }
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

        private String name = "";
        private Double price;
        private Boolean boolInfo;
        private Date greaterData;
        private int status;

        private DataExtension dataExtension;


        public Boolean getBoolInfo() {
            return boolInfo != null && boolInfo;
        }

        public void setBoolInfo(Boolean boolInfo) {
            this.boolInfo = boolInfo;
        }

        @Override
        public Boolean validate() {
            return new DataEntityRule().isSatisfy(this);
        }

        @Override
        protected BrokenRuleMessage getBrokenRuleMessages() {
            return new DataBrokenRuleMessage();
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
            return price == null ? 0.0 : price;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
