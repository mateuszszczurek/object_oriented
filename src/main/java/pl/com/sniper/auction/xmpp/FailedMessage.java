package pl.com.sniper.auction.xmpp;

public class FailedMessage {

    private final String content;
    private final Exception exception;
    private final String sniperId;

    private FailedMessage(String sniperId, String content, Exception exception) {
        this.sniperId = sniperId;
        this.content = content;
        this.exception = exception;
    }

    public static FailedMessage failedMessage(String sniperId, String content, Exception exception) {
        return new FailedMessage(sniperId, content, exception);
    }

    public String getContent() {
        return content;
    }

    public String getSniperId() {
        return sniperId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FailedMessage that = (FailedMessage) o;

        if (sniperId != null ? !sniperId.equals(that.sniperId) : that.sniperId != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (exception != null ? !exception.equals(that.exception) : that.exception != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + (exception != null ? exception.hashCode() : 0);
        result = 31 * result + (sniperId != null ? sniperId.hashCode() : 0);
        return result;
    }
}
