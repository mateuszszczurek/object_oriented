package pl.com.sniper.auction.xmpp;

public class MissinValueException extends RuntimeException {

    private String value;

    public MissinValueException(String value) {
        this.value = value;
    }
}
