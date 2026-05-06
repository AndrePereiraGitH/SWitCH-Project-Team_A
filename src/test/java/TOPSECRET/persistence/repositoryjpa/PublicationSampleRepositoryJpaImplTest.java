package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.assembler.PublicationSampleAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublicationSampleDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IPublicationSampleRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JPA-based implementation of {@link IPublicationSampleRepository}.
 *
 * <p>Acts as an anti-corruption layer between the domain model and the JPA persistence layer.
 * All conversions between domain objects ({@link PublicationSample}) and data model objects
 * ({@link PublicationSampleDM}) are delegated to {@link PublicationSampleAssembler}.</p>
 *
 * <p>Tests are organised into the following sections:</p>
 * <ul>
 *   <li><b>save</b> — persisting a new sample, duplicate detection, and null guards.</li>
 *   <li><b>existsById</b> — presence checks and null guards.</li>
 *   <li><b>findAll</b> — retrieval of zero, one, or many samples.</li>
 *   <li><b>findById</b> — retrieval by identity, empty result, and null guards.</li>
 *   <li><b>findIdsByBibliographicalDataId</b> — filtering by bibliographical data ID.</li>
 *
 */


class PublicationSampleRepositoryJpaImplTest {

    @Mock
    private PublicationSampleAssembler assembler;

    @Mock
    private IPublicationSampleRepositoryJpa jpa;

    @InjectMocks
    private PublicationSampleRepositoryJpaImpl repo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ===========================================================
    // ========== Tests for save =================================
    // ===========================================================

    @Test
    void save_shouldReturnTheSamePublicationSample() {

        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        PublicationSampleDM pbSampleDM = mock(PublicationSampleDM.class);

        when(pbSample.id()).thenReturn(pbSampleId);
        when(jpa.existsById(pbSampleId)).thenReturn(false);
        when(assembler.toDM(pbSample)).thenReturn(pbSampleDM);
        when(jpa.save(pbSampleDM)).thenReturn(pbSampleDM);
        when(assembler.toDomain(pbSampleDM)).thenReturn(pbSample);

        // Act
        PublicationSample result = repo.save(pbSample);

        // Assert
        assertSame(pbSample, result);
    }

    @Test
    void save_shouldThrowWhenPublicationSampleAlreadyExists() {

        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);

        when(pbSample.id()).thenReturn(pbSampleId);
        when(jpa.existsById(pbSampleId)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(pbSample));
    }

    @Test
    void save_shouldThrowException_whenPublicationSampleIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.save(null));
    }


    @Test
    void save_shouldNotAddDuplicate_whenExceptionIsThrown() {

        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);

        when(pbSample.id()).thenReturn(pbSampleId);
        when(jpa.existsById(pbSampleId)).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(pbSample));
    }


    // ===========================================================
    // ========== Tests for existsById ===========================
    // ===========================================================

    @Test
    void existsById_returnsTrue_WhenRepoContainsSamePublicationSample() {

        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        when(jpa.existsById(pbSampleId)).thenReturn(true);

        // Act
        boolean result = repo.existsById(pbSampleId);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenPublicationSampleDoesNotExist() {

        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        when(jpa.existsById(pbSampleId)).thenReturn(false);

        // Act
        boolean result = repo.existsById(pbSampleId);
        // Assert
        assertFalse(result);
    }


    @Test
    void existsById_shouldThrowException_whenPublicationSampleIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.existsById(null));
    }

    // ===========================================================
    // ========== Tests for findAll ==============================
    // ===========================================================

    @Test
    void findAll_shouldReturnEmptyList_whenRepositoryIsEmpty() {

        // Act
        List<PublicationSample> result = repo.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnListWithOneElement_whenOnePublicationWasAdded() {

        // Arrange
        PublicationSampleDM dm_1 = mock(PublicationSampleDM.class);
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.id()).thenReturn(pbId_1);

        when(jpa.findAll()).thenReturn(List.of(dm_1));
        when(assembler.toDomain(dm_1)).thenReturn(pbSample_1);

        // Act
        List<PublicationSample> result = repo.findAll();

        // Assert
        assertEquals(pbSample_1, result.get(0));
    }

    @Test
    void findAll_shouldReturnAllElements_whenMultiplePublicationsWereAdded()  {

        // Arrange
        PublicationSampleDM dm_1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_2 = mock(PublicationSampleDM.class);

        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.id()).thenReturn(pbId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.id()).thenReturn(pbId_2);

        when(jpa.findAll()).thenReturn(List.of(dm_1,dm_2));
        when(assembler.toDomain(dm_1)).thenReturn(pbSample_1);
        when(assembler.toDomain(dm_2)).thenReturn(pbSample_2);


        // Act
        List<PublicationSample> result = repo.findAll();

        // Assert
        assertEquals(2, result.size());
    }

    // ===========================================================
    // ========== Tests for findById =============================
    // ===========================================================

    @Test
    void findById_shouldThrowException_whenIdIsNull() {
        // Arrange

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findById(null));
    }

    @Test
    void findById_shouldReturn_whenIdExists() {

        //Arrange
        PublicationSampleDM dm_1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_2 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_3 = mock(PublicationSampleDM.class);

        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_3 = mock(PublicationSampleId.class);


        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.id()).thenReturn(pbId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.id()).thenReturn(pbId_2);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.id()).thenReturn(pbId_3);

        when(jpa.findById(pbId_1)).thenReturn(Optional.of(dm_1));
        when(jpa.findById(pbId_2)).thenReturn(Optional.of(dm_2));
        when(jpa.findById(pbId_3)).thenReturn(Optional.of(dm_3));

        when(assembler.toDomain(dm_1)).thenReturn(pbSample_1);
        when(assembler.toDomain(dm_2)).thenReturn(pbSample_2);
        when(assembler.toDomain(dm_3)).thenReturn(pbSample_3);

        //Act
        Optional<PublicationSample> result = repo.findById(pbId_1);

        //assert
        assertEquals(result,Optional.of(pbSample_1));
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() {
        // Arrange
        PublicationSampleId id = mock(PublicationSampleId.class);

        // Act
        Optional<PublicationSample> result = repo.findById(id);

        // Assert
        assertTrue(result.isEmpty());
    }


    // ===========================================================
    // ====== Tests for findIdsByBibliographicalDataId ===========
    // ===========================================================

    @Test
    void findPubSampleIdsByBibliographicalDataId_shouldReturnListWithMultipleId_whenMultipleMatchesBdId()   {

        // Arrange
        PublicationSampleDM dm_1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_2 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_3 = mock(PublicationSampleDM.class);

        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_3 = mock(PublicationSampleId.class);

        BibliographicalDataID bdId_1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId_2 = mock(BibliographicalDataID.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_1.id()).thenReturn(pbId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_2.id()).thenReturn(pbId_2);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_3.id()).thenReturn(pbId_3);

        when(jpa.findAllByBdId(bdId_1)).thenReturn(List.of(dm_1,dm_2));
        when(jpa.findAllByBdId(bdId_2)).thenReturn(List.of(dm_3));


        when(dm_1.getPubSampleId()).thenReturn(pbId_1);
        when(dm_2.getPubSampleId()).thenReturn(pbId_2);
        when(dm_3.getPubSampleId()).thenReturn(pbId_3);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataId(bdId_1);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void findPubSampleIdsByBibliographicalDataId_shouldReturnListWithOneId_whenOneMatchesBdId() {

        // Arrange
        PublicationSampleDM dm_1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_2 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_3 = mock(PublicationSampleDM.class);

        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_3 = mock(PublicationSampleId.class);

        BibliographicalDataID bdId_1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId_2 = mock(BibliographicalDataID.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_1.id()).thenReturn(pbId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_2.id()).thenReturn(pbId_2);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_3.id()).thenReturn(pbId_3);

        when(jpa.findAllByBdId(bdId_1)).thenReturn(List.of(dm_1,dm_2));
        when(jpa.findAllByBdId(bdId_2)).thenReturn(List.of(dm_3));


        when(dm_1.getPubSampleId()).thenReturn(pbId_1);
        when(dm_2.getPubSampleId()).thenReturn(pbId_2);
        when(dm_3.getPubSampleId()).thenReturn(pbId_3);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataId(bdId_2);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void findPubSampleByBibliographicalId_shouldThrowNullPointerException_whenBdIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findIdsByBibliographicalDataId(null));
    }

    @Test
    void findPubSampleIdsByBibliographicalDataId_shouldReturnEmptyList_whenNoMatchesBdId()  {

        // Arrange
        PublicationSampleDM dm_1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_2 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_3 = mock(PublicationSampleDM.class);

        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_3 = mock(PublicationSampleId.class);

        BibliographicalDataID bdId_1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId_2 = mock(BibliographicalDataID.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_1.id()).thenReturn(pbId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_2.id()).thenReturn(pbId_2);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_3.id()).thenReturn(pbId_3);

        when(jpa.findAllByBdId(bdId_1)).thenReturn(List.of(dm_1,dm_2,dm_3));

        when(dm_1.getPubSampleId()).thenReturn(pbId_1);
        when(dm_2.getPubSampleId()).thenReturn(pbId_2);
        when(dm_3.getPubSampleId()).thenReturn(pbId_3);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataId(bdId_2);

        // Assert
        assertEquals(0, result.size());
    }


    // ===========================================================
    // ========== Tests for findByUserIdAndLibraryIdIsNotNull ====
    // ===========================================================


    @Test
    void findByUserIdAndLibraryIdIsNotNull_WhenUserHasPublicationsInLibrary_ShouldReturnFilteredList() {
        // Arrange
        PublicationSampleDM dm_1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_2 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_3 = mock(PublicationSampleDM.class);

        UserId id = mock(UserId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getOwnerID()).thenReturn(id);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getOwnerID()).thenReturn(id);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getOwnerID()).thenReturn(id);

        when(jpa.findAllByOwnerId(id)).thenReturn(List.of(dm_1,dm_2,dm_3));
        when(assembler.toDomain(dm_1)).thenReturn(pbSample_1);
        when(assembler.toDomain(dm_2)).thenReturn(pbSample_2);
        when(assembler.toDomain(dm_3)).thenReturn(pbSample_3);

        when(pbSample_1.isOwnedByUser(id)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(id)).thenReturn(true);
        when(pbSample_3.isOwnedByUser(id)).thenReturn(true);

        when(pbSample_1.isInUserLibrary()).thenReturn(true);
        when(pbSample_2.isInUserLibrary()).thenReturn(true);
        when(pbSample_3.isInUserLibrary()).thenReturn(true);


        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNotNull(id);

        // Assert
        assertEquals(3, result.size());
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_WhenPublicationIsOwnedButNotInLibrary_ShouldReturnEmptyList() {
        // Arrange
        PublicationSampleDM dm_1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_2 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_3 = mock(PublicationSampleDM.class);

        UserId id = mock(UserId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getOwnerID()).thenReturn(id);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getOwnerID()).thenReturn(id);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getOwnerID()).thenReturn(id);

        when(jpa.findAllByOwnerId(id)).thenReturn(List.of(dm_1,dm_2,dm_3));
        when(assembler.toDomain(dm_1)).thenReturn(pbSample_1);
        when(assembler.toDomain(dm_2)).thenReturn(pbSample_2);
        when(assembler.toDomain(dm_3)).thenReturn(pbSample_3);

        when(pbSample_1.isOwnedByUser(id)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(id)).thenReturn(true);
        when(pbSample_3.isOwnedByUser(id)).thenReturn(true);

        when(pbSample_1.isInUserLibrary()).thenReturn(false);
        when(pbSample_2.isInUserLibrary()).thenReturn(false);
        when(pbSample_3.isInUserLibrary()).thenReturn(false);


        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNotNull(id);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_WhenUserHasNoPublications_ShouldReturnEmptyList() {
        // Arrange
        PublicationSampleDM dm_1 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_2 = mock(PublicationSampleDM.class);
        PublicationSampleDM dm_3 = mock(PublicationSampleDM.class);

        UserId id = mock(UserId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getOwnerID()).thenReturn(id);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getOwnerID()).thenReturn(id);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getOwnerID()).thenReturn(id);

        when(jpa.findAllByOwnerId(id)).thenReturn(List.of(dm_1,dm_2,dm_3));
        when(assembler.toDomain(dm_1)).thenReturn(pbSample_1);
        when(assembler.toDomain(dm_2)).thenReturn(pbSample_2);
        when(assembler.toDomain(dm_3)).thenReturn(pbSample_3);

        when(pbSample_1.isOwnedByUser(id)).thenReturn(false);
        when(pbSample_2.isOwnedByUser(id)).thenReturn(false);
        when(pbSample_3.isOwnedByUser(id)).thenReturn(false);

        when(pbSample_1.isInUserLibrary()).thenReturn(false);
        when(pbSample_2.isInUserLibrary()).thenReturn(false);
        when(pbSample_3.isInUserLibrary()).thenReturn(false);


        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNotNull(id);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_WhenUserIdIsNull_ShouldThrowIllegalArgumentException() {

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findByUserIdAndLibraryIdIsNotNull(null));
    }


    // THIS TESTS WERE ONLY USED TO COVER THE MUTATION COVERAGE AND LINE COVERAGE!
    // THEY NEED TO BE UPDATED

    @Test
    void findIdsByEditionIdIn_shouldReturnEmptyList() {
        List<EditionID> ids = List.of(mock(EditionID.class));
        List<PublicationSampleId> result = repo.findIdsByEditionIdIn(ids);
        assertTrue(result.isEmpty());
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnEmptyList() {
        List<BibliographicalDataID> ids = List.of(mock(BibliographicalDataID.class));
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataIdIn(ids);
        assertTrue(result.isEmpty());
    }


    @Test
    void findPublicationSamplesIdsByBiblioDataIds_shouldReturnEmptyList() {
        List<BibliographicalDataID> ids = List.of(mock(BibliographicalDataID.class));
        List<PublicationSampleId> result = repo.findPublicationSamplesIdsByBiblioDataIds(ids);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByLibraryId_shouldReturnEmptyList() {
        LibraryId libraryId = mock(LibraryId.class);
        List<PublicationSample> result = repo.findByLibraryId(libraryId);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldReturnEmptyList() {
        UserId userId = mock(UserId.class);
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNull(userId);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAvailableForSaleByLibraryId_shouldReturnEmptyList() {
        LibraryId libraryId = mock(LibraryId.class);
        List<PublicationSample> result = repo.findAvailableForSaleByLibraryId(libraryId);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByCollectionOfPublicationsId_shouldReturnEmptyList() {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        List<PublicationSample> result = repo.findByCollectionOfPublicationsId(collectionId);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAvailableForCollection_shouldReturnEmptyList() {
        UserId userId = mock(UserId.class);
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        List<PublicationSample> result = repo.findAvailableForCollection(userId, collectionId);
        assertTrue(result.isEmpty());
    }

    // ===========================================================
    // ========== Tests for addPublicationToLibrary ==============
    // ===========================================================

    @Test
    void addPublicationToLibrary_shouldReturnFalse() {

        // Arrange
        PublicationSample pbSample = mock(PublicationSample.class);
        LibraryId libraryId = mock(LibraryId.class);

        // Act
        boolean result = repo.addPublicationToLibrary(pbSample, libraryId);

        // Assert
        assertFalse(result);
    }

    // ===========================================================
    // ========== Tests for assignPublicationsToCollection =======
    // ===========================================================

    @Test
    void assignPublicationsToCollection_shouldReturnFalse() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        List<PublicationSampleId> ids = List.of(mock(PublicationSampleId.class));

        // Act
        boolean result = repo.assignPublicationsToCollection(collectionId, ids);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnPublicationSampleIdsByBibliographicalDataIds() {

        // Arrange
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        PublicationSampleId sampleId = mock(PublicationSampleId.class);
        PublicationSampleDM dm = mock(PublicationSampleDM.class);
        when(dm.getPubSampleId()).thenReturn(sampleId);
        when(jpa.findAllByBdIdIn(List.of(bdId))).thenReturn(List.of(dm));

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataIdIn(List.of(bdId));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleId, result.get(0));
    }

    @Test
    void shouldReturnEmptyListWhenNoPublicationSamplesExist() {

        // Arrange
        BibliographicalDataID bdId = mock(BibliographicalDataID.class);

        when(jpa.findAllByBdIdIn(List.of(bdId))).thenReturn(List.of());

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataIdIn(List.of(bdId));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    void shouldThrowExceptionWhenBdIdsIsNull() {

        // Act & Assert
        Exception ex = assertThrows(
                NullPointerException.class,
                () -> repo.findIdsByBibliographicalDataIdIn(null));

        assertEquals("BibliographicalData Ids cannot be null", ex.getMessage());
    }


}