package cn.easylib.domain.visual.entity;

import java.util.List;


public class EntityDescriptor {
    private final String description;

    private final Boolean isRoot;
    private final String clsName;

    private final List<FieldInfo> fieldInfoList;

    private final List<EntityActionDescriptor> entityActionDescriptorList;

    public EntityDescriptor(String clsName, String description, List<FieldInfo> fieldInfoList,
                            List<EntityActionDescriptor> entityActionDescriptorList, Boolean isRoot) {

        this.clsName = clsName;
        this.description = description;
        this.fieldInfoList = fieldInfoList;
        this.entityActionDescriptorList = entityActionDescriptorList;
        this.isRoot = isRoot;
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

    public List<EntityActionDescriptor> getEntityActionDescriptorList() {
        return entityActionDescriptorList;
    }
}
