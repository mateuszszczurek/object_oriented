package pl.com.sniper.auction;

public class SnipersLauncherTest {

    // todo - leaving in for later, mockito does not support order of execution checks

//    @Test
//    public void add_new_sniper_to_collector_and_then_joins_auction() {
//        final String itemId = "item 123";
//        SniperSnapshot joiningState = joining("item 123");
//
//        AuctionHouse auctionHouse = mock(AuctionHouse.class);
//        Auction auction = mock(Auction.class);
//        SniperCollector sniperCollector = mock(SnipersTableModel.class);
//        SnipersLauncher snipersLauncher = new SnipersLauncher(auctionHouse, sniperCollector);
//
//        when(auctionHouse.auctionFor(itemId)).thenReturn(auction);
//
//        snipersLauncher.joinAuction(itemId);
//        Class<SniperListener> spy = spy(SniperListener.class);
//        verify(auctionHouse.auctionFor(itemId));
//        verify(auction.addMessageListener(sniperForItem(itemId));
//        verify(sniperCollector).addSniper(joiningState);
//
//    }

//    private AuctionEventListener sniperForItem(String itemId) {
//        new AuctionSniper(SniperSnapshot.);
//    }

}