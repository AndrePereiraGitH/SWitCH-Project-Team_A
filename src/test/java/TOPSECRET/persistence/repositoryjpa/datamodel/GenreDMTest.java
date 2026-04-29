package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.GenreID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GenreDM}.
 * Verifies that the data model correctly stores genre data
 * and throws exceptions for invalid inputs.
 */
class GenreDMTest {

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void ensureGenreDMIsCreatedSuccessfully() {
        // Arrange
        GenreID id = new GenreID("drama");
        String genreType = "Drama";

        // Act
        GenreDM genreDM = new GenreDM(id, genreType);

        // Assert
        assertNotNull(genreDM);
    }

    @Test
    void ensureGenreDMStoresCorrectId() {
        // Arrange
        GenreID id = new GenreID("drama");
        String genreType = "Drama";

        // Act
        GenreDM genreDM = new GenreDM(id, genreType);

        // Assert
        assertEquals(id, genreDM.getId());
    }

    @Test
    void ensureGenreDMStoresCorrectGenreType() {
        // Arrange
        GenreID id = new GenreID("drama");
        String genreType = "Drama";

        // Act
        GenreDM genreDM = new GenreDM(id, genreType);

        // Assert
        assertEquals(genreType, genreDM.getGenreType());
    }

    // =========================================================================
    // Null checks
    // =========================================================================

    @Test
    void ensureGenreDMThrowsExceptionWithNullId() {
        assertThrows(NullPointerException.class, () -> new GenreDM(null, "Drama"));
    }

    @Test
    void ensureGenreDMThrowsExceptionWithNullGenreType() {
        // Arrange
        GenreID id = new GenreID("drama");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> new GenreDM(id, null));
    }
}