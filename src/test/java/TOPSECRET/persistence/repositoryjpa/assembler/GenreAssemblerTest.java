package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.persistence.repositoryjpa.datamodel.GenreDM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GenreAssembler}.
 * Verifies that the assembler correctly converts between
 * {@link Genre} domain objects and {@link GenreDM} persistence entities.
 */
class GenreAssemblerTest {

    private GenreAssembler assembler;
    private GenreFactory factory;

    @BeforeEach
    void setUp() {
        factory = new GenreFactory();
        assembler = new GenreAssembler(factory);
    }

    // =========================================================================
    // toDM()
    // =========================================================================

    @Test
    void ensureToDMReturnsNotNull() {
        // Arrange
        Genre genre = factory.create("Drama");

        // Act
        GenreDM dm = assembler.toDM(genre);

        // Assert
        assertNotNull(dm);
    }

    @Test
    void ensureToDMStoresCorrectId() {
        // Arrange
        Genre genre = factory.create("Drama");

        // Act
        GenreDM dm = assembler.toDM(genre);

        // Assert
        assertEquals(genre.id(), dm.getId());
    }

    @Test
    void ensureToDMStoresCorrectGenreType() {
        // Arrange
        Genre genre = factory.create("Drama");

        // Act
        GenreDM dm = assembler.toDM(genre);

        // Assert
        assertEquals(genre.getGenreType(), dm.getGenreType());
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
        GenreDM dm = new GenreDM(new GenreID("drama"), "Drama");

        // Act
        Genre genre = assembler.toDomain(dm);

        // Assert
        assertNotNull(genre);
    }

    @Test
    void ensureToDomainReturnsCorrectGenreType() {
        // Arrange
        GenreDM dm = new GenreDM(new GenreID("drama"), "Drama");

        // Act
        Genre genre = assembler.toDomain(dm);

        // Assert
        assertEquals("Drama", genre.getGenreType());
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
        Genre original = factory.create("Drama");

        // Act
        GenreDM dm = assembler.toDM(original);
        Genre reconstructed = assembler.toDomain(dm);

        // Assert
        assertEquals(original, reconstructed);
    }
}