package application.server.communication.messages;

import application.server.communication.ServerMessage;
import application.server.communication.ServerMessageType;

/**
 * Zadanie autoryzacji.
 */
public class LoginRequest extends AbstractAuthenticationRequest {
    public LoginRequest(String login, String password) {
        super(login, password);
    }

    @Override
    protected ServerMessage prepareRequest() {
        return new ServerMessage(ServerMessageType.REQUEST_LOGIN, getHeader());
    }
}
