package cn.easylib.domain.visual.application;

import cn.easylib.domain.application.IApplication;
import cn.easylib.domain.base.EntityBase;

import java.util.List;

public interface IApplicationServiceFinder {

   <T extends EntityBase<?>> List<IApplication> findList(Class<T> cls, String packageName);

}
