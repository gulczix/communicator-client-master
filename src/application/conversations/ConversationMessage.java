package application.conversations;

import java.time.LocalDateTime;

/**
 * Reprezentuje pojedyncza wiadomosc w konwersacji.
 */
public class ConversationMessage {
    /**
     * Typ wiadomosci (kierunek).
     */
    private ConversationMessageType type;

    /**
     * Tresc wiadomosci.
     */
    private String message;

    /**
     * Data wyslania lub odbioru wiadomosci.
     */
    private LocalDateTime date;

    public ConversationMessage(ConversationMessageType type, String message) {
        this.type = type;
        this.message = message;
        this.date = LocalDateTime.now();
    }

    public ConversationMessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
