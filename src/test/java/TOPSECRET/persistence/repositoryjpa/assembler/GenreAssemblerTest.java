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

import java.util.List;

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

    // =========================================================================
    // toDomainList()
    // =========================================================================

    @Test
    void ensureToDomainListReturnsListWithCorrectSize() {

        // Arrange
        GenreDM dm1 = mock(GenreDM.class);
        GenreDM dm2 = mock(GenreDM.class);

        GenreId id1 = mock(GenreId.class);
        GenreId id2 = mock(GenreId.class);

        Genre genre1 = mock(Genre.class);
        Genre genre2 = mock(Genre.class);

        when(dm1.getId()).thenReturn(id1);
        when(dm1.getGenreType()).thenReturn("Drama");

        when(dm2.getId()).thenReturn(id2);
        when(dm2.getGenreType()).thenReturn("Horror");

        when(factory.create(id1, "Drama")).thenReturn(genre1);
        when(factory.create(id2, "Horror")).thenReturn(genre2);

        List<GenreDM> dmList = List.of(dm1, dm2);

        // Act
        List<Genre> result = assembler.toDomainList(dmList);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void ensureToDomainListConvertsFirstElement() {

        // Arrange
        GenreDM dm = mock(GenreDM.class);
        GenreId id = mock(GenreId.class);
        Genre genre = mock(Genre.class);

        when(dm.getId()).thenReturn(id);
        when(dm.getGenreType()).thenReturn("Drama");

        when(factory.create(id, "Drama")).thenReturn(genre);

        List<GenreDM> dmList = List.of(dm);

        // Act
        Genre result = assembler.toDomainList(dmList).get(0);

        // Assert
        assertSame(genre, result);
    }

    @Test
    void ensureToDomainListUsesCorrectGenreType() {

        // Arrange
        GenreDM dm = mock(GenreDM.class);
        GenreId id = mock(GenreId.class);
        Genre genre = mock(Genre.class);

        when(dm.getId()).thenReturn(id);
        when(dm.getGenreType()).thenReturn("Drama");

        when(genre.getGenreType()).thenReturn("Drama");
        when(factory.create(id, "Drama")).thenReturn(genre);

        List<GenreDM> dmList = List.of(dm);

        // Act
        Genre result = assembler.toDomainList(dmList).get(0);

        // Assert
        assertEquals("Drama", result.getGenreType());
    }

    @Test
    void ensureToDomainListReturnsEmptyListWhenInputIsEmpty() {

        // Arrange
        List<GenreDM> dmList = List.of();

        // Act
        List<Genre> result = assembler.toDomainList(dmList);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void ensureToDomainListThrowsExceptionWhenInputIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> assembler.toDomainList(null));
    }
}