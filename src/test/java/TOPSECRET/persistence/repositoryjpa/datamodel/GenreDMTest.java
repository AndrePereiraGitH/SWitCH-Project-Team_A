package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.GenreId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GenreDM}.
 * Verifies that the data model correctly stores genre data
 * and throws exceptions for invalid inputs.
 */
@ExtendWith(MockitoExtension.class)
class GenreDMTest {

    @Mock
    private GenreId genreId;

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void ensureGenreDMIsCreatedSuccessfully() {
        // Act
        GenreDM genreDM = new GenreDM(genreId, "Drama");

        // Assert
        assertNotNull(genreDM);
    }

    @Test
    void ensureGenreDMStoresCorrectId() {
        // Act
        GenreDM genreDM = new GenreDM(genreId, "Drama");

        // Assert
        assertEquals(genreId, genreDM.getId());
    }

    @Test
    void ensureGenreDMStoresCorrectGenreType() {
        // Act
        GenreDM genreDM = new GenreDM(genreId, "Drama");

        // Assert
        assertEquals("Drama", genreDM.getGenreType());
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
        assertThrows(NullPointerException.class, () -> new GenreDM(genreId, null));
    }
}