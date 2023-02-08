package cn.easylib.domain.visual.entity;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityFieldFinder implements IEntityFieldFinder {

    private final List<Pair<FieldGetter<?, ?>, String>> fieldGetterList = new ArrayList<>();

    protected AbstractEntityFieldFinder() {

        this.initFieldList();
    }

    protected abstract void initFieldList();

    protected <T, R> void addField(FieldGetter<T, R> fieldGetter, String description) {
        fieldGetterList.add(new MutablePair<>(fieldGetter, description));
    }

    @Override
    public List<Pair<FieldGetter<?, ?>, String>> fieldGetterList() {
        return fieldGetterList;
    }
}
