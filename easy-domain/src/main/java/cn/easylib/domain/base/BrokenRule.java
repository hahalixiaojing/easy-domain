package cn.easylib.domain.base;

public class BrokenRule {

    private final String name;
    private final String description;
    private final String property;
    private final String alias;
    private final Object[] extraData;


    public BrokenRule(String name, String description) {
        this(name, description, "");
    }

    public BrokenRule(String name, String description, String property) {
        this(name, description, property, "", null);
    }

    public BrokenRule(String name, String description, String property, String alias, Object[] extraData) {
        this.name = name;
        this.description = description;
        this.property = property;
        this.alias = alias == null || alias.equals("") ? property : alias;
        this.extraData = extraData;
    }

    public String getAlias() {
        return alias;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProperty() {
        return property;
    }

    public Object[] getExtraData() {
        return this.extraData;
    }
}
