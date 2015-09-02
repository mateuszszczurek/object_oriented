package pl.com.sniper.auction.sniper;

import pl.com.sniper.auction.events.AuctionEventListener;

public interface Auction {

    void bid(int howMuch);
    void join();

    void addAuctionEventListener(AuctionEventListener listener);

}
