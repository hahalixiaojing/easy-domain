package cn.easylib.domain.visual.entity;

public class FieldInfo {

    private final String fieldName;
    private final String description;
    private final String type;
    private final Class<?> clsType;

    public FieldInfo(String fieldName, String description, String type, Class<?> clsType) {
        this.fieldName = fieldName;
        this.description = description;
        this.type = type;
        this.clsType = clsType;
    }


    public String getFieldName() {
        return fieldName;
    }

    public String getType() {
        return type;
    }

    public Class<?> getClsType() {
        return clsType;
    }

    public String getDescription() {
        return description;
    }
}
