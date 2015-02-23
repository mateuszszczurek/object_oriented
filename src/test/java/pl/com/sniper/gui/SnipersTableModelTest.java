package pl.com.sniper.gui;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import pl.com.sniper.auction.sniper.SniperSnapshot;
import pl.com.sniper.auction.sniper.SniperStatus;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static pl.com.sniper.gui.SnipersTableModel.stateFor;

public class SnipersTableModelTest {

    private TableModelListener listener = mock(TableModelListener.class);
    private final SnipersTableModel snipersTableModel = new SnipersTableModel();

    @Before
    public void attachModelListener() {
        snipersTableModel.addTableModelListener(listener);
    }

    @Test
    public void hasEnoughColumns() {
        assertThat(snipersTableModel.getColumnCount(), equalTo(Column.values().length));
    }

    @Test
    public void setsSniperValuesInColumns() {
        snipersTableModel.sniperStateChanged(new SniperSnapshot("item id", 1000, 100, SniperStatus.BIDDING));

        assertColumnEquals(Column.ITEM_IDENTIFIER, "item id");
        assertColumnEquals(Column.LAST_PRICE, 1000);
        assertColumnEquals(Column.LAST_BID, 100);
        assertColumnEquals(Column.SNIPER_STATE, stateFor(SniperStatus.BIDDING));

        verify(listener).tableChanged(argThat(aRowChangedEvent()));

    }

    @Test
    public void setUpColumnHeaders() {
        for(Column column : Column.values()) {
            assertThat(snipersTableModel.getColumnName(column.ordinal()), equalTo(column.name));
        }
    }

    private Matcher<TableModelEvent> aRowChangedEvent() {
        return samePropertyValuesAs(new TableModelEvent(snipersTableModel, 0));
    }

    private void assertColumnEquals(Column column, Object expected) {
        final int rowIndex = 0;
        final int columnIndex = column.ordinal();
        assertEquals(expected, snipersTableModel.getValueAt(rowIndex, columnIndex));
    }

}