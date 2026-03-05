package TOPSECRET.repository;

import TOPSECRET.domain.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class DirectSaleRepositoryTest {

    private DirectSaleFactory dsFactoryDouble;
    private DirectSaleRepository dsRepo;
    private DirectSale dsDouble;

    private Listing listing1Double;
    private Listing listing2Double;
    private List<Listing> listings;
    private User buyerDouble;

    @BeforeEach
    void setUp() {

        listing1Double = mock(Listing.class);
        listing2Double = mock(Listing.class);
        listings = List.of(listing1Double, listing2Double);

        buyerDouble = mock(User.class);

        dsDouble = mock(DirectSale.class);
        dsFactoryDouble = mock(DirectSaleFactory.class);
        when(dsFactoryDouble.create(listings, buyerDouble)).thenReturn(dsDouble);

        dsRepo = new DirectSaleRepository(new ArrayList<>(), dsFactoryDouble);
    }

    // Method createAndAdd ---------------------------------------------------------------------------------------------

    @Test
    void createAndAdd_createsAndAddsDirectSale() {
        // Act
        DirectSale result = dsRepo.createAndAdd(listings, buyerDouble);

        // Assert
        assertEquals(dsDouble, result);
        assertTrue(dsRepo.ifDirectSaleExists(dsDouble));
        verify(dsFactoryDouble).create(listings, buyerDouble);
    }

    @Test
    void createAndAdd_ThrowsWhenDuplicate() {
        // Arrange
        dsRepo.addDirectSale(dsDouble);                                     // first add

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> dsRepo.createAndAdd(listings, buyerDouble));

        verify(dsFactoryDouble).create(listings, buyerDouble);
    }

    @Test
    void createAndAdd_propagatesFactoryException() {
        // Arrange
        when(dsFactoryDouble.create(listings, buyerDouble)).thenThrow(new IllegalArgumentException("Invalid listings"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> dsRepo.createAndAdd(listings, buyerDouble)
        );

        assertEquals("Invalid listings", ex.getMessage());
        verify(dsFactoryDouble).create(listings, buyerDouble);
    }

    @Test
    void createAndAdd_throwsWhenListingsNull() {
        // Arrange
        when(dsFactoryDouble.create(null, buyerDouble))
                .thenThrow(new IllegalArgumentException("Listings cannot be null"));

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> dsRepo.createAndAdd(null, buyerDouble));
    }

    @Test
    void createAndAdd_throwsWhenBuyerNull() {
        // Arrange
        when(dsFactoryDouble.create(listings, null))
                .thenThrow(new IllegalArgumentException("Buyer cannot be null"));

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> dsRepo.createAndAdd(listings, null));
    }

    @Test
    void createAndAdd_returnsExactlyFactoryResult() {
        // Arrange
        DirectSale ds1 = mock(DirectSale.class);
        DirectSale ds2 = mock(DirectSale.class);

        when(dsFactoryDouble.create(listings, buyerDouble)).thenReturn(ds1).thenReturn(ds2);

        // Act
        DirectSale r1 = dsRepo.createAndAdd(listings, buyerDouble);
        DirectSale r2 = dsRepo.createAndAdd(listings, buyerDouble);

        // Assert
        assertSame(ds1, r1);
        assertSame(ds2, r2);
    }


    //Method ifDirectSaleExists ----------------------------------------------------------------------------------------

    @Test
    void ifDirectSaleExists_returnsTrue() {
        // Arrange
        DirectSaleRepository repo = new DirectSaleRepository(new ArrayList<>(List.of(dsDouble)),dsFactoryDouble);

        // Act
        boolean result = repo.ifDirectSaleExists(dsDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void ifDirectSaleExists_returnsFalse() {
        // Arrange
        DirectSaleRepository repo = new DirectSaleRepository(new ArrayList<>(), dsFactoryDouble);        // Repo doesn't contain any ds

        // Act
        boolean result = repo.ifDirectSaleExists(dsDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void ifDirectSaleExists_constructorCopiesListNotOriginalReferences() {
        // Arrange
        List<DirectSale> externalList = new ArrayList<>();                    // Creates an empty external list of DirectSale

        DirectSaleRepository repo = new DirectSaleRepository(externalList, dsFactoryDouble);   // Creates the repository using the external list

        externalList.add(dsDouble);                                           // Modifies the external list after the repository was created

        // Act
        boolean result = repo.ifDirectSaleExists(dsDouble);

        // Assert
        assertFalse(result);                                                  // The repository must NOT have the change in the external list
    }


    //Method addDirectSale ---------------------------------------------------------------------------------------------
    @Test
    void addDirectSale_addsWhenNotExists() {
        // Act
        boolean result = dsRepo.addDirectSale(dsDouble);

        // Assert
        assertTrue(result);
        assertTrue(dsRepo.ifDirectSaleExists(dsDouble));
    }

    @Test
    void addDirectSale_returnsFalseWhenExists() {
        // Arrange
        DirectSaleRepository repo = new DirectSaleRepository(new ArrayList<>(List.of(dsDouble)), dsFactoryDouble);

        // Act
        boolean result = repo.addDirectSale(dsDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void addDirectSale_throwsWhenNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> dsRepo.addDirectSale(null));
    }


    //Method addDirectSaleorThrow --------------------------------------------------------------------------------------
    @Test
    void addDirectSaleOrThrow_addsWhenNotExists() {
        // Act
        dsRepo.addDirectSaleOrThrow(dsDouble);

        // Assert
        assertTrue(dsRepo.ifDirectSaleExists(dsDouble));
    }

    @Test
    void addDirectSaleOrThrow_throwsWhenExists() {
        // Arrange
        DirectSaleRepository repo = new DirectSaleRepository(new ArrayList<>(List.of(dsDouble)), dsFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.addDirectSaleOrThrow(dsDouble));
    }

    @Test
    void addDirectSaleOrThrow_throwsWhenNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> dsRepo.addDirectSaleOrThrow(null));
    }
}