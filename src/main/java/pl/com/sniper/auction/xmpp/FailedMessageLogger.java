package pl.com.sniper.auction.xmpp;

import java.util.logging.Logger;

import static java.lang.String.format;

public class FailedMessageLogger {

    private final Logger logger;

    public FailedMessageLogger(Logger logger) {
        this.logger = logger;
    }

    public void reportFailedMessage(FailedMessage messageContent) {
        logger.severe(format("%s:%s", messageContent.getSniperId(), messageContent.getContent()));
    }

}
