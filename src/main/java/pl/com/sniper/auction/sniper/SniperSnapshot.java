package pl.com.sniper.auction.sniper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.com.sniper.auction.Item;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class SniperSnapshot {

    public final Item item;
    public final int lastPrice;
    public final int lastBid;

    public final SniperStatus status;

    public SniperSnapshot bidding(int lastPrice, int lastBid) {
        return new SniperSnapshot(item, lastPrice, lastBid, SniperStatus.BIDDING);
    }

    public SniperSnapshot winning(int winningBid) {
        return new SniperSnapshot(item, winningBid, winningBid, SniperStatus.WINNING);
    }

    public SniperSnapshot loosing(int winningPrice) {
        return new SniperSnapshot(item, winningPrice, lastBid, SniperStatus.LOOSING);
    }

    public SniperSnapshot(Item item, int lastPrice, int lastBid, SniperStatus status) {
        this.item = item;
        this.lastPrice = lastPrice;
        this.lastBid = lastBid;
        this.status = status;
    }

    public boolean isSameItemAs(SniperSnapshot other) {
        return this.item.equals(other.item);
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

    public static SniperSnapshot joining(Item item) {
        return new SniperSnapshot(item, 0, 0, SniperStatus.JOINING);
    }

    public SniperSnapshot closed() {
        return new SniperSnapshot(item, lastPrice, lastBid, status.whenAuctionClosed());
    }

    public SniperSnapshot failed() {
        return new SniperSnapshot(item, 0, 0, SniperStatus.FAILED);
    }

    public boolean canContinueBidding(int currentPrice) {
        return currentPrice < item.getStopPrice();
    }
}
