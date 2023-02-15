package cn.easylib.domain.visual.entity;

import java.util.List;



public class EntityDescriptor {
    private final String description;

    private final Boolean isRoot;
    private final String clsName;

    private final List<FieldInfo> fieldInfoList;

    public EntityDescriptor(String clsName, String description,List<FieldInfo> fieldInfoList,Boolean isRoot) {

        this.clsName = clsName;
        this.description = description;
        this.fieldInfoList =fieldInfoList;
        this.isRoot =isRoot;
    }

    public String getClsName() {
        return clsName;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getRoot() {
        return isRoot;
    }

    public List<FieldInfo> getFieldInfoList() {
        return fieldInfoList;
    }
}
