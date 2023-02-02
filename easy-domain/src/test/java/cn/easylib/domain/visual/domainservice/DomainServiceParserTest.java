package cn.easylib.domain.visual.domainservice;

import cn.easylib.domain.base.BrokenRuleMessage;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.base.IDomainService;
import cn.easylib.domain.base.IDomainServiceDescriptor;
import cn.easylib.domain.visual.DomainModelVisualManager;
import cn.easylib.domain.visual.service.DomainServiceDescriptor;
import cn.easylib.domain.visual.service.DomainServiceParser;
import cn.easylib.domain.visual.service.IDomainServiceFinder;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DomainServiceParserTest {

    @Test
    public void domainServiceParse() {

        DomainServiceParser domainServiceParser = new DomainServiceParser();

        domainServiceParser.registerDomainService(MockEntity.class, new IDomainServiceFinder() {
            @Override
            public <T extends EntityBase<?>> List<Class<?>> findList(Class<T> cls) {
                return Stream.of(TestDomainService.class).collect(Collectors.toList());
            }
        });

        List<DomainServiceDescriptor> parse = domainServiceParser.parse(MockEntity.class);

        System.out.println(JSON.toJSONString(parse));


        DomainModelVisualManager domainModelVisualManager = new DomainModelVisualManager(null);
        domainModelVisualManager.registerDomainEntity(MockEntity.class);


    }
}

@IDomainServiceDescriptor(description = "测试domainService")
class TestDomainService implements IDomainService {


    public void add() {
    }


}


class MockEntity extends EntityBase<Long> {

    @Override
    public Boolean validate() {
        return null;
    }

    @Override
    protected BrokenRuleMessage getBrokenRuleMessages() {
        return null;
    }
}