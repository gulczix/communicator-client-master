package application.server.communication;

/**
 * Reprezentuje typ wiadomosci.
 */
public enum ServerMessageType {
    REQUEST_MESSAGE_SEND(0x33),
    REQUEST_REGISTRATION(0x36),
    REQUEST_LOGIN(0x37),
    REQUEST_ACTIVE_USERS(0x38),
    RESPONSE_ENCRYPTED_MESSAGE(0x01, "Message has been received."),
    RESPONSE_REGISTERED(0x02, "User has been registered."),
    RESPONSE_AUTHENTICATED(0x03, "User has been authenticated."),
    RESPONSE_MESSAGE_SENT(0x04, "Message has been sent."),
    RESPONSE_UNAUTHENTICATED(0x65, "Authentication failed.", true),
    RESPONSE_REGISTRATION_OK(0x33, "User has been registered."),
    RESPONSE_REGISTRATION_FAILED(0x66, "Registration failed.", true),
    RESPONSE_CLIENT_UNREACHABLE(0xC9, "Friend unreachable.", true),
    RESPONSE_INTERNAL_SERVER_ERROR(0xFF, "Internal server error.", true);

    public int getNumericValue() {
        return numericValue;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return error;
    }

    /**
     * Wyszukuje typ wiadomosci dla podanej wartosci numerycznej.
     * @param value Wartosc numeryczna wiadomosci.
     * @return Typ wiadomosci jako {@link ServerMessageType}.
     */
    public static ServerMessageType getForValue(int value) {
        for (ServerMessageType type : ServerMessageType.values()) {
            if (type.getNumericValue() == value) {
                return type;
            }
        }

        return null;
    }

    private ServerMessageType(int numericValue) {
        this(numericValue, null);
    }

    private ServerMessageType(int numericValue, String message) {
        this(numericValue, message, false);
    }

    private ServerMessageType(int numericValue, String message, boolean error) {
        this.numericValue = numericValue;
        this.message = message;
        this.error = error;
    }

    private int numericValue;
    private String message;
    private boolean error;
}
