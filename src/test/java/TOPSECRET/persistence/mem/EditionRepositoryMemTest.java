package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.PublisherID;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EditionRepositoryMem}.
 *
 * <p>
 * Uses test doubles (mocks) to isolate repository behavior.
 * </p>
 */
class EditionRepositoryMemTest {

    // ─────────────────────────────────────────────
    // save
    // ─────────────────────────────────────────────

    @Test
    void save_shouldStoreEdition() {

        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionId editionIdDouble = mock(EditionId.class);
        when(editionDouble.id()).thenReturn(editionIdDouble);

        IEditionRepository repo = new EditionRepositoryMem();

        // Act
        Edition result = repo.save(editionDouble);

        // Assert
        assertEquals(editionDouble, result);
        assertTrue(repo.existsById(editionIdDouble));
    }

    @Test
    void save_shouldThrow_whenDuplicateEdition() {

        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionId editionIdDouble = mock(EditionId.class);
        when(editionDouble.id()).thenReturn(editionIdDouble);

        IEditionRepository repo = new EditionRepositoryMem();
        repo.save(editionDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.save(editionDouble));
    }

    @Test
    void save_shouldThrow_whenEditionIsNull() {

        // Arrange
        IEditionRepository repo = new EditionRepositoryMem();

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                repo.save(null));
    }

    // ─────────────────────────────────────────────
    // existsById
    // ─────────────────────────────────────────────

    @Test
    void existsById_shouldReturnTrue_whenEditionExists() {

        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionId editionIdDouble = mock(EditionId.class);
        when(editionDouble.id()).thenReturn(editionIdDouble);

        IEditionRepository repo = new EditionRepositoryMem();
        repo.save(editionDouble);

        // Act
        boolean result = repo.existsById(editionIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenEditionDoesNotExist() {

        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        IEditionRepository repo = new EditionRepositoryMem();

        // Act
        boolean result = repo.existsById(editionIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsById_shouldThrow_whenIdIsNull() {

        // Arrange
        IEditionRepository repo = new EditionRepositoryMem();

        // Act + Assert
        assertFalse(repo.existsById(null));
    }

    // ─────────────────────────────────────────────
    // findEditionById
    // ─────────────────────────────────────────────

    @Test
    void findEditionById_shouldReturn_whenFound() {

        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionId editionIdDouble = mock(EditionId.class);
        when(editionDouble.id()).thenReturn(editionIdDouble);

        IEditionRepository repo = new EditionRepositoryMem();
        repo.save(editionDouble);

        // Act
        Optional<Edition> result = repo.findById(editionIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(editionDouble, result.get());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {

        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        IEditionRepository repo = new EditionRepositoryMem();

        // Act
        Optional<Edition> result = repo.findById(editionIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldThrow_whenIdIsNull() {

        // Arrange
        IEditionRepository repo = new EditionRepositoryMem();

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                repo.findById(null));
    }

    // ─────────────────────────────────────────────
    // findAll
    // ─────────────────────────────────────────────

    @Test
    void findAll_shouldReturnAllEditions() {

        // Arrange
        Edition editionDouble1 = mock(Edition.class);
        Edition editionDouble2 = mock(Edition.class);

        when(editionDouble1.id()).thenReturn(mock(EditionId.class));
        when(editionDouble2.id()).thenReturn(mock(EditionId.class));

        IEditionRepository repo = new EditionRepositoryMem();
        repo.save(editionDouble1);
        repo.save(editionDouble2);

        // Act
        List<Edition> result = repo.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(editionDouble1));
        assertTrue(result.contains(editionDouble2));
    }

    @Test
    void findAll_shouldReturnUnmodifiableList() {

        // Arrange
        Edition editionDouble = mock(Edition.class);
        when(editionDouble.id()).thenReturn(mock(EditionId.class));

        IEditionRepository repo = new EditionRepositoryMem();
        repo.save(editionDouble);

        // Act
        List<Edition> result = repo.findAll();

        // Assert
        assertThrows(UnsupportedOperationException.class,
                () -> result.add(mock(Edition.class)));
    }

    // ─────────────────────────────────────────────
    // findEditionIdsByPublisherId
    // ─────────────────────────────────────────────

    @Test
    void findIdsByPublisherId_shouldReturnMatchingIds() {

        // Arrange
        PublisherID publisherIdDouble = mock(PublisherID.class);

        Edition editionDouble1 = mock(Edition.class);
        Edition editionDouble2 = mock(Edition.class);

        EditionId editionIdDouble1 = mock(EditionId.class);
        EditionId editionIdDouble2 = mock(EditionId.class);

        when(editionDouble1.id()).thenReturn(editionIdDouble1);
        when(editionDouble2.id()).thenReturn(editionIdDouble2);

        when(editionDouble1.getPublisherID()).thenReturn(publisherIdDouble);
        when(editionDouble2.getPublisherID()).thenReturn(publisherIdDouble);

        IEditionRepository repo = new EditionRepositoryMem();
        repo.save(editionDouble1);
        repo.save(editionDouble2);

        // Act
        List<EditionId> result = repo.findIdsByPublisherId(publisherIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(editionIdDouble1));
        assertTrue(result.contains(editionIdDouble2));
    }

    @Test
    void findIdsByPublisherId_shouldReturnEmpty_whenNoMatch() {

        // Arrange
        PublisherID publisherIdDouble = mock(PublisherID.class);
        IEditionRepository repo = new EditionRepositoryMem();

        // Act
        List<EditionId> result = repo.findIdsByPublisherId(publisherIdDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findIdsByPublisherId_shouldThrow_whenNull() {

        // Arrange
        IEditionRepository repo = new EditionRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.findIdsByPublisherId(null));
    }

    @Test
    void findByPublisherId_shouldReturnMatchingEditions() {
        // Arrange
        PublisherID publisherIdDouble = mock(PublisherID.class);

        Edition edition1Double = mock(Edition.class);
        Edition edition2Double = mock(Edition.class);

        EditionId editionId1Double = mock(EditionId.class);
        EditionId editionId2Double = mock(EditionId.class);

        when(edition1Double.id()).thenReturn(editionId1Double);
        when(edition2Double.id()).thenReturn(editionId2Double);

        when(edition1Double.getPublisherID()).thenReturn(publisherIdDouble);
        when(edition2Double.getPublisherID()).thenReturn(publisherIdDouble);

        IEditionRepository repo = new EditionRepositoryMem();

        repo.save(edition1Double);
        repo.save(edition2Double);

        // Act
        List<Edition> result = repo.findByPublisherId(publisherIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(edition1Double));
        assertTrue(result.contains(edition2Double));
    }

    @Test
    void findByPublisherId_shouldReturnEmptyList_whenNoMatches() {
        // Arrange
        PublisherID publisherIdDouble = mock(PublisherID.class);

        Edition editionDouble = mock(Edition.class);
        EditionId editionIdDouble = mock(EditionId.class);
        when(editionDouble.id()).thenReturn(editionIdDouble);

        PublisherID otherPublisherIdDouble = mock(PublisherID.class);

        when(editionDouble.getPublisherID()).thenReturn(otherPublisherIdDouble);

        IEditionRepository repo = new EditionRepositoryMem();

        repo.save(editionDouble);

        // Act
        List<Edition> result = repo.findByPublisherId(publisherIdDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByPublisherId_shouldThrow_whenNull() {
        // Arrange

        IEditionRepository repo = new EditionRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findByPublisherId(null));
    }

    @Test
    void findByPublicationTypeId_shouldReturnMatchingEditions() {

        // Arrange
        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);

        Edition edition1Double = mock(Edition.class);
        Edition edition2Double = mock(Edition.class);

        EditionId editionId1Double = mock(EditionId.class);
        EditionId editionId2Double = mock(EditionId.class);

        when(edition1Double.id()).thenReturn(editionId1Double);
        when(edition2Double.id()).thenReturn(editionId2Double);

        when(edition1Double.getPublicationTypeID()).thenReturn(publicationTypeIdDouble);
        when(edition2Double.getPublicationTypeID()).thenReturn(publicationTypeIdDouble);

        IEditionRepository repo = new EditionRepositoryMem();

        repo.save(edition1Double);
        repo.save(edition2Double);

        // Act
        List<Edition> result = repo.findByPublicationTypeId(publicationTypeIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(edition1Double));
        assertTrue(result.contains(edition2Double));
    }

    @Test
    void findByPublicationTypeId_shouldReturnEmptyList_whenNoMatches() {

        // Arrange
        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);

        Edition editionDouble = mock(Edition.class);
        PublicationTypeId otherPublicationTypeIdDouble = mock(PublicationTypeId.class);

        when(editionDouble.getPublicationTypeID()).thenReturn(otherPublicationTypeIdDouble);

        IEditionRepository repo = new EditionRepositoryMem();

        repo.save(editionDouble);

        // Act
        List<Edition> result = repo.findByPublicationTypeId(publicationTypeIdDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByPublicationTypeId_shouldThrow_whenNull() {

        // Arrange

        IEditionRepository repo = new EditionRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findByPublicationTypeId(null));
    }
}