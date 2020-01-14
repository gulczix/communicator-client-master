package application.conversations;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje konwersacje pomiedzy dwoma osobami.
 */
public class Conversation {
    /**
     * Wiadomosci w konwersacji.
     */
    private List<ConversationMessage> messages;

    public Conversation() {
        this.messages = new ArrayList<>();
    }

    /**
     * Dodaje wiadomosc do konwersacji.
     * @param type Kierunek wiadomosci.
     * @param message Tresc wiadomosci.
     */
    public void addMessage(ConversationMessageType type, String message) {
        messages.add(new ConversationMessage(type, message));
    }

    public List<ConversationMessage> getMessages() {
        return messages;
    }

    /**
     * Pobiera wszystkie wiadomosci w konwersacji w formie Stringa.
     * @param loggedInUser Uzytkownik wysylajacy wiadomosci.
     * @param friend Znajomy odbierajacy wiadomosci.
     * @return Cala konwersacja jako tekst.
     */
    public String getAll(String loggedInUser, String friend) {
        StringBuilder strBuilder = new StringBuilder();

        for (ConversationMessage message : messages) {
            strBuilder.append(message.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            strBuilder.append(" ");

            if (message.getType() == ConversationMessageType.INBOUND) {
                strBuilder.append(friend + " -> " + loggedInUser);
            } else {
                strBuilder.append(loggedInUser + " -> " + friend);
            }

            strBuilder.append("\n");
            strBuilder.append(message.getMessage());
            strBuilder.append("\n\n");
        }

        return strBuilder.toString();
    }
}
