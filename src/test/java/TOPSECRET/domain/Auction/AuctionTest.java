package TOPSECRET.domain.Auction;

import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for the {@link Auction} aggregate, validating its construction rules,
 * state transitions, bidding logic, equality semantics, and time‑dependent behaviour.
 *
 * <p>This suite ensures that the Auction domain model enforces all business invariants,
 * including:
 * <ul>
 *     <li>validation of constructor arguments (dates, prices, identifiers, clock);</li>
 *     <li>correct initial status assignment (SCHEDULED or ACTIVE);</li>
 *     <li>proper behaviour of {@code equals} and {@code sameAs} according to DDD rules;</li>
 *     <li>bid placement rules, including price progression, terminal states and buyout logic;</li>
 *     <li>status transitions based on time, including expiration and closing behaviour;</li>
 *     <li>immutability guarantees for exposed collections (e.g., bid history).</li>
 * </ul>
 *
 * <p>Several tests rely on a fixed {@link java.time.Clock} to simulate deterministic
 * time‑dependent behaviour, ensuring reproducibility.
 *
 * <p>Overall, this test class provides comprehensive coverage of the Auction lifecycle,
 * ensuring that the aggregate behaves consistently with the domain rules and remains
 * robust against invalid inputs or illegal state transitions.
 */
class AuctionTest {

    // -------------- Initial Set Up --------------------------------

    private ListingId listingId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Price initialPrice;
    private Price buyOutPrice;
    private UserId buyerId;

    @BeforeEach
    void setUp() {
        listingId = new ListingId("listingId");
        start = now();
        end = start.plusDays(1);
        initialPrice = new Price(new BigDecimal("10.00"));
        buyOutPrice = new Price(new BigDecimal("100.00"));
        buyerId = new UserId(UUID.randomUUID().toString());
    }


    // -------------- Constructors Tests ---------------------------

    @Test
    void constructor_shouldThrowWhenNullListing() {
        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(null, start, end, initialPrice, buyOutPrice, FIXED_CLOCK));

        assertEquals("Listing ID cannot be null.", ex.getMessage());
    }

    @Test
    void constructor_shouldThrowWhenNullClock() {
        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listingId, start, end, initialPrice, buyOutPrice, null));

        // Assert
        assertEquals("Clock cannot be null", ex.getMessage());
    }

    @Test
    void constructor_shouldThrowWhenInitialPriceIsNull() {
        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listingId, start, end, null, buyOutPrice, FIXED_CLOCK));

        // Assert
        assertEquals("Initial price cannot be null", ex.getMessage());
    }

    @Test
    void constructor_shouldThrowWhenInitialDateIsBeforeCurrentDate() {
        // Arrange
        start = now().minusDays(1);

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK));

        // Assert
        assertEquals("Initial date must not be before the current date.", ex.getMessage());
    }

    @Test
    void constructor_shouldThrowWhenEndDateIsEqualStartDate() {
        // Arrange
        end = start;

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK));

        // Assert
        assertEquals("Final date must not be before the initial date.", ex.getMessage());
    }

    @Test
    void constructor_shouldThrowWhenEndDateIsPreviousStartDate() {
        // Arrange
        end = now().minusDays(1);

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK));

        // Assert
        assertEquals("Final date must not be before the initial date.", ex.getMessage());
    }


    @Test
    void constructor_shouldThrowWhenBuyoutPriceIsLessThanInitialPrice() {
        // Arrange
        buyOutPrice = new Price(new BigDecimal("1.00"));

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK));

        // Assert
        assertEquals("Buyout price must be greater than Initial price.", ex.getMessage());
    }

    @Test
    void constructor_shouldThrowWhenBuyoutPriceIsEqualsToInitialPrice() {
        // Arrange
        buyOutPrice = new Price(new BigDecimal("10.00"));

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK));

        // Assert
        assertEquals("Buyout price must be greater than Initial price.", ex.getMessage());
    }

    @Test
    void reconstructionConstructorWithClock_withClockShouldStoreAllFieldsCorrectly() {
        // Arrange
        AuctionId auctionId = new AuctionId(UUID.randomUUID().toString());

        // Act
        Auction auction = new Auction(auctionId, listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Assert
        assertEquals(auctionId, auction.id());
        assertEquals(listingId, auction.getListingId());
        assertEquals(start, auction.getAuctionStartDateTime());
        assertEquals(end, auction.getAuctionEndDateTime());
        assertEquals(initialPrice, auction.getInitialPrice());
        assertEquals(buyOutPrice, auction.getBuyoutPrice());

        // internal state not initialized
        assertNull(auction.getWinningBid());
        assertNull(auction.getFinalPrice());
        assertNull(auction.getClosedAt());
    }

    @Test
    void reconstructionConstructorWithClock_ShouldThrowWhenClockIsNull() {
        // Arrange
        AuctionId auctionId = new AuctionId(UUID.randomUUID().toString());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Auction(auctionId, listingId, start, end, initialPrice, buyOutPrice, null)
        );

        assertEquals("Clock cannot be null", ex.getMessage());
    }

    @Test
    void reconstructionConstructorWithoutClock_shouldStoreAllFieldsCorrectly() {
        // Arrange
        AuctionId auctionId = new AuctionId(UUID.randomUUID().toString());

        // Act
        Auction auction = new Auction(auctionId, listingId, start, end, initialPrice, buyOutPrice);

        // Assert
        assertEquals(auctionId, auction.id());
        assertEquals(listingId, auction.getListingId());
        assertEquals(start, auction.getAuctionStartDateTime());
        assertEquals(end, auction.getAuctionEndDateTime());
        assertEquals(initialPrice, auction.getInitialPrice());
        assertEquals(buyOutPrice, auction.getBuyoutPrice());

        // internal state not initialized
        assertNull(auction.getWinningBid());
        assertNull(auction.getFinalPrice());
        assertNull(auction.getClosedAt());
    }

    // ------------- Initial status --------------------

    @Test
    void ctor_setsScheduled_whenStartAfterNow() {
        // Act
        Auction auction = createScheduledAuction("35.00", "150.00");

        // Assert
        assertEquals(AuctionStatus.SCHEDULED, auction.getAuctionStatus());
        assertNotNull(auction.getBidHistory());
        assertTrue(auction.getBidHistory().isEmpty());
    }

    @Test
    void ctor_setsActive_whenStartEqualsNow() {
        // Act
        Auction auction = createActiveAuction("35.00", "150.00");

        // Assert
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    // --------- Equals Method Tests ---------

    @Test
    void equals_shouldReturnTrueWhenSameInstance() {
        // Arrange
        AuctionFactory auctionFactory = new AuctionFactory();

        // Act
        Auction a1 = auctionFactory.create(listingId, start, end, initialPrice, buyOutPrice);

        // Assert
        assertTrue(a1.equals(a1));
    }

    @Test
    void equals_shouldReturnFalseWhenIdsAreDifferent() {
        // Arrange
        AuctionFactory auctionFactory = new AuctionFactory();

        ListingId listingId1 = new ListingId("listingId1");
        ListingId listingId2 = new ListingId("listingId2");

        // Act
        Auction a1 = auctionFactory.create(listingId1, start, end, initialPrice, buyOutPrice);
        Auction a2 = auctionFactory.create(listingId2, start, end, initialPrice, buyOutPrice);

        // Assert
        assertFalse(a1.equals(a2));
    }

    @Test
    void equals_shouldReturnFalseWhenObjectIsNotAuction() {
        // Arrange
        AuctionFactory auctionFactory = new AuctionFactory();

        // Act
        Auction a1 = auctionFactory.create(listingId, start, end, initialPrice, buyOutPrice);

        // Assert
        assertFalse(a1.equals("not an auction"));
    }

    @Test
    void equals_shouldReturnFalseWhenObjectIsNull() {
        // Arrange
        AuctionFactory auctionFactory = new AuctionFactory();

        // Act
        Auction a1 = auctionFactory.create(listingId, start, end, initialPrice, buyOutPrice);

        // Assert
        assertFalse(a1.equals(null));
    }

    // --------- Same As Method Tests ---------

    @Test
    void sameAs_shouldReturnTrueWhenAllParametersAreEqual() {
        // Act
        Auction a1 = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        Auction a2 = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Assert
        assertTrue(a1.sameAs(a2));
    }


    @Test
    void sameAs_shouldReturnFalseWhenObjectIsNullOrDifferentClass() {
        // Act
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Assert
        assertFalse(auction.sameAs(null));
        assertFalse(auction.sameAs("not an auction"));
    }

    @Test
    void sameAs_shouldReturnFalseWhenListingIdIsDifferent() {
        // Arrange
        ListingId listingId1 = new ListingId("listingId1");
        ListingId listingId2 = new ListingId("listingId2");

        // Act
        Auction a1 = new Auction(listingId1, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        Auction a2 = new Auction(listingId2, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Assert
        assertFalse(a1.sameAs(a2));
    }

    @Test
    void sameAs_shouldReturnFalseWhenStartDateDiffers() {
        // Arrange
        LocalDateTime start1 = now().plusMinutes(5);
        LocalDateTime start2 = now().plusMinutes(6);

        // Act
        Auction a1 = new Auction(listingId, start1, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        Auction a2 = new Auction(listingId, start2, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Assert
        assertFalse(a1.sameAs(a2));
    }

    @Test
    void sameAs_shouldReturnFalseWhenEndDateDiffers() {
        // Arrange
        LocalDateTime end1 = now().plusMinutes(5);
        LocalDateTime end2 = now().plusMinutes(6);

        // Act
        Auction a1 = new Auction(listingId, start, end1, initialPrice, buyOutPrice, FIXED_CLOCK);
        Auction a2 = new Auction(listingId, start, end2, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Assert
        assertFalse(a1.sameAs(a2));
    }

    @Test
    void sameAs_shouldReturnFalseWhenInitialPriceDiffers() {
        // Arrange
        Price initialPrice1 = new Price(new BigDecimal("10.00"));
        Price initialPrice2 = new Price(new BigDecimal("11.00"));

        // Act
        Auction a1 = new Auction(listingId, start, end, initialPrice1, buyOutPrice, FIXED_CLOCK);
        Auction a2 = new Auction(listingId, start, end, initialPrice2, buyOutPrice, FIXED_CLOCK);

        // Assert
        assertFalse(a1.sameAs(a2));
    }

    @Test
    void sameAs_shouldReturnFalseWhenBuyOutPriceDiffers() {
        // Arrange
        Price buyOutPrice1 = new Price(new BigDecimal("100.00"));
        Price buyOutPrice2 = new Price(new BigDecimal("110.00"));

        // Act
        Auction a1 = new Auction(listingId, start, end, initialPrice, buyOutPrice1, FIXED_CLOCK);
        Auction a2 = new Auction(listingId, start, end, initialPrice, buyOutPrice2, FIXED_CLOCK);

        // Assert
        assertFalse(a1.sameAs(a2));
    }

    @Test
    void sameAs_shouldIgnoreBidHistoryAndStatus() {
        // Arrange
        Auction a1 = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        Auction a2 = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        a1.placeBid(buyerId, new Price(new BigDecimal("20.00"))); // changes internal state

        // Act + Assert
        assertTrue(a1.sameAs(a2));
    }


    // --------- CloseIfExpired Method Tests ---------

    @Test
    void closeIfExpired_shouldReturnFalseWhenNotExpiredAndKeepsStatusActive() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        boolean closed = invokeCloseIfExpired(auction);

        // Assert
        assertFalse(closed);
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    @Test
    void closeIfExpired_shouldReturnTrueWhenExpiredWithoutBidsAndEndsWithoutBids() {
        // Arrange
        end = start.plusMinutes(5);
        Auction auction = new Auction(listingId,  start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // expires
        setClock(auction, Clock.offset(FIXED_CLOCK, Duration.ofMinutes(10)));

        // Act
        boolean closed = invokeCloseIfExpired(auction);

        // Assert
        assertTrue(closed);
        assertEquals(AuctionStatus.ENDED_WITHOUT_BIDS, auction.getAuctionStatus());
    }

    @Test
    void closeIfExpired_shouldReturnTrueWhenExpiredWithBidsAndEndsWithWinner() {
        // Arrange
        end  = start.plusMinutes(5);

        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        UserId buyerId = new UserId("buyerId");
        auction.placeBid(buyerId, new Price(new BigDecimal("20.00")));

        setClock(auction, Clock.offset(FIXED_CLOCK, Duration.ofMinutes(10))); // expires

        // Act
        boolean closed = invokeCloseIfExpired(auction);

        // Assert
        assertTrue(closed);
        assertEquals(AuctionStatus.ENDED_WITH_WINNER, auction.getAuctionStatus());
    }

    @Test
    void closeIfExpired_shouldNotCloseWhenAlreadyTerminal() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        auction.placeBid(buyerId, buyOutPrice); // BOUGHT_OUT

        // Act
        boolean closed = invokeCloseIfExpired(auction);

        // Assert
        assertFalse(closed);
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
    }

    @Test
    void closeIfExpired_shouldReturnFalseWhenAlreadyTerminal() {
        // Arrange
        end = start.plusMinutes(5);
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // expires and closes
        setClock(auction, Clock.offset(FIXED_CLOCK, Duration.ofMinutes(10)));
        boolean firstClose = invokeCloseIfExpired(auction);

        // Act
        boolean secondClose = invokeCloseIfExpired(auction);

        // Assert
        assertTrue(firstClose);  // first time closes
        assertFalse(secondClose); // second time can't close
        assertTrue(
                auction.getAuctionStatus() == AuctionStatus.ENDED_WITHOUT_BIDS
                        || auction.getAuctionStatus() == AuctionStatus.ENDED_WITH_WINNER
                        || auction.getAuctionStatus() == AuctionStatus.BOUGHT_OUT
        );
    }


    // --------- placeBid Method Tests ---------

    @Test
    void placeBid_shouldThrowWhenBidIsLowerThanInitialPrice() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        Price bidValue = new Price(new BigDecimal("5.00"));

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> auction.placeBid(buyerId, bidValue));

        // Assert
        assertEquals("Bid must be higher than initial price", ex.getMessage());
    }

    @Test
    void placeBid_shouldThrowWhenBidEqualsInitialPrice() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        Price equalToInitial = new Price(initialPrice.getPrice());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> auction.placeBid(buyerId, equalToInitial));

        assertEquals("Bid must be higher than initial price", ex.getMessage());
    }

    @Test
    void placeBid_shouldThrowWhenBidIsNotHigherThanLastBid() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        Price bidValue1 = new Price(new BigDecimal("15.00"));
        auction.placeBid(buyerId, bidValue1);
        Price bidValue2 = new Price(new BigDecimal("14.00"));

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> auction.placeBid(buyerId, bidValue2));

        // Assert
        assertEquals("Bid must be higher than last bid", ex.getMessage());
    }

    @Test
    void placeBid_shouldThrowWhenBidEqualsLastBid() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        Price bidValue1 = new Price(new BigDecimal("15.00"));
        auction.placeBid(buyerId, bidValue1);
        Price bidValue2 = new Price(new BigDecimal("15.00"));

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> auction.placeBid(buyerId, bidValue2));

        // Assert
        assertEquals("Bid must be higher than last bid", ex.getMessage());
    }

    @Test
    void placeBid_shouldCloseAuctionWhenBidEqualsBuyoutPrice() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        auction.placeBid(buyerId, buyOutPrice);

        // Assert
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
    }

    @Test
    void placeBid_shouldThrowWhenAuctionIsScheduled() {
        // Arrange
        LocalDateTime futureStart = now().plusDays(1);
        LocalDateTime futureEnd = futureStart.plusDays(1);
        Auction auction = new Auction(listingId, futureStart, futureEnd, initialPrice, buyOutPrice, FIXED_CLOCK);
        Price bidValue1 = new Price(new BigDecimal("20.00"));

        // Act
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> auction.placeBid(buyerId, bidValue1));

        // Assert
        assertEquals("Auction is not active", ex.getMessage());
    }

    @Test
    void placeBid_shouldThrowWhenAuctionIsExpired() {
        // Arrange
        LocalDateTime endSoon = start.plusMinutes(1);
        Auction auction = new Auction(listingId, start, endSoon, initialPrice, buyOutPrice, FIXED_CLOCK);
        setClock(auction, Clock.offset(FIXED_CLOCK, Duration.ofMinutes(10))); // expired
        Price bidValue1 = new Price(new BigDecimal("15.00"));

        // Act
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> auction.placeBid(buyerId, bidValue1));

        // Assert
        assertEquals("Auction is not active", ex.getMessage());
    }

    @Test
    void placeBid_shouldThrowWhenAuctionIsTerminal() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        auction.placeBid(buyerId, buyOutPrice); // BOUGHT_OUT

        Price newBid = new Price(new BigDecimal("200.00"));

        // Act
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> auction.placeBid(new UserId("other"), newBid));

        // Assert
        assertEquals("Auction is not active", ex.getMessage());
    }

    @Test
    void placeBid_shouldThrowWhenBidderIdIsNull() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> auction.placeBid(null, new Price(new BigDecimal("20.00"))));

        // Assert
        assertEquals("Bidder ID cannot be null", ex.getMessage());
    }

    @Test
    void placeBid_shouldThrowWhenBidValueIsNull() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> auction.placeBid(buyerId, null));

        // Assert
        assertEquals("Bid value cannot be null", ex.getMessage());
    }

    @Test
    void placeBid_shouldAddBidToHistory() {
        // Arrange
        Price price = new Price(new BigDecimal("20.00"));
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        auction.placeBid(buyerId, price);

        assertEquals(1, auction.getBidHistory().size());
    }

    // --------- winningBid Method Tests ---------

    @Test
    void placeBid_shouldSetWinningBidOnFirstBid() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        Price bidPrice = new Price(new BigDecimal("20.00"));

        // Act
        Bid bid = auction.placeBid(buyerId, bidPrice);

        // Assert
        assertEquals(bid, auction.getBidHistory().get(0));
    }

    @Test
    void placeBid_shouldUpdateWinningBidOnEachBid() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        auction.placeBid(buyerId, new Price(new BigDecimal("20.00")));
        Bid last = auction.placeBid(buyerId, new Price(new BigDecimal("30.00")));

        // Assert
        assertEquals(last, auction.getBidHistory().get(1));
    }

    @Test
    void placeBid_shouldSetFinalPriceToBuyoutWhenMatched() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        auction.placeBid(buyerId, buyOutPrice);

        // Assert
        assertEquals(buyOutPrice, auction.getFinalPrice());
    }

    @Test
    void placeBid_shouldTriggerBuyoutEvenAfterExistingBids() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        UserId bidder1 = mock(UserId.class);
        UserId bidder2 = mock(UserId.class);

        auction.placeBid(bidder1, new Price(new BigDecimal("50.00")));

        // Act
        Bid finalBid = auction.placeBid(bidder2, buyOutPrice);

        // Assert
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
        assertEquals(finalBid, auction.getWinningBid());
        assertEquals(buyOutPrice, auction.getFinalPrice());
    }

    @Test
    void placeBid_shouldThrowExceptionWhenAuctionAlreadyBoughtOut() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        auction.placeBid(mock(UserId.class), buyOutPrice);

        // Act
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> auction.placeBid(mock(UserId.class), new Price(new BigDecimal("200.00"))));

        // Assert
        assertEquals("Auction is not active", ex.getMessage());
    }

    // --------- refreshStatus Method Tests ---------

    @Test
    void refreshStatus_shouldSetActiveWhenNowIsAfterStart() {
        // Arrange
        LocalDateTime futureStart = now().plusMinutes(10);
        LocalDateTime futureEnd = futureStart.plusDays(1);
        Auction auction = new Auction(listingId, futureStart, futureEnd, initialPrice, buyOutPrice, FIXED_CLOCK);

        setClock(auction, Clock.offset(FIXED_CLOCK, Duration.ofMinutes(15)));

        // Act
        auction.refreshStatus();

        // Assert
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    @Test
    void refreshStatus_shouldTransitionFromScheduledToActive() {
        // Arrange
        LocalDateTime futureStart = now().plusHours(2);
        LocalDateTime futureEnd = futureStart.plusDays(1);

        Auction auction = new Auction(listingId, futureStart, futureEnd, initialPrice, buyOutPrice, FIXED_CLOCK);

        setClock(auction, Clock.offset(FIXED_CLOCK, Duration.ofHours(3))); // after start

        // Act
        auction.refreshStatus();

        // Assert
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    @Test
    void refreshStatus_shouldNotChangeTerminalStatus() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        auction.placeBid(buyerId, buyOutPrice); // BOUGHT_OUT

        AuctionStatus before = auction.getAuctionStatus();
        setClock(auction, Clock.offset(FIXED_CLOCK, Duration.ofDays(10)));

        // Act
        auction.refreshStatus();

        // Assert
        assertEquals(before, auction.getAuctionStatus());
    }

    @Test
    void refreshStatus_shouldKeepActiveWhenNowIsBetweenStartAndEnd() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        setClock(auction, Clock.offset(FIXED_CLOCK, Duration.ofMinutes(1)));

        // Act
        auction.refreshStatus();

        // Assert
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    @Test
    void refreshStatus_shouldSetScheduledWhenNowIsBeforeStart() {
        // Arrange
        LocalDateTime futureStart = now().plusHours(2);
        LocalDateTime futureEnd = futureStart.plusDays(1);
        Auction auction = new Auction(listingId, futureStart, futureEnd, initialPrice, buyOutPrice, FIXED_CLOCK);

        setClock(auction, Clock.offset(FIXED_CLOCK, Duration.ofHours(-1)));

        // Act
        auction.refreshStatus();

        // Assert
        assertEquals(AuctionStatus.SCHEDULED, auction.getAuctionStatus());
    }

    @Test
    void refreshStatus_shouldNotChangeWhenAlreadyScheduled() {
        // Arrange
        LocalDateTime futureStart = now().plusHours(5);
        LocalDateTime futureEnd = futureStart.plusDays(1);
        Auction auction = new Auction(listingId, futureStart, futureEnd, initialPrice, buyOutPrice, FIXED_CLOCK);

        AuctionStatus before = auction.getAuctionStatus();

        // Act
        auction.refreshStatus();

        // Assert
        assertEquals(before, auction.getAuctionStatus());
    }

    // --------- Rehydrate State Tests ---------

    @Test
    void rehydrateState_shouldSetStateFieldsCorrectly() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        Bid bidDouble = mock(Bid.class);
        List<Bid> bidHistory = List.of(bidDouble);
        LocalDateTime closedAt = now();

        // Act
        auction.rehydrateState(bidHistory, AuctionStatus.ENDED_WITH_WINNER, closedAt);

        // Assert
        assertEquals(AuctionStatus.ENDED_WITH_WINNER, auction.getAuctionStatus());
        assertEquals(bidDouble.getValue(), auction.getFinalPrice());
        assertEquals(closedAt, auction.getClosedAt());
    }

    @Test
    void rehydrateState_shouldAllowNullClosedAt() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        Bid bidDouble = mock(Bid.class);
        List<Bid> bidHistory = List.of(bidDouble);

        // Act
        auction.rehydrateState(bidHistory, AuctionStatus.CANCELLED, null);

        // Assert
        assertEquals(AuctionStatus.CANCELLED, auction.getAuctionStatus());
        assertNull(auction.getFinalPrice());
        assertNull(auction.getClosedAt());
    }

    @Test
    void rehydrateState_shouldHandleNullBidHistoryWithTerminalStatus() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        auction.rehydrateState(null, AuctionStatus.ENDED_WITH_WINNER, now());

        // Assert
        assertEquals(AuctionStatus.ENDED_WITH_WINNER, auction.getAuctionStatus());
        assertNull(auction.getFinalPrice());
        assertNull(auction.getWinningBid());
    }

    @Test
    void rehydrateState_shouldHandleEmptyBidHistoryInTerminalState() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        auction.rehydrateState(List.of(), AuctionStatus.ENDED_WITHOUT_BIDS, now());

        // Assert
        assertEquals(AuctionStatus.ENDED_WITHOUT_BIDS, auction.getAuctionStatus());
        assertNull(auction.getWinningBid());
        assertNull(auction.getFinalPrice());
    }

    @Test
    void rehydrateState_shouldNotBeAffectedByExternalListMutation() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        Bid bid = mock(Bid.class);
        List<Bid> externalList = new ArrayList<>(List.of(bid));

        // Act
        auction.rehydrateState(externalList, AuctionStatus.ENDED_WITH_WINNER, now());

        externalList.clear(); // mutate after rehydrate

        // Assert
        assertEquals(1, auction.getBidHistory().size());
    }

    // --------- Rehydrate Bid Tests ---------

    @Test
    void rehydrateBid_shouldReconstructBidCorrectly() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        Price price = new Price(new BigDecimal("25.00"));
        LocalDateTime time = now();

        // Act
        Bid bid = auction.rehydrateBid(buyerId, price, time);

        // Assert
        assertNotNull(bid);
        assertEquals(buyerId, bid.getBidderId());
        assertEquals(price, bid.getValue());
        assertEquals(time, bid.getTimeOfBid());
    }

    @Test
    void rehydrateBid_shouldThrowWhenAnyArgumentIsNull() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> auction.rehydrateBid(null, new Price(new BigDecimal("10.00")), now()));

        assertThrows(IllegalArgumentException.class,
                () -> auction.rehydrateBid(buyerId, null, now()));

        assertThrows(IllegalArgumentException.class,
                () -> auction.rehydrateBid(buyerId, new Price(new BigDecimal("10.00")), null));
    }

    // --------- Getters Tests ---------

    @Test
    void id_shouldReturnAuctionIdGeneratedInConstructor() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        AuctionId result = auction.id();

        // Assert
        assertNotNull(result);
        assertEquals(result, auction.id());
    }

    @Test
    void id_shouldBeUniqueAcrossDifferentAuctions() {
        // Arrange
        Auction a1 = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        Auction a2 = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        AuctionId id1 = a1.id();
        AuctionId id2 = a2.id();

        // Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void getListingId_shouldReturnListingIdPassedInConstructor() {
        // Arrange
        ListingId expected = new ListingId("listingId123");
        Auction auction = new Auction(expected, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        ListingId result = auction.getListingId();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void getBidHistory_shouldReturnUnmodifiableList() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        var history = auction.getBidHistory();

        // Assert
        assertThrows(UnsupportedOperationException.class,
                () -> history.add(mock(Bid.class)));
    }

    @Test
    void getBidHistory_shouldStartEmpty() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act
        List<Bid> history = auction.getBidHistory();

        // Assert
        assertTrue(history.isEmpty());
    }

    @Test
    void getWinningBid_shouldBeNullBeforeAnyBid() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act + Assert
        assertNull(auction.getWinningBid());
    }

    @Test
    void getWinningBid_shouldReturnTheHighestBid() {
        // Arrange
        Auction auction = new AuctionFactory().create(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        Bid bid2 = auction.placeBid(mock(UserId.class), new Price(new BigDecimal("30"))); // winning bid

        // Act
        Bid winning = auction.getWinningBid();

        // Assert
        assertNotNull(winning);
        assertEquals(bid2, winning);
    }

    @Test
    void getWinningBid_shouldReturnStoredWinningBidWhenTerminal() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        Bid bid = mock(Bid.class);
        Price price = new Price(new BigDecimal("50.00"));

        when(bid.getValue()).thenReturn(price);

        auction.rehydrateState(List.of(bid), AuctionStatus.ENDED_WITH_WINNER, now());

        // Act
        Bid result = auction.getWinningBid();

        // Assert
        assertEquals(bid, result);
    }

    @Test
    void getFinalPrice_shouldBeNullBeforeAnyBid() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act + Assert
        assertNull(auction.getFinalPrice());
    }

    @Test
    void getClosedAt_shouldBeNullBeforeAuctionCloses() {
        // Arrange
        Auction auction = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Act + Assert
        assertNull(auction.getClosedAt());
    }

    // --------- HashCode Method Tests ---------

    @Test
    void hashCode_shouldDifferWhenDifferentAuctions() {
        // Act
        Auction a1 = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);
        Auction a2 = new Auction(listingId, start, end, initialPrice, buyOutPrice, FIXED_CLOCK);

        // Assert
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

    private static Auction createActiveAuction(String initial, String buyout) {
        ListingId listingId = new ListingId("listingId");
        LocalDateTime start = now();
        LocalDateTime end = start.plusDays(2);
        return new Auction(listingId, start, end, mockPrice(initial), mockPrice(buyout), FIXED_CLOCK);
    }

    private static Auction createScheduledAuction(String initial, String buyout) {
        ListingId listingId = new ListingId("listingId");
        LocalDateTime start = now().plusMinutes(5);
        LocalDateTime end = start.plusDays(2);
        return new Auction(listingId, start, end, mockPrice(initial), mockPrice(buyout), FIXED_CLOCK);
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