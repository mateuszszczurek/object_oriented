package endtoend;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import pl.com.sniper.auction.Main;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class FakeAuctionServer {

    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost";
    private static final String AUCTION_PASSWORD = "auction";

    private final XMPPConnection connection;
    private Chat currentChat;

    private final SingleMessageListener messageListener = new SingleMessageListener();

    private String itemId;

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);

        connection.getChatManager().addChatListener((chat, b) -> {
            currentChat = chat;
            currentChat.addMessageListener(messageListener);
        });
    }

    public void hasReceivedJoiRequestFromSniper(String sniperId) throws InterruptedException {
        receivesAMessageMatching(sniperId, equalTo(Main.JOIN_COMMAND_FORMAT));
    }

    public void announceClosed() throws XMPPException {
        currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;");
    }

    public void stop() {
        connection.disconnect();
    }

    public String getItemId() {
        return itemId;
    }

    public void hasReceivedABid(int howMuch, String userId) throws InterruptedException {
        receivesAMessageMatching(userId, equalTo(format(Main.BID_COMMAND_FORMAT, howMuch)));
    }

    public void receivesAMessageMatching(String sniperId, Matcher<? super String> messageMatcher) throws InterruptedException {
        messageListener.receivesAMessageThat(messageMatcher);
        assertThat(currentChat.getParticipant(), equalTo(sniperId));
    }

    public void reportPrice(int price, int minimalIncrement, String auctioneer) throws XMPPException {
        currentChat.sendMessage(new AuctionEvent(price, minimalIncrement, auctioneer).toMessage());
    }

    private class AuctionEvent {

        private final int price;
        private final int minimalIncrement;
        private final String auctioneer;

        public AuctionEvent(int price, int minimalIncrement, String auctioneer) {
            this.price = price;
            this.minimalIncrement = minimalIncrement;
            this.auctioneer = auctioneer;
        }

        public Message toMessage() {
            String message = format("SOLVersion: 1.1; Event: PRICE; " +
                    "CurrentPrice: %d; Increment: %d; Bidder: %s;", price, minimalIncrement, auctioneer);

            Message msg = new Message();
            msg.setBody(message);
            return msg;
        }

    }
}
