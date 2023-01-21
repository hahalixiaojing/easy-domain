package cn.easylib.domain.visual.application;


import cn.easylib.domain.application.IApplication;
import cn.easylib.domain.base.BrokenRuleMessage;
import cn.easylib.domain.base.EntityBase;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

public class CommandParserTest {

    @Test
    public void aaa() {
    }


    @Test
    public void commandTest() {

        ApplicationServiceParser commandParser = new ApplicationServiceParser();
        commandParser.registerApplicationService(MockEntity.class, new MockCommandFinder());

        List<ApplicationDescriptor> parser = commandParser.parser(MockEntity.class);

        assert parser.size() == 2;

        out.println(JSON.toJSONString(parser));

    }

    static class MockCommandFinder implements IApplicationServiceFinder {

        @Override
        public <T extends EntityBase<?>> List<IApplication> findList(Class<T> cls) {

            return Stream.of(new MockCommandService()).collect(Collectors.toList());
        }
    }
}

interface Iddd {
    String key();

    String description();

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

enum Name implements Iddd {

    Color();

    @Override
    public String key() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }
}
