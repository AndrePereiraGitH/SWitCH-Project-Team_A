package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CollectionOfPublicationsDependenciesTest {

    /**
     * Creates a valid User instance for testing purposes.
     * <p>
     * Used across US017, US018, and US019 tests.
     *
     * @return A valid User with complete profile and contact information
     */

    // Creates a valid User
    private User createValidUser()
    {
        Contact contact = new Contact("912222222", "351", null, null);
        Country country = new Country("PORTUGAL");
        LocalDate birthday = LocalDate.of(1980, 4, 29);
        Profile profile = new Profile("John Doe", country, birthday);

        return new User("John Doe", contact, profile, UserRole.NORMAL_USER);
    }

    /**
     * Creates a valid Genre instance for testing purposes.
     * <p>
     * Used across US017, US018, and US019 tests.
     *
     * @return A valid Genre with type "Fiction"
     */

    // Creates a valid Genre
    private Genre createValidGenre()
    {
        return new Genre("Fiction");
    }


    /**
     * Creates a valid PublicationSample instance for testing purposes.
     * <p>
     * Primarily used for US018 tests (adding publications to collections).
     *
     * @return A complete PublicationSample with all required attributes
     */

    // Creates a valid PublicationSample
    private PublicationSample createValidPublication()
    {
        // Create Bibliographical data for publication
        Country country = new Country("PORTUGAL");

        Profile profile = new Profile("John Doe", country, LocalDate.of(1980, 4, 29));

        Author author = new Author(
                profile,
                "Test author for testing"
        );

        Publisher publisher = new Publisher(
                "Test Publisher",
                "PT123456789",
                new Contact("912345678", "351", null, null)
        );

        BibliographicalData bibliographicalData = new BibliographicalData(
                "Test Title",
                List.of(author),
                Locale.ENGLISH,
//                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
//                200,
                "Sample text"
        );

        // Create a publication type
        PublicationType publicationType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Standard publication"
        );

        // Create a user
        User user = new User(
                "validUser",
                new Contact("911111111", "351", null, null),
                profile,
                UserRole.ADMIN   // adjust enum value if needed
        );

        // Create a code
        Code code = Code.fromIsbn13("9783161484100");


        PublicationStatus publicationStatus = PublicationStatus.FOR_SALE;
//        String edition = "First Edition";
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Edition edition= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));


        // Create condition
        Condition condition1 = Condition.NEW;
        Condition condition2 = Condition.LIKE_NEW;
        Condition condition3 = Condition.VERY_GOOD;


        // Create publicationSample
        return new PublicationSample(
                bibliographicalData,
                user,
                publicationStatus,
                edition,
                condition1
        );
    }

    /**
     * Creates a base valid CollectionOfPublications instance for testing.
     * <p>
     * Used across US017, US018, and US019 tests.
     *
     * @return A valid CollectionOfPublications named "Favorites"
     */

    // Creates a base valid collection
    private CollectionOfPublications createBaseCollection()
    {
        return new CollectionOfPublications(
                "Favorites",
                createValidGenre(),
                createValidUser()
        );
    }

    // SUCCESS TESTS
    // Verifies that two collections with the same data are equal
    @Test
    void shouldCreateEqualCollectionsSuccessfully()            // US018
    {
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();

        assertEquals(expected, result);   // Collections must be equal
    }

    // Verifies that a publication can be added to the collection
    @Test
    void shouldAddPublicationSuccessfully() {                   // US018
        // US018: Add a publication to an existing collection

        // ARRANGE
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();
        PublicationSample publication = createValidPublication();

        // ACT
        expected.addPublication(publication);
        result.addPublication(publication);

        // ASSERT
        assertEquals(expected, result);   // Both collections must contain the same publication
    }


    // Verifies that duplicate publications are not added
    @Test
    void shouldNotAddDuplicatePublication()                     // US018
    {
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();
        PublicationSample publication = createValidPublication();

        expected.addPublication(publication);
        result.addPublication(publication);
        result.addPublication(publication);   // Try to add duplicate

        assertEquals(expected, result);   // Collection should contain only one publication
    }

    // ERROR TESTS
    // Verifies exception when collection name is null
    @Test
    void shouldThrowExceptionWhenCollectionNameIsNull()         // US017
    {
        Genre genre = createValidGenre();
        User user = createValidUser();

        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications(null, genre, user);
        });
    }

    // Verifies exception when collection name is blank
    @Test
    void shouldThrowExceptionWhenCollectionNameIsBlank()         // US017
    {
        Genre genre = createValidGenre();
        User user = createValidUser();

        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications("   ", genre, user);
        });
    }

    // Verifies exception when genre is null
    @Test
    void shouldThrowExceptionWhenGenreIsNull()                  // US017
    {
        User user = createValidUser();

        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications("Favorites", null,  user);
        });
    }

     // Verifies exception when library is null
    @Test
    void shouldThrowExceptionWhenLibraryIsNull()
    {
        Genre genre = createValidGenre();

        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications("Favorites", genre, null);
        });
    }

    // Verifies exception when User is null
    @Test
    void shouldThrowExceptionWhenUserIsNull()                   // US017
    {
        Genre genre = createValidGenre();

        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications("Favorites", genre, null);
        });
    }

    // Verifies that adding null publication does not change the collection
    @Test
    void shouldNotAddNullPublication()                          // US018
    {
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();

        result.addPublication(null);   // Attempt to add null

        assertEquals(expected, result);   // Collection must remain unchanged
    }

    // US017
    @Test
    void shouldCreateEmptyCollection_US017() {                   // US017
        // When a collection is created, it must start with no publications.
        // The population of the collection is handled by later user stories (e.g. US018).

        // Arrange & Act
        CollectionOfPublications collection = createBaseCollection();

        // Assert
        assertNotNull(collection.getPublications());
        assertTrue(collection.getPublications().isEmpty());
    }

    /**
     * US019 set_isPrivate false test - make collection public
     */
    @Test
    void set_isPrivate_shouldChangeIsPrivateToFalse_whenFalseIsPassed() {               // US019
        // arrange
        CollectionOfPublications collection = createBaseCollection();

        // act
        collection.set_isPrivate(false);

        // assert
        assertFalse(collection.getIsPrivate());

    }

    /**
     * US019 set_isPrivate test - make collection private
     */

    @Test
    void set_isPrivate_shouldChangeIsPrivateToTrue_whenTrueIsPassed() {                 // US019
        // arrange
        CollectionOfPublications collection = createBaseCollection();
        collection.set_isPrivate(false);

        // act
        collection.set_isPrivate(true);

        // assert
        assertTrue(collection.getIsPrivate());

    }

}