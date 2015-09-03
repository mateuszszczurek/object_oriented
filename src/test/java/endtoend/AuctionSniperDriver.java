package endtoend;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.*;
import com.objogate.wl.swing.gesture.GesturePerformer;
import pl.com.sniper.gui.MainWindow;

import javax.swing.*;
import javax.swing.table.JTableHeader;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static java.lang.String.valueOf;
import static pl.com.sniper.gui.MainWindow.NEW_ITEM_ID_NAME;

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

    public void startBiddingFor(String itemId) {
        textField(NEW_ITEM_ID_NAME).replaceAllText(itemId);
        bidButton().click();
    }

    public void startBiddingFor(String itemId, int stopPrice) {
        textField(NEW_ITEM_ID_NAME).replaceAllText(itemId);
//        textField(STOP_PRICE).replaceAllText(valueOf(stopPrice));
        bidButton().click();
    }

    private JTextFieldDriver textField(String fieldName) {
        JTextFieldDriver newItemid = new JTextFieldDriver(this, JTextField.class, named(fieldName));
        newItemid.focusWithMouse();
        return newItemid;
    }

    private JButtonDriver bidButton() {
        return new JButtonDriver(this, JButton.class, named(MainWindow.JOIN_BUTTON_NAME));
    }

    private JTextFieldDriver itemIdField() {
        JTextFieldDriver newItemid = new JTextFieldDriver(this, JTextField.class, named(NEW_ITEM_ID_NAME));
        newItemid.focusWithMouse();
        return newItemid;
    }
}
