package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link Listing}.
 * Tests the creation, validation, and behavior of listings for publication samples.
 */
class ListingTest {

    // ========== TEST DATA SETUP ==========

    private final String validDescription = "Valid Description";
    private final String validTitle = "PS5 Pro";
    private Price validPrice;
    private List<Picture> validPictures;
    private List<PublicationSample> oneItemList;
    private List<PublicationSample> severalItemList;
    private PublicationSample pub1;
    private PublicationSample pub2;
    private ListingFactory listingFactory;

    /**
     * Initializes shared test fixtures before each test.
     * Creates mock dependencies and valid data used across multiple test cases.
     */
    @BeforeEach
    void setup() {
        validPrice = mock(Price.class);
        Picture pic1 = mock(Picture.class);
        validPictures = List.of(pic1);

        pub1 = createMockValidPublicationSample();
        pub2 = createMockValidPublicationSample();
        oneItemList = List.of(pub1);
        severalItemList = List.of(pub1, pub2);
        listingFactory = new ListingFactory();
    }


    // ========== CONSTRUCTOR TESTS - HAPPY PATH ==========

    /**
     * Tests that a valid listing can be created when the items list contains a single publication sample.
     */
    @Test
    void shouldCreateValidListingWhenDataIsValidAndListHasOnlyOneBook() {
        Listing validListing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        assertNotNull(validListing);
    }

    /**
     * Tests that a valid listing can be created when the items list contains multiple publication samples.
     */
    @Test
    void shouldCreateValidListingWhenDataIsValidAndListHasSeveralBooks() {
        Listing validListing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        assertNotNull(validListing);
    }


    // ========== TITLE VALIDATION TESTS ==========

    /**
     * Tests that an exception is thrown when the title has fewer than 5 characters.
     */
    @Test
    void shouldThrowExceptionWhenTitleIsTooShort() {
        final String titleLessThanFiveCharacters = "abcd";

        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(titleLessThanFiveCharacters, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that an exception is thrown when the title has more than 100 characters.
     */
    @Test
    void shouldThrowExceptionWhenTitleIsTooLong() {
        final String titleTooLong = "a".repeat(101);

        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(titleTooLong, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that a title with exactly 5 characters (lower boundary) is accepted as valid.
     */
    @Test
    void ensureTitleWith5CharsIsValid() {
        final String validTitle5Chars = "12345";

        Listing listing = listingFactory.create(validTitle5Chars, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        assertNotNull(listing);
    }

    /**
     * Tests that a title with exactly 100 characters (upper boundary) is accepted as valid.
     */
    @Test
    void ensureTitleWith100CharsIsValid() {
        final String validTitle100Chars = "1234567890".repeat(10);

        Listing listing = listingFactory.create(validTitle100Chars, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        assertNotNull(listing);
    }


    // ========== DESCRIPTION VALIDATION TESTS ==========

    /**
     * Tests that an exception is thrown when the description has fewer than 10 characters.
     */
    @Test
    void shouldThrowExceptionWhenDescriptionIsTooShort() {
        final String invalidDescription = "123456789";

        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, invalidDescription, validPictures));
    }

    /**
     * Tests that a description with exactly 10 characters (lower boundary) is accepted as valid.
     */
    @Test
    void ensureDescriptionWith10CharsIsValid() {
        final String descriptionWith10Char = "1234567890";

        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, descriptionWith10Char, validPictures);

        assertNotNull(listing);
    }


    // ========== PRICE VALIDATION TESTS ==========

    /**
     * Tests that an exception is thrown when the price is null.
     */
    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, null, validDescription, validPictures));
    }


    // ========== ITEMS VALIDATION TESTS ==========

    /**
     * Tests that an exception is thrown when the items list is null.
     */
    @Test
    void shouldThrowExceptionWhenItemIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, null, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that an exception is thrown when the items list is empty.
     */
    @Test
    void shouldThrowExceptionWhenItemIsEmpty() {
        final List<PublicationSample> emptyItemList = new ArrayList<>();

        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, emptyItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }


    // ========== PICTURES VALIDATION TESTS ==========

    /**
     * Tests that an exception is thrown when the pictures list is null.
     */
    @Test
    void shouldThrowExceptionWhenPicturesIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, null));
    }


    // ========== PUBLICATION STATUS VALIDATION TESTS ==========

    /**
     * Tests that an exception is thrown when attempting to create a listing with an item
     * that is already marked as FOR_SALE from a previously created listing.
     */
    @Test
    void shouldThrowAnExceptionWhenAnItemIsAlreadyInAListing() {
        listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        assertEquals(PublicationStatus.FOR_SALE, oneItemList.get(0).getPublicationStatus());
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that an exception is thrown when attempting to create a listing with an item
     * that is already marked as SOLD.
     */
    @Test
    void shouldThrowExceptionWhenItemIsAlreadySold() {
        PublicationSample pubSampleWithSoldStatus = mock(PublicationSample.class);
        when(pubSampleWithSoldStatus.getPublicationStatus()).thenReturn(PublicationStatus.SOLD);

        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, List.of(pubSampleWithSoldStatus), SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that all publication samples in a listing are automatically set to FOR_SALE
     * upon listing creation.
     */
    @Test
    void shouldChangePublicationSampleStatusToSale() {
        Listing validListing = listingFactory.create(validTitle, List.of(pub1), SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);
        PublicationStatus actualStatus = validListing.getItems().get(0).getPublicationStatus();

        assertEquals(PublicationStatus.FOR_SALE, actualStatus);
    }


    // ========== GETTER METHODS TESTS ==========

    /**
     * Tests that {@link Listing#getPrice()} returns the price provided at creation.
     */
    @Test
    void shouldReturnPrice() {
        Price price = mock(Price.class);
        when(price.getPrice()).thenReturn(BigDecimal.valueOf(25));

        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, price, validDescription, validPictures);

        assertEquals(price, listing.getPrice());
    }

    /**
     * Tests that {@link Listing#getListingTitle()} returns the title provided at creation.
     */
    @Test
    void shouldReturnListingTitle() {
        Listing validListing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        String actualTitle = validListing.getListingTitle();

        assertEquals(validTitle, actualTitle);
    }

    /**
     * Tests that {@link Listing#getSeller()} returns the owner of the first item in the listing.
     */
    @Test
    void shouldReturnSellerUsername() {
        User userDouble = mock(User.class);
        when(pub1.getOwner()).thenReturn(userDouble);

        Listing validListing = listingFactory.create(validTitle, List.of(pub1), SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        assertEquals(userDouble, validListing.getSeller());
    }

    /**
     * Tests that {@link Listing#getItems()} returns a list equal in content to the original
     * but not the same reference, confirming a defensive copy is returned.
     */
    @Test
    void shouldReturnACopyOfItems() {
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);
        List<PublicationSample> items = listing.getItems();

        assertEquals(severalItemList, items);
        assertNotSame(severalItemList, items);
    }


    // ========== HELPER METHODS ==========

    /**
     * Creates a mock {@link PublicationSample} that simulates real status transitions.
     * The mock starts with {@link PublicationStatus#READING} and correctly updates
     * to {@link PublicationStatus#FOR_SALE} when {@code changeStatusToForSale()} is called,
     * mimicking the behaviour of a real domain object.
     *
     * @return a configured mock PublicationSample
     */
    private PublicationSample createMockValidPublicationSample() {
        PublicationSample pub = mock(PublicationSample.class);

        PublicationStatus[] status = {PublicationStatus.READING};

        when(pub.getPublicationStatus()).thenAnswer(inv -> status[0]);

        doAnswer(inv -> {
            status[0] = PublicationStatus.FOR_SALE;
            return null;
        }).when(pub).changeStatusToForSale();

        return pub;
    }
}