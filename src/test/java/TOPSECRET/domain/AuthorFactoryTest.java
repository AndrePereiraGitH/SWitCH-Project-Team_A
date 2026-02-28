package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AuthorFactoryTest {

    /**
     * Unit tests for the {@link AuthorFactory}.
     * <p>
     * This test suite validates:
     * <ul>
     *     <li>Correct creation of {@link Author} instances via the factory.</li>
     *     <li>Trimming of {@code name} and {@code biography} fields.</li>
     *     <li>Null-safe handling of {@code name} and {@code biography} before delegation.</li>
     *     <li>Proper delegation of validation to the {@link Author} constructor.</li>
     * </ul>
     */

    private AuthorFactory factory;

    @BeforeEach
    void setUp() {
        factory = new AuthorFactory();
    }

    // ========================================
    // HAPPY PATH - Valid creation
    // ========================================

    @Test
    void shouldCreateAuthorWithValidData() {
        // Act
        Author author = factory.createAuthor("John", new Country("BRAZIL"), LocalDate.of(1984, 12, 3), "A great author");

        // Assert
        assertNotNull(author);
        assertEquals("John", author.getProfile().getName());
        assertEquals(new Country("BRAZIL"), author.getProfile().getCountry());
        assertEquals(LocalDate.of(1984, 12, 3), author.getProfile().getBirthday());
        assertEquals("A great author", author.getBiography());
    }

    // ========================================
    // TRIMMING BEHAVIOR
    // ========================================

    @Test
    void shouldTrimNameBeforeCreation() {
        Author author = factory.createAuthor("  John  ", new Country("BRAZIL"), LocalDate.of(1984, 12, 3), "bio");

        assertEquals("John", author.getProfile().getName());
    }

    @Test
    void shouldTrimBiographyBeforeCreation() {
        Author author = factory.createAuthor("John", new Country("BRAZIL"), LocalDate.of(1984, 12, 3), "  some bio  ");

        assertEquals("some bio", author.getBiography());
    }

    @Test
    void shouldTrimBothNameAndBiography() {
        Author author = factory.createAuthor("  Jane  ", new Country("AUSTRIA"), LocalDate.of(1990, 1, 1), "  writer  ");

        assertEquals("Jane", author.getProfile().getName());
        assertEquals("writer", author.getBiography());
    }

    // ========================================
    // NULL HANDLING - Before delegation
    // ========================================

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.createAuthor(null, new Country("BRAZIL"), LocalDate.of(1984, 12, 3), "bio")
        );
    }

    @Test
    void shouldThrowExceptionWhenBiographyIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.createAuthor("John", new Country("BRAZIL"), LocalDate.of(1984, 12, 3), null)
        );
    }

    @Test
    void shouldThrowExceptionWhenCountryIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.createAuthor("John", null, LocalDate.of(1984, 12, 3), "bio")
        );
    }

    @Test
    void shouldThrowExceptionWhenDateOfBirthIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.createAuthor("John", new Country("BRAZIL"), null, "bio")
        );
    }

    // ========================================
    // TRIMMING + VALIDATION INTERACTION
    // ========================================

    @Test
    void shouldThrowExceptionWhenNameIsBlankAfterTrim() {
        // "   " trims to "" which is blank — Author constructor should reject it
        assertThrows(IllegalArgumentException.class,
                () -> factory.createAuthor("   ", new Country("BRAZIL"), LocalDate.of(1984, 12, 3), "bio")
        );
    }

    @Test
    void shouldThrowExceptionWhenBiographyIsBlankAfterTrim() {
        // "   " trims to "" which is blank — Author constructor should reject it
        assertThrows(IllegalArgumentException.class,
                () -> factory.createAuthor("John", new Country("BRAZIL"), LocalDate.of(1984, 12, 3), "   ")
        );
    }
}