package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.persistence.repositoryjpa.assembler.GenreAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.GenreDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IGenreRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GenreRepositoryJpaImpl}.
 * Dependencies are mocked to ensure proper isolation.
 */
@ExtendWith(MockitoExtension.class)
class GenreRepositoryJpaImplTest {

    @Mock
    private IGenreRepositoryJpa genreRepositoryJpa;

    @Mock
    private GenreAssembler assembler;

    @InjectMocks
    private GenreRepositoryJpaImpl genreRepositoryJpaImpl;

    // =========================================================================
    // save()
    // =========================================================================

    @Test
    void ensureSaveReturnsGenre() {
        // Arrange
        Genre genre = mock(Genre.class);
        GenreDM dm = mock(GenreDM.class);
        Genre savedGenre = mock(Genre.class);

        when(assembler.toDM(genre)).thenReturn(dm);
        when(genreRepositoryJpa.save(dm)).thenReturn(dm);
        when(assembler.toDomain(dm)).thenReturn(savedGenre);

        // Act
        Genre result = genreRepositoryJpaImpl.save(genre);

        // Assert
        assertSame(savedGenre, result);
    }

    @Test
    void ensureSaveThrowsExceptionWhenGenreIsNull() {
        assertThrows(NullPointerException.class,
                () -> genreRepositoryJpaImpl.save(null));
    }

    // =========================================================================
    // findByTitle()
    // =========================================================================

    @Test
    void ensureFindByTitleReturnsGenre() {
        // Arrange
        GenreDM dm = mock(GenreDM.class);
        Genre genre = mock(Genre.class);

        when(genreRepositoryJpa.findByGenreType("Drama")).thenReturn(Optional.of(dm));
        when(assembler.toDomain(dm)).thenReturn(genre);

        // Act
        Genre result = genreRepositoryJpaImpl.findByTitle("Drama");

        // Assert
        assertSame(genre, result);
    }

    @Test
    void ensureFindByTitleThrowsExceptionWhenNotFound() {
        // Arrange
        when(genreRepositoryJpa.findByGenreType("Unknown")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> genreRepositoryJpaImpl.findByTitle("Unknown"));
    }

    @Test
    void ensureFindByTitleThrowsExceptionWhenTitleIsNull() {
        assertThrows(NullPointerException.class,
                () -> genreRepositoryJpaImpl.findByTitle(null));
    }

    // =========================================================================
    // getAllGenres()
    // =========================================================================

    @Test
    void ensureGetAllGenresReturnsAllGenres() {
        // Arrange
        GenreDM dm1 = mock(GenreDM.class);
        GenreDM dm2 = mock(GenreDM.class);
        Genre genre1 = mock(Genre.class);
        Genre genre2 = mock(Genre.class);

        when(genreRepositoryJpa.findAll()).thenReturn(List.of(dm1, dm2));
        when(assembler.toDomain(dm1)).thenReturn(genre1);
        when(assembler.toDomain(dm2)).thenReturn(genre2);

        // Act
        List<Genre> genres = genreRepositoryJpaImpl.getAllGenres();

        // Assert
        assertEquals(2, genres.size());
    }

    @Test
    void ensureGetAllGenresReturnsEmptyListWhenNoGenres() {
        // Arrange
        when(genreRepositoryJpa.findAll()).thenReturn(List.of());

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
        GenreId id = new GenreId("drama");
        GenreDM dm = mock(GenreDM.class);
        Genre genre = mock(Genre.class);

        when(genreRepositoryJpa.findById(id)).thenReturn(Optional.of(dm));
        when(assembler.toDomain(dm)).thenReturn(genre);

        // Act
        Optional<Genre> result = genreRepositoryJpaImpl.findGenreById(id);

        // Assert
        assertTrue(result.isPresent());
        assertSame(genre, result.get());
    }

    @Test
    void ensureFindGenreByIdReturnsEmptyWhenNotFound() {
        // Arrange
        GenreId id = new GenreId("unknown");
        when(genreRepositoryJpa.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Genre> result = genreRepositoryJpaImpl.findGenreById(id);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void ensureFindGenreByIdThrowsExceptionWhenIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> genreRepositoryJpaImpl.findGenreById(null));
    }


    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowNullPointerException_whenRepositoryIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> new GenreRepositoryJpaImpl(null, assembler));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenAssemblerIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> new GenreRepositoryJpaImpl(genreRepositoryJpa, null));
    }
}