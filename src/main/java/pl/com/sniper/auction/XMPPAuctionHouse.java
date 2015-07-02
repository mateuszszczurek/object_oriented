package pl.com.sniper.auction;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import pl.com.sniper.auction.sniper.Auction;
import pl.com.sniper.auction.xmpp.XMPPAuction;
import pl.com.sniper.mistakes.Defect;

public class XMPPAuctionHouse implements AuctionHouse {

    private final XMPPConnection connection;

    private XMPPAuctionHouse(XMPPConnection connection) {
        this.connection = connection;
    }

    public static XMPPAuctionHouse connect(String host, String sniperId, String sniperPassword) {
        return new XMPPAuctionHouse(connection(host, sniperId, sniperPassword));
    }

    private static XMPPConnection connection(String host, String sniperId, String sniperPassword) {
        try {
            XMPPConnection connection = new XMPPConnection(host);
            connection.connect();
            connection.login(sniperId, sniperPassword, XMPPAuction.AUCTION_RESOURCE);
            return connection;
        } catch (XMPPException e) {
            throw new Defect();
        }
    }

    @Override
    public Auction auctionFor(String itemId) {
        return new XMPPAuction(connection, itemId);
    }

    public void cleanup() {
        connection.disconnect();
    }
}
