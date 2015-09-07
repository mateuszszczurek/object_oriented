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
    public void joinAuction(Item item) {
        Auction auction = auctionHouse.auctionFor(item.getItemId());
        AuctionSniper sniper = new AuctionSniper(joining(item), auction);
        auction.addAuctionEventListener(sniper);

        collector.addSniper(sniper);

        auction.join();
    }
}
