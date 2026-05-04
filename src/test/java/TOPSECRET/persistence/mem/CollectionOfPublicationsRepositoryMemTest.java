package TOPSECRET.persistence.mem;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

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
    CollectionOfPublicationsFactory factory =
            new CollectionOfPublicationsFactory();

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

    @Test
    void shouldReturnCollectionsOwnedByUser() throws Exception {

        // Arrange
        UserId user = mock(UserId.class);
        UserId otherUser = mock(UserId.class);
        sut = new CollectionOfPublicationsRepositoryMem();

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

        // ACT
        List<CollectionOfPublications> result =
                sut.getUserCollections(user);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(collection1, result.get(0));
    }

    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();
        IUserRepository userRepo = mock(IUserRepository.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.findPublicCollectionsByGenreId(null));
    }

    @Test
    void shouldReturnEmptyListPublicWhenNoCollectionsExist() {
        // Arrange
        GenreID desiredGenres = mock(GenreID.class);
        sut = new CollectionOfPublicationsRepositoryMem();

        // Act
        List<CollectionOfPublications> result = sut.findPublicCollectionsByGenreId(desiredGenres);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void makeCollectionPublicShouldThrowWhenIdIsNull() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> sut.makeCollectionPublic(null));
    }

    @Test
    void makeCollectionPublicShouldMakeCollectionPublic() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        GenreID genre = mock(GenreID.class);
        UserId user = mock(UserId.class);

        CollectionOfPublications collection =
                factory.create("Fantasy", genre, user);

        CollectionOfPublicationsId id = collection.id();

        sut.save(collection);

        // pre-condition
        assertTrue(collection.isPrivate());

        // Act
        boolean result = sut.makeCollectionPublic(id);

        // Assert
        assertFalse(collection.isPrivate());
        assertFalse(result);
    }

    @Test
    void makeCollectionPublicShouldDoNothingWhenIdDoesNotExist() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        GenreID genre = mock(GenreID.class);
        UserId user = mock(UserId.class);

        CollectionOfPublications collection =
                factory.create("Fantasy", genre, user);

        sut.save(collection);

        CollectionOfPublicationsId nonExistingId =
                mock(CollectionOfPublicationsId.class);

        // Act
        sut.makeCollectionPublic(nonExistingId);

        // Assert
        assertTrue(collection.isPrivate()); // state unchanged
    }

    @Test
    void shouldReturnOnlyPublicCollectionsMatchingGenre() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        GenreID genre = mock(GenreID.class);
        GenreID otherGenre = mock(GenreID.class);
        UserId user = mock(UserId.class);

        CollectionOfPublications publicCollection =
                factory.create("A", genre, user);

        CollectionOfPublications privateCollection =
                factory.create("B", genre, user);

        CollectionOfPublications differentGenreCollection =
                factory.create("C", otherGenre, user);

        sut.save(publicCollection);
        sut.save(privateCollection);
        sut.save(differentGenreCollection);

        // make only one public
        sut.makeCollectionPublic(publicCollection.id());

        // Act
        List<CollectionOfPublications> result =
                sut.findPublicCollectionsByGenreId(genre);

        // Assert
        assertEquals(1, result.size());

        CollectionOfPublications returned = result.get(0);

        assertEquals(publicCollection.id(), returned.id());
        assertEquals(genre, returned.getCollectionGenre());
        assertFalse(returned.isPrivate());
    }
}




