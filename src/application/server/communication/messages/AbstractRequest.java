package application.server.communication.messages;

import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;

import application.server.communication.ServerConnection;
import application.server.communication.ServerMessage;

/**
 * Klasa bazowa dla zadan.
 */
public abstract class AbstractRequest implements Closeable {
    /**
     * Polaczenie z serwerem.
     */
    private ServerConnection connection = null;

    /**
     * Czy polaczenie z serwerem zostalo przekazane w konstruktorze?
     */
    private boolean connectionProvidedFromOutside = false;

    /**
     * Przygotowuje zadanie.
     * @return Przygotowane zadanie.
     */
    protected abstract ServerMessage prepareRequest();

    /**
     * Wysyla wiadomosc.
     * @return Odpowiedz od serwera.
     * @throws UnknownHostException
     * @throws IOException
     */
    public ServerMessage send() throws UnknownHostException, IOException {
        ServerMessage message = prepareRequest();

        if (!connectionProvidedFromOutside) {
            if (connection != null) {
                connection.close();
            }

            connection = new ServerConnection();
        }

        return connection.sendMessage(message);
    }

    @Override
    public void close() throws IOException {
        if (!connectionProvidedFromOutside && connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public ServerConnection getConnection() {
        return connection;
    }

    protected AbstractRequest() {

    }

    protected AbstractRequest(ServerConnection connection) {
        this.connection = connection;
        this.connectionProvidedFromOutside = true;
    }
}
