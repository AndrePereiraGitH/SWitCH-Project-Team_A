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
 * <p>Verifies the behaviour of the JPA-backed implementation of
 * {@link IPublicationSampleRepository}, ensuring correct interaction between:</p>
 * <ul>
 *   <li>the JPA repository layer ({@link IPublicationSampleRepositoryJpa});</li>
 *   <li>the persistence assembler ({@link PublicationSampleAssembler});</li>
 *   <li>and the domain model ({@link PublicationSample}).</li>
 * </ul>
 *
 * <p>The test suite covers:</p>
 * <ul>
 *   <li><b>Persistence operations</b> — save flow and duplicate detection.</li>
 *   <li><b>Lookup operations</b> — retrieval by identity and associated identifiers.</li>
 *   <li><b>Filtering operations</b> — user/library-based queries.</li>
 *   <li><b>Conversion flow</b> — mapping between persistence entities and domain objects.</li>
 *   <li><b>Null guards</b> — validation of mandatory arguments.</li>
 *   <li><b>Placeholder methods</b> — methods currently returning empty collections or {@code false}.</li>
 * </ul>
 *
 * <p>Mockito is used to isolate the repository implementation from both the
 * persistence layer and domain reconstruction logic.</p>
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
    // Save
    // =========================================================================

    @Test
    void save_shouldReturnSavedPublicationSample() {

        // Arrange
        when(publicationSampleDouble.id()).thenReturn(publicationSampleIdDouble);
        when(jpa.existsById(publicationSampleIdDouble)).thenReturn(false);
        when(assembler.toDM(publicationSampleDouble)).thenReturn(publicationSampleDMDouble);
        when(jpa.save(publicationSampleDMDouble)).thenReturn(publicationSampleDMDouble);
        when(assembler.toDomain(publicationSampleDMDouble)).thenReturn(publicationSampleDouble);

        // Act
        PublicationSample result = repository.save(publicationSampleDouble);

        // Assert
        assertNotNull(result);
        assertSame(publicationSampleDouble, result);
    }

    @Test
    void save_shouldThrowIllegalArgumentException_whenPublicationAlreadyExists() {

        // Arrange
        when(publicationSampleDouble.id()).thenReturn(publicationSampleIdDouble);
        when(jpa.existsById(publicationSampleIdDouble)).thenReturn(true);

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
        when(jpa.existsById(publicationSampleIdDouble)).thenReturn(true);

        // Act
        boolean result = repository.existsById(publicationSampleIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenPublicationDoesNotExist() {

        // Arrange
        when(jpa.existsById(publicationSampleIdDouble)).thenReturn(false);

        // Act
        boolean result = repository.existsById(publicationSampleIdDouble);

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
        List<PublicationSample> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnMappedPublicationSamples() {

        // Arrange
        PublicationSampleDM secondDM = mock(PublicationSampleDM.class);
        PublicationSample secondPublication = mock(PublicationSample.class);

        when(jpa.findAll()).thenReturn(List.of(publicationSampleDMDouble, secondDM));

        when(assembler.toDomain(publicationSampleDMDouble))
                .thenReturn(publicationSampleDouble);

        when(assembler.toDomain(secondDM))
                .thenReturn(secondPublication);

        // Act
        List<PublicationSample> result = repository.findAll();

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
        PublicationSampleId secondId = mock(PublicationSampleId.class);

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
        when(jpa.findAllByBdId(bdIdDouble)).thenReturn(List.of());

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
    // findByUserIdAndLibraryIdIsNotNull
    // =========================================================================

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldReturnFilteredPublications() {

        // Arrange
        PublicationSample secondPublication = mock(PublicationSample.class);
        PublicationSampleDM secondDM = mock(PublicationSampleDM.class);

        when(jpa.findAllByOwnerId(userIdDouble))
                .thenReturn(List.of(publicationSampleDMDouble, secondDM));

        when(assembler.toDomain(publicationSampleDMDouble))
                .thenReturn(publicationSampleDouble);

        when(assembler.toDomain(secondDM))
                .thenReturn(secondPublication);

        when(publicationSampleDouble.isInUserLibrary()).thenReturn(true);

        when(secondPublication.isInUserLibrary()).thenReturn(true);

        // Act
        List<PublicationSample> result =
                repository.findByUserIdAndLibraryIdIsNotNull(userIdDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldReturnEmptyList_whenNoPublicationMatchesFilter() {

        // Arrange
        when(jpa.findAllByOwnerId(userIdDouble))
                .thenReturn(List.of(publicationSampleDMDouble));

        when(assembler.toDomain(publicationSampleDMDouble))
                .thenReturn(publicationSampleDouble);


        // Act
        List<PublicationSample> result =
                repository.findByUserIdAndLibraryIdIsNotNull(userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
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
    void findByUserIdAndLibraryIdIsNull_shouldReturnMappedPublicationSamples() {
        // Arrange
        when(jpa.findAllByOwnerIdAndLibraryIdIsNull(userIdDouble))
                .thenReturn(List.of(publicationSampleDMDouble));
        when(assembler.toDomain(publicationSampleDMDouble))
                .thenReturn(publicationSampleDouble);
        // Act
        List<PublicationSample> result =
                repository.findByUserIdAndLibraryIdIsNull(userIdDouble);
        // Assert
        assertEquals(1, result.size());
        assertEquals(publicationSampleDouble, result.get(0));
    }

    // =========================================================================
    // findAvailableForSaleByLibraryId
    // =========================================================================

    @Test
    void findAvailableForSaleByLibraryId_shouldReturnAvailablePublications() {

        // Arrange
        PublicationSampleDM secondDM = mock(PublicationSampleDM.class);
        PublicationSample secondPublication = mock(PublicationSample.class);

        List<PublicationStatus> excluded =
                List.of(PublicationStatus.SOLD, PublicationStatus.FOR_SALE);

        when(jpa.findAllByLibraryIdAndPublicationStatusNotIn(libraryIdDouble, excluded))
                .thenReturn(List.of(publicationSampleDMDouble, secondDM));

        when(assembler.toDomain(publicationSampleDMDouble))
                .thenReturn(publicationSampleDouble);

        when(assembler.toDomain(secondDM))
                .thenReturn(secondPublication);

        // Act
        List<PublicationSample> result =
                repository.findAvailableForSaleByLibraryId(libraryIdDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void findAvailableForSaleByLibraryId_shouldReturnEmptyList_whenNoPublicationsExist() {

        // Arrange
        List<PublicationStatus> excluded =
                List.of(PublicationStatus.SOLD, PublicationStatus.FOR_SALE);

        when(jpa.findAllByLibraryIdAndPublicationStatusNotIn(libraryIdDouble, excluded))
                .thenReturn(List.of());

        // Act
        List<PublicationSample> result =
                repository.findAvailableForSaleByLibraryId(libraryIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAvailableForSaleByLibraryId_shouldThrowNullPointerException_whenLibraryIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repository.findAvailableForSaleByLibraryId(null));
    }

    // =========================================================================
    // Placeholder methods
    // =========================================================================

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnEmptyList() {

        // Arrange
        List<BibliographicalDataID> ids = List.of(mock(BibliographicalDataID.class));

        // Act
        List<PublicationSampleId> result =
                repository.findIdsByBibliographicalDataIdIn(ids);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findIdsByEditionIdIn_shouldReturnEmptyList() {

        // Arrange
        List<EditionID> ids = List.of(mock(EditionID.class));

        // Act
        List<PublicationSampleId> result =
                repository.findIdsByEditionIdIn(ids);

        // Assert
        assertTrue(result.isEmpty());

    }
    void findByLibraryId_shouldThrow_whenLibraryIdIsNull() {

        // act + assert
        NullPointerException ex = assertThrows(NullPointerException.class, () -> repository.findByLibraryId(null));

        assertEquals("LibraryId cannot be null", ex.getMessage());
    }

    @Test
    void findByLibraryId_shouldReturnMappedPublicationSamples_whenLibraryHasSamples() {
        // Arrange
        LibraryId libraryId = mock(LibraryId.class);

        PublicationSampleDM dm_1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_2 = mock(PublicationSampleDM.class);

        PublicationSample sample_1 = mock(PublicationSample.class);
        PublicationSample sample_2 = mock(PublicationSample.class);

        when(jpa.findByLibraryId(libraryId)).thenReturn(List.of(dm_1, dm_2));
        when(assembler.toDomain(dm_1)).thenReturn(sample_1);
        when(assembler.toDomain(dm_2)).thenReturn(sample_2);

        // Act
        List<PublicationSample> result = repository.findByLibraryId(libraryId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(sample_1, result.get(0));
        assertEquals(sample_2, result.get(1));

    }

    @Test
    void findByLibraryId_shouldReturnEmptyList_whenRepositoryReturnsEmptyList() {
        // Arrange
        LibraryId libraryId = mock(LibraryId.class);

        when(jpa.findByLibraryId(libraryId)).thenReturn(List.of());

        // Act
        List<PublicationSample> result = repository.findByLibraryId(libraryId);

        // Assert
        assertTrue(result.isEmpty());
    }
// -------------------------------------------------

    @Test
    void findByLibraryId_shouldReturnEmptyList() {
        // Arrange
        when(jpa.findByLibraryId(libraryIdDouble)).thenReturn(List.of());
        // Act
        List<PublicationSample> result =
                repository.findByLibraryId(libraryIdDouble);
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByCollectionOfPublicationsId_shouldReturnEmptyList() {

        // Arrange
        CollectionOfPublicationsId collectionId =
                mock(CollectionOfPublicationsId.class);

        // Act
        List<PublicationSample> result =
                repository.findByCollectionOfPublicationsId(collectionId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAvailableForCollection_shouldReturnEmptyList() {

        // Arrange
        CollectionOfPublicationsId collectionId =
                mock(CollectionOfPublicationsId.class);

        // Act
        List<PublicationSample> result =
                repository.findAvailableForCollection(userIdDouble, collectionId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void addPublicationToLibrary_shouldReturnFalse() {

        // Act
        boolean result =
                repository.addPublicationToLibrary(publicationSampleDouble, libraryIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void assignPublicationsToCollection_shouldReturnFalse() {

        // Arrange
        CollectionOfPublicationsId collectionId =
                mock(CollectionOfPublicationsId.class);

        List<PublicationSampleId> ids =
                List.of(publicationSampleIdDouble);

        // Act
        boolean result =
                repository.assignPublicationsToCollection(collectionId, ids);

        // Assert
        assertFalse(result);
    }
}