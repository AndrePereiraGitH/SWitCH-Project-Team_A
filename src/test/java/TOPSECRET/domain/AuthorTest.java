package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {


    // Helper method to create a valid Profile for tests
    private Profile validProfile() {
        return new Profile("John", new Country("BRAZIL"), LocalDate.of(1984, 12, 3));
    }

    @Test
    void shouldCreateAuthorWithValidData() {

        // Arrange
        // Create a valid profile to be used when creating the Author
        Profile profile = validProfile();

        // Act
        Author author = new Author(profile, "valid bio");

        // Assert
        // The Author must be created successfully and keep the same Profile reference
        assertNotNull(author);
        assertSame(profile, author.getProfile());
        assertEquals("valid bio", author.getBiography());
    }

    @Test
    void shouldThrowExceptionWhenProfileIsNull() {
        // Arrange
        Profile profile = null;

        // Act + Assert
        // Creating an Author with a null profile must throw an exception
        assertThrows(IllegalArgumentException.class, () -> new Author(profile, "valid bio"));
    }

    @Test
    void shouldThrowExceptionWhenBiographyIsNullOrBlank() {
        // Arrange

        // Act + Assert
        // Creating an Author with an invalid biography must throw an exception
        assertThrows(IllegalArgumentException.class, () -> new Author(validProfile(), null));
        assertThrows(IllegalArgumentException.class, () -> new Author(validProfile(), "   "));
    }

    @Test
    void shouldBeTrue_whenSameReference() {
        Author a1 = new Author(validProfile(), "bio");
        // Same reference must always be equal (this == obj)
        assertEquals(a1, a1);
    }

    @Test
    void shouldBeFalse_whenNull() {
        Author a1 = new Author(validProfile(), "bio");
        // Comparing with null must return false (obj == null)
        assertNotEquals(a1, null); //  obj == null (return false)
    }

    @Test
    void shouldBeFalse_whenDifferentClass() {
        Author a1 = new Author(validProfile(), "bio");
        // Comparing with a different class must return false (getClass() != obj.getClass())
        assertNotEquals(a1, "not an author"); // getClass() != obj.getClass()
    }

    @Test
    void shouldBeFalse_whenDifferentProfile() {
        Author a1 = new Author(validProfile(), "bio");

        Profile different = new Profile("Mary", new Country("BRAZIL"), LocalDate.of(1984, 12, 3));
        Author a2 = new Author(different, "bio");
        // Must be false so we kill the mutant "always true" in equals
        assertNotEquals(a1, a2); // kills "return true" from the last line in equals
    }

    @Test
    void shouldBeTrue_whenSameProfileData() {
        Author a1 = new Author(validProfile(), "bio");

        Profile sameData = new Profile("John", new Country("BRAZIL"), LocalDate.of(1984, 12, 3));
        Author a2 = new Author(sameData, "bio");

        assertEquals(a1, a2);
    }
    @Test
    void shouldBeFalse_whenSameProfileButDifferentBio() {
        Profile p1 = new Profile("John Lewis", new Country("BRAZIL"), LocalDate.of(1984, 12, 3));
        Profile p2 = new Profile("John Lewis", new Country("BRAZIL"), LocalDate.of(1984, 12, 3));

        Author a1 = new Author(p1, "bio1");
        Author a2 = new Author(p2, "bio2");

        assertNotEquals(a1, a2);
    }

}