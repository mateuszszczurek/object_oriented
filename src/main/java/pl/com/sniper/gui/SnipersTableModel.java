package pl.com.sniper.gui;

import pl.com.sniper.auction.Main;
import pl.com.sniper.auction.sniper.SniperSnapshot;
import pl.com.sniper.auction.sniper.SniperStatus;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel {

    private static String[] STATUS_TEXT = {MainWindow.STATUS_JOINING, MainWindow.STATUS_WINNING,
            MainWindow.STATUS_BIDDING, MainWindow.STATUS_WON, MainWindow.STATUS_LOST};

    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperStatus.JOINING);
    private SniperSnapshot sniperSnapshot = STARTING_UP;

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return sniperSnapshot.itemId;
            case LAST_PRICE:
                return sniperSnapshot.lastPrice;
            case LAST_BID:
                return sniperSnapshot.lastBid;
            case SNIPER_STATE:
                return STATUS_TEXT[sniperSnapshot.status.ordinal()];
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);
        }

    }

    public void setSniperStatus(String newStatusText) {
        fireTableRowsUpdated(0,0);
    }

    public void updateSniperState(SniperSnapshot sniperSnapshot) {
        this.sniperSnapshot = sniperSnapshot;
        fireTableRowsUpdated(0, 0);
    }

}
