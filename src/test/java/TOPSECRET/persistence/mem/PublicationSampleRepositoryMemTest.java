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
 *
 * <p>Tests are organized into the following sections:</p>
 * <ul>
 *   <li><b>save</b> — persisting a new sample and null/duplicate guards.</li>
 *   <li><b>existsById</b> — presence checks with true/false outcomes and null guards.</li>
 *   <li><b>findAll</b> — retrieval of zero, one, or many samples, and immutability.</li>
 *   <li><b>findById</b> — retrieval by identity, empty results, and null guards.</li>
 *   <li><b>findIdsByBibliographicalDataId</b> — ID retrieval filtered by bibliographical data.</li>
 *   <li><b>findByBibliographicalDataId</b> — sample retrieval filtered by bibliographical data.</li>
 *   <li><b>findIdsByBibliographicalDataIdIn</b> — batch ID retrieval by multiple bibliographical data IDs.</li>
 *   <li><b>findByBibliographicalDataIdIn</b> — batch sample retrieval by multiple bibliographical data IDs.</li>
 *   <li><b>findIdsByEditionIdIn</b> — batch ID retrieval by edition IDs.</li>
 *   <li><b>findByEditionIdIn</b> — batch sample retrieval by edition IDs.</li>
 *   <li><b>findByLibraryId</b> — retrieval of samples by library identifier.</li>
 *   <li><b>findByCollectionOfPublicationsId</b> — retrieval of samples by collection identifier.</li>
 *   <li><b>findAllByOwnerId</b> — retrieval of all samples owned by a given user.</li>
 *   <li><b>findPublicationsInLibrary</b> — owner-scoped retrieval of library-assigned samples.</li>
 *   <li><b>findPublicationsNotInLibrary</b> — owner-scoped retrieval of samples not in any library.</li>
 *   <li><b>findByLibraryIdAndPublicationStatusNotIn</b> — domain-filtered retrieval excluding given statuses.</li>
 * </ul>
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

    // =========================================================================
    // save
    // =========================================================================

    @Test
    void save_shouldReturnTheSamePublicationSample() {
        // Arrange
        PublicationSample pbSample = mock(PublicationSample.class);
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        when(pbSample.id()).thenReturn(pbSampleId);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

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
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        repo.save(pbSample);

        // Assert
        assertTrue(repo.existsById(pbSampleId));
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
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

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
    void save_shouldThrowException_whenPublicationSampleAlreadyExists() {
        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(pbSampleId);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(pbSample));
    }

    @Test
    void save_shouldNotAddDuplicate_whenExceptionIsThrown() {
        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(pbSampleId);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(pbSample));
        assertEquals(1, repo.findAll().size());
    }

    @Test
    void save_shouldThrowException_whenPublicationSampleIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.save(null));
    }

    // =========================================================================
    // existsById
    // =========================================================================

    @Test
    void existsById_shouldReturnTrue_whenPublicationSampleExists() {
        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(pbSampleId);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
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
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        boolean result = repo.existsById(pbSampleId);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenRepositoryIsEmpty() {
        // Arrange
        PublicationSampleId psId = mock(PublicationSampleId.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertFalse(repo.existsById(psId));
    }

    @Test
    void existsById_shouldThrowException_whenIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.existsById(null));
    }

    // =========================================================================
    // findAll
    // =========================================================================

    @Test
    void findAll_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

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

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample);

        // Act
        List<PublicationSample> result = repo.findAll();

        // Assert
        assertEquals(1, result.size());
        assertSame(pbSample, result.get(0));
    }

    @Test
    void findAll_shouldReturnAllElements_whenMultiplePublicationsWereAdded() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(pbId_1);
        when(pbSample_2.id()).thenReturn(pbId_2);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
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
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findAll();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(PublicationSample.class)));
    }

    // =========================================================================
    // findById
    // =========================================================================

    @Test
    void findById_shouldReturnPublicationSample_whenIdExists() {
        // Arrange
        PublicationSampleId id = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(id);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample);

        // Act
        Optional<PublicationSample> result = repo.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertSame(pbSample, result.get());
    }

    @Test
    void findById_shouldReturnCorrectSample_whenMultipleSamplesExist() {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        Optional<PublicationSample> result = repo.findById(id_2);

        // Assert
        assertTrue(result.isPresent());
        assertSame(pbSample_2, result.get());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() {
        // Arrange
        PublicationSampleId id = mock(PublicationSampleId.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        Optional<PublicationSample> result = repo.findById(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldThrowException_whenIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.findById(null));
    }

    // =========================================================================
    // findIdsByBibliographicalDataId
    // =========================================================================

    @Test
    void findIdsByBibliographicalDataId_shouldReturnMultipleIds_whenMultipleMatchBdId() {
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

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataId(bdId_1);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void findIdsByBibliographicalDataId_shouldReturnOneId_whenOneMatchesBdId() {
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

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataId(bdId_1);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void findIdsByBibliographicalDataId_shouldReturnEmptyList_whenNoMatchesBdId() {
        // Arrange
        BibliographicalDataId bdId_1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId_2 = mock(BibliographicalDataId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataId(bdId_1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findIdsByBibliographicalDataId_shouldThrowNullPointerException_whenBdIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.findIdsByBibliographicalDataId(null));
    }

    // =========================================================================
    // findByBibliographicalDataId
    // =========================================================================

    @Test
    void findByBibliographicalDataId_shouldReturnMatchingPublications_whenMultipleMatchBdId() {
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

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        // Act
        List<PublicationSample> result = repo.findByBibliographicalDataId(bdId_1);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pbSample_1));
        assertTrue(result.contains(pbSample_2));
        assertFalse(result.contains(pbSample_3));
    }

    @Test
    void findByBibliographicalDataId_shouldReturnOnePublication_whenOneMatchesBdId() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);

        BibliographicalDataId bdId_1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId_2 = mock(BibliographicalDataId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_1.id()).thenReturn(pbId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_2.id()).thenReturn(pbId_2);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByBibliographicalDataId(bdId_1);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(pbSample_1));
    }

    @Test
    void findByBibliographicalDataId_shouldReturnEmptyList_whenNoMatchesBdId() {
        // Arrange
        BibliographicalDataId bdId_1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId_2 = mock(BibliographicalDataId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);

        // Act
        List<PublicationSample> result = repo.findByBibliographicalDataId(bdId_1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByBibliographicalDataId_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findByBibliographicalDataId(bdId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByBibliographicalDataId_shouldThrowNullPointerException_whenBdIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.findByBibliographicalDataId(null));
    }

    // =========================================================================
    // findIdsByBibliographicalDataIdIn
    // =========================================================================

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnMatchingIds_whenOneMatchExists() {
        // Arrange
        PublicationSampleId psDoubleId = mock(PublicationSampleId.class);
        PublicationSample psDouble = mock(PublicationSample.class);
        when(psDouble.getBibliographicalDataID()).thenReturn(bibliographicalDataId_Double);
        when(psDouble.id()).thenReturn(psDoubleId);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(psDouble);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId_Double));

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(psDoubleId));
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnMultipleIds_whenMultipleMatchesExist() {
        // Arrange
        PublicationSampleId idDouble1 = mock(PublicationSampleId.class);
        PublicationSampleId idDouble2 = mock(PublicationSampleId.class);

        PublicationSample psDouble1 = mock(PublicationSample.class);
        PublicationSample psDouble2 = mock(PublicationSample.class);

        when(psDouble1.getBibliographicalDataID()).thenReturn(bibliographicalDataId_Double);
        when(psDouble2.getBibliographicalDataID()).thenReturn(bibliographicalDataId_Double);
        when(psDouble1.id()).thenReturn(idDouble1);
        when(psDouble2.id()).thenReturn(idDouble2);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(psDouble1);
        repo.save(psDouble2);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId_Double));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(idDouble1));
        assertTrue(result.contains(idDouble2));
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnEmptyList_whenNoMatchExists() {
        // Arrange
        BibliographicalDataId bdId_1 = mock(BibliographicalDataId.class);

        PublicationSample psDouble = mock(PublicationSample.class);
        when(psDouble.getBibliographicalDataID()).thenReturn(bdId_1);
        when(psDouble.id()).thenReturn(mock(PublicationSampleId.class));

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(psDouble);

        // Act
        List<PublicationSampleId> result = repo.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId_Double));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldThrowException_whenListIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.findIdsByBibliographicalDataIdIn(null));
    }

    // =========================================================================
    // findByBibliographicalDataIdIn
    // =========================================================================

    @Test
    void findByBibliographicalDataIdIn_shouldReturnMatchingPublications_whenOneMatchExists() {
        // Arrange
        PublicationSampleId psId = mock(PublicationSampleId.class);
        PublicationSample psDouble = mock(PublicationSample.class);
        when(psDouble.getBibliographicalDataID()).thenReturn(bibliographicalDataId_Double);
        when(psDouble.id()).thenReturn(psId);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(psDouble);

        // Act
        List<PublicationSample> result = repo.findByBibliographicalDataIdIn(List.of(bibliographicalDataId_Double));

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(psDouble));
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnMultiplePublications_whenMultipleMatchesExist() {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample psDouble_1 = mock(PublicationSample.class);
        PublicationSample psDouble_2 = mock(PublicationSample.class);

        when(psDouble_1.getBibliographicalDataID()).thenReturn(bibliographicalDataId_Double);
        when(psDouble_2.getBibliographicalDataID()).thenReturn(bibliographicalDataId_Double);
        when(psDouble_1.id()).thenReturn(id_1);
        when(psDouble_2.id()).thenReturn(id_2);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(psDouble_1);
        repo.save(psDouble_2);

        // Act
        List<PublicationSample> result = repo.findByBibliographicalDataIdIn(List.of(bibliographicalDataId_Double));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(psDouble_1));
        assertTrue(result.contains(psDouble_2));
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnEmptyList_whenNoMatchExists() {
        // Arrange
        BibliographicalDataId bdId_1 = mock(BibliographicalDataId.class);

        PublicationSample psDouble = mock(PublicationSample.class);
        when(psDouble.getBibliographicalDataID()).thenReturn(bdId_1);
        when(psDouble.id()).thenReturn(mock(PublicationSampleId.class));

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(psDouble);

        // Act
        List<PublicationSample> result = repo.findByBibliographicalDataIdIn(List.of(bibliographicalDataId_Double));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findByBibliographicalDataIdIn(List.of(bibliographicalDataId_Double));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByBibliographicalDataIdIn_shouldThrowException_whenListIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> repo.findByBibliographicalDataIdIn(null));
    }

    // =========================================================================
    // findIdsByEditionIdIn
    // =========================================================================

    @Test
    void findIdsByEditionIdIn_shouldReturnMultipleIds_whenMultiplePubSamplesMatchEditionIds() {
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

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample1);
        repo.save(pbSample2);
        repo.save(pbSample3);

        // Act
        List<PublicationSampleId> result = repo.findIdsByEditionIdIn(List.of(editionId1Double, editionId2Double));

        // Assert
        assertEquals(3, result.size());
    }

    @Test
    void findIdsByEditionIdIn_shouldReturnOneId_whenOnePubSampleMatchesEditionId() {
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

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSampleId> result = repo.findIdsByEditionIdIn(List.of(editionId1Double));

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void findIdsByEditionIdIn_shouldReturnEmptyList_whenNoMatchesEditionIds() {
        // Arrange
        EditionId editionId1Double = mock(EditionId.class);
        EditionId editionId2Double = mock(EditionId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getEditionID()).thenReturn(editionId1Double);
        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getEditionID()).thenReturn(editionId1Double);
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSampleId> result = repo.findIdsByEditionIdIn(List.of(editionId2Double));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findIdsByEditionIdIn_shouldThrowNullPointerException_whenEditionIdsIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.findIdsByEditionIdIn(null));
    }

    // =========================================================================
    // findByEditionIdIn
    // =========================================================================

    @Test
    void findByEditionIdIn_shouldReturnMatchingPublications_whenMultipleMatchEditionIds() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_3 = mock(PublicationSampleId.class);

        EditionId editionId_1 = mock(EditionId.class);
        EditionId editionId_2 = mock(EditionId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getEditionID()).thenReturn(editionId_1);
        when(pbSample_1.id()).thenReturn(pbId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getEditionID()).thenReturn(editionId_2);
        when(pbSample_2.id()).thenReturn(pbId_2);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getEditionID()).thenReturn(editionId_1);
        when(pbSample_3.id()).thenReturn(pbId_3);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        // Act
        List<PublicationSample> result = repo.findByEditionIdIn(List.of(editionId_1, editionId_2));

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(pbSample_1));
        assertTrue(result.contains(pbSample_2));
        assertTrue(result.contains(pbSample_3));
    }

    @Test
    void findByEditionIdIn_shouldReturnOnePublication_whenOneMatchesEditionId() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);

        EditionId editionId_1 = mock(EditionId.class);
        EditionId editionId_2 = mock(EditionId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getEditionID()).thenReturn(editionId_1);
        when(pbSample_1.id()).thenReturn(pbId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getEditionID()).thenReturn(editionId_2);
        when(pbSample_2.id()).thenReturn(pbId_2);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByEditionIdIn(List.of(editionId_1));

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(pbSample_1));
        assertFalse(result.contains(pbSample_2));
    }

    @Test
    void findByEditionIdIn_shouldReturnEmptyList_whenNoMatchesEditionIds() {
        // Arrange
        EditionId editionId_1 = mock(EditionId.class);
        EditionId editionId_2 = mock(EditionId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getEditionID()).thenReturn(editionId_1);
        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);

        // Act
        List<PublicationSample> result = repo.findByEditionIdIn(List.of(editionId_2));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByEditionIdIn_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange
        EditionId editionId = mock(EditionId.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findByEditionIdIn(List.of(editionId));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByEditionIdIn_shouldThrowNullPointerException_whenEditionIdsIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.findByEditionIdIn(null));
    }

    // =========================================================================
    // findByLibraryId
    // =========================================================================

    @Test
    void findByLibraryId_shouldReturnPublicationsBelongingToGivenLibrary() {
        // Arrange
        LibraryId libraryId_1 = mock(LibraryId.class);
        LibraryId libraryId_2 = mock(LibraryId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        PublicationSample pbSample_3 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_3.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.getLibraryID()).thenReturn(libraryId_1);
        when(pbSample_2.getLibraryID()).thenReturn(libraryId_1);
        when(pbSample_3.getLibraryID()).thenReturn(libraryId_2);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
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
    void findByLibraryId_shouldReturnOnlyPublicationsWithMatchingLibraryId_whenMixed() {
        // Arrange
        LibraryId libraryId = mock(LibraryId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        PublicationSample pbSample_3 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_3.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.getLibraryID()).thenReturn(libraryId);
        when(pbSample_2.getLibraryID()).thenReturn(null);
        when(pbSample_3.getLibraryID()).thenReturn(mock(LibraryId.class));

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
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

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        PublicationSample pbSample_3 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_3.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.getLibraryID()).thenReturn(null);
        when(pbSample_2.getLibraryID()).thenReturn(libraryId);
        when(pbSample_3.getLibraryID()).thenReturn(null);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
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

    @Test
    void findByLibraryId_shouldReturnEmptyList_whenNoPublicationBelongsToLibrary() {
        // Arrange
        LibraryId libraryId_1 = mock(LibraryId.class);
        LibraryId libraryId_2 = mock(LibraryId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.getLibraryID()).thenReturn(libraryId_1);
        when(pbSample_2.getLibraryID()).thenReturn(libraryId_1);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
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
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findByLibraryId(libraryId);

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findByCollectionOfPublicationsId
    // =========================================================================

    @Test
    void findByCollectionOfPublicationsId_shouldReturnMatchingPublications() {
        // Arrange
        PublicationSample p1 = mock(PublicationSample.class);
        PublicationSample p2 = mock(PublicationSample.class);

        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(p1.id()).thenReturn(mock(PublicationSampleId.class));
        when(p2.id()).thenReturn(mock(PublicationSampleId.class));
        when(p1.getCollecOfPubID()).thenReturn(id);
        when(p2.getCollecOfPubID()).thenReturn(null);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(p1);
        repo.save(p2);

        // Act
        List<PublicationSample> result = repo.findByCollectionOfPublicationsId(id);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(p1));
    }

    @Test
    void findByCollectionOfPublicationsId_shouldReturnEmptyList_whenNoMatchExists() {
        // Arrange
        PublicationSample p = mock(PublicationSample.class);
        when(p.id()).thenReturn(mock(PublicationSampleId.class));
        when(p.getCollecOfPubID()).thenReturn(null);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(p);

        // Act
        List<PublicationSample> result = repo.findByCollectionOfPublicationsId(mock(CollectionOfPublicationsId.class));

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findAllByOwnerId
    // =========================================================================

    @Test
    void findAllByOwnerId_shouldReturnAllPublications_whenUserOwnsMultiple() {
        // Arrange
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        PublicationSample pbSample_3 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_3.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_3.isOwnedByUser(userID_Double)).thenReturn(true);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        // Act
        List<PublicationSample> result = repo.findAllByOwnerId(userID_Double);

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(pbSample_1));
        assertTrue(result.contains(pbSample_2));
        assertTrue(result.contains(pbSample_3));
    }

    @Test
    void findAllByOwnerId_shouldReturnOnePublication_whenUserOwnsOne() {
        // Arrange
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(false);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findAllByOwnerId(userID_Double);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(pbSample_1));
        assertFalse(result.contains(pbSample_2));
    }

    @Test
    void findAllByOwnerId_shouldNotReturnPublicationsOwnedByOtherUsers() {
        // Arrange
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(false);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(false);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findAllByOwnerId(userID_Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByOwnerId_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findAllByOwnerId(userID_Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findPublicationsInLibrary
    // =========================================================================

    @Test
    void findPublicationsInLibrary_shouldReturnAllPublications_whenAllAreInLibrary() {
        // Arrange
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.isInUserLibrary()).thenReturn(true);
        when(pbSample_2.isInUserLibrary()).thenReturn(true);

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(true);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findPublicationsInLibrary(userID_Double);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pbSample_1));
        assertTrue(result.contains(pbSample_2));
    }

    @Test
    void findPublicationsInLibrary_shouldReturnOnlyInLibrary_whenMixed() {
        // Arrange
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.isInUserLibrary()).thenReturn(true);
        when(pbSample_2.isInUserLibrary()).thenReturn(false);

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(true);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findPublicationsInLibrary(userID_Double);

        // Assert
        assertEquals(1, result.size());
        assertEquals(pbSample_1, result.get(0));
    }

    @Test
    void findPublicationsInLibrary_shouldNotReturnPublicationsOwnedByOtherUsers() {
        // Arrange
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.isInUserLibrary()).thenReturn(true);
        when(pbSample_2.isInUserLibrary()).thenReturn(true);

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(false);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findPublicationsInLibrary(userID_Double);

        // Assert
        assertEquals(1, result.size());
        assertEquals(pbSample_1, result.get(0));
    }

    @Test
    void findPublicationsInLibrary_shouldReturnEmptyList_whenNoneAreInLibrary() {
        // Arrange
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.isInUserLibrary()).thenReturn(false);
        when(pbSample_2.isInUserLibrary()).thenReturn(false);

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(true);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findPublicationsInLibrary(userID_Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicationsInLibrary_shouldThrowNullPointerException_whenUserIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.findPublicationsInLibrary(null));
    }

    // =========================================================================
    // findPublicationsNotInLibrary
    // =========================================================================

    @Test
    void findPublicationsNotInLibrary_shouldReturnAllPublications_whenNoneAreInLibrary() {
        // Arrange
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(true);

        when(pbSample_1.isInUserLibrary()).thenReturn(false);
        when(pbSample_2.isInUserLibrary()).thenReturn(false);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findPublicationsNotInLibrary(userID_Double);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pbSample_1));
        assertTrue(result.contains(pbSample_2));
    }

    @Test
    void findPublicationsNotInLibrary_shouldReturnOnlyNotInLibrary_whenMixed() {
        // Arrange
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(true);

        when(pbSample_1.isInUserLibrary()).thenReturn(false);
        when(pbSample_2.isInUserLibrary()).thenReturn(true);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findPublicationsNotInLibrary(userID_Double);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(pbSample_1));
        assertFalse(result.contains(pbSample_2));
    }

    @Test
    void findPublicationsNotInLibrary_shouldNotReturnPublicationsOwnedByOtherUsers() {
        // Arrange
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(false);

        when(pbSample_1.isInUserLibrary()).thenReturn(false);
        when(pbSample_2.isInUserLibrary()).thenReturn(false);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findPublicationsNotInLibrary(userID_Double);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(pbSample_1));
        assertFalse(result.contains(pbSample_2));
    }

    @Test
    void findPublicationsNotInLibrary_shouldReturnEmptyList_whenAllAreInLibrary() {
        // Arrange
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userID_Double)).thenReturn(true);

        when(pbSample_1.isInUserLibrary()).thenReturn(true);
        when(pbSample_2.isInUserLibrary()).thenReturn(true);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findPublicationsNotInLibrary(userID_Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicationsNotInLibrary_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findPublicationsNotInLibrary(userID_Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicationsNotInLibrary_shouldThrowNullPointerException_whenUserIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repo.findPublicationsNotInLibrary(null));
    }

    // =========================================================================
    // findByLibraryIdAndPublicationStatusNotIn
    // =========================================================================

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnMatchingPublications() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
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

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldThrowException_whenLibraryIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.findByLibraryIdAndPublicationStatusNotIn(null, List.of(PublicationStatus.FOR_SALE)));
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldThrowException_whenStatusesListIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        LibraryId libraryId = mock(LibraryId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.findByLibraryIdAndPublicationStatusNotIn(libraryId, null));
    }
}