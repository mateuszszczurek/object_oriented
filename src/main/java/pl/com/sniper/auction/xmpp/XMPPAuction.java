package pl.com.sniper.auction.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import pl.com.sniper.auction.Main;
import pl.com.sniper.auction.events.AuctionEventListener;
import pl.com.sniper.auction.sniper.Auction;
import pl.com.sniper.util.Announcer;

public class XMPPAuction implements Auction {

    public static final String JOIN_COMMAND_FORMAT = "JOIN COMMAND";
    public static final String AUCTION_RESOURCE = "Auction";
    private static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private Chat chat;

    private Announcer<AuctionEventListener> auctionEventListeners = new Announcer<>(AuctionEventListener.class);

    public XMPPAuction(XMPPConnection connection, String itemId) {
        this.chat =  connection.getChatManager().createChat(auctionId(itemId, connection),
                new AuctionMessageTranslator(auctionEventListeners.announce(), connection.getUser()));
    }

    private static String auctionId(String auctionId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, auctionId, connection.getServiceName());
    }

    @Override
    public void bid(int howMuch) {
        sendMessage(String.format(Main.BID_COMMAND_FORMAT, howMuch));
    }

    @Override
    public void join() {
        sendMessage(JOIN_COMMAND_FORMAT);
    }

    @Override
    public void addAuctionEventListener(AuctionEventListener listener) {
        this.auctionEventListeners.addListener(listener);
    }

    private void sendMessage(String joinCommandFormat) {
        try {
            chat.sendMessage(joinCommandFormat);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

}
