package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link Author} domain class.
 *
 * <p>This test class verifies the construction and equality behaviour of {@link Author},
 * using a Mockito mock of {@link Profile} to isolate the {@link Author} from its collaborator.
 *
 * <p><b>Test double strategy:</b> {@code Profile} is mocked via
 * {@code mock(Profile.class)} declared as a field. Because JUnit 5 creates a new
 * instance of the test class before each test method, the mock is automatically
 * fresh for every test — no {@code @BeforeEach} or {@code @ExtendWith} required.
 *
 * <p><b>What is tested:</b>
 * <ul>
 *   <li>Primary constructor — valid data, null profile, null/blank biography</li>
 *   <li>Secondary constructor — valid data, invalid arguments</li>
 *   <li>Equals contract — same reference, null, different class, different profile, different biography</li>
 * </ul>
 *
 * <p><b>What is NOT tested here:</b> the internal behaviour of {@link Profile}.
 * Profile has its own dedicated test class. The mock here exists purely to
 * isolate {@link Author} from that dependency.
 */

class AuthorTest {

    Profile profileDouble = mock(Profile.class);
    Country countryDouble = mock(Country.class);

    //========================================
    // CONSTRUCTOR TESTS - Primary Constructor
    //========================================

    @Test
    void shouldCreateAuthorWithValidData() {
        //Arrange
        when(profileDouble.getName()).thenReturn("Joana");
        // Act
        Author author = new Author(profileDouble, "valid bio");

        // Assert
        assertSame(profileDouble, author.getProfile());
        assertEquals("valid bio", author.getBiography());
    }

    @Test
    void shouldThrowExceptionWhenProfileIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Author(null, "valid bio")
        );
        assertEquals("Profile cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBiographyIsNullOrBlank() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Author(profileDouble, null));
        assertThrows(IllegalArgumentException.class, () -> new Author(profileDouble, "   "));
    }

// ========================================
// CONSTRUCTOR TESTS - Secondary Constructor
// ========================================

    @Test
    void shouldCreateAuthorUsingSecondaryConstructor() {
        // Act
        Author author = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "Author bio");

        // Assert
        assertEquals("Jane", author.getProfile().getName());
        assertEquals(countryDouble, author.getProfile().getCountry());
        assertEquals(LocalDate.of(1990, 1, 1), author.getProfile().getBirthday());
        assertEquals("Author bio", author.getBiography());
    }

    @Test
    void shouldThrowExceptionWhenSecondaryConstructorReceivesInvalidData() {
        // Profile validation should trigger
        assertThrows(IllegalArgumentException.class,
                () -> new Author(null, countryDouble, LocalDate.of(1990, 1, 1), "bio")
        );

        assertThrows(IllegalArgumentException.class,
                () -> new Author("", countryDouble, LocalDate.of(1990, 1, 1), "bio")
        );

        assertThrows(IllegalArgumentException.class,
                () -> new Author("Name", null, LocalDate.of(1990, 1, 1), "bio")
        );

        assertThrows(IllegalArgumentException.class,
                () -> new Author("Name", countryDouble, null, "bio")
        );
    }

// ========================================
// EQUALS TESTS
// ========================================

    @Test
    void shouldBeEqual_whenComparedToItself() {
        // Arrange
        Author author = new Author(profileDouble, "bio");

        // Act
        boolean result = author.equals(author);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldBeEqual_whenSameProfileAndBio() {
        // Arrange
        Author a1 = new Author(profileDouble, "bio");
        Author a2 = new Author(profileDouble, "bio");

        // Act
        boolean result = a1.equals(a2);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldNotBeEqual_whenComparedToNull() {
        // Arrange
        Author a1 = new Author(profileDouble, "bio");

        // Act
        boolean result = a1.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldNotBeEqual_whenComparedToDifferentClass() {
        // Arrange
        Author a1 = new Author(profileDouble, "bio");

        // Act
        boolean result = a1.equals("not an author");

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldNotBeEqual_whenDifferentProfile() {
        // Arrange
        Profile differentProfile = mock(Profile.class);
        Author a1 = new Author(profileDouble, "bio");
        Author a2 = new Author(differentProfile, "bio");

        // Act
        boolean result = a1.equals(a2);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldNotBeEqual_whenSameProfileButDifferentBio() {
        // Arrange
        Author a1 = new Author(profileDouble, "bio1");
        Author a2 = new Author(profileDouble, "bio2");

        // Act
        boolean result = a1.equals(a2);

        // Assert
        assertFalse(result);
    }
}


