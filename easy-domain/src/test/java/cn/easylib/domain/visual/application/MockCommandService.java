package cn.easylib.domain.visual.application;

import cn.easylib.domain.application.ICommandService;
import cn.easylib.domain.application.ServiceDescriptor;

public class MockCommandService implements ICommandService {

    @ServiceDescriptor(name = "测试", description = "测试描述")
    public void update() {

    }

    @ServiceDescriptor(name = "测试1", description = "测试描述2")
    public void add() {

    }
}
