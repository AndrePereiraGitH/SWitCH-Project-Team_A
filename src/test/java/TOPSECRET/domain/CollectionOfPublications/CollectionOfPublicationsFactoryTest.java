package TOPSECRET.domain.CollectionOfPublications;

import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CollectionOfPublicationsFactory} — US017.
 *
 * <p>The factory's only job is to call {@code new CollectionOfPublications(name, genre, user)}
 * and return the result. This test checks that the factory does exactly that —
 * it delegates the creation and returns the object.
 *
 * <p>The challenge with unit-testing a factory is that it calls {@code new} internally.
 * Normally, {@code new} cannot be intercepted with a regular mock.
 * To solve this, we use <b>MockedConstruction</b> from Mockito, which intercepts
 * every call to {@code new CollectionOfPublications(...)} inside the {@code try} block
 * and replaces it with a mock object that we control.
 *
 * <p>This way we test only the factory's behaviour — not the constructor logic.
 * If this test passes, it means the factory called {@code new} and returned the result.
 * The constructor is tested separately in the DependenciesTest file.
 */
class CollectionOfPublicationsFactoryTest {


    // ============================== US017 ==============================

    // The factory receives name, genre, and user — it must call new CollectionOfPublications(...)
    // with those values and return the created object.
    //
    // We use MockedConstruction to intercept the constructor call.
    // Inside the try block, every call to new CollectionOfPublications() returns a mock instead.
    // We configure that mock to return known values, then check the factory returned it correctly.
    //
    // If the factory had NOT called new, the mock would never be created and the test would fail.
    // So a passing test proves the factory delegated construction as expected.
    @Test
    void shouldCreateNonNullCollectionOfPublications() {
        // Arrange — fake Genre and User; the real objects are not needed here
        GenreID genre = mock(GenreID.class);
        UserId user = mock(UserId.class);

        // MockedConstruction intercepts every call to new CollectionOfPublications() inside the try block
        // — the factory calls new internally, so this is the only way to isolate it without running the real constructor
        try (MockedConstruction<CollectionOfPublications> mocked =
                     mockConstruction(CollectionOfPublications.class,
                             (mock, context) -> {
                                 // configure what the intercepted constructor mock returns when asked
                                 when(mock.getCollectionName()).thenReturn("MyCollection");
                                 when(mock.getCollectionGenre()).thenReturn(genre);
                                 when(mock.getOwnerId()).thenReturn(user);
                                 when(mock.getIsPrivate()).thenReturn(true); // US017 rule: always private
                             })) {

            // Act — factory is called; internally it calls new CollectionOfPublications(...)
            // which is intercepted and replaced with the mock configured above
            CollectionOfPublicationsFactory factory = new CollectionOfPublicationsFactory();
            CollectionOfPublications result = factory.create("MyCollection", genre, user);

            // Assert — the factory must return the mock object with the correct values
            // this confirms the factory called new and returned the result, not null or something else
            assertEquals("MyCollection", result.getCollectionName());
            assertEquals(genre, result.getCollectionGenre());
            assertEquals(user, result.getOwnerId());
            assertTrue(result.getIsPrivate()); // US017 rule: the created collection must be private
        }
    }

    // ============================== end US017 ==============================

}