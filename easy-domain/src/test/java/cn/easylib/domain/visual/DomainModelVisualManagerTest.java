package cn.easylib.domain.visual;

import cn.easylib.domain.application.IApplication;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.visual.application.IApplicationServiceFinder;
import cn.easylib.domain.visual.application.MockCommandService;
import cn.easylib.domain.visual.event.IEventFinder;
import cn.easylib.domain.visual.rule.IRuleFinder;
import cn.easylib.domain.visual.service.IDomainServiceFinder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DomainModelVisualManagerTest {

    @Test
    public void build(){

        DomainModelVisualManager domainModelVisualManager =
                new DomainModelVisualManager(MockDomainEventManager.mockIDomainEventManager());

        domainModelVisualManager.registerDomainEntity(MockEntity.class);
        domainModelVisualManager.registerApplicationService(MockEntity.class,new MockCommandFinder());
        domainModelVisualManager.registerDomainService(MockEntity.class,new MockIDomainServiceFinder());
        domainModelVisualManager.registerDomainEvent(MockEntity.class,new MockIEventFinder());
        domainModelVisualManager.registerDomainRule(MockEntity.class,new MockIRuleFinder());

        DomainModelVisualInfo build = domainModelVisualManager.build(MockEntity.class);

        System.out.println(JSON.toJSONString(build, SerializerFeature.PrettyFormat));

    }

    static class MockIRuleFinder implements IRuleFinder{

        @Override
        public <T extends EntityBase<?>> RuleFinderObject findEntityRuleList(Class<T> cls) {
            ArrayList<Class<?>> classes = new ArrayList<>();
            classes.add(MockEntityRule.class);
            return new RuleFinderObject(classes, MockEntityBrokenRuleMessage.message);
        }
    }

    static class MockIEventFinder implements IEventFinder{

        @Override
        public <T extends EntityBase<?>> List<Class<?>> findersList(Class<T> cls) {
            return Stream.of(TestEvent.class).collect(Collectors.toList());
        }
    }

    static class MockCommandFinder implements IApplicationServiceFinder {

        @Override
        public <T extends EntityBase<?>> List<IApplication> findList(Class<T> cls) {

            return Stream.of(new MockCommandService()).collect(Collectors.toList());
        }
    }

    static class MockIDomainServiceFinder implements IDomainServiceFinder{

        @Override
        public <T extends EntityBase<?>> List<Class<?>> findList(Class<T> cls) {
            return Stream.of(TestDomainService.class).collect(Collectors.toList());
        }
    }

}