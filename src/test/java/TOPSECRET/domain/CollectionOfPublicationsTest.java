package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link CollectionOfPublications} — US017.
 *
 * <p>These tests use <b>mocks</b> for {@link Genre} and {@link User}.
 * This means the tests only check the constructor logic of {@link CollectionOfPublications} itself —
 * not the behaviour of Genre or User. If a test fails here, the bug is in this class.
 *
 * <p>Why mocks and not real objects? The constructor of {@code CollectionOfPublications}
 * receives Genre and User as parameters. By mocking them, we remove any risk of those classes
 * causing a failure — so we can be sure the result comes only from this class.
 *
 * <p>All tests in this file belong to <b>US017</b>, which defines what a collection is
 * and what rules apply when it is created:
 * <ul>
 *   <li>Name must not be null or blank</li>
 *   <li>Genre must not be null</li>
 *   <li>User (owner) must not be null</li>
 *   <li>Collection always starts as private</li>
 *   <li>Collection always starts with no publications</li>
 * </ul>
 */
class CollectionOfPublicationsTest {


    // ============================== US017 ==============================

    // --- Constructor validation ---

    // A collection must always have a name — null is not valid.
    // We pass a mock Genre and mock User so the test only checks the null name check,
    // not whether Genre or User are valid objects.
    @Test
    void shouldThrowExceptionWhenCollectionNameIsNull() {
        // Arrange — mock Genre and User; we only care about the null name here
        Genre mockGenre = mock(Genre.class);
        User mockUser = mock(User.class);

        // Act & Assert — null name must be rejected by the constructor
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications(null, mockGenre, mockUser));
    }

    // A name with only spaces has no real value — it is considered blank.
    // The constructor must also reject this, not just null.
    @Test
    void shouldThrowExceptionWhenCollectionNameIsBlank() {
        // Arrange — mock Genre and User; we only care about the blank name here
        Genre mockGenre = mock(Genre.class);
        User mockUser = mock(User.class);

        // Act & Assert — a name with only spaces must be rejected
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications(" ", mockGenre, mockUser));
    }

    // US017 requires the user to pick a genre — a collection without a genre is not valid.
    // Only User is mocked here; genre is null on purpose to test this specific check.
    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        // Arrange — mock User; genre is null on purpose
        User mockUser = mock(User.class);

        // Act & Assert — null genre must be rejected by the constructor
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications("Favorites", null, mockUser));
    }

    // Every collection must have an owner — without a user it belongs to nobody.
    // Only Genre is mocked here; user is null on purpose to test this specific check.
    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        // Arrange — mock Genre; user is null on purpose
        Genre mockGenre = mock(Genre.class);

        // Act & Assert — null user must be rejected by the constructor
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications("Favorites", mockGenre, null));
    }

    // --- Happy path ---

    // When all three parameters are valid, the constructor must create the collection correctly.
    // We check all five expected values:
    // - name, genre, and owner must match what was passed in
    // - isPrivate must be true (US017 rule: always starts private)
    // - publications must be empty (US017 rule: starts with no publications)
    @Test
    void shouldInitializeInstanceWhenValidParameters() {
        // Arrange — valid name and mocked Genre and User
        String name = "Favorites";
        Genre mockGenre = mock(Genre.class);
        User mockUser = mock(User.class);

        // Act — constructor creates the collection with the given data
        CollectionOfPublications result = new CollectionOfPublications(name, mockGenre, mockUser);

        // Assert — all fields must be set correctly
        assertEquals(name, result.getCollectionName());
        assertEquals(mockGenre, result.getCollectionGenre());
        assertEquals(mockUser, result.getOwner());
        assertTrue(result.getIsPrivate());          // US017 rule: always private at creation
        assertTrue(result.getPublications().isEmpty()); // US017 rule: starts with no publications
    }

    // ============================== end US017 ==============================

}