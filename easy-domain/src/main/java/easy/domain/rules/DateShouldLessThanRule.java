package easy.domain.rules;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class DateShouldLessThanRule<T> extends PropertyRule<T, Date> {

    private final Date date;

    public DateShouldLessThanRule(String property, Date date) {
        super(property);
        this.date = DateUtils.truncate(date, Calendar.SECOND);

    }
    @Override
    public boolean isSatisfy(T model) {
        Date d = this.getObjectAttrValue(model);
        d = DateUtils.truncate(d, Calendar.SECOND);

        return d.compareTo(this.date) < 0;
    }
}
