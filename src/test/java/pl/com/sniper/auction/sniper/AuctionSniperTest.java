package pl.com.sniper.auction.sniper;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AuctionSniperTest {

    private final Auction auction = mock(Auction.class);
    private final SniperListener sniperListener = mock(SniperListener.class);

    private final AuctionSniper sniper = new AuctionSniper(auction, sniperListener);

    @Test
    public void reportsLostWhenAuctionCloses() {

        sniper.onAuctionClosed();

        verify(sniperListener).sniperLost();

    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {

        final int price = 1001;
        final int increment = 25;

        sniper.currentPrice(price, increment);

        verify(sniperListener).sniperBidding();
        verify(auction).bid(price + increment);

    }

}