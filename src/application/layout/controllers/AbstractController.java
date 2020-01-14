package application.layout.controllers;

import application.layout.alerts.CustomAlert;
import application.server.communication.ServerMessage;
import javafx.stage.Stage;

/**
 * Klasa bazowa dla kontrolerow.
 */
public abstract class AbstractController {
    /**
     * Obsluguje bledna odpowiedz od serwera.
     * @param response Odpowiedz od serwera.
     * @return True, jesli odpowiedz jest bledna, w przeciwnym razie false.
     */
    protected boolean handleErrorResponse(ServerMessage response) {
        if (response.getType().isError()) {
            CustomAlert.error(response.getMessage().getValue());
            return true;
        }

        return false;
    }

    /**
     * Zwraca {@link Stage} dla obecnego kontrolera.
     * @return {@link Stage} dla obecnego kontrolera.
     */
    protected abstract Stage getStage();
}
