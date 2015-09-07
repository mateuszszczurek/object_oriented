package pl.com.sniper.auction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import pl.com.sniper.auction.sniper.Auction;
import pl.com.sniper.auction.sniper.AuctionSniper;

import static org.mockito.Mockito.*;

public class SnipersLauncherTest {


    private final Auction auction = mock(Auction.class);
    private final SniperCollector sniperCollector = mock(SniperCollector.class);
    private final AuctionHouse auctionHouse = mock(AuctionHouse.class);

    @Test
    public void add_new_sniper_to_collector_and_then_joins_auction() {
        final String itemId = "item 123";

        when(auctionHouse.auctionFor(itemId)).thenReturn(auction);

        SnipersLauncher snipersLauncher = new SnipersLauncher(auctionHouse, sniperCollector);

        snipersLauncher.joinAuction(new Item(Integer.MAX_VALUE, itemId));

        verify(auctionHouse).auctionFor(itemId);
        verify(auction).addAuctionEventListener(argThat(sniperForItem(new Item(Integer.MAX_VALUE, itemId))));
        verify(sniperCollector).addSniper(argThat(sniperForItem(new Item(Integer.MAX_VALUE, itemId))));
        verify(auction).join();

    }

    private Matcher<AuctionSniper> sniperForItem(Item theItem) {
        return new TypeSafeMatcher<AuctionSniper>() {

            @Override
            public void describeTo(Description description) {

            }

            @Override
            protected boolean matchesSafely(AuctionSniper item) {
                return item.getSnapshot().item.equals(theItem);
            }
        };
    }

}