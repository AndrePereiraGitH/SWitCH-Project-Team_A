package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.persistence.repositoryjpa.assembler.CollectionOfPublicationsAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.CollectionOfPublicationsDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ICollectionOfPublicationsRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    // ========== Default return methods =========================
    // ===========================================================

//    @Test
//    void getUserCollections_shouldReturnEmptyList() {
//
//        // Arrange
//        UserId userId = mock(UserId.class);
//
//        // Act
//        List<CollectionOfPublications> result =
//                repo.getUserCollections(userId);
//
//        // Assert
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void makeCollectionPublic_shouldReturnFalse() {
//
//        // Arrange
//        CollectionOfPublicationsID id =
//                mock(CollectionOfPublicationsID.class);
//
//        // Act
//        boolean result = repo.makeCollectionPublic(id);
//
//        // Assert
//        assertFalse(result);
//    }
//
//    @Test
//    void findPublicCollectionsByGenreId_shouldReturnEmptyList() {
//
//        // Arrange
//        GenreID genreId = mock(GenreID.class);
//
//        // Act
//        List<CollectionOfPublications> result =
//                repo.findPublicCollectionsByGenreId(genreId);
//
//        // Assert
//        assertTrue(result.isEmpty());
//    }
}