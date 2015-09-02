package pl.com.sniper.auction;

import pl.com.sniper.auction.sniper.Auction;
import pl.com.sniper.auction.sniper.AuctionSniper;
import pl.com.sniper.gui.UserRequestListener;

import static pl.com.sniper.auction.sniper.SniperSnapshot.joining;

public class SnipersLauncher implements UserRequestListener {

    private final AuctionHouse auctionHouse;
    private final SniperCollector collector;

    public SnipersLauncher(AuctionHouse auctionHouse, SniperCollector collector) {
        this.auctionHouse = auctionHouse;
        this.collector = collector;
    }

    @Override
    public void joinAuction(String itemId) {
        Auction auction = auctionHouse.auctionFor(itemId);
        AuctionSniper sniper = new AuctionSniper(joining(itemId), auction);
        auction.addAuctionEventListener(sniper);

        collector.addSniper(sniper);

        auction.join();
    }
}
