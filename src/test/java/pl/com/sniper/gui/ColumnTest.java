package pl.com.sniper.gui;

import org.junit.Test;
import pl.com.sniper.auction.Item;
import pl.com.sniper.auction.sniper.SniperSnapshot;
import pl.com.sniper.auction.sniper.SniperStatus;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ColumnTest {

    @Test
    public void columnsTest() {
        SniperSnapshot sniperSnapshot = new SniperSnapshot(new Item(100, "item id"), 100, 20, SniperStatus.BIDDING);

        assertThat(Column.ITEM_IDENTIFIER.valueIn(sniperSnapshot), equalTo("item id"));
        assertThat(Column.LAST_PRICE.valueIn(sniperSnapshot), equalTo(100));
        assertThat(Column.LAST_BID.valueIn(sniperSnapshot), equalTo(20));
        assertThat(Column.SNIPER_STATE.valueIn(sniperSnapshot), equalTo("Bidding"));
    }

    @Test
    public void variousStatusesToColumnsTest() {
        assertThat(Column.SNIPER_STATE.valueIn(snapshot(SniperStatus.JOINING)), equalTo("Joining"));
        assertThat(Column.SNIPER_STATE.valueIn(snapshot(SniperStatus.WINNING)), equalTo("Winning"));
        assertThat(Column.SNIPER_STATE.valueIn(snapshot(SniperStatus.BIDDING)), equalTo("Bidding"));
        assertThat(Column.SNIPER_STATE.valueIn(snapshot(SniperStatus.LOOSING)), equalTo("Loosing"));
        assertThat(Column.SNIPER_STATE.valueIn(snapshot(SniperStatus.FAILED)), equalTo("Failed"));
        assertThat(Column.SNIPER_STATE.valueIn(snapshot(SniperStatus.LOST)), equalTo("Lost"));
        assertThat(Column.SNIPER_STATE.valueIn(snapshot(SniperStatus.WON)), equalTo("Won"));
    }

    private SniperSnapshot snapshot(SniperStatus status) {
        return new SniperSnapshot(new Item(100, "item id"), 100, 20, status);
    }


}