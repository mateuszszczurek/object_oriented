package pl.com.sniper.auction;

import pl.com.sniper.auction.sniper.SniperListener;
import pl.com.sniper.auction.sniper.SniperSnapshot;

import javax.swing.*;

public class SwingThreadSniperListener implements SniperListener {

    private final SniperCollector snipers;

    public SwingThreadSniperListener(SniperCollector snipers) {
        this.snipers = snipers;
    }

    @Override
    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        SwingUtilities.invokeLater(() -> snipers.sniperStateChanged(sniperSnapshot));
    }
}
