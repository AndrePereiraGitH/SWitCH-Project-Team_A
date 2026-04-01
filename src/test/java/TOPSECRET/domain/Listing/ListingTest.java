package TOPSECRET.domain.Listing;

import TOPSECRET.domain.Enum.*;
import TOPSECRET.domain.ValueObjects.*;
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
    private List<PublicationSampleID> oneItemList;
    private List<PublicationSampleID> severalItemList;
    private PublicationSampleID pub1Id;
    private PublicationSampleID pub2Id;
    private UserId validSellerId;
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
        pub1Id = new PublicationSampleID("pub1");
        pub2Id = new PublicationSampleID("pub2");
        oneItemList = List.of(pub1Id);
        severalItemList = List.of(pub1Id, pub2Id);
        validSellerId = new UserId("seller");
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
        Listing validListing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

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
        Listing validListing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

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
                () -> listingFactory.create(titleLessThanFiveCharacters, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures));
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
                () -> listingFactory.create(titleTooLong, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures));
    }

    /**
     * Tests that a title with exactly 5 characters (lower boundary) is accepted as valid.
     */
    @Test
    void ensureTitleWith5CharsIsValid() {
        //Arrange
        final String validTitle5Chars = "12345";

        //Act
        Listing listing = listingFactory.create(validTitle5Chars, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

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
        Listing listing = listingFactory.create(validTitle100Chars, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

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
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, invalidDescription, validSellerId, validPictures));
    }

    /**
     * Tests that a description with exactly 10 characters (lower boundary) is accepted as valid.
     */
    @Test
    void ensureDescriptionWith10CharsIsValid() {
        //Arrange
        final String descriptionWith10Char = "1234567890";

        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, descriptionWith10Char, validSellerId, validPictures);

        //Assert
        assertNotNull(listing);
    }

    /**
     * Tests that a description with exactly 100 characters (higher boundary) is accepted as valid.
     */
    @Test
    void ensureDescriptionWith100CharsIsValid() {
        //Arrange
        final String descriptionWith100Char = "This description is long enough to properly validate the one hundred character boundary rule.";

        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, descriptionWith100Char, validSellerId, validPictures);

        //Assert
        assertNotNull(listing);
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsTooLong() {
        //Arrange
        final String descriptionTooLong = "This description is too long to properly validate the one hundred character boundary rule. Should throw exception";

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE,
                        validPrice, descriptionTooLong, validSellerId, validPictures));
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
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, null, validDescription, validSellerId, validPictures));
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
                () -> listingFactory.create(validTitle, null, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures));
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the items list is empty.
     */
    @Test
    void shouldThrowExceptionWhenItemIsEmpty() {
        //Arrange
        final List<PublicationSampleID> emptyItemList = new ArrayList<>();

        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, emptyItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures));
    }

    // ========== SELLER VALIDATION TESTS ==========

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the seller ID is null.
     */
    @Test
    void shouldThrowExceptionWhenSellerIdIsNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, null,validPictures));
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
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId,null));
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
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, price, validDescription, validSellerId, validPictures);

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
        Listing validListing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);
        String actualTitle = validListing.getListingTitle();

        //Assert
        assertEquals(validTitle, actualTitle);
    }

    @Test
    void shouldReturnSellerId() {
        // Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        //Assert
        assertEquals(validSellerId, listing.getSellerId());
    }

    /**
     * Tests that {@link Listing#getShortDescription()} returns the exact description provided at creation.
     */
    @Test
    void shouldReturnShortDescription() {
        //Arrange
        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

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
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        //Assert
        assertEquals(validPictures, listing.getPictures());
    }

    /**
     * Tests that {@link Listing#getSaleType()} returns the same sale type provided at creation.
     */
    @Test
    void shouldReturnSaleType() {
        //Arrange
        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        //Assert
        assertEquals(SaleType.DIRECT_SALE, listing.getSaleType());
    }

    /**
     * Tests that {@link Listing#getPictures()} returns a list equal in content to the original
     * but not the same reference, confirming that a defensive copy is returned.
     */
    @Test
    void picturesListShouldBeUnmodifiable() {
        // Arrange
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        // Act
        List<Picture> pics = listing.getPictures();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> pics.add(mock(Picture.class)));
    }

    /**
     * Tests that {@link Listing#getItemsId()} returns a list equal in content to the original
     * but not the same reference, confirming that a defensive copy is returned.
     */
    @Test
    void shouldReturnACopyOfItems() {
        //Arrange
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        //Act
        List<PublicationSampleID> itemsId = listing.getItemsId();

        //Assert
        assertEquals(severalItemList, itemsId);
        assertNotSame(severalItemList, itemsId);
    }

    // ========== ID VALIDATION TESTS ==========

    @Test
    void idShouldNotBeNull() {
        // Arrange
        Listing listing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        // Act
        var id = listing.id();

        // Assert
        assertNotNull(id);
    }

    @Test
    void idShouldReturnSameInstanceOnMultipleCalls() {
        // Arrange
        Listing listing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        // Act
        var id1 = listing.id();
        var id2 = listing.id();

        // Assert
        assertSame(id1, id2);
    }

    @Test
    void eachListingShouldHaveUniqueId() {
        // Arrange
        Listing l1 = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);
        Listing l2 = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        // Act + Assert
        assertNotEquals(l1.id(), l2.id());
    }


    // ========== EQUALS METHOD TESTS ==========

    @Test
    void equalsShouldReturnTrueWhenComparingSameInstance() {
        // Arrange
        Listing listing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        // Act
        boolean result = listing.equals(listing);

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsShouldReturnFalseWhenComparingWithNull() {
        // Arrange
        Listing listing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        // Act
        boolean result = listing.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseWhenComparingWithDifferentType() {
        // Arrange
        Listing listing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        // Act
        boolean result = listing.equals("not a listing");

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseWhenListingIdsAreDifferent() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1Id), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        Listing listing2 = listingFactory.create(validTitle, List.of(pub2Id), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        // Act
        boolean result = listing1.equals(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnTrueWhenListingIdsAreEqual() throws Exception {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1Id), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        Listing listing2 = listingFactory.create(validTitle, List.of(pub2Id), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        // Forces the same ID (because, according to domain, we cannot have the same pub on different listings)
        var field = Listing.class.getDeclaredField("_listingId");
        field.setAccessible(true);
        field.set(listing2, listing1.id());

        // Act
        boolean result = listing1.equals(listing2);

        // Assert
        assertTrue(result);
    }

    // ========== SAME AS METHOD TESTS ==========

    @Test
    void sameAsShouldReturnTrueWhenAllFieldsMatch() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        Listing listing2 = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        // Act + Assert
        assertTrue(listing1.sameAs(listing2));
    }

    @Test
    void sameAsShouldReturnTrueWhenOneItemsDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1Id), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        Listing listing2 = listingFactory.create(validTitle, List.of(pub2Id), SaleType.AUCTION, validPrice, validDescription, validSellerId, validPictures);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenTitleDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create("Title 1", List.of(pub1Id), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        Listing listing2 = listingFactory.create("Title 2", List.of(pub2Id), SaleType.AUCTION, validPrice, validDescription, validSellerId, validPictures);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenSaleTypeDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1Id), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        Listing listing2 = listingFactory.create(validTitle, List.of(pub2Id), SaleType.AUCTION, validPrice, validDescription, validSellerId, validPictures);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenPriceDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1Id), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        Price validPrice2 = mock(Price.class);
        Listing listing2 = listingFactory.create(validTitle, List.of(pub2Id), SaleType.AUCTION, validPrice2, validDescription, validSellerId, validPictures);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenShortDescriptionDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1Id), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        Listing listing2 = listingFactory.create(validTitle, List.of(pub2Id), SaleType.AUCTION, validPrice, "different description", validSellerId, validPictures);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenSellerIdDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1Id), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        UserId validSellerId2 = new UserId("different seller");
        Listing listing2 = listingFactory.create(validTitle, List.of(pub2Id), SaleType.AUCTION, validPrice, validDescription, validSellerId2, validPictures);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenPicturesDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1Id), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        Picture pic2 = mock(Picture.class);
        List<Picture> validPictures2 = List.of(pic2);
        Listing listing2 = listingFactory.create(validTitle, List.of(pub2Id), SaleType.AUCTION, validPrice, validDescription, validSellerId, validPictures2);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenObjectIsNotAListing() {
        // Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        // Assert
        assertFalse(listing.sameAs("not a listing"));
    }

    @Test
    void sameAsShouldReturnFalseWhenObjectIsNull() {
        // Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerId, validPictures);

        // Assert
        assertFalse(listing.sameAs(null));
    }




    // ========== PUBLICATION STATUS VALIDATION TESTS ==========

    // With DDD (and the use of IDs to communicate between aggregates) the following methods don't apply (they should be on
    // the application layer, but we don't have it yet)

//    /**
//     * Tests that an {@link IllegalArgumentException} is thrown when attempting to create a listing
//     * with an item that already has a {@link PublicationStatus#FOR_SALE} status from a previously
//     * created listing.
//     */
//    @Test
//    void shouldThrowAnExceptionWhenAnItemIsAlreadyInAListing() {
//        //Arrange
//        listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);
//
//        //Act & Assert
//        assertEquals(PublicationStatus.FOR_SALE, oneItemList.get(0).getPublicationStatus());
//        assertThrows(IllegalArgumentException.class,
//                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
//    }
//
//    /**
//     * Tests that an {@link IllegalArgumentException} is thrown when attempting to create a listing
//     * with an item that already has a {@link PublicationStatus#SOLD} status.
//     */
//    @Test
//    void shouldThrowExceptionWhenItemIsAlreadySold() {
//        //Arrange
//        PublicationSample pubSampleWithSoldStatus = mock(PublicationSample.class);
//        when(pubSampleWithSoldStatus.getPublicationStatus()).thenReturn(PublicationStatus.SOLD);
//
//        //Act & Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> listingFactory.create(validTitle, List.of(pubSampleWithSoldStatus), SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
//    }
//
//    /**
//     * Tests that all publication samples in the listing have their status automatically set
//     * to {@link PublicationStatus#FOR_SALE} upon listing creation.
//     */
//    @Test
//    void shouldChangePublicationSampleStatusToSale() {
//        //Arrange
//
//        //Act
//        Listing validListing = listingFactory.create(validTitle, List.of(pub1Id), SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);
//        PublicationStatus actualStatus = validListing.getItems().get(0).getPublicationStatus();
//
//        //Assert
//        assertEquals(PublicationStatus.FOR_SALE, actualStatus);
//    }

    // ========== HELPER METHODS ==========

//    /**
//     * Creates a mock {@link PublicationSample} that simulates real status transitions.
//     * The mock starts with {@link PublicationStatus#READING} and correctly transitions
//     * to {@link PublicationStatus#FOR_SALE} when {@code changeStatusToForSale()} is called,
//     * mimicking the behaviour of a real domain object.
//     *
//     * @return a configured mock {@link PublicationSample}
//     */
//    private PublicationSample createMockValidPublicationSample() {
//        PublicationSample pub = mock(PublicationSample.class);
//
//        PublicationStatus[] status = {PublicationStatus.READING};
//
//        when(pub.getPublicationStatus()).thenAnswer(inv -> status[0]);
//
//        doAnswer(inv -> {
//            status[0] = PublicationStatus.FOR_SALE;
//            return null;
//        }).when(pub).changeStatusToForSale();
//
//        return pub;
//    }
}