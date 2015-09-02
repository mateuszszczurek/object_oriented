package pl.com.sniper.auction.xmpp;

import endtoend.ApplicationRunner;
import endtoend.FakeAuctionServer;
import org.jivesoftware.smack.XMPPException;
import org.junit.Test;
import pl.com.sniper.auction.XMPPAuctionHouse;
import pl.com.sniper.auction.events.AuctionEventListener;
import pl.com.sniper.auction.sniper.Auction;

import static org.mockito.Mockito.*;

public class XMPPAuctionTest {

    @Test
    public void receives_events_from_auction_server_after_joining() throws XMPPException, InterruptedException {
        String itemId = "item-54321";

        FakeAuctionServer fakeAuctionServer = new FakeAuctionServer(itemId);
        fakeAuctionServer.startSellingItem();

        XMPPAuctionHouse xmppAuctionHouse = XMPPAuctionHouse.connect("localhost", "sniper", "sniper");
        Auction auction = xmppAuctionHouse.auctionFor(itemId);

        AuctionEventListener listener =  mock(AuctionEventListener.class);
        auction.addAuctionEventListener(listener);

        auction.join();

        fakeAuctionServer.hasReceivedJoiRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        fakeAuctionServer.announceClosed();

        verify(listener, timeout(2000)).onAuctionClosed();
    }

}