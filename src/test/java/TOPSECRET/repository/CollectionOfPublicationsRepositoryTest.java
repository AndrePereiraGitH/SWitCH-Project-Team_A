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

        PublicationType publicationType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Standard publication"
        );

        User user = new User(
                "validUser",
                new Contact("911111111", "351", null, null),
                author,
                UserRole.ADMIN
        );

        return new Publication(
                bibliographicalData,
                publicationType,
                user,
                PublicationStatus.READING,
                "First Edition",
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

    private CollectionOfPublications createValidCollection(Library library) {
        return new CollectionOfPublications(
                "Favorites",
                new Genre("Fiction"),
                library,
                true
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
                createValidCollection(user.getLibrary());

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
                createValidCollection(user.getLibrary());

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
        CollectionOfPublications collection = createValidCollection(user.getLibrary());

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
        CollectionOfPublications collection = createValidCollection(user.getLibrary());
        user.addCollection(collection);

        // Add once
        repository.addPublicationToCollection(user, collection, publication);

        // Try to add again
        boolean result = repository.addPublicationToCollection(user, collection, publication);

        // ASSERT
        assertFalse(result);   // Duplicate publications are not allowed
    }
}
