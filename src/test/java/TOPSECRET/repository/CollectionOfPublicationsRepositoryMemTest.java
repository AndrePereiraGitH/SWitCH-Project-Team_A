package TOPSECRET.repository;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
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

}