package application.server.communication.messages;

import application.server.communication.ServerConnection;
import application.server.communication.ServerMessage;
import application.server.communication.ServerMessageContent;
import application.server.communication.ServerMessageHeader;
import application.server.communication.ServerMessageType;

/**
 * Zadanie wyslania wiadomosci.
 */
public class MessageSendRequest extends AbstractRequest {
    private String message;
    private String recipient;

    public MessageSendRequest(ServerConnection connection, String message,
                              String recipient) {
        super(connection);
        this.message = message;
        this.recipient = recipient;
    }

    @Override
    protected ServerMessage prepareRequest() {
        ServerMessageHeader header = new ServerMessageHeader("recipient", recipient);
        ServerMessageContent content = new ServerMessageContent(message);
        return new ServerMessage(ServerMessageType.REQUEST_MESSAGE_SEND, content, header);
    }
}
