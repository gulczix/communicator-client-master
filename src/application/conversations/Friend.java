package application.conversations;

/**
 * Reprezentuje znajomego.
 */
public class Friend implements Comparable<Friend> {
    /**
     * Nazwa znajomego.
     */
    private String name;

    /**
     * Status znajomego.
     */
    private FriendStatus status;

    public Friend(String name) {
        this.name = name;
        this.status = FriendStatus.INACTIVE;
    }

    public String getName() {
        return name;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }

    public FriendStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return name + " (" + status + ")";
    }

    @Override
    public int compareTo(Friend o) {
        return name.compareTo(o.name);
    }
}
