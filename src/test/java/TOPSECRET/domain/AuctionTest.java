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

class AuctionTest {

    // ============================================================
    // ========================= Test Clock =======================
    // ============================================================

    private static final ZoneId ZONE = ZoneId.of("UTC");
    private static final Instant FIXED_INSTANT = Instant.parse("2030-01-01T10:00:00Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, ZONE);

    private static LocalDateTime fixedNow() {
        return LocalDateTime.ofInstant(FIXED_INSTANT, ZONE);
    }

    // ============================================================
    // ============================ Helpers =======================
    // ============================================================

    private static Price price(String value) {
        return new Price(new BigDecimal(value));
    }

    private static Auction createValidScheduledAuction() {
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();

        LocalDateTime initialDate = now.plusMinutes(5); // future => SCHEDULED
        LocalDateTime finalDate = now.plusDays(2);

        return new Auction(listing, initialDate, finalDate, price("150.00"), FIXED_CLOCK);
    }

    private static Auction createValidActiveAuction() {
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();

        LocalDateTime initialDate = now;
        LocalDateTime finalDate = now.plusDays(2);

        return new Auction(listing, initialDate, finalDate, price("150.00"), FIXED_CLOCK);
    }

    private static void setPrivateField(Object target, String fieldName, Object value) {
        try {
            var f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            fail("Failed to set field '" + fieldName + "': " + e.getMessage());
        }
    }

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



    // ============================================================
    // ====================== Basic constructor ===================
    // ============================================================

    @Test
    void testEmptyConstructor() {
        // arrange + act
        Auction auction = new Auction();
        // assert
        assertNotNull(auction);
    }

    // ============================================================
    // ====================== Constructor validations =============
    // ============================================================

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
        // arrange + act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Auction(listing, initialDate, finalDate, buyOutPrice, FIXED_CLOCK)
        );

        // assert
        assertEquals(expectedMessage, ex.getMessage());
    }



    @Test
    void testConstructor_nullClock_throws() {
        // arrange
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();

        // act + assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Auction(listing, now.plusMinutes(1), now.plusDays(1), price("150.00"), null)
        );

        assertEquals("Clock cannot be null", ex.getMessage());
    }

    @Test
    void testValidConstructor_initializesEmptyBidHistory() {

        // arrange
        Auction auction = createValidScheduledAuction();

        // act + assert
        assertNotNull(auction.get_bidHistory());
        assertTrue(auction.get_bidHistory().isEmpty());
    }

    // ============================================================
    // ====================== AuctionStatus (Clock) ===============
    // ============================================================

    static Stream<Arguments> auctionStatusClockCases() {
        LocalDateTime now = fixedNow();

        return Stream.of(
                Arguments.of("initialDate after now => SCHEDULED", now.plusSeconds(1), now.plusDays(1), AuctionStatus.SCHEDULED),
                Arguments.of("initialDate equals now => ACTIVE", now, now.plusDays(1), AuctionStatus.ACTIVE)
        );
    }

    // ------- Initial Auction Status - Active/Scheduled
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("auctionStatusClockCases")
    void testAuctionStatus_withClock(
            String name,
            LocalDateTime initialDate,
            LocalDateTime finalDate,
            AuctionStatus expectedStatus
    ) {
        // arrange
        Listing listing = instantiateListing();

        // act
        Auction auction = new Auction(listing, initialDate, finalDate, price("150.00"), FIXED_CLOCK);

        // assert
        assertEquals(expectedStatus, auction.getAuctionStatus());
    }

    @Test
    void testConstructor_withClock_setsStatusActive_whenInitialDateEqualsNow() {
        // arrange
        Auction auction = createValidActiveAuction();

        // act + assert
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    @Test
    void testConstructor_withoutClock_setsStatusScheduled_whenInitialDateInFuture() {

        // arrange
        Listing listing = instantiateListing();
        LocalDateTime now = LocalDateTime.now();

        Auction auction = new Auction(
                listing,
                now.plusDays(1),
                now.plusDays(2),
                price("150.00")
        );

        // act + assert
        assertEquals(AuctionStatus.SCHEDULED, auction.getAuctionStatus());
    }

    //-------------------------------------------------------------------

    @Test
    void testConstructor_withoutClock_valid_createsEmptyBidHistory() {

       // arrange
        Listing listing = instantiateListing();
        LocalDateTime now = LocalDateTime.now();

        Auction auction = new Auction(
                listing,
                now.plusDays(1),
                now.plusDays(2),
                price("150.00")
        );

        // act + assert
        assertNotNull(auction.get_bidHistory());
        assertTrue(auction.get_bidHistory().isEmpty());
    }



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
                        listing, now.plusDays(1), now.plusDays(2), price("35.50"), price("35.50"),
                        "Buy out price must be greater than Initial price."
                )
        );
    }
/*
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
        // act + assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Auction(listing, initialDate, finalDate, buyOutPrice)
        );

        assertEquals(expectedMessage, ex.getMessage());
    }

 */


    // ============================================================
    // ======================= acceptBid tests ====================
    // ============================================================

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
                            auction.acceptBid(new Bid(instantiateUser("Rodolph"), price("35.50"))); // == initial
                        },
                        "Bid value must be greater than initial Price.",
                        true
                ),
                Arguments.of(
                        "bid below initial price must throw",
                        (Executable) () -> {
                            Auction auction = createValidActiveAuction();
                            auction.acceptBid(new Bid(instantiateUser("Rodolph"), price("35.20"))); // < initial
                        },
                        "Bid value must be greater than initial Price.",
                        true
                ),
                Arguments.of(
                        "bid equal to last bid must throw (>=)",
                        (Executable) () -> {
                            Auction auction = createValidActiveAuction();
                            auction.acceptBid(new Bid(instantiateUser("Rodolph"), price("40.00")));
                            auction.acceptBid(new Bid(instantiateUser("Albert"), price("40.00"))); // == last
                        },
                        "Bid value must be greater than last bid value:",
                        false
                ),
                Arguments.of(
                        "bid below last bid must throw",
                        (Executable) () -> {
                            Auction auction = createValidActiveAuction();
                            auction.acceptBid(new Bid(instantiateUser("Rodolph"), price("40.00")));
                            auction.acceptBid(new Bid(instantiateUser("Albert"), price("39.00"))); // < last
                        },
                        "Bid value must be greater than last bid value:",
                        false
                )
        );
    }
/*
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("invalidAcceptBidCases")
    void testAcceptBid_invalidCases_throwAndMessage(
            String name,
            Executable executable,
            String expectedMessage,
            boolean exactMatch
    ) {
        // act + assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, executable);

        if (exactMatch) {
            assertEquals(expectedMessage, ex.getMessage());
        } else {
            assertTrue(ex.getMessage().startsWith(expectedMessage));
        }
    }

 */

    static Stream<Arguments> validAcceptBidCases() {
        return Stream.of(
                Arguments.of("first bid just above initial", List.of("35.51"), 1),
                Arguments.of("two increasing bids", List.of("35.51", "40.00"), 2)
        );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("validAcceptBidCases")
    void testAcceptBid_validCases_returnsTrue_andAddsToHistory(
            String name,
            List<String> bidValues,
            int expectedSize
    ) {
        // arrange
        Auction auction = createValidActiveAuction();

        //act + assert
        int i = 0;
        for (String v : bidValues) {
            boolean result = auction.acceptBid(new Bid(instantiateUser("Bidder" + (++i)), price(v)));
            assertTrue(result);
        }

        assertEquals(expectedSize, auction.get_bidHistory().size());
    }

    @Test
    void testAcceptBid_whenThrows_doesNotChangeBidHistory() {

        // arrange
        Auction auction = createValidActiveAuction();

        // act + assert
        assertTrue(auction.acceptBid(new Bid(instantiateUser("Rodolph"), price("40.00"))));
        int sizeBefore = auction.get_bidHistory().size();

        assertThrows(IllegalArgumentException.class,
                () -> auction.acceptBid(new Bid(instantiateUser("Albert"), price("39.00"))));

        assertEquals(sizeBefore, auction.get_bidHistory().size());
    }

    @Test
    void testAcceptBid_whenScheduled_throwsException_andDoesNotBid() {
        // arrange
        Auction auction = createValidScheduledAuction();
        Bid bid = new Bid(instantiateUser("Rodolph"), price("40.00"));

        // act + assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> auction.acceptBid(bid));
        assertEquals("Auction is not active", exception.getMessage());

    }

    @Test
    void testAcceptBid_whenBidEqualsBuyOutPrice_colsesAsBoughtOut() {

        // arrange
        Auction auction = createValidActiveAuction();
        Bid buyOutBid = new Bid(instantiateUser("Rodolph"), price("150.00"));

        boolean result = auction.acceptBid(buyOutBid);

        assertTrue(result);

        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
        assertEquals(1, auction.get_bidHistory().size());

    }

    // ------ Bid value biggest than buyOutPrice, auction status still Active

    @Test
    void testAcceptBid_bidGreaterThanBuyOut_doesNotCloseBecauseEqualsIsUsed() {

        // arrange
        Auction auction = createValidActiveAuction();
        Bid bid = new Bid(instantiateUser("Rodolph"), price("150.01"));

        // act + assert
        assertTrue(auction.acceptBid(bid));
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
        assertEquals(1, auction.get_bidHistory().size());
    }

    @Test
    void testAcceptBid_scheduledButStartTimeReached_requiresRefreshStatus_toAcceptBid() {
        LocalDateTime now = fixedNow();
        Listing listing = instantiateListing();

        // starts in 5 minutes => SCHEDULED at creation time
        Auction auction = new Auction(
                listing,
                now.plusMinutes(5),
                now.plusDays(2),
                price("150.00"),
                FIXED_CLOCK
        );

        assertEquals(AuctionStatus.SCHEDULED, auction.getAuctionStatus());

        // move time forward to after initialDate but before finalDate
        Clock afterStart = Clock.fixed(FIXED_INSTANT.plusSeconds(6 * 60L), ZONE);
        setPrivateField(auction, "_clock", afterStart);

        // now a bid should be accepted (because refreshStatus should flip to ACTIVE)
        assertTrue(auction.acceptBid(new Bid(instantiateUser("ROdolph"), price("40.00"))));
        assertEquals(1, auction.get_bidHistory().size());
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

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

    // ----- Test AcceptsBid Expired Auctions ---------
    @Test
    void testExpiry_withoutBids_shouldThrow_auctionStatusEndedWithoutBids_bidHistoryIsEmpty() {

        // arrange
        Auction auction = createValidActiveAuction();
        Clock afterEnd = Clock.fixed(FIXED_INSTANT.plus(Duration.ofDays(3)), ZONE);
        setPrivateField(auction, "_clock", afterEnd);
        Bid bid = new Bid(instantiateUser("Rodolph"), price("40.00"));

        // act + assert
        assertThrows(IllegalStateException.class, () -> auction.acceptBid(bid));
        assertEquals(AuctionStatus.ENDED_WITHOUT_BIDS, auction.getAuctionStatus());
        assertTrue(auction.get_bidHistory().isEmpty());

        // when it closes without bids, winingBid and finalPrice are null
        LocalDateTime closedAt = getPrivateField(auction, "_closedAt", LocalDateTime.class);

        assertNotNull(closedAt);
        assertNull(getPrivateField(auction, "_winningBid", Bid.class));
        assertNull(getPrivateField(auction, "_finalPrice", Price.class));
    }

    @Test
    void testExpiry_withBids_setsEndedWithWinner_andKeepsLastBidAsWinner() {

        // arrange
        Auction auction = createValidActiveAuction();

        Bid bid1 = new Bid(instantiateUser("Rodolph"), price("40.00"));
        assertTrue(auction.acceptBid(bid1));

        Bid bid2 = new Bid(instantiateUser("ALbert"), price("45.00"));
        assertTrue(auction.acceptBid(bid2));

        int sizeBefore = auction.get_bidHistory().size();

        Clock afterEnd = Clock.fixed(FIXED_INSTANT.plusSeconds(60L * 60L * 24L * 3), ZONE);
        setPrivateField(auction, "_clock", afterEnd);

        // act
        Bid lateBid = new Bid(instantiateUser("ROn Weasley"), price("50.00"));
        assertThrows(IllegalStateException.class, () -> auction.acceptBid(lateBid));

        // assert
        assertEquals(AuctionStatus.ENDED_WITH_WINNER, auction.getAuctionStatus());
        assertEquals(sizeBefore, auction.get_bidHistory().size());

        Bid winningBid = getPrivateField(auction, "_winningBid", Bid.class);
        Price finalPrice = getPrivateField(auction, "_finalPrice", Price.class);
        LocalDateTime closedAt = getPrivateField(auction, "_closedAt", LocalDateTime.class);

        assertNotNull(winningBid);
        assertNotNull(finalPrice);
        assertNotNull(closedAt);

        assertEquals(auction.get_bidHistory().get(auction.get_bidHistory().size() -1), winningBid);
        assertEquals(winningBid.getValue(), finalPrice);

    }

    // ============================================================
    // ======================= RefreshStatus tests ====================
    // ============================================================

    @Test
    void testRefreshStatus_doesNotChangeTerminalStatus() {

        Auction auction = createValidActiveAuction();

        // force BOUGHT_OUT through real behavior
        auction.acceptBid(new Bid(instantiateUser("Buyer"), price("150.00")));
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());

        // advance time a 1 year and call refreshStatus
        Clock farFuture = Clock.fixed(FIXED_INSTANT.plusSeconds(60L * 60L * 24L * 365L), ZONE); // +1 year
        setPrivateField(auction, "_clock", farFuture);

        auction.refreshStatus();

        // must remain terminal
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
    }

    @Test
    void testRefreshStatus_terminalBoughtOut_mustNotChangeToActive() {

        // arrange
        Auction auction = createValidActiveAuction();

        // Make it terminal: BOUGHT_OUT
        assertTrue(auction.acceptBid(new Bid(instantiateUser("Buyer"), price("150.00"))));
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());

        // Set clock to a time that is between initialDate and finalDate
        // If refreshStatus did not return early, it would set status to ACTIVE.
        Clock withinWindow = Clock.fixed(FIXED_INSTANT.plusSeconds(60L), ZONE); // +1 minute
        setPrivateField(auction, "_clock", withinWindow);

        auction.refreshStatus();

        // Must remain terminal
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
    }

    // ============================================================
    // ================== CloseIfExpired tests ====================
    // ============================================================

    @Test
    void testCloseIfExpired_whenTerminalStatus_returnsFalse() {
        Auction auction = createValidActiveAuction();

        assertTrue(auction.acceptBid( new Bid(instantiateUser("Rodolph"), price("150.00"))));
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());

        boolean result = invokeCloseIfExpired(auction);

        assertFalse(result);
        assertEquals(AuctionStatus.BOUGHT_OUT, auction.getAuctionStatus());
    }

    @Test
    void testCloseIfExpired_whenNotExpired_returnsFalse() {

        Auction auction = createValidActiveAuction();
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());

        boolean result = invokeCloseIfExpired(auction);

        assertFalse(result);
        assertEquals(AuctionStatus.ACTIVE, auction.getAuctionStatus());
    }

    @Test
    void testCloseIfExpired_whenExpired_returnsTrue_andClosesAuction() {
        Auction auction = createValidActiveAuction();

        Clock afterEnd = Clock.fixed(FIXED_INSTANT.plusSeconds(60L * 60L * 24L * 3L), ZONE);
        setPrivateField(auction, "_clock", afterEnd);

        boolean reault = invokeCloseIfExpired(auction);

        assertTrue(reault);
        assertEquals(AuctionStatus.ENDED_WITHOUT_BIDS, auction.getAuctionStatus());
    }



    // ============================================================
    // ===================== equals & hashCode tests ==============
    // ============================================================

    @Test
    void testEquals_reflexive() {
        Auction auction = createValidScheduledAuction();
        assertEquals(auction, auction);
    }

    @Test
    void testEquals_null_andDifferentClass() {
        // arrange + act
        Auction auction = createValidScheduledAuction();

        // assert
        assertNotEquals(null, auction);
        assertNotEquals("not an auction", auction);
    }

    @Test
    void testEquals_sameData_true_andHashCodeSame() {

       // arrange
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();
        LocalDateTime initial = now.plusMinutes(5);
        LocalDateTime fin = now.plusDays(2);

        // act
        Auction a1 = new Auction(listing, initial, fin, price("150.00"), FIXED_CLOCK);
        Auction a2 = new Auction(listing, initial, fin, price("150.00"), FIXED_CLOCK);

        // assert
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testEquals_differentFinalDate_false() {

        // arrange
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();
        LocalDateTime initial = now.plusMinutes(5);

        // act
        Auction a1 = new Auction(listing, initial, initial.plusDays(1), price("150.00"), FIXED_CLOCK);
        Auction a2 = new Auction(listing, initial, initial.plusDays(2), price("150.00"), FIXED_CLOCK);

        // assert
        assertNotEquals(a1, a2);
    }

//    @Test
//    void testEquals_differentBidHistory_false() {
//
//        // arrange
//        Auction a1 = createValidActiveAuction();
//        Auction a2 = createValidActiveAuction();
//
//        // act
//        a1.acceptBid(new Bid(instantiateUser("Rodolph"), price("40.00")));
//
//        // assert
//        assertNotEquals(a1, a2);
//    }

    @Test
    void testEquals_withNullAndDifferentClass() {
        Auction auction = createValidActiveAuction();

        assertNotEquals(null, auction);
        assertFalse(auction.equals("not an auction"));
    }

    @Test
    void testHashCode_differentAuctions_shouldDiffer() {
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();

        Auction auction1 = new Auction(listing, now.plusMinutes(5), now.plusDays(2), price("150.00"), FIXED_CLOCK);
        Auction auction2 = new Auction(listing, now.plusMinutes(5), now.plusDays(3), price("150.50"), FIXED_CLOCK);

        assertNotEquals(auction1, auction2);
        assertNotEquals(auction1.hashCode(), auction2.hashCode());

    }

    // ============================================================
    // ======= Auxiliary methods to instantiate Objects ===========
    // ============================================================

    private static List<Picture> instantiatePictures() {
        Picture pic = new Picture("picture_url");
        List<Picture> pictures = new ArrayList<>();
        pictures.add(pic);
        return pictures;
    }

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
                instantiateUser("Joseph"),
                instantiatePictures()
        );
    }

    private static User instantiateUser(String name) {
        Email email = new Email("switcher", "example.com");
        Address add = new Address("Rua x", "12B", "4444-111", new Country("PORTUGAL"));
        Contact contact = new Contact("911111111", "351", email, add);
        LocalDate birthday = LocalDate.of(1982, 12, 3);
        Profile profile = new Profile("Albert Trump", new Country("EL_SALVADOR"), birthday);
        UserRole userRole = UserRole.NORMAL_USER;
        return new User(name, contact, profile,userRole);
    }

    private static BibliographicalData instantiateBibliographicalData() {
        Profile authorProfile = new Profile("J. R. R. Tolkien", new Country("UNITED_KINGDOM"), LocalDate.of(1892, 1, 3));
        Author author = new Author(authorProfile, "valid bio");

        List<Author> authors = new ArrayList<>();
        authors.add(author);


        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre ("Fantasy"));
        genres.add(new Genre ("Mister"));

        String chunkOfText =
                "Continuing the story begun in The Hobbit, all three parts of the epic masterpiece, " +
                        "The Lord of the Rings, in one paperback. Features the definitive edition of the text, " +
                        "fold-out flaps with the original two-colour maps, and a revised and expanded index.";

//        Contact contact = new Contact();
//        Publisher publisher = new Publisher("HarperCollins Publishers", "1111111111", contact);

        return new BibliographicalData(
                "The Lord Of The Rings",
                authors,
                Locale.ENGLISH,
//                publisher,
                LocalDate.of(1991, 11, 1),
                List.of(new Genre ("Fantasy")),
//                700,
                chunkOfText
        );
    }

    private static Publication createValidPublication(BibliographicalData bd) {
        // PublicationType
        PublicationType publicationType = new PublicationType(
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );

        // User (seller/owner of the publication)
        User user = instantiateUser("maria1");

        // Code
        Code code = Code.fromIsbn13("9781234567890");

        // Edition
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        return new Publication(
                bd,
                publicationType,
                user,
                PublicationStatus.FOR_SALE,
//                "1st edition",
                edition,
                code
        );
    }

    private static Publication createUniquePublication(BibliographicalData bd) {
        // PublicationType
        PublicationType publicationType = new PublicationType(
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );

        // User (seller/owner of the publication)
        User user = instantiateUser("maria1");

        // Code
        Code code = Code.fromIsbn13("978" + System.nanoTime());

        // Edition
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        return new Publication(
                bd,
                publicationType,
                user,
                PublicationStatus.FOR_SALE,
//                "1st edition",
                edition,
                code
        );
    }

    private static PublicationSample instantiateItem(BibliographicalData bd) {

        return new PublicationSample(
                bd,
                instantiateUser("Johny123"),
                PublicationStatus.FOR_SALE,
                instantiateEdition(),
                Condition.LIKE_NEW
        );
    }

    private static Edition instantiateEdition(){
        Code code = Code.fromIsbn13 ("978316148412");
        Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        return new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    }
}
