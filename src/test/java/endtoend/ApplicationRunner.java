package endtoend;

import pl.com.sniper.auction.Main;
import pl.com.sniper.auction.sniper.SniperStatus;
import pl.com.sniper.gui.MainWindow;

import static endtoend.FakeAuctionServer.XMPP_HOSTNAME;
import static pl.com.sniper.gui.SnipersTableModel.stateFor;

public class ApplicationRunner {

    private static final String SNIPER_PASSWORD = "sniper";
    private static final String SNIPER_ID = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@szczurki-komputer/Auction";

    private AuctionSniperDriver driver;
    private String auctionId;

    public void startBiddingIn(final FakeAuctionServer auction) {
        Thread thread = new Thread("Test Application") {

            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
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
        driver.showsSniperStatus(stateFor(SniperStatus.JOINING));

        this.auctionId = auction.getItemId();
    }

    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(stateFor(SniperStatus.LOST));
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
        driver.showsSniperStatus(auctionId, lastPrice, lastBid, stateFor(SniperStatus.BIDDING));
    }

    public void showSniperHasWonAuction(int lastPrice) {
        driver.showsSniperStatus(auctionId, lastPrice, lastPrice,  stateFor(SniperStatus.WON));
    }

    public void hasShownSniperIsWinning(int winningBid) {
        driver.showsSniperStatus(auctionId, winningBid, winningBid, stateFor(SniperStatus.WINNING));
    }
}
