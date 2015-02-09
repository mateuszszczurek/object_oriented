package pl.com.sniper.auction.events;

public interface AuctionEventListener {

    void onAuctionClosed();

    void currentPrice(int currentPrice, int increment);
}
