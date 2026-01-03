package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.SaleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ListingTest {

//// Valid attributes
//    private final double validPrice = 50.00;
//    private final String validDescription = " ";
//    private final String validTitle = "PS5 Pro";
//    private final SaleType validSaleType = SaleType.DIRECT_SALE;
//    LocalDate validBirthday = LocalDate.of(2000, 1, 1);
//    Profile validProfile = new Profile("John",validBirthday,false);
//    User validSeller = new User("Seller1","123456",validProfile);
//    private final Condition validCondition = Condition.NEW;
//    Dimensions validDimensions = new Dimensions(40,50,60);
//    BibliographicalData validBibliographicalData = new BibliographicalData();
//    Item validItem = new Item()




    private final String validItem = "Item123";
    private final double validPrice = 50.00;
    private final String validTitle = "PS5 Pro";
    private final String validDescription = "Short description";
    private final String validSaleType = "SALE_TYPE";
    private final String validSeller = "User01";


    @Test
    void shouldCreateValidListingWhenDataIsValid(){
        // Act
        Listing listing = new Listing(validItem,validPrice,validDescription,validTitle,validSaleType,validSeller);

        // Assert
        assertEquals(validItem, listing.copyItem());
        assertEquals(validPrice, listing.copyPrice(),0.001);
        assertEquals(validDescription, listing.copyShortDescriptionCopy());
        assertEquals(validTitle, listing.copyListingTitle());
        assertEquals(validSaleType, listing.copySaleType());
        assertEquals(validSeller, listing.copySeller());

    }

    @Test
    void shouldThrowExceptionWhenTitleIsTooShort(){
        final String titleLessThanFiveCharacters = "abcd";
        assertThrows(IllegalArgumentException.class, ()-> new Listing(validItem,validPrice,validDescription,titleLessThanFiveCharacters,validSaleType,validSeller));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsTooLong(){
        final String titleTooLong = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        assertThrows(IllegalArgumentException.class, ()-> new Listing(validItem,validPrice,validDescription,titleTooLong,validSaleType,validSeller));
    }

    @Test
    void ensureTitleWith5CharsIsValid() {
        final String validTitle5Chars = "12345";

        Listing listing = new Listing(validItem,validPrice,validDescription,validTitle5Chars,validSaleType,validSeller);

        assertEquals(validTitle5Chars, listing.copyListingTitle());
    }

    @Test
    void ensureTitleWith100CharsIsValid() {
        final String validTitle100Chars = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

        Listing listing = new Listing(validItem,validPrice,validDescription,validTitle100Chars,validSaleType,validSeller);

        assertEquals(validTitle100Chars, listing.copyListingTitle());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsInvalid(){
        final double invalidPrice = -45.45;
        assertThrows(IllegalArgumentException.class, ()-> new Listing(validItem,invalidPrice,validDescription,validTitle,validSaleType,validSeller));
    }

    @Test
    void ensurePriceIsValid() {
        final double validPrice = 100.52;
        Listing listing = new Listing(validItem,validPrice,validDescription,validTitle,validSaleType,validSeller);
        assertEquals(validPrice, listing.copyPrice());
    }

    @Test
    void shouldConvertNullDescriptionToEmptyString() {
        String nullDescription = null;

        Listing listing = new Listing(validItem, validPrice, nullDescription, validTitle, validSaleType, validSeller);

        assertEquals("", listing.copyShortDescriptionCopy());
    }

    @Test
    void shouldConvertBlankDescriptionToEmptyString() {
        String blankDescription = "   ";

        Listing listing = new Listing(validItem, validPrice, blankDescription, validTitle, validSaleType, validSeller);

        assertEquals("", listing.copyShortDescriptionCopy());
    }

    @Test
    void ensureDescriptionIsReturnedCorrectly() {

        String descriptionWithText = "Description not null or blank";

        Listing listing = new Listing(validItem, validPrice, descriptionWithText, validTitle, validSaleType, validSeller);

        assertEquals(descriptionWithText, listing.copyShortDescriptionCopy());

    }


}