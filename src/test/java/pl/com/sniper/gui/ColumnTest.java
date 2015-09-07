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

    //todo add tests for other states


}