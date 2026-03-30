package TOPSECRET.domain;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.Genre;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link CollectionOfPublicationsFactory} — US017.
 *
 * <p>These tests use <b>real objects</b> — no mocks. A real factory, a real Genre,
 * a real User, and a real CollectionOfPublications are all created together.
 * This means the test checks that the factory and the domain class work correctly together.
 *
 * <p>The factory's only job is to create a {@link CollectionOfPublications} by calling its constructor.
 * Validation (null checks, blank name) is not done by the factory — it is done by the constructor.
 * So these tests also indirectly confirm that the constructor validation is working.
 *
 * <p>All tests in this file belong to <b>US017</b> — the factory was created specifically
 * to support collection creation in that user story.
 */
class CollectionOfPublicationsFactoryDependenciesTest {

    // one shared factory instance — the factory has no state, so it is safe to reuse across tests
    private final CollectionOfPublicationsFactory factory = new CollectionOfPublicationsFactory();

    // helper — builds a valid User so we don't repeat this setup in every test
    private User createValidUser() {
        CountryFactory countryFactory = new CountryFactory();
        Country country = countryFactory.create("Portugal");
        return new User(
                "validUser",
                new Contact("912345678", "351", null, null),
                new Profile("User Name", country, LocalDate.of(1990, 1, 1)),
                UserRole.NORMAL_USER
        );
    }


    // ============================== US017 ==============================

    // --- Happy path ---

    // The factory must create a collection with the correct name, genre, and owner.
    // We also check that isPrivate is true (US017 rule) and that publications start empty.
    // If all five assertions pass, the factory delegated correctly to the constructor.
    @Test
    void shouldCreateCollectionSuccessfully() {
        // Arrange
        Genre genre = new GenreFactory().create("Fiction");
        User user = createValidUser();

        // Act — factory creates the collection by calling the domain constructor
        CollectionOfPublications result = factory.create("Favorites", genre, user);

        // Assert — all fields must match and collection must start private and empty
        assertNotNull(result);
        assertEquals("Favorites", result.getCollectionName());
        assertEquals(genre, result.getCollectionGenre());
        assertEquals(user, result.getOwner());
        assertTrue(result.getIsPrivate());   // US017 rule: always private at creation
        assertTrue(result.getPublications().isEmpty()); // starts with no publications
    }

    // --- Validation errors ---

    // The factory passes the name directly to the constructor.
    // The constructor rejects null — so the factory must also fail when name is null.
    @Test
    void shouldThrowWhenNameIsNull() {
        // Arrange
        Genre genre = new GenreFactory().create("Fiction");
        User user = createValidUser();

        // Act & Assert — null name must throw (caught by the constructor)
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, genre, user));
    }

    // US017 requires the user to choose a genre — null genre is not valid.
    // The constructor rejects it, so the factory must also fail when genre is null.
    @Test
    void shouldThrowWhenGenreIsNull() {
        // Arrange
        User user = createValidUser();

        // Act & Assert — null genre must throw (caught by the constructor)
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("Favorites", null, user));
    }

    // Every collection must have an owner — null user is not valid.
    // The constructor rejects it, so the factory must also fail when user is null.
    @Test
    void shouldThrowWhenUserIsNull() {
        // Arrange
        Genre genre = new GenreFactory().create("Fiction");

        // Act & Assert — null user must throw (caught by the constructor)
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("Favorites", genre, null));
    }

    // ============================== end US017 ==============================

}