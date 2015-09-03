package endtoend;

import pl.com.sniper.auction.Main;
import pl.com.sniper.auction.sniper.SniperStatus;
import pl.com.sniper.gui.MainWindow;

import static endtoend.FakeAuctionServer.XMPP_HOSTNAME;
import static pl.com.sniper.gui.SnipersTableModel.stateFor;

public class ApplicationRunner {

    private static final String SNIPER_PASSWORD = "sniper";
    private static final String SNIPER_ID = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@mateusz-pc/Auction";

    private AuctionSniperDriver driver;

    public void startBiddingWithStopPrice(int stopPrice, FakeAuctionServer ... auctions) {
        startSniper();

        for (FakeAuctionServer auction : auctions) {
            final String itemId = auction.getItemId();
            driver.startBiddingFor(itemId, stopPrice);
            driver.showsSniperStatus(auction.getItemId(), 0, 0, stateFor(SniperStatus.JOINING));
        }
    }


    private void startSniper() {
        Thread thread = new Thread("Test Application") {

            @Override
            public void run() {
                try {
                    Main.main(args());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.setDaemon(true);
        thread.start();

        driver = new AuctionSniperDriver(1000);
        driver.hasTitle(MainWindow.MAIN_WINDOW_NAME);
        driver.hasColumnTitles();
    }

    private String[] args() {

        String[] args = new String[3];
        args[0] = XMPP_HOSTNAME;
        args[1] = SNIPER_ID;
        args[2] = SNIPER_PASSWORD;

        return args;
    }

    public void showsSniperHasLostAuction(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, stateFor(SniperStatus.LOST));
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, stateFor(SniperStatus.BIDDING));
    }

    public void showSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice,  stateFor(SniperStatus.WON));
    }

    public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
        driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid, stateFor(SniperStatus.WINNING));
    }

    public void showsSniperIsLosing(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, stateFor(SniperStatus.LOOSING));
    }
}
