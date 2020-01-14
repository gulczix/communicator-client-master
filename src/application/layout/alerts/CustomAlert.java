package application.layout.alerts;

import java.util.Optional;

import application.server.communication.ServerMessage;
import application.server.communication.ServerMessageContent;
import application.validation.ValidationResult;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

/**
 * Upraszcza wyswietlanie alertow w okienku.
 */
public class CustomAlert {
    /**
     * Wyswietla alert z prosba o wpisanie tekstu.
     * @param message Wiadomosc do wyswietlenia.
     * @return Wpisany tekst.
     */
    public static Optional<String> input(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input dialog");
        dialog.setHeaderText(message);
        return dialog.showAndWait();
    }

    /**
     * Wyswietla alert z prosba o potwierdzenie.
     * @param message Wiadomosc do wyswietlenia.
     * @return Wybrana przez uzytkownika opcja.
     */
    public static ButtonType confirm(String message) {
        Optional<ButtonType> result = alert(AlertType.CONFIRMATION, "Confirmation", message, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

        if (result.isPresent()) {
            return result.get();
        } else {
            return ButtonType.NO;
        }
    }

    /**
     * Wyswietla alert z informacja.
     * @param message Wiadomosc do wyswietlenia.
     * @return Wybrana przez uzytkownika opcja.
     */
    public static Optional<ButtonType> info(String message) {
        return alert(AlertType.INFORMATION, "Information", message, ButtonType.OK);
    }

    /**
     * Wyswietla alert z informacja.
     * @param message Wiadomosc do wyswietlenia.
     * @return Wybrana przez uzytkownika opcja.
     */
    public static Optional<ButtonType> info(ServerMessage message) {
        ServerMessageContent content = message.getMessage();

        if (content != null && content.getValue() != null && !content.getValue().isEmpty()) {
            return info(content.getValue());
        } else {
            return info(message.getType().getMessage());
        }
    }

    /**
     * Wyswietla alert z bledem.
     * @param message Wiadomosc do wyswietlenia.
     * @return Wybrana przez uzytkownika opcja.
     */
    public static Optional<ButtonType> error(String message) {
        return alert(AlertType.ERROR, "Error", message, ButtonType.OK);
    }


    /**
     * Wyswietla alert z bledem.
     * @param exception Wyjatek.
     * @return Wybrana przez uzytkownika opcja.
     */
    public static Optional<ButtonType> error(Exception exception) {
        exception.printStackTrace();
        return error(exception.getMessage());
    }

    /**
     * Wyswietla alert z bledem (o ile jakis sie pojawil w wyniku walidacji).
     * @param validationResult Wynik walidacji.
     * @return Wybrana przez uzytkownika opcja.
     */
    public static Optional<ButtonType> error(ValidationResult validationResult) {
        if (validationResult.anyError()) {
            return error(validationResult.allErrorsToString());
        }

        return null;
    }

    /**
     * Wyswietla alert.
     * @param alertType Typ alertu.
     * @param title Tytul alertu.
     * @param message Wiadomosc do wyswietlenia.
     * @param buttons Przyciski.
     * @return Wybrana przez uzytkownika opcja.
     */
    private static Optional<ButtonType> alert(AlertType alertType, String title, String message,
                                              ButtonType... buttons) {
        Alert alert = new Alert(alertType, message, buttons);
        alert.setTitle(title);
        return alert.showAndWait();
    }
}
