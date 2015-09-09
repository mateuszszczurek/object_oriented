package pl.com.sniper.auction.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import pl.com.sniper.auction.Main;
import pl.com.sniper.auction.events.AuctionEventListener;
import pl.com.sniper.auction.sniper.Auction;
import pl.com.sniper.mistakes.Defect;
import pl.com.sniper.util.Announcer;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class XMPPAuction implements Auction {

    public static final String JOIN_COMMAND_FORMAT = "JOIN COMMAND";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String LOG_FILE_NAME = "auction-sniper.log";
    private static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private Chat chat;

    private Announcer<AuctionEventListener> auctionEventListeners = new Announcer<>(AuctionEventListener.class);

    public XMPPAuction(XMPPConnection connection, String itemId) {
        AuctionMessageTranslator translator = translatorFor(connection);

        this.chat =  connection.getChatManager().createChat(auctionId(itemId, connection),
                translator);

        addAuctionEventListener(chatDisconnectorFor(translator));
    }

    private AuctionEventListener chatDisconnectorFor(AuctionMessageTranslator translator) {
        return new AuctionEventListener() {
            @Override
            public void auctionFailed() {
                chat.removeMessageListener(translator);
            }

            @Override
            public void onAuctionClosed() {

            }

            @Override
            public void currentPrice(int currentPrice, int increment, PriceSource priceSource) {

            }
        };
    }

    private AuctionMessageTranslator translatorFor(XMPPConnection connection) {
        Logger logger = makeLogger();
        FailedMessageLogger failedMessageLogger = new FailedMessageLogger(logger);

        return new AuctionMessageTranslator(auctionEventListeners.announce(), connection.getUser(), failedMessageLogger);
    }

    private Logger makeLogger() {
        try {
            Logger logger = Logger.getLogger("logger");
            logger.setUseParentHandlers(false);
            logger.addHandler(fileHandler());
            return logger;
        } catch (IOException e) {
            throw new Defect();
        }
    }

    private Handler fileHandler() throws IOException {
        FileHandler fileHandler = new FileHandler(LOG_FILE_NAME);
        fileHandler.setFormatter(new SimpleFormatter());
        return fileHandler;
    }

    private static String auctionId(String auctionId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, auctionId, connection.getServiceName());
    }

    @Override
    public void bid(int howMuch) {
        sendMessage(String.format(Main.BID_COMMAND_FORMAT, howMuch));
    }

    @Override
    public void join() {
        sendMessage(JOIN_COMMAND_FORMAT);
    }

    @Override
    public void addAuctionEventListener(AuctionEventListener listener) {
        this.auctionEventListeners.addListener(listener);
    }

    private void sendMessage(String joinCommandFormat) {
        try {
            chat.sendMessage(joinCommandFormat);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

}
