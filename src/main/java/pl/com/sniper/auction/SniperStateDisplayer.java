package pl.com.sniper.auction;

import org.jivesoftware.smack.XMPPConnection;
import pl.com.sniper.auction.sniper.SniperListener;
import pl.com.sniper.gui.MainWindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.SwingUtilities.invokeAndWait;

public class SniperStateDisplayer implements SniperListener {

    private MainWindow ui;

    @Override
    public void sniperLost() {
        ui.showStatus(MainWindow.STATUS_LOST);
    }

    @Override
    public void sniperBidding() {
        ui.showStatus(MainWindow.STATUS_BIDDING);
    }

    @Override
    public void sniperWon() {

    }

    public void startUserInterface() throws Exception {
        invokeAndWait(() -> ui = new MainWindow());
    }

    public void disconnectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }
}
