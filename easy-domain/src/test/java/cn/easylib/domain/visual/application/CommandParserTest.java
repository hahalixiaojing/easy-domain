package cn.easylib.domain.visual.application;


import cn.easylib.domain.application.IApplication;
import cn.easylib.domain.base.EntityBase;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandParserTest {

    @Test
    public void aaa(){}


    @Test
    public void commandTest() {

        ApplicationServiceParser commandParser = new ApplicationServiceParser(new MockCommandFinder());

        List<ApplicationDescriptor> parser = commandParser.parser(null, null);

        assert parser.size() == 2;

    }

    static class MockCommandFinder implements IApplicationServiceFinder {

        @Override
        public <T extends EntityBase<?>> List<IApplication> findList(Class<T> cls, String packageName) {

            return Stream.of(new MockCommandService()).collect(Collectors.toList());
        }
    }
}

interface Iddd{
    String key();
    String description();

}

enum Name implements Iddd{

    Color()
    ;

    @Override
    public String key() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }
}
