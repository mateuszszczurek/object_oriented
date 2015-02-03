package endtoend;

import pl.com.sniper.auction.Main;
import pl.com.sniper.gui.MainWindow;

import static endtoend.FakeAuctionServer.XMPP_HOSTNAME;

public class ApplicationRunner {

    private static final String SNIPER_PASSWORD = "sniper";
    private static final String SNIPER_ID = "sniper";

    private AuctionSniperDriver driver;

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
        driver.showsSniperStatus(MainWindow.STATUS_JOINING);
    }

    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(Main.STATUS_LOST);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }
}
