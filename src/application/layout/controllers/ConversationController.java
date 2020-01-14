package application.layout.controllers;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import application.conversations.Conversation;
import application.conversations.ConversationMessageType;
import application.conversations.Friend;
import application.conversations.FriendStatus;
import application.layout.alerts.CustomAlert;
import application.server.communication.ServerConnection;
import application.server.communication.ServerMessage;
import application.server.communication.ServerMessageContent;
import application.server.communication.ServerMessageType;
import application.server.communication.messages.GetAllActiveUsersRequest;
import application.server.communication.messages.MessageSendRequest;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Kontroler dla okna konwersacji.
 */
public class ConversationController extends AbstractController {

    @FXML
    private TextArea conversationTextArea;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private Button sendButton;

    @FXML
    private ListView<Friend> friendsListView;

    @FXML
    private Button openFriendConversationButton;

    @FXML
    private Button addFriendButton;

    @FXML
    private Button deleteFriendButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label currentConversationFriendNameLabel;

    /**
     * Otwarte polaczenie z serwerem.
     */
    private ServerConnection connection;

    /**
     * Zalogowany uzytkownik.
     */
    private String login;

    /**
     * Konwersacje ze znajomymi.
     */
    private Map<String, Conversation> conversations;

    /**
     * Znajomy, dla ktorego obecnie jest otwarta konwersacja.
     */
    private Friend currentConversationFriend;

    /**
     * Timer aktualizujacy regularnie potrzebne dane.
     */
    private Timer updateTimer;

    @FXML
    void onAddFriendButtonClick(ActionEvent event) {
        Optional<String> friendName = CustomAlert.input("Please provide your friend's name:");

        if (friendName.isPresent()) {
            addFriendToList(friendName.get());
        }
    }

    @FXML
    void onDeleteFriendButtonClick(ActionEvent event) {
        if (CustomAlert.confirm("Are you sure you want to delete selected friends?") == ButtonType.YES) {
            Friend selectedFriend = getSelectedFriend();

            if (!selectedFriend.getName().isEmpty()) {
                friendsListView.getItems().remove(selectedFriend);
                conversations.remove(selectedFriend.getName());

                if (currentConversationFriend.equals(selectedFriend)) {
                    currentConversationFriend = null;
                    updateConversation();
                }
            } else {
                CustomAlert.error("No one was selected.");
            }
        }
    }

    @FXML
    void onOpenFriendConversationButtonClick(ActionEvent event) {
        Friend selectedFriend = getSelectedFriend();

        if (selectedFriend != null) {
            createConversationForFriendIfNotExists(selectedFriend);
            currentConversationFriend = selectedFriend;
            updateConversation();
        } else {
            CustomAlert.error("No one was selected.");
        }
    }

    @FXML
    void onSendButtonClick(ActionEvent event) throws UnknownHostException, IOException {
        String message = messageTextArea.getText();

        if (message.isEmpty()) {
            return;
        }

        if (currentConversationFriend == null) {
            return;
        }

        MessageSendRequest request = new MessageSendRequest(connection, message,
                                                            currentConversationFriend.getName());
        ServerMessage response = request.send();

        if (!handleErrorResponse(response)) {
            if (response.getType() != ServerMessageType.RESPONSE_MESSAGE_SENT) {
                CustomAlert.error(response.getMessage().getValue());
            } else {
                getCurrentConversation().addMessage(ConversationMessageType.OUTBOUND, message);
                messageTextArea.clear();
                updateConversation();
            }
        }

        request.close();
    }

    @FXML
    void onLogoutButtonClick(ActionEvent event) throws IOException {
        if (CustomAlert.confirm("Are you sure you want to exit the program?") == ButtonType.YES) {
            updateTimer.cancel();
            updateTimer.purge();
            Platform.exit();
            System.exit(0);
        }
    }

    @FXML
    void onMessageTextFieldKeyPressed(KeyEvent event) throws UnknownHostException, IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onSendButtonClick(new ActionEvent());
            event.consume();
        }
    }

    public ConversationController() {
        this.conversations = new HashMap<>();
        this.currentConversationFriend = null;
        this.updateTimer = new Timer();

        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    backgroundLoop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 1000);
    }

    /**
     * Inicjalizuje obsluge zdarzen.
     */
    public void initEventHandlers() {
        getStage().setOnCloseRequest(event -> {
            try {
                onLogoutButtonClick(new ActionEvent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setConnection(ServerConnection connection) {
        this.connection = connection;
    }

    public void setLogin(String login) {
        this.login = login;
        getStage().setTitle("Logged in as " + login);
    }

    @Override
    protected Stage getStage() {
        return (Stage) conversationTextArea.getScene().getWindow();
    }

    /**
     * Zwraca znajomego zaznaczonego na liscie znajomych.
     * @return
     */
    private Friend getSelectedFriend() {
        ObservableList<Friend> selectedFriends = friendsListView.getSelectionModel().getSelectedItems();

        if (!selectedFriends.isEmpty()) {
            return selectedFriends.get(0);
        } else {
            return null;
        }
    }

    /**
     * Dodaje znajomego do listy.
     * @param friend Nazwa znajomego do dodania.
     * @return Obiekt reprezentujacy znajomego.
     */
    private Friend addFriendToList(String friend) {
        if (friend == null || friend.isEmpty()) {
            return null;
        }

        Friend foundFriend = getFriendByName(friend);

        if (getFriendByName(friend) != null) {
            CustomAlert.error("Friend with given name already exists.");
            return foundFriend;
        } else {
            Friend newFriend = new Friend(friend);
            friendsListView.getItems().add(newFriend);
            friendsListView.getItems().sort((s1, s2) -> s1.compareTo(s2));
            return newFriend;
        }
    }

    /**
     * Aktualizuje okno konwersacji.
     */
    private void updateConversation() {
        if (currentConversationFriend == null) {
            currentConversationFriendNameLabel.setText("-");
            conversationTextArea.clear();
            return;
        }

        currentConversationFriendNameLabel.setText(currentConversationFriend.toString());
        conversationTextArea.clear();
        conversationTextArea.appendText(getCurrentConversation().getAll(login, currentConversationFriend.getName()));
    }

    /**
     * Petla dzialajaca w tle.
     * @throws UnknownHostException
     * @throws IOException
     */
    private void backgroundLoop() throws UnknownHostException, IOException {
        receiveMessage();
        updateStatuses();
    }

    /**
     * Sprawdza, czy serwer przyslal jakas wiadomosc - jesli tak,
     * dopisuje ja do odpowiedniej konwersacji.
     * @throws IOException
     */
    private void receiveMessage() throws IOException {
        ServerMessage message = connection.tryToReceiveMessage();

        if (message != null) {
            String sender = message.findHeader("sender").getValue();
            String content = message.getMessage().getValue();

            Friend friend = getFriendByName(sender);

            if (friend == null) {
                friend = addFriendToList(sender);
            }

            createConversationForFriendIfNotExists(friend);

            conversations.get(friend.getName())
                         .addMessage(ConversationMessageType.INBOUND, content);

            updateConversation();
        }
    }

    /**
     * Aktualizuje statusy znajomych.
     * @throws UnknownHostException
     * @throws IOException
     */
    private void updateStatuses() throws UnknownHostException, IOException {
        GetAllActiveUsersRequest request = new GetAllActiveUsersRequest(connection);
        ServerMessage response = request.send();
        ServerMessageContent responseMessage = response.getMessage();
        List<String> activeFriends = Arrays.asList(responseMessage.getValue().split(","));

        request.close();

        for (Friend friend : friendsListView.getItems()) {
            if (activeFriends.contains(friend.getName())) {
                friend.setStatus(FriendStatus.ACTIVE);
            } else {
                friend.setStatus(FriendStatus.INACTIVE);
            }
        }

        friendsListView.refresh();
    }

    /**
     * Tworzy konwersacje dla znajomego, jesli jeszcze nie istnieje.
     * @param friend Znajomy.
     */
    private void createConversationForFriendIfNotExists(Friend friend) {
        if (!conversations.containsKey(friend.getName())) {
            conversations.put(friend.getName(), new Conversation());
        }
    }

    /**
     * Zwraca obiekt reprezentujacy znajomego na podstawie jego nazwy.
     * @param name Nazwa znajomego.
     * @return Obiekt reprezentujacy znajomego.
     */
    private Friend getFriendByName(String name) {
        for (Friend friend : friendsListView.getItems()) {
            if (friend.getName().equals(name)) {
                return friend;
            }
        }

        return null;
    }

    /**
     * Zwraca obecnie otwarta konwersacje.
     * @return Obecnie otwarta konwersacja.
     */
    private Conversation getCurrentConversation() {
        if (currentConversationFriend == null) {
            return null;
        }

        return conversations.get(currentConversationFriend.getName());
    }
}
