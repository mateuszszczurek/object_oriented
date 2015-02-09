package pl.com.sniper.auction.events;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import pl.com.sniper.gui.MainWindow;

import java.util.HashMap;
import java.util.Map;

public class AuctionMessageTranslator implements MessageListener {

    private final AuctionEventListener listener;

    public AuctionMessageTranslator(AuctionEventListener listener) {
        this.listener = listener;
    }

     private Map<String, String> unpackEvent(String eventContent) {
         Map<String, String> stuff = new HashMap<>();

         String[] split = eventContent.split(";");

         for (String element : split) {
             String[] pair = element.split(":");
             stuff.put(pair[0].trim(), pair[1].trim());
         }

         return stuff;
     }

    @Override
    public void processMessage(Chat chat, Message message) {

        Map<String, String> event = unpackEvent(message.getBody());

        switch (event.get("Event")) {
            case "CLOSE" : listener.onAuctionClosed();break;
            case "PRICE" : processPriceMessage(event) ; break;
        }

    }

    private void processPriceMessage(Map<String, String> body) {
        int price = Integer.parseInt(body.get("CurrentPrice"));
        int increment = Integer.parseInt(body.get("Increment"));
        listener.currentPrice(price, increment);

    }
}
