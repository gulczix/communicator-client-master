package application.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Wynik walidacji.
 */
public class ValidationResult {
    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    /**
     * Sprawdza, czy walidacja ma jakiekolwiek bledy.
     * @return True, jesli walidacja nie przeszla pomyslenia, w przeciwnym razie false.
     */
    public boolean anyError() {
        return !errors.isEmpty();
    }

    /**
     * Buduje String ze wszystkich bledow walidacji.
     * @return String ze wszystkich bledow walidacji.
     */
    public String allErrorsToString() {
        StringBuilder builder = new StringBuilder();

        for (String error : errors) {
            builder.append(error);
        }

        return builder.toString();
    }

    /**
     * Dodaje blad walidacji.
     * @param error Tresc bledu.
     */
    public void addError(String error) {
        errors.add(error);
    }

    protected ValidationResult() {
        this.errors = new ArrayList<>();
    }
}
