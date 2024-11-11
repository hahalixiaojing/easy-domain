package cn.easylib.domain.base.test2.rule;

import cn.easylib.domain.base.test2.boxvalueobject.PersonCopyData;
import cn.easylib.domain.base.test2.entity.Person;
import cn.easylib.domain.base.test2.entity.enums.Status;
import cn.easylib.domain.base.test2.rule.validator.PersonGradeValidator;
import cn.easylib.domain.base.test2.rule.validator.PersonScoreValidator;
import cn.easylib.domain.rules.EntityRule;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static cn.easylib.domain.base.test2.rule.PersonBrokeRuleMessage.*;

public class PersonEntityRule extends EntityRule<Person> {

    public PersonEntityRule(PersonScoreValidator personScoreValidator,
                            PersonGradeValidator personGradeValidator
    ) {

        super(false);

        this.addRule(s -> StringUtils.isNoneEmpty(s.getName()), NAME_ERROR);
        this.addRule(s -> StringUtils.isNoneEmpty(s.getAge()), AGE_ERROR);
        this.addRule(s -> StringUtils.isNoneEmpty(s.getEmail()), EMAIL_ERROR);
        this.addRule(s -> StringUtils.isNoneEmpty(s.getPhone()), PHONE_ERROR);
        this.addRule(s -> !(s.getStatus() == Status.ILLEGAL), STATUS_ERROR, s -> {

            PersonCopyData personCopyData = s.obtainCopyData(PersonCopyData.class);
            return personCopyData != null && !Objects.equals(personCopyData.getStatus(), s.getStatus());
        });

        this.addRule(personScoreValidator, PERSON_SCORE_ERROR);
        this.addRule(personGradeValidator, PERSON_SCORE_ERROR);
    }
}
