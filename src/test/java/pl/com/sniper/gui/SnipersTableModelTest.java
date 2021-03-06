package pl.com.sniper.gui;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import pl.com.sniper.auction.Item;
import pl.com.sniper.auction.sniper.SniperSnapshot;
import pl.com.sniper.mistakes.Defect;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

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
        SniperSnapshot joining = SniperSnapshot.joining(new Item(Integer.MAX_VALUE, "item id"));
        SniperSnapshot bidding = joining.bidding(1000, 100);

        snipersTableModel.sniperAdded(joining);
        snipersTableModel.sniperStateChanged(bidding);

        ArgumentCaptor<TableModelEvent> captor = ArgumentCaptor.forClass(TableModelEvent.class);
        verify(listener, times(2)).tableChanged(captor.capture());

        assertThat(captor.getValue(), is(anyInsertionEvent()));
        assertThat(captor.getValue(), aChangeInRow(0));

    }

    @Test(expected = Defect.class)
    public void throwsDefectIfNoExistingSniperForAnUpdate() {
        snipersTableModel.sniperStateChanged(SniperSnapshot.joining(new Item(Integer.MAX_VALUE, "random id")));
    }

    private Matcher<TableModelEvent> aChangeInRow(int rowNo) {
        return new TypeSafeMatcher<TableModelEvent>() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            protected boolean matchesSafely(TableModelEvent item) {
                return item.getFirstRow() == rowNo;
            }
        };
    }

    private Matcher<TableModelEvent> anyInsertionEvent() {
        return any(TableModelEvent.class);
    }

    @Test
    public void setUpColumnHeaders() {
        for (Column column : Column.values()) {
            assertThat(snipersTableModel.getColumnName(column.ordinal()), equalTo(column.name));
        }
    }

    @Test
    public void notifiesListenerWhenAddingASniper() {
        SniperSnapshot sniperSnapshot = SniperSnapshot.joining(new Item(Integer.MAX_VALUE, "item123"));

        snipersTableModel.sniperAdded(sniperSnapshot);

        assertThat(snipersTableModel.getRowCount(), equalTo(1));
        assertRowMatchesSnapshot(0, sniperSnapshot);
        aRowChangedEvent();
    }

    @Test
    public void holdsSniperInAdditionOrder() {
        SniperSnapshot sniperSnapshot = SniperSnapshot.joining(new Item(Integer.MAX_VALUE, "item123"));
        SniperSnapshot secondSniperSnapshot = SniperSnapshot.joining(new Item(Integer.MAX_VALUE, "item234"));

        snipersTableModel.sniperAdded(sniperSnapshot);
        snipersTableModel.sniperAdded(secondSniperSnapshot);

        assertEquals("item123", cellValue(0, Column.ITEM_IDENTIFIER));
        assertEquals("item234", cellValue(1, Column.ITEM_IDENTIFIER));
    }

    @Test
    public void updateCorrectRowForSniper() {
        SniperSnapshot sniperSnapshot = SniperSnapshot.joining(new Item(Integer.MAX_VALUE, "item123"));
        SniperSnapshot secondSniperSnapshot = SniperSnapshot.joining(new Item(Integer.MAX_VALUE, "item234"));

        snipersTableModel.sniperAdded(sniperSnapshot);
        snipersTableModel.sniperAdded(secondSniperSnapshot);

        verify(listener, times(1)).tableChanged(argThat(aChangeInRow(0)));
        verify(listener, times(1)).tableChanged(argThat(aChangeInRow(1)));

    }

    private Object cellValue(int row, Column column) {
        return snipersTableModel.getValueAt(row, column.ordinal());
    }

    private void assertRowMatchesSnapshot(int whichRow, SniperSnapshot whatState) {
        for (Column column : Column.values()) {
            int columnId = column.ordinal();
            assertThat(snipersTableModel.getValueAt(whichRow, columnId), equalTo(column.valueIn(whatState)));
        }
    }

    private Matcher<TableModelEvent> aRowChangedEvent() {
        return samePropertyValuesAs(new TableModelEvent(snipersTableModel, 0));
    }

}