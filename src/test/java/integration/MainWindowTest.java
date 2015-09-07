package integration;

import com.objogate.wl.swing.probe.ValueMatcherProbe;
import endtoend.AuctionSniperDriver;
import org.junit.Before;
import org.junit.Test;
import pl.com.sniper.auction.Item;
import pl.com.sniper.auction.SniperPortfolio;
import pl.com.sniper.gui.MainWindow;

import static org.hamcrest.Matchers.equalTo;

public class MainWindowTest {

    private final SniperPortfolio sniperPortfolio = new SniperPortfolio();
    private final MainWindow mainWindow = new MainWindow(sniperPortfolio);
    private final AuctionSniperDriver sniperDriver = new AuctionSniperDriver(100);

    @Before
    public void setUpWindowClicker() {
        System.setProperty("com.objogate.wl.keyboard", "GB");
    }

    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<Item> itemProbe = new ValueMatcherProbe<>(equalTo(new Item(100, "an item id")), "join request");

        mainWindow.addUserRequestListener(itemProbe::setReceivedValue);

        sniperDriver.startBiddingFor("an item id", 100);
        sniperDriver.check(itemProbe);
    }

}