package pl.com.sniper.gui;

import java.util.EventListener;

public interface UserRequestListener extends EventListener {

    void joinAuction(String itemId);

}
