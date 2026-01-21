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


    // Creates a valid Genre
    private Genre createValidGenre() {
        return new Genre("Fiction");
    }

    // Creates a valid Library
    private Library createValidLibrary() {
        return new Library();
    }

    // Creates a valid Publication
    private Publication createValidPublication() {

        // BIBLIOGRAPHICAL DATA
        Country country = new Country("PORTUGAL");

        Profile author = new Profile(
                "Author Name",
                country,
                LocalDate.of(1980, 1, 1)
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

        // PUBLICATION TYPE
        PublicationType publicationType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Standard publication"
        );

        // USER
        User user = new User(
                "validUser",
                new Contact("911111111", "351", null, null),
                author,
                UserRole.ADMIN   // adjust enum value if needed
        );

        // CODE
        Code code = Code.fromIsbn13("9783161484100");

        // OTHER ATTRIBUTES
        PublicationStatus publicationStatus = PublicationStatus.FOR_SALE;
        String edition = "First Edition";

        // CREATE PUBLICATION
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
    private CollectionOfPublications createBaseCollection() {
        return new CollectionOfPublications(
                "Favorites",
                createValidGenre(),
                createValidLibrary(),
                true
        );
    }

    // SUCCESS TESTS
    // Verifies that two collections with the same data are equal
    @Test
    void shouldCreateEqualCollectionsSuccessfully() {

        // ARRANGE AND ACT
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();

        // ASSERT
        assertEquals(expected, result);   // Collections must be equal after creation
    }

    // Verifies that a publication can be added to the collection
    @Test
    void shouldAddPublicationSuccessfully() {

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
    void shouldNotAddDuplicatePublication() {

        // ARRANGE
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();
        Publication publication = createValidPublication();

        // ACT
        expected.addPublication(publication);
        result.addPublication(publication);
        result.addPublication(publication);   // Attempt duplicate add

        // ASSERT
        assertEquals(expected, result);   // Collection should contain only one publication
    }

    // ERROR TESTS
    // Verifies exception when collection name is null
    @Test
    void shouldThrowExceptionWhenCollectionNameIsNull() {

        // ARRANGE AND ACT
        Genre genre = createValidGenre();
        Library library = createValidLibrary();

        // ASSERT
        assertThrows(IllegalArgumentException.class, () -> {   // Name cannot be null
            new CollectionOfPublications(null, genre, library, true);
        });
    }

    // Verifies exception when collection name is blank
    @Test
    void shouldThrowExceptionWhenCollectionNameIsBlank() {

        // ARRANGE AND ACT
        Genre genre = createValidGenre();
        Library library = createValidLibrary();

        // ASSERT
        assertThrows(IllegalArgumentException.class, () -> {   // Name cannot be blank
            new CollectionOfPublications("   ", genre, library, true);
        });
    }

    // Verifies exception when genre is null
    @Test
    void shouldThrowExceptionWhenGenreIsNull() {

        // ARRANGE AND ACT
        Library library = createValidLibrary();

        // ASSERT
        assertThrows(IllegalArgumentException.class, () -> {   // Genre cannot be null
            new CollectionOfPublications("Favorites", null, library, true);
        });
    }

    // Verifies exception when library is null
    @Test
    void shouldThrowExceptionWhenLibraryIsNull() {

        // ARRANGE AND ACT
        Genre genre = createValidGenre();

        // ASSERT
        assertThrows(IllegalArgumentException.class, () -> {   // Library cannot be null
            new CollectionOfPublications("Favorites", genre, null, true);
        });
    }

    // Verifies that adding null publication does not change the collection
    @Test
    void shouldNotAddNullPublication() {

        // ARRANGE
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();

        // ACT
        result.addPublication(null);   // Attempt to add null

        // ASSERT
        assertEquals(expected, result);   // Collection must remain unchanged
    }
}
