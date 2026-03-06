package TOPSECRET.repository;

import TOPSECRET.controller.US019ToPubliclyShareCollectionOfPublicationsController;
import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.CollectionOfPublicationsDTO;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US018;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US019;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


class CollectionOfPublicationsRepositoryDependenciesTest {

    private CollectionOfPublicationsRepository repository;

    @BeforeEach
    void setup() {
        repository = new CollectionOfPublicationsRepository();
    }

    // HELPER
    private PublicationSample createValidPublication() {

        Country country = new Country("PORTUGAL");

        Profile profile = new Profile(
                "Author Name",
                new Country("PORTUGAL"),
                LocalDate.of(1980, 1, 1)
        );
        AuthorFactory authorFactory = new AuthorFactory();
        Author author = authorFactory.createAuthorWithExistingProfile(
                profile,
                "Short biography"
        );

        List<Author> authors = List.of(author);

 //       Publisher publisher = new Publisher(
 //               "Test Publisher",
 //               new Contact("912345678", "351", null, null)
 //       );

        BibliographicalData bibliographicalData = new BibliographicalData(
                "Test Title",
                authors,
                Locale.ENGLISH,
 //               publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
 //               200,
                "Sample text"
        );

        PublicationType publicationType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Standard publication"
        );

        Profile profileUser = new Profile(
                "User Name",
                new Country("PORTUGAL"),
                LocalDate.of(1980, 1, 1)
        );

        User user = new User(
                "validUser",
                new Contact("911111111", "351", null, null),
                profileUser,
                UserRole.ADMIN
        );

        // Edition
        Code code = Code.fromIsbn13 ("9783161484120");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);

        EditionFactory editionFactory = new EditionFactory();
        Edition edition= editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        return new PublicationSample(
                bibliographicalData,
                user,
                PublicationStatus.READING,
//                publicationType,
 //               "First Edition",
                edition,
                Condition.NEW
//                Code.fromIsbn13("9783161484100")
        );
    }

    private User createValidUser() {

        Profile profile = new Profile(
                "Test User",
                new Country("PORTUGAL"),
                LocalDate.of(1990, 1, 1)
        );

        return new User(
                "validUser",
                new Contact("912222222", "351", null, null),
                profile,
                UserRole.NORMAL_USER
        );
    }

/**
 * - US017: Creation of private collections and duplicate validation.
 */

    @Test
    void shouldCreatePrivateCollectionSuccessfully_US017() {
        // US017: Create a private collection with name and genre

        User user = createValidUser();
        Genre genre = new Genre("Fiction");

        CollectionOfPublications result =
                repository.createIfNotExists(user, "Favorites", genre);

        assertNotNull(result);
        assertEquals("Favorites", result.getCollectionName());
        assertEquals(genre, result.getCollectionGenre());
        assertTrue(result.getIsPrivate());
        assertTrue(result.getPublications().isEmpty());
    }

    @Test
    void shouldNotAllowDuplicateCollectionForSameUser_US017() {
        // US017: Prevent duplicate collections with same name and genre

        User user = createValidUser();
        Genre genre = new Genre("Fiction");

        repository.createIfNotExists(user, "Favorites", genre);

        assertThrows(IllegalArgumentException.class, () ->
                repository.createIfNotExists(user, "Favorites", genre)
        );
    }

    @Test
    void shouldAllowSameNameWithDifferentGenre_US017() {

        User user = createValidUser();

        repository.createIfNotExists(user, "Favorites", new Genre("Fiction"));

        CollectionOfPublications result =
                repository.createIfNotExists(user, "Favorites", new Genre("History"));

        assertNotNull(result);
    }

    /**
     * US019 - tests for getAllCollectionsOfPublicationsByUser
     */

    @Test
    void getAllCollectionOfPublicationsByUser_shouldReturnEmptyList_ifUserDoesNotOwnsAnyCollection() {

        List<CollectionOfPublications> result = repository.getAllCollectionOfPublicationsByUser(createValidUser());

        assertEquals(0, result.size());

    }

    @Test
    void getAllCollectionOfPublicationsByUser_shouldReturn_ListOfCollectionOfPublications_IfUserOwnsACollection() {

        // arrange

        User owner = createValidUser();

        CollectionOfPublications collection = repository.createIfNotExists(
                owner,
                "My Funny Collection",
                new Genre("Fiction")
        );

        repository.addCollectionToRepo(collection);

        // act
        List<CollectionOfPublications> result = repository.getAllCollectionOfPublicationsByUser(owner);

        // assert
        assertEquals(1, result.size());

    }

    @Test
    void getAllCollectionOfPublicationsByUser_shouldThrow_IfUIserIsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repository.getAllCollectionOfPublicationsByUser(null));

        assertEquals("User cannot be null", exception.getMessage());

    }

    @Test
    void getCollectionByDTO_shouldReturn_collectionOfPublicationsThatMatchesDTO() throws InstanceNotFoundException {
        // arrange
        US019ToPubliclyShareCollectionOfPublicationsController US019ctrl = new US019ToPubliclyShareCollectionOfPublicationsController(repository);

        User owner = createValidUser();
        Genre genre = new Genre("Fiction");
        CollectionOfPublications collection = repository.createIfNotExists(
                owner,
                "My Funny Collection",
                genre
        );
        repository.addCollectionToRepo(collection);

        List<CollectionOfPublicationsDTO_US019> listDTO = US019ctrl.requestAllCollectionsOfPublicationsByUser(owner);

        // act
        CollectionOfPublications result = repository.getCollectionByDTO(listDTO.get(0));

        // assert
        assertEquals(collection, result);
    }

    @Test
    void getCollectionDTO_shouldThrows_ifCollectionDTO_doesNotMatchesAnyCollection() {

        // arrange
        User owner = createValidUser();
        CollectionOfPublications collection = repository.createIfNotExists(
                owner,
                "My Funny Collection",
                new Genre("Fiction")
        );
        repository.addCollectionToRepo(collection);

        CollectionOfPublicationsDTO_US019 fakeDTO = new CollectionOfPublicationsDTO_US019(
                "Romance",
                "Romance",
                owner
        );

        // act + assert

        InstanceNotFoundException exception = assertThrows(InstanceNotFoundException.class, () -> repository.getCollectionByDTO(fakeDTO));

        assertEquals("selected collection, does not match any CollectionOfPublications", exception.getMessage());

    }

    /**
     * Ensures that making a collection public returns {@code true} when a valid
     * {@link CollectionOfPublicationsDTO_US019} identifies an existing collection in the repository.
     *
     * @throws InstanceNotFoundException if the DTO does not match any existing collection (not expected in this test)
     */
    @Test
    void makeCollectionOfPublicationsPublic_shouldReturnTRUE_() throws InstanceNotFoundException {
        // arrange
        US019ToPubliclyShareCollectionOfPublicationsController US019ctrl = new US019ToPubliclyShareCollectionOfPublicationsController(repository);

        User owner = createValidUser();
        Genre genre = new Genre("Fiction");
        CollectionOfPublications collection = repository.createIfNotExists(
                owner,
                "My Funny Collection",
                genre
        );
        repository.addCollectionToRepo(collection);

        List<CollectionOfPublicationsDTO_US019> listDTO = US019ctrl.requestAllCollectionsOfPublicationsByUser(owner);

        // act
        boolean result = repository.makeCollectionOfPublicationsPublic(listDTO.get(0));

        // assert

        assertTrue(result);

    }

    /**
     * Ensures that {@link CollectionOfPublicationsRepository#makeCollectionOfPublicationsPublic(CollectionOfPublicationsDTO_US019)}
     * rejects a {@code null} DTO by throwing {@link IllegalArgumentException} with the expected message.
     */
    @Test
    void makeCollectionOfPublicationsPublic_shouldThrows_ifCollectionDTOisNull() {

        // act + assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repository.makeCollectionOfPublicationsPublic(null));

        assertEquals("collection dto cannot be null", exception.getMessage());

    }

    /**
     * Ensures that making a collection public updates the entity state by setting its privacy flag
     * to {@code false} (i.e., the collection becomes public).
     * <p>
     * This test is important for mutation testing because it verifies the side-effect of the method.
     * Without it, mutations that remove or invert {@code coll.set_isPrivate(false)} may survive.
     * </p>
     *
     * @throws InstanceNotFoundException if the DTO does not match any existing collection (not expected in this test)
     */
    @Test
    void makeCollectionOfPublicationsPublic_shouldSetCollectionAsPublic() throws InstanceNotFoundException {
        // Arrange
        US019ToPubliclyShareCollectionOfPublicationsController US019ctrl =
                new US019ToPubliclyShareCollectionOfPublicationsController(repository);

        User owner = createValidUser();
        Genre genre = new Genre("Fiction");
        CollectionOfPublications collection = repository.createIfNotExists(
                owner,
                "My Funny Collection",
                genre
        );

        // Ensure the collection starts as private
        assertTrue(collection.getIsPrivate());

        repository.addCollectionToRepo(collection);

        List<CollectionOfPublicationsDTO_US019> listDTO =
                US019ctrl.requestAllCollectionsOfPublicationsByUser(owner);

        // Act
        boolean result = repository.makeCollectionOfPublicationsPublic(listDTO.get(0));

        // Assert
        assertTrue(result);
        assertFalse(collection.getIsPrivate());
    }

    @Test
    void shouldUseGetPublications() {
        // US017 - new collection should start empty
        User user = createValidUser();
        Genre genre = new Genre("Fiction");

        CollectionOfPublications collection =
                repository.createIfNotExists(user, "My Collection", genre);

        // Use the getter to confirm the list is empty.
        assertTrue(collection.getPublications().isEmpty());
    }

    @Test
    void shouldUseEqualsAndHashCode_US017() {
        // US017: validate equals/hashCode use name, genre, and owner
        User user = createValidUser();
        Genre genre = new Genre("Fiction");

        CollectionOfPublications c1 = new CollectionOfPublications("MyCollection", genre, user);
        CollectionOfPublications c2 = new CollectionOfPublications("MyCollection", genre, user);
        CollectionOfPublications c3 = new CollectionOfPublications("DifferentName", genre, user);

        // equals should consider c1 and c2 equal
        assertEquals(c1, c2);
        assertNotEquals(c1, c3);

        // hashCode should be consistent with equals
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    // US017 — equals(), hashCode() e getPublications()

    @Test
    void equals_shouldReturnTrue_whenComparingToSelf_US017() {
        // equals must be reflexive
        User user = createValidUser();
        CollectionOfPublications collection = repository.createIfNotExists(user, "Favorites", new Genre("Fiction"));
        assertEquals(collection, collection);
    }

    @Test
    void equals_shouldReturnFalse_whenComparingToNull_US017() {
        // equals with null should return false
        User user = createValidUser();
        CollectionOfPublications collection = repository.createIfNotExists(user, "Favorites", new Genre("Fiction"));
        assertNotEquals(collection, null);
    }

    @Test
    void equals_shouldReturnFalse_whenComparingToDifferentType_US017() {
        // equals with another type should return false
        User user = createValidUser();
        CollectionOfPublications collection = repository.createIfNotExists(user, "Favorites", new Genre("Fiction"));
        assertNotEquals(collection, "uma string qualquer");
    }

    @Test
    void equals_shouldReturnFalse_whenGenreIsDifferent_US017() {
        // Different genres imply different collections.
        User user = createValidUser();
        CollectionOfPublications c1 = new CollectionOfPublications("Favorites", new Genre("Fiction"), user);
        CollectionOfPublications c2 = new CollectionOfPublications("Favorites", new Genre("History"), user);
        assertNotEquals(c1, c2);
    }

    @Test
    void equals_shouldReturnFalse_whenOwnerIsDifferent_US017() {
        // Different owner implies different collections
        User user1 = createValidUser();
        User user2 = new User(
                "otherUser",
                new Contact("913333333", "351", null, null),
                new Profile("Other", new Country("PORTUGAL"), LocalDate.of(1985, 1, 1)),
                UserRole.NORMAL_USER
        );
        Genre genre = new Genre("Fiction");
        CollectionOfPublications c1 = new CollectionOfPublications("Favorites", genre, user1);
        CollectionOfPublications c2 = new CollectionOfPublications("Favorites", genre, user2);
        assertNotEquals(c1, c2);
    }

    @Test
    void equals_shouldReturnFalse_whenIsPrivateDiffers_US017() {
        // privacy flag should be part of equals
        User user = createValidUser();
        Genre genre = new Genre("Fiction");
        CollectionOfPublications privateCollection = new CollectionOfPublications("Favorites", genre, user);
        CollectionOfPublications publicCollection = new CollectionOfPublications("Favorites", genre, user);
        publicCollection.set_isPrivate(false);
        assertNotEquals(privateCollection, publicCollection);
    }

    @Test
    void hashCode_shouldDiffer_whenCollectionsAreDifferent_US017() {
        // hashCode should differ when equals is false
        User user = createValidUser();
        CollectionOfPublications c1 = new CollectionOfPublications("Favorites", new Genre("Fiction"), user);
        CollectionOfPublications c2 = new CollectionOfPublications("Favorites", new Genre("History"), user);
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void getPublications_shouldReturnDefensiveCopy_US017() {
        // returns a copy (not the internal list)
        User user = createValidUser();
        CollectionOfPublications collection = repository.createIfNotExists(user, "Favorites", new Genre("Fiction"));
        List<PublicationSample> returned = collection.getPublications();
        assertTrue(collection.getPublications().isEmpty());
    }

    // --------- US018

    /**
     * US018
     * Ensures getUserCollections returns only collections
     * owned by the given user.
     */
    @Test
    void getUserCollections_shouldReturnOnlyCollectionsOfGivenUser_US018() {

        // Arrange
        User owner1 = createValidUser();
        User owner2 = new User(
                "otherUser",
                new Contact("913333333", "351", null, null),
                new Profile("Other", new Country("PORTUGAL"), LocalDate.of(1985, 1, 1)),
                UserRole.NORMAL_USER
        );

        CollectionOfPublications c1 =
                repository.createIfNotExists(owner1, "Favorites", new Genre("Fiction"));

        CollectionOfPublications c2 =
                repository.createIfNotExists(owner2, "OtherCollection", new Genre("History"));

        repository.addCollectionToRepo(c1);
        repository.addCollectionToRepo(c2);

        // Act
        List<CollectionOfPublications> result = repository.getUserCollections(owner1);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Favorites", result.get(0).getCollectionName());
    }

    /**
     * US018
     * Ensures collectionDTOToDomain returns the correct domain object.
     */
    @Test
    void collectionDTOToDomain_shouldReturnMatchingCollection_US018() throws Exception {

        // ARRANGE
        User owner = createValidUser();
        Genre genre = new Genre("Fiction");

        CollectionOfPublications collection =
                repository.createIfNotExists(owner, "Favorites", genre);

        repository.addCollectionToRepo(collection);

        CollectionOfPublicationsDTO_US018 dto =
                new CollectionOfPublicationsDTO_US018(
                        "Favorites",
                        "Fiction",
                        owner.getUserName()
                );

        // ACT
        CollectionOfPublications result =
                repository.collectionDTOToDomain(dto);

        // ASSERT
        assertEquals(collection, result);
    }


    /**
     * US018
     * Should throw InstanceNotFoundException if DTO does not match any collection.
     */
    @Test
    void collectionDTOToDomain_shouldThrowWhenNotFound_US018() {

        // ARRANGE
        CollectionOfPublicationsDTO_US018 fakeDTO =
                new CollectionOfPublicationsDTO_US018(
                        "NonExisting",
                        "Fiction",
                        "validUser"
                );

        // ACT + ASSERT
        assertThrows(InstanceNotFoundException.class,
                () -> repository.collectionDTOToDomain(fakeDTO));
    }


    @Test
    void addPublicationToCollection_shouldReturnFalse_whenUserIsNull_US018() {

        boolean result = repository.addPublicationToCollection(
                null,
                null,
                null,
                null
        );

        assertFalse(result);
    }

//    @Test
//    void addPublicationToCollection_shouldReturnFalse_whenUserIsNotOwner_US018() {
//
//        User user = createValidUser();
//        User otherUser = new User(
//                "otherUser",
//                new Contact("913333333", "351", null, null),
//                new Profile("Other", new Country("PORTUGAL"), LocalDate.of(1985, 1, 1)),
//                UserRole.NORMAL_USER
//        );
//
//        CollectionOfPublications collection =
//                new CollectionOfPublications("Favorites", new Genre("Fiction"), otherUser);
//
//        Library library = new Library("Test", user);
//
//        PublicationSample publication = createValidPublication();
//        library.addPublication(publication);
//
//        boolean result = repository.addPublicationToCollection(
//                user,
//                library,
//                collection,
//                publication
//        );
//
//        assertFalse(result);
//    }

//    @Test
//    void addPublicationToCollection_shouldReturnFalse_whenLibraryDoesNotBelongToUser_US018() {
//
//        User user = createValidUser();
//        User otherUser = new User(
//                "otherUser",
//                new Contact("913333333", "351", null, null),
//                new Profile("Other", new Country("PORTUGAL"), LocalDate.of(1985, 1, 1)),
//                UserRole.NORMAL_USER
//        );
//
//        CollectionOfPublications collection =
//                new CollectionOfPublications("Favorites", new Genre("Fiction"), user);
//
//        Library library = new Library("Test", otherUser);
//
//        PublicationSample publication = createValidPublication();
//        library.addPublication(publication);
//
//        boolean result = repository.addPublicationToCollection(
//                user,
//                library,
//                collection,
//                publication
//        );
//
//        assertFalse(result);
//    }

//    @Test
//    void addPublicationToCollection_shouldReturnFalse_whenPublicationNotInLibrary_US018() {
//
//        User user = createValidUser();
//
//        CollectionOfPublications collection =
//                new CollectionOfPublications("Favorites", new Genre("Fiction"), user);
//
//        Library library = new Library("Test", user);
//
//        PublicationSample publication = createValidPublication();
//
//        boolean result = repository.addPublicationToCollection(
//                user,
//                library,
//                collection,
//                publication
//        );
//
//        assertFalse(result);
//    }

//    @Test
//    void addPublicationToCollection_shouldReturnTrue_whenAllValid_US018() {
//
//        User user = createValidUser();
//
//        CollectionOfPublications collection =
//                new CollectionOfPublications("Favorites", new Genre("Fiction"), user);
//
//        Library library = new Library("Test", user);
//
//        PublicationSample publication = createValidPublication();
//        library.addPublication(publication);
//
//        boolean result = repository.addPublicationToCollection(
//                user,
//                library,
//                collection,
//                publication
//        );
//
//        assertTrue(result);
//        assertEquals(1, collection.getPublications().size());
//    }

    /**
     * US020
     * Should return a list of public collections of publications, given a user selected genre
     */

    @Test
    void shouldReturnPublicListByGenre()
    {
        Genre desiredGenre = new Genre("Action");

        // User Creation
        Profile profile = new Profile("Joao",new Country("Portugal"), LocalDate.of(1974,7,24));
        Email email = new Email("john.doe25","hotmail.com");
        Address location = new Address("rua das pedras","29","4852-2524",new Country("Portugal"));
        Contact contact = new Contact("919402245", "+351", email, location);
        User userName = new User("John Doe", contact, profile, UserRole.NORMAL_USER);


        // Publication sample creation begins

        // Author creation
        Profile authorProfile = new Profile("John Doe",new Country("Portugal"), LocalDate.of(1940,2,24));
        List<Author> authors = new ArrayList<>();
        AuthorFactory authorFactory = new AuthorFactory();
        authors.add(authorFactory.createAuthorWithExistingProfile(authorProfile, "Author Biography"));

        // Bibliographical Data creation
        Locale language = Locale.getDefault();
        LocalDate publishDate = LocalDate.now();
        List<Genre> genre = new ArrayList<>();
        genre.add(new Genre("Sci-Fi"));

        BibliographicalData bd = new BibliographicalData(
                "bibliographical data test",
                authors,
                language,
                publishDate,
                genre,
                "Sample text"
        );

        PublicationStatus pubStatus = PublicationStatus.FOR_SALE;

        // Edition creation
        Code code1 = Code.fromIsbn13 ("9783161484123");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType1 = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);

        EditionFactory editionFactory = new EditionFactory();
        Edition edition = editionFactory.create("1st", code1, physicalAttributes, pubType1, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));


        PublicationSample publicationSample = new PublicationSample(bd, userName, pubStatus, edition, Condition.NEW);
        // Publication Sample creation ends


        // Creates a private library for the user
//        Library privateLibrary = new Library("My Library", userName);


        // Creation of Collection of Publications
        CollectionOfPublications c1 = new CollectionOfPublications(
                "Collection1",
                new Genre("Action"),
                userName
        );
        c1.set_isPrivate(false);  // Torna pública para US020

        CollectionOfPublications c2 = new CollectionOfPublications(
                "Collectioion2",
                new Genre("Action"),
                userName
        );
        // c2 fica privada (default)

        CollectionOfPublications c3 = new CollectionOfPublications(
                "Collection3",
                new Genre("Sci-fi"),
                userName
        );
        c3.set_isPrivate(false);  // Torna pública

        CollectionOfPublications c4 = new CollectionOfPublications(
                "Collection4",
                new Genre("Action"),
                userName
        );
        c4.set_isPrivate(false);  // Torna pública


//        repository.addPublicationToCollection(userName, privateLibrary, c1, publicationSample);
//        repository.addPublicationToCollection(userName, privateLibrary, c2, publicationSample);
//        repository.addPublicationToCollection(userName, privateLibrary, c3, publicationSample);
//        repository.addPublicationToCollection(userName, privateLibrary, c4, publicationSample);

        repository.addCollectionToRepo(c1);
        repository.addCollectionToRepo(c2);
        repository.addCollectionToRepo(c3);
        repository.addCollectionToRepo(c4);

        assertEquals(2, repository.findPublicByGenre(desiredGenre).size()); // checks if the function correctly found the 2 public lists of the desired genre
    }

    @Test
    void shouldReturnEmptyWhenNoPublicListsExist() {

        Genre desiredGenre = new Genre("Action");

        List<CollectionOfPublicationsDTO> result = repository.findPublicByGenre(desiredGenre);

        assertTrue(result.isEmpty());
    }


}
