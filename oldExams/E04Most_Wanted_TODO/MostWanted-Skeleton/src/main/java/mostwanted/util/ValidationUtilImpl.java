package mostwanted.util;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Validation;
import javax.validation.Validator;

public class ValidationUtilImpl implements ValidationUtil {

    private Validator validator;

    @Autowired
    public ValidationUtilImpl() {
        this.validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Override
    public <E> boolean isValid(E entity) {
        return this.validator.validate(entity).isEmpty();
    }
}
