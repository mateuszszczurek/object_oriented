package pl.com.sniper.auction.xmpp;

import org.junit.Test;

import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static pl.com.sniper.auction.xmpp.FailedMessage.failedMessage;

public class FailedMessageLoggerTest {

    private Logger logger = mock(Logger.class);
    private FailedMessageLogger failedMessageLogger = new FailedMessageLogger(logger);

    @Test
    public void messagesAreLoggerWithAppropriateFormatAndLevel() {

        failedMessageLogger.reportFailedMessage(failedMessage("some id", "failedMessage", mock(Exception.class)));

        verify(logger).severe("some id:failedMessage");

    }

}