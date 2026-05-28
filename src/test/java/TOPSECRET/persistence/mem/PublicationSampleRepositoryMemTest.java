package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublicationSampleRepositoryMem}.
 *
 * <p>This test class validates the in-memory repository implementation of
 * {@link IPublicationSampleRepository}, covering all active operations
 * including construction, creation, persistence, identity lookup, and
 * domain-specific queries.</p>
 *
 * <p>All collaborators ({@link PublicationSampleFactory}, {@link BibliographicalDataId},
 * {@link UserId}, {@link EditionId}) are provided as Mockito test doubles,
 * keeping each test focused on the behaviour of the repository in isolation.</p>
 */
class PublicationSampleRepositoryMemTest {

    private BibliographicalDataId bibliographicalDataId_Double;
    private EditionId editionId_Double;
    private UserId userID_Double;

    @BeforeEach
    void setUp() {
        userID_Double = mock(UserId.class);
        editionId_Double = mock(EditionId.class);
        bibliographicalDataId_Double = mock(BibliographicalDataId.class);
    }

    // ===========================================================
    // ========== Tests for save =================================
    // ===========================================================

    @Test
    void save_shouldReturnTheSamePublicationSample() {
        // Arrange
        PublicationSample pbSample = mock(PublicationSample.class);
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        when(pbSample.id()).thenReturn(pbSampleId);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act
        PublicationSample result = repo.save(pbSample);

        // Assert
        assertSame(pbSample, result);
    }

    @Test
    void save_shouldAddPublicationSampleToRepository() {
        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(pbSampleId);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act
        repo.save(pbSample);

        // Assert
        assertTrue(repo.existsById(pbSampleId));
    }

    @Test
    void save_shouldThrowException_whenPublicationSampleAlreadyExists() {
        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(pbSampleId);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(pbSample));
    }

    @Test
    void save_shouldThrowException_whenPublicationSampleIsNull() {
        // Arrange
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.save(null));
    }

    @Test
    void save_shouldAllowSavingMultipleDifferentPublicationSamples() {
        // Arrange
        PublicationSampleId pbSampleId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbSampleId_2 = mock(PublicationSampleId.class);
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_1.id()).thenReturn(pbSampleId_1);
        when(pbSample_2.id()).thenReturn(pbSampleId_2);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Assert
        List<PublicationSample> all = repo.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(pbSample_1));
        assertTrue(all.contains(pbSample_2));
    }

    @Test
    void save_shouldNotAddDuplicate_whenExceptionIsThrown() {
        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(pbSampleId);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(pbSample));
        assertEquals(1, repo.findAll().size());
    }

    // ===========================================================
    // ========== Tests for findAll ==============================
    // ===========================================================

    @Test
    void findAll_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnListWithOneElement_whenOnePublicationWasAdded() {
        // Arrange
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(publicationSampleId);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample);

        // Act
        List<PublicationSample> result = repo.findAll();

        // Assert
        assertEquals(1, result.size());
        assertSame(pbSample, result.get(0));
    }

    @Test
    void findAll_shouldReturnAllElements_whenMultiplePublicationsWereAdded()  {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.id()).thenReturn(pbId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.id()).thenReturn(pbId_2);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pbSample_1));
        assertTrue(result.contains(pbSample_2));
    }

    @Test
    void findAll_shouldReturnImmutableList() {
        // Arrange
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findAll();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(PublicationSample.class)));
    }


    // ===========================================================
    // ========== Tests for findById =============================
    // ===========================================================

    @Test
    void findById_shouldThrowException_whenIdIsNull() {
        // Arrange
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.findById(null));
    }

    @Test
    void findPublicationSampleById_shouldReturn_whenIdExists() {
        // Arrange
        PublicationSampleId id = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(id);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample);

        // Act
        Optional<PublicationSample> result = repo.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertSame(pbSample, result.get());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() {
        // Arrange
        PublicationSampleId id = mock(PublicationSampleId.class);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act
        Optional<PublicationSample> result = repo.findById(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void find_whenMultipleSamplesExist() {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        Optional<PublicationSample> result = repo.findById(id_2);

        // Assert
        assertTrue(result.isPresent());
        assertSame(pbSample_2, result.get());
    }

    // ===========================================================
    // ========== Tests for existsById ===========================
    // ===========================================================

    @Test
    void existsById_returnsTrue_WhenRepoContainsSamePublicationSample() {
        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(pbSampleId);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample);

        // Act
        boolean result = repo.existsById(pbSampleId);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenPublicationSampleDoesNotExist() {
        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act
        boolean result = repo.existsById(pbSampleId);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsById_returnsFalse_whenEmptyRepo() {
        // Arrange
        PublicationSampleId psId = mock(PublicationSampleId.class);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertFalse(repo.existsById(psId));
    }

    @Test
    void existsById_shouldThrowException_whenPublicationSampleIsNull() {
        // Arrange
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.existsById(null));
    }

    // ===========================================================
    // ====== Tests for findIdsByBibliographicalDataId ===========
    // ===========================================================

    @Test
    void findPubSampleIdsByBibliographicalDataId_shouldReturnListWithMultipleId_whenMultipleMatchesBdId()   {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_3 = mock(PublicationSampleId.class);

        BibliographicalDataId bdId_1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId_2 = mock(BibliographicalDataId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_1.id()).thenReturn(pbId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_2.id()).thenReturn(pbId_2);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_3.id()).thenReturn(pbId_3);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataId(bdId_1);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void findPubSampleIdsByBibliographicalDataId_shouldReturnListWithOneId_whenOneMatchesBdId() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_3 = mock(PublicationSampleId.class);

        BibliographicalDataId bdId_1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId_2 = mock(BibliographicalDataId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_1.id()).thenReturn(pbId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_2.id()).thenReturn(pbId_2);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_3.id()).thenReturn(pbId_3);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataId(bdId_1);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void findPubSampleIdsByBibliographicalDataId_shouldThrowNullPointerException_whenBdIdIsNull() {
        // Arrange
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.findIdsByBibliographicalDataId(null));
    }

    @Test
    void findPubSampleIdsByBibliographicalDataId_shouldReturnEmptyList_whenNoMatchesBdId()  {
        // Arrange
        BibliographicalDataId bdId_1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId_2 = mock(BibliographicalDataId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataId(bdId_1);

        // Assert
        assertEquals(0, result.size());
    }

    // ===========================================================
    // ====== Tests for findIdsByEditionIdIn =====================
    // ===========================================================

    @Test
    void findIdsByEditionIdIn_shouldReturnListWithMultipleId_whenMultiplePubSampleMatchesEditionIds()  {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_3 = mock(PublicationSampleId.class);

        EditionId editionId1Double = mock(EditionId.class);
        EditionId editionId2Double = mock(EditionId.class);

        PublicationSample pbSample1 = mock(PublicationSample.class);
        when(pbSample1.getEditionID()).thenReturn(editionId1Double);
        when(pbSample1.id()).thenReturn(pbId_1);

        PublicationSample pbSample2 = mock(PublicationSample.class);
        when(pbSample2.getEditionID()).thenReturn(editionId2Double);
        when(pbSample2.id()).thenReturn(pbId_2);

        PublicationSample pbSample3 = mock(PublicationSample.class);
        when(pbSample3.getEditionID()).thenReturn(editionId1Double);
        when(pbSample3.id()).thenReturn(pbId_3);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample1);
        repo.save(pbSample2);
        repo.save(pbSample3);

        // Act
        List<PublicationSampleId> result = repo.findIdsByEditionIdIn(List.of(editionId1Double, editionId2Double));

        // Assert
        assertEquals(3, result.size());
    }

    @Test
    void findIdsByEditionIdIn_shouldReturnListWithOneId_whenOnePubSampleMatchesEditionIds()  {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        EditionId editionId1Double = mock(EditionId.class);
        EditionId editionId2Double = mock(EditionId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getEditionID()).thenReturn(editionId1Double);
        when(pbSample_1.id()).thenReturn(id_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getEditionID()).thenReturn(editionId2Double);
        when(pbSample_2.id()).thenReturn(id_2);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSampleId> result = repo.findIdsByEditionIdIn(List.of(editionId1Double));

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void findIdsByEditionIdIn_shouldThrowNullPointerException_whenEditionIdsIsNull() {
        // Arrange
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.findIdsByEditionIdIn(null));
    }

    @Test
    void findIdsByEditionIdIn_shouldReturnEmptyList_whenNoPubSampleMatchesEditionIds()  {
        // Arrange
        EditionId editionId1Double = mock(EditionId.class);
        EditionId editionId2Double = mock(EditionId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getEditionID()).thenReturn(editionId1Double);
        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getEditionID()).thenReturn(editionId1Double);
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSampleId> result = repo.findIdsByEditionIdIn(List.of(editionId2Double));

        // Assert
        assertEquals(0, result.size());
    }

    // ===========================================================
    // ======== Tests for findByUserIdAndLibraryIdIsNotNull ======
    // ===========================================================

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldReturnPublications_whenUserHasPublicationsInLibrary()  {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);

        when(pbSample_1.isInUserLibrary()).thenReturn(true);
        when(pbSample_2.isInUserLibrary()).thenReturn(true);

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(true);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNotNull(userID_Double);

        // Assert
        assertEquals(2, result.size());
        assertEquals(pbSample_1, result.get(0));
        assertEquals(pbSample_2, result.get(1));
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldReturnEmptyList_whenUserHasNoPublicationsInLibrary()  {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);

        when(pbSample_1.isInUserLibrary()).thenReturn(false);
        when(pbSample_2.isInUserLibrary()).thenReturn(false);

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(true);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNotNull(userID_Double);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldNotReturn_publicationsOwnedByOtherUsers()  {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);

        when(pbSample_1.isInUserLibrary()).thenReturn(true);
        when(pbSample_2.isInUserLibrary()).thenReturn(true);

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(false);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNotNull(userID_Double);

        // Assert
        assertEquals(1, result.size());
        assertEquals(pbSample_1, result.get(0));
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldNotReturn_publicationsNotInALibrary() {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);

        when(pbSample_1.isInUserLibrary()).thenReturn(true);
        when(pbSample_2.isInUserLibrary()).thenReturn(false);

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(true);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNotNull(userID_Double);

        // Assert
        assertEquals(1, result.size());
        assertEquals(pbSample_1, result.get(0));
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldThrowException_whenUserIdIsNull() {
        // Arrange
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.findByUserIdAndLibraryIdIsNotNull(null));
    }

    // ===========================================================
    // =========== Tests for findByLibraryId =====================
    // ===========================================================

    @Test
    void findByLibraryId_shouldReturnPublicationsBelongingToGivenLibrary() {
        // Arrange
        LibraryId libraryId_1 = mock(LibraryId.class);
        LibraryId libraryId_2 = mock(LibraryId.class);

        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);
        PublicationSampleId id_3 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        PublicationSample pbSample_3 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);
        when(pbSample_3.id()).thenReturn(id_3);

        when(pbSample_1.getLibraryID()).thenReturn(libraryId_1);
        when(pbSample_2.getLibraryID()).thenReturn(libraryId_1);
        when(pbSample_3.getLibraryID()).thenReturn(libraryId_2);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        // Act
        List<PublicationSample> result = repo.findByLibraryId(libraryId_1);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pbSample_1));
        assertTrue(result.contains(pbSample_2));
        assertFalse(result.contains(pbSample_3));
    }

    @Test
    void findByLibraryId_shouldReturnEmptyList_whenNoPublicationBelongsToLibrary() {
        // Arrange
        LibraryId libraryId_1 = mock(LibraryId.class);
        LibraryId libraryId_2 = mock(LibraryId.class);

        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);

        when(pbSample_1.getLibraryID()).thenReturn(libraryId_1);
        when(pbSample_2.getLibraryID()).thenReturn(libraryId_1);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByLibraryId(libraryId_2);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByLibraryId_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange
        LibraryId libraryId = mock(LibraryId.class);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findByLibraryId(libraryId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByLibraryId_shouldReturnOnlyPublicationsWithMatchingLibraryId() {
        // Arrange
        LibraryId libraryId = mock(LibraryId.class);

        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);
        PublicationSampleId id_3 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        PublicationSample pbSample_3 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);
        when(pbSample_3.id()).thenReturn(id_3);

        when(pbSample_1.getLibraryID()).thenReturn(libraryId);
        when(pbSample_2.getLibraryID()).thenReturn(null);
        when(pbSample_3.getLibraryID()).thenReturn(mock(LibraryId.class));

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        // Act
        List<PublicationSample> result = repo.findByLibraryId(libraryId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(pbSample_1, result.get(0));
    }

    @Test
    void findByLibraryId_shouldReturnPublicationsWithNullLibraryId_whenArgumentIsNull() {
        // Arrange
        LibraryId libraryId = mock(LibraryId.class);

        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);
        PublicationSampleId id_3 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        PublicationSample pbSample_3 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);
        when(pbSample_3.id()).thenReturn(id_3);

        when(pbSample_1.getLibraryID()).thenReturn(null);
        when(pbSample_2.getLibraryID()).thenReturn(libraryId);
        when(pbSample_3.getLibraryID()).thenReturn(null);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        // Act
        List<PublicationSample> result = repo.findByLibraryId(null);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pbSample_1));
        assertTrue(result.contains(pbSample_3));
        assertFalse(result.contains(pbSample_2));
    }

    // ===========================================================
    // ====== Tests for findByUserIdAndLibraryIdIsNull ===========
    // ===========================================================

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldReturnPublications_whenUserHasPublicationsWithoutLibrary() {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(true);

        when(pbSample_1.isInUserLibrary()).thenReturn(false);
        when(pbSample_2.isInUserLibrary()).thenReturn(false);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNull(userID_Double);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pbSample_1));
        assertTrue(result.contains(pbSample_2));
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldReturnEmptyList_whenAllUserPublicationsAreInLibrary()  {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(true);

        when(pbSample_1.isInUserLibrary()).thenReturn(true);
        when(pbSample_2.isInUserLibrary()).thenReturn(true);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNull(userID_Double);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldNotReturn_publicationsOwnedByOtherUsers()  {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(false);

        when(pbSample_1.isInUserLibrary()).thenReturn(false);
        when(pbSample_2.isInUserLibrary()).thenReturn(false);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNull(userID_Double);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(pbSample_1));
        assertFalse(result.contains(pbSample_2));
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldReturnOnlyPublicationsWithoutLibrary_whenMixed() {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(true);

        when(pbSample_1.isInUserLibrary()).thenReturn(false);
        when(pbSample_2.isInUserLibrary()).thenReturn(true);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNull(userID_Double);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(pbSample_1));
        assertFalse(result.contains(pbSample_2));
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNull(userID_Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldThrowNullPointerException_whenUserIdIsNull() {
        // Arrange
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.findByUserIdAndLibraryIdIsNull(null));
    }


    // ===========================================================
    // == Tests for findIdsByBibliographicalDataIdIn =============
    // ===========================================================

    @Test
    void findIdsByBibliographicalDataIdIn_shouldThrowException_whenListIsNull() {
        // Arrange
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.findIdsByBibliographicalDataIdIn(null));
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnMatchingIds() {
        // Arrange
        PublicationSampleId psDoubleId = mock(PublicationSampleId.class);
        PublicationSample psDouble = mock(PublicationSample.class);
        when(psDouble.getBibliographicalDataID()).thenReturn(bibliographicalDataId_Double);
        when(psDouble.id()).thenReturn(psDoubleId);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(psDouble);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId_Double));

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(psDoubleId));
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnEmptyList_whenNoMatch() {
        // Arrange
        BibliographicalDataId bdIdDouble1 = mock(BibliographicalDataId.class);
        PublicationSampleId psDoubleId = mock(PublicationSampleId.class);
        PublicationSample psDouble = mock(PublicationSample.class);
        when(psDouble.getBibliographicalDataID()).thenReturn(bdIdDouble1);
        when(psDouble.id()).thenReturn(psDoubleId);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(psDouble);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId_Double));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnMultipleMatches() {
        // Arrange
        PublicationSampleId idDouble1 = mock(PublicationSampleId.class);
        PublicationSampleId idDouble2 = mock(PublicationSampleId.class);

        PublicationSample psDouble1 = mock(PublicationSample.class);
        PublicationSample psDouble2 = mock(PublicationSample.class);

        when(psDouble1.getBibliographicalDataID()).thenReturn(bibliographicalDataId_Double);
        when(psDouble2.getBibliographicalDataID()).thenReturn(bibliographicalDataId_Double);
        when(psDouble1.id()).thenReturn(idDouble1);
        when(psDouble2.id()).thenReturn(idDouble2);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(psDouble1);
        repo.save(psDouble2);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId_Double));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(idDouble1));
        assertTrue(result.contains(idDouble2));
    }

    // ===========================================================
    // == Tests for findByCollectionOfPublicationsId =============
    // ===========================================================

    @Test
    void shouldReturnPublicationsByCollectionId() {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample p1 = mock(PublicationSample.class);
        PublicationSample p2 = mock(PublicationSample.class);

        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(p1.id()).thenReturn(id_1);
        when(p2.id()).thenReturn(id_2);
        when(p1.getCollecOfPubID()).thenReturn(id);
        when(p2.getCollecOfPubID()).thenReturn(null);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(p1);
        repo.save(p2);

        // Act
        List<PublicationSample> result = repo.findByCollectionOfPublicationsId(id);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(p1));
    }

    @Test
    void shouldReturnEmptyWhenNoPublicationMatchesCollection() {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSample p = mock(PublicationSample.class);
        when(p.id()).thenReturn(id_1);
        when(p.getCollecOfPubID()).thenReturn(null);

        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        repo.save(p);

        // Act
        List<PublicationSample> result = repo.findByCollectionOfPublicationsId(mock(CollectionOfPublicationsId.class));

        // Assert
        assertTrue(result.isEmpty());
    }


    // =========================================================================
    // findByLibraryIdAndPublicationStatusNotIn()
    // =========================================================================

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldThrowException_whenLibraryIdIsNull() {
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        assertThrows(IllegalArgumentException.class, () ->
                repo.findByLibraryIdAndPublicationStatusNotIn(null, List.of(PublicationStatus.FOR_SALE)));
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldThrowException_whenStatusesListIsNull() {
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        LibraryId libraryId = mock(LibraryId.class);
        assertThrows(IllegalArgumentException.class, () ->
                repo.findByLibraryIdAndPublicationStatusNotIn(libraryId, null));
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnMatchingPublications() {
        // Arrange
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem();
        LibraryId targetLibrary = mock(LibraryId.class);
        LibraryId otherLibrary = mock(LibraryId.class);

        PublicationSample ps1 = mock(PublicationSample.class);
        PublicationSample ps2 = mock(PublicationSample.class);
        PublicationSample ps3 = mock(PublicationSample.class);

        when(ps1.id()).thenReturn(mock(PublicationSampleId.class));
        when(ps2.id()).thenReturn(mock(PublicationSampleId.class));
        when(ps3.id()).thenReturn(mock(PublicationSampleId.class));

        when(ps1.getLibraryID()).thenReturn(targetLibrary);
        when(ps1.getPublicationStatus()).thenReturn(PublicationStatus.READ);

        when(ps2.getLibraryID()).thenReturn(targetLibrary);
        when(ps2.getPublicationStatus()).thenReturn(PublicationStatus.SOLD);

        when(ps3.getLibraryID()).thenReturn(otherLibrary);
        when(ps3.getPublicationStatus()).thenReturn(PublicationStatus.READ);

        repo.save(ps1);
        repo.save(ps2);
        repo.save(ps3);

        List<PublicationStatus> excluded = List.of(PublicationStatus.SOLD, PublicationStatus.FOR_SALE);

        // Act
        List<PublicationSample> result = repo.findByLibraryIdAndPublicationStatusNotIn(targetLibrary, excluded);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(ps1));
    }


}