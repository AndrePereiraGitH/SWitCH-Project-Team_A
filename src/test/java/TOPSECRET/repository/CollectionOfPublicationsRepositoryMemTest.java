package TOPSECRET.repository;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        when(collection1.getOwner()).thenReturn(user);
        when(collection2.getOwner()).thenReturn(otherUser);

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

    @Test
    void getAllCollectionOfPublicationsByUser_shouldThrowWhenOwnerIsNull() {
        CollectionOfPublicationsFactory factoryDouble = mock(CollectionOfPublicationsFactory.class);


        // Arrange
        CollectionOfPublicationsRepositoryMem repo = new CollectionOfPublicationsRepositoryMem(factoryDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.getAllCollectionOfPublicationsByUser(null));
    }

    @Test
    void getAllCollectionOfPublicationsByUser_shouldReturnEmptyListWhenNoCollections() {
        CollectionOfPublicationsFactory factoryDouble = mock(CollectionOfPublicationsFactory.class);

        // Arrange
        CollectionOfPublicationsRepositoryMem repo = new CollectionOfPublicationsRepositoryMem(factoryDouble);
        UserId ownerDouble = mock(UserId.class);

        // Act
        List<CollectionOfPublications> result = repo.getAllCollectionOfPublicationsByUser(ownerDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    /**
     * Verifies that when the repository contains collections belonging to different owners,
     * only the collections owned by the given user are returned.
     * Two collections are added via the factory — one for each owner —
     * and only the one matching the queried owner should appear in the result.
     */
    @Test
    void getAllCollectionOfPublicationsByUser_shouldReturnOnlyCollectionsOfGivenOwner() {
        CollectionOfPublicationsFactory factoryDouble = mock(CollectionOfPublicationsFactory.class);


        // Arrange
        CollectionOfPublicationsRepositoryMem repo = new CollectionOfPublicationsRepositoryMem(factoryDouble);

        UserId ownerDouble = mock(UserId.class);
        UserId otherOwnerDouble = mock(UserId.class);

        CollectionOfPublications collection1Double = mock(CollectionOfPublications.class);
        CollectionOfPublications collection2Double = mock(CollectionOfPublications.class);

        when(collection1Double.getOwner()).thenReturn(ownerDouble);
        when(collection1Double.getCollectionName()).thenReturn("Collection 1");
        when(collection1Double.getCollectionGenre()).thenReturn(mock(GenreID.class));
        when(collection2Double.getOwner()).thenReturn(otherOwnerDouble);

        when(factoryDouble.create(any(), any(), any()))
                .thenReturn(collection1Double)
                .thenReturn(collection2Double);

        repo.createAndAdd(ownerDouble, "Collection 1", mock(GenreID.class));
        repo.createAndAdd(ownerDouble, "Collection 2", mock(GenreID.class));

        // Act
        List<CollectionOfPublications> result = repo.getAllCollectionOfPublicationsByUser(ownerDouble);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(collection1Double));
    }

}