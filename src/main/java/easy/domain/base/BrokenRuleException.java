package easy.domain.base;

public class BrokenRuleException extends RuntimeException {
    private static final long serialVersionUID = 708303351975681548L;

    private final String code;
    private final transient Object[] extraData;


    public BrokenRuleException(String code, String message) {
        this(code, message, new Object[0]);
    }

    public BrokenRuleException(String code, String message, Object[] extraData) {
        super(message);
        this.code = code;
        this.extraData = extraData;
    }

    public String getCode() {
        return code;
    }

    public Object[] getExtraData() {
        return extraData;
    }
}
