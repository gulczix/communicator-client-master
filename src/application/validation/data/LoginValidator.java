package application.validation.data;

import application.validation.AbstractValidator;

/**
 * Walidator loginu.
 */
public class LoginValidator extends AbstractValidator<String> {

    @Override
    public void performValidations(String obj) {
        if (obj == null) {
            result.addError("Login cannot be null.");
        } else if (obj.isEmpty()) {
            result.addError("Login cannot be empty.");
        }
    }

}
