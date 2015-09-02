package pl.com.sniper.auction;

import pl.com.sniper.gui.MainWindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.SwingUtilities.invokeAndWait;

public class Main {

    public static final int ARG_HOSTNAME = 0;
    public static final int ARG_USERNAME = 1;

    public static final int ARG_PASSWORD = 2;
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %s;";

    private MainWindow ui;
    private final SniperPortfolio snipers = new SniperPortfolio();


    public void startUserInterface() throws Exception {
        invokeAndWait(() -> ui = new MainWindow(snipers));
    }

    public void disconnectWhenUICloses(final XMPPAuctionHouse auctionFactory) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                auctionFactory.cleanup();
            }
        });
    }

    public static void main(String... args) throws Exception {
        final String host = args[ARG_HOSTNAME];
        final String sniperId = args[ARG_USERNAME];
        final String sniperPassword = args[ARG_PASSWORD];

        XMPPAuctionHouse auctionFactory = XMPPAuctionHouse.connect(host, sniperId, sniperPassword);

        Main main = new Main();

        main.registerUserEventListener(auctionFactory);
        main.disconnectWhenUICloses(auctionFactory);
    }

    public Main() throws Exception {

        startUserInterface();
    }

    private void registerUserEventListener(final AuctionHouse auctionFactory) {
        ui.addUserRequestListener(new SnipersLauncher(auctionFactory, snipers));
    }

}