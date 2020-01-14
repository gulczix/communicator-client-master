package application.server.communication;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import application.configuration.Configuration;

/**
 * Reprezentuje polaczenie z serwerem.
 */
public class ServerConnection implements Closeable {
    /**
     * Socket klienta.
     */
    private Socket socket;

    public ServerConnection() throws UnknownHostException, IOException {
        socket = new Socket(Configuration.get("IP"),
                            Configuration.getInt("PORT"));
    }

    /**
     * Wysyla wiadomosc do serwera i odbiera odpowiedz.
     * @param message Wiadomosc do wyslania.
     * @return Wiadomosc uzyskana od serwera.
     * @throws IOException
     */
    public ServerMessage sendMessage(ServerMessage message) throws IOException {
        byte[] request = message.toRequestFormat();
        socket.getOutputStream().write(request);

        return readMessageFromSocketInputStream();
    }

    /**
     * Sprawdza, czy serwer przyslal jakas wiadomosc.
     * @return Wiadomosc, jesli serwer cos przyslal. Jesli nie, null.
     * @throws IOException
     */
    public ServerMessage tryToReceiveMessage() throws IOException {
        if (socket.getInputStream().available() < 9) {
            return null;
        }

        return readMessageFromSocketInputStream();
    }

    /**
     * Sprawdza, czy polaczenie jest zamkniete.
     * @return True, jesli polaczenie jest zamkniete, w przeciwnym razie false.
     */
    public boolean isClosed() {
        return socket != null && socket.isClosed();
    }

    /**
     * Odbiera wiadomosc od serwera.
     * @return Wiadomosc od serwera.
     * @throws IOException
     */
    private ServerMessage readMessageFromSocketInputStream() throws IOException {
        InputStream inputStream = socket.getInputStream();

        int messageType = inputStream.read();
        int headerSize = readInt(inputStream, 4);
        int contentSize = readInt(inputStream, 4);

        String header = readString(inputStream, headerSize);
        String response = readString(inputStream, contentSize);

        ServerMessage message = new ServerMessage(ServerMessageType.getForValue(messageType));

        if (!header.isEmpty()) {
            String[] splittedHeader = header.replace("\n", "").split(":");
            message.setHeaders(new ServerMessageHeader(splittedHeader[0], splittedHeader[1]));
        }

        if (!response.isEmpty()) {
            message.setMessage(new ServerMessageContent(response));
        }

        return message;
    }

    /**
     * Wczytuje String z serwera.
     * @param inputStream Strumien danych serwera.
     * @param size Rozmiar Stringa do wczytania.
     * @return Wczytany String.
     * @throws IOException
     */
    private String readString(InputStream inputStream, int size) throws IOException {
        byte[] stringArray = new byte[size];

        inputStream.read(stringArray, 0, size);

        StringBuilder strBuilder = new StringBuilder();

        for (byte b : stringArray) {
            strBuilder.append((char)b);
        }

        return strBuilder.toString();
    }

    /**
     * Wczytuje int z serwera.
     * @param inputStream Strumien danych serwera.
     * @param size Rozmiar inta do wczytania.
     * @return Wczytany int.
     * @throws IOException
     */
    private int readInt(InputStream inputStream, int size) throws IOException {
        byte[] array = new byte[size];
        inputStream.read(array, 0, size);
        array = ServerMessage.reverseByteArray(array);

        int intReadFromStream = ByteBuffer.wrap(array).getInt();

        return intReadFromStream;
    }

    @Override
    public void close() throws IOException {
        if (!isClosed()) {
            socket.close();
        }
    }
}
