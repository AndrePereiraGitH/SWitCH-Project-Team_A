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
        GenreId genreId = mock(GenreId.class);
        when(genre.id()).thenReturn(genreId);
        when(genre.getGenreType()).thenReturn("Drama");

        // Act
        GenreDM dm = assembler.toDM(genre);

        // Assert
        assertNotNull(dm);
    }

    @Test
    void ensureToDMStoresCorrectId() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(genreId);
        when(genre.getGenreType()).thenReturn("Drama");

        // Act
        GenreDM dm = assembler.toDM(genre);

        // Assert
        assertEquals(genreId, dm.getId());
    }

    @Test
    void ensureToDMStoresCorrectGenreType() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(genreId);
        when(genre.getGenreType()).thenReturn("Drama");

        // Act
        GenreDM dm = assembler.toDM(genre);

        // Assert
        assertEquals("Drama", dm.getGenreType());
    }

    @Test
    void ensureToDMThrowsExceptionWithNullGenre() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> assembler.toDM(null));
    }

    // =========================================================================
    // toDomain()
    // =========================================================================

    @Test
    void ensureToDomainReturnsNotNull() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        GenreDM dm = mock(GenreDM.class);
        Genre genre = mock(Genre.class);
        when(dm.getId()).thenReturn(genreId);
        when(dm.getGenreType()).thenReturn("Drama");
        when(factory.create(genreId, "Drama")).thenReturn(genre);

        // Act
        Genre result = assembler.toDomain(dm);

        // Assert
        assertNotNull(result);
    }

    @Test
    void ensureToDomainReturnsCorrectGenreType() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        GenreDM dm = mock(GenreDM.class);
        Genre genre = mock(Genre.class);
        when(dm.getId()).thenReturn(genreId);
        when(dm.getGenreType()).thenReturn("Drama");
        when(genre.getGenreType()).thenReturn("Drama");
        when(factory.create(genreId, "Drama")).thenReturn(genre);

        // Act
        Genre result = assembler.toDomain(dm);

        // Assert
        assertEquals("Drama", result.getGenreType());
    }

    @Test
    void ensureToDomainThrowsExceptionWithNullDM() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> assembler.toDomain(null));
    }

    // =========================================================================
    // Round trip
    // =========================================================================

    @Test
    void ensureRoundTripProducesEqualGenre() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(genreId);
        when(genre.getGenreType()).thenReturn("Drama");

        GenreDM dm = assembler.toDM(genre);

        Genre reconstructed = mock(Genre.class);
        when(factory.create(genreId, "Drama")).thenReturn(reconstructed);
        when(reconstructed.id()).thenReturn(genreId);

        // Act
        Genre result = assembler.toDomain(dm);

        // Assert
        assertEquals(reconstructed.id(), result.id());
    }
}