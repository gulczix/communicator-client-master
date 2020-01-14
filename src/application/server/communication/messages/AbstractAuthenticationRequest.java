package application.server.communication.messages;

import java.util.Base64;

import application.server.communication.ServerMessageHeader;

/**
 * Klasa bazowa dla zadan autoryzacyjnych.
 */
public abstract class AbstractAuthenticationRequest extends AbstractRequest {
    private String login;
    private String password;

    public AbstractAuthenticationRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Buduje naglowek autoryzacyjny.
     * @return Naglowek autoryzacyjny.
     */
    public ServerMessageHeader getHeader() {
        String loginAndPassword = login + ":" + password;
        String loginAndPasswordBase64 = Base64.getEncoder().encodeToString(loginAndPassword.getBytes());

        ServerMessageHeader header = new ServerMessageHeader();
        header.setKey("authentication");
        header.setValue(loginAndPasswordBase64);

        return header;
    }

}
