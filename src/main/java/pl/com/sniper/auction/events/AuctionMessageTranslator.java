package pl.com.sniper.auction.events;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.HashMap;

import static pl.com.sniper.auction.events.AuctionEventListener.PriceSource.FromOtherBidder;
import static pl.com.sniper.auction.events.AuctionEventListener.PriceSource.FromSniper;

public class AuctionMessageTranslator implements MessageListener {

    public static final String CLOSE_EVENT = "CLOSE";
    public static final String PRICE_EVENT = "PRICE";

    private final AuctionEventListener listener;
    private final String sniperId;

    public AuctionMessageTranslator(AuctionEventListener listener, String sniperId) {
        this.listener = listener;
        this.sniperId = sniperId;
    }


    @Override
    public void processMessage(Chat chat, Message message) {

        AuctionEvent event = AuctionEvent.from(message);

        switch (event.getType()) {
            case CLOSE_EVENT: listener.onAuctionClosed(); break;
            case PRICE_EVENT: listener.currentPrice(event.getPrice(), event.getIncrement(), event.isFrom(sniperId)) ; break;
        }

    }

    private boolean ifSniperMadeThatBid(AuctionEvent event) {
        return event.getAuctioneer().equals(sniperId);
    }

    private static class AuctionEvent {

        public static final String EVENT_KEY = "Event";

        private final HashMap<String, String> event = new HashMap<>();

        public static AuctionEvent from(Message message) {
            return new AuctionEvent(message.getBody());
        }

        public AuctionEvent(String messageBody) {
            unpackEvent(messageBody);
        }

        private void unpackEvent(String eventContent) {

            for (String element : fieldsIn(eventContent)) {
                addProperty(fieldAndValue(element));
            }

        }

        private String[] fieldAndValue(String element) {
            return element.split(":");
        }

        private String addProperty(String[] pair) {
            return event.put(pair[0].trim(), pair[1].trim());
        }

        private static String[] fieldsIn(String eventContent) {
            return eventContent.split(";");
        }

        public String getType() {
            return event.get(EVENT_KEY);
        }

        public int getPrice() {
            return Integer.parseInt(event.get("CurrentPrice"));
        }

        public int getIncrement() {
            return Integer.parseInt(event.get("Increment"));
        }

        public String getAuctioneer() {
            return event.get("Auctioneer");
        }

        public AuctionEventListener.PriceSource isFrom(String sniperId) {
            return getAuctioneer().equals(sniperId) ? FromSniper : FromOtherBidder;
        }
    }
}
