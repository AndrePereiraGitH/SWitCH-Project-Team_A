package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


class CollectionOfPublicationsRepositoryTest {

    private CollectionOfPublicationsRepository repository;

    @BeforeEach
    void setup() {
        repository = new CollectionOfPublicationsRepository();
    }

    // HELPER
    private Publication createValidPublication() {

        Country country = new Country("PORTUGAL");

        Profile profile = new Profile(
                "Author Name",
                new Country("PORTUGAL"),
                LocalDate.of(1980, 1, 1)
        );

        Author author = new Author(
                profile,
                "Short biography"
        );

        List<Author> authors = List.of(author);

//        Publisher publisher = new Publisher(
//                "Test Publisher",
//                "PT123456789",
//                new Contact("912345678", "351", null, null)
//        );

        BibliographicalData bibliographicalData = new BibliographicalData(
                "Test Title",
                authors,
                Locale.ENGLISH,
//                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
//                200,
                "Sample text"
        );

        PublicationType publicationType = new PublicationType(
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
        Code code = Code.fromIsbn13 ("978316148412");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        return new Publication(
                bibliographicalData,
                publicationType,
                user,
                PublicationStatus.READING,
//                "First Edition",
                edition,
                Code.fromIsbn13("9783161484100")
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

    private CollectionOfPublications createValidCollection(User user) {

        return new CollectionOfPublications(
                "Favorites",
                new Genre("Fiction"),
                user
        );
    }

    // SUCCESS TEST
    @Test
    void shouldAddPublicationSuccessfully() {

        // ARRANGE
        User user = createValidUser();
        Publication publication = createValidPublication();

        // Add publication to user's library
        user.getLibrary().addPublication(publication);

        CollectionOfPublications collection =
                createValidCollection(user);

        // User owns the collection
        user.addCollection(collection);

        // ACT
        boolean result =
                repository.addPublicationToCollection(user, collection, publication);

        // ASSERT
        assertTrue(result);                                         // Publication was added
        assertTrue(collection.containsPublication(publication));    // Publication exists in collection
    }


    // ERROR TESTS
    @Test
    void shouldReturnFalseWhenUserIsNull() {

        boolean result = repository.addPublicationToCollection(null, null, null);

        assertFalse(result);   // Null parameters are not allowed
    }

    @Test
    void shouldReturnFalseWhenCollectionDoesNotBelongToUser() {

        // ARRANGE
        User user = createValidUser();
        Publication publication = createValidPublication();

        user.getLibrary().addPublication(publication);

        // Collection NOT added to user
        CollectionOfPublications collection =
                createValidCollection(user);

        // ACT
        boolean result = repository.addPublicationToCollection(user, collection, publication);

        // ASSERT
        assertFalse(result);   // User does not own the collection
    }

    @Test
    void shouldReturnFalseWhenPublicationIsNotInLibrary() {

        // ARRANGE
        User user = createValidUser();
        Publication publication = createValidPublication();

        // Publication NOT added to library
        CollectionOfPublications collection = createValidCollection(user);

        user.addCollection(collection);

        // ACT
        boolean result = repository.addPublicationToCollection(user, collection, publication);

        // ASSERT
        assertFalse(result);   // Publication must exist in user's library
    }

    @Test
    void shouldReturnFalseWhenPublicationAlreadyExistsInCollection() {

        // ARRANGE
        User user = createValidUser();
        Publication publication = createValidPublication();

        // ACT
        user.getLibrary().addPublication(publication);
        CollectionOfPublications collection = createValidCollection(user);
        user.addCollection(collection);

        // Add once
        repository.addPublicationToCollection(user, collection, publication);

        // Try to add again
        boolean result = repository.addPublicationToCollection(user, collection, publication);

        // ASSERT
        assertFalse(result);   // Duplicate publications are not allowed
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

}
