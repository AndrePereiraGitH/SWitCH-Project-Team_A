package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.assembler.PublicationSampleAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublicationSampleDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IPublicationSampleRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
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
 * Unit tests for {@link PublicationSampleRepositoryJpaImpl}.
 *
 * <p>Tests the JPA-based implementation of {@link IPublicationSampleRepository}, which acts as
 * an anti-corruption layer between the domain model and the JPA persistence layer. All conversions
 * between domain objects ({@link PublicationSample}) and data model objects ({@link PublicationSampleDM})
 * are delegated to {@link PublicationSampleAssembler}.</p>
 *
 * <p>Tests are organized into the following sections:</p>
 * <ul>
 *   <li><b>save</b> — persisting a new sample and null guards.</li>
 *   <li><b>existsById</b> — presence checks with true/false outcomes and null guards.</li>
 *   <li><b>findAll</b> — retrieval of zero, one, or many samples.</li>
 *   <li><b>findById</b> — retrieval by identity, empty results, and null guards.</li>
 *   <li><b>findPubSampleIdsByBibliographicalDataId</b> — filtering by bibliographical data ID with multiple/single/no matches.</li>
 *   <li><b>findIdsByBibliographicalDataId</b> — retrieving IDs by bibliographical data with empty list and null guards.</li>
 *   <li><b>findIdsByBibliographicalDataIdIn</b> — batch retrieval of IDs by multiple bibliographical data IDs.</li>
 *   <li><b>findIdsByEditionIdIn</b> — batch retrieval of IDs by edition IDs with null/empty/match handling.</li>
 *   <li><b>findByLibraryId</b> — retrieval of samples by library with mapping and null guards.</li>
 *   <li><b>findByUserIdAndLibraryIdIsNotNull</b> — filtered retrieval of user publications in libraries.</li>
 *   <li><b>findByUserIdAndLibraryIdIsNull</b> — filtered retrieval of user publications not in libraries.</li>
 *   <li><b>findByCollectionOfPublicationsId</b> — retrieval of samples by collection ID.</li>
 *   <li><b>findByLibraryIdAndPublicationStatusNotIn</b> — retrieval excluding specific publication statuses.</li>
 * </ul>
 *
 * @see PublicationSampleRepositoryJpaImpl
 * @see IPublicationSampleRepository
 * @see PublicationSampleAssembler
 */

@ExtendWith(MockitoExtension.class)
class PublicationSampleRepositoryJpaImplTest {

    @Mock
    private PublicationSampleAssembler assembler;

    @Mock
    private IPublicationSampleRepositoryJpa jpa;

    @InjectMocks
    private PublicationSampleRepositoryJpaImpl repository;

    private PublicationSampleId publicationSampleIdDouble;
    private PublicationSample publicationSampleDouble;
    private PublicationSampleDM publicationSampleDMDouble;
    private UserId userIdDouble;
    private LibraryId libraryIdDouble;
    private BibliographicalDataId bdIdDouble;

    @BeforeEach
    void setup() {

        publicationSampleIdDouble = mock(PublicationSampleId.class);
        publicationSampleDouble = mock(PublicationSample.class);
        publicationSampleDMDouble = mock(PublicationSampleDM.class);

        userIdDouble = mock(UserId.class);
        libraryIdDouble = mock(LibraryId.class);
        bdIdDouble = mock(BibliographicalDataId.class);
    }

    // =========================================================================
    // save
    // =========================================================================

    @Test
    void save_shouldReturnSavedPublicationSample() {

        // Arrange
        when(assembler.toDM(publicationSampleDouble))
                .thenReturn(publicationSampleDMDouble);

        when(jpa.save(publicationSampleDMDouble))
                .thenReturn(publicationSampleDMDouble);

        when(assembler.toDomain(publicationSampleDMDouble))
                .thenReturn(publicationSampleDouble);

        // Act
        PublicationSample result =
                repository.save(publicationSampleDouble);

        // Assert
        assertNotNull(result);
        assertEquals(publicationSampleDouble, result);
    }


    @Test
    void save_shouldThrowNullPointerException_whenPublicationSampleIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.save(null));
    }

    // =========================================================================
    // existsById
    // =========================================================================

    @Test
    void existsById_shouldReturnTrue_whenPublicationExists() {

        // Arrange
        when(jpa.existsById(publicationSampleIdDouble))
                .thenReturn(true);

        // Act
        boolean result =
                repository.existsById(publicationSampleIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenPublicationDoesNotExist() {

        // Arrange
        when(jpa.existsById(publicationSampleIdDouble))
                .thenReturn(false);

        // Act
        boolean result =
                repository.existsById(publicationSampleIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsById_shouldThrowNullPointerException_whenIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.existsById(null));
    }


    // =========================================================================
    // findAll
    // =========================================================================

    @Test
    void findAll_shouldReturnEmptyList_whenRepositoryContainsNoPublications() {

        // Arrange
        when(jpa.findAll()).thenReturn(List.of());

        // Act
        List<PublicationSample> result =
                repository.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnListWithOneElement_whenOnePublicationWasAdded() {

        // Arrange
        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);

        PublicationSample sample1 =
                mock(PublicationSample.class);

        when(jpa.findAll())
                .thenReturn(List.of(dm1));

        when(assembler.toDomain(dm1))
                .thenReturn(sample1);

        // Act
        List<PublicationSample> result =
                repository.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(sample1, result.get(0));
    }

    @Test
    void findAll_shouldReturnMappedPublicationSamples() {

        // Arrange
        PublicationSampleDM secondDM = mock(PublicationSampleDM.class);

        PublicationSample secondPublication =
                mock(PublicationSample.class);

        when(jpa.findAll())
                .thenReturn(List.of(publicationSampleDMDouble, secondDM));

        when(assembler.toDomain(publicationSampleDMDouble))
                .thenReturn(publicationSampleDouble);

        when(assembler.toDomain(secondDM))
                .thenReturn(secondPublication);

        // Act
        List<PublicationSample> result =
                repository.findAll();

        // Assert
        assertEquals(2, result.size());

        assertTrue(result.contains(publicationSampleDouble));
        assertTrue(result.contains(secondPublication));
    }

    // =========================================================================
    // findById
    // =========================================================================

    @Test
    void findById_shouldReturnPublicationSample_whenIdExists() {

        // Arrange
        when(jpa.findById(publicationSampleIdDouble))
                .thenReturn(Optional.of(publicationSampleDMDouble));

        when(assembler.toDomain(publicationSampleDMDouble))
                .thenReturn(publicationSampleDouble);

        // Act
        Optional<PublicationSample> result =
                repository.findById(publicationSampleIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(publicationSampleDouble, result.get());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() {

        // Arrange
        when(jpa.findById(publicationSampleIdDouble))
                .thenReturn(Optional.empty());

        // Act
        Optional<PublicationSample> result =
                repository.findById(publicationSampleIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldThrowNullPointerException_whenIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findById(null));
    }

    // =========================================================================
    // findPubSampleIdsByBibliographicalDataId
    // =========================================================================

    @Test
    void findPubSampleIdsByBibliographicalDataId_shouldReturnListWithMultipleId_whenMultipleMatchesBdId() {

        // Arrange
        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm2 = mock(PublicationSampleDM.class);

        PublicationSampleId id1 = mock(PublicationSampleId.class);
        PublicationSampleId id2 = mock(PublicationSampleId.class);

        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        when(jpa.findAllByBdId(bdId))
                .thenReturn(List.of(dm1, dm2));

        when(dm1.getPubSampleId()).thenReturn(id1);
        when(dm2.getPubSampleId()).thenReturn(id2);

        // Act
        List<PublicationSampleId> result =
                repository.findIdsByBibliographicalDataId(bdId);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(id1));
        assertTrue(result.contains(id2));
    }

    @Test
    void findPubSampleIdsByBibliographicalDataId_shouldReturnListWithOneId_whenOneMatchesBdId() {

        // Arrange
        PublicationSampleDM dm = mock(PublicationSampleDM.class);

        PublicationSampleId pubId = mock(PublicationSampleId.class);

        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        when(jpa.findAllByBdId(bdId))
                .thenReturn(List.of(dm));

        when(dm.getPubSampleId())
                .thenReturn(pubId);

        // Act
        List<PublicationSampleId> result =
                repository.findIdsByBibliographicalDataId(bdId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(pubId, result.get(0));
    }

    @Test
    void findPubSampleByBibliographicalId_shouldThrowNullPointerException_whenBdIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findById(null));
    }

    // =========================================================================
    // findIdsByBibliographicalDataId
    // =========================================================================

    @Test
    void findIdsByBibliographicalDataId_shouldReturnEmptyList_whenNoMatchesExist() {

        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        when(jpa.findAllByBdId(bdId))
                .thenReturn(List.of());

        // Act
        List<PublicationSampleId> result =
                repository.findIdsByBibliographicalDataId(bdId);

        // Assert
        assertTrue(result.isEmpty());
    }


    @Test
    void findIdsByBibliographicalDataId_shouldThrowNullPointerException_whenBdIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findIdsByBibliographicalDataId(null));
    }

    // =========================================================================
    // findIdsByBibliographicalDataIdIn
    // =========================================================================

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnEmptyList_whenNoMatchesExist() {
        // Arrange
        List<BibliographicalDataId> ids = List.of(mock(BibliographicalDataId.class));
        when(jpa.findAllByBdIdIn(ids)).thenReturn(List.of());

        // Act
        List<PublicationSampleId> result = repository.findIdsByBibliographicalDataIdIn(ids);

        // Assert
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnPublicationSampleIds_whenMatchesExist() {
        // Arrange
        BibliographicalDataId bdId1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId2 = mock(BibliographicalDataId.class);
        List<BibliographicalDataId> ids = List.of(bdId1, bdId2);

        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm2 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm3 = mock(PublicationSampleDM.class);

        PublicationSampleId id1 = mock(PublicationSampleId.class);
        PublicationSampleId id2 = mock(PublicationSampleId.class);
        PublicationSampleId id3 = mock(PublicationSampleId.class);

        when(jpa.findAllByBdIdIn(ids))
                .thenReturn(List.of(dm1, dm2, dm3));

        when(dm1.getPubSampleId()).thenReturn(id1);
        when(dm2.getPubSampleId()).thenReturn(id2);
        when(dm3.getPubSampleId()).thenReturn(id3);

        // Act
        List<PublicationSampleId> result = repository.findIdsByBibliographicalDataIdIn(ids);

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(id1));
        assertTrue(result.contains(id2));
        assertTrue(result.contains(id3));
        assertEquals(id1, result.get(0));
        assertEquals(id2, result.get(1));
        assertEquals(id3, result.get(2));
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnSingleId_whenOneMatchExists() {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        List<BibliographicalDataId> ids = List.of(bdId);

        PublicationSampleDM dm = mock(PublicationSampleDM.class);
        PublicationSampleId pubId = mock(PublicationSampleId.class);

        when(jpa.findAllByBdIdIn(ids))
                .thenReturn(List.of(dm));

        when(dm.getPubSampleId()).thenReturn(pubId);

        // Act
        List<PublicationSampleId> result = repository.findIdsByBibliographicalDataIdIn(ids);

        // Assert
        assertEquals(1, result.size());
        assertEquals(pubId, result.get(0));
        assertTrue(result.contains(pubId));
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldThrowNullPointerException_whenBdIdListIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findIdsByBibliographicalDataIdIn(null));
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnEmptyList_whenEmptyListProvided() {
        // Arrange
        List<BibliographicalDataId> ids = List.of();
        when(jpa.findAllByBdIdIn(ids)).thenReturn(List.of());

        // Act
        List<PublicationSampleId> result = repository.findIdsByBibliographicalDataIdIn(ids);

        // Assert
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    // =========================================================================
    // findIdsByEditionIdIn
    // =========================================================================

    @Test
    void findIdsByEditionIdIn_shouldThrowException_whenListOfEditionIdsIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findIdsByEditionIdIn(null));
    }

    @Test
    void findIdsByEditionIdIn_shouldReturnEmptyList_whenListIsEmpty() {

        // Arrange
        List<EditionId> editionIds = List.of();

        // Act
        List<PublicationSampleId> result =
                repository.findIdsByEditionIdIn(editionIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findIdsByEditionIdIn_shouldReturnPublicationSampleIds_whenMatchesExist() {

        // Arrange
        EditionId editionId = mock(EditionId.class);

        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm2 = mock(PublicationSampleDM.class);

        PublicationSampleId id1 = mock(PublicationSampleId.class);
        PublicationSampleId id2 = mock(PublicationSampleId.class);

        List<EditionId> editionIds = List.of(editionId);

        when(jpa.findAllByEditionIdIn(editionIds))
                .thenReturn(List.of(dm1, dm2));

        when(dm1.getPubSampleId()).thenReturn(id1);
        when(dm2.getPubSampleId()).thenReturn(id2);

        // Act
        List<PublicationSampleId> result =
                repository.findIdsByEditionIdIn(editionIds);

        // Assert
        assertEquals(2, result.size());
        assertEquals(id1, result.get(0));
        assertEquals(id2, result.get(1));
    }

    @Test
    void findIdsByEditionIdIn_shouldReturnEmptyList_whenNoMatchesExist() {
        // Arrange
        EditionId editionId = mock(EditionId.class);
        List<EditionId> editionIds = List.of(editionId);

        when(jpa.findAllByEditionIdIn(editionIds)).thenReturn(List.of());

        // Act
        List<PublicationSampleId> result = repository.findIdsByEditionIdIn(editionIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
// findByBibliographicalDataId
// =========================================================================

    @Test
    void findByBibliographicalDataId_shouldReturnMappedPublications_whenMultipleMatchExist() {

        // Arrange
        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm2 = mock(PublicationSampleDM.class);

        PublicationSample sample1 = mock(PublicationSample.class);
        PublicationSample sample2 = mock(PublicationSample.class);

        when(jpa.findAllByBdId(bdIdDouble))
                .thenReturn(List.of(dm1, dm2));

        when(assembler.toDomain(dm1)).thenReturn(sample1);
        when(assembler.toDomain(dm2)).thenReturn(sample2);

        // Act
        List<PublicationSample> result =
                repository.findByBibliographicalDataId(bdIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(sample1));
        assertTrue(result.contains(sample2));
    }

    @Test
    void findByBibliographicalDataId_shouldReturnOnePublication_whenOneMatchExists() {

        // Arrange
        PublicationSampleDM dm = mock(PublicationSampleDM.class);
        PublicationSample sample = mock(PublicationSample.class);

        when(jpa.findAllByBdId(bdIdDouble))
                .thenReturn(List.of(dm));

        when(assembler.toDomain(dm)).thenReturn(sample);

        // Act
        List<PublicationSample> result =
                repository.findByBibliographicalDataId(bdIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals(sample, result.get(0));
    }

    @Test
    void findByBibliographicalDataId_shouldReturnEmptyList_whenNoMatchExists() {

        // Arrange
        when(jpa.findAllByBdId(bdIdDouble))
                .thenReturn(List.of());

        // Act
        List<PublicationSample> result =
                repository.findByBibliographicalDataId(bdIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByBibliographicalDataId_shouldThrowNullPointerException_whenBdIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findByBibliographicalDataId(null));
    }

// =========================================================================
// findByBibliographicalDataIdIn
// =========================================================================

    @Test
    void findByBibliographicalDataIdIn_shouldReturnMappedPublications_whenMultipleMatchesExist() {

        // Arrange
        BibliographicalDataId bdId1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId2 = mock(BibliographicalDataId.class);
        List<BibliographicalDataId> ids = List.of(bdId1, bdId2);

        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm2 = mock(PublicationSampleDM.class);

        PublicationSample sample1 = mock(PublicationSample.class);
        PublicationSample sample2 = mock(PublicationSample.class);

        when(jpa.findAllByBdIdIn(ids))
                .thenReturn(List.of(dm1, dm2));

        when(assembler.toDomain(dm1)).thenReturn(sample1);
        when(assembler.toDomain(dm2)).thenReturn(sample2);

        // Act
        List<PublicationSample> result =
                repository.findByBibliographicalDataIdIn(ids);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(sample1));
        assertTrue(result.contains(sample2));
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnOnePublication_whenOneMatchExists() {

        // Arrange
        List<BibliographicalDataId> ids = List.of(bdIdDouble);

        PublicationSampleDM dm = mock(PublicationSampleDM.class);
        PublicationSample sample = mock(PublicationSample.class);

        when(jpa.findAllByBdIdIn(ids))
                .thenReturn(List.of(dm));

        when(assembler.toDomain(dm)).thenReturn(sample);

        // Act
        List<PublicationSample> result =
                repository.findByBibliographicalDataIdIn(ids);

        // Assert
        assertEquals(1, result.size());
        assertEquals(sample, result.get(0));
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnEmptyList_whenNoMatchExists() {

        // Arrange
        List<BibliographicalDataId> ids = List.of(bdIdDouble);

        when(jpa.findAllByBdIdIn(ids))
                .thenReturn(List.of());

        // Act
        List<PublicationSample> result =
                repository.findByBibliographicalDataIdIn(ids);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnEmptyList_whenEmptyListProvided() {

        // Arrange
        List<BibliographicalDataId> ids = List.of();

        when(jpa.findAllByBdIdIn(ids))
                .thenReturn(List.of());

        // Act
        List<PublicationSample> result =
                repository.findByBibliographicalDataIdIn(ids);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByBibliographicalDataIdIn_shouldThrowNullPointerException_whenBdIdListIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findByBibliographicalDataIdIn(null));
    }

    // =========================================================================
    // findByEditionIdIn
    // =========================================================================

    @Test
    void findByEditionIdIn_shouldReturnMappedPublications_whenMultipleMatchesExist() {

        // Arrange
        EditionId editionId1 = mock(EditionId.class);
        EditionId editionId2 = mock(EditionId.class);
        List<EditionId> editionIds = List.of(editionId1, editionId2);

        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm2 = mock(PublicationSampleDM.class);

        PublicationSample sample1 = mock(PublicationSample.class);
        PublicationSample sample2 = mock(PublicationSample.class);

        when(jpa.findAllByEditionIdIn(editionIds))
                .thenReturn(List.of(dm1, dm2));

        when(assembler.toDomain(dm1)).thenReturn(sample1);
        when(assembler.toDomain(dm2)).thenReturn(sample2);

        // Act
        List<PublicationSample> result =
                repository.findByEditionIdIn(editionIds);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(sample1));
        assertTrue(result.contains(sample2));
    }

    @Test
    void findByEditionIdIn_shouldReturnOnePublication_whenOneMatchExists() {

        // Arrange
        EditionId editionId = mock(EditionId.class);
        List<EditionId> editionIds = List.of(editionId);

        PublicationSampleDM dm = mock(PublicationSampleDM.class);
        PublicationSample sample = mock(PublicationSample.class);

        when(jpa.findAllByEditionIdIn(editionIds))
                .thenReturn(List.of(dm));

        when(assembler.toDomain(dm)).thenReturn(sample);

        // Act
        List<PublicationSample> result =
                repository.findByEditionIdIn(editionIds);

        // Assert
        assertEquals(1, result.size());
        assertEquals(sample, result.get(0));
    }

    @Test
    void findByEditionIdIn_shouldReturnEmptyList_whenNoMatchExists() {

        // Arrange
        EditionId editionId = mock(EditionId.class);
        List<EditionId> editionIds = List.of(editionId);

        when(jpa.findAllByEditionIdIn(editionIds))
                .thenReturn(List.of());

        // Act
        List<PublicationSample> result =
                repository.findByEditionIdIn(editionIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByEditionIdIn_shouldReturnEmptyList_whenEmptyListProvided() {

        // Arrange
        List<EditionId> editionIds = List.of();

        when(jpa.findAllByEditionIdIn(editionIds))
                .thenReturn(List.of());

        // Act
        List<PublicationSample> result =
                repository.findByEditionIdIn(editionIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByEditionIdIn_shouldThrowNullPointerException_whenEditionIdListIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findByEditionIdIn(null));
    }

    // =========================================================================
    // findByLibraryId
    // =========================================================================

    @Test
    void findByLibraryId_shouldReturnEmptyList() {
        LibraryId libraryId = mock(LibraryId.class);
        List<PublicationSample> result = repository.findByLibraryId(libraryId);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByLibraryId_shouldThrow_whenLibraryIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findByLibraryId(null));
    }

    @Test
    void findByLibraryId_shouldReturnMappedPublicationSamples() {

        // Arrange
        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm2 = mock(PublicationSampleDM.class);

        PublicationSample sample1 = mock(PublicationSample.class);
        PublicationSample sample2 = mock(PublicationSample.class);

        when(jpa.findByLibraryId(libraryIdDouble))
                .thenReturn(List.of(dm1, dm2));

        when(assembler.toDomain(dm1)).thenReturn(sample1);
        when(assembler.toDomain(dm2)).thenReturn(sample2);

        // Act
        List<PublicationSample> result =
                repository.findByLibraryId(libraryIdDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void findByLibraryId_shouldReturnEmptyList_whenRepositoryReturnsEmptyList() {

        // Arrange
        when(jpa.findByLibraryId(libraryIdDouble))
                .thenReturn(List.of());

        // Act
        List<PublicationSample> result =
                repository.findByLibraryId(libraryIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findByUserIdAndLibraryIdIsNotNull
    // =========================================================================

    @Test
    void findByUserIdAndLibraryIdIsNotNull_WhenUserHasPublicationsInLibrary_ShouldReturnFilteredList() {

        // Arrange
        PublicationSampleDM dm_1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_2 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_3 = mock(PublicationSampleDM.class);

        UserId id = mock(UserId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        PublicationSample pbSample_3 = mock(PublicationSample.class);

        when(jpa.findAllByOwnerId(id))
                .thenReturn(List.of(dm_1, dm_2, dm_3));

        when(assembler.toDomain(dm_1))
                .thenReturn(pbSample_1);

        when(assembler.toDomain(dm_2))
                .thenReturn(pbSample_2);

        when(assembler.toDomain(dm_3))
                .thenReturn(pbSample_3);

        when(pbSample_1.isInUserLibrary()).thenReturn(true);
        when(pbSample_2.isInUserLibrary()).thenReturn(true);
        when(pbSample_3.isInUserLibrary()).thenReturn(true);

        // Act
        List<PublicationSample> result =
                repository.findByUserIdAndLibraryIdIsNotNull(id);

        // Assert
        assertEquals(3, result.size());
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldReturnFilteredList() {

        // Arrange
        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm2 = mock(PublicationSampleDM.class);

        PublicationSample sample1 = mock(PublicationSample.class);
        PublicationSample sample2 = mock(PublicationSample.class);

        when(jpa.findAllByOwnerId(userIdDouble))
                .thenReturn(List.of(dm1, dm2));

        when(assembler.toDomain(dm1)).thenReturn(sample1);
        when(assembler.toDomain(dm2)).thenReturn(sample2);

        when(sample1.isInUserLibrary()).thenReturn(true);
        when(sample2.isInUserLibrary()).thenReturn(false);

        // Act
        List<PublicationSample> result =
                repository.findByUserIdAndLibraryIdIsNotNull(userIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals(sample1, result.get(0));
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldThrowNullPointerException_whenUserIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findByUserIdAndLibraryIdIsNotNull(null));
    }

    // =========================================================================
    // findByUserIdAndLibraryIdIsNull
    // =========================================================================

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldReturnOnlyPublicationsWithoutLibrary() {

        // Arrange
        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm2 = mock(PublicationSampleDM.class);

        PublicationSample sample1 = mock(PublicationSample.class);
        PublicationSample sample2 = mock(PublicationSample.class);

        when(jpa.findAllByOwnerId(userIdDouble))
                .thenReturn(List.of(dm1, dm2));

        when(assembler.toDomain(dm1)).thenReturn(sample1);
        when(assembler.toDomain(dm2)).thenReturn(sample2);

        when(sample1.isInUserLibrary()).thenReturn(false);
        when(sample2.isInUserLibrary()).thenReturn(true);

        // Act
        List<PublicationSample> result =
                repository.findByUserIdAndLibraryIdIsNull(userIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals(sample1, result.get(0));
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldReturnEmptyList_whenNoMatchesExist() {

        // Arrange
        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);

        PublicationSample sample1 = mock(PublicationSample.class);

        when(jpa.findAllByOwnerId(userIdDouble))
                .thenReturn(List.of(dm1));

        when(assembler.toDomain(dm1)).thenReturn(sample1);

        when(sample1.isInUserLibrary()).thenReturn(true);

        // Act
        List<PublicationSample> result =
                repository.findByUserIdAndLibraryIdIsNull(userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldThrowNullPointerException_whenUserIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findByUserIdAndLibraryIdIsNull(null));
    }

    // =========================================================================
    // findByCollectionOfPublicationsId
    // =========================================================================

    @Test
    void findByCollectionOfPublicationsId_shouldReturnMappedList() {

        PublicationSampleDM dm = mock(PublicationSampleDM.class);
        PublicationSample domain = mock(PublicationSample.class);
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(jpa.findAllByCollectionOfPublicationsId(id))
                .thenReturn(List.of(dm));

        when(assembler.toDomain(dm)).thenReturn(domain);

        List<PublicationSample> result =
                repository.findByCollectionOfPublicationsId(id);

        assertEquals(1, result.size());
    }

    // =========================================================================
    // findByLibraryIdAndPublicationStatusNotIn
    // =========================================================================

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnMappedList() {

        LibraryId id = mock(LibraryId.class);
        PublicationSampleDM dm = mock(PublicationSampleDM.class);
        PublicationSample domain = mock(PublicationSample.class);

        List<PublicationStatus> excluded =
                List.of(PublicationStatus.SOLD, PublicationStatus.FOR_SALE);

        when(jpa.findAllByLibraryIdAndPublicationStatusNotIn(id, excluded))
                .thenReturn(List.of(dm));

        when(assembler.toDomain(dm)).thenReturn(domain);

        List<PublicationSample> result =
                repository.findByLibraryIdAndPublicationStatusNotIn(id,excluded);

        assertEquals(1, result.size());
    }

}