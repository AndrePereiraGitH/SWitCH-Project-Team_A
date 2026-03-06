//package TOPSECRET.controller;
//
//import TOPSECRET.domain.*;
//import TOPSECRET.domain.Enum.*;
//import TOPSECRET.dto.CollectionOfPublicationsDTO_US018;
//import TOPSECRET.dto.PublicationSampleDTO_US018;
//import TOPSECRET.repository.CollectionOfPublicationsRepository;
//import TOPSECRET.repository.LibraryRepository;
//import TOPSECRET.repository.PublicationSampleRepository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import javax.management.InstanceAlreadyExistsException;
//import javax.management.InstanceNotFoundException;
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for US018ToAddPublicationToAListOfMineController.
// * <p>
// * Coverage goals:
// * </p>
// * <ul>
// *   <li>All public controller methods are covered.</li>
// *   <li>Both success and failure scenarios are tested for mutation resistance.</li>
// *   <li>AAA (Arrange-Act-Assert) pattern is followed in each test.</li>
// * </ul>
// */
//class US018ToAddPublicationToAListOfMineControllerDependenciesTest {
//
//    // =====================================================
//    // HELPERS (setup only)
//    // =====================================================
//
//    private void createAndRegisterLibraryForUser(String libraryName, User owner) throws InstanceAlreadyExistsException {
//        libraryRepo.createAndAdd(libraryName, owner);
//    }
//
//    private CollectionOfPublications createAndRegisterCollectionForUser(String name, String genre, User owner) {
//        CollectionOfPublications collection = new CollectionOfPublications(name, new Genre(genre), owner);
//        boolean added = collectionRepo.addCollectionToRepo(collection);
//        assertTrue(added); // ensures test environment is valid
//        return collection;
//    }
//
//    private PublicationSample addOnePublicationSampleToUserLibrary(User owner, String title, String editionNumber)
//            throws InstanceNotFoundException {
//
//        Library userLibrary = libraryRepo.getLibraryOfUser(owner.getUserName());
//
//        PublicationSample sample = createValidPublicationSample(owner, title, editionNumber);
//        userLibrary.addPublication(sample);
//
//        return sample;
//    }
//
//    private User createValidUser() {
//        Profile profile = new Profile(
//                "Test User",
//                new Country("PORTUGAL"),
//                LocalDate.of(1990, 1, 1)
//        );
//
//        return new User(
//                "validUser",
//                new Contact("912345678", "351", null, null),
//                profile,
//                UserRole.ADMIN
//        );
//    }
//
//    private PublicationSample createValidPublicationSample(User owner, String title, String editionNumber) {
//
//        Profile authorProfile = new Profile(
//                "Author Name",
//                new Country("PORTUGAL"),
//                LocalDate.of(1980, 1, 1)
//        );
//
//        Author author = authorFactory.createWithExistingProfile(authorProfile, "Short biography");
//
//        BibliographicalData bd = new BibliographicalData(
//                title,
//                List.of(author),
//                Locale.ENGLISH,
//                LocalDate.of(2020, 1, 1),
//                List.of(new Genre("Fiction")),
//                "Sample text"
//        );
//
//        Code code = Code.fromIsbn13("9783161484100");
//        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
//        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
//
//        PublicationType pubType = new PublicationType(
//                MediaFormat.BOOK,
//                TypeOfBinding.HARDBACK,
//                "Standard"
//        );
//
//        Publisher publisher = new Publisher("Test Publisher", "PT123456789", null);
//
//        Edition edition = editionFactory.create(
//                editionNumber,
//                code,
//                physicalAttributes,
//                pubType,
//                publisher,
//                Locale.ENGLISH,
//                LocalDate.of(2020, 1, 1)
//        );
//
//        return new PublicationSample(
//                bd,
//                owner,
//                PublicationStatus.READING,
//                edition,
//                Condition.NEW
//        );
//    }
//
//    private US018ToAddPublicationToAListOfMineController controller;
//    private CollectionOfPublicationsRepository collectionRepo;
//    private LibraryRepository libraryRepo;
//    private PublicationSampleRepository publicationSampleRepo;
//    private final AuthorFactory authorFactory = new AuthorFactory();
//    private final EditionFactory editionFactory = new EditionFactory();
//    private User user;
//
//    @BeforeEach
//    void setup() throws Exception {
//        // ARRANGE
//        collectionRepo = new CollectionOfPublicationsRepository();
//        publicationSampleRepo = new PublicationSampleRepository();
//
//        // LibraryRepository requires (Map, Factory)
//        Map<String, Library> libraries = new HashMap<>();
//        LibraryFactory libraryFactory = new LibraryFactory();
//        libraryRepo = new LibraryRepository(libraries, libraryFactory);
//
//        user = createValidUser();
//        createAndRegisterLibraryForUser("UserLibrary", user);
//
//        controller = new US018ToAddPublicationToAListOfMineController(
//                user,
//                collectionRepo,
//                libraryRepo,
//                publicationSampleRepo
//        );
//    }
//
//    // =====================================================
//    // getUserCollections(User user)
//    // =====================================================
//
//    @Test
//    void shouldReturnUserCollections() {
//        // ARRANGE
//        createAndRegisterCollectionForUser("Favorites", "Fiction", user);
//
//        // ACT
//        List<CollectionOfPublicationsDTO_US018> result = controller.getUserCollections(user);
//
//        // ASSERT
//        assertEquals(1, result.size());
//        assertEquals("Favorites", result.get(0).getName());
//        assertEquals("Fiction", result.get(0).getGenreName());
//        assertEquals(user.getUserName(), result.get(0).getOwnerName());
//    }
//
//    @Test
//    void shouldThrowWhenGetUserCollectionsUserIsNull() {
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class, () -> controller.getUserCollections(null));
//    }
//
//    // =====================================================
//    // getPublicationsForCollection(CollectionDTO)
//    // =====================================================
//
//    @Test
//    void shouldReturnAvailablePublicationsWhenCollectionIsEmpty() throws Exception {
//        // ARRANGE
//        addOnePublicationSampleToUserLibrary(user, "Test Title", "1st");
//        createAndRegisterCollectionForUser("Favorites", "Fiction", user);
//
//        CollectionOfPublicationsDTO_US018 collectionDTO = controller.getUserCollections(user).get(0);
//
//        // ACT
//        List<PublicationSampleDTO_US018> available = controller.getPublicationsForCollection(collectionDTO);
//
//        // ASSERT
//        assertEquals(1, available.size());
//        assertEquals("Test Title", available.get(0).getTitle());
//    }
//
//    @Test
//    void shouldReturnNoAvailablePublicationsWhenAlreadyInCollection() throws Exception {
//        // ARRANGE
//        PublicationSample sample = addOnePublicationSampleToUserLibrary(user, "Test Title", "1st");
//        CollectionOfPublications collection = createAndRegisterCollectionForUser("Favorites", "Fiction", user);
//
//        Library userLibrary = libraryRepo.getLibraryOfUser(user.getUserName());
//        boolean added = collectionRepo.addPublicationToCollection(user, userLibrary, collection, sample);
//        assertTrue(added);
//
//        CollectionOfPublicationsDTO_US018 collectionDTO = controller.getUserCollections(user).get(0);
//
//        // ACT
//        List<PublicationSampleDTO_US018> available = controller.getPublicationsForCollection(collectionDTO);
//
//        // ASSERT
//        assertTrue(available.isEmpty());
//    }
//
//    @Test
//    void shouldThrowWhenGetPublicationsForCollectionDTOIsNull() {
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class, () -> controller.getPublicationsForCollection(null));
//    }
//
//
//
//    // =====================================================
//    // addPublicationToCollection(CollectionDTO, List<PublicationDTO>)
//    // =====================================================
//
//    @Test
//    void shouldAddPublicationToCollection() throws Exception {
//        // ARRANGE
//        addOnePublicationSampleToUserLibrary(user, "Test Title", "1st");
//        createAndRegisterCollectionForUser("Favorites", "Fiction", user);
//
//        CollectionOfPublicationsDTO_US018 collectionDTO = controller.getUserCollections(user).get(0);
//        PublicationSampleDTO_US018 pubDTO = controller.getPublicationsForCollection(collectionDTO).get(0);
//
//        // ACT
//        boolean result = controller.addPublicationToCollection(collectionDTO, List.of(pubDTO));
//
//        // ASSERT
//        assertTrue(result);
//
//        List<PublicationSampleDTO_US018> availableAfter = controller.getPublicationsForCollection(collectionDTO);
//        assertTrue(availableAfter.isEmpty());
//    }
//
//    @Test
//    void shouldReturnFalseWhenAddingDuplicatePublication() throws Exception {
//        // ARRANGE
//        addOnePublicationSampleToUserLibrary(user, "Test Title", "1st");
//        createAndRegisterCollectionForUser("Favorites", "Fiction", user);
//
//        CollectionOfPublicationsDTO_US018 collectionDTO = controller.getUserCollections(user).get(0);
//        PublicationSampleDTO_US018 pubDTO = controller.getPublicationsForCollection(collectionDTO).get(0);
//
//        boolean first = controller.addPublicationToCollection(collectionDTO, List.of(pubDTO));
//        assertTrue(first);
//
//        // ACT
//        boolean second = controller.addPublicationToCollection(collectionDTO, List.of(pubDTO));
//
//        // ASSERT
//        assertFalse(second);
//    }
//
//    @Test
//    void shouldThrowWhenAddPublicationCollectionDTOIsNull() {
//        // ARRANGE
//        PublicationSampleDTO_US018 pubDTO = new PublicationSampleDTO_US018("Any", "1st", "validUser");
//
//        // ACT + ASSER
//        assertThrows(IllegalArgumentException.class,
//                () -> controller.addPublicationToCollection(null, List.of(pubDTO)));
//    }
//
//    @Test
//    void shouldThrowWhenAddPublicationListIsNull() {
//        // ARRANGE
//        CollectionOfPublicationsDTO_US018 collectionDTO =
//                new CollectionOfPublicationsDTO_US018("Favorites", "Fiction", "validUser");
//
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class,
//                () -> controller.addPublicationToCollection(collectionDTO, null));
//    }
//
//    @Test
//    void shouldThrowWhenAddPublicationListIsEmpty() {
//        // ARRANGE
//        CollectionOfPublicationsDTO_US018 collectionDTO =
//                new CollectionOfPublicationsDTO_US018("Favorites", "Fiction", "validUser");
//
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class,
//                () -> controller.addPublicationToCollection(collectionDTO, List.of()));
//    }
//}