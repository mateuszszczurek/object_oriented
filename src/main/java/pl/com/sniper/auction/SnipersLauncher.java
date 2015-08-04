package pl.com.sniper.auction;

import pl.com.sniper.auction.sniper.Auction;
import pl.com.sniper.auction.sniper.AuctionSniper;
import pl.com.sniper.auction.sniper.SniperListener;
import pl.com.sniper.gui.UserRequestListener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import static pl.com.sniper.auction.sniper.SniperSnapshot.joining;

public class SnipersLauncher implements UserRequestListener {

    private final AuctionHouse auctionHouse;
    private final SniperCollector snipers;

    @SuppressWarnings("unused")
    private Collection<Auction> notToBeGCd = new ArrayList<>();

    public SnipersLauncher(AuctionHouse auctionHouse, SniperCollector snipers) {
        this.auctionHouse = auctionHouse;
        this.snipers = snipers;
    }

    @Override
    public void joinAuction(String itemId) {
        try {
            joinAuction(itemId, auctionHouse);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void joinAuction(String itemId, AuctionHouse auctionFactory) throws InvocationTargetException, InterruptedException {
        Auction auction = auctionFactory.auctionFor(itemId);
        AuctionSniper sniper = new AuctionSniper(joining(itemId), auction);
        snipers.addSniper(sniper);
        SniperListener sniperListener = new SwingThreadSniperListener(snipers);
        sniper.addSniperListener(sniperListener);

        auction.addMessageListener(sniper);
        auction.join();

        notToBeGCd.add(auction);

    }

}
