package TOPSECRET.repository;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsID;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CollectionOfPublicationsRepositoryMem} class.
 *
 * <p>Uses Mockito to isolate the SUT ({@link CollectionOfPublicationsRepositoryMem})
 * from its collaborators ({@link CollectionOfPublicationsFactory} and {@link CollectionOfPublications}).</p>
 *
 * <p>Validates the following responsibilities:</p>
 * <ul>
 *     <li>Rejecting null constructor arguments</li>
 *     <li>Creating and storing valid collections</li>
 *     <li>Preventing duplicate collections (same name, genre and owner)</li>
 *     <li>Returning only collections owned by a given user</li>
 *     <li>Finding a collection by its {@link CollectionOfPublicationsID}</li>
 * </ul>
 */
class CollectionOfPublicationsRepositoryMemTest {

    // ============================================================
    // Constructor Tests
    // ============================================================

    @Test
    void ensureThrowsExceptionWhenFactoryIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CollectionOfPublicationsRepositoryMem(null));
    }

    @Test
    void ensureRepositoryIsCreatedSuccessfullyWithValidFactory() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);

        // Act & Assert
        assertDoesNotThrow(() -> new CollectionOfPublicationsRepositoryMem(factory));
    }

    // ============================================================
    // createAndAdd() Tests
    // ============================================================

    @Test
    void ensureCreateAndAddThrowsExceptionWhenUserIdIsNull() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        GenreID genre = mock(GenreID.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(null, "My Collection", genre));
    }

    @Test
    void ensureCreateAndAddThrowsExceptionWhenNameIsNull() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        UserId userId = mock(UserId.class);
        GenreID genre = mock(GenreID.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(userId, null, genre));
    }

    @Test
    void ensureCreateAndAddThrowsExceptionWhenGenreIdIsNull() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        UserId userId = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(userId, "My Collection", null));
    }

    @Test
    void ensureCreateAndAddReturnsCreatedCollection() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        UserId userId = mock(UserId.class);
        GenreID genre = mock(GenreID.class);
        CollectionOfPublications expected = mock(CollectionOfPublications.class);

        when(expected.getOwnerId()).thenReturn(userId);
        when(expected.getCollectionName()).thenReturn("My Collection");
        when(expected.getCollectionGenre()).thenReturn(genre);
        when(factory.create("My Collection", genre, userId)).thenReturn(expected);

        // Act
        CollectionOfPublications result = repository.createAndAdd(userId, "My Collection", genre);

        // Assert
        assertSame(expected, result);
    }

    @Test
    void ensureCreateAndAddStoresCollectionInRepository() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        UserId userId = mock(UserId.class);
        GenreID genre = mock(GenreID.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        when(collection.getOwnerId()).thenReturn(userId);
        when(collection.getCollectionName()).thenReturn("My Collection");
        when(collection.getCollectionGenre()).thenReturn(genre);
        when(factory.create("My Collection", genre, userId)).thenReturn(collection);

        // Act
        repository.createAndAdd(userId, "My Collection", genre);
        List<CollectionOfPublications> result = repository.getUserCollections(userId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(collection, result.get(0));
    }

    @Test
    void ensureCreateAndAddThrowsExceptionWhenDuplicateNameAndGenreForSameUser() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        UserId userId = mock(UserId.class);
        GenreID genre = mock(GenreID.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        when(collection.getOwnerId()).thenReturn(userId);
        when(collection.getCollectionName()).thenReturn("My Collection");
        when(collection.getCollectionGenre()).thenReturn(genre);
        when(factory.create("My Collection", genre, userId)).thenReturn(collection);

        repository.createAndAdd(userId, "My Collection", genre);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(userId, "My Collection", genre));
    }

    @Test
    void ensureCreateAndAddIsCaseInsensitiveForName() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        UserId userId = mock(UserId.class);
        GenreID genre = mock(GenreID.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        when(collection.getOwnerId()).thenReturn(userId);
        when(collection.getCollectionName()).thenReturn("My Collection");
        when(collection.getCollectionGenre()).thenReturn(genre);
        when(factory.create("My Collection", genre, userId)).thenReturn(collection);

        repository.createAndAdd(userId, "My Collection", genre);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(userId, "MY COLLECTION", genre));
    }

    @Test
    void ensureCreateAndAddAllowsSameNameWithDifferentGenre() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        UserId userId = mock(UserId.class);
        GenreID genre1 = mock(GenreID.class);
        GenreID genre2 = mock(GenreID.class);

        CollectionOfPublications col1 = mock(CollectionOfPublications.class);
        CollectionOfPublications col2 = mock(CollectionOfPublications.class);

        when(col1.getOwnerId()).thenReturn(userId);
        when(col1.getCollectionName()).thenReturn("My Collection");
        when(col1.getCollectionGenre()).thenReturn(genre1);
        when(factory.create("My Collection", genre1, userId)).thenReturn(col1);

        when(col2.getOwnerId()).thenReturn(userId);
        when(col2.getCollectionName()).thenReturn("My Collection");
        when(col2.getCollectionGenre()).thenReturn(genre2);
        when(factory.create("My Collection", genre2, userId)).thenReturn(col2);

        // Act
        repository.createAndAdd(userId, "My Collection", genre1);
        CollectionOfPublications result = repository.createAndAdd(userId, "My Collection", genre2);

        // Assert
        assertNotNull(result);
        assertEquals(2, repository.getUserCollections(userId).size());
    }

    @Test
    void ensureCreateAndAddAllowsSameNameAndGenreForDifferentUsers() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        UserId userId1 = mock(UserId.class);
        UserId userId2 = mock(UserId.class);
        GenreID genre = mock(GenreID.class);

        CollectionOfPublications col1 = mock(CollectionOfPublications.class);
        CollectionOfPublications col2 = mock(CollectionOfPublications.class);

        when(col1.getOwnerId()).thenReturn(userId1);
        when(col1.getCollectionName()).thenReturn("My Collection");
        when(col1.getCollectionGenre()).thenReturn(genre);
        when(factory.create("My Collection", genre, userId1)).thenReturn(col1);

        when(col2.getOwnerId()).thenReturn(userId2);
        when(col2.getCollectionName()).thenReturn("My Collection");
        when(col2.getCollectionGenre()).thenReturn(genre);
        when(factory.create("My Collection", genre, userId2)).thenReturn(col2);

        // Act
        repository.createAndAdd(userId1, "My Collection", genre);
        repository.createAndAdd(userId2, "My Collection", genre);

        // Assert
        assertEquals(1, repository.getUserCollections(userId1).size());
        assertEquals(1, repository.getUserCollections(userId2).size());
    }

    // ============================================================
    // getUserCollections() Tests
    // ============================================================

    @Test
    void ensureGetUserCollectionsThrowsExceptionWhenUserIdIsNull() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.getUserCollections(null));
    }

    @Test
    void ensureGetUserCollectionsReturnsEmptyListWhenUserHasNoCollections() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        UserId userId = mock(UserId.class);

        // Act
        List<CollectionOfPublications> result = repository.getUserCollections(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void ensureGetUserCollectionsReturnsOnlyCollectionsOwnedByUser() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        UserId userId1 = mock(UserId.class);
        UserId userId2 = mock(UserId.class);
        GenreID genre = mock(GenreID.class);

        CollectionOfPublications col1 = mock(CollectionOfPublications.class);
        CollectionOfPublications col2 = mock(CollectionOfPublications.class);

        when(col1.getOwnerId()).thenReturn(userId1);
        when(col1.getCollectionName()).thenReturn("Collection A");
        when(col1.getCollectionGenre()).thenReturn(genre);
        when(factory.create("Collection A", genre, userId1)).thenReturn(col1);

        when(col2.getOwnerId()).thenReturn(userId2);
        when(col2.getCollectionName()).thenReturn("Collection B");
        when(col2.getCollectionGenre()).thenReturn(genre);
        when(factory.create("Collection B", genre, userId2)).thenReturn(col2);

        repository.createAndAdd(userId1, "Collection A", genre);
        repository.createAndAdd(userId2, "Collection B", genre);

        // Act
        List<CollectionOfPublications> result = repository.getUserCollections(userId1);

        // Assert
        assertEquals(1, result.size());
        assertEquals(col1, result.get(0));
    }

    @Test
    void ensureGetUserCollectionsReturnsAllCollectionsOfUser() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        UserId userId = mock(UserId.class);
        GenreID genre1 = mock(GenreID.class);
        GenreID genre2 = mock(GenreID.class);
        GenreID genre3 = mock(GenreID.class);

        CollectionOfPublications col1 = mock(CollectionOfPublications.class);
        CollectionOfPublications col2 = mock(CollectionOfPublications.class);
        CollectionOfPublications col3 = mock(CollectionOfPublications.class);

        when(col1.getOwnerId()).thenReturn(userId);
        when(col1.getCollectionName()).thenReturn("Col1");
        when(col1.getCollectionGenre()).thenReturn(genre1);
        when(factory.create("Col1", genre1, userId)).thenReturn(col1);

        when(col2.getOwnerId()).thenReturn(userId);
        when(col2.getCollectionName()).thenReturn("Col2");
        when(col2.getCollectionGenre()).thenReturn(genre2);
        when(factory.create("Col2", genre2, userId)).thenReturn(col2);

        when(col3.getOwnerId()).thenReturn(userId);
        when(col3.getCollectionName()).thenReturn("Col3");
        when(col3.getCollectionGenre()).thenReturn(genre3);
        when(factory.create("Col3", genre3, userId)).thenReturn(col3);

        repository.createAndAdd(userId, "Col1", genre1);
        repository.createAndAdd(userId, "Col2", genre2);
        repository.createAndAdd(userId, "Col3", genre3);

        // Act
        List<CollectionOfPublications> result = repository.getUserCollections(userId);

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(col1));
        assertTrue(result.contains(col2));
        assertTrue(result.contains(col3));
    }

    // ============================================================
    // findCollectionOfPublicationsById() Tests
    // ============================================================

    @Test
    void ensureFindByIdThrowsExceptionWhenIdIsNull() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> repository.findCollectionOfPublicationsById(null));
    }

    @Test
    void ensureFindByIdReturnsEmptyOptionalWhenIdNotFound() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        CollectionOfPublicationsID id = mock(CollectionOfPublicationsID.class);

        // Act
        Optional<CollectionOfPublications> result = repository.findCollectionOfPublicationsById(id);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void ensureFindByIdReturnsCollectionWhenIdFound() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        UserId userId = mock(UserId.class);
        GenreID genre = mock(GenreID.class);
        CollectionOfPublicationsID collectionId = mock(CollectionOfPublicationsID.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        when(collection.getOwnerId()).thenReturn(userId);
        when(collection.getCollectionName()).thenReturn("My Collection");
        when(collection.getCollectionGenre()).thenReturn(genre);
        when(collection.id()).thenReturn(collectionId);
        when(factory.create("My Collection", genre, userId)).thenReturn(collection);

        repository.createAndAdd(userId, "My Collection", genre);

        // Act
        Optional<CollectionOfPublications> result = repository.findCollectionOfPublicationsById(collectionId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(collection, result.get());
    }

    @Test
    void ensureFindByIdReturnsCorrectCollectionWhenMultipleExist() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);
        CollectionOfPublicationsRepositoryMem repository = new CollectionOfPublicationsRepositoryMem(factory);
        UserId userId = mock(UserId.class);
        GenreID genre1 = mock(GenreID.class);
        GenreID genre2 = mock(GenreID.class);

        CollectionOfPublicationsID targetId = mock(CollectionOfPublicationsID.class);
        CollectionOfPublicationsID otherId = mock(CollectionOfPublicationsID.class);

        CollectionOfPublications target = mock(CollectionOfPublications.class);
        CollectionOfPublications other = mock(CollectionOfPublications.class);

        when(target.getOwnerId()).thenReturn(userId);
        when(target.getCollectionName()).thenReturn("Target");
        when(target.getCollectionGenre()).thenReturn(genre1);
        when(target.id()).thenReturn(targetId);
        when(factory.create("Target", genre1, userId)).thenReturn(target);

        when(other.getOwnerId()).thenReturn(userId);
        when(other.getCollectionName()).thenReturn("Other");
        when(other.getCollectionGenre()).thenReturn(genre2);
        when(other.id()).thenReturn(otherId);
        when(factory.create("Other", genre2, userId)).thenReturn(other);

        repository.createAndAdd(userId, "Target", genre1);
        repository.createAndAdd(userId, "Other", genre2);

        // Act
        Optional<CollectionOfPublications> result = repository.findCollectionOfPublicationsById(targetId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(target, result.get());
    }
}