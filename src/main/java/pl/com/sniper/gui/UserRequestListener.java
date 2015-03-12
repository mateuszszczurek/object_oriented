package pl.com.sniper.gui;

import java.util.EventListener;

/**
 * Created by Mateusz on 2015-03-12.
 */
public interface UserRequestListener extends EventListener {

    void joinAuction(String itemId);

}
