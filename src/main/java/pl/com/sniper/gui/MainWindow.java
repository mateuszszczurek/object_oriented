package pl.com.sniper.gui;

import pl.com.sniper.auction.sniper.SniperSnapshot;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public static final String MAIN_WINDOW_NAME = "AuctionSniper";
    public static final String SNIPERS_TABLE_NAME = "Snipers table";
    public static final String JOIN_BUTTON_NAME = "Join";
    public static final String NEW_ITEM_ID_NAME = "Auction Id";

    private final SnipersTableModel snipers;

    public MainWindow(SnipersTableModel snipers) throws HeadlessException {
        super(MAIN_WINDOW_NAME);
        this.snipers = snipers;
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable(), makeControls());
        makeControls();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel makeControls() {
        JPanel controls = new JPanel(new FlowLayout());
        final JTextField itemIdField = new JTextField();
        itemIdField.setColumns(25);
        itemIdField.setName(NEW_ITEM_ID_NAME);
        controls.add(itemIdField);

        JButton joinAuctionButton  = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);
        controls.add(joinAuctionButton);

        return controls;
    }

    private void fillContentPane(JTable snipersTableModel, JPanel controls) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTableModel), BorderLayout.CENTER);
        contentPane.add(controls);
    }

    private JTable makeSnipersTable() {
        final JTable table = new JTable(snipers);
        table.setName(SNIPERS_TABLE_NAME);
        return table;
    }

}
