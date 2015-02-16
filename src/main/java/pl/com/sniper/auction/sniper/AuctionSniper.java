package pl.com.sniper.auction.sniper;

import pl.com.sniper.auction.events.AuctionEventListener;

import static pl.com.sniper.auction.events.AuctionEventListener.PriceSource.FromOtherBidder;
import static pl.com.sniper.auction.events.AuctionEventListener.PriceSource.FromSniper;

public class AuctionSniper implements AuctionEventListener {

    private Auction auction;
    private SniperListener sniperListener;

    private boolean sniperWinning;

    public AuctionSniper(Auction auction, SniperListener sniperListener) {
        this.auction = auction;
        this.sniperListener = sniperListener;
    }

    @Override
    public void onAuctionClosed() {
        if(sniperWinning) {
            sniperListener.sniperWon();
        } else {
            sniperListener.sniperLost();
        }
    }

    @Override
    public void currentPrice(int currentPrice, int increment, PriceSource priceSource) {
        if(FromOtherBidder.equals(priceSource)) {
            sniperWinning = false;
            auction.bid(currentPrice + increment);
            sniperListener.sniperBidding();
        } else if (FromSniper.equals(priceSource)) {
            sniperWinning = true;
            sniperListener.sniperWinning();
        }
    }

}
