package TOPSECRET.domain.CollectionOfPublications;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link CollectionOfPublications} — US017.
 *
 * <p>These tests use <b>mocks</b> for {@link Genre} and {@link User}.
 * This means the tests only check the constructor logic of {@link CollectionOfPublications} itself —
 * not the behaviour of Genre or User. If a test fails here, the bug is in this class.
 *
 * <p>Why mocks and not real objects? The constructor of {@code CollectionOfPublications}
 * receives Genre and User as parameters. By mocking them, we remove any risk of those classes
 * causing a failure — so we can be sure the result comes only from this class.
 *
 * <p>All tests in this file belong to <b>US017</b>, which defines what a collection is
 * and what rules apply when it is created:
 * <ul>
 *   <li>Name must not be null or blank</li>
 *   <li>Genre must not be null</li>
 *   <li>User (owner) must not be null</li>
 *   <li>Collection always starts as private</li>
 *   <li>Collection always starts with no publications</li>
 * </ul>
 */
class CollectionOfPublicationsTest {


    // ============================== US017 ==============================

    @Test
    void shouldThrowExceptionWhenCollectionNameIsNull() {
        // Arrange
        GenreID mockGenre = mock(GenreID.class);
        UserId mockUser = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications(null, mockGenre, mockUser));
    }


    @Test
    void shouldThrowExceptionWhenCollectionNameIsBlank() {
        // Arrange
        GenreID mockGenre = mock(GenreID.class);
        UserId mockUser = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications(" ", mockGenre, mockUser));
    }

    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        // Arrange
        UserId mockUser = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications("Favorites", null, mockUser));
    }

    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        // Arrange
        GenreID mockGenre = mock(GenreID.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications("Favorites", mockGenre, null));
    }

    @Test
    void shouldInitializeInstanceWhenValidParameters() {
        // Arrange
        String name = "Favorites";
        GenreID mockGenre = mock(GenreID.class);
        UserId mockUser = mock(UserId.class);

        // Act
        CollectionOfPublications result = new CollectionOfPublications(name, mockGenre, mockUser);

        // Assert
        assertEquals(name, result.getCollectionName());
        assertEquals(mockGenre, result.getCollectionGenre());
        assertEquals(mockUser, result.getOwnerId());
        assertTrue(result.getIsPrivate());          // US017 rule: always private at creation
    }

    // ============================== end US017 ==============================

}