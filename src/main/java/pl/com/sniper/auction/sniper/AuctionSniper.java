package pl.com.sniper.auction.sniper;

import pl.com.sniper.auction.events.AuctionEventListener;

public class AuctionSniper implements AuctionEventListener {

    private Auction auction;
    private SniperListener sniperListener;

    public AuctionSniper(Auction auction, SniperListener sniperListener) {
        this.auction = auction;
        this.sniperListener = sniperListener;
    }

    @Override
    public void onAuctionClosed() {
        sniperListener.sniperLost();
    }

    @Override
    public void currentPrice(int currentPrice, int increment) {
        auction.bid(currentPrice + increment);
        sniperListener.sniperBidding();
    }

}
