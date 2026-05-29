package TOPSECRET.persistence.mem;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
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


    private CollectionOfPublicationsFactory factoryDouble;

    @BeforeEach
    void setUp() {
        factoryDouble = mock(CollectionOfPublicationsFactory.class);
    }

    @Test
    void ensureRepositoryIsCreatedSuccessfully() {
        // Act & Assert
        assertDoesNotThrow(() -> new CollectionOfPublicationsRepositoryMem());
    }

    @Test
    void saveShouldReturnSavedCollection() {

        // Arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(collection.id()).thenReturn(id);

        // Act
        CollectionOfPublications result = sut.save(collection);

        // Assert
        assertEquals(collection, result);
    }

    // ===================================================
    // ---------------------  SAVE -----------------------
    // ===================================================

    @Test
    void saveShouldThrowWhenCollectionAlreadyExists() {

        // Arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();

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

    // ===================================================
    // -------------------  UPDATE -----------------------
    // ===================================================

    @Test
    void update_shouldUpdateAndReturnCollection_whenCollectionExists() throws InstanceNotFoundException {
        // arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        CollectionOfPublications existingCollection = mock(CollectionOfPublications.class);

        CollectionOfPublications updatedCollection = mock(CollectionOfPublications.class);

        when(existingCollection.id()).thenReturn(id);
        when(updatedCollection.id()).thenReturn(id);

        sut.save(existingCollection);

        // act
        CollectionOfPublications result = sut.update(updatedCollection);

        // assert
        assertEquals(updatedCollection, result);
        assertEquals(updatedCollection, sut.findById(id));
    }

    @Test
    void update_shouldThrow_whenCollectionIsNull() {
        // arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();

        // act + assert
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> sut.update(null)
        );

        assertEquals("collection cannot be null", ex.getMessage());
    }

    @Test
    void update_shouldReturnNull_whenCollectionDoesNotExist() {
        // arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();

        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(collection.id()).thenReturn(id);

        // act
        CollectionOfPublications result = sut.update(collection);

        // assert
        assertNull(result);
    }

    // ===================================================
    // ------------------  EXISTS BY ID-------------------
    // ===================================================

    @Test
    void existsByIdShouldThrowWhenIdIsNull() {

        // Arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.existsById(null)
        );
    }

    @Test
    void existsByIdShouldReturnTrueWhenCollectionExists() {

        // Arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();

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
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();

        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        // Act
        boolean result = sut.existsById(id);

        // Assert
        assertFalse(result);
    }

    // ===================================================
    // ----------------  FIND BY ID ----------------------
    // ===================================================

    @Test
    void findById_shouldFindCollection_whenExistsById() throws InstanceNotFoundException {
        // Arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(collection.id()).thenReturn(id);

        sut.save(collection);

        // Act
        CollectionOfPublications result = sut.findById(id);

        // Assert
        assertEquals(result, collection);
    }

    @Test
    void findById_shouldThrow_whenCollectionDoesNotExists() {
        //arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        //act + assert
        InstanceNotFoundException ex = assertThrows(InstanceNotFoundException.class, () -> sut.findById(id));
        assertEquals("collection not found by Id: " + id, ex.getMessage());
    }

    @Test
    void findById_shouldThrow_whenCollectionIdIsNull() {
        //arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();

        //act + assert
        NullPointerException ex = assertThrows(NullPointerException.class, () -> sut.findById(null));
        assertEquals("CollectionId cannot be null", ex.getMessage());

    }

    // ===================================================
    // -----------  FIND ALL BY USER ID ------------------
    // ===================================================

    @Test
    void getUserCollectionsShouldThrowExceptionIfAllByUserIdNull() {
        //Arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();

        //Act + assert
        assertThrows(IllegalArgumentException.class, () -> sut.findAllByOwnerId(null));
    }

    @Test
    void shouldReturnCollectionsOwnedByUser() throws Exception {

        // Arrange
        UserId user = mock(UserId.class);
        UserId otherUser = mock(UserId.class);
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();

        CollectionOfPublications collection1 = mock(CollectionOfPublications.class);
        CollectionOfPublications collection2 = mock(CollectionOfPublications.class);

        when(collection1.getOwnerId()).thenReturn(user);
        when(collection2.getOwnerId()).thenReturn(otherUser);

        // Act
        Field field = CollectionOfPublicationsRepositoryMem.class
                .getDeclaredField("_collectionOfPublicationsList");
        field.setAccessible(true);

        List<CollectionOfPublications> list =
                (List<CollectionOfPublications>) field.get(sut);

        list.add(collection1);
        list.add(collection2);

        // ACT
        List<CollectionOfPublications> result =
                sut.findAllByOwnerId(user);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(collection1, result.get(0));
    }

    // =================================================================
    // ---------  findPublicCollectionsByGenreId -----------------------
    // =================================================================

    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        // Arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();
        IUserRepository userRepo = mock(IUserRepository.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.findPublicCollectionsByGenreId(null));
    }

    @Test
    void shouldReturnEmptyListPublicWhenNoCollectionsExist() {
        // Arrange
        GenreId desiredGenres = mock(GenreId.class);
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();

        // Act
        List<CollectionOfPublications> result = sut.findPublicCollectionsByGenreId(desiredGenres);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void findPublicCollectionsByGenreIdShouldReturnPublicCollectionsWhenMatchingCollectionsExist() {
        // ARRANGE
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();
        GenreId genreIdDouble = mock(GenreId.class);
        UserId userIdDouble = mock(UserId.class);

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(factoryDouble.create("My Collection", genreIdDouble, userIdDouble)).thenReturn(collection);
        when(collection.id()).thenReturn(mock(CollectionOfPublicationsId.class));
        when(collection.getCollectionGenre()).thenReturn(genreIdDouble);
        when(collection.getIsPrivate()).thenReturn(false);

        sut.save(collection);

        // ACT
        List<CollectionOfPublications> result =
                sut.findPublicCollectionsByGenreId(genreIdDouble);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(collection, result.get(0));
    }

    @Test
    void findPublicCollectionsByGenreIdShouldNotReturnPrivateCollections() {
        // ARRANGE
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();
        GenreId genreIdDouble = mock(GenreId.class);
        UserId userIdDouble = mock(UserId.class);

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(factoryDouble.create("My Collection", genreIdDouble, userIdDouble)).thenReturn(collection);
        when(collection.id()).thenReturn(mock(CollectionOfPublicationsId.class));
        when(collection.getCollectionGenre()).thenReturn(genreIdDouble);
        when(collection.getIsPrivate()).thenReturn(true);

        sut.save(collection);

        //ACT
        List<CollectionOfPublications> result =
                sut.findPublicCollectionsByGenreId(genreIdDouble);

        //ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicCollectionsByGenreIdShouldNotReturnCollectionsWithDifferentGenre() {
        // ARRANGE
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();
        GenreId genreIdDouble = mock(GenreId.class);
        GenreId otherGenreIdDouble = mock(GenreId.class);
        UserId userIdDouble = mock(UserId.class);

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(factoryDouble.create("My Collection", otherGenreIdDouble, userIdDouble)).thenReturn(collection);
        when(collection.id()).thenReturn(mock(CollectionOfPublicationsId.class));
        when(collection.getCollectionGenre()).thenReturn(otherGenreIdDouble);
        when(collection.getIsPrivate()).thenReturn(false);

        sut.save(collection);

        // ACT
        List<CollectionOfPublications> result =
                sut.findPublicCollectionsByGenreId(genreIdDouble);

        // ASSERT
        assertTrue(result.isEmpty());
    }
}




