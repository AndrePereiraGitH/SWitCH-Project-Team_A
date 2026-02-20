package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Listing}.
 * Tests the creation, validation, and behavior of listings for publication samples.
 */
class ListingTest {

    // ========== TEST DATA SETUP ==========

    // Valid primitive attributes
    private final String validDescription = "Valid Description";
    private final String validTitle = "PS5 Pro";

    // Price
    Price validPrice = new Price(BigDecimal.valueOf(50.99));

    // Pictures
    List<Picture> validPictures = List.of(new Picture("path/to/pic.jpg"));

    // Dimensions
    Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);

    // BibliographicalData
    Profile profile = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
    Author author = new Author(profile, "valio bio");
    Address address = new Address("R Maria", "12", "156-5", new Country("PARAGUAY"));
    Email email = new Email("Maria1", "gmail.com");
    Contact contact = new Contact("915945695", "+351", email, address);
    BibliographicalData bibliographicalData = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author),
            Locale.ENGLISH,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre("Fiction")),
            "Some text"
    );

    // User
    User user = new User("maria1", contact, profile, UserRole.NORMAL_USER);

    // Edition
    Code code = Code.fromIsbn13("9783161484123");
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
    Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
    Edition edition1 = new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition2 = new Edition("2nd", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition3 = new Edition("3rd", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    // PublicationSample
    PublicationSample validItem1 = new PublicationSample(bibliographicalData, user, PublicationStatus.READING, edition1, Condition.LIKE_NEW);
    PublicationSample validItem2 = new PublicationSample(bibliographicalData, user, PublicationStatus.READ, edition2, Condition.LIKE_NEW);
    PublicationSample validItem3 = new PublicationSample(bibliographicalData, user, PublicationStatus.READING, edition3, Condition.LIKE_NEW);

    private final List<PublicationSample> oneItemList = List.of(validItem1);
    private final List<PublicationSample> severalItemList = List.of(validItem1, validItem2, validItem3);


    // ========== CONSTRUCTOR TESTS - HAPPY PATH ==========

    /**
     * Tests that a valid listing can be created with a single publication sample.
     */
    @Test
    void shouldCreateValidListingWhenDataIsValidAndListHasOnlyOneBook() {
        // Act
        Listing validListing = new Listing(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        // Assert
        assertNotNull(validListing);
    }

    /**
     * Tests that a valid listing can be created with multiple publication samples.
     */
    @Test
    void shouldCreateValidListingWhenDataIsValidAndListHasSeveralBooks() {
        // Act
        Listing validListing = new Listing(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        // Assert
        assertNotNull(validListing);
    }


    // ========== TITLE VALIDATION TESTS ==========

    /**
     * Tests that an exception is thrown when the title has fewer than 5 characters.
     */
    @Test
    void shouldThrowExceptionWhenTitleIsTooShort() {
        // Arrange
        final String titleLessThanFiveCharacters = "abcd";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Listing(titleLessThanFiveCharacters, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that an exception is thrown when the title has more than 100 characters.
     */
    @Test
    void shouldThrowExceptionWhenTitleIsTooLong() {
        // Arrange
        final String titleTooLong = "a".repeat(101);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Listing(titleTooLong, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that a title with exactly 5 characters is accepted as valid.
     */
    @Test
    void ensureTitleWith5CharsIsValid() {
        // Arrange
        final String validTitle5Chars = "12345";

        // Act
        Listing listing = new Listing(validTitle5Chars, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        // Assert
        assertNotNull(listing);
    }

    /**
     * Tests that a title with exactly 100 characters is accepted as valid.
     */
    @Test
    void ensureTitleWith100CharsIsValid() {
        // Arrange
        final String validTitle100Chars = "1234567890".repeat(10);

        // Act
        Listing listing = new Listing(validTitle100Chars, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        // Assert
        assertNotNull(listing);
    }


    // ========== DESCRIPTION VALIDATION TESTS ==========

    /**
     * Tests that an exception is thrown when the description has fewer than 10 characters.
     */
    @Test
    void shouldThrowExceptionWhenDescriptionIsTooShort() {
        // Arrange
        final String invalidDescription = "123456789";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Listing(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, invalidDescription, validPictures));
    }

    /**
     * Tests that a description with exactly 10 characters is accepted as valid.
     */
    @Test
    void ensureDescriptionWith10CharsIsValid() {
        // Arrange
        final String descriptionWith10Char = "1234567890";

        // Act
        Listing testingDescriptionWith10Char = new Listing(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, descriptionWith10Char, validPictures);

        // Assert
        assertNotNull(testingDescriptionWith10Char);
    }


    // ========== PRICE VALIDATION TESTS ==========

    /**
     * Tests that an exception is thrown when the price is null.
     */
    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Listing(validTitle, severalItemList, SaleType.DIRECT_SALE, null, validDescription, validPictures));
    }


    // ========== ITEMS VALIDATION TESTS ==========

    /**
     * Tests that an exception is thrown when the items list is null.
     */
    @Test
    void shouldThrowExceptionWhenItemIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Listing(validTitle, null, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that an exception is thrown when the items list is empty.
     */
    @Test
    void shouldThrowExceptionWhenItemIsEmpty() {
        // Arrange
        final List<PublicationSample> emptyItemList = new ArrayList<>();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Listing(validTitle, emptyItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }


    // ========== PICTURES VALIDATION TESTS ==========

    /**
     * Tests that an exception is thrown when the pictures list is null.
     */
    @Test
    void shouldThrowExceptionWhenPicturesIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Listing(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, null));
    }


    // ========== PUBLICATION STATUS VALIDATION TESTS ==========

    /**
     * Tests that an exception is thrown when attempting to create a listing with an item
     * that is already marked as FOR_SALE in another listing.
     */
    @Test
    void shouldThrowAnExceptionWhenAnItemIsAlreadyInAListing() {
        // Arrange
        Listing firstListing = new Listing(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        // Act & Assert
        assertEquals(PublicationStatus.FOR_SALE, oneItemList.get(0).getPublicationStatus());
        assertThrows(IllegalArgumentException.class,
                () -> new Listing(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that an exception is thrown when attempting to create a listing with an item
     * that has already been sold.
     */
    @Test
    void shouldThrowExceptionWhenItemIsAlreadySold() {
        // Arrange
        PublicationSample pubSampleWithSoldStatus = new PublicationSample(bibliographicalData, user, PublicationStatus.SOLD, edition1, Condition.LIKE_NEW);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Listing(validTitle, List.of(pubSampleWithSoldStatus), SaleType.DIRECT_SALE, validPrice, validDescription, validPictures));
    }

    /**
     * Tests that all publication samples in a listing have their status changed to FOR_SALE
     * upon listing creation.
     */
    @Test
    void shouldChangePublicationSampleStatusToSale() {
        // Arrange
        PublicationSample pubSampleWithDifStatus = new PublicationSample(bibliographicalData, user, PublicationStatus.READING, edition1, Condition.LIKE_NEW);

        // Act
        Listing validListing = new Listing(validTitle, List.of(pubSampleWithDifStatus), SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);
        PublicationStatus actualStatus = validListing.getItems().get(0).getPublicationStatus();

        // Assert
        assertEquals(PublicationStatus.FOR_SALE, actualStatus);
    }


    // ========== GETTER METHODS TESTS ==========

    /**
     * Tests that the getPrice method returns the correct price.
     */
    @Test
    void shouldReturnPrice() {
        // Arrange
        Price price = new Price(BigDecimal.valueOf(10.00));

        // Act
        Listing listing = new Listing(validTitle, severalItemList, SaleType.DIRECT_SALE, price, validDescription, validPictures);

        // Assert
        assertEquals(price, listing.getPrice());
    }

    /**
     * Tests that the getListingTitle method returns the correct title.
     */
    @Test
    void shouldReturnListingTitle() {
        // Arrange
        Listing validListing = new Listing(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        // Act
        String actualTitle = validListing.getListingTitle();

        // Assert
        assertEquals(validTitle, actualTitle);
    }

    /**
     * Tests that the getSeller method returns the owner of the first item in the listing.
     */
    @Test
    void shouldReturnSellerUsername() {
        // Arrange
        Listing validListing = new Listing(validTitle, oneItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        // Act
        User expectedSeller = oneItemList.get(0).getOwner();
        User actualSeller = validListing.getSeller();

        // Assert
        assertEquals(expectedSeller, actualSeller);
    }

    /**
     * Tests that the getItems method returns a copy of the items list,
     * not the original list reference.
     */
    @Test
    void shouldReturnACopyOfItems() {
        // Act
        Listing listing = new Listing(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);
        List<PublicationSample> items = listing.getItems();

        // Assert
        assertEquals(severalItemList, items);
        assertNotSame(severalItemList, items);
    }
}