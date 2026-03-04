package TOPSECRET.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockConstruction;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

/**
 * Unit tests for the {@link AuthorFactory} class.
 *
 * <p>This test class verifies the creation behaviour of {@link AuthorFactory},
 * ensuring that {@link Author} instances are constructed correctly and that
 * the factory performs the expected input normalization before delegating
 * object creation to the {@link Author} constructors.
 *
 * <p><b>Test double strategy:</b> {@link Author} construction is intercepted using
 * Mockito {@code mockConstruction(Author.class)}. This allows the tests to verify
 * that the factory invokes the {@link Author} constructor while avoiding execution
 * of the real constructor logic. Collaborators such as {@link Country} and
 * {@link Profile} are replaced with Mockito mocks created via
 * {@code mock(Class)} to isolate the factory from its dependencies.
 *
 * <p><b>What is tested:</b>
 * <ul>
 *   <li>Invocation of the {@link Author} constructor when creating authors</li>
 *   <li>Normalization of input values (trimming of name and biography)</li>
 *   <li>Forwarding of arguments such as {@link Country}, {@link Profile},
 *       and {@link java.time.LocalDate} to the {@link Author} constructor</li>
 *   <li>Behaviour of both factory methods:
 *       {@link AuthorFactory#create(String, Country, java.time.LocalDate, String)}
 *       and
 *       {@link AuthorFactory#createAuthorWithExistingProfile(Profile, String)}</li>
 * </ul>
 *
 * <p><b>What is NOT tested here:</b> the internal validation or domain behaviour
 * implemented inside {@link Author} or {@link Profile}. Those classes have their
 * own dedicated unit tests. This test class focuses solely on verifying that the
 * {@link AuthorFactory} correctly prepares and forwards data to the {@link Author}
 * constructors.
 */

class AuthorFactoryTest {

    private final AuthorFactory factory = new AuthorFactory();
    private final Country country = mock(Country.class);
    private final Profile profile = mock(Profile.class);


    // =====================
    // Constants
    // =====================

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

        // ARRANGE
        try (MockedConstruction<Author> mocked = mockConstruction(Author.class)) {

            // ACT
            factory.create(expectedName, country, dob, expectedBio);

            // ASSERT
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    void createShouldTrimName() {

        // ARRANGE
        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(profile.getName()).thenReturn(expectedName);

                         when(mock.getProfile()).thenReturn(profile);
                     })) {

            // ACT
            Author author = factory.create(rawName, country, dob, expectedBio);

            // ASSERT
            assertEquals(expectedName, author.getProfile().getName());
        }
    }

    @Test
    void createShouldForwardNullNameUnchanged() {

        // ARRANGE

        try (MockedConstruction<Author> mocked = mockConstruction(Author.class)) {

            // ACT
            factory.create(null, country, dob, expectedBio);

            // ASSERT
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    void createShouldTrimBiography() {

        // ARRANGE
        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getBiography()).thenReturn(expectedBio);
                     })) {

            // ACT
            Author author = factory.create(expectedName, country, dob, rawBio);

            // ASSERT
            assertEquals(expectedBio, author.getBiography());
        }
    }

    @Test
    void createShouldForwardNullBiographyUnchanged() {

        // ARRANGE

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getBiography()).thenReturn(null);
                     })) {

            // ACT
            Author author = factory.create(expectedName, country, dob, null);

            // ASSERT
            assertNull(author.getBiography());
        }
    }

    @Test
    void createShouldForwardCountryAndDobUnchanged() {

        // ARRANGE
        final Country expectedCountry = country;
        final LocalDate expectedDob = dob;

        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {

                         Profile profile = mock(Profile.class);
                         when(profile.getCountry()).thenReturn(expectedCountry);
                         when(profile.getBirthday()).thenReturn(expectedDob);

                         when(mock.getProfile()).thenReturn(profile);
                     })) {

            // ACT
            Author author = factory.create(expectedName, expectedCountry, expectedDob, expectedBio);

            // ASSERT
            assertSame(expectedCountry, author.getProfile().getCountry());
            assertSame(expectedDob, author.getProfile().getBirthday());
        }
    }

    // ========================================
    // createAuthorWithExistingProfile(Profile, String)
    // ========================================

    @Test
    void createAuthorWithExistingProfile_shouldCallAuthorConstructorOnce() {

        // ARRANGE
        try (MockedConstruction<Author> mocked = mockConstruction(Author.class)) {

            // ACT
            factory.createAuthorWithExistingProfile(profile, expectedBio);

            // ASSERT
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    void createAuthorWithExistingProfile_shouldTrimBiography() {

        // ARRANGE
        try (MockedConstruction<Author> mocked =
                     mockConstruction(Author.class, (mock, context) -> {
                         when(mock.getBiography()).thenReturn(expectedBio);
                         when(mock.getProfile()).thenReturn(profile);
                     })) {

            // ACT
            Author author = factory.createAuthorWithExistingProfile(profile, rawBio);

            // ASSERT
            assertSame(profile, author.getProfile());
            assertEquals(expectedBio, author.getBiography());
        }
    }
}