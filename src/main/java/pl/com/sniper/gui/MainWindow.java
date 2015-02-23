package pl.com.sniper.gui;

import pl.com.sniper.auction.sniper.SniperSnapshot;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public static final String MAIN_WINDOW_NAME = "AuctionSniper";
    public static final String SNIPERS_TABLE_NAME = "Snipers table";

    private final SnipersTableModel snipers;

    public MainWindow(SnipersTableModel snipers) throws HeadlessException {
        super(MAIN_WINDOW_NAME);
        this.snipers = snipers;
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTableModel) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTableModel), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        final JTable table = new JTable(snipers);
        table.setName(SNIPERS_TABLE_NAME);
        return table;
    }

}
