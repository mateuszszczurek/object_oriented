package pl.com.sniper.auction;

import pl.com.sniper.auction.sniper.SniperListener;
import pl.com.sniper.auction.sniper.SniperSnapshot;

public interface PortfolioListener extends SniperListener {

    void sniperAdded(SniperSnapshot auctionSniper);

}
