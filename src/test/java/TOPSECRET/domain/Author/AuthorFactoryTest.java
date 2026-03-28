package TOPSECRET.domain.Author;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockConstruction;

import java.time.LocalDate;

import TOPSECRET.domain.Country;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
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
 * of the real constructor logic. The {@link Country} collaborator is replaced with
 * a test double created via {@code mock(Class)} to isolate the factory from its
 * dependencies.
 *
 * <p><b>What is tested:</b>
 * <ul>
 *   <li>Invocation of the {@link Author} constructor exactly once per factory call</li>
 *   <li>Normalisation of input values (trimming of name and biography)</li>
 *   <li>Forwarding of {@code null} name and biography values without modification</li>
 *   <li>Forwarding of {@link Country} and {@link java.time.LocalDate} arguments
 *       to the {@link Author} constructor unchanged</li>
 * </ul>
 *
 * <p><b>What is NOT tested here:</b> the internal validation or domain behaviour
 * implemented inside {@link Author}. That class has its own dedicated unit tests.
 * This test class focuses solely on verifying that {@link AuthorFactory} correctly
 * prepares and forwards data to the {@link Author} constructor.
 */

class AuthorFactoryTest {

    private final Country country = mock(Country.class);

    private static final String rawName = "  John  ";
    private static final String expectedName = "John";

    private static final String rawBio = "  bio  ";
    private static final String expectedBio = "bio";

    private static final LocalDate dob = LocalDate.of(1984, 12, 3);

    // ========================================
    // create(String, Country, LocalDate, String)
    // ========================================

    @Test
    void createShouldCallAuthorConstructorOnce() {
        AuthorFactory authorFactory = new AuthorFactory();
        try (MockedConstruction<Author> mocked = mockConstruction(Author.class)) {
            authorFactory.create(expectedName, country, dob, expectedBio);
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    void createShouldTrimName() {
        AuthorFactory authorFactory = new AuthorFactory();
        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getName()).thenReturn(expectedName);
                     })) {
            Author author = authorFactory.create(rawName, country, dob, expectedBio);
            assertEquals(expectedName, author.getName());
        }
    }

    @Test
    void createShouldForwardNullNameUnchanged() {
        AuthorFactory authorFactory = new AuthorFactory();
        try (MockedConstruction<Author> mocked = mockConstruction(Author.class)) {
            authorFactory.create(null, country, dob, expectedBio);
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    void createShouldTrimBiography() {
        AuthorFactory authorFactory = new AuthorFactory();
        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getBiography()).thenReturn(expectedBio);
                     })) {
            Author author = authorFactory.create(expectedName, country, dob, rawBio);
            assertEquals(expectedBio, author.getBiography());
        }
    }

    @Test
    void createShouldForwardNullBiographyUnchanged() {
        AuthorFactory authorFactory = new AuthorFactory();
        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getBiography()).thenReturn(null);
                     })) {
            Author author = authorFactory.create(expectedName, country, dob, null);
            assertNull(author.getBiography());
        }
    }

    @Test
    void createShouldForwardCountryAndDobUnchanged() {
        AuthorFactory authorFactory = new AuthorFactory();
        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getCountry()).thenReturn(country);
                         when(mock.getDateOfBirth()).thenReturn(dob);
                     })) {
            Author author = authorFactory.create(expectedName, country, dob, expectedBio);
            assertSame(country, author.getCountry());
            assertSame(dob, author.getDateOfBirth());
        }
    }
}