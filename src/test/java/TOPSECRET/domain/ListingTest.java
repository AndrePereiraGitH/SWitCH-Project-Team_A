package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ListingTest {

// Valid attributes
    private final String validDescription = "Valid Description";
    private final String validTitle = "PS5 Pro";
    private final SaleType validSaleType = SaleType.DIRECT_SALE;

    Price validPrice = new Price(BigDecimal.valueOf(50.99));
    LocalDate validBirthday = LocalDate.of(2000, 1, 1);
    Profile validProfile = new Profile("John",Country.PORTUGAL,validBirthday,false);
    Email validEmail = new Email("john.doe", "example.com");
    Address validAddress = new Address("dummyStreet", "dummyNumber", "dummyZipCode", Country.PORTUGAL);
    Contact validContact = new Contact("123456789","+351",validEmail,validAddress);
    User validSeller = new User("Seller1",validContact,validProfile);


    Profile author = new Profile("Tove Ditlevsen", Country.DENMARK, LocalDate.of(1917, 12, 14), true);
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
    Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);
    List<Picture> pictures = List.of(new Picture("path/to/pic.jpg"));
    Item validItem = new Item(Condition.LIKE_NEW,1.2,pictures,dimensions,TypeOfBinding.PAPERBACK,"1st edition"
            ,bibliographicalData);


    @Test
    void shouldCreateValidListingWhenDataIsValid(){
        // Arrange

        //Act
        Listing validListing = new Listing(validItem,validPrice,validDescription,validTitle,validSaleType,validSeller);

        //Assert
        assertNotNull(validListing);

    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsTooShort(){
        // Arrange
        final String invalidDescription = "123456780";

        //Act

        //Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(validItem,validPrice,invalidDescription,validTitle,validSaleType,validSeller));
    }

    @Test
    void ensureDescriptionWith10CharsIsValid(){
        // Arrange
        final String descriptionWith10Char = "1234567890";

        //Act
        Listing testingDescriptionWith10Char = new Listing(validItem,validPrice,descriptionWith10Char,validTitle,validSaleType,validSeller);

        //Assert
        assertNotNull(testingDescriptionWith10Char);
    }



    @Test
    void shouldThrowExceptionWhenTitleIsTooShort(){
        // Arrange
        final String titleLessThanFiveCharacters = "acbd";

        //Act

        //Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(validItem,validPrice,validDescription,titleLessThanFiveCharacters,validSaleType,validSeller));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsTooLong(){
        // Arrange
        final String titleTooLong = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        // Act
        // Assert
        assertThrows(IllegalArgumentException.class, ()-> new Listing(validItem,validPrice,validDescription,titleTooLong,validSaleType,validSeller));
    }

    @Test
    void ensureTitleWith5CharsIsValid() {
        //Arrange
        final String validTitle5Chars = "12345";
        // Act
        Listing listing = new Listing(validItem,validPrice,validDescription,validTitle5Chars,validSaleType,validSeller);
        // Assert
        assertNotNull(listing);
    }

    @Test
    void ensureTitleWith100CharsIsValid() {
        //Arrange
        final String validTitle100Chars = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        // Act
        Listing listing = new Listing(validItem,validPrice,validDescription,validTitle100Chars,validSaleType,validSeller);
        // Assert
        assertNotNull(listing);
    }

    @Test

    void shouldReturnPrice(){
        //Arrange
        Price price = new Price(BigDecimal.valueOf(10.00));
        // Act
        Listing listing = new Listing(validItem,price,validDescription,validTitle,validSaleType,validSeller);
        // Assert
        assertEquals(price,listing.getPrice());
    }


}