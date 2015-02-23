package pl.com.sniper.auction;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import pl.com.sniper.auction.events.AuctionMessageTranslator;
import pl.com.sniper.auction.sniper.Auction;
import pl.com.sniper.auction.sniper.XMPPAuction;
import pl.com.sniper.auction.sniper.AuctionSniper;
import pl.com.sniper.gui.MainWindow;
import pl.com.sniper.gui.SnipersTableModel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.SwingUtilities.invokeAndWait;

public class Main {

    public static final String AUCTION_RESOURCE = "Auction";
    private static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    public static final int ARG_HOSTNAME = 0;
    public static final int ARG_USERNAME = 1;
    public static final int ARG_PASSWORD = 2;
    public static final int ARG_ITEM_ID = 3;

    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %s;";
    private static final MessageListener NO_LISTENER = null;

    private final SnipersTableModel snipers = new SnipersTableModel();

    @SuppressWarnings("unused")
    private Chat notToBeGCd;
    private MainWindow ui;


    public void startUserInterface() throws Exception {
        invokeAndWait(() -> ui = new MainWindow(snipers));
    }

    public void disconnectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }
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

        Auction auction = new XMPPAuction(chat);
        AuctionMessageTranslator listener = new AuctionMessageTranslator(new AuctionSniper(itemId, auction, new SwingThreadSniperListener(snipers)), connection.getUser());

        chat.addMessageListener(listener);

        this.notToBeGCd = chat;

        auction.join();
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


}