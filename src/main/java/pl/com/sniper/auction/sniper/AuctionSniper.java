package pl.com.sniper.auction.sniper;

import pl.com.sniper.auction.events.AuctionEventListener;

import java.util.ArrayList;
import java.util.List;

import static pl.com.sniper.auction.events.AuctionEventListener.PriceSource.FromOtherBidder;
import static pl.com.sniper.auction.events.AuctionEventListener.PriceSource.FromSniper;

public class AuctionSniper implements AuctionEventListener {

    private final Auction auction;
    private final List<SniperListener> sniperListener = new ArrayList<>();

    private SniperSnapshot snapshot;

    public AuctionSniper(SniperSnapshot snapshot, Auction auction) {
        this.auction = auction;
        this.snapshot = snapshot;
        notifyChange();
    }

    @Override
    public void onAuctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        int bid = price + increment;

        if (!snapshot.canContinueBidding(bid)) {
            snapshot = snapshot.loosing(price, bid);
        } else if (FromOtherBidder.equals(priceSource)) {
            auction.bid(bid);
            snapshot = snapshot.bidding(price, bid);
        } else if (FromSniper.equals(priceSource)) {
            snapshot = snapshot.winning(price);
        }

        notifyChange();
    }

    private void notifyChange() {
        sniperListener.forEach(listener -> listener.sniperStateChanged(snapshot));
    }

    public SniperSnapshot getSnapshot() {
        return snapshot;
    }

    public void addSniperListener(SniperListener sniperListener) {
        this.sniperListener.add(sniperListener);
    }
}
