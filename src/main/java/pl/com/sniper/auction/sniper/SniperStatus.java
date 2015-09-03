package pl.com.sniper.auction.sniper;

public enum SniperStatus {


    JOINING {
        @Override
        public SniperStatus whenAuctionClosed() {
            return LOST;
        }
    },
    WINNING {
        @Override
        public SniperStatus whenAuctionClosed() {
            return WON;
        }
    },
    BIDDING {
        @Override
        public SniperStatus whenAuctionClosed() {
            return LOST;
        }
    },

    LOOSING {
        @Override
        public SniperStatus whenAuctionClosed() {
            return LOST;
        }

    },

    WON,

    LOST;

    public SniperStatus whenAuctionClosed() {
        throw new RuntimeException("Auction is already closed");
    }

}
