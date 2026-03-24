package TOPSECRET.domain;

import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.ValueObjects.Price;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionTest {

    // -------------- Constructors ---------------------------

    @Test
    void constructor_shouldThrow_whenNullClock() {
        Listing listing = mockAuctionListingWithInitialPrice("35.00");
        LocalDateTime start = now().plusMinutes(1);
        LocalDateTime end = start.plusDays(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listing, start, end, mockPrice("150.00"), null));

        assertEquals("Clock cannot be null", ex.getMessage());
    }

    @Test
    void constructor_shouldThrow_whenNullListing() {
        LocalDateTime start = now().plusMinutes(1);
        LocalDateTime end = start.plusDays(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(null, start, end, mockPrice("150.00"), FIXED_CLOCK));

        assertEquals("Listing cannot be null.", ex.getMessage());
    }

    @Test
    void constructor_saleTypeNotAuction_throws() {
        Listing listing = mock(Listing.class);
        when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        LocalDateTime start = now().plusMinutes(1);
        LocalDateTime end = start.plusDays(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listing, start, end, mockPrice("150.00"), FIXED_CLOCK));

        assertEquals("This listing is not an auction. SaleType must be AUCTION.", ex.getMessage());
    }

    @Test
    void constructor_startDateBeforeNow_throws() {
        Listing listing = mockAuctionListingWithInitialPrice("35.00");

        LocalDateTime start = now().minusSeconds(1);
        LocalDateTime end = now().plusDays(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listing, start, end, mockPrice("150.00"), FIXED_CLOCK));

        assertEquals("Initial date must not be before the current date.", ex.getMessage());
    }

    @Test
    void constructor_endEqualOrBeforeStart_throws() {
        Listing listing = mockAuctionListingWithInitialPrice("35.00");
        LocalDateTime start = now().plusMinutes(5);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listing, start, start, mockPrice("150.00"), FIXED_CLOCK));
        assertEquals("Final date must not be before the initial date.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listing, start, start.minusSeconds(1), mockPrice("150.00"), FIXED_CLOCK));
        assertEquals("Final date must not be before the initial date.", ex2.getMessage());
    }

    @Test
    void constructor_buyoutLessOrEqualInitial_throws() {
        Listing listing = mockAuctionListingWithInitialPrice("35.00");
        LocalDateTime start = now().plusMinutes(5);
        LocalDateTime end = start.plusDays(1);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listing, start, end, mockPrice("35.00"), FIXED_CLOCK));
        assertEquals("Buy out price must be greater than Initial price.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listing, start, end, mockPrice("10.00"), FIXED_CLOCK));
        assertEquals("Buy out price must be greater than Initial price.", ex2.getMessage());
    }

    // ------------- initial status --------------------

    @Test
    void ctor_setsScheduled_whenStartAfterNow() {
        Auction auction = createScheduledAuction("35.00", "150.00");
        assertEquals(AuctionStatus.SCHEDULED, auction.getAuctionStatus());
        assertNotNull(auction.get_bidHistory());
        assertTrue(auction.get_bidHistory().isEmpty());
    }

    @Test
    void ctor_setsActive_whenStartEqualsNow() {
        Auction auction = createActiveAuction("35.00", "150.00");
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    // ------------------- acceptBid --------------------

    @Test
    void acceptBid_nullBid_throws() {
        Auction auction = createActiveAuction("35.00", "150.00");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> auction.acceptBid(null));

        assertEquals("Bid cannot be null", ex.getMessage());
    }

    @Test
    void acceptBid_whenScheduled_throwsIllegalState() {
        Auction auction = createScheduledAuction("35.00", "150.00");
        Bid bid = mockBid("40.00");

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> auction.acceptBid(bid));

        assertEquals("Auction is not active", ex.getMessage());
    }

    @Test
    void acceptBid_bidLessOrEqualInitial_throws() {
        Auction auction = createActiveAuction("35.00", "150.00");

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> auction.acceptBid(mockBid("35.00")));
        assertEquals("Bid value must be greater than initial Price.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> auction.acceptBid(mockBid("34.99")));
        assertEquals("Bid value must be greater than initial Price.", ex2.getMessage());
    }

    @Test
    void acceptBid_validBid_addsToHistory_andStaysActive() {
        Auction auction = createActiveAuction("35.00", "150.00");
        Bid bid = mockBid("40.00");

        assertTrue(auction.acceptBid(bid));
        assertEquals(1, auction.get_bidHistory().size());
        assertSame(bid, auction.get_bidHistory().get(0));
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    @Test
    void acceptBid_secondBidMustBeGreaterThanLastBid() {
        Auction auction = createActiveAuction("35.00", "150.00");

        assertTrue(auction.acceptBid(mockBid("40.00")));

        IllegalArgumentException exEqual = assertThrows(IllegalArgumentException.class,
                () -> auction.acceptBid(mockBid("40.00")));
        assertTrue(exEqual.getMessage().startsWith("Bid value must be greater than last bid value:"));

        IllegalArgumentException exLower = assertThrows(IllegalArgumentException.class,
                () -> auction.acceptBid(mockBid("39.00")));
        assertTrue(exLower.getMessage().startsWith("Bid value must be greater than last bid value:"));
    }

    @Test
    void acceptBid_whenBidEqualsBuyout_closesAsBoughtOut() {
        Auction auction = createActiveAuction("35.00", "150.00");
        Bid bid = mockBid("150.00");

        assertTrue(auction.acceptBid(bid));
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
        assertEquals(1, auction.get_bidHistory().size());
    }

    @Test
    void acceptBid_scheduledButStartReached_isAcceptedBecauseRefreshStatusRuns() {
        LocalDateTime baseNow = now();

        Auction auction = new Auction(
                mockAuctionListingWithInitialPrice("35.00"),
                baseNow.plusMinutes(5),     // start no futuro => SCHEDULED
                baseNow.plusDays(2),
                mockPrice("150.00"),
                FIXED_CLOCK
        );

        assertEquals(AuctionStatus.SCHEDULED, auction.getAuctionStatus());

        Clock afterStart = Clock.offset(FIXED_CLOCK, Duration.ofMinutes(6));
        setClock(auction, afterStart);

        assertTrue(auction.acceptBid(mockBid("40.00")));
        assertEquals(1, auction.get_bidHistory().size());
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    @Test
    void acceptBid_whenExpiredWithoutBids_setsEndedWithoutBids_andThrowsIllegalState() {
        LocalDateTime baseNow = now();

        Auction auction = new Auction(
                mockAuctionListingWithInitialPrice("35.00"),
                baseNow,
                baseNow.plusMinutes(5),
                mockPrice("150.00"),
                FIXED_CLOCK
        );

        setClock(auction, Clock.offset(FIXED_CLOCK, Duration.ofMinutes(10)));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> auction.acceptBid(mockBid("40.00")));

        assertEquals("Auction is not active", ex.getMessage());
        assertEquals(AuctionStatus.ENDED_WITHOUT_BIDS, auction.getAuctionStatus());
        assertTrue(auction.get_bidHistory().isEmpty());
    }

    @Test
    void refreshStatus_doesNotChangeTerminalStatus_boughtOut() {
        Auction auction = createActiveAuction("35.00", "150.00");
        assertTrue(auction.acceptBid(mockBid("150.00")));
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());

        auction.refreshStatus();

        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
    }

    // --------- Equals and hashCode

    @Test
    void equals_returnsFalse_forNull_andDifferentClass() {
        Auction auction = createScheduledAuction("35.00", "150.00");
        assertFalse(auction.equals(null));
        assertFalse(auction.equals("not an auction"));
    }

    @Test
    void equals_false_whenStartDateDiffers() {
        Listing listing = mockAuctionListingWithInitialPrice("35.00");
        LocalDateTime baseNow = now();

        Auction a1 = new Auction(listing, baseNow.plusMinutes(5), baseNow.plusDays(2), mockPrice("150.00"), FIXED_CLOCK);
        Auction a2 = new Auction(listing, baseNow.plusMinutes(6), baseNow.plusDays(2), mockPrice("150.00"), FIXED_CLOCK);

        assertNotEquals(a1, a2);
    }

    @Test
    void equals_false_whenEndDateDiffers() {
        Listing listing = mockAuctionListingWithInitialPrice("35.00");
        LocalDateTime baseNow = now();

        Auction a1 = new Auction(listing, baseNow.plusMinutes(5), baseNow.plusDays(2), mockPrice("150.00"), FIXED_CLOCK);
        Auction a2 = new Auction(listing, baseNow.plusMinutes(5), baseNow.plusDays(3), mockPrice("150.00"), FIXED_CLOCK);

        assertNotEquals(a1, a2);
    }

    @Test
    void equals_false_whenBuyoutDiffers() {
        Listing listing = mockAuctionListingWithInitialPrice("35.00");
        LocalDateTime baseNow = now();

        Auction a1 = new Auction(listing, baseNow.plusMinutes(5), baseNow.plusDays(2), mockPrice("150.00"), FIXED_CLOCK);
        Auction a2 = new Auction(listing, baseNow.plusMinutes(5), baseNow.plusDays(2), mockPrice("150.01"), FIXED_CLOCK);

        assertNotEquals(a1, a2);
    }

    @Test
    void hashCode_sameData_sameHashCode() {
        Listing listing = mockAuctionListingWithInitialPrice("35.00");
        LocalDateTime baseNow = now();
        Price buyoutDouble = mockPrice("150.00");

        Auction a1 = new Auction(listing, baseNow.plusMinutes(5), baseNow.plusDays(2), buyoutDouble, FIXED_CLOCK);
        Auction a2 = new Auction(listing, baseNow.plusMinutes(5), baseNow.plusDays(2), buyoutDouble, FIXED_CLOCK);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void closeIfExpired_whenNotExpired_returnsFalse_andKeepsStatusActive() {
        LocalDateTime baseNow = now();

        Auction auction = new Auction(
                mockAuctionListingWithInitialPrice("35.00"),
                baseNow,
                baseNow.plusMinutes(10),
                mockPrice("150.00"),
                FIXED_CLOCK
        );

        boolean closed = invokeCloseIfExpired(auction);

        assertFalse(closed);
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    @Test
    void closeIfExpired_whenExpiredWithoutBids_returnsTrue_andEndsWithoutBids() {
        LocalDateTime baseNow = now();

        Auction auction = new Auction(
                mockAuctionListingWithInitialPrice("35.00"),
                baseNow,
                baseNow.plusMinutes(5),
                mockPrice("150.00"),
                FIXED_CLOCK
        );

        // expires
        setClock(auction, Clock.offset(FIXED_CLOCK, Duration.ofMinutes(10)));

        boolean closed = invokeCloseIfExpired(auction);

        assertTrue(closed);
        assertEquals(AuctionStatus.ENDED_WITHOUT_BIDS, auction.getAuctionStatus());
    }

    @Test
    void closeIfExpired_whenExpiredWithBids_returnsTrue_andEndsWithWinner() {
        LocalDateTime baseNow = now();

        Auction auction = new Auction(
                mockAuctionListingWithInitialPrice("35.00"),
                baseNow,
                baseNow.plusMinutes(5),
                mockPrice("150.00"),
                FIXED_CLOCK
        );

        assertTrue(auction.acceptBid(mockBid("40.00")));
        assertTrue(auction.acceptBid(mockBid("45.00")));

        setClock(auction, Clock.offset(FIXED_CLOCK, Duration.ofMinutes(10))); // expires

        boolean closed = invokeCloseIfExpired(auction);

        assertTrue(closed);
        assertEquals(AuctionStatus.ENDED_WITH_WINNER, auction.getAuctionStatus());
    }

    @Test
    void hashCode_differentAuctions_shouldDiffer() {
        Listing listing = mockAuctionListingWithInitialPrice("35.00");
        LocalDateTime baseNow = now();

        Auction a1 = new Auction(listing, baseNow.plusMinutes(5), baseNow.plusDays(2), mockPrice("150.00"), FIXED_CLOCK);
        Auction a2 = new Auction(listing, baseNow.plusMinutes(5), baseNow.plusDays(3), mockPrice("150.00"), FIXED_CLOCK);

        assertNotEquals(a1, a2);
        assertNotEquals(a1.hashCode(), a2.hashCode());
    }








    // ================== HELPERS ==============================================

    private static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2030-01-01T10:00:00Z"), ZoneOffset.UTC);

    private static LocalDateTime now() {
        return LocalDateTime.now(FIXED_CLOCK);
    }

    private static Price mockPrice(String v) {
        Price p = mock(Price.class);
        when(p.getPrice()).thenReturn(new BigDecimal(v));
        return p;
    }

    private static Listing mockAuctionListingWithInitialPrice(String initialPrice) {
        Listing listing = mock(Listing.class);
        when(listing.getSaleType()).thenReturn(SaleType.AUCTION);

        Price initial = mock(Price.class);
        when(initial.getPrice()).thenReturn(new BigDecimal(initialPrice));
        when(listing.getPrice()).thenReturn(initial);
        return listing;
    }

    private static Auction createActiveAuction(String initial, String buyout) {
        Listing listing = mockAuctionListingWithInitialPrice(initial);
        LocalDateTime start = now();
        LocalDateTime end = start.plusDays(2);
        return new Auction(listing, start, end, mockPrice(buyout), FIXED_CLOCK);
    }

    private static Auction createScheduledAuction(String initial, String buyout) {
        Listing listing = mockAuctionListingWithInitialPrice(initial);
        LocalDateTime start = now().plusMinutes(5);
        LocalDateTime end = start.plusDays(2);
        return new Auction(listing, start, end, mockPrice(buyout), FIXED_CLOCK);
    }

    private Bid mockBid(String v) {
        Bid bid = mock(Bid.class);
        Price p = mockPrice(v);
        when(bid.getValue()).thenReturn(p);
        return bid;
    }

    private static void setClock(Auction auction, Clock clock) {
        try {
            Field f = Auction.class.getDeclaredField("_clock");
            f.setAccessible(true);
            f.set(auction, clock);
        } catch (Exception e) {
            fail("Failed to set _clock: " + e.getMessage());
        }
    }

    private static boolean invokeCloseIfExpired(Auction auction) {
        try {
            var m = Auction.class.getDeclaredMethod("closeIfExpired");
            m.setAccessible(true);
            return (boolean) m.invoke(auction);
        } catch (Exception e) {
            fail("Failed to invoke closeIfExpired(): " + e.getMessage());
            return false;
        }
    }

}