package pl.com.sniper.auction.events;

import java.util.EventListener;

public interface AuctionEventListener extends EventListener {

    enum PriceSource {
        FromSniper, FromOtherBidder
    }

    void onAuctionClosed();

    void currentPrice(int currentPrice, int increment, PriceSource priceSource);

}
