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

    Profile profile = mock(Profile.class);

    //========================================
    // CONSTRUCTOR TESTS - Primary Constructor
    //========================================

    @Test
    void shouldCreateAuthorWithValidData() {
        //Arrange
        when(profile.getName()).thenReturn("Joana");
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
        assertEquals("Profile cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBiographyIsNullOrBlank() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Author(profile, null));
        assertThrows(IllegalArgumentException.class, () -> new Author(profile, "   "));
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
    void shouldThrowExceptionWhenSecondaryConstructorReceivesInvalidData() {
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
// EQUALS TESTS
// ========================================

    @Test
    void shouldBeEquals_whenSameProfileAndBio() {
        // Arrange
        Author a1 = new Author(profile, "bio");
        Author a2 = new Author(profile, "bio");

        // Act + Assert
        assertEquals(a1, a2);
    }

    @Test
    void shouldNotBeEqual_whenComparedToNull() {
        // Arrange
        Author a1 = new Author(profile, "bio");
        // Act + Assert
        assertNotEquals(null, a1);
    }

    @Test
    void shouldNotBeEquals_whenDifferentClass() {
        // Arrange
        Author a1 = new Author(profile, "bio");
        // Act + Assert
        assertNotEquals("not an author", a1);
    }

    @Test
    void shouldBeEqual_whenSameProfileAndBiography() {
        // Arrange

        Author a1 = new Author(profile, "bio");
        Author a2 = new Author(profile, "bio");

        // Act + Assert
        assertEquals(a1, a2);
    }

    @Test
    void shouldNotBeEqual_whenDifferentProfile() {
        Author a1 = new Author(profile, "bio");

        Profile differentProfile = mock(Profile.class);
        Author a2 = new Author(differentProfile, "bio");

        assertNotEquals(a1, a2);
    }

    @Test
    void shouldBeEquals_whenSameProfileData() {
        // Arrange
        Author a1 = new Author(profile, "bio");

        Author a2 = new Author(profile, "bio");
        // Act + Assert
        assertEquals(a1, a2);
    }

    @Test
    void shouldNotBeEquals_whenSameProfileButDifferentBio() {
        // Arrange

        Author a1 = new Author(profile, "bio1");
        Author a2 = new Author(profile, "bio2");
        // Act + Assert
        assertNotEquals(a1, a2);
    }

    @Test
    void shouldBeEqualsWhenAuthorsHaveSameState() {
        // Arrange
        Author author1 = new Author(profile, "Bio");
        Author author2 = new Author(profile, "Bio");

        // Act + Assert
        assertEquals(author1, author2);
    }

    @Test
    void shouldNotBeEqualsWhenAuthorsHaveDifferentState() {
        // Arrange
        Author author1 = new Author(profile, "Bio A");
        Author author2 = new Author(profile, "Bio B");

        // Act + Assert
        assertNotEquals(author1, author2);
    }
}


