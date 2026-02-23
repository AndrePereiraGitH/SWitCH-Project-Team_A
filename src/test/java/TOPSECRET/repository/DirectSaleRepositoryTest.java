package TOPSECRET.repository;

import TOPSECRET.domain.*;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


class DirectSaleRepositoryTest {


    // Dimensions
    Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);

    // User
    LocalDate validBirthday = LocalDate.of(2000, 1, 1);
    Profile validProfile = new Profile("John",new Country("PORTUGAL"),validBirthday);
    Email validEmail = new Email("john.doe", "example.com");
    Address validAddress = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("PORTUGAL"));
    Contact validContact = new Contact("123456789","+351",validEmail,validAddress);
    User validSeller = new User("Seller1",validContact,validProfile, UserRole.NORMAL_USER);
    Genre validGenre = new Genre("Fiction");


    // BibliographicalData
    Profile profile = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
    Author author = new Author(profile, "valio bio");
    Address address = new Address("R Maria", "12", "156-5", new Country("PARAGUAY"));
    Email email = new Email("Maria1", "gmail.com");
    Contact contact = new Contact("915945695", "+351", email, address);
    Publisher publisher = new Publisher("Some Publisher", "PT123456789", contact);
    BibliographicalData bibliographicalData = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author),
            Locale.ENGLISH,
            LocalDate.of(2020, 1, 1),
            List.of(validGenre),
            "Some text"
    );


    PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback"
    );

    // PublicationStatus
    PublicationStatus publicationStatus = PublicationStatus.READING;
    User user = new User("maria1", contact, profile, UserRole.NORMAL_USER);
    // Edition
    Code code = Code.fromIsbn13("9783161484121");
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");

    Edition edition1 = new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition2 = new Edition("2st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition3 = new Edition("3st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition4 = new Edition("4st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    // Code
    Code code1 = Code.fromIsbn13("9781234567890");
    Code code2 = Code.fromIsbn13("9781234567891");
    Code code3 = Code.fromIsbn13("9781234567892");

    // Items (create 3 items, referring to the same bibliographicalData, but different editions)
    PublicationSample validItem1 = new PublicationSample(bibliographicalData, validSeller, publicationStatus, edition1, Condition.LIKE_NEW);
    PublicationSample validItem2 = new PublicationSample(bibliographicalData, validSeller, publicationStatus, edition2, Condition.LIKE_NEW);
    PublicationSample validItem3 = new PublicationSample(bibliographicalData, validSeller, publicationStatus, edition3, Condition.LIKE_NEW);
    PublicationSample validItem4 = new PublicationSample(bibliographicalData, validSeller, publicationStatus, edition4, Condition.LIKE_NEW);

    private final List<PublicationSample> severalItemList = new ArrayList<>(List.of(validItem1, validItem2));

    // Listing
    Price validPrice = new Price(BigDecimal.valueOf(50.99));
    private final String validDescription = "Valid Description";
    private final String validTitle = "PS5 Pro";
    List<Picture> validPictures = List.of(new Picture("path/to/pic.jpg"));

    Listing validListing = new Listing(validTitle, severalItemList, SaleType.DIRECT_SALE, validPrice, validDescription, validPictures);

    // DirectSale
    // Buyer
    LocalDate validBirthday_buyer = LocalDate.of(1999, 1, 1);
    Profile validProfile_buyer = new Profile("Ross", new Country("PORTUGAL"), validBirthday);
    Email validEmail_buyer = new Email("ross.doe", "example.com");
    Address validAddress_buyer = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("PORTUGAL"));
    Contact validContact_buyer = new Contact("123456789", "+351", validEmail, validAddress);
    User validBuyer = new User("Ross Geller", validContact, validProfile, UserRole.NORMAL_USER);

    DirectSale validDirectSale = new DirectSale(List.of(validListing), validBuyer, false);


    // Private Methods for Tests With Genre
    private BibliographicalData createBibliographicalDataWithGenre(String genreName) {
        return new BibliographicalData(
                "Different Title",
                bibliographicalData.getAuthors(),
                bibliographicalData.getOriginalLanguage(),
                bibliographicalData.getPublishDate(),
                List.of(new Genre(genreName)),
                bibliographicalData.getChunksOfText()
        );
    }

    private PublicationSample createItemWithGenre(String genreName) {
        PublicationSample pub = new PublicationSample(
                createBibliographicalDataWithGenre(genreName),
                validSeller,
                publicationStatus,
                edition1,
                Condition.POOR
        );
        return pub;
    }


    private Listing createListingWithGenre(String genreName) {
        return new Listing(
                validTitle,
                List.of(createItemWithGenre(genreName)),
                SaleType.DIRECT_SALE,
                validPrice,
                validDescription,
                validPictures
        );
    }

    //============================================================================================================
    // TESTS

    // Method createAndAddDirectSale ----------------------------------------------------------------------------
    @Test
    void createAndAddDirectSale_ensureANewDirectSaleIsCreatedAndAdded() {
        // Arrange
        List<DirectSale> list = new ArrayList<>();
        DirectSaleRepository repo = new DirectSaleRepository(list);

        //Act
        boolean result = repo.createAndAddDirectSale(List.of(validListing), validBuyer, false);

        //Assert
        assertTrue(result);
    }

    @Test
    void createAndAddDirectSale_ensureADuplicateDirectSaleIsNotCreatedAndAdded() {
        // Arrange
        List<DirectSale> list = new ArrayList<>();
        list.add(validDirectSale);
        DirectSaleRepository repo = new DirectSaleRepository(list);

        //Act
        boolean result = repo.createAndAddDirectSale(List.of(validListing), validBuyer, false);

        //Assert
        assertFalse(result);
    }

    @Test
    void createAndAddDirectSale_ReturnShouldThrowErrorWhenDuplicate() {
        DirectSaleRepository repo = new DirectSaleRepository(List.of(validDirectSale));

        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAddDirectSaleReturn(List.of(validListing), validBuyer, false));
    }

    @Test
    void createAndAddDirectSale_ReturnShouldThrowWhenDuplicate() {
        // Arrange
        DirectSaleRepository repo = new DirectSaleRepository(List.of(validDirectSale));

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAddDirectSaleReturn(List.of(validListing), validBuyer, false)
        );
    }

    @Test
    void createAndAddDirectSale_ReturnShouldReturnCreatedDirectSaleAndAddItToRepository() {
        // Arrange
        List<DirectSale> list = new ArrayList<>();
        DirectSaleRepository repo = new DirectSaleRepository(list);

        // Act
        DirectSale result = repo.createAndAddDirectSaleReturn(List.of(validListing), validBuyer, false);

        // Assert
        assertNotNull(result);
        assertTrue(repo.ifDirectSaleExists(result));
    }


    //Method ifDirectSaleExists ----------------------------------------------------------------------------
    @Test
    void ifDirectSaleExists_ensureIffDirectSaleExistsReturnsTrue() {
        // Arrange
        List<DirectSale> list = new ArrayList<>();
        list.add(validDirectSale);
        DirectSaleRepository repo = new DirectSaleRepository(list);

        //Act
        boolean result = repo.ifDirectSaleExists(validDirectSale);

        //Assert
        assertTrue(result);
    }

    @Test
    void ifDirectSaleExists_ensureIfDirectSaleDoesNotExistReturnsFalse() {
        // Arrange
        List<DirectSale> list = new ArrayList<>();
        DirectSaleRepository repo = new DirectSaleRepository(list);

        //Act
        boolean result = repo.ifDirectSaleExists(validDirectSale);

        //Assert
        assertFalse(result);

    }

    @Test
    void ifDirectSaleExists_ensureConstructorCopiesListNotOriginalReferences() {
        // Arrange
        List<DirectSale> list = new ArrayList<>();
        DirectSaleRepository repo = new DirectSaleRepository(list);
        list.add(validDirectSale);

        // Act
        boolean expectedResult = repo.ifDirectSaleExists(validDirectSale);

        //Assert
        assertFalse(expectedResult);
    }

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

    @Test
    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnEmptyListIfNoMatchingGenre() {
        // Arrange
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);

        // Add a DirectSale with "Science" genre
        Listing listing = createListingWithGenre("Science");
        directSaleRepository.createAndAddDirectSale(List.of(listing), validBuyer, false);

        Genre genre = new Genre("Fiction");                     // looking for Fiction, but only Science exists

        // Act
        List<PublicationSample> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());                                     // should return nothing
    }

    @Test
    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnListWithOneMatch() {
        // Arrange
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);

        Listing listing = createListingWithGenre("Fiction");
        directSaleRepository.createAndAddDirectSale(List.of(listing), validBuyer, false);

        Genre genre = new Genre("Fiction");

        // Act
        List<PublicationSample> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnListWithMultipleMatches() {
        // Arrange
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);

        // Two different listings both with Fiction items
        directSaleRepository.createAndAddDirectSale(List.of(createListingWithGenre("Fiction")), validBuyer, false);
        directSaleRepository.createAndAddDirectSale(List.of(createListingWithGenre("Fiction")), validBuyer, false);

        Genre genre = new Genre("Fiction");

        // Act
        List<PublicationSample> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnItemsWithCorrectGenre() {
        // Arrange
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
        directSaleRepository.createAndAddDirectSale(List.of(validListing), validBuyer, false);

        // Act
        List<PublicationSample> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(validGenre);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).hasGenre(validGenre));
    }

    @Test
    void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnEmptyListWhenNoItemMatchesGenre() {
        // Arrange
        List<DirectSale> directSales = new ArrayList<>();
        DirectSaleRepository directSaleRepository = new DirectSaleRepository(directSales);
        directSaleRepository.createAndAddDirectSale(List.of(validListing), validBuyer, false);

        Genre genre = new Genre("Suspense");                                // genre doesn't exit

        // Act
        List<PublicationSample> result = directSaleRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertTrue(result.isEmpty());
    }


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

    //Verifies if publication is not null
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
}