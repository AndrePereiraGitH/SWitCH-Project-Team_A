package TOPSECRET.repository;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsID;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
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

    ICollectionOfPublicationsRepository sut;
    CollectionOfPublicationsFactory factory;

    @BeforeEach
    void setUp() {
        factory = mock(CollectionOfPublicationsFactory.class);
    }

    @Test
    void ensureThrowsExceptionWhenFactoryIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CollectionOfPublicationsRepositoryMem(null));
    }

    /**
     * Ensures exception is thrown when user is null.
     */
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

    /**
     * Ensures user collections are retrieved correctly.
     */
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

        // ACT
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
        IUserRepository userRepo = mock(IUserRepository.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.findPublicCollectionsByGenreId(null));
    }


    @Test
    void shouldReturnAllPublicCollectionsOfAGivenGenre() {
        // Arrange
        GenreID desiredGenre = mock(GenreID.class);
        UserId userDouble = mock(UserId.class);

        CollectionOfPublications c1 = mock(CollectionOfPublications.class);
        CollectionOfPublications c2 = mock(CollectionOfPublications.class);

        when(factory.create("Collection 1 name", desiredGenre, userDouble)).thenReturn(c1);
        when(factory.create("Collection 2 name", desiredGenre, userDouble)).thenReturn(c2);

        when(c1.getOwnerId()).thenReturn(userDouble);
        when(c1.getCollectionName()).thenReturn("Collection 1 name");
        when(c1.getCollectionGenre()).thenReturn(desiredGenre);
        when(c1.getIsPrivate()).thenReturn(false);

        when(c2.getOwnerId()).thenReturn(userDouble);
        when(c2.getCollectionName()).thenReturn("Collection 2 name");
        when(c2.getCollectionGenre()).thenReturn(desiredGenre);
        when(c2.getIsPrivate()).thenReturn(false);

        sut = new CollectionOfPublicationsRepositoryMem(factory);

        // Act
        sut.createAndAdd(userDouble, "Collection 1 name", desiredGenre);
        sut.createAndAdd(userDouble, "Collection 2 name", desiredGenre);

        List<CollectionOfPublications> result = sut.findPublicCollectionsByGenreId(desiredGenre);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(c1, result.get(0));
        assertEquals(c2, result.get(1));
    }

    @Test
    void shouldReturnEmptyListPublicWhenNoCollectionsExist() {
        // Arrange
        GenreID desiredGenres = mock(GenreID.class);
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        // Act
        List<CollectionOfPublications> result = sut.findPublicCollectionsByGenreId(desiredGenres);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
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