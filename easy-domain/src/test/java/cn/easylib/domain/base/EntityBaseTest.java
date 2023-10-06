package cn.easylib.domain.base;

import org.junit.Test;

public class EntityBaseTest {


    @Test
    public void setAndReturnOldTest() {
        TestData testData = new TestData();
        testData.setData2(1);
        Integer i = testData.updateData2Info(100);

        assert  i == 1;
        assert  100 ==  testData.getData2();
    }
}

class TestData extends EntityBase<Long> {
    private Boolean data;
    private Integer data2;


    public Integer updateData2Info(Integer integer) {

        return this.setAndReturnOld(this::setData2, this::getData2, integer);
    }


    public Boolean getData() {
        return data;
    }

    public void setData(Boolean data) {
        this.data = data;
    }

    public Integer getData2() {
        return data2;
    }

    public void setData2(Integer data2) {
        this.data2 = data2;
    }

    @Override
    public Boolean validate() {
        return null;
    }

    @Override
    protected BrokenRuleMessage getBrokenRuleMessages() {
        return null;
    }
}
