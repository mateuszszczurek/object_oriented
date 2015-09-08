package pl.com.sniper.auction.sniper;

import org.junit.Before;
import org.junit.Test;
import pl.com.sniper.auction.Item;
import pl.com.sniper.auction.events.AuctionEventListener;

import static java.lang.Integer.MAX_VALUE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static pl.com.sniper.auction.sniper.SniperSnapshot.joining;
import static pl.com.sniper.auction.sniper.SniperStatus.*;

public class AuctionSniperTest {

    private static final String ITEM_ID = "Item id";

    private final Auction auction = mock(Auction.class);
    private final SniperListener sniperListener = mock(SniperListener.class);

    private final AuctionSniper auctionSniper = new AuctionSniper(joining(item()), auction);

    @Before
    public void setUp() throws Exception {
        auctionSniper.addSniperListener(sniperListener);
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        int currentPrice = 100;
        int increment = 45;
        int bid = currentPrice + increment;

        auctionSniper.currentPrice(currentPrice, increment, AuctionEventListener.PriceSource.FromOtherBidder);
        auctionSniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);

        verify(sniperListener).sniperStateChanged(new SniperSnapshot(item(), 100, 145, SniperStatus.BIDDING));
        verify(sniperListener).sniperStateChanged(new SniperSnapshot(item(), 123, 123, SniperStatus.WINNING));
        verify(auction).bid(bid);
    }

    @Test
    public void reportsIsBiddingWhenCurrentPriceComesFromOtherBidder() {

        int currentPrice = 123;
        int increment = 45;
        int bid = currentPrice + increment;

        auctionSniper.currentPrice(currentPrice, increment, AuctionEventListener.PriceSource.FromOtherBidder);

        verify(auction).bid(bid);
        verify(sniperListener).sniperStateChanged(new SniperSnapshot(item(), currentPrice, bid, BIDDING));
    }

    @Test
    public void reportsLostIfAuctionClosesImmediately() {

        auctionSniper.onAuctionClosed();

        verify(sniperListener).sniperStateChanged(new SniperSnapshot(item(), 0, 0, LOST));
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {

        auctionSniper.currentPrice(123, 12, AuctionEventListener.PriceSource.FromOtherBidder);
        auctionSniper.onAuctionClosed();

        verify(sniperListener).sniperStateChanged(new SniperSnapshot(item(), 123, 135, BIDDING));
        verify(sniperListener).sniperStateChanged(new SniperSnapshot(item(), 123, 135, LOST));
    }

    @Test
    public void reportsLostIfThePriceIsTooHighFromTheStart() {
        AuctionSniper auctionSniper = new AuctionSniper(joining(item(10, ITEM_ID)), auction);
        auctionSniper.addSniperListener(sniperListener);

        auctionSniper.currentPrice(100, 10, AuctionEventListener.PriceSource.FromOtherBidder);
        auctionSniper.onAuctionClosed();

        verify(sniperListener).sniperStateChanged(new SniperSnapshot(item(10, ITEM_ID), 100, 0, LOOSING));
        verify(sniperListener).sniperStateChanged(new SniperSnapshot(item(10, ITEM_ID), 100, 0, LOST));
    }

    @Test
    public void doesNotBidWhenThePriceGoesAboveStopPrice() {
        Item item = item(100, ITEM_ID);

        AuctionSniper auctionSniper = new AuctionSniper(joining(item), auction);
        auctionSniper.addSniperListener(sniperListener);

        auctionSniper.currentPrice(60, 10, AuctionEventListener.PriceSource.FromOtherBidder);
        auctionSniper.currentPrice(120, 10, AuctionEventListener.PriceSource.FromOtherBidder);
        auctionSniper.onAuctionClosed();

        verify(sniperListener).sniperStateChanged(new SniperSnapshot(item, 60, 70, BIDDING));
        verify(sniperListener).sniperStateChanged(new SniperSnapshot(item, 120, 70, LOOSING));
        verify(sniperListener).sniperStateChanged(new SniperSnapshot(item, 120, 70, LOST));
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {

        auctionSniper.currentPrice(123, 12, AuctionEventListener.PriceSource.FromSniper);
        auctionSniper.onAuctionClosed();

        verify(sniperListener).sniperStateChanged(new SniperSnapshot(item(), 123, 123, WINNING));
        verify(sniperListener).sniperStateChanged(new SniperSnapshot(item(), 123, 123, WON));
    }


    @Test
    public void whenAuctionFailsDisplayFailureStatus() {

        auctionSniper.auctionFailed();
        auctionSniper.onAuctionClosed();

        verify(sniperListener, times(2)).sniperStateChanged(new SniperSnapshot(item(), 0, 0, FAILED));
    }


    private Item item() {
        return item(MAX_VALUE, ITEM_ID);
    }

    private Item item(int stopPrice, String itemId) {
        return new Item(stopPrice, itemId);
    }

}