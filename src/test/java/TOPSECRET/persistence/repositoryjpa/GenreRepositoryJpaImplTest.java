package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.persistence.repositoryjpa.assembler.GenreAssembler;
import TOPSECRET.persistence.repositoryjpa.jpa.IGenreRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link GenreRepositoryJpaImpl}.
 * Uses an in-memory H2 database to verify the full persistence flow
 * including assembly between domain and data model.
 */
@DataJpaTest
@Import({GenreRepositoryJpaImpl.class, GenreAssembler.class, GenreFactory.class})
class GenreRepositoryJpaImplIT {

    @Autowired
    private GenreRepositoryJpaImpl genreRepositoryJpaImpl;

    @Autowired
    private IGenreRepositoryJpa genreRepositoryJpa;

    @BeforeEach
    void setUp() {
        genreRepositoryJpa.deleteAll();
    }

    // =========================================================================
    // createAndAdd()
    // =========================================================================

    @Test
    void ensureCreateAndAddReturnsGenre() {
        // Act
        Genre genre = genreRepositoryJpaImpl.createAndAdd("Drama");

        // Assert
        assertNotNull(genre);
        assertEquals("Drama", genre.getGenreType());
    }

    @Test
    void ensureCreateAndAddPersistsGenre() {
        // Act
        genreRepositoryJpaImpl.createAndAdd("Drama");

        // Assert
        assertTrue(genreRepositoryJpa.existsById(new GenreID("drama")));
    }

    @Test
    void ensureCreateAndAddThrowsExceptionWhenGenreAlreadyExists() {
        // Arrange
        genreRepositoryJpaImpl.createAndAdd("Drama");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> genreRepositoryJpaImpl.createAndAdd("Drama"));
    }

    // =========================================================================
    // findByTitle()
    // =========================================================================

    @Test
    void ensureFindByTitleReturnsGenre() {
        // Arrange
        genreRepositoryJpaImpl.createAndAdd("Drama");

        // Act
        Genre found = genreRepositoryJpaImpl.findByTitle("Drama");

        // Assert
        assertNotNull(found);
        assertEquals("Drama", found.getGenreType());
    }

    @Test
    void ensureFindByTitleThrowsExceptionWhenNotFound() {
        assertThrows(IllegalArgumentException.class,
                () -> genreRepositoryJpaImpl.findByTitle("Unknown"));
    }

    // =========================================================================
    // getAllGenres()
    // =========================================================================

    @Test
    void ensureGetAllGenresReturnsAllGenres() {
        // Arrange
        genreRepositoryJpaImpl.createAndAdd("Drama");
        genreRepositoryJpaImpl.createAndAdd("Comedy");

        // Act
        List<Genre> genres = genreRepositoryJpaImpl.getAllGenres();

        // Assert
        assertEquals(2, genres.size());
    }

    @Test
    void ensureGetAllGenresReturnsEmptyListWhenNoGenres() {
        // Act
        List<Genre> genres = genreRepositoryJpaImpl.getAllGenres();

        // Assert
        assertTrue(genres.isEmpty());
    }

    // =========================================================================
    // findGenreById()
    // =========================================================================

    @Test
    void ensureFindGenreByIdReturnsGenre() {
        // Arrange
        genreRepositoryJpaImpl.createAndAdd("Drama");
        GenreID id = new GenreID("drama");

        // Act
        Optional<Genre> found = genreRepositoryJpaImpl.findGenreById(id);

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Drama", found.get().getGenreType());
    }

    @Test
    void ensureFindGenreByIdReturnsEmptyWhenNotFound() {
        // Act
        Optional<Genre> found = genreRepositoryJpaImpl.findGenreById(new GenreID("unknown"));

        // Assert
        assertFalse(found.isPresent());
    }
}