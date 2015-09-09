package pl.com.sniper.auction.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import pl.com.sniper.auction.events.AuctionEventListener;

import java.util.HashMap;

import static pl.com.sniper.auction.events.AuctionEventListener.PriceSource.FromOtherBidder;
import static pl.com.sniper.auction.events.AuctionEventListener.PriceSource.FromSniper;
import static pl.com.sniper.auction.xmpp.FailedMessage.failedMessage;

public class AuctionMessageTranslator implements MessageListener {

    public static final String CLOSE_EVENT = "CLOSE";
    public static final String PRICE_EVENT = "PRICE";

    private final AuctionEventListener listener;
    private final FailedMessageLogger failedMessagesLogger;
    private final String sniperId;

    public AuctionMessageTranslator(AuctionEventListener listener, String sniperId, FailedMessageLogger failedMessagesLogger) {
        this.listener = listener;
        this.sniperId = sniperId;
        this.failedMessagesLogger = failedMessagesLogger;
    }

    @Override
    public void processMessage(Chat chat, Message message) {

        String messageBody = message.getBody();

        try {
            processMessage(messageBody);
        } catch (Exception e) {
            failedMessagesLogger.reportFailedMessage(failedMessage(sniperId, messageBody, e));
            listener.auctionFailed();
        }
    }

    private void processMessage(String messageBody) {
        AuctionEvent event = AuctionEvent.from(messageBody);

        switch (event.getType()) {
            case CLOSE_EVENT: listener.onAuctionClosed(); break;
            case PRICE_EVENT: listener.currentPrice(event.getPrice(), event.getIncrement(), event.isFrom(sniperId)) ; break;
        }
    }

    private static class AuctionEvent {

        public static final String EVENT_KEY = "Event";

        private final HashMap<String, String> event = new HashMap<>();

        public static AuctionEvent from(String messageBody) {
            return new AuctionEvent(messageBody);
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
            return get(EVENT_KEY);
        }

        public int getPrice() {
            return Integer.parseInt(get("CurrentPrice"));
        }

        public int getIncrement() {
            return Integer.parseInt(get("Increment"));
        }

        public String getAuctioneer() {
            return get("Bidder");
        }

        private String get(String whichValue) {
            String value = event.get(whichValue);

            if(value == null) {
                throw new MissingValue(whichValue);
            }

            return value;
        }

        public AuctionEventListener.PriceSource isFrom(String sniperId) {
            return getAuctioneer().equals(sniperId) ? FromSniper : FromOtherBidder;
        }
    }
}
