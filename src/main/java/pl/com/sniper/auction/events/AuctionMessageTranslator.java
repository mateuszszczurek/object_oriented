package pl.com.sniper.auction.events;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import pl.com.sniper.gui.MainWindow;

public class AuctionMessageTranslator implements MessageListener {


    private final AuctionEventListener listener;

    public AuctionMessageTranslator(AuctionEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        listener.onAuctionClosed();
    }
}
