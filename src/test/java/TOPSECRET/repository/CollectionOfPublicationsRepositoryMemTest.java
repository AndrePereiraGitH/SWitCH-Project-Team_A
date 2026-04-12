package TOPSECRET.repository;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CollectionOfPublicationsRepositoryMem} — US017.
 *
 * <p>Verifies that the repository correctly stores collections, retrieves them by owner,
 * and rejects invalid arguments.
 */
class CollectionOfPublicationsRepositoryMemTest {

    @Test
    void shouldThrowExceptionWhenFactoryIsNull_US017() {
        assertThrows(IllegalArgumentException.class,
                () -> new CollectionOfPublicationsRepositoryMem(null));
    }

    /**
     * Ensures exception is thrown when user is null.
     */
    @Test
    void shouldThrowExceptionWhenUserIsNull() {

        // ARRANGE
        CollectionOfPublicationsRepositoryMem sut =
                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.getUserCollections(null));
    }


    /**
     * Ensures user collections are retrieved correctly.
     */
    @Test
    void shouldReturnCollectionsOwnedByUser() throws Exception {

        // ARRANGE
        CollectionOfPublicationsRepositoryMem sut =
                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));

        UserId user = mock(UserId.class);
        UserId otherUser = mock(UserId.class);

        CollectionOfPublications collection1 = mock(CollectionOfPublications.class);
        CollectionOfPublications collection2 = mock(CollectionOfPublications.class);

        when(collection1.getOwnerId()).thenReturn(user);
        when(collection2.getOwnerId()).thenReturn(otherUser);

        // the list is private with no setter, so reflection is the only way to pre-populate it in a unit test
        Field field = CollectionOfPublicationsRepositoryMem.class
                .getDeclaredField("_collectionOfPublicationsList");
        field.setAccessible(true);

        List<CollectionOfPublications> list =
                (List<CollectionOfPublications>) field.get(sut);

        list.add(collection1);
        list.add(collection2);

        // ACT
        List<CollectionOfPublications> result =
                sut.getUserCollections(user);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(collection1, result.get(0));
    }


    // ============================== createAndAdd() ==============================

    @Test
    void createAndAdd_shouldThrowWhenUserIdIsNull() {
        // ARRANGE
        CollectionOfPublicationsRepositoryMem sut =
                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.createAndAdd(null, "Favorites", mock(GenreID.class)));
    }

    @Test
    void createAndAdd_shouldThrowWhenNameIsNull() {
        // ARRANGE
        CollectionOfPublicationsRepositoryMem sut =
                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.createAndAdd(mock(UserId.class), null, mock(GenreID.class)));
    }

    @Test
    void createAndAdd_shouldThrowWhenGenreIdIsNull() {
        // ARRANGE
        CollectionOfPublicationsRepositoryMem sut =
                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.createAndAdd(mock(UserId.class), "Favorites", null));
    }

    @Test
    void createAndAdd_shouldReturnCreatedCollection() {
        // ARRANGE
        UserId userId = mock(UserId.class);
        GenreID genreId = mock(GenreID.class);
        CollectionOfPublications expected = mock(CollectionOfPublications.class);

        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        when(factory.create("Favorites", genreId, userId)).thenReturn(expected);

        CollectionOfPublicationsRepositoryMem sut = new CollectionOfPublicationsRepositoryMem(factory);

        // ACT
        CollectionOfPublications result = sut.createAndAdd(userId, "Favorites", genreId);

        // ASSERT
        assertEquals(expected, result);
    }

    @Test
    void createAndAdd_shouldThrowWhenDuplicateCollectionExists() {
        // ARRANGE
        UserId userId = mock(UserId.class);
        GenreID genreId = mock(GenreID.class);

        CollectionOfPublications existing = mock(CollectionOfPublications.class);
        when(existing.getOwnerId()).thenReturn(userId);
        when(existing.getCollectionName()).thenReturn("Favorites");
        when(existing.getCollectionGenre()).thenReturn(genreId);

        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        when(factory.create("Favorites", genreId, userId)).thenReturn(existing);

        CollectionOfPublicationsRepositoryMem sut = new CollectionOfPublicationsRepositoryMem(factory);
        sut.createAndAdd(userId, "Favorites", genreId); // first call succeeds

        // ACT + ASSERT — second call with same userId, name, genreId → duplicate
        assertThrows(IllegalArgumentException.class,
                () -> sut.createAndAdd(userId, "Favorites", genreId));
    }

    @Test
    void createAndAdd_shouldThrowWhenDuplicateNameDiffersOnlyInCase() {
        // ARRANGE
        UserId userId = mock(UserId.class);
        GenreID genreId = mock(GenreID.class);

        CollectionOfPublications existing = mock(CollectionOfPublications.class);
        when(existing.getOwnerId()).thenReturn(userId);
        when(existing.getCollectionName()).thenReturn("Favorites");
        when(existing.getCollectionGenre()).thenReturn(genreId);

        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        when(factory.create("Favorites", genreId, userId)).thenReturn(existing);

        CollectionOfPublicationsRepositoryMem sut = new CollectionOfPublicationsRepositoryMem(factory);
        sut.createAndAdd(userId, "Favorites", genreId); // stored as "Favorites"

        // ACT + ASSERT — "favorites" (lowercase) is considered a duplicate
        assertThrows(IllegalArgumentException.class,
                () -> sut.createAndAdd(userId, "favorites", genreId));
    }

    @Test
    void createAndAdd_shouldAllowSameNameForDifferentUsers() {
        // ARRANGE
        UserId user1 = mock(UserId.class);
        UserId user2 = mock(UserId.class);
        GenreID genreId = mock(GenreID.class);

        CollectionOfPublications collection1 = mock(CollectionOfPublications.class);
        when(collection1.getOwnerId()).thenReturn(user1);
        when(collection1.getCollectionName()).thenReturn("Favorites");
        when(collection1.getCollectionGenre()).thenReturn(genreId);

        CollectionOfPublications collection2 = mock(CollectionOfPublications.class);

        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        when(factory.create("Favorites", genreId, user1)).thenReturn(collection1);
        when(factory.create("Favorites", genreId, user2)).thenReturn(collection2);

        CollectionOfPublicationsRepositoryMem sut = new CollectionOfPublicationsRepositoryMem(factory);
        sut.createAndAdd(user1, "Favorites", genreId);

        // ACT — user2 can have a collection with the same name
        CollectionOfPublications result = sut.createAndAdd(user2, "Favorites", genreId);

        // ASSERT
        assertEquals(collection2, result);
    }

}