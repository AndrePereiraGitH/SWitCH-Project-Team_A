package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectSaleTest {

    // Tests to constructor --------------------------------------------------------------------------------------------
    @Test
    void emptyConstructor_createsInstance() {
        DirectSale ds = new DirectSale();
        assertNotNull(ds);
    }

    @Test
    void validConstructor_createsInstance() {
        // Arrange
        Listing listing1Double = mock(Listing.class);
        Listing listing2Double = mock(Listing.class);
        User buyerDouble = mock(User.class);

        // Act
        DirectSale ds = new DirectSale(List.of(listing1Double, listing2Double), buyerDouble, false);

        // Assert
        assertNotNull(ds);
    }

    @Test
    void validConstructor_withNullListingsThrowsException() {
        // Arrange
        User buyerDouble = mock(User.class);

        // Act
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSale(null, buyerDouble, false)
        );

        // Assert
        assertEquals("List of listings cannot be null", e.getMessage());
    }

    @Test
    void validconstructor_withNullBuyerThrowsException() {
        // Arrange
        Listing listingDouble = mock(Listing.class);

        // Act
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSale(List.of(listingDouble), null, false)
        );

        // Assert
        assertEquals("Buyer cannot be empty", e.getMessage());
    }

    // Method equals ---------------------------------------------------------------------------------------------------
    @Test
    void equals_withNullReturnsFalse() {
        // Arrange
        Listing listingDouble = mock(Listing.class);
        User buyerDouble = mock(User.class);
        DirectSale ds = new DirectSale(List.of(listingDouble), buyerDouble, false);

        // Act
        boolean result = ds.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_withDifferentObjectsReturnsFalse() {
        // Arrange
        Listing listing1Double = mock(Listing.class);
        Listing listing2Double = mock(Listing.class);
        User buyerDouble = mock(User.class);

        DirectSale ds1 = new DirectSale(List.of(listing1Double), buyerDouble, false);
        DirectSale ds2 = new DirectSale(List.of(listing2Double), buyerDouble, true);

        // Act
        boolean result = ds1.equals(ds2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_withSameListingsAndBuyerReturnsTrue() {
        // Arrange
        Listing listingDouble = mock(Listing.class);
        User buyerDouble = mock(User.class);

        DirectSale ds1 = new DirectSale(List.of(listingDouble), buyerDouble, false);
        DirectSale ds2 = new DirectSale(List.of(listingDouble), buyerDouble, false);

        // Act
        boolean result = ds1.equals(ds2);

        // Assert
        assertTrue(result);
    }

    // Method hasCode --------------------------------------------------------------------------------------------------
    @Test
    void hashCode_sameObjectReturnsSameHash() {
        // Arrange
        Listing listingDouble = mock(Listing.class);
        User buyerDouble = mock(User.class);
        DirectSale ds = new DirectSale(List.of(listingDouble), buyerDouble, false);

        // Act and Assert
        assertEquals(ds.hashCode(), ds.hashCode());
    }

    @Test
    void hashCode_differentObjectsReturnsDifferentHash() {
        // Arrange
        Listing listing1Double = mock(Listing.class);
        Listing listing2Double = mock(Listing.class);
        User buyerDouble = mock(User.class);

        DirectSale ds1 = new DirectSale(List.of(listing1Double), buyerDouble, false);
        DirectSale ds2 = new DirectSale(List.of(listing2Double), buyerDouble, false);

        // Act and Assert
        assertNotEquals(ds1.hashCode(), ds2.hashCode());
    }

    // Method getListings ----------------------------------------------------------------------------------------------
    @Test
    void getListings_returnsSameList() {
        // Arrange
        Listing listingDouble = mock(Listing.class);
        User buyerDouble = mock(User.class);
        List<Listing> listings = List.of(listingDouble);

        DirectSale ds = new DirectSale(listings, buyerDouble, false);

        // Act
        List<Listing> result = ds.getListings();

        // Assert
        assertSame(listings, result);
    }

    // Method getDetails -----------------------------------------------------------------------------------------------
    @Test
    void getDetails_returnsCorrectValues() {
        // Arrange
        Listing listing1Double = mock(Listing.class);
        Listing listing2Double = mock(Listing.class);
        User buyerDouble = mock(User.class);

        List<Listing> listings = List.of(listing1Double, listing2Double);
        DirectSale ds = new DirectSale(listings, buyerDouble, false);

        // Act
        DirectSale.DirectSaleDetails details = ds.getDetails();

        // Assert
        assertNotNull(details);
        assertSame(listings, details.listings());
        assertSame(buyerDouble, details.buyer());
        assertFalse(details.isFinished());
    }

    // Method directSaleDetails (record) -------------------------------------------------------------------------------
    @Test
    void directSaleDetails_recordStoresValuesCorrectly() {
        // Arrange
        Listing listingDouble = mock(Listing.class);
        User buyerDouble = mock(User.class);
        List<Listing> listings = List.of(listingDouble);

        // Act
        DirectSale.DirectSaleDetails details = new DirectSale.DirectSaleDetails(listings, buyerDouble, true);

        // Assert
        assertSame(listings, details.listings());
        assertSame(buyerDouble, details.buyer());
        assertTrue(details.isFinished());
    }

    @Test
    void directSaleDetails_allowsNullValues() {
        // Act
        DirectSale.DirectSaleDetails details = new DirectSale.DirectSaleDetails(null, null, false);

        // Assert
        assertNull(details.listings());
        assertNull(details.buyer());
    }
}