package integration;

import com.objogate.wl.swing.probe.ValueMatcherProbe;
import endtoend.AuctionSniperDriver;
import org.junit.Before;
import org.junit.Test;
import pl.com.sniper.gui.MainWindow;
import pl.com.sniper.gui.SnipersTableModel;

import static org.hamcrest.Matchers.equalTo;

public class MainWindowTest {

    private final SnipersTableModel tableModel = new SnipersTableModel();
    private final MainWindow mainWindow = new MainWindow(tableModel);
    private final AuctionSniperDriver sniperDriver = new AuctionSniperDriver(100);

    @Before
    public void setUpWindowClicker() {
        System.setProperty("com.objogate.wl.keyboard", "GB");
    }

    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<String> buttonProbe = new ValueMatcherProbe<>(equalTo("an item id"), "join request");

        mainWindow.addUserRequestListener(
                buttonProbe::setReceivedValue
        );

        sniperDriver.startBiddingFor("an item id");
        sniperDriver.check(buttonProbe);
    }

}