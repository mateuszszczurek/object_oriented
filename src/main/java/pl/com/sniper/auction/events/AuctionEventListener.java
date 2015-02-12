package pl.com.sniper.auction.events;

public interface AuctionEventListener {

    enum PriceSource {
        FromSniper, FromOtherBidder
    }

    void onAuctionClosed();

    void currentPrice(int currentPrice, int increment, PriceSource priceSource);

}
