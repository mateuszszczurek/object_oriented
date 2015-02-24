package pl.com.sniper.auction.sniper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class SniperSnapshot {

    public final String itemId;
    public final int lastPrice;
    public final int lastBid;

    public final SniperStatus status;

    public SniperSnapshot bidding(int lastPrice, int lastBid) {
        return new SniperSnapshot(itemId, lastPrice, lastBid, SniperStatus.BIDDING);
    }

    public SniperSnapshot winning(int winningBid) {
        return new SniperSnapshot(itemId, winningBid, winningBid, SniperStatus.WINNING);
    }

    public SniperSnapshot(String itemId, int lastPrice, int lastBid, SniperStatus status) {
        this.itemId = itemId;
        this.lastPrice = lastPrice;
        this.lastBid = lastBid;
        this.status = status;
    }

    public boolean isSameItemAs(SniperSnapshot other) {
        return this.itemId.equals(other.itemId);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    public static SniperSnapshot joining(String itemId) {
        return new SniperSnapshot(itemId, 0, 0, SniperStatus.JOINING);
    }

    public SniperSnapshot closed() {
        return new SniperSnapshot(itemId, lastPrice, lastPrice, status.whenAuctionClosed());
    }

}
