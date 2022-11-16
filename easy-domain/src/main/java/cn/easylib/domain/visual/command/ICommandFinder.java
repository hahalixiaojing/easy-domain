package cn.easylib.domain.visual.command;

import cn.easylib.domain.application.ICommandService;
import cn.easylib.domain.base.EntityBase;

import java.util.List;

public interface ICommandFinder<T extends EntityBase<?>> {

    List<ICommandService> findList(Class<T> cls, String packageName);

}
