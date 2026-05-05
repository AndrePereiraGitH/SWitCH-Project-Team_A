package TOPSECRET.domain.Author;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockConstruction;

import java.time.LocalDate;

import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import TOPSECRET.domain.ValueObjects.AuthorId;
/**
 * Unit tests for the {@link AuthorFactory} class.
 *
 * <p>This test class verifies the creation behaviour of {@link AuthorFactory},
 * ensuring that {@link Author} instances are constructed correctly and that
 * the factory performs the expected input normalisation before delegating
 * object creation to the {@link Author} constructor.
 *
 * <p><b>Test double strategy:</b> {@link Author} construction is intercepted using
 * Mockito {@code mockConstruction(Author.class)}. This allows the tests to verify
 * that the factory invokes the {@link Author} constructor while avoiding execution
 * of the real constructor logic. The {@link CountryId} collaborator is replaced with
 * a test double created via {@code mock(Class)} to isolate the factory from its
 * dependencies.
 *
 * <p><b>What is tested:</b>
 * <ul>
 *   <li>Invocation of the {@link Author} constructor exactly once per factory call</li>
 *   <li>Normalisation of input values (trimming of name and biography)</li>
 *   <li>Forwarding of {@code null} name and biography values without modification</li>
 *   <li>Forwarding of {@link CountryId} and {@link java.time.LocalDate} arguments
 *       to the {@link Author} constructor unchanged</li>
 * </ul>
 *
 * <p><b>What is NOT tested here:</b> the internal validation or domain behaviour
 * implemented inside {@link Author}. That class has its own dedicated unit tests.
 * This test class focuses solely on verifying that {@link AuthorFactory} correctly
 * prepares and forwards data to the {@link Author} constructor.
 */

class AuthorFactoryTest {

    private final AuthorId authorId = mock(AuthorId.class);
    private final CountryId countryID = mock(CountryId.class);

    private static final String rawName = "  John  ";
    private static final String expectedName = "John";

    private static final String rawBio = "  bio  ";
    private static final String expectedBio = "bio";

    private static final LocalDate dob = LocalDate.of(1984, 12, 3);

    // ========================================
    // create(String, CountryID, LocalDate, String)
    // ========================================

    @Test
    void createShouldCallAuthorConstructorOnce() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        // Act + Assert
        try (MockedConstruction<Author> mocked = mockConstruction(Author.class)) {
            authorFactory.create(expectedName, countryID, dob, expectedBio);

            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    void createShouldTrimName() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getName()).thenReturn(expectedName);
                     })) {

            // Act
            Author author = authorFactory.create(rawName, countryID, dob, expectedBio);

            // Assert
            assertEquals(expectedName, author.getName());
        }
    }

    @Test
    void createShouldForwardNullNameUnchanged() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        // Act + Assert
        try (MockedConstruction<Author> mocked = mockConstruction(Author.class)) {
            authorFactory.create(null, countryID, dob, expectedBio);

            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    void createShouldTrimBiography() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getBiography()).thenReturn(expectedBio);
                     })) {

            // Act
            Author author = authorFactory.create(expectedName, countryID, dob, rawBio);

            // Assert
            assertEquals(expectedBio, author.getBiography());
        }
    }

    @Test
    void createShouldForwardNullBiographyUnchanged() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getBiography()).thenReturn(null);
                     })) {

            // Act
            Author author = authorFactory.create(expectedName, countryID, dob, null);

            // Assert
            assertNull(author.getBiography());
        }
    }

    @Test
    void createShouldForwardCountryAndDobUnchanged() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getCountryID()).thenReturn(countryID);
                         when(mock.getDateOfBirth()).thenReturn(dob);
                     })) {

            // Act
            Author author = authorFactory.create(expectedName, countryID, dob, expectedBio);

            // Assert
            assertSame(countryID, author.getCountryID());
            assertSame(dob, author.getDateOfBirth());
        }
    }

    // ========================================
    // create(AuthorId, String, CountryID, LocalDate, String)
    // ========================================

    @Test
    void createWithIdShouldCallAuthorConstructorOnce() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        // Act + Assert
        try (MockedConstruction<Author> mocked = mockConstruction(Author.class)) {
            authorFactory.create(authorId, expectedName, countryID, dob, expectedBio);

            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    void createWithIdShouldForwardIdUnchanged() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.id()).thenReturn(authorId);
                     })) {

            // Act
            Author author = authorFactory.create(authorId, expectedName, countryID, dob, expectedBio);

            // Assert
            assertSame(authorId, author.id());
        }
    }

    @Test
    void createWithIdShouldForwardNameUnchanged() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getName()).thenReturn(rawName);
                     })) {

            // Act
            Author author = authorFactory.create(authorId, rawName, countryID, dob, expectedBio);

            // Assert
            assertEquals(rawName, author.getName());
        }
    }

    @Test
    void createWithIdShouldForwardCountryUnchanged() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getCountryID()).thenReturn(countryID);
                     })) {

            // Act
            Author author = authorFactory.create(authorId, expectedName, countryID, dob, expectedBio);

            // Assert
            assertSame(countryID, author.getCountryID());
        }
    }

    @Test
    void createWithIdShouldForwardDateOfBirthUnchanged() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getDateOfBirth()).thenReturn(dob);
                     })) {

            // Act
            Author author = authorFactory.create(authorId, expectedName, countryID, dob, expectedBio);

            // Assert
            assertSame(dob, author.getDateOfBirth());
        }
    }

    @Test
    void createWithIdShouldForwardBiographyUnchanged() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getBiography()).thenReturn(rawBio);
                     })) {

            // Act
            Author author = authorFactory.create(authorId, expectedName, countryID, dob, rawBio);

            // Assert
            assertEquals(rawBio, author.getBiography());
        }
    }

    @Test
    void createWithIdShouldForwardNullNameUnchanged() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getName()).thenReturn(null);
                     })) {

            // Act
            Author author = authorFactory.create(authorId, null, countryID, dob, expectedBio);

            // Assert
            assertNull(author.getName());
        }
    }

    @Test
    void createWithIdShouldForwardNullBiographyUnchanged() {
        // Arrange
        AuthorFactory authorFactory = new AuthorFactory();

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getBiography()).thenReturn(null);
                     })) {

            // Act
            Author author = authorFactory.create(authorId, expectedName, countryID, dob, null);

            // Assert
            assertNull(author.getBiography());
        }
    }

}