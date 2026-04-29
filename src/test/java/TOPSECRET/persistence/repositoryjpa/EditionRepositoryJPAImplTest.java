package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.persistence.repositoryjpa.assembler.EditionAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.EditionDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IEditionRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Integration-style unit tests for {@link EditionRepositoryJPAImpl}.
 *
 * <p>
 * This test class verifies the behavior of the JPA repository implementation
 * using a Spring Boot test context with mocked dependencies.
 * </p>
 *
 * <p>
 * The {@link EditionAssembler} and {@link IEditionRepositoryJpa} are mocked
 * to isolate the repository logic and ensure that only the orchestration
 * between persistence and mapping layers is tested.
 * </p>
 *
 * <p>
 * The test suite covers:
 * <ul>
 *     <li>Saving an {@link Edition}.</li>
 *     <li>Checking existence by ID.</li>
 *     <li>Retrieving an Edition by ID.</li>
 *     <li>Retrieving all Editions.</li>
 *     <li>Filtering Edition IDs by Publisher.</li>
 * </ul>
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class EditionRepositoryJPAImplTest {

    @Mock
    private EditionAssembler assembler;

    @Mock
    private IEditionRepositoryJpa jpa;

    @InjectMocks
    private EditionRepositoryJPAImpl repo;

    // ─────────────────────────────────────────────
    // save
    // ─────────────────────────────────────────────
    @Test
    void save_shouldPersistAndReturnEdition() {

        // Arrange
        Edition editionDouble = mock(Edition.class);
        EditionDM editionDM = mock(EditionDM.class);

        when(assembler.toDM(editionDouble)).thenReturn(editionDM);
        when(jpa.save(editionDM)).thenReturn(editionDM);
        when(assembler.toDomain(editionDM)).thenReturn(editionDouble);

        // Act
        Edition result = repo.save(editionDouble);

        // Assert
        assertNotNull(result);
        assertEquals(editionDouble, result);
    }

    // ─────────────────────────────────────────────
    // existsById
    // ─────────────────────────────────────────────

    @Test
    void existsById_shouldReturnFalse_whenEditionDoesNotExist() {
        // Arrange
        EditionID editionIdDouble = mock(EditionID.class);

        when(jpa.existsById(editionIdDouble)).thenReturn(false);

        // Act
        boolean result = repo.existsById(editionIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsById_shouldReturnTrue_whenExists() {

        // Arrange
        EditionID id = mock(EditionID.class);
        when(jpa.existsById(id)).thenReturn(true);

        // Act
        boolean result = repo.existsById(id);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_shouldThrow_whenNull() {

        // Arrange

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.existsById(null));
    }

    // ─────────────────────────────────────────────
    // findEditionById
    // ─────────────────────────────────────────────

    @Test
    void findEditionById_shouldReturn_whenFound() {

        // Arrange
        EditionID id = mock(EditionID.class);
        EditionDM dm = mock(EditionDM.class);
        Edition edition = mock(Edition.class);

        when(jpa.findById(id)).thenReturn(Optional.of(dm));
        when(assembler.toDomain(dm)).thenReturn(edition);

        // Act
        Optional<Edition> result = repo.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(edition, result.get());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {

        // Arrange
        EditionID id = mock(EditionID.class);
        when(jpa.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Edition> result = repo.findById(id);

        // Assert
        assertTrue(result.isEmpty());

    }

    // ─────────────────────────────────────────────
    // findAll
    // ─────────────────────────────────────────────

    @Test
    void findAll_shouldReturnMappedEditions() {

        // Arrange
        EditionDM dm1 = mock(EditionDM.class);
        EditionDM dm2 = mock(EditionDM.class);

        Edition e1 = mock(Edition.class);
        Edition e2 = mock(Edition.class);

        when(jpa.findAll()).thenReturn(List.of(dm1, dm2));
        when(assembler.toDomain(dm1)).thenReturn(e1);
        when(assembler.toDomain(dm2)).thenReturn(e2);

        // Act
        List<Edition> result = repo.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(e1, result.get(0));
        assertEquals(e2, result.get(1));

    }

    // ─────────────────────────────────────────────
    // findEditionIdsByPublisherId
    // ─────────────────────────────────────────────

    @Test
    void findIdsByPublisherId_shouldReturnMatchingIds() {

        // Arrange
        PublisherID publisherID = mock(PublisherID.class);

        EditionDM dm1 = mock(EditionDM.class);
        EditionDM dm2 = mock(EditionDM.class);

        EditionID id1 = mock(EditionID.class);
        EditionID id2 = mock(EditionID.class);

        when(dm1.getPublisherId()).thenReturn(publisherID);
        when(dm1.getEditionID()).thenReturn(id1);

        when(dm2.getPublisherId()).thenReturn(mock(PublisherID.class));

        when(jpa.findAll()).thenReturn(List.of(dm1, dm2));

        // Act
        List<EditionID> result = repo.findIdsByPublisherId(publisherID);

        // Assert
        assertEquals(1, result.size());
        assertEquals(id1, result.get(0));

    }

    @Test
    void findIdsByPublisherId_shouldThrow_whenNull() {

        // Arrange
        repo = new EditionRepositoryJPAImpl(jpa, assembler);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                repo.findIdsByPublisherId(null));
    }

    @Test
    void findByPublicationTypeId_shouldReturnEditions_whenFound() {
        // Arrange

        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);

        EditionDM editionDMDouble1 = mock(EditionDM.class);
        EditionDM editionDMDouble2 = mock(EditionDM.class);

        Edition editionDouble1 = mock(Edition.class);
        Edition editionDouble2 = mock(Edition.class);

        when(jpa.findByPublicationTypeId(publicationTypeIdDouble))
                .thenReturn(List.of(editionDMDouble1, editionDMDouble2));

        when(assembler.toDomain(editionDMDouble1)).thenReturn(editionDouble1);
        when(assembler.toDomain(editionDMDouble2)).thenReturn(editionDouble2);

        // Act
        List<Edition> result = repo.findByPublicationTypeId(publicationTypeIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(editionDouble1));
        assertTrue(result.contains(editionDouble2));
    }

    @Test
    void findByPublisherId_shouldReturnEditions_whenFound() {
        // Arrange
        PublisherID publisherIdDouble = mock(PublisherID.class);

        EditionDM editionDMDouble1 = mock(EditionDM.class);
        EditionDM editionDMDouble2 = mock(EditionDM.class);

        Edition editionDouble1 = mock(Edition.class);
        Edition editionDouble2 = mock(Edition.class);

        when(jpa.findByPublisherId(publisherIdDouble))
                .thenReturn(List.of(editionDMDouble1, editionDMDouble2));

        when(assembler.toDomain(editionDMDouble1)).thenReturn(editionDouble1);
        when(assembler.toDomain(editionDMDouble2)).thenReturn(editionDouble2);

        // Act
        List<Edition> result = repo.findByPublisherId(publisherIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(editionDouble1));
        assertTrue(result.contains(editionDouble2));
    }
}