package endtoend;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.ComponentSelector;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTableHeaderDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import pl.com.sniper.gui.MainWindow;
import pl.com.sniper.auction.Main;

import javax.swing.table.JTableHeader;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.equalTo;

public class AuctionSniperDriver extends JFrameDriver{

    public AuctionSniperDriver(int timeoutInMillis) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame(
                        named(MainWindow.MAIN_WINDOW_NAME),
                        showingOnScreen()),
                        new AWTEventQueueProber(timeoutInMillis, 100));
    }

    public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String status) {
        JTableDriver tableDriver = new JTableDriver(this);

        tableDriver.hasRow(
                matching(withLabelText(itemId), withLabelText(valueOf(lastPrice)),
                        withLabelText(valueOf(lastBid)), withLabelText(status)));

    }

    public void hasColumnTitles() {
        JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
        headers.hasHeaders(matching(withLabelText("Item"), withLabelText("Last Price"),
                withLabelText("Last Bid"), withLabelText("State")));
    }
}
