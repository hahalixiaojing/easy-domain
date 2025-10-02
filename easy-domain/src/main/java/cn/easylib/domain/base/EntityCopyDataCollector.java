package cn.easylib.domain.base;

import java.util.HashMap;
import java.util.Map;

public class EntityCopyDataCollector {

    private Object copyData;
    private final Map<String,Object> extraParam = new HashMap<>();

    public <T> EntityCopyDataCollector initCopyData(ICopyData<T> copyData) {
        this.copyData = copyData.copy();
        return this;
    }

    public EntityCopyDataCollector putExtraParam(String key, Object value) {
        this.extraParam.put(key, value);
        return this;
    }

    public <C> C getCopyData(Class<C> cls) {
        return cls.cast(copyData);
    }
    public Map<String, Object> getExtraParam() {
        return extraParam;
    }
}
