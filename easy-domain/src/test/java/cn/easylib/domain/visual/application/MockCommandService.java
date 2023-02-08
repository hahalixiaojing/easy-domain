package cn.easylib.domain.visual.application;

import cn.easylib.domain.application.IApplication;

public class MockCommandService implements IApplication {

    @CommandServiceDescriptor(name = "测试", description = "测试描述")
    public void update() {

    }

    @CommandServiceDescriptor(name = "测试1", description = "测试描述2")
    public void add() {

    }

    @ReadServiceDescriptor(name = "测试读",description = "测试读")
    public Object getById(){
        return "";
    }
}
