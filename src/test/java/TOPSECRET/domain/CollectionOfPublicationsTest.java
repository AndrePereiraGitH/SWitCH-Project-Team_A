package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CollectionOfPublicationsTest {

    // ========== US017 - Exception Tests ==========

    /**
     * US017: Verifies that IllegalArgumentException is thrown when collection name is null
     */
    @Test
    void shouldThrowExceptionWhenCollectionNameIsNull() {

        // Arrange
        Genre mockGenre = mock(Genre.class);
        User mockUser = mock(User.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications(null, mockGenre, mockUser));
    }

    /**
     * US017: Verifies that IllegalArgumentException is thrown when collection name is blank
     */
    @Test
    void shouldThrowExceptionWhenCollectionNameIsBlank() {

        // Arrange
        Genre mockGenre = mock(Genre.class);
        User mockUser = mock(User.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications(" ", mockGenre, mockUser));
    }

    /**
     * US017: Verifies that IllegalArgumentException is thrown when user is null
     */
    @Test
    void shouldThrowExceptionWhenUserIsNull() {

        // Arrange
        Genre mockGenre = mock(Genre.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications("Favorites", mockGenre, null));
    }

    /**
     * US017: Verifies that IllegalArgumentException is thrown when genre is null
     */
    @Test
    void shouldThrowExceptionWhenGenreIsNull() {

        // Arrange
        User mockUser = mock(User.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications("Favorites", null, mockUser));
    }

    /**
     * US017: Verifies that a valid CollectionOfPublications is correctly initialized
     * with the given name, genre, owner, private by default, and an empty publication list.
     */
    @Test
    void shouldInitializeInstanceWhenValidParameters() {

        // Arrange
        String name = "Favorites";
        Genre mockGenre = mock(Genre.class);
        User mockUser = mock(User.class);

        // Act
        CollectionOfPublications result = new CollectionOfPublications(name, mockGenre, mockUser);

        // Assert
        assertEquals(name, result.getCollectionName());
        assertEquals(mockGenre, result.getCollectionGenre());
        assertEquals(mockUser, result.getOwner());
        assertTrue(result.getIsPrivate());
        assertTrue(result.getPublications().isEmpty());
    }
}
