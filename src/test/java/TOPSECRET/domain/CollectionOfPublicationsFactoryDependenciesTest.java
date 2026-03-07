package TOPSECRET.domain;

import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link CollectionOfPublicationsFactory}.
 * SUT: CollectionOfPublicationsFactory — uses real Genre, User, and CollectionOfPublications objects.
 */
class CollectionOfPublicationsFactoryDependenciesTest {

    private final CollectionOfPublicationsFactory factory = new CollectionOfPublicationsFactory();

    private User createValidUser() {
        Country country = new Country("PORTUGAL");
        return new User(
                "validUser",
                new Contact("912345678", "351", null, null),
                new Profile("User Name", country, LocalDate.of(1990, 1, 1)),
                UserRole.NORMAL_USER
        );
    }

    /**
     * Verifies that the factory creates a valid private CollectionOfPublications with the correct fields.
     */
    @Test
    void shouldCreateCollectionSuccessfully() {
        // Arrange
        Genre genre = new Genre("Fiction");
        User user = createValidUser();

        // Act
        CollectionOfPublications result = factory.create("Favorites", genre, user);

        // Assert
        assertNotNull(result);
        assertEquals("Favorites", result.getCollectionName());
        assertEquals(genre, result.getCollectionGenre());
        assertEquals(user, result.getOwner());
        assertTrue(result.getIsPrivate());
        assertTrue(result.getPublications().isEmpty());
    }

    /**
     * Verifies that an exception is thrown when name is null.
     */
    @Test
    void shouldThrowWhenNameIsNull() {
        Genre genre = new Genre("Fiction");
        User user = createValidUser();

        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, genre, user));
    }

    /**
     * Verifies that an exception is thrown when genre is null.
     */
    @Test
    void shouldThrowWhenGenreIsNull() {
        User user = createValidUser();

        assertThrows(IllegalArgumentException.class,
                () -> factory.create("Favorites", null, user));
    }

    /**
     * Verifies that an exception is thrown when user is null.
     */
    @Test
    void shouldThrowWhenUserIsNull() {
        Genre genre = new Genre("Fiction");

        assertThrows(IllegalArgumentException.class,
                () -> factory.create("Favorites", genre, null));
    }
}