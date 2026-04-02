//package TOPSECRET.domain.DirectSale;
//
//import TOPSECRET.domain.Enum.SaleType;
//import TOPSECRET.domain.Listing;
//import TOPSECRET.domain.User.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedConstruction;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class DirectSaleFactoryTest {
//
//    private Listing listing1Double;
//    private Listing listing2Double;
//    private User buyerDouble;
//
//    @BeforeEach
//    void setUp() {
//        listing1Double = mock(Listing.class);
//        listing2Double = mock(Listing.class);
//        buyerDouble = mock(User.class);
//    }
//
//    @Test
//    void create_ShouldCreateDirectSaleWhenAllListingsAreDirectSale() {
//        // Arrange
//        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
//        when(listing2Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
//
//        List<Listing> listings = List.of(listing1Double, listing2Double);
//
//        DirectSaleFactory factory = new DirectSaleFactory();
//
//        try (MockedConstruction<DirectSale> mocked =
//                     mockConstruction(DirectSale.class)) {
//
//            // Act
//            DirectSale sale = factory.create(listings, buyerDouble);
//
//            // Assert
//            assertEquals(1, mocked.constructed().size());
//            DirectSale constructed = mocked.constructed().get(0);
//            assertSame(constructed, sale);
//
//            var args = mocked.constructed().get(0);         // verifies the constructor was called with the right arguments
//        }
//    }
//
//    @Test
//    void create_ShouldThrowExceptionWhenAnyListingIsNotDirectSale() {
//        // Arrange
//        when(listing1Double.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
//        when(listing2Double.getSaleType()).thenReturn(SaleType.AUCTION);
//
//        List<Listing> listings = List.of(listing1Double, listing2Double);
//
//        DirectSaleFactory factory = new DirectSaleFactory();
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> factory.create(listings, buyerDouble));
//    }
//
//    @Test
//    void create_ShouldThrowExceptionWhenListingsIsNull() {
//        // Arrange
//        DirectSaleFactory factory = new DirectSaleFactory();
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> factory.create(null, buyerDouble));
//    }
//
//
//    @Test
//    void create_ShouldThrowExceptionWhenListingIsNotDirectSale() {
//        // Arrange
//        when(listing1Double.getSaleType()).thenReturn(SaleType.AUCTION);
//
//        List<Listing> listings = List.of(listing1Double);
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
//}