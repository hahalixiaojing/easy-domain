package cn.easylib.domain.base;

public class BrokenRuleException extends RuntimeException {
    private static final long serialVersionUID = 708303351975681548L;

    private final String code;
    private final String entityInfo;
    private final transient Object[] extraData;


    public BrokenRuleException(String code, String message) {
        this(code, message, "", new Object[0]);
    }

    public BrokenRuleException(String code, String message, String entityInfo, Object[] extraData) {
        super(message);
        this.code = code;
        this.extraData = extraData;
        this.entityInfo = entityInfo;
    }

    public BrokenRuleException(String code, String message, Object[] extraData) {
        this(code, message, "", extraData);
    }

    public String getCode() {
        return code;
    }

    public Object[] getExtraData() {
        return extraData;
    }

    public String getEntityInfo() {
        return entityInfo;
    }
}
