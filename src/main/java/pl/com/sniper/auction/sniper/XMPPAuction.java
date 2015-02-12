package pl.com.sniper.auction.sniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import pl.com.sniper.auction.Main;

public class XMPPAuction implements Auction {

    public static final String JOIN_COMMAND_FORMAT = "JOIN COMMAND";
    private Chat chat;

    public XMPPAuction(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void bid(int howMuch) {
        sendMessage(String.format(Main.BID_COMMAND_FORMAT, howMuch));
    }

    @Override
    public void join() {
        sendMessage(JOIN_COMMAND_FORMAT);
    }

    private void sendMessage(String joinCommandFormat) {
        try {
            chat.sendMessage(joinCommandFormat);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

}
