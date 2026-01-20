package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {

    @Test
    void shouldCreateAuthorWithValidData() {
        // Arrange
        Profile profile = new Profile("John Lewis", Countries.BRAZIL, LocalDate.of(1984, 12, 3));

        // Act
        Author author = new Author(profile, "valid bio");

        // Assert
        assertNotNull(author);
        assertSame(profile, author.getProfile());
        assertEquals("valid bio", author.getBiography());
    }

    @Test
    void shouldThrowExceptionWhenProfileIsNull() {
        // Arrange
        Profile profile = null;

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Author(profile, "valid bio"));
    }

    @Test
    void shouldThrowExceptionWhenBiographyIsNullOrBlank() {
        // Arrange
        Profile profile = new Profile("John Lewis", Countries.BRAZIL, LocalDate.of(1984, 12, 3));

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Author(profile, null));
        assertThrows(IllegalArgumentException.class, () -> new Author(profile, "   "));
    }
}