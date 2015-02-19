package pl.com.sniper.auction.sniper;

import org.junit.Test;
import pl.com.sniper.auction.events.AuctionEventListener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static pl.com.sniper.auction.sniper.SniperStatus.*;

public class AuctionSniperTest {

    private static final String ITEM_ID = "Item id";

    private final Auction auction = mock(Auction.class);
    private final SniperListener sniperListener = mock(SniperListener.class);

    private final AuctionSniper auctionSniper = new AuctionSniper(ITEM_ID, auction, sniperListener);

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        int currentPrice = 100;
        int increment = 45;
        int bid = currentPrice + increment;

        auctionSniper.currentPrice(currentPrice, increment, AuctionEventListener.PriceSource.FromOtherBidder);
        auctionSniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);

        verify(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 100, 145, SniperStatus.BIDDING));
        verify(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 123, SniperStatus.WINNING));
        verify(auction).bid(bid);
    }

    @Test
    public void reportsIsBiddingWhenCurrentPriceComesFromOtherBidder() {

        int currentPrice = 123;
        int increment = 45;
        int bid = currentPrice + increment;

        auctionSniper.currentPrice(currentPrice, increment, AuctionEventListener.PriceSource.FromOtherBidder);

        verify(auction).bid(bid);
        verify(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, currentPrice, bid, BIDDING));
    }

    @Test
    public void reportsLostIfAuctionClosesImmediately() {

        auctionSniper.onAuctionClosed();

        verify(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 0, 0, LOST));
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {

        auctionSniper.currentPrice(123, 12, AuctionEventListener.PriceSource.FromOtherBidder);
        auctionSniper.onAuctionClosed();

        verify(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 135, BIDDING));
        verify(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 123, LOST));
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {

        auctionSniper.currentPrice(123, 12, AuctionEventListener.PriceSource.FromSniper);
        auctionSniper.onAuctionClosed();

        verify(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 123, WINNING));
        verify(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 123, WON));
    }

}