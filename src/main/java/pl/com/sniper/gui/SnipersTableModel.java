package pl.com.sniper.gui;

import pl.com.sniper.auction.PortfolioListener;
import pl.com.sniper.auction.sniper.SniperSnapshot;
import pl.com.sniper.auction.sniper.SniperStatus;
import pl.com.sniper.mistakes.Defect;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class SnipersTableModel extends AbstractTableModel implements PortfolioListener {

    private static String[] STATUS_TEXT = {"Joining", "Winning",
            "Bidding", "Loosing", "Failed", "Won", "Lost"};

    private List<SniperSnapshot> auctionSniper = new ArrayList<>();

    @Override
    public int getRowCount() {
        return auctionSniper.size();
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SniperSnapshot sniper = auctionSniper.get(rowIndex);
        return Column.at(columnIndex).valueIn(sniper);
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
        int rowNo = getRowNo(sniperSnapshot);
        this.auctionSniper.set(rowNo, sniperSnapshot);
        fireTableRowsUpdated(rowNo, 0);
    }

    private int getRowNo(SniperSnapshot sniperSnapshot) {
        for (int i = 0; i < this.auctionSniper.size(); i++) {

            if (this.auctionSniper.get(i).isSameItemAs(sniperSnapshot)) {
                return i;
            }

        }
        throw new Defect();
    }

    @Override
    public void sniperAdded(SniperSnapshot sniperSnapshot) {
        addSniperSnapshot(sniperSnapshot);
    }

    private void addSniperSnapshot(SniperSnapshot sniperSnapshot) {
        this.auctionSniper.add(sniperSnapshot);
        fireTableRowsUpdated(this.auctionSniper.size() - 1, 0);
    }
}
