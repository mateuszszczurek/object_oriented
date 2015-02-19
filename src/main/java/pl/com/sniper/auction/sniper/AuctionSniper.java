package pl.com.sniper.auction.sniper;

import pl.com.sniper.auction.events.AuctionEventListener;

import static pl.com.sniper.auction.events.AuctionEventListener.PriceSource.FromOtherBidder;
import static pl.com.sniper.auction.events.AuctionEventListener.PriceSource.FromSniper;

public class AuctionSniper implements AuctionEventListener {

    private final Auction auction;
    private final SniperListener sniperListener;

    private SniperSnapshot snapshot;

    public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
        this.auction = auction;
        this.sniperListener = sniperListener;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    @Override
    public void onAuctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    @Override
    public void currentPrice(int currentPrice, int increment, PriceSource priceSource) {
        if(FromOtherBidder.equals(priceSource)) {
            int bid = currentPrice + increment;
            auction.bid(bid);
            snapshot = snapshot.bidding(currentPrice, bid);
        } else if (FromSniper.equals(priceSource)) {
            snapshot = snapshot.winning(currentPrice);
        }

        notifyChange();
    }

    private void notifyChange() {
        sniperListener.sniperStateChanged(snapshot);
    }

}
