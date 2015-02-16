package pl.com.sniper.auction.sniper;

import org.junit.Test;
import pl.com.sniper.auction.events.AuctionEventListener;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class AuctionSniperTest {

    private final Auction auction = mock(Auction.class);
    private final SniperListener sniperListener = mock(SniperListener.class);

    private final AuctionSniper auctionSniper = new AuctionSniper(auction, sniperListener);

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {

        auctionSniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);

        verify(sniperListener).sniperWinning();
        verifyZeroInteractions(auction);
    }

    @Test
    public void reportsIsBiddingWhenCurrentPriceComesFromOtherBidder() {

        auctionSniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromOtherBidder);

        verify(auction).bid(123 + 45);
        verify(sniperListener).sniperBidding();
    }

    @Test
    public void reportsLostIfAuctionClosesImmediately() {

        auctionSniper.onAuctionClosed();

        verify(sniperListener).sniperLost();
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {

        auctionSniper.currentPrice(123, 12, AuctionEventListener.PriceSource.FromOtherBidder);
        auctionSniper.onAuctionClosed();

        verify(sniperListener).sniperLost();
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {

        auctionSniper.currentPrice(123, 12, AuctionEventListener.PriceSource.FromSniper);
        auctionSniper.onAuctionClosed();

        verify(sniperListener).sniperWon();
    }

}