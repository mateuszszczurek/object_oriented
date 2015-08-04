package pl.com.sniper.auction;

import pl.com.sniper.auction.sniper.AuctionSniper;
import pl.com.sniper.auction.sniper.SniperSnapshot;

public interface SniperCollector {

    void addSniper(AuctionSniper snapshot);

    void sniperStateChanged(SniperSnapshot sniperSnapshot);

}
