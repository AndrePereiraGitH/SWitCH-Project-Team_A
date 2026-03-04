package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AuthorDependenciesTest {

    /**
     * Unit tests for the {@link Author} entity.
     * <p>
     * This test suite validates:
     * <ul>
     *     <li>Correct creation using both primary and secondary constructors.</li>
     *     <li>Validation of mandatory fields and enforcement of domain invariants.</li>
     *     <li>Delegation of profile validation to the {@link Profile} value object.</li>
     *     <li>Correct behavior of getter methods.</li>
     *     <li>Proper implementation of the {@code equals} method,
     *     including null comparison, type comparison, and value-based equality.</li>
     * </ul>
     * <p>
     * The tests ensure that {@code Author} maintains consistency,
     * respects immutability constraints of its composed objects,
     * and correctly defines equality based on its identity fields.
     */
    // Helper method to create a valid Profile for tests
    private Profile validProfile() {
        return new Profile("John", new Country("BRAZIL"), LocalDate.of(1984, 12, 3));
    }

    // ========================================
    // CONSTRUCTOR TESTS - Primary Constructor
    // ========================================

    @Test
    void shouldCreateAuthorWithValidData() {

        // Arrange
        Profile profile = validProfile();

        // Act
        Author author = new Author(profile, "valid bio");

        // Assert
        assertSame(profile, author.getProfile());
        assertEquals("valid bio", author.getBiography());
    }

    @Test
    void shouldThrowExceptionWhenProfileIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Author(null, "valid bio")
        );
        assertEquals("Profile cannot be null", exception.getMessage());    }

    @Test
    void shouldThrowExceptionWhenBiographyIsNullOrBlank() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Author(validProfile(), null));
        assertThrows(IllegalArgumentException.class, () -> new Author(validProfile(), "   "));
    }

    // ========================================
    // CONSTRUCTOR TESTS - Secondary Constructor
    // ========================================

    @Test
    void shouldCreateAuthorUsingSecondaryConstructor() {
        // Act
        Author author = new Author("Jane", new Country("AUSTRIA"), LocalDate.of(1990, 1, 1), "Author bio");

        // Assert
        assertEquals("Jane", author.getProfile().getName());
        assertEquals(new Country("AUSTRIA"), author.getProfile().getCountry());
        assertEquals(LocalDate.of(1990, 1, 1), author.getProfile().getBirthday());
        assertEquals("Author bio", author.getBiography());
    }

    @Test
    void shouldThrowExceptionWhenSecondaryConstructorReceivesInvalidProfileData() {
        // Profile validation should trigger
        assertThrows(IllegalArgumentException.class,
                () -> new Author(null, new Country("AUSTRIA"), LocalDate.of(1990, 1, 1), "bio")
        );

        assertThrows(IllegalArgumentException.class,
                () -> new Author("", new Country("AUSTRIA"), LocalDate.of(1990, 1, 1), "bio")
        );

        assertThrows(IllegalArgumentException.class,
                () -> new Author("Name", null, LocalDate.of(1990, 1, 1), "bio")
        );

        assertThrows(IllegalArgumentException.class,
                () -> new Author("Name", new Country("AUSTRIA"), null, "bio")
        );
    }
    // ========================================
    // EQUALS TESTS - Basic Cases
    // ========================================

    @Test
    void shouldBeTrue_whenSameReference() {
        // Arrange
        Author a1 = new Author(validProfile(), "bio");
        Author a2 = new Author(validProfile(), "bio");

        // Act + Assert
        assertEquals(a1, a2);
    }

    @Test
    void shouldNotBeEqual_whenNull() {
        // Arrange
        Author a1 = new Author(validProfile(), "bio");
        // Act + Assert
        assertNotEquals(null, a1); //  obj == null (return false)
    }

    @Test
    void shouldBeFalse_whenDifferentClass() {
        // Arrange
        Author a1 = new Author(validProfile(), "bio");
        // Act + Assert
        assertNotEquals("not an author", a1); // getClass() != obj.getClass()
    }

    @Test
    void shouldBeEqual_whenSameProfileAndBiography() {
        // Arrange
        Profile p1 = new Profile("John", new Country("BRAZIL"), LocalDate.of(1984, 12, 3));
        Profile p2 = new Profile("John", new Country("BRAZIL"), LocalDate.of(1984, 12, 3));

        Author a1 = new Author(p1, "bio");
        Author a2 = new Author(p2, "bio");

        // Act + Assert
        assertEquals(a1, a2);
    }

    @Test
    void shouldBeFalse_whenDifferentProfile() {
        // Arrange
        Author a1 = new Author(validProfile(), "bio");

        Profile different = new Profile("Mary", new Country("BRAZIL"), LocalDate.of(1984, 12, 3));
        Author a2 = new Author(different, "bio");
        // Act + Assert
        assertNotEquals(a1, a2); // kills "return true" from the last line in equals
    }

    @Test
    void shouldBeTrue_whenSameProfileData() {
        // Arrange
        Author a1 = new Author(validProfile(), "bio");

        Profile sameData = new Profile("John", new Country("BRAZIL"), LocalDate.of(1984, 12, 3));
        Author a2 = new Author(sameData, "bio");
        // Act + Assert
        assertEquals(a1, a2);
    }
    @Test
    void shouldBeFalse_whenSameProfileButDifferentBio() {
        // Arrange
        Profile p1 = new Profile("John Lewis", new Country("BRAZIL"), LocalDate.of(1984, 12, 3));
        Profile p2 = new Profile("John Lewis", new Country("BRAZIL"), LocalDate.of(1984, 12, 3));

        Author a1 = new Author(p1, "bio1");
        Author a2 = new Author(p2, "bio2");
        // Act + Assert
        assertNotEquals(a1, a2);
    }
    @Test
    void shouldReturnTrueWhenAuthorsHaveSameState() {
        // Arrange
        Profile profile1 = new Profile("Ana Silva", new Country("BRAZIL"), LocalDate.of(1990, 1, 1));
        Profile profile2 = new Profile("Ana Silva", new Country("BRAZIL"), LocalDate.of(1990, 1, 1));

        Author author1 = new Author(profile1, "Bio");
        Author author2 = new Author(profile2, "Bio");

        // Act + Assert
        assertEquals(author1, author2);
    }
    @Test
    void shouldReturnFalseWhenAuthorsHaveDifferentState() {
        // Arrange
        Profile profile1 = new Profile("Ana Silva", new Country("BRAZIL"), LocalDate.of(1990, 1, 1));
        Profile profile2 = new Profile("Ana Silva", new Country("BRAZIL"), LocalDate.of(1990, 1, 1));

        Author author1 = new Author(profile1, "Bio A");
        Author author2 = new Author(profile2, "Bio B");

        // Act + Assert
        assertNotEquals(author1, author2);
    }
}