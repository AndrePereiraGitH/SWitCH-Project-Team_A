package TOPSECRET.domain.Listing;

import TOPSECRET.domain.Enum.*;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private List<PublicationSampleId> oneItemList;
    private List<PublicationSampleId> severalItemList;
    private PublicationSampleId pub1IdDouble;
    private PublicationSampleId pub2IdDouble;
    private UserId validSellerIdDouble;
    private UserId validBuyerIdDouble;
    private ListingFactory listingFactory;
    private LocalDate date;
    private ListingStatus listingStatus;

    @BeforeEach
    void setup() {
        validPrice = mock(Price.class);
        Picture pic1 = mock(Picture.class);
        validPictures = List.of(pic1);
        pub1IdDouble = mock(PublicationSampleId.class);
        pub2IdDouble = mock(PublicationSampleId.class);
        oneItemList = List.of(pub1IdDouble);
        severalItemList = List.of(pub1IdDouble, pub2IdDouble);
        validSellerIdDouble = mock(UserId.class);
        validBuyerIdDouble = mock(UserId.class);
        listingFactory = new ListingFactory();
        date = LocalDate.now();
        listingStatus = ListingStatus.ACTIVE;

    }


    // ========== CONSTRUCTOR TESTS - HAPPY PATH ==========

    @Test
    void shouldCreateValidListingWhenDataIsValidAndListHasOnlyOneBook() {
        //Arrange
        //Act
        Listing validListing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Assert
        assertNotNull(validListing);
    }

    @Test
    void shouldCreateValidListingWhenDataIsValidAndListHasSeveralBooks() {
        //Arrange
        //Act
        Listing validListing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Assert
        assertNotNull(validListing);
    }


    // ========== TITLE VALIDATION TESTS ==========

    @Test
    void shouldThrowExceptionWhenTitleIsTooShort() {
        //Arrange
        final String titleLessThanFiveCharacters = "abcd";

        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(titleLessThanFiveCharacters, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsTooLong() {
        //Arrange
        final String titleTooLong = "a".repeat(101);

        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(titleTooLong, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures));
    }

    @Test
    void ensureTitleWith5CharsIsValid() {
        //Arrange
        final String validTitle5Chars = "12345";

        //Act
        Listing listing = listingFactory.create(validTitle5Chars, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Assert
        assertNotNull(listing);
    }

    @Test
    void ensureTitleWith100CharsIsValid() {
        //Arrange
        final String validTitle100Chars = "1234567890".repeat(10);

        //Act
        Listing listing = listingFactory.create(validTitle100Chars, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Assert
        assertNotNull(listing);
    }

    // ========== CONSTRUCTOR WITH LISTINGID TESTS ==========

    @Test
    void constructor_shouldCreateListingWithValidData() {
        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);

        // Act
        Listing listing = new Listing(
                listingIdDouble,
                validTitle,
                oneItemList,
                SaleType.DIRECT_SALE,
                validPrice,
                validDescription,
                validSellerIdDouble,
                null,
                validPictures,
                date,
                listingStatus);

        // Assert
        assertNotNull(listing);
    }

    @Test
    void constructor_shouldCreateSoldListingWithBuyerId() {
        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);

        // Act
        Listing listing = new Listing(
                listingIdDouble,
                validTitle,
                oneItemList,
                SaleType.DIRECT_SALE,
                validPrice,
                validDescription,
                validSellerIdDouble,
                validBuyerIdDouble,
                validPictures,
                date,
                ListingStatus.SOLD);

        // Assert
        assertNotNull(listing);
        assertEquals(validBuyerIdDouble, listing.getBuyerId());
        assertEquals(ListingStatus.SOLD, listing.getListingStatus());
    }

    @Test
    void constructor_shouldThrowWhenListingIdIsNull() {
        // Arrange

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new Listing(
                        null,
                        validTitle,
                        oneItemList,
                        SaleType.DIRECT_SALE,
                        validPrice,
                        validDescription,
                        validSellerIdDouble,
                        null,
                        validPictures,
                        date,
                        listingStatus));
    }

    @Test
    void constructor_shouldThrowWhenTitleIsNull() {
        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Listing(
                        listingIdDouble,
                        null,
                        oneItemList,
                        SaleType.DIRECT_SALE,
                        validPrice,
                        validDescription,
                        validSellerIdDouble,
                        null,
                        validPictures,
                        date,
                        listingStatus));
    }

    @Test
    void constructor_shouldThrowWhenItemsIdIsNull() {
        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Listing(
                        listingIdDouble,
                        validTitle,
                        null,
                        SaleType.DIRECT_SALE,
                        validPrice,
                        validDescription,
                        validSellerIdDouble,
                        null,
                        validPictures,
                        date,
                        listingStatus));
    }

    @Test
    void constructor_shouldThrowWhenSaleTypeIsNull() {
        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new Listing(
                        listingIdDouble,
                        validTitle,
                        oneItemList,
                        null,
                        validPrice,
                        validDescription,
                        validSellerIdDouble,
                        null,
                        validPictures,
                        date,
                        listingStatus));
    }

    @Test
    void constructor_shouldThrowWhenPriceIsNull() {
        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Listing(
                        listingIdDouble,
                        validTitle,
                        oneItemList,
                        SaleType.DIRECT_SALE,
                        null,
                        validDescription,
                        validSellerIdDouble,
                        null,
                        validPictures,
                        date,
                        listingStatus));
    }

    @Test
    void constructor_shouldThrowWhenDescriptionIsNull() {
        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Listing(
                        listingIdDouble,
                        validTitle,
                        oneItemList,
                        SaleType.DIRECT_SALE,
                        validPrice,
                        null,
                        validSellerIdDouble,
                        null,
                        validPictures,
                        date,
                        listingStatus));
    }

    @Test
    void constructor_shouldThrowWhenSellerIdIsNull() {
        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Listing(
                        listingIdDouble,
                        validTitle,
                        oneItemList,
                        SaleType.DIRECT_SALE,
                        validPrice,
                        validDescription,
                        null,
                        null,
                        validPictures,
                        date,
                        listingStatus));
    }

    @Test
    void constructor_shouldThrowWhenPicturesIsNull() {
        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Listing(
                        listingIdDouble,
                        validTitle,
                        oneItemList,
                        SaleType.DIRECT_SALE,
                        validPrice,
                        validDescription,
                        validSellerIdDouble,
                        null,
                        null,
                        date,
                        listingStatus));
    }

    @Test
    void constructor_shouldPreserveListingId() {
        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);

        // Act
        Listing listing = new Listing(
                listingIdDouble,
                validTitle,
                oneItemList,
                SaleType.DIRECT_SALE,
                validPrice,
                validDescription,
                validSellerIdDouble,
                null,
                validPictures,
                date,
                listingStatus);

        // Assert
        assertSame(listingIdDouble, listing.id());
    }

    @Test
    void constructor_shouldThrowWhenListingStatusIsNull() {
        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new Listing(
                        listingIdDouble,
                        validTitle,
                        oneItemList,
                        SaleType.DIRECT_SALE,
                        validPrice,
                        validDescription,
                        validSellerIdDouble,
                        null,
                        validPictures,
                        date,
                        null));
    }

    @Test
    void constructor_shouldThrowWhenPublicationDateIsNull() {
        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new Listing(
                        listingIdDouble,
                        validTitle,
                        oneItemList,
                        SaleType.DIRECT_SALE,
                        validPrice,
                        validDescription,
                        validSellerIdDouble,
                        null,
                        validPictures,
                        null,
                        listingStatus));
    }

    // ========== DESCRIPTION VALIDATION TESTS ==========

    @Test
    void shouldThrowExceptionWhenDescriptionIsTooShort() {
        //Arrange
        final String invalidDescription = "123456789";

        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(
                        validTitle,
                        severalItemList,
                        SaleType.DIRECT_SALE,
                        validPrice,
                        invalidDescription,
                        validSellerIdDouble,
                        validPictures));
    }

    @Test
    void ensureDescriptionWith10CharsIsValid() {
        //Arrange
        final String descriptionWith10Char = "1234567890";

        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, descriptionWith10Char, validSellerIdDouble, validPictures);

        //Assert
        assertNotNull(listing);
    }

    @Test
    void ensureDescriptionWith100CharsIsValid() {
        //Arrange
        final String descriptionWith100Char = "This description is long enough to properly validate the one hundred character boundary rule.";

        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, descriptionWith100Char, validSellerIdDouble, validPictures);

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
                        validPrice, descriptionTooLong, validSellerIdDouble, validPictures));
    }


    // ========== PRICE VALIDATION TESTS ==========

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        //Arrange
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, null, validDescription, validSellerIdDouble, validPictures));
    }


    // ========== ITEMS VALIDATION TESTS ==========

    @Test
    void shouldThrowExceptionWhenItemIsNull() {
        //Arrange
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, null, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures));
    }

    @Test
    void shouldThrowExceptionWhenItemIsEmpty() {
        //Arrange
        final List<PublicationSampleId> emptyItemList = new ArrayList<>();

        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, emptyItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures));
    }

    // ========== SELLER VALIDATION TESTS ==========

    @Test
    void shouldThrowExceptionWhenSellerIdIsNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, null,validPictures));
    }


    // ========== PICTURES VALIDATION TESTS ==========

    @Test
    void shouldThrowExceptionWhenPicturesIsNull() {
        //Arrange
        //Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble,null));
    }


    // ========== GETTER METHODS TESTS ==========

    @Test
    void shouldReturnPrice() {
        //Arrange
        Price price = mock(Price.class);
        when(price.getPrice()).thenReturn(BigDecimal.valueOf(25));

        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, price, validDescription, validSellerIdDouble, validPictures);

        //Assert
        assertEquals(price, listing.getPrice());
    }

    @Test
    void shouldReturnListingTitle() {
        //Arrange

        //Act
        Listing validListing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);
        String actualTitle = validListing.getListingTitle();

        //Assert
        assertEquals(validTitle, actualTitle);
    }

    @Test
    void shouldReturnSellerId() {
        // Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Assert
        assertEquals(validSellerIdDouble, listing.getSellerId());
    }

    @Test
    void shouldReturnShortDescription() {
        //Arrange
        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Assert
        assertEquals(validDescription, listing.getShortDescription());
    }

    @Test
    void shouldReturnPictures() {
        //Arrange
        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Assert
        assertEquals(validPictures, listing.getPictures());
    }

    @Test
    void shouldReturnSaleType() {
        //Arrange
        //Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Assert
        assertEquals(SaleType.DIRECT_SALE, listing.getSaleType());
    }

    @Test
    void picturesListShouldBeUnmodifiable() {
        // Arrange
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Act
        List<Picture> pics = listing.getPictures();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> pics.add(mock(Picture.class)));
    }

    @Test
    void shouldReturnACopyOfItems() {
        //Arrange
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Act
        List<PublicationSampleId> itemsId = listing.getItemsId();

        //Assert
        assertEquals(severalItemList, itemsId);
        assertNotSame(severalItemList, itemsId);
    }

    @Test
    void shouldReturnPublicationDate() {
        //Arrange
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Act
        LocalDate date = listing.getPublicationDate();

        //Assert
        assertEquals(date, LocalDate.now());
    }

    @Test
    void shouldReturnPublicationStatus() {
        //Arrange
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Act
        ListingStatus status = listing.getListingStatus();

        //Assert
        assertEquals(status, ListingStatus.ACTIVE);
    }

    @Test
    void shouldReturnNullBuyerId_whenListingNotSold() {
        //Arrange
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Act
        UserId buyerId = listing.getBuyerId();

        //Assert
        assertNull(buyerId);
    }

    @Test
    void shouldReturnBuyerId_whenListingSold() {
        //Arrange
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Act
        listing.markListingAsSold(validBuyerIdDouble);

        //Assert
        assertEquals(validBuyerIdDouble, listing.getBuyerId());
    }

    // ========== ID VALIDATION TESTS ==========

    @Test
    void idShouldNotBeNull() {
        // Arrange
        Listing listing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Act
        var id = listing.id();

        // Assert
        assertNotNull(id);
    }

    @Test
    void idShouldReturnSameInstanceOnMultipleCalls() {
        // Arrange
        Listing listing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Act
        var id1 = listing.id();
        var id2 = listing.id();

        // Assert
        assertSame(id1, id2);
    }

    @Test
    void eachListingShouldHaveUniqueId() {
        // Arrange
        Listing l1 = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);
        Listing l2 = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Act + Assert
        assertNotEquals(l1.id(), l2.id());
    }


    // ========== EQUALS METHOD TESTS ==========

    @Test
    void equalsShouldReturnTrueWhenComparingSameInstance() {
        // Arrange
        Listing listing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Act
        boolean result = listing.equals(listing);

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsShouldReturnFalseWhenComparingWithNull() {
        // Arrange
        Listing listing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Act
        boolean result = listing.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseWhenComparingWithDifferentType() {
        // Arrange
        Listing listing = listingFactory.create(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Act
        boolean result = listing.equals("not a listing");

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseWhenListingIdsAreDifferent() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        Listing listing2 = listingFactory.create(validTitle, List.of(pub2IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Act
        boolean result = listing1.equals(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnTrueWhenListingIdsAreEqual() throws Exception {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        Listing listing2 = listingFactory.create(validTitle, List.of(pub2IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

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
        Listing listing1 = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        Listing listing2 = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Act + Assert
        assertTrue(listing1.sameAs(listing2));
    }

    @Test
    void sameAsShouldReturnTrueWhenOneItemsDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        Listing listing2 = listingFactory.create(validTitle, List.of(pub2IdDouble), SaleType.AUCTION, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenTitleDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create("Title 1", List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        Listing listing2 = listingFactory.create("Title 2", List.of(pub2IdDouble), SaleType.AUCTION, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenSaleTypeDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        Listing listing2 = listingFactory.create(validTitle, List.of(pub2IdDouble), SaleType.AUCTION, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenPriceDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        Price validPrice2 = mock(Price.class);
        Listing listing2 = listingFactory.create(validTitle, List.of(pub2IdDouble), SaleType.AUCTION, validPrice2, validDescription, validSellerIdDouble, validPictures);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenShortDescriptionDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        Listing listing2 = listingFactory.create(validTitle, List.of(pub2IdDouble), SaleType.AUCTION, validPrice, "different description", validSellerIdDouble, validPictures);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenSellerIdDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        UserId validSellerId2 = new UserId("different seller");
        Listing listing2 = listingFactory.create(validTitle, List.of(pub2IdDouble), SaleType.AUCTION, validPrice, validDescription, validSellerId2, validPictures);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenPicturesDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        Picture pic2 = mock(Picture.class);
        List<Picture> validPictures2 = List.of(pic2);
        Listing listing2 = listingFactory.create(validTitle, List.of(pub2IdDouble), SaleType.AUCTION, validPrice, validDescription, validSellerIdDouble, validPictures2);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenListingStatusDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        Listing listing2 = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);
        listing2.markListingAsSold(validBuyerIdDouble);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenBuyerIdDiffers() {
        // Arrange
        Listing listing1 = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);
        listing1.markListingAsSold(validBuyerIdDouble);

        Listing listing2 = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);
        UserId otherBuyerIdDouble = mock(UserId.class);
        listing2.markListingAsSold(otherBuyerIdDouble);

        // Act
        boolean result = listing1.sameAs(listing2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenObjectIsNotAListing() {
        // Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Assert
        assertFalse(listing.sameAs("not a listing"));
    }

    @Test
    void sameAsShouldReturnFalseWhenObjectIsNull() {
        // Act
        Listing listing = listingFactory.create(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        // Assert
        assertFalse(listing.sameAs(null));
    }


    // ========== isListingLinkedToAnAuction TESTS ==========


    @Test
    void isListingLinkedToAnAuction_shouldReturnTrue_ifSaleTypeIsAuction(){

        //Arrange
        Listing validListing = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.AUCTION, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Act
        boolean result = validListing.isListingLinkedToAnAuction();

        //Assert
        assertTrue(result);

    }

    @Test
    void isListingLinkedToAnAuction_shouldReturnFalse_ifSaleTypeIsNotAuction(){

        //Arrange
        Listing validListing = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Act
        boolean result = validListing.isListingLinkedToAnAuction();

        //Assert
        assertFalse(result);

    }

    // ========== isListingLinkedToADirectSale TESTS ==========


    @Test
    void isListingLinkedToADirectSale_shouldReturnTrue_ifSaleTypeIsDirectSale(){

        //Arrange
        Listing validListing = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Act
        boolean result = validListing.isListingLinkedToADirectSale();

        //Assert
        assertTrue(result);

    }

    @Test
    void isListingLinkedToADirectSale_shouldReturnFalse_ifSaleTypeIsNotDirectSale(){

        //Arrange
        Listing validListing = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.AUCTION, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Act
        boolean result = validListing.isListingLinkedToADirectSale();

        //Assert
        assertFalse(result);

    }

    // ========== containsPubSampleId TESTS ==========

    @Test
    void containsPubSampleId_shouldReturnTrue_ifItemsContainPubSampleId(){

        //Arrange
        Listing validListing = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Act
        boolean result = validListing.containsPubSampleId(pub1IdDouble);

        //Assert
        assertTrue(result);

    }

    @Test
    void containsPubSampleId_shouldReturnFalse_ifItemsDoesNotContainPubSampleId(){

        //Arrange
        Listing validListing = listingFactory.create(validTitle, List.of(pub1IdDouble), SaleType.DIRECT_SALE, validPrice, validDescription, validSellerIdDouble, validPictures);

        //Act
        boolean result = validListing.containsPubSampleId(pub2IdDouble);

        //Assert
        assertFalse(result);

    }

    // ========== markListingAsSold TESTS ==========

    @Test
    void markListingAsSold_shouldChangeStatusToSold() {
        // Arrange
        Listing validListing = listingFactory.create(
                validTitle,
                oneItemList,
                SaleType.DIRECT_SALE,
                validPrice,
                validDescription,
                validSellerIdDouble,
                validPictures
        );

        // Act
        validListing.markListingAsSold(validBuyerIdDouble);

        // Assert
        assertEquals(ListingStatus.SOLD, validListing.getListingStatus(), "Listing status should be updated to SOLD.");
    }

    @Test
    void markListingAsSold_shouldSetBuyerId() {
        // Arrange
        Listing validListing = listingFactory.create(
                validTitle,
                oneItemList,
                SaleType.DIRECT_SALE,
                validPrice,
                validDescription,
                validSellerIdDouble,
                validPictures
        );

        // Act
        validListing.markListingAsSold(validBuyerIdDouble);

        // Assert
        assertEquals(validBuyerIdDouble, validListing.getBuyerId(), "Buyer ID should be recorded after sale.");
    }

    @Test
    void markListingAsSold_shouldReturnSameInstance() {
        // Arrange
        Listing validListing = listingFactory.create(
                validTitle,
                oneItemList,
                SaleType.DIRECT_SALE,
                validPrice,
                validDescription,
                validSellerIdDouble,
                validPictures
        );

        // Act
        Listing returnedListing = validListing.markListingAsSold(validBuyerIdDouble);

        // Assert
        assertSame(validListing, returnedListing, "Method should return the same Listing instance to support method chaining.");
    }

    @Test
    void markListingAsSold_shouldThrowNullPointerException_whenBuyerIdIsNull() {
        // Arrange
        Listing validListing = listingFactory.create(
                validTitle,
                oneItemList,
                SaleType.DIRECT_SALE,
                validPrice,
                validDescription,
                validSellerIdDouble,
                validPictures
        );

        // Act & Assert
        assertThrows(NullPointerException.class, () -> validListing.markListingAsSold(null));
    }

}