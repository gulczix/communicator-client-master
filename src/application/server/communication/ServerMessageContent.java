package application.server.communication;

/**
 * Reprezentuje tresc wiadomosci.
 */
public class ServerMessageContent {
    private String value;

    public ServerMessageContent() {

    }

    public ServerMessageContent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
