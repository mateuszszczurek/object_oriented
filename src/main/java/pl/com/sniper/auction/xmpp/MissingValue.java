package pl.com.sniper.auction.xmpp;

public class MissingValue extends RuntimeException {

    private String value;

    public MissingValue(String value) {
        this.value = value;
    }

}
