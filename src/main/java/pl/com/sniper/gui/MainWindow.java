package pl.com.sniper.gui;

import pl.com.sniper.auction.SniperPortfolio;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {

    public static final String MAIN_WINDOW_NAME = "AuctionSniper";
    public static final String SNIPERS_TABLE_NAME = "Snipers table";
    public static final String JOIN_BUTTON_NAME = "Join";
    public static final String NEW_ITEM_ID_NAME = "Auction Id";

    private final List<UserRequestListener> userEventListeners = new ArrayList<>();

    public MainWindow(SniperPortfolio snipers) throws HeadlessException {
        super(MAIN_WINDOW_NAME);
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable(snipers), makeControls());
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
        joinAuctionButton.addActionListener(
                 e -> notifyUsers(itemIdField.getText())

        );

        controls.add(joinAuctionButton);

        return controls;
    }

    private void fillContentPane(JTable snipersTableModel, JPanel controls) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(controls, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(snipersTableModel), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable(SniperPortfolio portfolio) {
        SnipersTableModel snipersTableModel = new SnipersTableModel();
        portfolio.registerPortfolioListener(snipersTableModel);
        final JTable table = new JTable(snipersTableModel);
        table.setName(SNIPERS_TABLE_NAME);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return table;
    }

    private void notifyUsers(String auctionItem) {
        userEventListeners.forEach(listener -> listener.joinAuction(auctionItem));
    }

    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userEventListeners.add(userRequestListener);
    }
}
