package pl.com.sniper.gui;

import pl.com.sniper.auction.Main;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainWindow extends JFrame {

    public static final String MAIN_WINDOW_NAME = "AuctionSniper";

    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_JOINING = "Joining";

    private final JLabel sniperStatus = createLabel(STATUS_JOINING);

    public MainWindow() throws HeadlessException {
        super("Auction Sniper");
        add(sniperStatus);
        pack();
        setName(MAIN_WINDOW_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(Main.SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }

    public void showStatus(String status){
        sniperStatus.setText(status);
    }
}