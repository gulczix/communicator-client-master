package application.validation;

import application.layout.alerts.CustomAlert;

/**
 * Klasa bazowa dla walidatorow.
 *
 * @param <T> Typ walidowanego obiektu.
 */
public abstract class AbstractValidator<T> {
    protected ValidationResult result;

    /**
     * Wykonuje walidacje.
     * @param obj Obiekt do walidacji.
     */
    protected abstract void performValidations(T obj);

    /**
     * Sprawdza, czy podany obiekt jest poprawny.
     * @param obj Obiekt do walidacji.
     * @param showAlertOnError Czy w razie bledu ma sie pojawic alert?
     * @return True, jesli obiekt jest poprawny, w przeciwnym razie false.
     */
    public boolean isValid(T obj, boolean showAlertOnError) {
        performValidations(obj);

        if (showAlertOnError && result.anyError()) {
            CustomAlert.error(result);
        }

        return !result.anyError();
    }

    /**
     * Sprawdza, czy podany obiekt jest poprawny, bez pokazywania alertu.
     * @param obj Obiekt do walidacji.
     * @return True, jesli obiekt jest poprawny, w przeciwnym razie false.
     */
    public boolean isValid(T obj) {
        return isValid(obj, false);
    }

    public ValidationResult getResult() {
        return result;
    }

    protected AbstractValidator() {
        this.result = new ValidationResult();
    }
}
