package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Auction}.
 * <p>
 * This test suite validates:
 * <ul>
 *   <li>Constructor validation rules (with and without explicit {@link Clock})</li>
 *   <li>Initial auction status transitions (scheduled/active)</li>
 *   <li>Bid acceptance rules and buyout behavior</li>
 *   <li>Expiration handling and terminal states</li>
 *   <li>Status refresh behavior</li>
 *   <li>{@code equals()} and {@code hashCode()} contracts</li>
 * </ul>
 */
class AuctionTest {

    /**
     * Fixed test zone used to make time-dependent tests deterministic.
     */
    private static final ZoneId ZONE = ZoneId.of("UTC");

    /**
     * Fixed instant used as the reference "now" in tests.
     */
    private static final Instant FIXED_INSTANT = Instant.parse("2030-01-01T10:00:00Z");

    /**
     * Fixed clock used to control time in constructor and bidding tests.
     */
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, ZONE);

    /**
     * Returns the fixed current date-time used in tests.
     *
     * @return deterministic "now" based on {@link #FIXED_INSTANT} and {@link #ZONE}
     */
    private static LocalDateTime fixedNow() {
        return LocalDateTime.ofInstant(FIXED_INSTANT, ZONE);
    }

    /**
     * Creates a {@link Price} from a numeric string.
     *
     * @param value numeric value as string
     * @return a price instance
     */
    private static Price price(String value) {
        return new Price(new BigDecimal(value));
    }

    /**
     * Creates a valid scheduled auction (start date in the future) using the fixed clock.
     *
     * @return a scheduled auction ready for testing
     */
    private static Auction createValidScheduledAuction() {
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();

        LocalDateTime initialDate = now.plusMinutes(5);
        LocalDateTime finalDate = now.plusDays(2);

        return new Auction(listing, initialDate, finalDate, price("150.00"), FIXED_CLOCK);
    }

    /**
     * Creates a valid active auction (start date equals "now") using the fixed clock.
     *
     * @return an active auction ready for testing
     */
    private static Auction createValidActiveAuction() {
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();

        LocalDateTime initialDate = now;
        LocalDateTime finalDate = now.plusDays(2);

        return new Auction(listing, initialDate, finalDate, price("150.00"), FIXED_CLOCK);
    }

    /**
     * Sets a private field via reflection for controlled test setup.
     *
     * @param target object whose field will be updated
     * @param fieldName private field name
     * @param value value to assign
     */
    private static void setPrivateField(Object target, String fieldName, Object value) {
        try {
            var f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            fail("Failed to set field '" + fieldName + "': " + e.getMessage());
        }
    }

    /**
     * Reads a private field via reflection for state assertions.
     *
     * @param target object whose field will be read
     * @param fieldName private field name
     * @param type expected field type
     * @param <T> generic field type
     * @return field value cast to the requested type
     */
    @SuppressWarnings("unchecked")
    private static <T> T getPrivateField(Object target, String fieldName, Class<T> type) {
        try {
            var f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return (T) f.get(target);
        } catch (Exception e) {
            fail("Failed to get field '" + fieldName + "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Invokes the private {@code closeIfExpired()} method via reflection.
     *
     * @param auction auction instance under test
     * @return the boolean result returned by {@code closeIfExpired()}
     */
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

    /**
     * Verifies that the no-args constructor creates a non-null auction instance.
     */
    @Test
    void testEmptyConstructor() {
        Auction auction = new Auction();
        assertNotNull(auction);
    }

    /**
     * Provides invalid constructor scenarios for {@link Auction} when a fixed clock is supplied.
     * <p>
     * Each case includes invalid input and the expected exception message.
     * </p>
     *
     * @return a stream of invalid constructor test cases
     */
    static Stream<Arguments> invalidAuctionConstructorCases_withClock() {
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();
        LocalDateTime past = now.minusSeconds(1);
        LocalDateTime future1 = now.plusMinutes(5);
        LocalDateTime future2 = now.plusDays(2);

        return Stream.of(
                Arguments.of(
                        "null listing",
                        null, future1, future2, price("150.00"),
                        "Listing cannot be null."
                ),
                Arguments.of(
                        "null initialDate",
                        listing, null, future2, price("150.00"),
                        "Initial date must not be before the current date."
                ),
                Arguments.of(
                        "initialDate in the past",
                        listing, past, future2, price("150.00"),
                        "Initial date must not be before the current date."
                ),
                Arguments.of(
                        "null finalDate",
                        listing, future1, null, price("150.00"),
                        "Final date must not be before the initial date."
                ),
                Arguments.of(
                        "finalDate == initialDate",
                        listing, future1, future1, price("150.00"),
                        "Final date must not be before the initial date."
                ),
                Arguments.of(
                        "finalDate before initialDate",
                        listing, future2, future1, price("150.00"),
                        "Final date must not be before the initial date."
                ),
                Arguments.of(
                        "buyOutPrice equal to initialPrice",
                        listing, future1, future2, price("35.00"),
                        "Buy out price must be greater than Initial price."
                ),
                Arguments.of(
                        "buyOutPrice below initialPrice",
                        listing, future1, future2, price("10.00"),
                        "Buy out price must be greater than Initial price."
                )
        );
    }

    /**
     * Verifies that invalid constructor inputs (with explicit clock) throw
     * {@link IllegalArgumentException} with the expected message.
     *
     * @param name test case description
     * @param listing listing input
     * @param initialDate start date input
     * @param finalDate end date input
     * @param buyOutPrice buyout price input
     * @param expectedMessage expected exception message
     */
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("invalidAuctionConstructorCases_withClock")
    void testConstructorInvalidCases_throwAndMessage(
            String name,
            Listing listing,
            LocalDateTime initialDate,
            LocalDateTime finalDate,
            Price buyOutPrice,
            String expectedMessage
    ) {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Auction(listing, initialDate, finalDate, buyOutPrice, FIXED_CLOCK)
        );

        assertEquals(expectedMessage, ex.getMessage());
    }

    /**
     * Verifies that providing a null clock to the constructor throws an exception.
     */
    @Test
    void testConstructor_nullClock_throws() {
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Auction(listing, now.plusMinutes(1), now.plusDays(1), price("150.00"), null)
        );

        assertEquals("Clock cannot be null", ex.getMessage());
    }

    /**
     * Verifies that a valid auction constructor initializes an empty bid history.
     */
    @Test
    void testValidConstructor_initializesEmptyBidHistory() {
        Auction auction = createValidScheduledAuction();

        assertNotNull(auction.get_bidHistory());
        assertTrue(auction.get_bidHistory().isEmpty());
    }

    /**
     * Provides constructor scenarios to validate the initial auction status when using a fixed clock.
     *
     * @return a stream of start/end date combinations and expected status
     */
    static Stream<Arguments> auctionStatusClockCases() {
        LocalDateTime now = fixedNow();

        return Stream.of(
                Arguments.of("initialDate after now => SCHEDULED", now.plusSeconds(1), now.plusDays(1), AuctionStatus.SCHEDULED),
                Arguments.of("initialDate equals now => ACTIVE", now, now.plusDays(1), AuctionStatus.ACTIVE)
        );
    }

    /**
     * Verifies the initial status assigned by the constructor when a clock is provided.
     *
     * @param name case description
     * @param initialDate start date
     * @param finalDate end date
     * @param expectedStatus expected initial status
     */
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("auctionStatusClockCases")
    void testAuctionStatus_withClock(
            String name,
            LocalDateTime initialDate,
            LocalDateTime finalDate,
            AuctionStatus expectedStatus
    ) {
        Listing listing = instantiateListing();
        Auction auction = new Auction(listing, initialDate, finalDate, price("150.00"), FIXED_CLOCK);
        assertEquals(expectedStatus, auction.getAuctionStatus());
    }

    /**
     * Verifies that an auction becomes active when the start date equals the current time.
     */
    @Test
    void testConstructor_withClock_setsStatusActive_whenInitialDateEqualsNow() {
        Auction auction = createValidActiveAuction();
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    /**
     * Verifies that the constructor without explicit clock creates a scheduled auction
     * when the start date is in the future.
     */
    @Test
    void testConstructor_withoutClock_setsStatusScheduled_whenInitialDateInFuture() {
        Listing listing = instantiateListing();
        LocalDateTime now = LocalDateTime.now();

        Auction auction = new Auction(
                listing,
                now.plusDays(1),
                now.plusDays(2),
                price("150.00")
        );

        assertEquals(AuctionStatus.SCHEDULED, auction.getAuctionStatus());
    }

    /**
     * Verifies that the constructor without explicit clock initializes an empty bid history.
     */
    @Test
    void testConstructor_withoutClock_valid_createsEmptyBidHistory() {
        Listing listing = instantiateListing();
        LocalDateTime now = LocalDateTime.now();

        Auction auction = new Auction(
                listing,
                now.plusDays(1),
                now.plusDays(2),
                price("150.00")
        );

        assertNotNull(auction.get_bidHistory());
        assertTrue(auction.get_bidHistory().isEmpty());
    }

    /**
     * Provides invalid constructor scenarios for {@link Auction} when no clock is supplied.
     *
     * @return a stream of invalid input combinations and expected messages
     */
    static Stream<Arguments> invalidAuctionConstructorCases_withoutClock() {
        Listing listing = instantiateListing();
        LocalDateTime now = LocalDateTime.now();

        return Stream.of(
                Arguments.of("null listing",
                        null, now.plusDays(1), now.plusDays(2), price("35.50"), price("150.00"),
                        "Listing cannot be null."
                ),
                Arguments.of("initialDate null",
                        listing, null, now.plusDays(2), price("35.50"), price("150.00"),
                        "Initial date must not be before the current date."
                ),
                Arguments.of("initialDate in the past",
                        listing, now.minusDays(1), now.plusDays(2), price("35.50"), price("150.00"),
                        "Initial date must not be before the current date."
                ),
                Arguments.of("finalDate == initialDate",
                        listing, now.plusDays(2), now.plusDays(2), price("35.50"), price("150.00"),
                        "Final date must not be before the initial date."
                ),
                Arguments.of("buyout <= initial",
                        listing, now.plusDays(1), now.plusDays(2), price("35.00"), price("35.00"),
                        "Buy out price must be greater than Initial price."
                )
        );
    }

    /**
     * Verifies that invalid constructor inputs (without explicit clock) throw
     * {@link IllegalArgumentException} with the expected message.
     *
     * @param name case description
     * @param listing listing input
     * @param initialDate start date input
     * @param finalDate end date input
     * @param initialPrice initial price test value (kept for case readability)
     * @param buyOutPrice buyout price input
     * @param expectedMessage expected exception message
     */
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("invalidAuctionConstructorCases_withoutClock")
    void testConstructor_withoutClock_invalidCases_throwAndMessage(
            String name,
            Listing listing,
            LocalDateTime initialDate,
            LocalDateTime finalDate,
            Price initialPrice,
            Price buyOutPrice,
            String expectedMessage
    ) {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Auction(listing, initialDate, finalDate, buyOutPrice)
        );

        assertEquals(expectedMessage, ex.getMessage());
    }

    /**
     * Provides invalid bid acceptance scenarios and expected exception assertions.
     *
     * @return a stream of executable test cases and expected messages
     */
    static Stream<Arguments> invalidAcceptBidCases() {
        return Stream.of(
                Arguments.of(
                        "null bid",
                        (Executable) () -> {
                            Auction auction = createValidActiveAuction();
                            auction.acceptBid(null);
                        },
                        "Bid cannot be null",
                        true
                ),
                Arguments.of(
                        "bid equal to initial price must throw (<=)",
                        (Executable) () -> {
                            Auction auction = createValidActiveAuction();
                            auction.acceptBid(new Bid(instantiateUser("Rodolph"), price("35.00")));
                        },
                        "Bid value must be greater than initial Price.",
                        true
                ),
                Arguments.of(
                        "bid below initial price must throw",
                        (Executable) () -> {
                            Auction auction = createValidActiveAuction();
                            auction.acceptBid(new Bid(instantiateUser("Rodolph"), price("34.00")));
                        },
                        "Bid value must be greater than initial Price.",
                        true
                ),
                Arguments.of(
                        "bid equal to last bid must throw (>=)",
                        (Executable) () -> {
                            Auction auction = createValidActiveAuction();
                            auction.acceptBid(new Bid(instantiateUser("Rodolph"), price("40.00")));
                            auction.acceptBid(new Bid(instantiateUser("Albert"), price("40.00")));
                        },
                        "Bid value must be greater than last bid value:",
                        false
                ),
                Arguments.of(
                        "bid below last bid must throw",
                        (Executable) () -> {
                            Auction auction = createValidActiveAuction();
                            auction.acceptBid(new Bid(instantiateUser("Rodolph"), price("40.00")));
                            auction.acceptBid(new Bid(instantiateUser("Albert"), price("39.00")));
                        },
                        "Bid value must be greater than last bid value:",
                        false
                )
        );
    }

    /**
     * Verifies invalid bid acceptance cases and validates thrown exception messages.
     *
     * @param name case description
     * @param executable action under test
     * @param expectedMessage expected exception message or prefix
     * @param exactMatch whether to use exact message match or prefix match
     */
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("invalidAcceptBidCases")
    void testAcceptBid_invalidCases_throwAndMessage(
            String name,
            Executable executable,
            String expectedMessage,
            boolean exactMatch
    ) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, executable);

        if (exactMatch) {
            assertEquals(expectedMessage, ex.getMessage());
        } else {
            assertTrue(ex.getMessage().startsWith(expectedMessage));
        }
    }

    /**
     * Provides valid bid sequences and expected bid history sizes.
     *
     * @return a stream of valid bidding scenarios
     */
    static Stream<Arguments> validAcceptBidCases() {
        return Stream.of(
                Arguments.of("first bid just above initial", List.of("35.51"), 1),
                Arguments.of("two increasing bids", List.of("35.51", "40.00"), 2)
        );
    }

    /**
     * Verifies that valid bids are accepted and added to the bid history.
     *
     * @param name case description
     * @param bidValues bid values to submit in order
     * @param expectedSize expected final size of bid history
     */
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("validAcceptBidCases")
    void testAcceptBid_validCases_returnsTrue_andAddsToHistory(
            String name,
            List<String> bidValues,
            int expectedSize
    ) {
        Auction auction = createValidActiveAuction();

        int i = 0;
        for (String v : bidValues) {
            boolean result = auction.acceptBid(new Bid(instantiateUser("Bidder" + (++i)), price(v)));
            assertTrue(result);
        }

        assertEquals(expectedSize, auction.get_bidHistory().size());
    }

    /**
     * Verifies that a failed bid attempt does not change the existing bid history.
     */
    @Test
    void testAcceptBid_whenThrows_doesNotChangeBidHistory() {
        Auction auction = createValidActiveAuction();

        assertTrue(auction.acceptBid(new Bid(instantiateUser("Rodolph"), price("40.00"))));
        int sizeBefore = auction.get_bidHistory().size();

        assertThrows(IllegalArgumentException.class,
                () -> auction.acceptBid(new Bid(instantiateUser("Albert"), price("39.00"))));

        assertEquals(sizeBefore, auction.get_bidHistory().size());
    }

    /**
     * Verifies that bids cannot be accepted while the auction is still scheduled.
     */
    @Test
    void testAcceptBid_whenScheduled_throwsException_andDoesNotBid() {
        Auction auction = createValidScheduledAuction();
        Bid bid = new Bid(instantiateUser("Rodolph"), price("40.00"));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> auction.acceptBid(bid));
        assertEquals("Auction is not active", exception.getMessage());
    }

    /**
     * Verifies that a bid equal to the buyout price closes the auction as BOUGHT_OUT.
     */
    @Test
    void testAcceptBid_whenBidEqualsBuyOutPrice_colsesAsBoughtOut() {
        Auction auction = createValidActiveAuction();
        Bid buyOutBid = new Bid(instantiateUser("Rodolph"), price("150.00"));

        boolean result = auction.acceptBid(buyOutBid);

        assertTrue(result);
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
        assertEquals(1, auction.get_bidHistory().size());
    }

    /**
     * Verifies current behavior when a bid is greater than the buyout price.
     * <p>
     * The auction remains ACTIVE because the implementation closes only when the bid
     * value is exactly equal to the buyout price.
     * </p>
     */
    @Test
    void testAcceptBid_bidGreaterThanBuyOut_doesNotCloseBecauseEqualsIsUsed() {
        Auction auction = createValidActiveAuction();
        Bid bid = new Bid(instantiateUser("Rodolph"), price("150.01"));

        assertTrue(auction.acceptBid(bid));
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
        assertEquals(1, auction.get_bidHistory().size());
    }

    /**
     * Verifies that {@link Auction#acceptBid(Bid)} can accept bids after the scheduled start time
     * is reached, because it internally refreshes the auction status.
     */
    @Test
    void testAcceptBid_scheduledButStartTimeReached_requiresRefreshStatus_toAcceptBid() {
        LocalDateTime now = fixedNow();
        Listing listing = instantiateListing();

        Auction auction = new Auction(
                listing,
                now.plusMinutes(5),
                now.plusDays(2),
                price("150.00"),
                FIXED_CLOCK
        );

        assertEquals(AuctionStatus.SCHEDULED, auction.getAuctionStatus());

        Clock afterStart = Clock.fixed(FIXED_INSTANT.plusSeconds(6 * 60L), ZONE);
        setPrivateField(auction, "_clock", afterStart);

        assertTrue(auction.acceptBid(new Bid(instantiateUser("ROdolph"), price("40.00"))));
        assertEquals(1, auction.get_bidHistory().size());
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    /**
     * Verifies that once the auction is bought out, additional bids are rejected
     * and no bid is added to the history.
     */
    @Test
    void testAcceptBid_afterBoughtOut_throws_andDoesNotAdd() {
        Auction auction = createValidActiveAuction();

        assertTrue(auction.acceptBid(new Bid(instantiateUser("Rodolph"), price("150.00"))));
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());

        int sizeBefore = auction.get_bidHistory().size();

        assertThrows(IllegalStateException.class,
                () -> auction.acceptBid(new Bid(instantiateUser("Albert"), price("160.00"))));

        assertEquals(sizeBefore, auction.get_bidHistory().size());
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
    }

    /**
     * Verifies that attempting to bid on an expired auction with no bids:
     * <ul>
     *   <li>throws an exception because the auction is not active</li>
     *   <li>marks the auction as ended without bids</li>
     *   <li>keeps winning bid and final price as {@code null}</li>
     * </ul>
     */
    @Test
    void testExpiry_withoutBids_shouldThrow_auctionStatusEndedWithoutBids_bidHistoryIsEmpty() {
        Auction auction = createValidActiveAuction();
        Clock afterEnd = Clock.fixed(FIXED_INSTANT.plus(Duration.ofDays(3)), ZONE);
        setPrivateField(auction, "_clock", afterEnd);
        Bid bid = new Bid(instantiateUser("Rodolph"), price("40.00"));

        assertThrows(IllegalStateException.class, () -> auction.acceptBid(bid));
        assertEquals(AuctionStatus.ENDED_WITHOUT_BIDS, auction.getAuctionStatus());
        assertTrue(auction.get_bidHistory().isEmpty());

        LocalDateTime closedAt = getPrivateField(auction, "_closedAt", LocalDateTime.class);

        assertNotNull(closedAt);
        assertNull(getPrivateField(auction, "_winningBid", Bid.class));
        assertNull(getPrivateField(auction, "_finalPrice", Price.class));
    }

    /**
     * Verifies that an expired auction with bids is closed with winner status and preserves
     * the last accepted bid as the winning bid.
     */
    @Test
    void testExpiry_withBids_setsEndedWithWinner_andKeepsLastBidAsWinner() {
        Auction auction = createValidActiveAuction();

        Bid bid1 = new Bid(instantiateUser("Rodolph"), price("40.00"));
        assertTrue(auction.acceptBid(bid1));

        Bid bid2 = new Bid(instantiateUser("ALbert"), price("45.00"));
        assertTrue(auction.acceptBid(bid2));

        int sizeBefore = auction.get_bidHistory().size();

        Clock afterEnd = Clock.fixed(FIXED_INSTANT.plusSeconds(60L * 60L * 24L * 3), ZONE);
        setPrivateField(auction, "_clock", afterEnd);

        Bid lateBid = new Bid(instantiateUser("ROn Weasley"), price("50.00"));
        assertThrows(IllegalStateException.class, () -> auction.acceptBid(lateBid));

        assertEquals(AuctionStatus.ENDED_WITH_WINNER, auction.getAuctionStatus());
        assertEquals(sizeBefore, auction.get_bidHistory().size());

        Bid winningBid = getPrivateField(auction, "_winningBid", Bid.class);
        Price finalPrice = getPrivateField(auction, "_finalPrice", Price.class);
        LocalDateTime closedAt = getPrivateField(auction, "_closedAt", LocalDateTime.class);

        assertNotNull(winningBid);
        assertNotNull(finalPrice);
        assertNotNull(closedAt);

        assertEquals(auction.get_bidHistory().get(auction.get_bidHistory().size() - 1), winningBid);
        assertEquals(winningBid.getValue(), finalPrice);
    }

    /**
     * Verifies that refreshing the status does not change a terminal auction status.
     */
    @Test
    void testRefreshStatus_doesNotChangeTerminalStatus() {
        Auction auction = createValidActiveAuction();

        auction.acceptBid(new Bid(instantiateUser("Buyer"), price("150.00")));
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());

        Clock farFuture = Clock.fixed(FIXED_INSTANT.plusSeconds(60L * 60L * 24L * 365L), ZONE);
        setPrivateField(auction, "_clock", farFuture);

        auction.refreshStatus();

        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
    }

    /**
     * Verifies that a terminal BOUGHT_OUT status remains unchanged even if the current
     * time is within the auction active window.
     */
    @Test
    void testRefreshStatus_terminalBoughtOut_mustNotChangeToActive() {
        Auction auction = createValidActiveAuction();

        assertTrue(auction.acceptBid(new Bid(instantiateUser("Buyer"), price("150.00"))));
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());

        Clock withinWindow = Clock.fixed(FIXED_INSTANT.plusSeconds(60L), ZONE);
        setPrivateField(auction, "_clock", withinWindow);

        auction.refreshStatus();

        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
    }

    /**
     * Verifies that invoking {@code closeIfExpired()} on a terminal auction returns {@code false}
     * and preserves the terminal status.
     */
    @Test
    void testCloseIfExpired_whenTerminalStatus_returnsFalse() {
        Auction auction = createValidActiveAuction();

        assertTrue(auction.acceptBid(new Bid(instantiateUser("Rodolph"), price("150.00"))));
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());

        boolean result = invokeCloseIfExpired(auction);

        assertFalse(result);
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
    }

    /**
     * Verifies that invoking {@code closeIfExpired()} before the end date returns {@code false}
     * and does not change the status.
     */
    @Test
    void testCloseIfExpired_whenNotExpired_returnsFalse() {
        Auction auction = createValidActiveAuction();
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());

        boolean result = invokeCloseIfExpired(auction);

        assertFalse(result);
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    /**
     * Verifies that invoking {@code closeIfExpired()} after the end date closes the auction
     * and returns {@code true}.
     */
    @Test
    void testCloseIfExpired_whenExpired_returnsTrue_andClosesAuction() {
        Auction auction = createValidActiveAuction();

        Clock afterEnd = Clock.fixed(FIXED_INSTANT.plusSeconds(60L * 60L * 24L * 3L), ZONE);
        setPrivateField(auction, "_clock", afterEnd);

        boolean reault = invokeCloseIfExpired(auction);

        assertTrue(reault);
        assertEquals(AuctionStatus.ENDED_WITHOUT_BIDS, auction.getAuctionStatus());
    }

    /**
     * Verifies the reflexive property of {@link Auction#equals(Object)}.
     */
    @Test
    void testEquals_reflexive() {
        Auction auction = createValidScheduledAuction();
        assertEquals(auction, auction);
    }

    /**
     * Verifies that an auction is not equal to {@code null} or an object of another type.
     */
    @Test
    void testEquals_null_andDifferentClass() {
        Auction auction = createValidScheduledAuction();

        assertNotEquals(null, auction);
        assertNotEquals("not an auction", auction);
    }

    /**
     * Verifies that auctions with the same significant constructor data are equal
     * and produce the same hash code.
     */
    @Test
    void testEquals_sameData_true_andHashCodeSame() {
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();
        LocalDateTime initial = now.plusMinutes(5);
        LocalDateTime fin = now.plusDays(2);

        Auction a1 = new Auction(listing, initial, fin, price("150.00"), FIXED_CLOCK);
        Auction a2 = new Auction(listing, initial, fin, price("150.00"), FIXED_CLOCK);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    /**
     * Verifies that auctions with different end dates are not equal.
     */
    @Test
    void testEquals_differentFinalDate_false() {
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();
        LocalDateTime initial = now.plusMinutes(5);

        Auction a1 = new Auction(listing, initial, initial.plusDays(1), price("150.00"), FIXED_CLOCK);
        Auction a2 = new Auction(listing, initial, initial.plusDays(2), price("150.00"), FIXED_CLOCK);

        assertNotEquals(a1, a2);
    }

    /**
     * Verifies current equality behavior when bid history differs.
     * <p>
     * This test documents the current behavior of the implementation and may reveal
     * whether bid history is part of equality semantics.
     * </p>
     */
    @Test
    void testEquals_differentBidHistory_false() {
        Auction a1 = createValidActiveAuction();
        Auction a2 = createValidActiveAuction();

        a1.acceptBid(new Bid(instantiateUser("Rodolph"), price("40.00")));

        assertNotEquals(a1, a2);
    }

    /**
     * Additional equality checks against {@code null} and different classes.
     */
    @Test
    void testEquals_withNullAndDifferentClass() {
        Auction auction = createValidActiveAuction();

        assertNotEquals(null, auction);
        assertFalse(auction.equals("not an auction"));
    }

    /**
     * Verifies that different auctions produce different hash codes in this scenario.
     */
    @Test
    void testHashCode_differentAuctions_shouldDiffer() {
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();

        Auction auction1 = new Auction(listing, now.plusMinutes(5), now.plusDays(2), price("150.00"), FIXED_CLOCK);
        Auction auction2 = new Auction(listing, now.plusMinutes(5), now.plusDays(3), price("150.50"), FIXED_CLOCK);

        assertNotEquals(auction1, auction2);
        assertNotEquals(auction1.hashCode(), auction2.hashCode());
    }

    /**
     * Creates a minimal picture list to build listings in tests.
     *
     * @return a list containing one picture
     */
    private static List<Picture> instantiatePictures() {
        Picture pic = new Picture("picture_url");
        List<Picture> pictures = new ArrayList<>();
        pictures.add(pic);
        return pictures;
    }

    /**
     * Creates a valid listing configured for auction tests.
     *
     * @return a listing instance with one publication sample and auction sale type
     */
    private static Listing instantiateListing() {
        List<Picture> pictures = instantiatePictures();
        PublicationSample bookItem = instantiateItem(instantiateBibliographicalData());
        Price price = new Price(new BigDecimal("35.00"));
        final List<PublicationSample> items = new ArrayList<>(List.of(bookItem));
        return new Listing(
                "This is our first listing",
                items,
                SaleType.AUCTION,
                price,
                "short description",
                instantiatePictures()
        );
    }

    /**
     * Creates a valid user for bidding and ownership scenarios.
     *
     * @param name username to assign
     * @return a user instance
     */
    private static User instantiateUser(String name) {
        Email email = new Email("switcher", "example.com");
        Address add = new Address("Rua x", "12B", "4444-111", new Country("PORTUGAL"));
        Contact contact = new Contact("911111111", "351", email, add);
        LocalDate birthday = LocalDate.of(1982, 12, 3);
        Profile profile = new Profile("Albert Trump", new Country("EL_SALVADOR"), birthday);
        UserRole userRole = UserRole.NORMAL_USER;
        return new User(name, contact, profile, userRole);
    }

    /**
     * Creates bibliographical data used by publication and publication sample helpers.
     *
     * @return bibliographical data instance
     */
    private static BibliographicalData instantiateBibliographicalData() {
        Profile authorProfile = new Profile("J. R. R. Tolkien", new Country("UNITED_KINGDOM"), LocalDate.of(1892, 1, 3));
        Author author = new Author(authorProfile, "valid bio");

        List<Author> authors = new ArrayList<>();
        authors.add(author);

        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("Fantasy"));
        genres.add(new Genre("Mister"));

        String chunkOfText =
                "Continuing the story begun in The Hobbit, all three parts of the epic masterpiece, " +
                        "The Lord of the Rings, in one paperback. Features the definitive edition of the text, " +
                        "fold-out flaps with the original two-colour maps, and a revised and expanded index.";

        return new BibliographicalData(
                "The Lord Of The Rings",
                authors,
                Locale.ENGLISH,
                LocalDate.of(1991, 11, 1),
                List.of(new Genre("Fantasy")),
                chunkOfText
        );
    }

    /**
     * Creates a valid {@link Publication} instance for helper scenarios.
     *
     * @param bd bibliographical data
     * @return a valid publication
     */
    private static Publication createValidPublication(BibliographicalData bd) {
        PublicationType publicationType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );

        User user = instantiateUser("maria1");

        Code code = Code.fromIsbn13("9781234567890");

        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition = new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        return new Publication(
                bd,
                publicationType,
                user,
                PublicationStatus.READING,
                edition,
                code
        );
    }

    /**
     * Creates a publication with a pseudo-unique ISBN for helper scenarios.
     *
     * @param bd bibliographical data
     * @return a publication instance with a unique code
     */
    private static Publication createUniquePublication(BibliographicalData bd) {
        PublicationType publicationType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );

        User user = instantiateUser("maria1");

        Code code = Code.fromIsbn13("978000000000" + (System.nanoTime() % 10));

        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition = new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        return new Publication(
                bd,
                publicationType,
                user,
                PublicationStatus.READ,
                edition,
                code
        );
    }

    /**
     * Creates a {@link PublicationSample} used in listing creation helpers.
     *
     * @param bd bibliographical data
     * @return a publication sample instance
     */
    private static PublicationSample instantiateItem(BibliographicalData bd) {
        return new PublicationSample(
                bd,
                instantiateUser("Johny123"),
                PublicationStatus.READING,
                instantiateEdition(),
                Condition.LIKE_NEW
        );
    }

    /**
     * Creates a valid edition helper object.
     *
     * @return an edition instance
     */
    private static Edition instantiateEdition() {
        Code code = Code.fromIsbn13("9783161484123");
        Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        return new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    }
}