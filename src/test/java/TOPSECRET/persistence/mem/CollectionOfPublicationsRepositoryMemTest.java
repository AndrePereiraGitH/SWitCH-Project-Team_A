package TOPSECRET.persistence.mem;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CollectionOfPublicationsRepositoryMem} class.
 *
 *
 * <p>Verifies that the repository correctly stores collections, retrieves them by owner,
 * and rejects invalid arguments.
 */

class CollectionOfPublicationsRepositoryMemTest {

    ICollectionOfPublicationsRepository sut;

    @Test
    void ensureRepositoryIsCreatedSuccessfully() {
        // Act & Assert
        assertDoesNotThrow(() -> new CollectionOfPublicationsRepositoryMem());
    }

    @Test
    void saveShouldReturnSavedCollection() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(collection.id()).thenReturn(id);

        // Act
        CollectionOfPublications result = sut.save(collection);

        // Assert
        assertEquals(collection, result);
    }

    @Test
    void saveShouldThrowWhenCollectionAlreadyExists() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(collection.id()).thenReturn(id);

        sut.save(collection);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.save(collection)
        );
    }

    @Test
    void existsByIdShouldThrowWhenIdIsNull() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.existsById(null)
        );
    }

    @Test
    void existsByIdShouldReturnTrueWhenCollectionExists() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(collection.id()).thenReturn(id);

        sut.save(collection);

        // Act
        boolean result = sut.existsById(id);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsByIdShouldReturnFalseWhenCollectionDoesNotExist() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        // Act
        boolean result = sut.existsById(id);

        // Assert
        assertFalse(result);
    }

//    @Test
//    void shouldReturnCollectionsOwnedByUser() throws Exception {
//
//        // Arrange
//        UserId user = mock(UserId.class);
//        UserId otherUser = mock(UserId.class);
//        sut = new CollectionOfPublicationsRepositoryMem(factory);
//
//        CollectionOfPublications collection1 = mock(CollectionOfPublications.class);
//        CollectionOfPublications collection2 = mock(CollectionOfPublications.class);
//
//        when(collection1.getOwnerId()).thenReturn(user);
//        when(collection2.getOwnerId()).thenReturn(otherUser);
//
//        // Act
//        // the list is private with no setter, so reflection is the only way to pre-populate it in a unit test
//        Field field = CollectionOfPublicationsRepositoryMem.class
//                .getDeclaredField("_collectionOfPublicationsList");
//        field.setAccessible(true);
//
//        List<CollectionOfPublications> list =
//                (List<CollectionOfPublications>) field.get(sut);
//
//        list.add(collection1);
//        list.add(collection2);
//
//        // ACT
//        List<CollectionOfPublications> result =
//                sut.getUserCollections(user);
//
//        // ASSERT
//        assertEquals(1, result.size());
//        assertEquals(collection1, result.get(0));
//    }

//    @Test
//    void shouldThrowExceptionWhenGenreIsNull() {
//        // Arrange
//        sut = new CollectionOfPublicationsRepositoryMem(factory);
//        IUserRepository userRepo = mock(IUserRepository.class);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class, () -> sut.findPublicCollectionsByGenreId(null));
//    }
//}
//    @Test
//    void shouldReturnEmptyListPublicWhenNoCollectionsExist() {
//        // Arrange
//        GenreID desiredGenres = mock(GenreID.class);
//        sut = new CollectionOfPublicationsRepositoryMem(factory);
//
//        // Act
//        List<CollectionOfPublications> result = sut.findPublicCollectionsByGenreId(desiredGenres);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(0, result.size());
//    }
//
}