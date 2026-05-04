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
import java.util.UUID;

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
    CollectionOfPublicationsFactory factory = new CollectionOfPublicationsFactory();

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
    void makeCollectionPublicShouldAffectQueryResults() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        GenreID genre = mock(GenreID.class);
        UserId user = mock(UserId.class);

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(collection.id()).thenReturn(id);
        when(collection.getCollectionGenre()).thenReturn(genre);

        // Initially private
        when(collection.isPrivate()).thenReturn(true);

        sut.save(collection);

        // Act
        sut.makeCollectionPublic(id);

        // Simulate state change after call
        when(collection.isPrivate()).thenReturn(false);

        List<CollectionOfPublications> result =
                sut.findPublicCollectionsByGenreId(genre);

        // Assert
        assertEquals(1, result.size());
        assertSame(collection, result.get(0));
    }

    @Test
    void makeCollectionPublicShouldDoNothingWhenIdDoesNotExist() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        GenreID genre = mock(GenreID.class);

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId existingId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublicationsId nonExistingId = mock(CollectionOfPublicationsId.class);

        when(collection.id()).thenReturn(existingId);
        when(collection.getCollectionGenre()).thenReturn(genre);

        when(collection.isPrivate()).thenReturn(true);

        sut.save(collection);

        // Act
        sut.makeCollectionPublic(nonExistingId);

        List<CollectionOfPublications> result =
                sut.findPublicCollectionsByGenreId(genre);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnOnlyPublicCollectionsMatchingGenre() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        GenreID genre = mock(GenreID.class);
        GenreID otherGenre = mock(GenreID.class);

        CollectionOfPublications publicCollection = mock(CollectionOfPublications.class);
        CollectionOfPublications privateCollection = mock(CollectionOfPublications.class);
        CollectionOfPublications differentGenreCollection = mock(CollectionOfPublications.class);

        CollectionOfPublicationsId id1 = mock(CollectionOfPublicationsId.class);
        CollectionOfPublicationsId id2 = mock(CollectionOfPublicationsId.class);
        CollectionOfPublicationsId id3 = mock(CollectionOfPublicationsId.class);

        when(publicCollection.id()).thenReturn(id1);
        when(privateCollection.id()).thenReturn(id2);
        when(differentGenreCollection.id()).thenReturn(id3);

        when(publicCollection.getCollectionGenre()).thenReturn(genre);
        when(privateCollection.getCollectionGenre()).thenReturn(genre);
        when(differentGenreCollection.getCollectionGenre()).thenReturn(otherGenre);

        // all private initially
        when(publicCollection.isPrivate()).thenReturn(true);
        when(privateCollection.isPrivate()).thenReturn(true);
        when(differentGenreCollection.isPrivate()).thenReturn(true);

        sut.save(publicCollection);
        sut.save(privateCollection);
        sut.save(differentGenreCollection);

        // Act
        sut.makeCollectionPublic(id1);

        // simulating post-condition
        when(publicCollection.isPrivate()).thenReturn(false);

        List<CollectionOfPublications> result =
                sut.findPublicCollectionsByGenreId(genre);

        // Assert
        assertEquals(1, result.size());
        assertSame(publicCollection, result.get(0));
    }

    @Test
    void makeCollectionPublicShouldMakeCollectionPublic() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        when(id.getId()).thenReturn(UUID.randomUUID().toString());

        when(collection.id()).thenReturn(id);

        when(collection.isPrivate()).thenReturn(true, false);

        sut.save(collection);

        assertTrue(collection.isPrivate());

        // Act
        boolean result = sut.makeCollectionPublic(id);

        // Assert
        assertFalse(result);
        assertFalse(collection.isPrivate());
    }

    @Test
    void makeCollectionPublicShouldOnlyChangeMatchingCollection() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        CollectionOfPublicationsId targetId = mock(CollectionOfPublicationsId.class);
        CollectionOfPublicationsId otherId = mock(CollectionOfPublicationsId.class);

        CollectionOfPublications target = mock(CollectionOfPublications.class);
        CollectionOfPublications other = mock(CollectionOfPublications.class);

        when(target.id()).thenReturn(targetId);
        when(other.id()).thenReturn(otherId);

        // simulate state transitions
        when(target.isPrivate()).thenReturn(true, false);
        when(other.isPrivate()).thenReturn(true);

        sut.save(target);
        sut.save(other);

        assertTrue(target.isPrivate());
        assertTrue(other.isPrivate());

        // Act
        sut.makeCollectionPublic(targetId);

        // Assert
        assertFalse(target.isPrivate());
        assertTrue(other.isPrivate());
    }

    @Test
    void getUserCollectionsShouldThrowExceptionIfUserIdNull() {
        //Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        //Act + assert
        assertThrows(IllegalArgumentException.class, () -> sut.getUserCollections(null));
    }

    @Test
    void makeCollectionPublicShouldOnlyAffectMatchingCollection() {

        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem();

        GenreID genre = mock(GenreID.class);
        UserId user = mock(UserId.class);

        CollectionOfPublications target =
                factory.create("A", genre, user);

        CollectionOfPublications other =
                factory.create("B", genre, user);

        sut.save(target);
        sut.save(other);

        assertTrue(target.isPrivate());
        assertTrue(other.isPrivate());

        // Act
        sut.makeCollectionPublic(target.id());

        // Assert
        assertFalse(target.isPrivate());
        assertTrue(other.isPrivate());
    }
}




