package application.validation.data;

import application.validation.AbstractValidator;
import application.validation.ValidationResult;
import javafx.util.Pair;

/**
 * Walidator powtorzonego hasla.
 */
public class RepeatedPasswordValidator extends AbstractValidator<Pair<String, String>> {

    @Override
    public void performValidations(Pair<String, String> obj) {
        if (obj == null) {
            result.addError("Passwords cannot be null.");
        } else if (!obj.getKey().equals(obj.getValue())) {
            result.addError("Passwords cannot be different.");
        }

        validateSinglePassword(obj.getKey());
    }

    private void validateSinglePassword(String password) {
        PasswordValidator validator = new PasswordValidator();
        validator.performValidations(password);
        ValidationResult singleValidationResult = validator.getResult();

        if (singleValidationResult.anyError()) {
            for (String error : singleValidationResult.getErrors()) {
                result.addError(error);
            }
        }
    }

}
