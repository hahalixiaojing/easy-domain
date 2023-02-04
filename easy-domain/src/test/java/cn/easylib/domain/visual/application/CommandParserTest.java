package cn.easylib.domain.visual.application;


import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.visual.MockEntity;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

public class CommandParserTest {


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
        public <T extends EntityBase<?>> List<Class<?>> findList(Class<T> cls) {

            return Stream.of(MockCommandService.class).collect(Collectors.toList());
        }
    }
}

