package pl.com.sniper.auction.sniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import pl.com.sniper.auction.Main;

public class Auction {

    private Chat chat;

    public Auction(Chat chat) {

        this.chat = chat;
    }

    public void bid(int howMuch) {
        try {
            chat.sendMessage(String.format(Main.BID_COMMAND_FORMAT, howMuch));
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

}
