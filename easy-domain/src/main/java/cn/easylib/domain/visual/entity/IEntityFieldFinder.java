package cn.easylib.domain.visual.entity;


import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IEntityFieldFinder {
    List<Pair<FieldGetter<?,?>,String>> fieldGetterList();


}
