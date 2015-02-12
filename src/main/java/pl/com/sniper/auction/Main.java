package pl.com.sniper.auction;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import pl.com.sniper.auction.events.AuctionMessageTranslator;
import pl.com.sniper.auction.sniper.Auction;
import pl.com.sniper.auction.sniper.AuctionSniper;
import pl.com.sniper.auction.sniper.SniperListener;
import pl.com.sniper.gui.MainWindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.SwingUtilities.invokeAndWait;

public class Main implements SniperListener {

    public static final String SNIPER_STATUS_NAME = "Sniper Status";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_BIDDING = "Bidding";

    public static final String AUCTION_RESOURCE = "Auction";
    private static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    public static final int ARG_HOSTNAME = 0;
    public static final int ARG_USERNAME = 1;
    public static final int ARG_PASSWORD = 2;
    public static final int ARG_ITEM_ID = 3;

    public static final String JOIN_COMMAND_FORMAT = "JOIN COMMAND";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %s;";
    private static final MessageListener NO_LISTENER = null;

    private MainWindow ui;

    @SuppressWarnings("unused")
    private Chat notToBeGCd;

    public static void main(String ... args) throws Exception {
        final String host = args[ARG_HOSTNAME];
        final String sniperId = args[ARG_USERNAME];
        final String sniperPassword = args[ARG_PASSWORD];
        final String itemId = args[ARG_ITEM_ID];

        XMPPConnection connection = connection(host, sniperId, sniperPassword);

        Main main = new Main();
        main.joinAuction(itemId, connection);
    }

    private void joinAuction(String itemId, XMPPConnection connection) throws XMPPException {

       disconnectWhenUICloses(connection);

        Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection),
                NO_LISTENER);

        AuctionMessageTranslator listener = new AuctionMessageTranslator(new AuctionSniper(new Auction(chat), this));

        chat.addMessageListener(listener);

        this.notToBeGCd = chat;

        chat.sendMessage(JOIN_COMMAND_FORMAT);
    }

    private void disconnectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    private static String auctionId(String auctionId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, auctionId, connection.getServiceName());
    }

    private static XMPPConnection connection(String host, String sniperId, String sniperPassword) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(host);
        connection.connect();
        connection.login(sniperId, sniperPassword, AUCTION_RESOURCE);
        return connection;
    }

    public Main() throws Exception {
        startUserInterface();
    }

    private void startUserInterface() throws Exception {
        invokeAndWait(() -> ui = new MainWindow());
    }

    @Override
    public void sniperLost() {
        ui.showStatus(STATUS_LOST);
    }

    @Override
    public void sniperBidding() {
        ui.showStatus(STATUS_BIDDING);
    }
}