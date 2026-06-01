package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.assembler.CollectionOfPublicationsAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.CollectionOfPublicationsDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ICollectionOfPublicationsRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CollectionOfPublicationsRepositoryJpaImpl}.
 *
 * <p>Acts as an anti-corruption layer between the domain model and the
 * JPA persistence layer. All conversions between domain objects
 * ({@link CollectionOfPublications}) and data model objects
 * ({@link CollectionOfPublicationsDM}) are delegated to
 * {@link CollectionOfPublicationsAssembler}.</p>
 *
 * <p>Tests are organised into the following sections:</p>
 * <ul>
 *     <li><b>save</b> — persisting a new collection, duplicate detection,
 *     and null guards.</li>
 *
 *     <li><b>existsById</b> — presence checks and null guards.</li>
 *
 *     <li><b>temporary default methods</b> — methods currently returning
 *     default values only for coverage purposes.</li>
 * </ul>
 */
class CollectionOfPublicationsRepositoryJpaImplTest {

    @Mock
    private ICollectionOfPublicationsRepositoryJpa jpa;

    @Mock
    private CollectionOfPublicationsAssembler assembler;

    @InjectMocks
    private CollectionOfPublicationsRepositoryJpaImpl repo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ===========================================================
    // ========== Tests for Constructor ==========================
    // ===========================================================

    @Test
    void constructor_shouldThrowException_whenRepositoryIsNull() {

        // Arrange
        ICollectionOfPublicationsRepositoryJpa repoJpa = null;

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublicationsRepositoryJpaImpl(repoJpa, assembler)
        );
    }

    @Test
    void constructor_shouldThrowException_whenAssemblerIsNull() {

        // Arrange
        CollectionOfPublicationsAssembler assembler = null;

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublicationsRepositoryJpaImpl(jpa, assembler)
        );
    }

    @Test
    void constructor_shouldCreateInstance_whenArgumentsAreValid() {

        // Act
        CollectionOfPublicationsRepositoryJpaImpl result =
                new CollectionOfPublicationsRepositoryJpaImpl(jpa, assembler);

        // Assert
        assertNotNull(result);
    }

    // ===========================================================
    // ========== Tests for save =================================
    // ===========================================================

    @Test
    void save_shouldReturnSavedCollection() {

        // Arrange
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsDM collectionDM = mock(CollectionOfPublicationsDM.class);

        when(collection.id()).thenReturn(id);
        when(jpa.existsById(id)).thenReturn(false);
        when(assembler.toDM(collection)).thenReturn(collectionDM);
        when(jpa.save(collectionDM)).thenReturn(collectionDM);
        when(assembler.toDomain(collectionDM)).thenReturn(collection);

        // Act
        CollectionOfPublications result = repo.save(collection);

        // Assert
        assertSame(collection, result);
    }

    @Test
    void save_shouldThrowWhenCollectionAlreadyExists() {

        // Arrange
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        when(collection.id()).thenReturn(id);
        when(jpa.existsById(id)).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.save(collection));
    }

    @Test
    void save_shouldThrowExceptionWhenCollectionIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repo.save(null));
    }

    // ===========================================================
    // ========== Tests for existsById ===========================
    // ===========================================================

    @Test
    void existsById_shouldReturnTrueWhenCollectionExists() {

        // Arrange
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(jpa.existsById(id)).thenReturn(true);

        // Act
        boolean result = repo.existsById(id);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalseWhenCollectionDoesNotExist() {

        // Arrange
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(jpa.existsById(id)).thenReturn(false);

        // Act
        boolean result = repo.existsById(id);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsById_shouldThrowExceptionWhenIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repo.existsById(null));
    }

    // ===========================================================
    // ========== Tests for findById ===========================
    // ===========================================================

    @Test
    void findById_shouldThrow_whenCollectionIsNull() {

        //act + assert
        NullPointerException ex = assertThrows(NullPointerException.class, () ->repo.findById(null));
        assertEquals("CollectionOfPublications Id cannot be null", ex.getMessage());

    }

    @Test
    void findById_shouldReturnCollection_whenCollectionExists() throws InstanceNotFoundException {
        // arrange
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);
        CollectionOfPublicationsDM collectionDM = mock(CollectionOfPublicationsDM.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        when(jpa.findById(id)).thenReturn(Optional.of(collectionDM));

        when(assembler.toDomain(collectionDM)).thenReturn(collection);

        // act
        CollectionOfPublications result = repo.findById(id);

        // assert
        assertEquals(result, collection);
    }

    // ===========================================================
    // ========== Tests findAllByOwnerId ===========================
    // ===========================================================

    @Test
    void findAllByOwnerIdShouldReturnEmptyList() {

        // Arrange
        UserId userId = mock(UserId.class);

        // Act
        List<CollectionOfPublications> result =
                repo.findAllByOwnerId(userId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByOwnerId_shouldThrow_whenOwnerIdIsNull() {

        // act + assert
        NullPointerException ex = assertThrows(NullPointerException.class, ()-> repo.findAllByOwnerId(null));
        assertEquals("ownerId cannot be null", ex.getMessage());
    }

    @Test
    void findAllByUserId_shouldReturnCollections_whenOwnerHasCollections() {

        // arrange
        UserId ownerId = mock(UserId.class);

        CollectionOfPublicationsDM collectionDM1 = mock(CollectionOfPublicationsDM.class);
        CollectionOfPublicationsDM collectionDM2 = mock(CollectionOfPublicationsDM.class);

        CollectionOfPublications collection1 = mock(CollectionOfPublications.class);
        CollectionOfPublications collection2 = mock(CollectionOfPublications.class);

        when(jpa.findAllByOwnerId(ownerId))
                .thenReturn(List.of(collectionDM1, collectionDM2));

        when(assembler.toDomain(collectionDM1))
                .thenReturn(collection1);

        when(assembler.toDomain(collectionDM2))
                .thenReturn(collection2);

        // act
        List<CollectionOfPublications> result = repo.findAllByOwnerId(ownerId);

        // assert
        assertEquals(List.of(collection1, collection2), result);

    }

    // ===========================================================
    // ========== Default return methods =========================
    // ===========================================================

    @Test
    void findPublicCollectionsByGenreIdShouldReturnEmptyList() {

        // Arrange
        GenreId genreId = mock(GenreId.class);

        // Act
        List<CollectionOfPublications> result =
                repo.findPublicCollectionsByGenreId(genreId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicCollectionsByGenreId_shouldReturnOnlyPublicCollections_WhenBothTypesExist() {

        // ARRANGE
        GenreId genreIdDouble = mock(GenreId.class);

        CollectionOfPublicationsDM publicDM = mock(CollectionOfPublicationsDM.class);
        CollectionOfPublicationsDM privateDM = mock(CollectionOfPublicationsDM.class);

        CollectionOfPublications publicCollection = mock(CollectionOfPublications.class);
        CollectionOfPublications privateCollection = mock(CollectionOfPublications.class);

        when(publicCollection.isCurrentlyPublic()).thenReturn(true);
        when(privateCollection.isCurrentlyPublic()).thenReturn(false);

        when(jpa.findByGenreIdAndIsPrivateFalse(genreIdDouble))
                .thenReturn(List.of(publicDM, privateDM));

        when(assembler.toDomain(publicDM)).thenReturn(publicCollection);
        when(assembler.toDomain(privateDM)).thenReturn(privateCollection);

        // ACT
        List<CollectionOfPublications> result =
                repo.findPublicCollectionsByGenreId(genreIdDouble);

        // ASSERT
        assertEquals(1, result.size(), "One public collection found matching this genre");
        assertEquals(publicCollection, result.get(0), "No public collections matching this genre found");
        assertFalse(result.contains(privateCollection), "All filtered collections should be public");
    }

    @Test
    void findPublicCollectionsByGenreId_shouldThrowNullPointerException_whenGenreIdIsNull() {
        // ACT & ASSERT
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            repo.findPublicCollectionsByGenreId(null);
        });

        assertEquals("genreId cannot be null", exception.getMessage());
    }
}