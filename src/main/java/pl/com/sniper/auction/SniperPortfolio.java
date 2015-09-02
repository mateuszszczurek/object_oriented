package pl.com.sniper.auction;

import pl.com.sniper.auction.sniper.AuctionSniper;
import pl.com.sniper.auction.sniper.SniperSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SniperPortfolio implements SniperCollector {

    private List<PortfolioListener> portfolioListeners = new ArrayList<>();
    private List<AuctionSniper> snipers = new ArrayList<>();

    @Override
    public void addSniper(AuctionSniper auctionSniper) {
        this.snipers.add(auctionSniper);
        auctionSniper.addSniperListener(new SwingThreadSniperListener(this));
        portfolioListeners.forEach(listener -> listener.sniperAdded(auctionSniper.getSnapshot()));
    }

    @Override
    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        this.portfolioListeners.forEach(listener -> listener.sniperStateChanged(sniperSnapshot));
    }

    public void registerPortfolioListener(PortfolioListener portfolio) {
        this.portfolioListeners.add(portfolio);
    }
}
