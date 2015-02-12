package pl.com.sniper.auction.sniper;

import org.junit.Test;
import pl.com.sniper.auction.events.AuctionEventListener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static pl.com.sniper.auction.events.AuctionEventListener.PriceSource.FromSniper;

public class XMPPAuctionSniperTest {

    private final Auction XMPPAuction = mock(Auction.class);
    private final SniperListener sniperListener = mock(SniperListener.class);

    private final AuctionSniper sniper = new AuctionSniper(XMPPAuction, sniperListener);

    @Test
    public void reportsLostWhenAuctionCloses() {

        sniper.onAuctionClosed();

        verify(sniperListener).sniperLost();

    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {

        final int price = 1001;
        final int increment = 25;

        sniper.currentPrice(price, increment, FromSniper);

        verify(sniperListener).sniperBidding();
        verify(XMPPAuction).bid(price + increment);

    }



}