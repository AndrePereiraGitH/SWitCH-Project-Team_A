package TOPSECRET.repository;

import TOPSECRET.domain.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class DirectSaleRepositoryTest {

    //Method ifDirectSaleExists ----------------------------------------------------------------------------
    @Test
    void ifDirectSaleExists_returnsTrue_withProfessorLogic() {
        // Arrange
        DirectSale dsDouble = mock(DirectSale.class);

        Listing listing1Double = mock(Listing.class);
        Listing listing2Double = mock(Listing.class);

        List<Listing> listingListDouble = List.of(listing1Double, listing2Double);
        when(dsDouble.getListings()).thenReturn(listingListDouble);

        DirectSaleRepository repo = new DirectSaleRepository(new ArrayList<>(List.of(dsDouble)));

        // Act
        boolean result = repo.ifDirectSaleExists(dsDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void ifDirectSaleExists_returnsFalse() {
        // Arrange
        DirectSale dsDouble = mock(DirectSale.class);

        Listing listing1Double = mock(Listing.class);
        Listing listing2Double = mock(Listing.class);

        List<Listing> listingListDouble = List.of(listing1Double, listing2Double);
        when(dsDouble.getListings()).thenReturn(listingListDouble);

        DirectSaleRepository repo = new DirectSaleRepository(new ArrayList<>());        // Repo doesn't contain any ds

        // Act
        boolean result = repo.ifDirectSaleExists(dsDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void ifDirectSaleExists_constructorCopiesListNotOriginalReferences() {
        // Arrange
        DirectSale dsDouble = mock(DirectSale.class);

        Listing listingDouble = mock(Listing.class);

        List<Listing> listingListDouble = List.of(listingDouble);
        when(dsDouble.getListings()).thenReturn(listingListDouble);

        List<DirectSale> externalList = new ArrayList<>();                    // Creates an empty external list of DirectSale

        DirectSaleRepository repo = new DirectSaleRepository(externalList);   // Creates the repository using the external list

        externalList.add(dsDouble);                                           // Modifies the external list after the repository was created

        // Act
        boolean result = repo.ifDirectSaleExists(dsDouble);

        // Assert
        assertFalse(result);                                                  // The repository must NOT have the change in the external list
    }


    //Method addDirectSale ---------------------------------------------------------------------------------------------
    @Test
    void addDirectSale_addsWhenNotExists() {
        // Arrange
        DirectSale dsDouble = mock(DirectSale.class);
        when(dsDouble.getListings()).thenReturn(List.of(mock(Listing.class)));

        DirectSaleRepository repo = new DirectSaleRepository(new ArrayList<>());

        // Act
        boolean result = repo.addDirectSale(dsDouble);

        // Assert
        assertTrue(result);
        assertTrue(repo.ifDirectSaleExists(dsDouble));
    }

    @Test
    void addDirectSale_returnsFalseWhenExists() {
        // Arrange
        DirectSale dsDouble = mock(DirectSale.class);
        when(dsDouble.getListings()).thenReturn(List.of(mock(Listing.class)));

        DirectSaleRepository repo = new DirectSaleRepository(new ArrayList<>(List.of(dsDouble)));

        // Act
        boolean result = repo.addDirectSale(dsDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void addDirectSale_throwsWhenNull() {
        // Arrange
        DirectSaleRepository repo = new DirectSaleRepository(new ArrayList<>());

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.addDirectSale(null));
    }


    //Method addDirectSaleorThrow --------------------------------------------------------------------------------------
    @Test
    void addDirectSaleOrThrow_addsWhenNotExists() {
        // Arrange
        DirectSale dsDouble = mock(DirectSale.class);
        when(dsDouble.getListings()).thenReturn(List.of(mock(Listing.class)));

        DirectSaleRepository repo = new DirectSaleRepository(new ArrayList<>());

        // Act
        repo.addDirectSaleOrThrow(dsDouble);

        // Assert
        assertTrue(repo.ifDirectSaleExists(dsDouble));
    }

    @Test
    void addDirectSaleOrThrow_throwsWhenExists() {
        // Arrange
        DirectSale dsDouble = mock(DirectSale.class);
        when(dsDouble.getListings()).thenReturn(List.of(mock(Listing.class)));

        DirectSaleRepository repo = new DirectSaleRepository(new ArrayList<>(List.of(dsDouble)));

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.addDirectSaleOrThrow(dsDouble));
    }

    @Test
    void addDirectSaleOrThrow_throwsWhenNull() {
        // Arrange
        DirectSaleRepository repo = new DirectSaleRepository(new ArrayList<>());

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.addDirectSaleOrThrow(null));
    }


    // TODO - passar os testes deste método para a factory
    // Method createAndAddDirectSale -----------------------------------------------------------------------------------
//    @Test
//    void createAndAddDirectSale_ensureANewDirectSaleIsCreatedAndAdded() {
//        // Arrange
//        List<DirectSale> list = new ArrayList<>();
//        DirectSaleRepository repo = new DirectSaleRepository(list);
//
//        //Act
//        boolean result = repo.createAndAddDirectSale(List.of(validListing), validBuyer, false);
//
//        //Assert
//        assertTrue(result);
//    }
//
//    @Test
//    void createAndAddDirectSale_ensureADuplicateDirectSaleIsNotCreatedAndAdded() {
//        // Arrange
//        List<DirectSale> list = new ArrayList<>();
//        list.add(validDirectSale);
//        DirectSaleRepository repo = new DirectSaleRepository(list);
//
//        //Act
//        boolean result = repo.createAndAddDirectSale(List.of(validListing), validBuyer, false);
//
//        //Assert
//        assertFalse(result);
//    }
//
//    @Test
//    void createAndAddDirectSale_ReturnShouldThrowErrorWhenDuplicate() {
//        DirectSaleRepository repo = new DirectSaleRepository(List.of(validDirectSale));
//
//        assertThrows(IllegalArgumentException.class, () ->
//                repo.createAndAddDirectSaleReturn(List.of(validListing), validBuyer, false));
//    }
//
//    @Test
//    void createAndAddDirectSale_ReturnShouldThrowWhenDuplicate() {
//        // Arrange
//        DirectSaleRepository repo = new DirectSaleRepository(List.of(validDirectSale));
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                repo.createAndAddDirectSaleReturn(List.of(validListing), validBuyer, false)
//        );
//    }
//
//    @Test
//    void createAndAddDirectSale_ReturnShouldReturnCreatedDirectSaleAndAddItToRepository() {
//        // Arrange
//        List<DirectSale> list = new ArrayList<>();
//        DirectSaleRepository repo = new DirectSaleRepository(list);
//
//        // Act
//        DirectSale result = repo.createAndAddDirectSaleReturn(List.of(validListing), validBuyer, false);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(repo.ifDirectSaleExists(result));
//    }


    // TODO - os metodos seguintes devem ser movidos para a classe ListingRepository
    /*
    //Method getListOfItemsOnDirectSaleOfAGivenGenre--------------------------------------------------
    @Test
    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldThrownErrorWhenGenreIsNull() {
        // Arrange
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(null);
        });
        assertEquals("Genre cannot be null", exception.getMessage());
    }

    @Test
    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnEmptyList() {
        // Arrange
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);

        // Act
        List<PublicationSample> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(validGenre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

//    @Test
//    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnEmptyListIfNoMatchingGenre() {
//        // Arrange
//        List<DirectSale> directSales = new ArrayList<>();
//        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
//
//        // Add a DirectSale with "Science" genre
//        Listing listing = createListingWithGenre("Science");
//        directSaleRepository.createAndAddDirectSale(List.of(listing), validBuyer, false);
//
//        Genre genre = new Genre("Fiction");                     // looking for Fiction, but only Science exists
//
//        // Act
//        List<PublicationSample> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());                                     // should return nothing
//    }
//
//    @Test
//    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnListWithOneMatch() {
//        // Arrange
//        List<DirectSale> directSales = new ArrayList<>();
//        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
//
//        Listing listing = createListingWithGenre("Fiction");
//        directSaleRepository.createAndAddDirectSale(List.of(listing), validBuyer, false);
//
//        Genre genre = new Genre("Fiction");
//
//        // Act
//        List<PublicationSample> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnListWithMultipleMatches() {
//        // Arrange
//        List<DirectSale> directSales = new ArrayList<>();
//        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
//
//        // Two different listings both with Fiction items
//        directSaleRepository.createAndAddDirectSale(List.of(createListingWithGenre("Fiction")), validBuyer, false);
//        directSaleRepository.createAndAddDirectSale(List.of(createListingWithGenre("Fiction")), validBuyer, false);
//
//        Genre genre = new Genre("Fiction");
//
//        // Act
//        List<PublicationSample> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//    }
//
//    @Test
//    void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnItemsWithCorrectGenre() {
//        // Arrange
//        List<DirectSale> directSales = new ArrayList<>();
//        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
//        directSaleRepository.createAndAddDirectSale(List.of(validListing), validBuyer, false);
//
//        // Act
//        List<PublicationSample> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(validGenre);
//
//        // Assert
//        assertEquals(2, result.size());
//        assertTrue(result.get(0).hasGenre(validGenre));
//    }
//
//    @Test
//    void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnEmptyListWhenNoItemMatchesGenre() {
//        // Arrange
//        List<DirectSale> directSales = new ArrayList<>();
//        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
//        directSaleRepository.createAndAddDirectSale(List.of(validListing), validBuyer, false);
//
//        Genre genre = new Genre("Suspense");                                // genre doesn't exit
//
//        // Act
//        List<PublicationSample> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);
//
//        // Assert
//        assertTrue(result.isEmpty());
//    }


    //Method getListOfItemsOnDirectSale ----------------------------------------------------------------------------
//    @Test
//    void getListOfItemsOnDirectSale_ensureThatDoNotExposeInternalList() {
//        // Arrange
//        List<DirectSale> original = new ArrayList<>();
//        DirectSaleRepository repo = new DirectSaleRepository(original);
//
//        // Act
//        //List<DirectSale> result = repo.getListOfItemsOnDirectSale(validItem1.getPublication());             // Gets the internal list by a public method
//        List<DirectSale> result = repo.getListOfItemsOnDirectSale(validItem1);             // Gets the internal list by a public method
//        result.add(validDirectSale);                                                                        // Changes the returned list
//
//        //Assert
//        assertFalse(repo.ifDirectSaleExists(validDirectSale));                                              // Ensures the internal list should NOT be changed
//    }
//
//    @Test
//    void getListOfItemsOnDirectSale_shouldReturnAllItemsOnDirectSaleOfAGivenPublication() {
//        // Arrange
//        // Create 2 directSale with 1 item each, referring to the same publication (same BibliographicalData, not PublicationSample)
//        PublicationSample targetPublication = validItem1;
//
//        Listing listing1 = new Listing(validTitle, List.of(validItem1),SaleType.DIRECT_SALE, validPrice,validDescription, validPictures);
//        Listing listing2 = new Listing(validTitle, List.of(validItem1), SaleType.DIRECT_SALE, validPrice, validDescription, validSeller, validPictures);
//
//        DirectSale ds1 = new DirectSale(List.of(listing1), validBuyer, false);
//        DirectSale ds2 = new DirectSale(List.of(listing2), validBuyer, false);
//
//        DirectSaleRepository repo = new DirectSaleRepository(List.of(ds1, ds2));
//
//        // Act
//        List<DirectSale> result = repo.getListOfItemsOnDirectSale(targetPublication);
//
//        // Assert
//        assertEquals(2, result.size());
//        assertTrue(result.contains(ds1));
//        assertTrue(result.contains(ds2));
//    }
//
//    @Test
//    void getListOfItemsOnDirectSale_shouldReturnEmptyListWhenThereIsNotItemsOnDirectSaleForAGivenPublication() {
//        // Arrange
//        // Creates a Publication That Is Not For DirectSale
//        BibliographicalData bd = new BibliographicalData(
//                "Childhood: First Book Of The Copenhagen Trilogy ",
//                List.of(author),
//                Locale.ENGLISH,
//                LocalDate.of(2020, 1, 1),
//                List.of(new Genre("Fiction")),
//                " "
//        );
//        Publication publicationNotForSale = new Publication(bd, publicationType, validSeller, publicationStatus, edition1, code1);
//
//        DirectSaleRepository repo = new DirectSaleRepository(List.of(validDirectSale));
//
//        // Act
//        List<DirectSale> result = repo.getListOfItemsOnDirectSale(publicationNotForSale);
//
//        // Assert
//        assertTrue(result.isEmpty());
//    }

    @Test
    void getListOfItemsOnDirectSale_shouldReturnsAllItemsOnDirectSaleForAGivenAuthor() {
        // Arrange
        // Create 2 listings with publications from the same author
        Listing listing4 = new Listing(validTitle, List.of(validItem4), SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        DirectSale ds1 = new DirectSale(List.of(validListing), validBuyer, false);
        DirectSale ds2 = new DirectSale(List.of(listing4), validBuyer, false);

        DirectSaleRepository repo = new DirectSaleRepository(List.of(ds1, ds2));

        // Act
        List<DirectSale> result = repo.getListOfItemsOnDirectSale(author);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(ds1));
        assertTrue(result.contains(ds2));
    }

    @Test
    void getListOfItemsOnDirectSale_shouldReturnsEmptyListWhenThereIsNotItemsOnDirectSaleForAGivenAuthor() {
        // Arrange
        Author unrelatedAuthor = new Author(new Profile("John Snow", new Country("United_Kingdom"), LocalDate.of(1500, 1, 1)), "Bio");
        DirectSaleRepository repo = new DirectSaleRepository(List.of(validDirectSale));

        // Act
        List<DirectSale> result = repo.getListOfItemsOnDirectSale(unrelatedAuthor);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getListOfItemsOnDirectSale_shouldReturnsAllItemsOnDirectSaleForAGivenPublisher() {
        // Arrange
        // Create 2 listings and directSales with the same publisher
        Listing listing3 = new Listing(validTitle, List.of(validItem3),SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

        DirectSale ds1 = new DirectSale(List.of(validListing), validBuyer, false);
        DirectSale ds2 = new DirectSale(List.of(listing3), validBuyer, false);

        DirectSaleRepository repo = new DirectSaleRepository(List.of(ds1, ds2));

        // Act
        List<DirectSale> result = repo.getListOfItemsOnDirectSale(publisher);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(ds1));
        assertTrue(result.contains(ds2));
    }

    @Test
    void getListOfItemsOnDirectSale_shouldReturnEmptyListWhenThereIsNotItemsOnDirectSaleForAGivenPublisher() {
        // Arrange
        Publisher unrelatedPublisher = new Publisher("Ghost Publisher Company, Lda.", "PT000000000", null);

        DirectSaleRepository repo = new DirectSaleRepository(List.of(validDirectSale));

        // Act
        List<DirectSale> result = repo.getListOfItemsOnDirectSale(unrelatedPublisher);

        // Assert
        assertTrue(result.isEmpty());
    }

    //One DS with more than 1 listing referring to the same publication, only returns 1
//    @Test
//    void getListOfItemsOnDirectSale_doesNotDuplicateDirectSale() {
//        // Arrange
//        Publication targetPublication = validItem1.getPublication();
//
//        Listing listing1 = new Listing( validTitle, List.of(validItem1, validItem2), SaleType.DIRECT_SALE, validPrice, validDescription, validSeller, validPictures);
//
//        DirectSale ds = new DirectSale(List.of(listing1), validBuyer, false);
//
//        DirectSaleRepository repo = new DirectSaleRepository(List.of(ds));
//
//        // Act
//        List<DirectSale> result = repo.getListOfItemsOnDirectSale(targetPublication);
//
//        // Assert
//        assertEquals(1, result.size());
//        assertSame(ds, result.get(0));
//    }
//
//    Verifies if publication is not null
//    @Test
//    void getListOfItemsOnDirectSale_shouldThrowErrorWhenPublicationIsNull() {
//        // Arrange
//        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of(validDirectSale));
//
//        // Act
//        IllegalArgumentException e = assertThrows(
//                IllegalArgumentException.class,
//                () -> dsRepo.getListOfItemsOnDirectSale((Publication) null)     // Publication cast because there's also this method signature for Author
//        );
//
//        // Assert
//        assertEquals("Publication cannot be null", e.getMessage());
//    }
//
//
//    @Test
//    void getListOfItemsOnDirectSale_shouldNotDuplicateWhenMultipleListingsMatch() {
//        Publication target = validItem1.getPublication();
//
//        Listing listingA = new Listing(validTitle, List.of(validItem1), SaleType.DIRECT_SALE, validPrice, validDescription, validSeller, validPictures);
//        Listing listingB = new Listing(validTitle, List.of(validItem1), SaleType.DIRECT_SALE,  validPrice, validDescription, validSeller, validPictures);
//
//        DirectSale ds = new DirectSale(List.of(listingA, listingB), validBuyer, false);
//
//        DirectSaleRepository repo = new DirectSaleRepository(List.of(ds));
//
//        List<DirectSale> result = repo.getListOfItemsOnDirectSale(target);
//
//        assertEquals(1, result.size());
//    }

     */
}