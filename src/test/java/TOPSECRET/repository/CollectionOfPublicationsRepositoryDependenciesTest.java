//package TOPSECRET.repository;
//
//import TOPSECRET.controller.US019ToPubliclyShareCollectionOfPublicationsController;
//import TOPSECRET.domain.*;
//import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
//import TOPSECRET.domain.Enum.*;
//import TOPSECRET.dto.CollectionOfPublicationsDTO;
//import TOPSECRET.dto.CollectionOfPublicationsDTO_US018;
//import TOPSECRET.dto.CollectionOfPublicationsDTO_US019;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import javax.management.InstanceNotFoundException;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Locale;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Integration tests for {@link CollectionOfPublicationsRepository}.
// *
// * <p>These tests use <b>real objects</b> — no mocks. A real repository, a real factory,
// * and real domain objects (User, Genre, CollectionOfPublications) are all created together.
// * If a test fails here, the bug could be in the repository or in the domain class it uses.
// *
// * <p>{@code @BeforeEach} creates a fresh repository before every test,
// * so tests do not share state and cannot affect each other.
// *
// * <p>This file covers four user stories:
// * <ul>
// *   <li><b>US017</b> — create a private collection, prevent duplicates, confirm empty start</li>
// *   <li><b>US018</b> — add publications to collections, get user collections, convert DTOs</li>
// *   <li><b>US019</b> — make a collection public, find collections by user</li>
// *   <li><b>US020</b> — find public collections by genre</li>
// * </ul>
// */
//class CollectionOfPublicationsRepositoryDependenciesTest {
//
//    private CollectionOfPublicationsRepository repository;
//
//    // runs before each test — creates a fresh empty repository so tests don't affect each other
//    @BeforeEach
//    void setup() {
//        repository = new CollectionOfPublicationsRepository(new CollectionOfPublicationsFactory());
//    }
//
//
//    // ========================= Test helpers =========================
//
//    /**
//     * Creates a {@link Country} using the factory — used across multiple tests to avoid repetition.
//     */
//    private Country createCountry() {
//        CountryFactory factory = new CountryFactory();
//        return factory.create("Portugal");
//    }
//
//    /**
//     * Creates a valid {@link PublicationSample} — used in US018 and US020 tests.
//     */
//    private PublicationSample createValidPublication() {
//
//        // CHANGE: Replaced new Country("PORTUGAL") with createPortugal()
//        Profile profile = new Profile(
//                "Author Name",
//                createCountry(),         // <- BEFORE: new Country("PORTUGAL")
//                LocalDate.of(1980, 1, 1)
//        );
//        AuthorFactory authorFactory = new AuthorFactory();
//        Author author = authorFactory.createWithExistingProfile(
//                profile,
//                "Short biography"
//        );
//
//        List<Author> authors = List.of(author);
//
// //       Publisher publisher = new Publisher(
// //               "Test Publisher",
// //               new Contact("912345678", "351", null, null)
// //       );
//
//        BibliographicalData bibliographicalData = new BibliographicalData(
//                "Test Title",
//                authors,
//                Locale.ENGLISH,
// //               publisher,
//                LocalDate.of(2020, 1, 1),
//                List.of(new Genre("Fiction")),
// //               200,
//                "Sample text"
//        );
//
//        PublicationType publicationType = new PublicationType(
//                MediaFormat.BOOK,
//                TypeOfBinding.HARDBACK,
//                "Standard publication"
//        );
//
//        // CHANGE: Replaced new Country("PORTUGAL") with createPortugal()
//        Profile profileUser = new Profile(
//                "User Name",
//                createCountry(),        // <- BEFORE: new Country("PORTUGAL")
//                LocalDate.of(1980, 1, 1)
//        );
//
//        User user = new User(
//                "validUser",
//                new Contact("911111111", "351", null, null),
//                profileUser,
//                UserRole.ADMIN
//        );
//
//        // Edition
//        Code code = Code.fromIsbn13 ("9783161484120");
//        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
//        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
//        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
//        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
//
//        EditionFactory editionFactory = new EditionFactory();
//        Edition edition= editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
//
//        return new PublicationSample(
//                bibliographicalData,
//                user,
//                PublicationStatus.READING,
////                publicationType,
// //               "First Edition",
//                edition,
//                Condition.NEW
////                Code.fromIsbn13("9783161484100")
//        );
//    }
//
//    /**
//     * Creates a valid {@link User} — used across US017, US018, US019, and US020 tests.
//     */
//    private User createValidUser() {
//
//        // CHANGE: Replaced new Country("PORTUGAL") with createPortugal()
//        Profile profile = new Profile(
//                "Test User",
//                createCountry(),            // <- BEFORE: new Country("PORTUGAL")
//                LocalDate.of(1990, 1, 1)
//        );
//
//        return new User(
//                "validUser",
//                new Contact("912222222", "351", null, null),
//                profile,
//                UserRole.NORMAL_USER
//        );
//    }
//
//
//    // ============================== US017 ==============================
//
//    // US017 happy path: the user provides a name and genre, and the repository creates the collection.
//    // We check all five expected values to confirm the repository delegated to the factory correctly:
//    // - name and genre must match what was passed in
//    // - owner must be the correct user
//    // - isPrivate must be true (US017 rule: always starts private)
//    // - publications must be empty (US017 rule: starts with no publications)
//    @Test
//    void shouldCreatePrivateCollectionSuccessfully_US017() {
//        // Arrange
//        User user = createValidUser();
//        Genre genre = new Genre("Fiction");
//
//        // Act — repository asks the factory to create the collection
//        CollectionOfPublications result =
//                repository.createIfNotExists(user, "Favorites", genre);
//
//        // Assert — collection is created, private by default, and starts empty
//        assertNotNull(result);
//        assertEquals("Favorites", result.getCollectionName());
//        assertEquals(genre, result.getCollectionGenre());
//        assertTrue(result.getIsPrivate());
//        assertTrue(result.getPublications().isEmpty());
//    }
//
//    // US017 rule: a user cannot have two collections with the same name and genre.
//    // The first creation succeeds. The second one has the exact same data, so it must throw.
//    // The repository checks for this duplicate before calling the factory.
//    @Test
//    void shouldNotAllowDuplicateCollectionForSameUser_US017() {
//        // Arrange — first collection is created successfully
//        User user = createValidUser();
//        Genre genre = new Genre("Fiction");
//        repository.createIfNotExists(user, "Favorites", genre);
//
//        // Act & Assert — same user + same name + same genre = duplicate = must throw
//        assertThrows(IllegalArgumentException.class, () ->
//                repository.createIfNotExists(user, "Favorites", genre)
//        );
//    }
//
//    // The duplicate check uses both name AND genre — changing the genre makes it a different collection.
//    // This test confirms that the same name with a different genre is NOT treated as a duplicate.
//    @Test
//    void shouldAllowSameNameWithDifferentGenre_US017() {
//        // Arrange — first collection with genre "Fiction"
//        User user = createValidUser();
//        repository.createIfNotExists(user, "Favorites", new Genre("Fiction"));
//
//        // Act — same name but different genre ("History") — this must succeed
//        CollectionOfPublications result =
//                repository.createIfNotExists(user, "Favorites", new Genre("History"));
//
//        // Assert — a second collection was created successfully
//        assertNotNull(result);
//    }
//
//    // US017 rule: a new collection must always start with no publications.
//    // getPublications() returns a copy of the internal list — if it is empty, the collection started correctly.
//    @Test
//    void shouldUseGetPublications() {
//        // Arrange & Act
//        User user = createValidUser();
//        Genre genre = new Genre("Fiction");
//
//        CollectionOfPublications collection =
//                repository.createIfNotExists(user, "My Collection", genre);
//
//        // Assert — the list must be empty right after creation
//        assertTrue(collection.getPublications().isEmpty());
//    }
//
//    // ============================== end US017 ==============================
//
//
//    // ============================== US019 ==============================
//
//    /**
//     * US019 - tests for getAllCollectionsOfPublicationsByUser
//     */
//
//    @Test
//    void getAllCollectionOfPublicationsByUser_shouldReturnEmptyList_ifUserDoesNotOwnsAnyCollection() {
//
//        List<CollectionOfPublications> result = repository.getAllCollectionOfPublicationsByUser(createValidUser());
//
//        assertEquals(0, result.size());
//
//    }
//
//    @Test
//    void getAllCollectionOfPublicationsByUser_shouldReturn_ListOfCollectionOfPublications_IfUserOwnsACollection() {
//
//        // arrange
//
//        User owner = createValidUser();
//
//        CollectionOfPublications collection = repository.createIfNotExists(
//                owner,
//                "My Funny Collection",
//                new Genre("Fiction")
//        );
//
//        repository.addCollectionToRepo(collection);
//
//        // act
//        List<CollectionOfPublications> result = repository.getAllCollectionOfPublicationsByUser(owner);
//
//        // assert
//        assertEquals(1, result.size());
//
//    }
//
//    @Test
//    void getAllCollectionOfPublicationsByUser_shouldThrow_IfUIserIsNull() {
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repository.getAllCollectionOfPublicationsByUser(null));
//
//        assertEquals("User cannot be null", exception.getMessage());
//
//    }
//
//    @Test
//    void getCollectionByDTO_shouldReturn_collectionOfPublicationsThatMatchesDTO() throws InstanceNotFoundException {
//        // arrange
//        US019ToPubliclyShareCollectionOfPublicationsController US019ctrl = new US019ToPubliclyShareCollectionOfPublicationsController(repository);
//
//        User owner = createValidUser();
//        Genre genre = new Genre("Fiction");
//        CollectionOfPublications collection = repository.createIfNotExists(
//                owner,
//                "My Funny Collection",
//                genre
//        );
//        repository.addCollectionToRepo(collection);
//
//        List<CollectionOfPublicationsDTO_US019> listDTO = US019ctrl.requestAllCollectionsOfPublicationsByUser(owner);
//
//        // act
//        CollectionOfPublications result = repository.getCollectionByDTO(listDTO.get(0));
//
//        // assert
//        assertEquals(collection, result);
//    }
//
//    @Test
//    void getCollectionDTO_shouldThrows_ifCollectionDTO_doesNotMatchesAnyCollection() {
//
//        // arrange
//        User owner = createValidUser();
//        CollectionOfPublications collection = repository.createIfNotExists(
//                owner,
//                "My Funny Collection",
//                new Genre("Fiction")
//        );
//        repository.addCollectionToRepo(collection);
//
//        CollectionOfPublicationsDTO_US019 fakeDTO = new CollectionOfPublicationsDTO_US019(
//                "Romance",
//                "Romance",
//                "Joana"
//        );
//
//        // act + assert
//
//        InstanceNotFoundException exception = assertThrows(InstanceNotFoundException.class, () -> repository.getCollectionByDTO(fakeDTO));
//
//        assertEquals("selected collection, does not match any CollectionOfPublications", exception.getMessage());
//
//    }
//
//    /**
//     * Ensures that making a collection public returns {@code true} when a valid
//     * {@link CollectionOfPublicationsDTO_US019} identifies an existing collection in the repository.
//     *
//     * @throws InstanceNotFoundException if the DTO does not match any existing collection (not expected in this test)
//     */
//    @Test
//    void makeCollectionOfPublicationsPublic_shouldReturnTRUE_() throws InstanceNotFoundException {
//        // arrange
//        US019ToPubliclyShareCollectionOfPublicationsController US019ctrl = new US019ToPubliclyShareCollectionOfPublicationsController(repository);
//
//        User owner = createValidUser();
//        Genre genre = new Genre("Fiction");
//        CollectionOfPublications collection = repository.createIfNotExists(
//                owner,
//                "My Funny Collection",
//                genre
//        );
//        repository.addCollectionToRepo(collection);
//
//        List<CollectionOfPublicationsDTO_US019> listDTO = US019ctrl.requestAllCollectionsOfPublicationsByUser(owner);
//
//        // act
//        boolean result = repository.makeCollectionOfPublicationsPublic(listDTO.get(0));
//
//        // assert
//
//        assertTrue(result);
//
//    }
//
//    /**
//     * Ensures that {@link CollectionOfPublicationsRepository#makeCollectionOfPublicationsPublic(CollectionOfPublicationsDTO_US019)}
//     * rejects a {@code null} DTO by throwing {@link IllegalArgumentException} with the expected message.
//     */
//    @Test
//    void makeCollectionOfPublicationsPublic_shouldThrows_ifCollectionDTOisNull() {
//
//        // act + assert
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repository.makeCollectionOfPublicationsPublic(null));
//
//        assertEquals("collection dto cannot be null", exception.getMessage());
//
//    }
//
//    /**
//     * Ensures that making a collection public updates the entity state by setting its privacy flag
//     * to {@code false} (i.e., the collection becomes public).
//     * <p>
//     * This test is important for mutation testing because it verifies the side-effect of the method.
//     * Without it, mutations that remove or invert {@code coll.set_isPrivate(false)} may survive.
//     * </p>
//     *
//     * @throws InstanceNotFoundException if the DTO does not match any existing collection (not expected in this test)
//     */
//    @Test
//    void makeCollectionOfPublicationsPublic_shouldSetCollectionAsPublic() throws InstanceNotFoundException {
//        // Arrange
//        US019ToPubliclyShareCollectionOfPublicationsController US019ctrl =
//                new US019ToPubliclyShareCollectionOfPublicationsController(repository);
//
//        User owner = createValidUser();
//        Genre genre = new Genre("Fiction");
//        CollectionOfPublications collection = repository.createIfNotExists(
//                owner,
//                "My Funny Collection",
//                genre
//        );
//
//        // Ensure the collection starts as private
//        assertTrue(collection.getIsPrivate());
//
//        repository.addCollectionToRepo(collection);
//
//        List<CollectionOfPublicationsDTO_US019> listDTO =
//                US019ctrl.requestAllCollectionsOfPublicationsByUser(owner);
//
//        // Act
//        boolean result = repository.makeCollectionOfPublicationsPublic(listDTO.get(0));
//
//        // Assert
//        assertTrue(result);
//        assertFalse(collection.getIsPrivate());
//    }
//
//    // ============================== end US019 ==============================
//
//
//    // ============================== US018 ==============================
//
//    /**
//     * US018
//     * Ensures getUserCollections returns only collections
//     * owned by the given user.
//     */
//    @Test
//    void getUserCollections_shouldReturnOnlyCollectionsOfGivenUser_US018() {
//
//        // Arrange
//        User owner1 = createValidUser();
//        User owner2 = new User(
//                "otherUser",
//                new Contact("913333333", "351", null, null),
//                new Profile("Other", createCountry(), LocalDate.of(1985, 1, 1)),
//                UserRole.NORMAL_USER
//        );
//
//        CollectionOfPublications c1 =
//                repository.createIfNotExists(owner1, "Favorites", new Genre("Fiction"));
//
//        CollectionOfPublications c2 =
//                repository.createIfNotExists(owner2, "OtherCollection", new Genre("History"));
//
//        repository.addCollectionToRepo(c1);
//        repository.addCollectionToRepo(c2);
//
//        // Act
//        List<CollectionOfPublications> result = repository.getUserCollections(owner1);
//
//        // Assert
//        assertEquals(1, result.size());
//        assertEquals("Favorites", result.get(0).getCollectionName());
//    }
//
//    /**
//     * US018
//     * Ensures collectionDTOToDomain returns the correct domain object.
//     */
//    @Test
//    void collectionDTOToDomain_shouldReturnMatchingCollection_US018() throws Exception {
//
//        // ARRANGE
//        User owner = createValidUser();
//        Genre genre = new Genre("Fiction");
//
//        CollectionOfPublications collection =
//                repository.createIfNotExists(owner, "Favorites", genre);
//
//        repository.addCollectionToRepo(collection);
//
//        CollectionOfPublicationsDTO_US018 dto =
//                new CollectionOfPublicationsDTO_US018(
//                        "Favorites",
//                        "Fiction",
//                        owner.getUserName()
//                );
//
//        // ACT
//        CollectionOfPublications result =
//                repository.collectionDTOToDomain(dto);
//
//        // ASSERT
//        assertEquals(collection, result);
//    }
//
//
//    /**
//     * US018
//     * Should throw InstanceNotFoundException if DTO does not match any collection.
//     */
//    @Test
//    void collectionDTOToDomain_shouldThrowWhenNotFound_US018() {
//
//        // ARRANGE
//        CollectionOfPublicationsDTO_US018 fakeDTO =
//                new CollectionOfPublicationsDTO_US018(
//                        "NonExisting",
//                        "Fiction",
//                        "validUser"
//                );
//
//        // ACT + ASSERT
//        assertThrows(InstanceNotFoundException.class,
//                () -> repository.collectionDTOToDomain(fakeDTO));
//    }
//
//
//    @Test
//    void addPublicationToCollection_shouldReturnFalse_whenUserIsNull_US018() {
//
//        boolean result = repository.addPublicationToCollection(
//                null,
//                null,
//                null,
//                null
//        );
//
//        assertFalse(result);
//    }
//
////    @Test
////    void addPublicationToCollection_shouldReturnFalse_whenUserIsNotOwner_US018() {
////
////        User user = createValidUser();
////        User otherUser = new User(
////                "otherUser",
////                new Contact("913333333", "351", null, null),
////                new Profile("Other", new Country("PORTUGAL"), LocalDate.of(1985, 1, 1)),
////                UserRole.NORMAL_USER
////        );
////
////        CollectionOfPublications collection =
////                new CollectionOfPublications("Favorites", new Genre("Fiction"), otherUser);
////
////        Library library = new Library("Test", user);
////
////        PublicationSample publication = createValidPublication();
////        library.addPublication(publication);
////
////        boolean result = repository.addPublicationToCollection(
////                user,
////                library,
////                collection,
////                publication
////        );
////
////        assertFalse(result);
////    }
//
////    @Test
////    void addPublicationToCollection_shouldReturnFalse_whenLibraryDoesNotBelongToUser_US018() {
////
////        User user = createValidUser();
////        User otherUser = new User(
////                "otherUser",
////                new Contact("913333333", "351", null, null),
////                new Profile("Other", new Country("PORTUGAL"), LocalDate.of(1985, 1, 1)),
////                UserRole.NORMAL_USER
////        );
////
////        CollectionOfPublications collection =
////                new CollectionOfPublications("Favorites", new Genre("Fiction"), user);
////
////        Library library = new Library("Test", otherUser);
////
////        PublicationSample publication = createValidPublication();
////        library.addPublication(publication);
////
////        boolean result = repository.addPublicationToCollection(
////                user,
////                library,
////                collection,
////                publication
////        );
////
////        assertFalse(result);
////    }
//
////    @Test
////    void addPublicationToCollection_shouldReturnFalse_whenPublicationNotInLibrary_US018() {
////
////        User user = createValidUser();
////
////        CollectionOfPublications collection =
////                new CollectionOfPublications("Favorites", new Genre("Fiction"), user);
////
////        Library library = new Library("Test", user);
////
////        PublicationSample publication = createValidPublication();
////
////        boolean result = repository.addPublicationToCollection(
////                user,
////                library,
////                collection,
////                publication
////        );
////
////        assertFalse(result);
////    }
//
////    @Test
////    void addPublicationToCollection_shouldReturnTrue_whenAllValid_US018() {
////
////        User user = createValidUser();
////
////        CollectionOfPublications collection =
////                new CollectionOfPublications("Favorites", new Genre("Fiction"), user);
////
////        Library library = new Library("Test", user);
////
////        PublicationSample publication = createValidPublication();
////        library.addPublication(publication);
////
////        boolean result = repository.addPublicationToCollection(
////                user,
////                library,
////                collection,
////                publication
////        );
////
////        assertTrue(result);
////        assertEquals(1, collection.getPublications().size());
////    }
//
//    // ============================== end US018 ==============================
//
//
//    // ============================== US020 ==============================
//
//    /**
//     * US020
//     * Should return a list of public collections of publications, given a user selected genre
//     */
//
//    @Test
//    void shouldReturnPublicListByGenre()
//    {
//        Genre desiredGenre = new Genre("Action");
//
//        // Setup
//        User userName = createValidUser();
//        PublicationSample publicationSample = createValidPublication();
//        LibraryFactory libraryFactory = new LibraryFactory();
//        Library library = libraryFactory.create("library name", userName);
//
//
//        // Creation of Collection of Publications
//        CollectionOfPublications c1 = new CollectionOfPublications(
//                "Collection1",
//                new Genre("Action"),
//                userName
//        );
//        c1.set_isPrivate(false);  // Torna pública para US020
//
//        CollectionOfPublications c2 = new CollectionOfPublications(
//                "Collectioion2",
//                new Genre("Action"),
//                userName
//        );
//        // c2 fica privada (default)
//
//        CollectionOfPublications c3 = new CollectionOfPublications(
//                "Collection3",
//                new Genre("Sci-fi"),
//                userName
//        );
//        c3.set_isPrivate(false);  // Torna pública
//
//        CollectionOfPublications c4 = new CollectionOfPublications(
//                "Collection4",
//                new Genre("Action"),
//                userName
//        );
//        c4.set_isPrivate(false);  // Torna pública
//
//
//        repository.addPublicationToCollection(userName, library, c1, publicationSample);
//        repository.addPublicationToCollection(userName, library, c2, publicationSample);
//        repository.addPublicationToCollection(userName, library, c3, publicationSample);
//        repository.addPublicationToCollection(userName, library, c4, publicationSample);
//
//        repository.addCollectionToRepo(c1);
//        repository.addCollectionToRepo(c2);
//        repository.addCollectionToRepo(c3);
//        repository.addCollectionToRepo(c4);
//
//        assertEquals(2, repository.findPublicByGenre(desiredGenre).size()); // checks if the function correctly found the 2 public lists of the desired genre
//    }
//
//    @Test
//    void shouldReturnEmptyWhenNoPublicListsExist() {
//
//        Genre desiredGenre = new Genre("Action");
//
//        List<CollectionOfPublicationsDTO> result = repository.findPublicByGenre(desiredGenre);
//
//        assertTrue(result.isEmpty());
//    }
//
//    // ============================== end US020 ==============================
//
//}