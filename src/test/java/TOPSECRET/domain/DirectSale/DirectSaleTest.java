package TOPSECRET.domain.DirectSale;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DirectSaleTest {

    // Factory and doubles setup for tests
    DirectSaleFactory dsFactory = new DirectSaleFactory();;
    Listing listing1Double = mock(Listing.class);
    Listing listing2Double = mock(Listing.class);
    User buyerDouble = mock(User.class);

    // Sale Type setup
    @BeforeEach
    void setUp() {
        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing2Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
    }

    // Tests to constructor --------------------------------------------------------------------------------------------

    @Test
    void validConstructor_createsInstance() {

        // Act
        DirectSale ds = dsFactory.create(List.of(listing1Double), buyerDouble);

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
                () -> dsFactory.create(null, buyerDouble)
        );

        // Assert
        assertEquals("Listings cannot be null", e.getMessage());
    }

    @Test
    void validconstructor_withNullBuyerThrowsException() {

        // Act
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dsFactory.create(List.of(listing1Double), null)
        );

        // Assert
        assertEquals("Buyer cannot be empty", e.getMessage());
    }

    // Method equals ---------------------------------------------------------------------------------------------------
    @Test
    void equals_withNullReturnsFalse() {

        // Arrange
        DirectSale ds = dsFactory.create(List.of(listing1Double), buyerDouble);

        // Act
        boolean result = ds.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_withDifferentObjectsReturnsFalse() {

        // Arrange
        DirectSale ds1 = dsFactory.create(List.of(listing1Double), buyerDouble);
        DirectSale ds2 = dsFactory.create(List.of(listing2Double), buyerDouble);

        // Act
        boolean result = ds1.equals(ds2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_withSameListingsAndBuyerReturnsTrue() {

        // Arrange
        DirectSale ds1 = dsFactory.create(List.of(listing1Double), buyerDouble);
        DirectSale ds2 = dsFactory.create(List.of(listing1Double), buyerDouble);

        // Act
        boolean result = ds1.equals(ds2);

        // Assert
        assertTrue(result);
    }

    // Method hasCode --------------------------------------------------------------------------------------------------
    @Test
    void hashCode_sameObjectReturnsSameHash() {

        // Arrange
        DirectSale ds = dsFactory.create(List.of(listing1Double), buyerDouble);

        // Act and Assert
        assertEquals(ds.hashCode(), ds.hashCode());
    }

    @Test
    void hashCode_differentObjectsReturnsDifferentHash() {

        // Arrange
        DirectSale ds1 = dsFactory.create(List.of(listing1Double), buyerDouble);
        DirectSale ds2 = dsFactory.create(List.of(listing2Double), buyerDouble);

        // Act and Assert
        assertNotEquals(ds1.hashCode(), ds2.hashCode());
    }

    // Method getListings ----------------------------------------------------------------------------------------------
    @Test
    void getListings_returnsSameList() {

        // Arrange
        List<Listing> listings = List.of(listing1Double, listing2Double);
        DirectSale ds = dsFactory.create(listings, buyerDouble);

        // Act
        List<Listing> result = ds.getListings();

        // Assert
        assertSame(listings, result);
    }

    // Method getDetails -----------------------------------------------------------------------------------------------
    @Test
    void getDetails_returnsCorrectValues() {

        // Arrange
        List<Listing> listings = List.of(listing1Double, listing2Double);
        DirectSale ds = dsFactory.create(listings, buyerDouble);

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
        List<Listing> listings = List.of(listing1Double, listing2Double);

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