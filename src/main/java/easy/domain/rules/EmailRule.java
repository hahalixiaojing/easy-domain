package easy.domain.rules;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailRule<T> extends PropertyRule<T, String> {

    public static final String EMAIL_REG = "^\\s*([A-Za-z0-9_-]+(\\.\\w+)*@(\\w+\\.)+\\w{2,5})\\s*$";
    private final Pattern pattern;

    public EmailRule(String property) {
        super(property);
        this.pattern = Pattern.compile(EMAIL_REG);

    }

    @Override
    public boolean isSatisfy(T model) {
        String value = this.getObjectAttrValue(model);

        if (StringUtils.isEmpty(value)) {
            return true;
        }
        Matcher m = this.pattern.matcher(value);
        return m.matches();
    }
}
