package cn.easylib.domain.visual.entity;

public class EntityDescriptor {
    private final String name;
    private final String clsName;

    public EntityDescriptor(String name, String clsName) {

        this.name = name;
        this.clsName = clsName;
    }

    public String getName() {
        return name;
    }

    public String getClsName() {
        return clsName;
    }
}
