package TOPSECRET.persistence.mem;

import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
 * <p>Test coverage is organised into the following sections:</p>
 * <ul>
 *   <li><b>Constructor</b> – valid instantiation and null factory guard.</li>
 *   <li><b>createAndAdd</b> – delegation to factory, null-argument guards, and persistence.</li>
 *   <li><b>save</b> – return value, side effects, duplicate rejection, and null guard.</li>
 *   <li><b>findAll</b> – empty state, single and multiple elements, immutability.</li>
 *   <li><b>findById</b> – null guard, match found, no match, and multiple-sample disambiguation.</li>
 *   <li><b>existsById</b> – presence and absence checks, empty repository, and null guard.</li>
 *   <li><b>findIdsByBibliographicalDataId</b> – multiple matches, single match,
 *       no matches, and null bibliographical data ID guard.</li>
 *   <li><b>findByUserIdAndLibraryIdIsNotNull</b> – publications owned by user in a library,
 *       no publications in library, filtering out other users' publications,
 *       filtering out publications not in a library, and null userId guard.</li>
 *   <li><b>findByLibraryId</b> – matching publications, empty results, null library ID.</li>
 *   <li><b>findByUserIdAndLibraryIdIsNull</b> – publications without library, mixed states.</li>
 *   <li><b>findAvailableForSaleByLibraryId</b> – available and unavailable samples.</li>
 *   <li><b>findPublicationSamplesIdsByBiblioDataIds</b> – multiple matches, single match, no matches.</li>
 *   <li><b>addPublicationToLibrary</b> – success, null guards, duplicate and already-assigned rejection.</li>
 *   <li><b>findIdsByBibliographicalDataIdIn</b> – matching IDs, empty results, null guard.</li>
 *   <li><b>findByCollectionOfPublicationsId</b> – match and no match.</li>
 *   <li><b>assignPublicationsToCollection</b> – success, null guards, not-found guard.</li>
 *   <li><b>findAvailableForCollection</b> – excludes already-assigned publications.</li>
 * </ul>
 */
class PublicationSampleRepositoryMemTest {

    private BibliographicalDataId bibliographicalDataId_Double;
    private EditionId editionId_Double;
    private UserId userID_Double;

    @BeforeEach
    void setUp() {
        userID_Double = createDoubleUserID();
        editionId_Double = createDoubleEditionID();
        bibliographicalDataId_Double = createDoubleBibliographicalDataID();
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
    void save_shouldThrowException_whenPublicationSampleAlreadyExists() {
        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(pbSampleId);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(pbSample));
    }

    @Test
    void save_shouldThrowException_whenPublicationSampleIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
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
    void save_shouldNotAddDuplicate_whenExceptionIsThrown() {
        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(pbSampleId);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample);

        assertThrows(IllegalArgumentException.class, () -> repo.save(pbSample));

        assertEquals(1, repo.findAll().size());
    }

    // ===========================================================
    // ========== Tests for findAll ==============================
    // ===========================================================

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
    void findAll_shouldReturnAllElements_whenMultiplePublicationsWereAdded()  {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.id()).thenReturn(pbId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
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
        PublicationSample pbSample = mock(PublicationSample.class);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findAll();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(PublicationSample.class)));
    }

    // =========================================================================
// update()
// =========================================================================

    @Test
    void updateShouldReturnUpdatedPublicationSample() {

        // ARRANGE
        PublicationSample publicationSample = mock(PublicationSample.class);
        PublicationSampleId id = mock(PublicationSampleId.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        when(publicationSample.id()).thenReturn(id);

        repo.save(publicationSample);

        // ACT
        PublicationSample result =
                repo.update(publicationSample);

        // ASSERT
        assertEquals(publicationSample, result);
    }

    @Test
    void updateShouldKeepUpdatedPublicationSampleInRepository() {

        // ARRANGE
        PublicationSample publicationSample = mock(PublicationSample.class);
        PublicationSampleId id = mock(PublicationSampleId.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        when(publicationSample.id()).thenReturn(id);

        repo.save(publicationSample);

        // ACT
        repo.update(publicationSample);

        // ASSERT
        assertTrue(repo.existsById(id));
    }

    @Test
    void updateShouldThrowExceptionWhenPublicationSampleIsNull() {

        //ARRANGE
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repo.update(null));
    }

    @Test
    void updateShouldThrowExceptionWhenPublicationSampleDoesNotExist() {

        // ARRANGE
        PublicationSample publicationSample = mock(PublicationSample.class);
        PublicationSampleId id = mock(PublicationSampleId.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        when(publicationSample.id()).thenReturn(id);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repo.update(publicationSample));
    }

    // ===========================================================
    // ========== Tests for findById =============================
    // ===========================================================

    @Test
    void findById_shouldThrowException_whenIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findById(null));
    }

    @Test
    void findPublicationSampleById_shouldReturn_whenIdExists() {
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
    void find_whenMultipleSamplesExist() {
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

    // ===========================================================
    // ========== Tests for existsById ===========================
    // ===========================================================

    @Test
    void existsById_returnsTrue_WhenRepoContainsSamePublicationSample() {
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
    void existsById_returnsFalse_whenEmptyRepo() {
        // Arrange
        PublicationSampleId psId = mock(PublicationSampleId.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertFalse(repo.existsById(psId));
    }

    @Test
    void existsById_shouldThrowException_whenPublicationSampleIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
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
    void findPubSampleIdsByBibliographicalDataId_shouldThrowNullPointerException_whenBdIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findIdsByBibliographicalDataId(null));
    }

    @Test
    void findPubSampleIdsByBibliographicalDataId_shouldReturnEmptyList_whenNoMatchesBdId()  {
        // Arrange
        BibliographicalDataId bdId_1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId_2 = mock(BibliographicalDataId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_2);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_2);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getBibliographicalDataID()).thenReturn(bdId_2);


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

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

        BibliographicalDataId bdId1Double = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId2Double = mock(BibliographicalDataId.class);

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
    void findIdsByEditionIdIn_shouldReturnListWithOneId_whenOnePubSampleMatchesEditionIds()  {
        // Arrange

        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        BibliographicalDataId bdId1Double = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId2Double = mock(BibliographicalDataId.class);

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
    void findIdsByEditionIdIn_shouldThrowNullPointerException_whenEditionIdsIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findIdsByEditionIdIn(null));
    }

    @Test
    void findIdsByEditionIdIn_shouldReturnEmptyList_whenNoPubSampleMatchesEditionIds()  {
        // Arrange
        BibliographicalDataId bdId1Double = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId2Double = mock(BibliographicalDataId.class);

        EditionId editionId1Double = mock(EditionId.class);
        EditionId editionId2Double = mock(EditionId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getEditionID()).thenReturn(editionId1Double);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getEditionID()).thenReturn(editionId1Double);


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

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


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNotNull(userID_Double);

        // Assert
        assertEquals(2, result.size());
        assertEquals(result.get(0), pbSample_1);
        assertEquals(result.get(1), pbSample_2);
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


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNotNull(userID_Double);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldNotReturn_publicationsOwnedByOtherUsers()  {
        // Arrange
        UserId userId_Other = mock(UserId.class);

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


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNotNull(userID_Double);

        // Assert
        assertEquals(1, result.size());
        assertEquals(result.get(0), pbSample_1);
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


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNotNull(userID_Double);

        // Assert
        assertEquals(1, result.size());
        assertEquals(result.get(0), pbSample_1);
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldThrowException_whenUserIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
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

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.findByLibraryId(libraryId_2);

        // Assert
        assertNotNull(result);
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
        assertNotNull(result);
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


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

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


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNull(userID_Double);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldNotReturn_publicationsOwnedByOtherUsers()  {
        // Arrange
        UserId userId_Other = mock(UserId.class);

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

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

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


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

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
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findByUserIdAndLibraryIdIsNull(userID_Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldThrowNullPointerException_whenUserIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repo.findByUserIdAndLibraryIdIsNull(null));
    }

    // ==================================================================================
    // =========== Tests for findAvailableForSaleByLibraryId ============================
    // ==================================================================================

    @Test
    void findAvailableForSaleByLibraryId_shouldReturnOnlyAvailableSamples() {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);

        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);
        PublicationSampleId id_3 = mock(PublicationSampleId.class);

        PublicationSample ps1Double = mock(PublicationSample.class);
        PublicationSample ps2Double = mock(PublicationSample.class);
        PublicationSample ps3Double = mock(PublicationSample.class);

        when(ps1Double.id()).thenReturn(id_1);
        when(ps2Double.id()).thenReturn(id_2);
        when(ps3Double.id()).thenReturn(id_3);

        when(ps1Double.isAvailableForSale()).thenReturn(true);
        when(ps2Double.isAvailableForSale()).thenReturn(false);
        when(ps3Double.isAvailableForSale()).thenReturn(true);
        when(ps1Double.getLibraryID()).thenReturn(libraryIdDouble);
        when(ps2Double.getLibraryID()).thenReturn(libraryIdDouble);
        when(ps3Double.getLibraryID()).thenReturn(libraryIdDouble);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(ps1Double);
        repo.save(ps2Double);
        repo.save(ps3Double);

        // Act
        List<PublicationSample> result = repo.findAvailableForSaleByLibraryId(libraryIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(ps1Double));
        assertTrue(result.contains(ps3Double));
        assertFalse(result.contains(ps2Double));
    }

    @Test
    void findAvailableForSaleByLibraryId_shouldReturnEmptyListWhenNoSamplesAvailable() {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);

        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample ps1Double = mock(PublicationSample.class);
        PublicationSample ps2Double = mock(PublicationSample.class);

        when(ps1Double.id()).thenReturn(id_1);
        when(ps2Double.id()).thenReturn(id_2);

        when(ps1Double.isAvailableForSale()).thenReturn(false);
        when(ps2Double.isAvailableForSale()).thenReturn(false);
        when(ps1Double.getLibraryID()).thenReturn(libraryIdDouble);
        when(ps2Double.getLibraryID()).thenReturn(libraryIdDouble);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(ps1Double);
        repo.save(ps2Double);

        // Act
        List<PublicationSample> result = repo.findAvailableForSaleByLibraryId(libraryIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAvailableForSaleByLibraryId_shouldReturnEmptyListWhenLibraryHasNoSamples() {
        // Arrange
        LibraryId libraryIdDouble = mock(LibraryId.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        List<PublicationSample> result = repo.findAvailableForSaleByLibraryId(libraryIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    // ===========================================================
    // == Tests for findPublicationSamplesIdsByBiblioDataIds =====
    // ===========================================================

    @Test
    void findPublicationSamplesIdsByBiblioDataIds_shouldReturnListWithMultipleIds_whenMultiplePublicationSamplesIdsMatchGivenBdIds() {
        // Arrange
        BibliographicalDataId bdId_1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId_2 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId_3 = mock(BibliographicalDataId.class);

        PublicationSampleId psId_1 = mock(PublicationSampleId.class);
        PublicationSampleId psId_2 = mock(PublicationSampleId.class);
        PublicationSampleId psId_3 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_1.id()).thenReturn(psId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_2.id()).thenReturn(psId_2);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getBibliographicalDataID()).thenReturn(bdId_3);
        when(pbSample_3.id()).thenReturn(psId_3);


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        List<BibliographicalDataId> bds = List.of(bdId_1, bdId_2);

        // Act
        List<PublicationSampleId> result = repo.findPublicationSamplesIdsByBiblioDataIds(bds);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(psId_1));
        assertTrue(result.contains(psId_2));
        assertFalse(result.contains(psId_3));
    }

    @Test
    void findPublicationSamplesIdsByBiblioDataIds_shouldReturnListWithOneId_whenOnePublicationSampleMatchesGivenBdIds()  {
        // Arrange
        BibliographicalDataId bdId_1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId_2 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId_3 = mock(BibliographicalDataId.class);

        PublicationSampleId psId_1 = mock(PublicationSampleId.class);
        PublicationSampleId psId_2 = mock(PublicationSampleId.class);
        PublicationSampleId psId_3 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_1.id()).thenReturn(psId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_2.id()).thenReturn(psId_2);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getBibliographicalDataID()).thenReturn(bdId_3);
        when(pbSample_3.id()).thenReturn(psId_3);


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        List<BibliographicalDataId> bds = List.of(bdId_2);

        // Act
        List<PublicationSampleId> result = repo.findPublicationSamplesIdsByBiblioDataIds(bds);

        // Assert
        assertEquals(1, result.size());
        assertEquals(psId_2, result.get(0));
    }

    @Test
    void findPublicationSamplesIdsByBiblioDataIds_shouldReturnEmptyList_whenNoPublicationSampleMatchesGivenBdIds() {
        // Arrange
        BibliographicalDataId bdId_1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId_2 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId_3 = mock(BibliographicalDataId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_1);
        when(pbSample_1.id()).thenReturn(mock(PublicationSampleId.class));

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_2);
        when(pbSample_2.id()).thenReturn(mock(PublicationSampleId.class));

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getBibliographicalDataID()).thenReturn(bdId_3);
        when(pbSample_3.id()).thenReturn(mock(PublicationSampleId.class));


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        List<BibliographicalDataId> bds = List.of(mock(BibliographicalDataId.class));

        // Act
        List<PublicationSampleId> result = repo.findPublicationSamplesIdsByBiblioDataIds(bds);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findPublicationSamplesIdsByBiblioDataIds_shouldThrowException_whenBdIdsListIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.findPublicationSamplesIdsByBiblioDataIds(null));
    }

    // ===========================================================
    // ======== Tests for addPublicationToLibrary ================
    // ===========================================================

    @Test
    void addPublicationToLibrary_shouldAddPublicationAndReturnTrue() {
        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample publication = mock(PublicationSample.class);
        when(publication.id()).thenReturn(pbSampleId);

        LibraryId libraryId = mock(LibraryId.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act
        boolean result = repo.addPublicationToLibrary(publication, libraryId);

        // Assert
        assertTrue(result);
        assertTrue(repo.existsById(pbSampleId));
    }

    @Test
    void addPublicationToLibrary_shouldThrowException_whenLibraryIdIsNull() {
        // Arrange
        PublicationSample publication = mock(PublicationSample.class);
        doThrow(new IllegalArgumentException("LibraryID can not be null"))
                .when(publication).addPublicationToLibrary(null);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.addPublicationToLibrary(publication, null));
    }

    @Test
    void addPublicationToLibrary_shouldThrowException_whenAlreadyAssignedToLibrary() {
        // Arrange
        PublicationSample publication = mock(PublicationSample.class);
        LibraryId libraryId = mock(LibraryId.class);
        doThrow(new IllegalArgumentException("Already assigned to a library"))
                .when(publication).addPublicationToLibrary(libraryId);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.addPublicationToLibrary(publication, libraryId));
    }

    @Test
    void addPublicationToLibrary_shouldThrowException_whenPublicationAlreadyExists() {
        // Arrange
        PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
        PublicationSample publication = mock(PublicationSample.class);
        when(publication.id()).thenReturn(pbSampleId);

        LibraryId libraryId = mock(LibraryId.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(publication);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.addPublicationToLibrary(publication, libraryId));
    }

    @Test
    void addPublicationToLibrary_shouldThrowException_whenPublicationIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        LibraryId libraryId = mock(LibraryId.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.addPublicationToLibrary(null, libraryId));
    }

    @Test
    void addPublicationToLibrary_shouldThrowException_whenLibraryIsNull() {
        // Arrange
        PublicationSample publication = mock(PublicationSample.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.addPublicationToLibrary(publication, null));
    }

    // ===========================================================
    // == Tests for findIdsByBibliographicalDataIdIn =============
    // ===========================================================

    @Test
    void findIdsByBibliographicalDataIdIn_shouldThrowException_whenListIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findIdsByBibliographicalDataIdIn(null));
    }

    @Test
    void findIdsByBibliographicalDataIdIn_shouldReturnMatchingIds() {
        // Arrange
        PublicationSampleId psDoubleId = mock(PublicationSampleId.class);
        PublicationSample psDouble = mock(PublicationSample.class);
        when(psDouble.getBibliographicalDataID()).thenReturn(bibliographicalDataId_Double);
        when(psDouble.id()).thenReturn(psDoubleId);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(psDouble);

        // Act
        List<PublicationSampleId> result =
                repo.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId_Double));

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

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(psDouble);

        // Act
        List<PublicationSampleId> result =
                repo.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId_Double));

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

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(psDouble1);
        repo.save(psDouble2);

        // Act
        List<PublicationSampleId> result =
                repo.findIdsByBibliographicalDataIdIn(List.of(bibliographicalDataId_Double));

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
    void shouldReturnEmptyWhenNoPublicationMatchesCollection() {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSample p = mock(PublicationSample.class);
        when(p.id()).thenReturn(id_1);
        when(p.getCollecOfPubID()).thenReturn(null);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(p);

        // Act
        List<PublicationSample> result =
                repo.findByCollectionOfPublicationsId(mock(CollectionOfPublicationsId.class));

        // Assert
        assertTrue(result.isEmpty());
    }

    // ===========================================================
    // == Tests for assignPublicationsToCollection ===============
    // ===========================================================

    @Test
    void shouldAssignPublicationsToCollection() {
        // Arrange
        PublicationSampleId id = mock(PublicationSampleId.class);
        PublicationSample p = mock(PublicationSample.class);
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        when(p.id()).thenReturn(id);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(p);

        // Act
        boolean result = repo.assignPublicationsToCollection(collectionId, List.of(id));

        // Assert
        assertTrue(result);
        verify(p).addPublicationToCollection(collectionId);
    }

    @Test
    void shouldThrowWhenPublicationNotFound() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.assignPublicationsToCollection(
                        mock(CollectionOfPublicationsId.class),
                        List.of(mock(PublicationSampleId.class))));
    }

    @Test
    void shouldThrowWhenCollectionIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.assignPublicationsToCollection(null, List.of(mock(PublicationSampleId.class))));
    }

    @Test
    void shouldThrowWhenPublicationListIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.assignPublicationsToCollection(
                        mock(CollectionOfPublicationsId.class), null));
    }

    @Test
    void shouldThrowWhenPublicationListIsEmpty() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.assignPublicationsToCollection(
                        mock(CollectionOfPublicationsId.class), new ArrayList<>()));
    }

    // ===========================================================
    // == Tests for findAvailableForCollection ===================
    // ===========================================================

    @Test
    void shouldReturnOnlyPublicationsNotInCollection() {
        // Arrange
        UserId userId = mock(UserId.class);
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);

        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pub1 = mock(PublicationSample.class);
        PublicationSample pub2 = mock(PublicationSample.class);

        when(pub1.id()).thenReturn(id_1);
        when(pub2.id()).thenReturn(id_2);

        when(pub1.isOwnedByUser(userId)).thenReturn(true);
        when(pub1.isInUserLibrary()).thenReturn(true);

        when(pub2.isOwnedByUser(userId)).thenReturn(true);
        when(pub2.isInUserLibrary()).thenReturn(true);

        when(pub1.getCollecOfPubID()).thenReturn(null);
        when(pub2.getCollecOfPubID()).thenReturn(collectionId);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem();
        repo.save(pub1);
        repo.save(pub2);

        // Act
        List<PublicationSample> result = repo.findAvailableForCollection(userId, collectionId);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(pub1));
        assertFalse(result.contains(pub2));
    }

    // ===========================================================
    // =================== Helper Methods ========================
    // ===========================================================

    private static BibliographicalDataId createDoubleBibliographicalDataID() {
        BibliographicalDataId bdID = mock(BibliographicalDataId.class);
        when(bdID.getID()).thenReturn(UUID.randomUUID().toString());
        return bdID;
    }

    private static UserId createDoubleUserID() {
        UserId userID = mock(UserId.class);
        when(userID.getUsername()).thenReturn(UUID.randomUUID().toString());
        return userID;
    }

    private static EditionId createDoubleEditionID() {
        EditionId editionID = mock(EditionId.class);
        when(editionID.getID()).thenReturn(UUID.randomUUID().toString());
        return editionID;
    }
}