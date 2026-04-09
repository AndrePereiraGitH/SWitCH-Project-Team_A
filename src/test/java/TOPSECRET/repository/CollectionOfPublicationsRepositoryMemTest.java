package TOPSECRET.repository;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollectionOfPublicationsRepositoryMemTest {

    CollectionOfPublicationsRepositoryMem sut;
    CollectionOfPublicationsFactory factory;

    @BeforeEach
    void setUp() {
        factory = mock(CollectionOfPublicationsFactory.class);
    }

    @Test
    void shouldThrowExceptionWhenFactoryIsNull_US017() {
        assertThrows(IllegalArgumentException.class,
                () -> new CollectionOfPublicationsRepositoryMem(null));
    }


    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.getUserCollections(null));
    }


    @Test
    void shouldReturnCollectionsOwnedByUser() throws Exception {

        // Arrange
        UserId user = mock(UserId.class);
        UserId otherUser = mock(UserId.class);
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        CollectionOfPublications collection1 = mock(CollectionOfPublications.class);
        CollectionOfPublications collection2 = mock(CollectionOfPublications.class);

        when(collection1.getOwnerId()).thenReturn(user);
        when(collection2.getOwnerId()).thenReturn(otherUser);

        // Act
        // the list is private with no setter, so reflection is the only way to pre-populate it in a unit test
        Field field = CollectionOfPublicationsRepositoryMem.class
                .getDeclaredField("_collectionOfPublicationsList");
        field.setAccessible(true);

        List<CollectionOfPublications> list =
                (List<CollectionOfPublications>) field.get(sut);

        list.add(collection1);
        list.add(collection2);

        List<CollectionOfPublications> result =
                sut.getUserCollections(user);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(collection1, result.get(0));
    }


    // ==== US020 tests ====

    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.findPublicCollectionsByGenre(null));
    }


    @Test
    void shouldReturnAllPublicCollectionsOfAGivenGenre() {
        // Arrange
        GenreID desiredGenre = mock(GenreID.class);
        UserId userId = mock(UserId.class);
        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
        CollectionOfPublications collection1;
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        // Act
        when(factory.create("Collection test", desiredGenre, userId)).thenReturn(collectionDouble);
        when(collectionDouble.getIsPrivate()).thenReturn(false);
        when(collectionDouble.getCollectionGenre()).thenReturn(desiredGenre);

        collection1 = sut.createAndAdd(userId, "Collection test", desiredGenre);

        List<CollectionOfPublications> result = sut.findPublicCollectionsByGenre(desiredGenre);

        // Assert
        assertNotNull(result);
        assertEquals(collection1, result.get(0));
        assertEquals(1, result.size());
    }


    @Test
    void shouldReturnEmptyListPublicWhenNoCollectionsExist() {
        // Arrange
        GenreID desiredGenres = mock(GenreID.class);
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        // Act
        List<CollectionOfPublications> result = sut.findPublicCollectionsByGenre(desiredGenres);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}