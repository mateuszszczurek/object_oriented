package pl.com.sniper.auction.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import pl.com.sniper.auction.events.AuctionEventListener;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static pl.com.sniper.auction.events.AuctionEventListener.PriceSource.FromOtherBidder;
import static pl.com.sniper.auction.events.AuctionEventListener.PriceSource.FromSniper;

public class AuctionMessageTranslatorTest {

    private static final Chat UNUSED_CHAT = null;
    private static final String SNIPER_ID = "sniper_id";

    private AuctionEventListener listener = mock(AuctionEventListener.class);
    private FailedMessageLogger failedMessageLogger = mock(FailedMessageLogger.class);

    private AuctionMessageTranslator sut = new AuctionMessageTranslator(listener, SNIPER_ID, failedMessageLogger);

    @Test
    public void notifiesAuctionClosedWhenCloseMessageReceived() {

        Message auctionClosedMessage = new Message();
        auctionClosedMessage.setBody("SOLVersion: 1.1; Event: CLOSE;");

        sut.processMessage(UNUSED_CHAT, auctionClosedMessage);

        verify(listener).onAuctionClosed();

    }

    @Test
    public void notifiesBidDetailsWhenCurrentMessageReceivedFromSomeoneElse() {

        Message auctionClosedMessage = new Message();
        auctionClosedMessage.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");

        sut.processMessage(UNUSED_CHAT, auctionClosedMessage);

        verify(listener).currentPrice(192, 7, FromOtherBidder);

    }

    @Test
        public void notifiesBidDetailsWhenCurrentMessageReceivedFromSniper() {

        Message auctionClosedMessage = new Message();
        auctionClosedMessage.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: " +SNIPER_ID + ";");

        sut.processMessage(UNUSED_CHAT, auctionClosedMessage);

        verify(listener).currentPrice(192, 7, FromSniper);

    }

    @Test
         public void notifiesAboutErranousMessage() {

        Message corruptedMessage = new Message();
        String messageContent = "Blah blah...";
        corruptedMessage.setBody(messageContent);

        sut.processMessage(UNUSED_CHAT, corruptedMessage);

        verify(listener).auctionFailed();
        verify(failedMessageLogger).reportFailedMessage(argThat(hasMessageContent(messageContent)));

    }

    @Test
    public void notifiesWhenThereAreMissingValuesInTheMessage() {

        Message corruptedMessage = new Message();
        String messageContent = "SOLVersion: 1.1; CurrentPrice: 192; Increment: 7; Bidder: Someone else;";
        corruptedMessage.setBody(messageContent);

        sut.processMessage(UNUSED_CHAT, corruptedMessage);

        verify(listener).auctionFailed();
        verify(failedMessageLogger).reportFailedMessage(argThat(hasMessageContent(messageContent)));

    }

    private ArgumentMatcher<FailedMessage> hasMessageContent(String messageContent) {
        return new ArgumentMatcher<FailedMessage>() {
            @Override
            public boolean matches(Object argument) {
                FailedMessage failedMessage = (FailedMessage) argument;
                return failedMessage.getContent().equals(messageContent);
            }
        };
    }


}