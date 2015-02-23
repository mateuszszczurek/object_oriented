package pl.com.sniper.gui;

import pl.com.sniper.auction.Main;
import pl.com.sniper.auction.sniper.SniperListener;
import pl.com.sniper.auction.sniper.SniperSnapshot;
import pl.com.sniper.auction.sniper.SniperStatus;

import javax.swing.table.AbstractTableModel;

public class
        SnipersTableModel extends AbstractTableModel implements SniperListener {

    private static String[] STATUS_TEXT = {"Joining", "Winning",
            "Bidding", "Won", "Lost"};

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
        return Column.at(columnIndex).valueIn(sniperSnapshot);
    }

    public static String stateFor(SniperStatus status) {
        return STATUS_TEXT[status.ordinal()];
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    @Override
    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        this.sniperSnapshot = sniperSnapshot;
        fireTableRowsUpdated(0, 0);
    }

}
