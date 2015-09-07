package pl.com.sniper.auction;

public class Item {

    private final int stopPrice;
    private final String itemId;

    public Item(int stopPrice, String itemId) {
        this.stopPrice = stopPrice;
        this.itemId = itemId;
    }

    public int getStopPrice() {
        return stopPrice;
    }

    public String getItemId() {
        return itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (stopPrice != item.stopPrice) return false;
        if (itemId != null ? !itemId.equals(item.itemId) : item.itemId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stopPrice;
        result = 31 * result + (itemId != null ? itemId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Item{" +
                "stopPrice=" + stopPrice +
                ", itemId='" + itemId + '\'' +
                '}';
    }

}
