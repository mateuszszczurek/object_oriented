package pl.com.sniper.auction;

import org.jivesoftware.smack.XMPPConnection;
import pl.com.sniper.auction.sniper.SniperListener;
import pl.com.sniper.auction.sniper.SniperSnapshot;
import pl.com.sniper.gui.MainWindow;
import pl.com.sniper.gui.SnipersTableModel;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.SwingUtilities.invokeAndWait;

public class SwingThreadSniperListener implements SniperListener {

    private final SnipersTableModel snipers;

    public SwingThreadSniperListener(SnipersTableModel snipers) {
        this.snipers = snipers;
    }

    @Override
    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        SwingUtilities.invokeLater(() -> snipers.sniperStateChanged(sniperSnapshot));
    }
}
