package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link Listing}.
 * Tests the creation, validation, and behaviour of listings for publication samples.
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
     * Initialises shared test fixtures before each test.
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
        //Arrange
        //Act
        Listing validListing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        //Assert
        assertNotNull(validListing);
    }

    /**
     * Tests that a valid listing can be created when the items list contains multiple publication samples.
     */
    @Test
    void shouldCreateValidListingWhenDataIsValidAndListHasSeveralBooks() {
        //Arrange
        //Act
        Listing validListing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        //Assert
        assertNotNull(validListing);
    }


    // ========== TITLE VALIDATION TESTS ==========

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the title has fewer than 5 characters.
     */
    @Test
    void shouldThrowExceptionWhenTitleIsTooShort() {
        //Arrange
        final String titleLessThanFiveCharacters = "abcd";

        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(titleLessThanFiveCharacters, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the title has more than 100 characters.
     */
    @Test
    void shouldThrowExceptionWhenTitleIsTooLong() {
        //Arrange
        final String titleTooLong = "a".repeat(101);

        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(titleTooLong, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that a title with exactly 5 characters (lower boundary) is accepted as valid.
     */
    @Test
    void ensureTitleWith5CharsIsValid() {
        //Arrange
        final String validTitle5Chars = "12345";

        //Act
        Listing listing = listingFactory.create(validTitle5Chars, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        //Assert
        assertNotNull(listing);
    }

    /**
     * Tests that a title with exactly 100 characters (upper boundary) is accepted as valid.
     */
    @Test
    void ensureTitleWith100CharsIsValid() {
        //Arrange
        final String validTitle100Chars = "1234567890".repeat(10);

        //Act
        Listing listing = listingFactory.create(validTitle100Chars, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        //Assert
        assertNotNull(listing);
    }


    // ========== DESCRIPTION VALIDATION TESTS ==========

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the description has fewer than 10 characters.
     */
    @Test
    void shouldThrowExceptionWhenDescriptionIsTooShort() {
        //Arrange
        final String invalidDescription = "123456789";

        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, invalidDescription, validPictures));
    }

    /**
     * Tests that a description with exactly 10 characters (lower boundary) is accepted as valid.
     */
    @Test
    void ensureDescriptionWith10CharsIsValid() {
        //Arrange
        final String descriptionWith10Char = "1234567890";

        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, descriptionWith10Char, validPictures);

        //Assert
        assertNotNull(listing);
    }


    // ========== PRICE VALIDATION TESTS ==========

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the price is null.
     */
    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        //Arrange
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, null, validDescription, validPictures));
    }


    // ========== ITEMS VALIDATION TESTS ==========

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the items list is null.
     */
    @Test
    void shouldThrowExceptionWhenItemIsNull() {
        //Arrange
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, null, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the items list is empty.
     */
    @Test
    void shouldThrowExceptionWhenItemIsEmpty() {
        //Arrange
        final List<PublicationSample> emptyItemList = new ArrayList<>();

        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, emptyItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }


    // ========== PICTURES VALIDATION TESTS ==========

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the pictures list is null.
     */
    @Test
    void shouldThrowExceptionWhenPicturesIsNull() {
        //Arrange
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, null));
    }


    // ========== PUBLICATION STATUS VALIDATION TESTS ==========

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when attempting to create a listing
     * with an item that already has a {@link PublicationStatus#FOR_SALE} status from a previously
     * created listing.
     */
    @Test
    void shouldThrowAnExceptionWhenAnItemIsAlreadyInAListing() {
        //Arrange
        listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        //Act & Assert
        assertEquals(PublicationStatus.FOR_SALE, oneItemList.get(0).getPublicationStatus());
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when attempting to create a listing
     * with an item that already has a {@link PublicationStatus#SOLD} status.
     */
    @Test
    void shouldThrowExceptionWhenItemIsAlreadySold() {
        //Arrange
        PublicationSample pubSampleWithSoldStatus = mock(PublicationSample.class);
        when(pubSampleWithSoldStatus.getPublicationStatus()).thenReturn(PublicationStatus.SOLD);

        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, List.of(pubSampleWithSoldStatus), SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that all publication samples in the listing have their status automatically set
     * to {@link PublicationStatus#FOR_SALE} upon listing creation.
     */
    @Test
    void shouldChangePublicationSampleStatusToSale() {
        //Arrange

        //Act
        Listing validListing = listingFactory.create(validTitle, List.of(pub1), SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);
        PublicationStatus actualStatus = validListing.getItems().get(0).getPublicationStatus();

        //Assert
        assertEquals(PublicationStatus.FOR_SALE, actualStatus);
    }


    // ========== GETTER METHODS TESTS ==========

    /**
     * Tests that {@link Listing#getPrice()} returns the same {@link Price} instance provided at creation.
     */
    @Test
    void shouldReturnPrice() {
        //Arrange
        Price price = mock(Price.class);
        when(price.getPrice()).thenReturn(BigDecimal.valueOf(25));

        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, price, validDescription, validPictures);

        //Assert
        assertEquals(price, listing.getPrice());
    }

    /**
     * Tests that {@link Listing#getListingTitle()} returns the exact title provided at creation.
     */
    @Test
    void shouldReturnListingTitle() {
        //Arrange

        //Act
        Listing validListing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);
        String actualTitle = validListing.getListingTitle();

        //Assert
        assertEquals(validTitle, actualTitle);
    }

    /**
     * Tests that {@link Listing#getSeller()} returns the {@link User} who owns the first item in the listing.
     */
    @Test
    void shouldReturnSellerUsername() {
        //Arrange
        User userDouble = mock(User.class);
        when(pub1.getOwner()).thenReturn(userDouble);

        //Act
        Listing validListing = listingFactory.create(validTitle, List.of(pub1), SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        //Assert
        assertEquals(userDouble, validListing.getSeller());
    }

    /**
     * Tests that {@link Listing#getShortDescription()} returns the exact description provided at creation.
     */
    @Test
    void shouldReturnShortDescription() {
        //Arrange
        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        //Assert
        assertEquals(validDescription, listing.getShortDescription());
    }

    /**
     * Tests that {@link Listing#getPictures()} returns the same list of pictures provided at creation.
     */
    @Test
    void shouldReturnPictures() {
        //Arrange
        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        //Assert
        assertEquals(validPictures, listing.getPictures());
    }

    /**
     * Tests that {@link Listing#getItems()} returns a list equal in content to the original
     * but not the same reference, confirming that a defensive copy is returned.
     */
    @Test
    void shouldReturnACopyOfItems() {
        //Arrange
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        //Act
        List<PublicationSample> items = listing.getItems();

        //Assert
        assertEquals(severalItemList, items);
        assertNotSame(severalItemList, items);
    }


    // ========== HELPER METHODS ==========

    /**
     * Creates a mock {@link PublicationSample} that simulates real status transitions.
     * The mock starts with {@link PublicationStatus#READING} and correctly transitions
     * to {@link PublicationStatus#FOR_SALE} when {@code changeStatusToForSale()} is called,
     * mimicking the behaviour of a real domain object.
     *
     * @return a configured mock {@link PublicationSample}
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