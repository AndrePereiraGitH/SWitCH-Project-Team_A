package TOPSECRET.domain.Author;

import TOPSECRET.domain.ValueObjects.CountryID;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link Author} domain class.
 *
 * <p>This test class verifies the construction and equality behaviour of {@link Author}.
 *
 * <p><b>Test double strategy:</b> {@link CountryID} is replaced with a test double
 * via {@code mock(Country.class)} declared as a field. Because JUnit 5 creates a
 * new instance of the test class before each test method, the double is automatically
 * fresh for every test — no {@code @BeforeEach} or {@code @ExtendWith} required.
 *
 * <p><b>What is tested:</b>
 * <ul>
 *   <li>Constructor — valid data, null/blank name, null country, null date of birth,
 *       null/blank biography</li>
 *   <li>Equals contract — same reference, null, different class, different name,
 *       different country, different date of birth</li>
 * </ul>
 *
 * <p><b>What is NOT tested here:</b> the internal behaviour of {@link CountryID}.
 * That class has its own dedicated test class. The test double exists purely to
 * isolate {@link Author} from that dependency.
 */
class AuthorTest {

    CountryID countryDouble = mock(CountryID.class);

    // ========================================
    // CONSTRUCTOR TESTS
    // ========================================

    @Test
    void should_CreateAuthor_WithValidData() {
        // Act
        Author author = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "valid bio");

        // Assert
        assertEquals("Jane", author.getName());
        assertSame(countryDouble, author.getCountryID());
        assertEquals(LocalDate.of(1990, 1, 1), author.getDateOfBirth());
        assertEquals("valid bio", author.getBiography());
    }

    @Test
    void should_ThrowExceptionWhen_NameIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Author(null, countryDouble, LocalDate.of(1990, 1, 1), "bio")
        );
        assertEquals("Name cannot be null or blank", ex.getMessage());
    }

    @Test
    void should_ThrowExceptionWhenNameIsBlank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Author("   ", countryDouble, LocalDate.of(1990, 1, 1), "bio")
        );
        assertEquals("Name cannot be null or blank", ex.getMessage());
    }

    @Test
    void should_ThrowExceptionWhen_CountryIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Author("Jane", null, LocalDate.of(1990, 1, 1), "bio")
        );
        assertEquals("Country cannot be null", ex.getMessage());
    }

    @Test
    void should_ThrowExceptionWhen_DateOfBirthIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Author("Jane", countryDouble, null, "bio")
        );
        assertEquals("DateOfBirth cannot be null", ex.getMessage());
    }

    @Test
    void should_ThrowExceptionWhen_BiographyIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), null)
        );
        assertEquals("Biography cannot be null or blank", ex.getMessage());
    }

    @Test
    void should_ThrowExceptionWhen_BiographyIsBlank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "   ")
        );
        assertEquals("Biography cannot be null or blank", ex.getMessage());
    }

    // ========================================
    // ID TESTS
    // ========================================

    @Test
    void id_shouldReturnNotNullId() {
        Author author = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio");
        assertNotNull(author.id());
    }

    @Test
    void id_shouldReturnSameId() {
        Author author = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio");
        assertEquals(author.id(), author.id());
    }

    // ========================================
    // EQUALS TESTS
    // ========================================

    @Test
    void shouldBeEqual_whenComparedToItself() {
        // Arrange
        Author author = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio");

        // Act + Assert
        assertEquals(author, author);
    }

    @Test
    void shouldNotBeEqual_whenComparedToNull() {
        // Arrange
        Author author = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio");

        // Act + Assert
        assertFalse(author.equals(null));
    }

    @Test
    void shouldNotBeEqual_whenComparedToDifferentClass() {
        // Arrange
        Author author = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio");

        // Act + Assert
        assertFalse(author.equals("not an author"));
    }

    @Test
    void shouldNotBeEquals_whenComparedDifferentId() {
        // Arrange
        Author author = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio");
        Author author2 = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio");

        // Act + Assert
        assertNotEquals(author.id(), author2.id());

    }

    @Test
    void shouldBeEqual_whenComparedToDifferentInstanceWithSameId() throws Exception {
        // arrange
        Author a1 = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio");
        Author a2 = new Author("John", countryDouble, LocalDate.of(2000, 2, 2), "other bio");

        java.lang.reflect.Field idField = Author.class.getDeclaredField("_id");
        idField.setAccessible(true);
        idField.set(a2, a1.id());

        // act + assert
        assertEquals(a1, a2);
    }

    // ========================================
    // HASHCODE TESTS
    // ========================================

    @Test
    void shouldHaveSameHashCode_whenSameNameCountryAndDateOfBirth() {
        // Arrange
        Author a1 = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio one");

        // Act + Assert
        assertEquals(a1.hashCode(), a1.hashCode());
    }

    @Test
    void shouldHaveDifferentHashCode_whenDifferentName() {
        // Arrange
        Author a1 = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio");
        Author a2 = new Author("John", countryDouble, LocalDate.of(1990, 1, 1), "bio");

        // Act + Assert
        assertNotEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void shouldHaveDifferentHashCode_whenDifferentDateOfBirth() {
        // Arrange
        Author a1 = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio");
        Author a2 = new Author("Jane", countryDouble, LocalDate.of(1995, 6, 15), "bio");

        // Act + Assert
        assertNotEquals(a1.hashCode(), a2.hashCode());
    }

    // ========================================
    // SAME IDENTITY AS TESTS
    // ========================================

    @Test
    void sameAs_shouldHaveSameIdentity_whenAllFieldsAreEqual() {
        // arrange
        CountryID sameCountryDouble = mock(CountryID.class);
        LocalDate dob = LocalDate.of(1990,4, 4);

        Author a1 = new Author("Jane", sameCountryDouble, dob, "bio");
        Author a2 = new Author("Jane", sameCountryDouble, dob, "bio");

        // act + assert
        assertTrue(a1.sameAs(a2));
    }

    @Test
    void sameAs_shouldNotHaveSame_whenComparedToNull() {
        // arrange
        LocalDate dob = LocalDate.of(1990,4, 4);
        Author a1 = new Author("Jane", countryDouble, dob, "bio");

        // act + assert
        assertFalse(a1.sameAs(null));
    }

    @Test
    void sameAs_shouldNotHaveSame_whenComparedToDifferentClass() {
        // arrange
        LocalDate dob = LocalDate.of(1990, 4, 4);
        Author a1 = new Author("Jane", countryDouble, dob, "bio");

        // act + assert
        assertFalse(a1.sameAs("Not an author"));
    }

    @Test
    void sameAs_shouldNotHaveSame_whenNameIsDifferent() {

        // arrange
        Author a1 = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio");
        Author a2 = new Author("John", countryDouble, LocalDate.of(1990, 1, 1), "bio");

        // act + assert
        assertFalse(a1.sameAs(a2));
    }

    @Test
    void sameAs_shouldNotHaveSameIdentity_whenCountryIsDifferent() {
        // arrange
        CountryID otherCountry = mock(CountryID.class);
        Author a1 = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio");
        Author a2 = new Author("Jane", otherCountry, LocalDate.of(1990, 1, 1), "bio");

        // act + assert
        assertFalse(a1.sameAs(a2));
    }

    @Test
    void sameAs_shouldNotHaveSameIdentity_whenDateOfBirthIsDifferent() {
        Author a1 = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio");
        Author a2 = new Author("Jane", countryDouble, LocalDate.of(1995, 6, 15), "bio");

        assertFalse(a1.sameAs(a2));
    }

    @Test
    void sameAs_shouldNotHaveSameIdentity_whenBiographyIsDifferent() {
        Author a1 = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio one");
        Author a2 = new Author("Jane", countryDouble, LocalDate.of(1990, 1, 1), "bio two");

        assertFalse(a1.sameAs(a2));
    }
}
