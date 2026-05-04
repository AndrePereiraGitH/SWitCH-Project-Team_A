package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.persistence.repositoryjpa.datamodel.GenreDM;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link IGenreRepositoryJpa}.
 * Uses an in-memory H2 database to verify Spring Data JPA queries.
 */
@DataJpaTest
class IGenreRepositoryJpaTest {

    @Autowired
    private IGenreRepositoryJpa genreRepositoryJpa;

    // =========================================================================
    // save() + findById()
    // =========================================================================

    @Test
    void ensureSaveAndFindByIdReturnsGenre() {
        // Arrange
        GenreId id = new GenreId("drama");
        GenreDM dm = new GenreDM(id, "Drama");

        // Act
        genreRepositoryJpa.save(dm);
        Optional<GenreDM> found = genreRepositoryJpa.findById(id);

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Drama", found.get().getGenreType());
    }

    @Test
    void ensureFindByIdReturnsEmptyWhenNotFound() {
        // Arrange
        GenreId id = new GenreId("unknown");

        // Act
        Optional<GenreDM> found = genreRepositoryJpa.findById(id);

        // Assert
        assertFalse(found.isPresent());
    }

    // =========================================================================
    // findByGenreType()
    // =========================================================================

    @Test
    void ensureFindByGenreTypeReturnsGenre() {
        // Arrange
        GenreDM dm = new GenreDM(new GenreId("drama"), "Drama");
        genreRepositoryJpa.save(dm);

        // Act
        GenreDM found = genreRepositoryJpa.findByGenreType("Drama").orElse(null);

        // Assert
        assertNotNull(found);
        assertEquals("Drama", found.getGenreType());
    }

    @Test
    void ensureFindByGenreTypeReturnsNullWhenNotFound() {
        // Act
        GenreDM found = genreRepositoryJpa.findByGenreType("Unknown").orElse(null);

        // Assert
        assertNull(found);
    }

    // =========================================================================
    // findAll()
    // =========================================================================

    @Test
    void ensureFindAllReturnsAllSavedGenres() {
        // Arrange
        genreRepositoryJpa.save(new GenreDM(new GenreId("drama"), "Drama"));
        genreRepositoryJpa.save(new GenreDM(new GenreId("comedy"), "Comedy"));

        // Act
        List<GenreDM> all = genreRepositoryJpa.findAll();

        // Assert
        assertEquals(2, all.size());
    }

    @Test
    void ensureFindAllReturnsEmptyWhenNoGenresSaved() {
        // Act
        List<GenreDM> all = genreRepositoryJpa.findAll();

        // Assert
        assertTrue(all.isEmpty());
    }

    // =========================================================================
    // existsById()
    // =========================================================================

    @Test
    void ensureExistsByIdReturnsTrueWhenGenreExists() {
        // Arrange
        GenreId id = new GenreId("drama");
        genreRepositoryJpa.save(new GenreDM(id, "Drama"));

        // Act & Assert
        assertTrue(genreRepositoryJpa.existsById(id));
    }

    @Test
    void ensureExistsByIdReturnsFalseWhenGenreDoesNotExist() {
        assertFalse(genreRepositoryJpa.existsById(new GenreId("unknown")));
    }
}