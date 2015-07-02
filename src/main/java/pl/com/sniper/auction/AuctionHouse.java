package pl.com.sniper.auction;

import pl.com.sniper.auction.sniper.Auction;

public interface AuctionHouse {
    Auction auctionFor(String itemId);
}
