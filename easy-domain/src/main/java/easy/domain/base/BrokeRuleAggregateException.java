package easy.domain.base;

import java.util.List;

public class BrokeRuleAggregateException extends RuntimeException {

    private final List<BrokenRuleException> exceptions;

    public BrokeRuleAggregateException(String message, List<BrokenRuleException> exceptions) {
        super(message);
        this.exceptions = exceptions;
    }

    public BrokeRuleAggregateException(List<BrokenRuleException> exceptions) {
        this("", exceptions);
    }

    public List<BrokenRuleException> getExceptions() {
        return exceptions;
    }
}
