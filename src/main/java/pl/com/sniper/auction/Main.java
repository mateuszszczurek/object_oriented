package pl.com.sniper.auction;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import pl.com.sniper.gui.MainWindow;

import javax.swing.*;

public class Main {

    public static final String SNIPER_STATUS_NAME = "Sniper Status";
    public static final String STATUS_LOST = "Lost";
    public static final String AUCTION_RESOURCE = "Auction";

    private static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    public static final int ARG_HOSTNAME = 0;
    public static final int ARG_USERNAME = 1;
    public static final int ARG_PASSWORD = 2;
    public static final int ARG_ITEM_ID = 3;

    private MainWindow ui;

    public static void main(String ... args) throws Exception {
        final String host = args[ARG_HOSTNAME];
        final String sniperId = args[ARG_USERNAME];
        final String sniperPassword = args[ARG_PASSWORD];
        final String itemId = args[ARG_ITEM_ID];

        XMPPConnection connection = connection(host, sniperId, sniperPassword, itemId);

        Main main = new Main();
        main.joinAuction(itemId, connection);
    }

    private void joinAuction(String itemId, XMPPConnection connection) throws XMPPException {
        Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection),
                new MessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) {
                        ui.showStatus(MainWindow.STATUS_LOST);
                    }
                });

        chat.sendMessage(new Message());
    }

    private static String auctionId(String auctionId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, auctionId, connection.getServiceName());
    }

    private static XMPPConnection connection(String host, String sniperId, String sniperPassword, String auctionId) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(host);
        connection.connect();;
        connection.login(sniperId, sniperPassword, AUCTION_RESOURCE);
        return connection;
    }

    public Main() throws Exception {
        startUserInterface();
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ui = new MainWindow();
            }
        });
    }
}
