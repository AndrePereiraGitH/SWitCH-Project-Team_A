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
    private BibliographicalDataID bdIdDouble;

    @BeforeEach
    void setup() {

        publicationSampleIdDouble = mock(PublicationSampleId.class);
        publicationSampleDouble = mock(PublicationSample.class);
        publicationSampleDMDouble = mock(PublicationSampleDM.class);

        userIdDouble = mock(UserId.class);
        libraryIdDouble = mock(LibraryId.class);
        bdIdDouble = mock(BibliographicalDataID.class);
    }

    // =========================================================================
    // save
    // =========================================================================

    @Test
    void save_shouldReturnSavedPublicationSample() {

        // Arrange
        when(publicationSampleDouble.id()).thenReturn(publicationSampleIdDouble);

        when(jpa.existsById(publicationSampleIdDouble))
                .thenReturn(false);

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
    void save_shouldThrowIllegalArgumentException_whenPublicationAlreadyExists() {

        // Arrange
        when(publicationSampleDouble.id()).thenReturn(publicationSampleIdDouble);

        when(jpa.existsById(publicationSampleIdDouble))
                .thenReturn(true);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(publicationSampleDouble));
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
    // update
    // =========================================================================

    @Test
    void update_shouldReturnUpdatedPublicationSample() {

        // Arrange
        when(assembler.toDM(publicationSampleDouble))
                .thenReturn(publicationSampleDMDouble);

        when(jpa.save(publicationSampleDMDouble))
                .thenReturn(publicationSampleDMDouble);

        when(assembler.toDomain(publicationSampleDMDouble))
                .thenReturn(publicationSampleDouble);

        // Act
        PublicationSample result =
                repository.update(publicationSampleDouble);

        // Assert
        assertEquals(publicationSampleDouble, result);
    }

    @Test
    void update_shouldThrowNullPointerException_whenPublicationSampleIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.update(null));
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
    // findIdsByBibliographicalDataId
    // =========================================================================

    @Test
    void findIdsByBibliographicalDataId_shouldReturnMatchingIds() {

        // Arrange
        PublicationSampleDM secondDM = mock(PublicationSampleDM.class);

        PublicationSampleId secondId =
                mock(PublicationSampleId.class);

        when(publicationSampleDMDouble.getPubSampleId())
                .thenReturn(publicationSampleIdDouble);

        when(secondDM.getPubSampleId())
                .thenReturn(secondId);

        when(jpa.findAllByBdId(bdIdDouble))
                .thenReturn(List.of(publicationSampleDMDouble, secondDM));

        // Act
        List<PublicationSampleId> result =
                repository.findIdsByBibliographicalDataId(bdIdDouble);

        // Assert
        assertEquals(2, result.size());

        assertTrue(result.contains(publicationSampleIdDouble));
        assertTrue(result.contains(secondId));
    }

    @Test
    void findIdsByBibliographicalDataId_shouldReturnEmptyList_whenNoMatchesExist() {

        // Arrange
        when(jpa.findAllByBdId(bdIdDouble))
                .thenReturn(List.of());

        // Act
        List<PublicationSampleId> result =
                repository.findIdsByBibliographicalDataId(bdIdDouble);

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
    // findIdsByEditionIdIn
    // =========================================================================

    @Test
    void findIdsByEditionIdIn_shouldThrowException_whenListOfEditionIdsIsNull() {

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.findIdsByEditionIdIn(null));
    }

    @Test
    void findIdsByEditionIdIn_shouldReturnEmptyList_whenListIsEmpty() {

        // Arrange
        List<EditionID> editionIds = List.of();

        // Act
        List<PublicationSampleId> result =
                repository.findIdsByEditionIdIn(editionIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findIdsByEditionIdIn_shouldReturnPublicationSampleIds_whenMatchesExist() {

        // Arrange
        EditionID editionId = mock(EditionID.class);

        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm2 = mock(PublicationSampleDM.class);

        PublicationSampleId id1 = mock(PublicationSampleId.class);
        PublicationSampleId id2 = mock(PublicationSampleId.class);

        when(jpa.findAllByEditionIdIn(List.of(editionId)))
                .thenReturn(List.of(dm1, dm2));

        when(dm1.getPubSampleId()).thenReturn(id1);
        when(dm2.getPubSampleId()).thenReturn(id2);

        // Act
        List<PublicationSampleId> result =
                repository.findIdsByEditionIdIn(List.of(editionId));

        // Assert
        assertEquals(2, result.size());
        assertEquals(id1, result.get(0));
        assertEquals(id2, result.get(1));
    }

    // =========================================================================
    // findByLibraryId
    // =========================================================================

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
    // findAvailableForSaleByLibraryId
    // =========================================================================

    @Test
    void findAvailableForSaleByLibraryId_shouldThrowNullPointerException_whenLibraryIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findAvailableForSaleByLibraryId(null));
    }

    @Test
    void findAvailableForSaleByLibraryId_shouldReturnEmptyList_whenNoPublicationsExist() {

        // Arrange
        List<PublicationStatus> excluded =
                List.of(PublicationStatus.SOLD, PublicationStatus.FOR_SALE);

        when(jpa.findAllByLibraryIdAndPublicationStatusNotIn(
                libraryIdDouble,
                excluded))
                .thenReturn(List.of());

        // Act
        List<PublicationSample> result =
                repository.findAvailableForSaleByLibraryId(libraryIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAvailableForSaleByLibraryId_shouldReturnMappedPublicationSamples() {

        // Arrange
        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm2 = mock(PublicationSampleDM.class);

        PublicationSample sample1 = mock(PublicationSample.class);
        PublicationSample sample2 = mock(PublicationSample.class);

        List<PublicationStatus> excluded =
                List.of(PublicationStatus.SOLD, PublicationStatus.FOR_SALE);

        when(jpa.findAllByLibraryIdAndPublicationStatusNotIn(
                libraryIdDouble,
                excluded))
                .thenReturn(List.of(dm1, dm2));

        when(assembler.toDomain(dm1)).thenReturn(sample1);
        when(assembler.toDomain(dm2)).thenReturn(sample2);

        // Act
        List<PublicationSample> result =
                repository.findAvailableForSaleByLibraryId(libraryIdDouble);

        // Assert
        assertEquals(2, result.size());
    }

    // =========================================================================
    // findByCollectionOfPublicationsId
    // =========================================================================

    @Test
    void findByCollectionOfPublicationsId_shouldThrowWhenCollectionIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findByCollectionOfPublicationsId(null));
    }

    @Test
    void findByCollectionOfPublicationsId_shouldReturnConvertedList() {

        // Arrange
        CollectionOfPublicationsId collectionId =
                mock(CollectionOfPublicationsId.class);

        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm2 = mock(PublicationSampleDM.class);

        PublicationSample publication1 = mock(PublicationSample.class);
        PublicationSample publication2 = mock(PublicationSample.class);

        when(jpa.findAllByCollectionOfPublicationsId(collectionId))
                .thenReturn(List.of(dm1, dm2));

        when(assembler.toDomain(dm1)).thenReturn(publication1);
        when(assembler.toDomain(dm2)).thenReturn(publication2);

        // Act
        List<PublicationSample> result =
                repository.findByCollectionOfPublicationsId(collectionId);

        // Assert
        assertEquals(2, result.size());
    }

    // =========================================================================
    // findAvailableForCollection
    // =========================================================================

    @Test
    void findAvailableForCollection_shouldThrowWhenArgumentsAreNull() {

        // Arrange
        CollectionOfPublicationsId collectionId =
                mock(CollectionOfPublicationsId.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.findAvailableForCollection(null, collectionId));

        assertThrows(IllegalArgumentException.class,
                () -> repository.findAvailableForCollection(userIdDouble, null));
    }

    @Test
    void findAvailableForCollection_shouldReturnOnlyAvailablePublications() {

        // Arrange
        CollectionOfPublicationsId collectionId =
                mock(CollectionOfPublicationsId.class);

        PublicationSample publication1 = mock(PublicationSample.class);
        PublicationSample publication2 = mock(PublicationSample.class);

        PublicationSampleDM dm1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm2 = mock(PublicationSampleDM.class);

        when(jpa.findAllByOwnerId(userIdDouble))
                .thenReturn(List.of(dm1, dm2));

        when(assembler.toDomain(dm1)).thenReturn(publication1);
        when(assembler.toDomain(dm2)).thenReturn(publication2);

        when(publication1.isInUserLibrary()).thenReturn(true);
        when(publication2.isInUserLibrary()).thenReturn(true);

        when(jpa.findAllByCollectionOfPublicationsId(collectionId))
                .thenReturn(List.of(dm2));

        // Act
        List<PublicationSample> result =
                repository.findAvailableForCollection(userIdDouble, collectionId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(publication1, result.get(0));
    }

    // =========================================================================
    // assignPublicationsToCollection
    // =========================================================================

    @Test
    void assignPublicationsToCollection_shouldReturnTrueWhenAllAssignmentsSucceed() {

        // Arrange
        CollectionOfPublicationsId collectionId =
                mock(CollectionOfPublicationsId.class);

        PublicationSample publication =
                mock(PublicationSample.class);

        when(jpa.findById(publicationSampleIdDouble))
                .thenReturn(Optional.of(publicationSampleDMDouble));

        when(assembler.toDomain(publicationSampleDMDouble))
                .thenReturn(publication);

        when(assembler.toDM(publication))
                .thenReturn(publicationSampleDMDouble);

        when(jpa.save(publicationSampleDMDouble))
                .thenReturn(publicationSampleDMDouble);

        // Act
        boolean result =
                repository.assignPublicationsToCollection(
                        collectionId,
                        List.of(publicationSampleIdDouble));

        // Assert
        assertTrue(result);

        verify(publication)
                .addPublicationToCollection(collectionId);
    }

    @Test
    void assignPublicationsToCollection_shouldThrowWhenCollectionIdIsNull() {

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.assignPublicationsToCollection(
                        null,
                        List.of(publicationSampleIdDouble)));
    }

    @Test
    void assignPublicationsToCollection_shouldThrowWhenPublicationIdsAreNull() {

        // Arrange
        CollectionOfPublicationsId collectionId =
                mock(CollectionOfPublicationsId.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.assignPublicationsToCollection(
                        collectionId,
                        null));
    }

    @Test
    void assignPublicationsToCollection_shouldThrowWhenPublicationIdsAreEmpty() {

        // Arrange
        CollectionOfPublicationsId collectionId =
                mock(CollectionOfPublicationsId.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.assignPublicationsToCollection(
                        collectionId,
                        List.of()));
    }

    @Test
    void assignPublicationsToCollection_shouldThrowWhenPublicationDoesNotExist() {

        // Arrange
        CollectionOfPublicationsId collectionId =
                mock(CollectionOfPublicationsId.class);

        when(jpa.findById(publicationSampleIdDouble))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.assignPublicationsToCollection(
                        collectionId,
                        List.of(publicationSampleIdDouble)));
    }

    @Test
    void addPublicationToLibrary_shouldReturnFalse() {

        // ACT
        boolean result =
                repository.addPublicationToLibrary(
                        publicationSampleDouble,
                        libraryIdDouble
                );

        // ASSERT
        assertFalse(result);
    }
}