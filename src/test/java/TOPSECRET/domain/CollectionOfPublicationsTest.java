package TOPSECRET.domain;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CollectionOfPublicationsTest {

    private User createValidUser()
    {
        Contact contact = new Contact("912222222", "351", null, null);
        Country country = new Country("PORTUGAL");
        LocalDate birthday = LocalDate.of(1980, 4, 29);
        Profile profile = new Profile("John Doe", country, birthday);

        return new User("John Doe", contact, profile, UserRole.NORMAL_USER);
    }


    // Creates a valid Genre
    private Genre createValidGenre()
    {
        return new Genre("Fiction");
    }

    // Creates a valid Library
    private Library createValidLibrary()
    {
        User user = createValidUser();
        return new Library("Library name", user);
    }

    // Creates a valid Publication
    private Publication createValidPublication()
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
                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
                200,
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
        String edition = "First Edition";

        // Create publication
        return new Publication(
                bibliographicalData,
                publicationType,
                user,
                publicationStatus,
                edition,
                code
        );
    }

    // Creates a base valid collection
    private CollectionOfPublications createBaseCollection()
    {
        return new CollectionOfPublications(
                "Favorites",
                createValidGenre(),
                createValidLibrary(),
                true, createValidUser()
        );
    }

    // SUCCESS TESTS
    // Verifies that two collections with the same data are equal
    @Test
    void shouldCreateEqualCollectionsSuccessfully()
    {
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();

        assertEquals(expected, result);   // Collections must be equal
    }

    // Verifies that a publication can be added to the collection
    @Test
    void shouldAddPublicationSuccessfully() {
        // US018: Add a publication to an existing collection

        // ARRANGE
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();
        Publication publication = createValidPublication();

        // ACT
        expected.addPublication(publication);
        result.addPublication(publication);

        // ASSERT
        assertEquals(expected, result);   // Both collections must contain the same publication
    }


    // Verifies that duplicate publications are not added
    @Test
    void shouldNotAddDuplicatePublication()
    {
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();
        Publication publication = createValidPublication();

        expected.addPublication(publication);
        result.addPublication(publication);
        result.addPublication(publication);   // Try to add duplicate

        assertEquals(expected, result);   // Collection should contain only one publication
    }

    // ERROR TESTS
    // Verifies exception when collection name is null
    @Test
    void shouldThrowExceptionWhenCollectionNameIsNull()
    {
        Genre genre = createValidGenre();
        Library library = createValidLibrary();
        User user = createValidUser();

        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications(null, genre, library, true, user);
        });
    }

    // Verifies exception when collection name is blank
    @Test
    void shouldThrowExceptionWhenCollectionNameIsBlank()
    {
        Genre genre = createValidGenre();
        Library library = createValidLibrary();
        User user = createValidUser();

        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications("   ", genre, library, true ,user);
        });
    }

    // Verifies exception when genre is null
    @Test
    void shouldThrowExceptionWhenGenreIsNull()
    {
        Library library = createValidLibrary();
        User user = createValidUser();

        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications("Favorites", null, library, true,  user);
        });
    }

    // Verifies exception when library is null
    @Test
    void shouldThrowExceptionWhenLibraryIsNull()
    {
        Genre genre = createValidGenre();
        User user = createValidUser();

        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications("Favorites", genre, null, true, user);
        });
    }

    // Verifies exception when User is null
    @Test
    void shouldThrowExceptionWhenUserIsNull()
    {
        Genre genre = createValidGenre();

        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications("Favorites", genre, null, true, null);
        });
    }

    // Verifies that adding null publication does not change the collection
    @Test
    void shouldNotAddNullPublication()
    {
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();

        result.addPublication(null);   // Attempt to add null

        assertEquals(expected, result);   // Collection must remain unchanged
    }

    // US017
    @Test
    void shouldCreateEmptyCollection_US017() {
        // When a collection is created, it must start with no publications.
        // The population of the collection is handled by later user stories (e.g. US018).

        // Arrange & Act
        CollectionOfPublications collection = createBaseCollection();

        // Assert
        assertNotNull(collection.getPublications());
        assertTrue(collection.getPublications().isEmpty());
    }

}