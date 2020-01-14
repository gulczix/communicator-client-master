package application.server.communication;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Reprezentuje wiadomosci wymieniane z serwerem.
 */
public class ServerMessage {
    /**
     * Typ.
     */
    private ServerMessageType type;

    /**
     * Naglowki.
     */
    private List<ServerMessageHeader> headers;

    /**
     * Tresc.
     */
    private ServerMessageContent message;

    public ServerMessage(ServerMessageType type, ServerMessageHeader... headers) {
        this.type = type;
        this.headers = Arrays.asList(headers);
    }

    public ServerMessage(ServerMessageType type, ServerMessageContent message, ServerMessageHeader... headers) {
        this(type, headers);

        this.message = message;
    }

    /**
     * Probuje znalezc naglowek.
     * @param key Nazwa naglowka.
     * @return Naglowek.
     */
    public ServerMessageHeader findHeader(String key) {
        for (ServerMessageHeader header : headers) {
            if (header.getKey().equals(key)) {
                return header;
            }
        }

        return null;
    }

    public List<ServerMessageHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(ServerMessageHeader... headers) {
        this.headers = Arrays.asList(headers);
    }

    public ServerMessageContent getMessage() {
        return message;
    }

    public void setMessage(ServerMessageContent message) {
        this.message = message;
    }

    public void setType(ServerMessageType type) {
        this.type = type;
    }

    public ServerMessageType getType() {
        return type;
    }

    /**
     * Konwertuje wiadomosc do formy bajtowej.
     * @return Bajtowa forma wiadomosci.
     */
    public byte[] toRequestFormat() {
        List<Byte> bytes = new ArrayList<>();

        appendTypeToCollection(bytes);
        appendHeaderSizeToCollection(bytes);
        appendContentSizeToCollection(bytes);
        appendHeaderToCollection(bytes);
        appendContentToCollection(bytes);

        byte[] bytesArray = new byte[bytes.size()];

        for (int i = 0; i < bytes.size(); i++) {
            bytesArray[i] = bytes.get(i);
        }

        return bytesArray;
    }

    /**
     * Odwraca tablice bajtow.
     * @param array Tablica bajtow.
     * @return Odwrocona tablica bajtow.
     */
    public static byte[] reverseByteArray(byte[] array) {
        byte[] reversedArray = new byte[array.length];

        for (int i = 0; i < reversedArray.length; i++) {
            reversedArray[i] = array[array.length - i - 1];
        }

        return reversedArray;
    }

    /**
     * Konwertuje naglowki do formy bajtowej.
     * @return Naglowki w formie bajtowej.
     */
    private Byte[] getHeadersAsBytes() {
        if (headers == null || headers.isEmpty()) {
            return new Byte[0];
        }

        List<Byte> headersAsBytes = new ArrayList<>();

        for (ServerMessageHeader header : headers) {
            appendStringBytesToCollection(headersAsBytes, header.toString());
            appendStringBytesToCollection(headersAsBytes, "\n");
        }

        return headersAsBytes.toArray(new Byte[0]);
    }

    /**
     * Konwertuje tresc do formy bajtowej.
     * @return Tresc w formie bajtowej.
     */
    private Byte[] getMessageAsBytes() {
        if (message == null || message.getValue().length() == 0) {
            return new Byte[0];
        }

        List<Byte> contentAsBytes = new ArrayList<>();

        appendStringBytesToCollection(contentAsBytes, message.getValue());

        return contentAsBytes.toArray(new Byte[0]);
    }

    /**
     * Dopisuje typ wiadomosci do kolekcji bajtow.
     * @param bytes Kolekcja bajtow.
     */
    private void appendTypeToCollection(Collection<Byte> bytes) {
        bytes.add((byte)type.getNumericValue());
    }

    /**
     * Dopisuje rozmiar naglowkow do kolekcji bajtow.
     * @param collection Kolekcja bajtow.
     */
    private void appendHeaderSizeToCollection(Collection<Byte> collection) {
        appendSizeToCollection(collection, getHeadersAsBytes());
    }

    /**
     * Dopisuje rozmiar tresci do kolekcji bajtow.
     * @param collection Kolekcja bajtow.
     */
    private void appendContentSizeToCollection(Collection<Byte> collection) {
        appendSizeToCollection(collection, getMessageAsBytes());
    }

    /**
     * Dopisuje rozmiar do kolekcji bajtow.
     * @param collection Kolekcja bajtow.
     * @param array Tablica bajtow z danymi.
     */
    private void appendSizeToCollection(Collection<Byte> collection, Byte[] array) {
        byte[] arraySize = reverseByteArray(ByteBuffer.allocate(4).putInt(array.length).array());

        for (byte b : arraySize) {
            collection.add(b);
        }
    }

    /**
     * Dopisuje naglowek do kolekcji bajtow.
     * @param collection Kolekcja bajtow.
     */
    private void appendHeaderToCollection(Collection<Byte> collection) {
        appendDataToCollection(collection, getHeadersAsBytes());
    }

    /**
     * Dopisuje tresc do kolekcji bajtow.
     * @param collection Kolekcja bajtow.
     */
    private void appendContentToCollection(Collection<Byte> collection) {
        appendDataToCollection(collection, getMessageAsBytes());
    }

    /**
     * Dopisuje String do kolekcji bajtow.
     * @param collection Kolekcja bajtow.
     * @param str String do dopisania.
     */
    private void appendStringBytesToCollection(Collection<Byte> collection, String str) {
        byte[] originalStringBytes = str.getBytes();
        Byte[] objectStringBytes = new Byte[originalStringBytes.length];

        for (int i = 0; i < originalStringBytes.length; i++) {
            objectStringBytes[i] = originalStringBytes[i];
        }

        appendDataToCollection(collection, objectStringBytes);
    }

    /**
     * Dopisuje dane do kolekcji bajtow.
     * @param collection Kolekcja bajtow.
     * @param array Tablica z danymi.
     */
    private void appendDataToCollection(Collection<Byte> collection, Byte[] array) {
        if (array.length == 0) {
            return;
        }

        for (Byte b : array) {
            collection.add(b);
        }
    }
}
