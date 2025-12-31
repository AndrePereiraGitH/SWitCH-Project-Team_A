package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListingTest {


    private final String validItem = "Item123";
    private final double validPrice = 50.00;
    private final String validTitle = "PS5 Pro";
    private final String validDescription = " ";
    private final String validSaleType = "SALE_TYPE";
    private final String validSeller = "User01";

    @Test
    void shouldCreateValidListingWhenDataIsValid(){

        Listing validListing = new Listing(validItem,validPrice,validDescription,validTitle,validSaleType,validSeller);

        assertNotNull(validListing);
    }

    @Test
    void shouldThrowExceptionWhenTitleIsNull(){
        final String titleNull = null;

        assertThrows(IllegalArgumentException.class, ()->{
            new Listing(validItem,validPrice,validDescription,titleNull,validSaleType,validSeller);
        });
    }

    @Test
    void shouldThrowExceptionWhenTitleIsEmpty(){
        final String titleEmpty = "";
        assertThrows(IllegalArgumentException.class, ()->{
            new Listing(validItem,validPrice,validDescription,titleEmpty,validSaleType,validSeller);
        });
    }

    @Test
    void shouldThrowExceptionWhenTitleIsTooShort(){
        final String titleLessThanFiveCharacters = "abcd";
        assertThrows(IllegalArgumentException.class, ()->{
            new Listing(validItem,validPrice,validDescription,titleLessThanFiveCharacters,validSaleType,validSeller);
        });
    }

    @Test
    void shouldThrowExceptionWhenTitleIsTooLong(){
        final String titleTooLong = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        assertThrows(IllegalArgumentException.class, ()->{
            new Listing(validItem,validPrice,validDescription,titleTooLong,validSaleType,validSeller);
        });
    }

    @Test
    void shouldThrowExceptionWhenPriceIsInvalid(){
        final double invalidPrice = -45.45;
        assertThrows(IllegalArgumentException.class, ()->{
            new Listing(validItem,invalidPrice,validDescription,validTitle,validSaleType,validSeller);
        });
    }





}