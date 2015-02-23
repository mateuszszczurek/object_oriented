package endtoend;

import pl.com.sniper.auction.Main;
import pl.com.sniper.auction.sniper.Auction;
import pl.com.sniper.auction.sniper.SniperStatus;
import pl.com.sniper.gui.MainWindow;

import java.util.Arrays;
import java.util.stream.Stream;

import static endtoend.FakeAuctionServer.XMPP_HOSTNAME;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static pl.com.sniper.gui.SnipersTableModel.stateFor;

public class ApplicationRunner {

    private static final String SNIPER_PASSWORD = "sniper";
    private static final String SNIPER_ID = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@szczurki-komputer/Auction";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer ... auctions) {
        Thread thread = new Thread("Test Application") {

            @Override
            public void run() {
                try {
                    Main.main(args(auctions));
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

        for (FakeAuctionServer auction : auctions) {
            driver.showsSniperStatus(auction.getItemId(), 0, 0, stateFor(SniperStatus.JOINING));
        }
//        stream(auctions).forEach(auction -> driver.showsSniperStatus(auction.getItemId(), 0, 0, stateFor(SniperStatus.JOINING)));

    }

    private String[] args(FakeAuctionServer[] auctions) {
        String[] auctionNames = stream(auctions)
                .map(auction -> auction.getItemId())
                .toArray(size -> new String[size]);

        String[] args = new String[auctionNames.length + 3];
        args[0] = XMPP_HOSTNAME;
        args[1] = SNIPER_ID;
        args[2] = SNIPER_PASSWORD;
        for (int i = 0; i < auctionNames.length; i++) {
            args[3 + i] = auctionNames[i];
        }

        return args;
    }

    public void showsSniperHasLostAuction(FakeAuctionServer auction, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastBid, lastBid, stateFor(SniperStatus.LOST));
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
}
