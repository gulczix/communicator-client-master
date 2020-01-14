package application.server.communication.messages;

import application.server.communication.ServerConnection;
import application.server.communication.ServerMessage;
import application.server.communication.ServerMessageType;

/**
 * Zadanie informacji o aktywnych uzytkownikach.
 */
public class GetAllActiveUsersRequest extends AbstractRequest {
    public GetAllActiveUsersRequest(ServerConnection connection) {
        super(connection);
    }

    @Override
    protected ServerMessage prepareRequest() {
        return new ServerMessage(ServerMessageType.REQUEST_ACTIVE_USERS);
    }
}
