package pl.com.sniper.gui;

import pl.com.sniper.auction.SniperCollector;
import pl.com.sniper.auction.SwingThreadSniperListener;
import pl.com.sniper.auction.sniper.AuctionSniper;
import pl.com.sniper.auction.sniper.SniperListener;
import pl.com.sniper.auction.sniper.SniperSnapshot;
import pl.com.sniper.auction.sniper.SniperStatus;
import pl.com.sniper.mistakes.Defect;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class SnipersTableModel extends AbstractTableModel implements SniperListener, SniperCollector {

    private static String[] STATUS_TEXT = {"Joining", "Winning",
            "Bidding", "Won", "Lost"};

    private List<AuctionSniper> notToBeGCd = new ArrayList<>();
    private List<SniperSnapshot> sniperSnapshot = new ArrayList<>();

    @Override
    public int getRowCount() {
        return sniperSnapshot.size();
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SniperSnapshot sniper = sniperSnapshot.get(rowIndex);
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
        this.sniperSnapshot.set(rowNo, sniperSnapshot);
        fireTableRowsUpdated(rowNo, 0);
    }

    private int getRowNo(SniperSnapshot sniperSnapshot) {
        for (int i = 0; i < this.sniperSnapshot.size(); i++) {

            if (this.sniperSnapshot.get(i).isSameItemAs(sniperSnapshot)) {
                return i;
            }

        }
        throw new Defect();
    }

    public void addSniper(AuctionSniper auctionSniper) {
        this.notToBeGCd.add(auctionSniper);
        addSniperSnapshot(auctionSniper.getSnapshot());
        auctionSniper.addSniperListener(new SwingThreadSniperListener(this));
    }

    private void addSniperSnapshot(SniperSnapshot sniperSnapshot) {
        this.sniperSnapshot.add(sniperSnapshot);
        fireTableRowsUpdated(this.sniperSnapshot.size() - 1, 0);
    }
}
