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

    // Saletype
    private final SaleType validSaleType = SaleType.DIRECT_SALE;

    //Price
    Price validPrice = new Price(BigDecimal.valueOf(50.99));

    // Pictures
    List<Picture> pictures = List.of(new Picture("path/to/pic.jpg"));

    // Dimensions
    Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);

    // User
    LocalDate validBirthday = LocalDate.of(2000, 1, 1);
    Profile validProfile = new Profile("John",Country.PORTUGAL,validBirthday);
    Email validEmail = new Email("john.doe", "example.com");
    Address validAddress = new Address("dummyStreet", "dummyNumber", "dummyZipCode", Country.PORTUGAL);
    Contact validContact = new Contact("123456789","+351",validEmail,validAddress);
    User validSeller = new User("Seller1",validContact,validProfile);

    // BibliographicalData
    Profile author = new Profile("Tove Ditlevsen", Country.DENMARK, LocalDate.of(1917, 12, 14));
    Address address = new Address("R Maria", "12", "156-5", Country.PARAGUAY);
    Email email = new Email("Maria1", "gmail.com");
    Contact contact = new Contact("915945695", "+351", email, address);
    Publisher publisher = new Publisher("Some Publisher", "PT123456789", contact);
    BibliographicalData bibliographicalData = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author),
            Locale.ENGLISH,
            publisher,
            LocalDate.of(2020, 1, 1),
            List.of(Genre.FICTION),
            200,
            "Some text"
    );


    // Item

    Item validItem1 = new Item(Condition.LIKE_NEW,1.2,pictures,dimensions,TypeOfBinding.PAPERBACK,"1st edition"
            ,bibliographicalData);

    Item validItem2 = new Item(Condition.POOR,1.2,pictures,dimensions,TypeOfBinding.PAPERBACK,"2nd edition"
            ,bibliographicalData);

    Item validItem3 = new Item(Condition.POOR,1.2,pictures,dimensions,TypeOfBinding.PAPERBACK,"3rd edition"
            ,bibliographicalData);




    private final List<Item> oneItemList =
            new ArrayList<>(List.of(validItem1));

    private final List<Item> severalItemList =
            new ArrayList<>(List.of(validItem1, validItem2, validItem3));


    // Happy path

    @Test
    void shouldCreateValidListingWhenDataIsValidAndListHasOnlyOneBook(){
        // Arrange

        //Act
        Listing validListing = new Listing(oneItemList,validPrice,validDescription,validTitle,validSaleType,validSeller);

        //Assert
        assertNotNull(validListing);

    }


    @Test
    void shouldCreateValidListingWhenDataIsValidAndListHasSeveralBooks(){
        // Arrange

        //Act
        Listing validListing = new Listing(severalItemList,validPrice,validDescription,validTitle,validSaleType,validSeller);

        //Assert
        assertNotNull(validListing);

    }

    // Description tests

    @Test
    void shouldThrowExceptionWhenDescriptionIsTooShort(){
        // Arrange
        final String invalidDescription = "123456780";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(oneItemList,validPrice,invalidDescription,validTitle,validSaleType,validSeller));
    }

    @Test
    void ensureDescriptionWith10CharsIsValid(){

        // Arrange
        final String descriptionWith10Char = "1234567890";

        //Act
        Listing testingDescriptionWith10Char = new Listing(oneItemList,validPrice,descriptionWith10Char,validTitle,validSaleType,validSeller);

        //Assert
        assertNotNull(testingDescriptionWith10Char);
    }

    // Title tests

    @Test
    void shouldThrowExceptionWhenTitleIsTooShort(){

        // Arrange
        final String titleLessThanFiveCharacters = "acbd";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(oneItemList,validPrice,validDescription,titleLessThanFiveCharacters,validSaleType,validSeller));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsTooLong(){

        // Arrange
        final String titleTooLong = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(oneItemList,validPrice,validDescription,titleTooLong,validSaleType,validSeller));
    }

    @Test
    void ensureTitleWith5CharsIsValid() {

        //Arrange
        final String validTitle5Chars = "12345";

        // Act
        Listing listing = new Listing(oneItemList,validPrice,validDescription,validTitle5Chars,validSaleType,validSeller);

        // Assert
        assertNotNull(listing);
    }

    @Test
    void ensureTitleWith100CharsIsValid() {

        //Arrange
        final String validTitle100Chars = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

        // Act
        Listing listing = new Listing(oneItemList,validPrice,validDescription,validTitle100Chars,validSaleType,validSeller);

        // Assert
        assertNotNull(listing);
    }

    // Price test

    @Test
    void shouldReturnPrice(){

        //Arrange
        Price price = new Price(BigDecimal.valueOf(10.00));

        // Act
        Listing listing = new Listing(oneItemList,price,validDescription,validTitle,validSaleType,validSeller);

        // Assert
        assertEquals(price,listing.getPrice());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull(){

        //Arrange
        Price nullPrice = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(oneItemList,nullPrice,validDescription,validTitle,validSaleType,validSeller));

    }

    // Saletype

    @Test
    void shouldThrowExceptionWhenSaleTypeIsNull(){
        //Arrange
        SaleType nullSaleType = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(oneItemList,validPrice,validDescription,validTitle,nullSaleType,validSeller));

    }

    // Seller

    @Test
    void shouldThrowExceptionWhenSellerIsNull(){
        //Arrange
        User nullSeller = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(oneItemList,validPrice,validDescription,validTitle,validSaleType,nullSeller));

    }


    // Item

    @Test
    void shouldThrowExceptionWhenItemIsNull(){

        //Arrange
        final List<Item> nullItemList = null;


        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(nullItemList,validPrice,validDescription,validTitle,validSaleType,validSeller));

    }

    @Test
    void shouldThrowExceptionWhenItemIsEmpty(){
        //Arrange
        final List<Item> emptyItemList =
                new ArrayList<>(List.of());


        // Act & Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(emptyItemList,validPrice,validDescription,validTitle,validSaleType,validSeller));

    }

    // Methods

    @Test
    void shouldReturnACopyOfItems(){

        // Arrange


        //Act
        Listing listing = new Listing(severalItemList, validPrice, validDescription, validTitle, validSaleType, validSeller);
        List<Item> items = listing.getItems();

        //Assert
        assertEquals(severalItemList, items);
        assertNotSame(severalItemList, items);



    }




}