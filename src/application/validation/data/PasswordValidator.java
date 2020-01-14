package application.validation.data;

import application.validation.AbstractValidator;

/**
 * Walidator hasla.
 */
public class PasswordValidator extends AbstractValidator<String> {

    @Override
    public void performValidations(String obj) {
        if (obj == null) {
            result.addError("Password cannot be null.");
        } else if (obj.isEmpty()) {
            result.addError("Password cannot be empty.");
        }
    }

}
