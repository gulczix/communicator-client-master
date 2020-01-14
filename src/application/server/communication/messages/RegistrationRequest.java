package application.server.communication.messages;

import application.server.communication.ServerMessage;
import application.server.communication.ServerMessageType;

/**
 * Zadanie rejestracji.
 */
public class RegistrationRequest extends AbstractAuthenticationRequest {
    public RegistrationRequest(String login, String password) {
        super(login, password);
    }

    @Override
    protected ServerMessage prepareRequest() {
        return new ServerMessage(ServerMessageType.REQUEST_REGISTRATION, getHeader());
    }
}
