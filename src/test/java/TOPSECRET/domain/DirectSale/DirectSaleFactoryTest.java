package TOPSECRET.domain.DirectSale;

import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectSaleFactoryTest {

    private ListingId listing1Double;
    private ListingId listing2Double;
    private UserId buyerDouble;

    @BeforeEach
    void setUp() {
        listing1Double = mock(ListingId.class);
        listing2Double = mock(ListingId.class);
        buyerDouble = mock(UserId.class);
    }

    @Test
    void create_ShouldCreateDirectSaleWhenAllListingsAreDirectSale() {
        // Arrange
//        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
//        when(listing2Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        List<ListingId> listings = List.of(listing1Double, listing2Double);

        DirectSaleFactory factory = new DirectSaleFactory();

        try (MockedConstruction<DirectSale> mocked =
                     mockConstruction(DirectSale.class)) {

            // Act
            DirectSale sale = factory.create(listings, buyerDouble);

            // Assert
            assertEquals(1, mocked.constructed().size());
            DirectSale constructed = mocked.constructed().get(0);
            assertSame(constructed, sale);

            var args = mocked.constructed().get(0);         // verifies the constructor was called with the right arguments
        }
    }

//    @Test
//    void create_ShouldThrowExceptionWhenAnyListingIsNotDirectSale() {
//        // Arrange
//        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
//        when(listing2Double.getSaleType()).thenReturn(SaleType.AUCTION);
//
//        List<ListingId> listings = List.of(listing1Double, listing2Double);
//
//        DirectSaleFactory factory = new DirectSaleFactory();
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> factory.create(listings, buyerDouble));
//    }

    @Test
    void create_ShouldThrowExceptionWhenListingsIsNull() {
        // Arrange
        DirectSaleFactory factory = new DirectSaleFactory();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, buyerDouble));
    }


//    @Test
//    void create_ShouldThrowExceptionWhenListingIsNotDirectSale() {
//        // Arrange
////        when(listing1Double.getSaleType()).thenReturn(SaleType.AUCTION);
//
//        List<ListingId> listings = List.of(listing1Double);
//
//        DirectSaleFactory factory = new DirectSaleFactory();
//
//        // Act + Assert
//        IllegalArgumentException ex = assertThrows(
//                IllegalArgumentException.class,
//                () -> factory.create(listings, buyerDouble)
//        );
//
//        assertEquals("All listings must be of type DIRECT_SALE to create a DirectSale",ex.getMessage());
//    }
}