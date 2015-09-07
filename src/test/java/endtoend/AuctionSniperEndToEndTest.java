package endtoend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static endtoend.ApplicationRunner.SNIPER_XMPP_ID;
import static java.lang.Integer.MAX_VALUE;

public class AuctionSniperEndToEndTest {

    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final FakeAuctionServer auction2 = new FakeAuctionServer("item-12345");
    private final ApplicationRunner application = new ApplicationRunner();

    @Before
    public void setUpWindowClicker() {
        System.setProperty("com.objogate.wl.keyboard", "GB");
    }

    @Test
    public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auction.startSellingItem();
        application.startBiddingWithStopPrice(MAX_VALUE, auction);
        auction.hasReceivedJoiRequestFromSniper(SNIPER_XMPP_ID);
        auction.announceClosed();
        application.showsSniperHasLostAuction(auction, 0, 0);
    }

    @Test
    public void snipersWinsAuctionByMakingHigherBid() throws Exception {
        auction.startSellingItem();

        application.startBiddingWithStopPrice(MAX_VALUE, auction);
        auction.hasReceivedJoiRequestFromSniper(SNIPER_XMPP_ID);

        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(auction, 1000, 1098);

        auction.hasReceivedABid(1098, SNIPER_XMPP_ID);

        auction.reportPrice(1098, 97, SNIPER_XMPP_ID);
        application.hasShownSniperIsWinning(auction, 1098);

        auction.announceClosed();
        application.showSniperHasWonAuction(auction, 1098);
    }

    @Test
    public void sniperBidsForMultipleItems() throws Exception {
        auction.startSellingItem();
        auction2.startSellingItem();

        application.startBiddingWithStopPrice(MAX_VALUE, auction, auction2);

        auction.hasReceivedJoiRequestFromSniper(SNIPER_XMPP_ID);
        auction2.hasReceivedJoiRequestFromSniper(SNIPER_XMPP_ID);

        auction.reportPrice(1000, 98, "other bidder");
        auction.hasReceivedABid(1098, SNIPER_XMPP_ID);

        auction2.reportPrice(500, 10, "other bidder");
        auction2.hasReceivedABid(510, SNIPER_XMPP_ID);

        auction.reportPrice(1098, 97, SNIPER_XMPP_ID);
        auction2.reportPrice(510, 9, SNIPER_XMPP_ID);

        application.hasShownSniperIsWinning(auction, 1098);
        application.hasShownSniperIsWinning(auction2, 510);

        auction.announceClosed();
        auction2.announceClosed();

        application.showSniperHasWonAuction(auction, 1098);
        application.showSniperHasWonAuction(auction2, 510);
    }

    @Test
    public void sniperJoinsAuctionBidsAnLoses() throws Exception {
        auction.startSellingItem();

        application.startBiddingWithStopPrice(MAX_VALUE, auction);
        auction.hasReceivedJoiRequestFromSniper(SNIPER_XMPP_ID);

        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(auction, 1000, 1098);

        auction.hasReceivedABid(1098, SNIPER_XMPP_ID);

        auction.announceClosed();
        application.showsSniperHasLostAuction(auction, 1000, 1098);
    }

    @Test
    public void sniperLosesAnAuctionWhenThePriceIsTooHigh() throws Exception {
        auction.startSellingItem();

        application.startBiddingWithStopPrice(1090, auction);
        auction.hasReceivedJoiRequestFromSniper(SNIPER_XMPP_ID);

        auction.reportPrice(1000, 50, "other bidder");

        auction.hasReceivedABid(1050, SNIPER_XMPP_ID);

        auction.reportPrice(1110, 10, "other bidder");

        auction.reportPrice(1250, 10, "some other bidder");

        application.showsSniperIsLosing(auction, 1250, 1050);

        auction.announceClosed();

        application.showsSniperHasLostAuction(auction, 1250, 1050);
    }

    @After
    public void stopAuction() {
        auction.stop();
    }

    @After
    public void stopApplication() {
        application.stop();
    }

}
