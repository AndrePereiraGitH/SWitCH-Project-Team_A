package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.persistence.repositoryjpa.datamodel.GenreDM;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GenreAssembler}.
 * Verifies that the assembler correctly converts between
 * {@link Genre} domain objects and {@link GenreDM} persistence entities.
 */
@ExtendWith(MockitoExtension.class)
class GenreAssemblerTest {

    @Mock
    private GenreFactory factory;

    @InjectMocks
    private GenreAssembler assembler;

    // =========================================================================
    // toDM()
    // =========================================================================

    @Test
    void ensureToDMReturnsNotNull() {
        // Arrange
        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(new GenreId("drama"));
        when(genre.getGenreType()).thenReturn("Drama");

        // Act
        GenreDM dm = assembler.toDM(genre);

        // Assert
        assertNotNull(dm);
    }

    @Test
    void ensureToDMStoresCorrectId() {
        // Arrange
        GenreId id = new GenreId("drama");
        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(id);
        when(genre.getGenreType()).thenReturn("Drama");

        // Act
        GenreDM dm = assembler.toDM(genre);

        // Assert
        assertEquals(id, dm.getId());
    }

    @Test
    void ensureToDMStoresCorrectGenreType() {
        // Arrange
        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(new GenreId("drama"));
        when(genre.getGenreType()).thenReturn("Drama");

        // Act
        GenreDM dm = assembler.toDM(genre);

        // Assert
        assertEquals("Drama", dm.getGenreType());
    }

    @Test
    void ensureToDMThrowsExceptionWithNullGenre() {
        assertThrows(NullPointerException.class, () -> assembler.toDM(null));
    }

    // =========================================================================
    // toDomain()
    // =========================================================================

    @Test
    void ensureToDomainReturnsNotNull() {
        // Arrange
        GenreId id = new GenreId("drama");
        GenreDM dm = new GenreDM(id, "Drama");
        Genre genre = mock(Genre.class);
        when(factory.create(id, "Drama")).thenReturn(genre);

        // Act
        Genre result = assembler.toDomain(dm);

        // Assert
        assertNotNull(result);
    }

    @Test
    void ensureToDomainReturnsCorrectGenreType() {
        // Arrange
        GenreId id = new GenreId("drama");
        GenreDM dm = new GenreDM(id, "Drama");
        Genre genre = mock(Genre.class);
        when(genre.getGenreType()).thenReturn("Drama");
        when(factory.create(id, "Drama")).thenReturn(genre);

        // Act
        Genre result = assembler.toDomain(dm);

        // Assert
        assertEquals("Drama", result.getGenreType());
    }

    @Test
    void ensureToDomainThrowsExceptionWithNullDM() {
        assertThrows(NullPointerException.class, () -> assembler.toDomain(null));
    }

    // =========================================================================
    // Round trip
    // =========================================================================

    @Test
    void ensureRoundTripProducesEqualGenre() {
        // Arrange
        GenreId id = new GenreId("drama");
        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(id);
        when(genre.getGenreType()).thenReturn("Drama");

        GenreDM dm = assembler.toDM(genre);

        Genre reconstructed = mock(Genre.class);
        when(factory.create(id, "Drama")).thenReturn(reconstructed);
        when(reconstructed.id()).thenReturn(id);

        // Act
        Genre result = assembler.toDomain(dm);

        // Assert
        assertEquals(reconstructed.id(), result.id());
    }
}