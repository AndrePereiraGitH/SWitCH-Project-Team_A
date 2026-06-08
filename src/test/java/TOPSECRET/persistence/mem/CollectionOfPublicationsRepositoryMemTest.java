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

import java.lang.reflect.Field;
import java.util.Optional;
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

    private CollectionOfPublications createMockCollection(boolean isPrivate, boolean isPublic) {
        CollectionOfPublications c = mock(CollectionOfPublications.class);
        when(c.getIsPrivate()).thenReturn(isPrivate);
        when(c.isCurrentlyPublic()).thenReturn(isPublic);
        return c;
    }

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
    void findById_shouldFindCollection_whenExistsById() {
        // Arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(collection.id()).thenReturn(id);

        sut.save(collection);

        // Act
        Optional<CollectionOfPublications> result = sut.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(collection, result.get());
    }

    @Test
    void findById_shouldReturnEmpty_whenCollectionDoesNotExist() {
        // Arrange
        ICollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepositoryMem();
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        // Act
        Optional<CollectionOfPublications> result = sut.findById(id);

        // Assert
        assertTrue(result.isEmpty());
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

    // ===========================================================
    // ========== findAllPublicCollections =======================
    // ===========================================================

    @Test
    void findAllPublicCollections_shouldReturnOnlyPublicCollections() {

        // ARRANGE
        GenreId genreId = mock(GenreId.class);

        CollectionOfPublicationsId id1 = mock(CollectionOfPublicationsId.class);
        CollectionOfPublicationsId id2 = mock(CollectionOfPublicationsId.class);

        CollectionOfPublications c1 = mock(CollectionOfPublications.class);
        CollectionOfPublications c2 = mock(CollectionOfPublications.class);

        when(c1.id()).thenReturn(id1);
        when(c2.id()).thenReturn(id2);

        when(c1.getCollectionGenre()).thenReturn(genreId);
        when(c2.getCollectionGenre()).thenReturn(genreId);

        when(c1.getIsPrivate()).thenReturn(false);
        when(c2.getIsPrivate()).thenReturn(false);

        CollectionOfPublicationsRepositoryMem repo =
                new CollectionOfPublicationsRepositoryMem();

        repo.save(c1);
        repo.save(c2);

        // ACT
        List<CollectionOfPublications> result =
                repo.findPublicCollectionsByGenreId(genreId);

        // ASSERT
        assertEquals(2, result.size());
        assertTrue(result.contains(c1));
        assertTrue(result.contains(c2));
    }

    @Test
    void findAllPublicCollections_shouldReturnEmpty_whenRepositoryEmpty() {

        //Arrange
        CollectionOfPublicationsRepositoryMem repo =
                new CollectionOfPublicationsRepositoryMem();

        //Act
        List<CollectionOfPublications> result = repo.findAllPublicCollections();

        //Assert
        assertTrue(result.isEmpty());
    }

    // ===========================================================
    // ========== findAll ========================================
    // ===========================================================

    @Test
    void findAll_shouldReturnAllStoredCollections() {

        // ARRANGE
        CollectionOfPublications c1 = mock(CollectionOfPublications.class);
        CollectionOfPublications c2 = mock(CollectionOfPublications.class);

        CollectionOfPublicationsId id1 = mock(CollectionOfPublicationsId.class);
        CollectionOfPublicationsId id2 = mock(CollectionOfPublicationsId.class);

        when(c1.id()).thenReturn(id1);
        when(c2.id()).thenReturn(id2);

        CollectionOfPublicationsRepositoryMem repo =
                new CollectionOfPublicationsRepositoryMem();

        repo.save(c1);
        repo.save(c2);

        // ACT
        List<CollectionOfPublications> result = repo.findAll();

        // ASSERT
        assertEquals(2, result.size());
        assertTrue(result.contains(c1));
        assertTrue(result.contains(c2));
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoCollectionsExist() {

        CollectionOfPublicationsRepositoryMem repo =
                new CollectionOfPublicationsRepositoryMem();

        List<CollectionOfPublications> result = repo.findAll();

        assertTrue(result.isEmpty());
    }
}