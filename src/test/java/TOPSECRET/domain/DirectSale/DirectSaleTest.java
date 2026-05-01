package TOPSECRET.domain.DirectSale;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectSaleTest {

    // Factory and doubles setup for tests
    DirectSaleFactory dsFactory = new DirectSaleFactory();;
    ListingId listingId1Double = mock(ListingId.class);
    ListingId listingId2Double = mock(ListingId.class);
    Listing listing1Double = mock(Listing.class);
    Listing listing2Double = mock(Listing.class);
    UserId buyerDouble = mock(UserId.class);
    IListingRepository listingRepoDouble = mock(IListingRepository.class);

//     Sale Type setup
    @BeforeEach
    void setUp() {
        when(listingRepoDouble.findById(listingId1Double)).thenReturn(Optional.ofNullable(listing1Double));
        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        when(listingRepoDouble.findById(listingId2Double)).thenReturn(Optional.ofNullable(listing2Double));
        when(listing2Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
    }

    // Tests to constructor --------------------------------------------------------------------------------------------

    @Test
    void validConstructor_createsInstance() {

        // Act
        DirectSale ds = dsFactory.create(List.of(listingId1Double), buyerDouble, listingRepoDouble);

        // Assert
        assertNotNull(ds);
    }

    @Test
    void validConstructor_withNullListingsThrowsException() {

        // Arrange
        UserId buyerDouble = mock(UserId.class);

        // Act
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dsFactory.create(null, buyerDouble, listingRepoDouble)
        );

        // Assert
        assertEquals("Listings cannot be null", e.getMessage());
    }

    @Test
    void validconstructor_withNullBuyerThrowsException() {

        // Act
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> dsFactory.create(List.of(listingId1Double), null, listingRepoDouble)
        );

        // Assert
        assertEquals("Buyer cannot be empty", e.getMessage());
    }

    // Method equals ---------------------------------------------------------------------------------------------------
    @Test
    void equals_withNullReturnsFalse() {

        // Arrange
        DirectSale ds = dsFactory.create(List.of(listingId1Double), buyerDouble, listingRepoDouble);

        // Act
        boolean result = ds.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_withDifferentObjectsReturnsFalse() {

        // Arrange
        DirectSale ds1 = dsFactory.create(List.of(listingId1Double), buyerDouble, listingRepoDouble);
        DirectSale ds2 = dsFactory.create(List.of(listingId2Double), buyerDouble, listingRepoDouble);

        // Act
        boolean result = ds1.equals(ds2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_withSameListingsAndBuyerReturnsTrue() {

        // Arrange
        DirectSale ds1 = dsFactory.create(List.of(listingId1Double), buyerDouble, listingRepoDouble);
        DirectSale ds2 = dsFactory.create(List.of(listingId1Double), buyerDouble, listingRepoDouble);

        // Act
        boolean result = ds1.equals(ds2);

        // Assert
        assertTrue(result);
    }

    // Method hasCode --------------------------------------------------------------------------------------------------
    @Test
    void hashCode_sameObjectReturnsSameHash() {

        // Arrange
        DirectSale ds = dsFactory.create(List.of(listingId1Double), buyerDouble, listingRepoDouble);

        // Act and Assert
        assertEquals(ds.hashCode(), ds.hashCode());
    }

    @Test
    void hashCode_differentObjectsReturnsDifferentHash() {

        // Arrange
        DirectSale ds1 = dsFactory.create(List.of(listingId1Double), buyerDouble, listingRepoDouble);
        DirectSale ds2 = dsFactory.create(List.of(listingId2Double), buyerDouble, listingRepoDouble);

        // Act and Assert
        assertNotEquals(ds1.hashCode(), ds2.hashCode());
    }

    // Method getListings ----------------------------------------------------------------------------------------------
    @Test
    void getListings_returnsSameList() {

        // Arrange
        List<ListingId> listings = List.of(listingId1Double, listingId2Double);
        DirectSale ds = dsFactory.create(listings, buyerDouble, listingRepoDouble);

        // Act
        List<ListingId> result = ds.getListings();

        // Assert
        assertSame(listings, result);
    }

    // Method getDetails -----------------------------------------------------------------------------------------------
    @Test
    void getDetails_returnsCorrectValues() {

        // Arrange
        List<ListingId> listings = List.of(listingId1Double, listingId2Double);
        DirectSale ds = dsFactory.create(listings, buyerDouble, listingRepoDouble);

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
        List<ListingId> listings = List.of(listingId1Double, listingId2Double);

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

    @Test
    void id_shouldReturnNonNull() {
        // Arrange
        List<ListingId> listings = List.of(new ListingId("L1"));
        UserId buyer = new UserId("U1");

        // Act
        DirectSale sale = new DirectSale(listings, buyer, false);

        // Assert
        assertNotNull(sale.id());
    }

    // Same as tests
    @Test
    void sameAs_shouldReturnFalse_whenOtherIsNull() {
        // Act
        DirectSale sale = new DirectSale(List.of(new ListingId("L1")), new UserId("U1"), false);

        // Assert
        assertFalse(sale.sameAs(null));
    }

    @Test
    void sameAs_shouldReturnFalse_whenDifferentType() {
        // Act
        DirectSale sale = new DirectSale(List.of(new ListingId("L1")), new UserId("U1"), false);

        // Assert
        assertFalse(sale.sameAs("not a DirectSale"));
    }

    @Test
    void sameAs_shouldReturnTrue_whenSameInstance() {
        // Arrange
        DirectSale sale = new DirectSale(List.of(new ListingId("L1")), new UserId("U1"), false);

        // Assert
        assertTrue(sale.sameAs(sale));
    }

    @Test
    void sameAs_shouldReturnFalse_whenDifferentInstance() {
        // Arrange
        DirectSale sale1 = new DirectSale(List.of(new ListingId("L1")), new UserId("U1"), false);
        DirectSale sale2 = new DirectSale(List.of(new ListingId("L1")), new UserId("U1"), false);

        // Assert
        assertFalse(sale1.sameAs(sale2));
    }
}