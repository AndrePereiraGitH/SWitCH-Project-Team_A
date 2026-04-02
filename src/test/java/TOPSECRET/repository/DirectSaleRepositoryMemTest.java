package TOPSECRET.repository;

import TOPSECRET.domain.*;

import TOPSECRET.domain.DirectSale.DirectSale;
import TOPSECRET.domain.DirectSale.DirectSaleFactory;
import TOPSECRET.domain.DirectSale.IDirectSaleRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class DirectSaleRepositoryMemTest {

    private DirectSaleFactory dsFactoryDouble;
    private DirectSale dsDouble;

    private ListingId listing1Double;
    private ListingId listing2Double;
    private List<ListingId> listings;
    private UserId buyerDouble;

    @BeforeEach
    void setUp() {

        listing1Double = mock(ListingId.class);
        listing2Double = mock(ListingId.class);
        listings = List.of(listing1Double, listing2Double);

        buyerDouble = mock(UserId.class);

        dsDouble = mock(DirectSale.class);
        dsFactoryDouble = mock(DirectSaleFactory.class);
        when(dsFactoryDouble.create(listings, buyerDouble)).thenReturn(dsDouble);
    }

    // Method createAndAdd ---------------------------------------------------------------------------------------------

    @Test
    void createAndAdd_createsAndAddsDirectSale() {

        // Arrange
        IDirectSaleRepository dsRepo = new DirectSaleRepositoryMem(new ArrayList<>(), dsFactoryDouble);

        // Act
        DirectSale result = dsRepo.createAndAdd(listings, buyerDouble);

        // Assert
        assertEquals(dsDouble, result);
        assertTrue(dsRepo.exists(dsDouble));
    }

    @Test
    void createAndAdd_ThrowsWhenDuplicate() {
        // Arrange
        IDirectSaleRepository dsRepo = new DirectSaleRepositoryMem(new ArrayList<>(), dsFactoryDouble);
        dsRepo.add(dsDouble);                                     // first add

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> dsRepo.createAndAdd(listings, buyerDouble));
    }

    @Test
    void createAndAdd_propagatesFactoryException() {
        // Arrange
        when(dsFactoryDouble.create(listings, buyerDouble)).thenThrow(new IllegalArgumentException("Invalid listings"));

        IDirectSaleRepository dsRepo = new DirectSaleRepositoryMem(new ArrayList<>(), dsFactoryDouble);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> dsRepo.createAndAdd(listings, buyerDouble)
        );

        assertEquals("Invalid listings", ex.getMessage());
    }

    @Test
    void createAndAdd_throwsWhenListingsNull() {
        // Arrange
        when(dsFactoryDouble.create(null, buyerDouble))
                .thenThrow(new IllegalArgumentException("Listings cannot be null"));

        DirectSaleRepositoryMem dsRepo = new DirectSaleRepositoryMem(new ArrayList<>(), dsFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> dsRepo.createAndAdd(null, buyerDouble));
    }

    @Test
    void createAndAdd_throwsWhenBuyerNull() {
        // Arrange
        when(dsFactoryDouble.create(listings, null))
                .thenThrow(new IllegalArgumentException("Buyer cannot be null"));

        IDirectSaleRepository dsRepo = new DirectSaleRepositoryMem(new ArrayList<>(), dsFactoryDouble);

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

        IDirectSaleRepository dsRepo = new DirectSaleRepositoryMem(new ArrayList<>(), dsFactoryDouble);

        // Act
        DirectSale r1 = dsRepo.createAndAdd(listings, buyerDouble);
        DirectSale r2 = dsRepo.createAndAdd(listings, buyerDouble);

        // Assert
        assertSame(ds1, r1);
        assertSame(ds2, r2);
    }


    //Method exists ----------------------------------------------------------------------------------------

    @Test
    void exists_returnsTrue() {
        // Arrange
        IDirectSaleRepository repo = new DirectSaleRepositoryMem(new ArrayList<>(List.of(dsDouble)),dsFactoryDouble);

        // Act
        boolean result = repo.exists(dsDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void exists_returnsFalse() {
        // Arrange
        IDirectSaleRepository repo = new DirectSaleRepositoryMem(new ArrayList<>(), dsFactoryDouble);        // Repo doesn't contain any ds

        // Act
        boolean result = repo.exists(dsDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void exists_constructorCopiesListNotOriginalReferences() {
        // Arrange
        List<DirectSale> externalList = new ArrayList<>();                    // Creates an empty external list of DirectSale

        IDirectSaleRepository repo = new DirectSaleRepositoryMem(externalList, dsFactoryDouble);   // Creates the repository using the external list

        externalList.add(dsDouble);                                           // Modifies the external list after the repository was created

        // Act
        boolean result = repo.exists(dsDouble);

        // Assert
        assertFalse(result);                                                  // The repository must NOT have the change in the external list
    }


    //Method add -------------------------------------------------------------------------------------------------------

    @Test
    void add_addsWhenNotExists() {
        // Arrange
        IDirectSaleRepository dsRepo = new DirectSaleRepositoryMem(new ArrayList<>(), dsFactoryDouble);

        // Act
        dsRepo.add(dsDouble);

        // Assert
        assertTrue(dsRepo.exists(dsDouble));
    }

    @Test
    void add_throwsExceptionWhenExists() {
        // Arrange
        IDirectSaleRepository repo = new DirectSaleRepositoryMem(new ArrayList<>(List.of(dsDouble)), dsFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.add(dsDouble));
    }

    @Test
    void add_throwsExceptionWhenNull() {
        // Arrange
        IDirectSaleRepository dsRepo = new DirectSaleRepositoryMem(new ArrayList<>(), dsFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> dsRepo.add(null));
    }
}