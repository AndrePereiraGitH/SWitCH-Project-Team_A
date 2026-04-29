package TOPSECRET.domain.DirectSale;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectSaleFactoryTest {

    private ListingId listing1IdDouble;
    private ListingId listing2IdDouble;
    private Listing listing1Double;
    private Listing listingDouble2;
    private UserId buyerDouble;
    private IListingRepository listingRepoDouble;

    @BeforeEach
    void setUp() {
        listing1IdDouble = mock(ListingId.class);
        listing2IdDouble = mock(ListingId.class);
        listing1Double = mock(Listing.class);
        listingDouble2 = mock(Listing.class);
        buyerDouble = mock(UserId.class);
        listingRepoDouble = mock(IListingRepository.class);
    }

    @Test
    void create_ShouldCreateDirectSaleWhenAllListingsAreDirectSale() {
        // Arrange
        when(listingRepoDouble.findById(listing1IdDouble)).thenReturn(Optional.of(listing1Double));
        when(listingRepoDouble.findById(listing2IdDouble)).thenReturn(Optional.of(listingDouble2));

        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listingDouble2.getSaleType()).thenReturn(SaleType.DIRECT_SALE);


        List<ListingId> listings = List.of(listing1IdDouble, listing2IdDouble);

        DirectSaleFactory factory = new DirectSaleFactory();

        try (MockedConstruction<DirectSale> mocked =
                     mockConstruction(DirectSale.class)) {

            // Act
            DirectSale sale = factory.create(listings, buyerDouble, listingRepoDouble);

            // Assert
            assertEquals(1, mocked.constructed().size());
            DirectSale constructed = mocked.constructed().get(0);
            assertSame(constructed, sale);
        }
    }

    @Test
    void create_ShouldThrowExceptionWhenAnyListingIsNotDirectSale() {
        // Arrange
        when(listingRepoDouble.findById(listing1IdDouble)).thenReturn(Optional.of(listing1Double));
        when(listingRepoDouble.findById(listing2IdDouble)).thenReturn(Optional.of(listingDouble2));

        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listingDouble2.getSaleType()).thenReturn(SaleType.AUCTION);

        List<ListingId> listings = List.of(listing1IdDouble, listing2IdDouble);

        DirectSaleFactory factory = new DirectSaleFactory();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(listings, buyerDouble, listingRepoDouble));
    }

    @Test
    void create_ShouldThrowExceptionWhenListingsIsNull() {
        // Arrange
        DirectSaleFactory factory = new DirectSaleFactory();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, buyerDouble, listingRepoDouble));
    }


    @Test
    void create_ShouldThrowExceptionWhenListingIsNotDirectSale() {
        // Arrange
        when(listingRepoDouble.findById(listing1IdDouble)).thenReturn(Optional.of(listing1Double));

        when(listing1Double.getSaleType()).thenReturn(SaleType.AUCTION);

        List<ListingId> listings = List.of(listing1IdDouble);

        DirectSaleFactory factory = new DirectSaleFactory();

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> factory.create(listings, buyerDouble, listingRepoDouble)
        );

        assertEquals("All listings must be of type DIRECT_SALE",ex.getMessage());
    }

    @Test
    void shouldThrowWhenListingNotFound() {
        // Arrange
        ListingId missingId = new ListingId("123");
        List<ListingId> listingIds = List.of(missingId);

        UserId buyer = new UserId("buyer-1");

        IListingRepository listingRepo = mock(IListingRepository.class);
        when(listingRepo.findById(missingId)).thenReturn(Optional.empty());

        DirectSaleFactory service = new DirectSaleFactory();

        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(listingIds, buyer, listingRepo)
        );

        // Assert
        assertEquals("Listing not found: " + missingId, ex.getMessage());
    }
}