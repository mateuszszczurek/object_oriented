package pl.com.sniper.gui;

import pl.com.sniper.auction.Item;

import java.util.EventListener;

public interface UserRequestListener extends EventListener {

    void joinAuction(Item item);

}
