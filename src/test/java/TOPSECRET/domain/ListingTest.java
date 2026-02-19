package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ListingTest {

    // Valid primitive attributes
    private final String validDescription = "Valid Description";
    private final String validTitle = "PS5 Pro";


    //Price
    Price validPrice = new Price(BigDecimal.valueOf(50.99));

    // Pictures
    List<Picture> validPictures = List.of(new Picture("path/to/pic.jpg"));

    // Dimensions
    Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);

    // User
    LocalDate validBirthday = LocalDate.of(2000, 1, 1);
    Profile validProfile = new Profile("John",new Country("PORTUGAL"),validBirthday);
    Email validEmail = new Email("john.doe", "example.com");
    Address validAddress = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("PORTUGAL"));
    Contact validContact = new Contact("123456789","+351",validEmail,validAddress);
    User validSeller = new User("Seller1",validContact,validProfile,UserRole.NORMAL_USER);

    // BibliographicalData
    Profile profile = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
    Author author = new Author(profile, "valio bio");
    Address address = new Address("R Maria", "12", "156-5", new Country("PARAGUAY"));
    Email email = new Email("Maria1", "gmail.com");
    Contact contact = new Contact("915945695", "+351", email, address);
//    Publisher publisher = new Publisher("Some Publisher", "PT123456789", contact);
    BibliographicalData bibliographicalData = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author),
            Locale.ENGLISH,
//            publisher,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre ("Fiction")),
//            200,
            "Some text"
    );

    //Item
    // --- minimal objects needed for Publication ---

    PublicationType publicationType = new PublicationType(TypeOfBinding.PAPERBACK, "Paperback"
    );

    User user = new User("maria1", contact, profile, UserRole.NORMAL_USER);
    PublicationStatus publicationStatus = PublicationStatus.FOR_SALE;

    Code code1 = Code.fromIsbn13("9781234567890");
    Code code2 = Code.fromIsbn13("9781234567891");
    Code code3 = Code.fromIsbn13("9781234567892");

    // Edition
    Code code = Code.fromIsbn13 ("978316148412");
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
    Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
    Edition edition1= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition2= new Edition("2st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition3= new Edition("3st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    Item validItem1 = new Item(Condition.LIKE_NEW, 1.2, dimensions,
//            new Publication(bibliographicalData, publicationType, user, publicationStatus, "1st edition", code1));
            new Publication(bibliographicalData, publicationType, user, publicationStatus, edition1, code1));

    Item validItem2 = new Item(Condition.POOR, 1.2, dimensions,
//            new Publication(bibliographicalData, publicationType, user, publicationStatus, "2nd edition", code2));
            new Publication(bibliographicalData, publicationType, user, publicationStatus, edition2, code2));

    Item validItem3 = new Item(Condition.POOR, 1.2, dimensions,
//            new Publication(bibliographicalData, publicationType, user, publicationStatus, "3rd edition", code3));
            new Publication(bibliographicalData, publicationType, user, publicationStatus, edition3, code3));



    private final List<Item> oneItemList =
            new ArrayList<>(List.of(validItem1));

    private final List<Item> severalItemList =
            new ArrayList<>(List.of(validItem1, validItem2, validItem3));


    // Happy path

    @Test
    void shouldCreateValidListingWhenDataIsValidAndListHasOnlyOneBook(){
        // Arrange

        //Act
        Listing validListing = new Listing(validTitle,oneItemList,SaleType.DIRECT_SALE,validPrice,validDescription,validSeller, validPictures);

        //Assert
        assertNotNull(validListing);

    }


    @Test
    void shouldCreateValidListingWhenDataIsValidAndListHasSeveralBooks(){
        // Arrange

        //Act
        Listing validListing = new Listing(validTitle,severalItemList,SaleType.DIRECT_SALE,validPrice,validDescription,validSeller, validPictures);

        //Assert
        assertNotNull(validListing);

    }

    // Description tests

    @Test
    void shouldThrowExceptionWhenDescriptionIsTooShort(){
        // Arrange
        final String invalidDescription = "123456780";



        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(validTitle,severalItemList,SaleType.DIRECT_SALE,validPrice,invalidDescription,validSeller, validPictures));
    }

    @Test
    void ensureDescriptionWith10CharsIsValid(){

        // Arrange
        final String descriptionWith10Char = "1234567890";

        //Act
        Listing testingDescriptionWith10Char = new Listing(validTitle,severalItemList,SaleType.DIRECT_SALE,validPrice,descriptionWith10Char,validSeller, validPictures);

        //Assert
        assertNotNull(testingDescriptionWith10Char);
    }

    // Title tests

    @Test
    void shouldThrowExceptionWhenTitleIsTooShort(){

        // Arrange
        final String titleLessThanFiveCharacters = "acbd";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(titleLessThanFiveCharacters,severalItemList,SaleType.DIRECT_SALE,validPrice,validDescription,validSeller, validPictures));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsTooLong(){

        // Arrange
        final String titleTooLong = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(titleTooLong,severalItemList,SaleType.DIRECT_SALE,validPrice,validDescription,validSeller, validPictures));
    }

    @Test
    void ensureTitleWith5CharsIsValid() {

        //Arrange
        final String validTitle5Chars = "12345";

        // Act
        Listing listing = new Listing(validTitle5Chars,severalItemList,SaleType.DIRECT_SALE,validPrice,validDescription,validSeller, validPictures);

        // Assert
        assertNotNull(listing);
    }

    @Test
    void ensureTitleWith100CharsIsValid() {

        //Arrange
        final String validTitle100Chars = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

        // Act
        Listing listing = new Listing(validTitle100Chars,severalItemList,SaleType.DIRECT_SALE,validPrice,validDescription,validSeller, validPictures);

        // Assert
        assertNotNull(listing);
    }

    // Price test

    @Test
    void shouldReturnPrice(){

        //Arrange
        Price price = new Price(BigDecimal.valueOf(10.00));

        // Act
        Listing listing = new Listing(validTitle,severalItemList,SaleType.DIRECT_SALE,price,validDescription,validSeller, validPictures);

        // Assert
        assertEquals(price,listing.getPrice());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull(){

        //Arrange

        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(validTitle,severalItemList,SaleType.DIRECT_SALE,null,validDescription,validSeller, validPictures));

    }



    // Seller

    @Test
    void shouldThrowExceptionWhenSellerIsNull(){
        //Arrange

        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(validTitle,severalItemList,SaleType.DIRECT_SALE,validPrice,validDescription,null, validPictures));


    }


    // Item

    @Test
    void shouldThrowExceptionWhenItemIsNull(){

        //Arrange

        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(validTitle,null,SaleType.DIRECT_SALE,validPrice,validDescription,validSeller, validPictures));

    }

    @Test
    void shouldThrowExceptionWhenItemIsEmpty(){
        //Arrange
        final List<Item> emptyItemList =
                new ArrayList<>(List.of());


        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(validTitle,emptyItemList,SaleType.DIRECT_SALE,validPrice,validDescription,validSeller, validPictures));

    }

    @Test
    void shouldThrowExceptionWhenPicturesIsNull(){
        //Arrange

        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(validTitle,null,SaleType.DIRECT_SALE,validPrice,validDescription,validSeller, null));


    }

    // Methods

    @Test
    void shouldReturnACopyOfItems(){

        // Arrange


        //Act
        Listing listing = new Listing(validTitle,severalItemList,SaleType.DIRECT_SALE,validPrice,validDescription,validSeller, validPictures);
        List<Item> items = listing.getItems();

        //Assert
        assertEquals(severalItemList, items);
        assertNotSame(severalItemList, items);



    }




}