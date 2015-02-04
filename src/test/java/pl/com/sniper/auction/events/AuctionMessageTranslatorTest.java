package pl.com.sniper.auction.events;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AuctionMessageTranslatorTest {

    private static final Chat UNUSED_CHAT = null;

    private AuctionEventListener listener = mock(AuctionEventListener.class);
    private AuctionMessageTranslator sut = new AuctionMessageTranslator(listener);

    @Test
    public void notifiesAuctionClosedWhenCloseMessageReceived() {

        Message auctionClosedMessage = new Message();
        auctionClosedMessage.setBody("SOLVersion: 1.1; Event: CLOSE;");

        sut.processMessage(UNUSED_CHAT, auctionClosedMessage);

        verify(listener).onAuctionClosed();

    }

    // todo
//    @Test
//    public void whenMessageIsNotCloseShouldNotNotify() {
//
//        Message auctionClosedMessage = new Message();
//        auctionClosedMessage.setBody("SOLVersion: 1.1; Event: NOT_CLOSED;");
//
//        sut.processMessage(auctionClosedMessage);
//
//        verify(listener).onAuctionClosed();
//
//    }


}