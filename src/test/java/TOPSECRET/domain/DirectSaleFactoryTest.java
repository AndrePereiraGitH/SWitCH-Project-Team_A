package TOPSECRET.domain;

import TOPSECRET.domain.Enum.SaleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DirectSaleFactoryTest {

    private DirectSaleFactory factory;
    private Listing listing1Double;
    private Listing listing2Double;
    private User buyerDouble;

    @BeforeEach
    void setUp() {
        factory = new DirectSaleFactory();

        listing1Double = mock(Listing.class);
        listing2Double = mock(Listing.class);
        buyerDouble = mock(User.class);
    }

    @Test
    void create_ShouldCreateDirectSaleWhenAllListingsAreDirectSale() {
        // Arrange
        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing2Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        List<Listing> listings = List.of(listing1Double, listing2Double);

        // Act
        DirectSale sale = factory.create(listings, buyerDouble);

        // Assert
        assertNotNull(sale);
        assertEquals(listings, sale.getListings());
        assertEquals(buyerDouble, sale.getDetails().buyer());
        assertFalse(sale.getDetails().isFinished());
    }

    @Test
    void create_ShouldThrowExceptionWhenAnyListingIsNotDirectSale() {
        // Arrange
        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing2Double.getSaleType()).thenReturn(SaleType.AUCTION); // inválido

        List<Listing> listings = List.of(listing1Double, listing2Double);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> factory.create(listings, buyerDouble)
        );

        assertEquals("All listings must be of type DIRECT_SALE to create a DirectSale", ex.getMessage());
    }
    @Test
    void create_WithFinishedFlagShouldRespectFinishedState() {
        // Arrange
        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        List<Listing> listings = List.of(listing1Double);

        // Act
        DirectSale sale = factory.create(listings, buyerDouble, true);

        // Assert
        assertTrue(sale.getDetails().isFinished());
    }

    @Test
    void create_ShouldThrowExceptionWhenListingsIsNull() {
        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> factory.create(null, buyerDouble)
        );

        assertEquals("Listings cannot be null", ex.getMessage());
    }

    @Test
    void create_ShouldThrowExceptionWhenListingIsNotDirectSale() {
        // Arrange
        when(listing1Double.getSaleType()).thenReturn(SaleType.AUCTION);

        List<Listing> listings = List.of(listing1Double);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> factory.create(listings, buyerDouble)
        );

        assertEquals("All listings must be of type DIRECT_SALE to create a DirectSale",ex.getMessage());
    }

}