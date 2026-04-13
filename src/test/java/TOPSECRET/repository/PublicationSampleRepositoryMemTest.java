package TOPSECRET.repository;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

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
 * <p>All collaborators ({@link PublicationSampleFactory}, {@link BibliographicalDataID},
 * {@link UserId}, {@link EditionID}) are provided as Mockito test doubles,
 * keeping each test focused on the behaviour of the repository in isolation.</p>
 *
 * <p>Test coverage is organised into the following sections:</p>
 * <ul>
 *   <li><b>Constructor</b> – valid instantiation and null factory guard.</li>
 *   <li><b>createAndAdd</b> – delegation to factory, null-argument guards, and persistence.</li>
 *   <li><b>save</b> – return value, side effects, duplicate rejection, and null guard.</li>
 *   <li><b>getAllPublications</b> – empty state, single and multiple elements, immutability.</li>
 *   <li><b>findById</b> – null guard, match found, no match, and multiple-sample disambiguation.</li>
 *   <li><b>isExistent</b> – presence and absence checks, empty repository, and null guard.</li>
 *   <li><b>findPubSampleIdsByBibliographicalDataId</b> – multiple matches, single match,
 *       no matches, and null bibliographical data ID guard.</li>
 *   <li><b>getAllPublicationsInUserLibrary</b> – publications owned by user in a library,
 *       no publications in library, filtering out other users' publications,
 *       filtering out publications not in a library, and null userId guard.</li>
 * </ul>
 */

class PublicationSampleRepositoryMemTest {

    private PublicationSampleFactory factory;
    private BibliographicalDataID bibliographicalDataID_Double;
    private EditionID editionID_Double;
    private UserId userID_Double;

    @BeforeEach
    void setUp(){
        factory = mock(PublicationSampleFactory.class);
        userID_Double = createDoubleUserID();
        editionID_Double = createDoubleEditionID();
        bibliographicalDataID_Double = createDoubleBibliographicalDataID();

    }

    // ===========================================================
    // ========== Tests for Constructor ==========================
    // ===========================================================

    @Test
    void constructor_shouldCreateRepository_whenValidFactoryIsProvided() {
        // Act + Assert
        assertDoesNotThrow(() -> new PublicationSampleRepositoryMem(factory));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> new PublicationSampleRepositoryMem(null));
    }


    // ===========================================================
    // ============== Tests for createAndAdd =====================
    // ===========================================================

    @Test
    void createAndAdd_shouldReturnThePublicationSampleCreatedByFactory() throws InstanceAlreadyExistsException {

        // Arrange
        PublicationSample pbSample = mock(PublicationSample.class);
        when(factory.create(bibliographicalDataID_Double,userID_Double, PublicationStatus.READ, editionID_Double,Condition.NEW)).thenReturn(pbSample);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act
        PublicationSample result =
                repo.createAndAdd(
                        bibliographicalDataID_Double,userID_Double, PublicationStatus.READ, editionID_Double,Condition.NEW);

        // Assert
        assertSame(pbSample, result);
    }

    @Test
    void createAndAdd_shouldThrowException_whenBibliographicalDataIDIsNull() {

        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(null, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW));
    }

    @Test
    void createAndAdd_shouldThrowException_whenUserIsNull() {

        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(bibliographicalDataID_Double,null, PublicationStatus.READ, editionID_Double, Condition.NEW));
    }

    @Test
    void createAndAdd_shouldThrowException_whenStatusIsNull() {

        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(bibliographicalDataID_Double,userID_Double, null, editionID_Double, Condition.NEW));
    }

    @Test
    void createAndAdd_shouldThrowException_whenEditionIsNull() {

        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(bibliographicalDataID_Double,userID_Double, PublicationStatus.READ,null, Condition.NEW));
    }

    @Test
    void createAndAdd_shouldThrowException_whenConditionIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(bibliographicalDataID_Double,userID_Double, PublicationStatus.READ,editionID_Double, null));
    }

    // ===========================================================
    // ========== Tests for save =================================
    // ===========================================================

    @Test
    void save_shouldReturnTheSamePublicationSample() {
        // Arrange
        PublicationSample pbSample = mock(PublicationSample.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act
        PublicationSample result = repo.save(pbSample);

        // Assert
        assertSame(pbSample, result);
    }

    @Test
    void save_shouldAddPublicationSampleToRepository() {
        // Arrange
        PublicationSample pbSample = mock(PublicationSample.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act
        repo.save(pbSample);

        // Assert
        assertTrue(repo.isExistent(pbSample));
    }

    @Test
    void save_shouldThrowException_whenPublicationSampleAlreadyExists() {
        // Arrange
        PublicationSample pbSample = mock(PublicationSample.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.save(pbSample);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(pbSample));
    }

    @Test
    void save_shouldThrowException_whenPublicationSampleIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(null));
    }

    @Test
    void save_shouldAllowSavingMultipleDifferentPublicationSamples() {
        // Arrange
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Assert
        List<PublicationSample> all = repo.getAllPublications();
        assertEquals(2, all.size());
        assertTrue(all.contains(pbSample_1));
        assertTrue(all.contains(pbSample_2));
    }

    @Test
    void save_shouldNotAddDuplicate_whenExceptionIsThrown() {
        // Arrange
        PublicationSample pbSample = mock(PublicationSample.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.save(pbSample);

        assertThrows(IllegalArgumentException.class, () -> repo.save(pbSample));

        assertEquals(1, repo.getAllPublications().size());
    }

    // ===========================================================
    // ========== Tests for getAllPublications ===================
    // ===========================================================

    @Test
    void getAllPublications_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act
        List<PublicationSample> result = repo.getAllPublications();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublications_shouldReturnListWithOneElement_whenOnePublicationWasAdded() throws InstanceAlreadyExistsException {
        // Arrange
        PublicationSample pbSample = mock(PublicationSample.class);
        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);

        // Act
        List<PublicationSample> result = repo.getAllPublications();

        // Assert
        assertEquals(1, result.size());
        assertSame(pbSample, result.get(0));
    }

    @Test
    void getAllPublications_shouldReturnAllElements_whenMultiplePublicationsWereAdded() throws InstanceAlreadyExistsException {
        // Arrange
        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample_1);
        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_2);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);

        // Act
        List<PublicationSample> result = repo.getAllPublications();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pbSample_1));
        assertTrue(result.contains(pbSample_2));
    }

    @Test
    void getAllPublications_shouldReturnImmutableList() throws InstanceAlreadyExistsException {
        // Arrange
        PublicationSample pbSample = mock(PublicationSample.class);
        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);

        // Act
        List<PublicationSample> result = repo.getAllPublications();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(PublicationSample.class)));
    }


    // ===========================================================
    // ========== Tests for findPublicationSampleById ============
    // ===========================================================

    @Test
    void findPublicationSampleById_shouldThrowException_whenIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findPublicationSampleById(null));
    }

    @Test
    void findPublicationSampleById_shouldReturnPublicationSample_whenIdExists() throws InstanceAlreadyExistsException {
        // Arrange
        PublicationSampleId id = mock(PublicationSampleId.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(id);
        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);

        // Act
        Optional<PublicationSample> result = repo.findPublicationSampleById(id);

        // Assert
        assertTrue(result.isPresent());
        assertSame(pbSample, result.get());
    }

    @Test
    void findPublicationSampleById_shouldReturnEmptyOptional_whenIdDoesNotExist() {
        // Arrange
        PublicationSampleId id = mock(PublicationSampleId.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act
        Optional<PublicationSample> result = repo.findPublicationSampleById(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicationSampleById_shouldReturnCorrectSample_whenMultipleSamplesExist() throws InstanceAlreadyExistsException {
        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);

        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample_1);
        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_2);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);

        // Act
        Optional<PublicationSample> result = repo.findPublicationSampleById(id_2);

        // Assert
        assertTrue(result.isPresent());
        assertSame(pbSample_2, result.get());
    }

    // ===========================================================
    // ====== Tests for findPubSampleByBibliographicalDataId =========
    // ===========================================================

    @Test
    void findPubSampleIdsByBibliographicalDataId_shouldReturnListWithMultipleId_whenMultiplePubSampleMatchesBdId() throws InstanceAlreadyExistsException {

        // Arrange

        BibliographicalDataID bdId_1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId_2 = mock(BibliographicalDataID.class);


        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_1);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getBibliographicalDataID()).thenReturn(bdId_2);



        when(factory.create(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample_1);
        when(factory.create(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_2);
        when(factory.create(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_3);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);
        repo.createAndAdd(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);
        repo.createAndAdd(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);

        // Act
        List<PublicationSampleId> result = repo.findPubSampleIdsByBibliographicalDataId(bdId_1);

        // Assert
        assertEquals(2,result.size());

    }

    @Test
    void findPubSampleIdsByBibliographicalDataId_shouldReturnListWithOneId_whenOnePubSampleMatchesBdId() throws InstanceAlreadyExistsException {

        // Arrange

        BibliographicalDataID bdId_1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId_2 = mock(BibliographicalDataID.class);


        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_1);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_2);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getBibliographicalDataID()).thenReturn(bdId_2);



        when(factory.create(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample_1);
        when(factory.create(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_2);
        when(factory.create(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_3);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);
        repo.createAndAdd(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);
        repo.createAndAdd(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);

        // Act
        List<PublicationSampleId> result = repo.findPubSampleIdsByBibliographicalDataId(bdId_1);

        // Assert
        assertEquals(1,result.size());

    }

    @Test
    void findPubSampleByBibliographicalId_shouldThrowNullPointerException_whenBdIdIsNull() {

        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findPubSampleIdsByBibliographicalDataId(null));
    }

    @Test
    void findPubSampleIdsByBibliographicalDataId_shouldReturnEmptyList_whenNoPubSampleMatchesBdId() throws InstanceAlreadyExistsException {

        // Arrange
        BibliographicalDataID bdId_1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId_2 = mock(BibliographicalDataID.class);


        PublicationSample pbSample_1 = mock(PublicationSample.class);
        when(pbSample_1.getBibliographicalDataID()).thenReturn(bdId_2);

        PublicationSample pbSample_2 = mock(PublicationSample.class);
        when(pbSample_2.getBibliographicalDataID()).thenReturn(bdId_2);

        PublicationSample pbSample_3 = mock(PublicationSample.class);
        when(pbSample_3.getBibliographicalDataID()).thenReturn(bdId_2);



        when(factory.create(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample_1);
        when(factory.create(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_2);
        when(factory.create(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.LIKE_NEW))
                .thenReturn(pbSample_3);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);
        repo.createAndAdd(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);
        repo.createAndAdd(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.LIKE_NEW);

        // Act
        List<PublicationSampleId> result = repo.findPubSampleIdsByBibliographicalDataId(bdId_1);

        // Assert
        assertEquals(0,result.size());
    }




    // ===========================================================
    // ========== Tests for isExistent ===========================
    // ===========================================================

    @Test
    void isExistent_returnsTrue_WhenRepoContainsSamePublicationSample() throws InstanceAlreadyExistsException {

        // Arrange
        PublicationSample pbSample = mock(PublicationSample.class);
        when(factory.create(bibliographicalDataID_Double,userID_Double, PublicationStatus.READ, editionID_Double,Condition.NEW)).thenReturn(pbSample);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        repo.createAndAdd(bibliographicalDataID_Double,userID_Double, PublicationStatus.READ, editionID_Double,Condition.NEW);

        //Act
        boolean result = repo.isExistent(pbSample);

        //Assert
        assertTrue(result);
    }

    @Test
    void isExistent_shouldReturnFalse_whenPublicationSampleDoesNotExist() {
        // Arrange
        PublicationSample pbSample = mock(PublicationSample.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act
        boolean result = repo.isExistent(pbSample);

        // Assert
        assertFalse(result);
    }


    @Test
    void isExistent_returnsFalse_whenEmptyRepo() {

        // Arrange
        PublicationSample psDouble = mock(PublicationSample.class);

        PublicationSampleFactory psFactory = mock(PublicationSampleFactory.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(psFactory);

        // Act + Assert
        assertFalse(repo.isExistent(psDouble));

    }

    @Test
    void isExistent_shouldThrowException_whenPublicationSampleIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.isExistent(null));
    }

    // ===========================================================
    // ======== Tests for getAllPublicationsInUserLibrary ========
    // ===========================================================

    @Test
    void getAllPublicationsInUserLibrary_shouldReturnPublications_whenUserHasPublicationsInLibrary() throws InstanceAlreadyExistsException {

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


        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample_1);
        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_2);


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);


        // Act
        List<PublicationSample> result = repo.getAllPublicationsInUserLibrary(userID_Double);

        // Assert
        assertEquals(2, result.size());
        assertEquals(result.get(0),pbSample_1);
        assertEquals(result.get(1),pbSample_2);

    }

    @Test
    void getAllPublicationsInUserLibrary_shouldReturnEmptyList_whenUserHasNoPublicationsInLibrary() throws InstanceAlreadyExistsException {

        // Arrange
        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.getOwnerID()).thenReturn(userID_Double);
        when(pbSample_2.getOwnerID()).thenReturn(userID_Double);

        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);


        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample_1);
        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_2);


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);


        // Act
        List<PublicationSample> result = repo.getAllPublicationsInUserLibrary(userID_Double);

        // Assert
        assertEquals(0, result.size());

    }

    @Test
    void getAllPublicationsInUserLibrary_shouldNotReturn_publicationsOwnedByOtherUsers() throws InstanceAlreadyExistsException {

        // Arrange
        UserId userId_Other = mock(UserId.class);
        User user_Other = mock(User.class);

        when(user_Other.id()).thenReturn(userId_Other);

        PublicationSampleId id_1 = mock(PublicationSampleId.class);
        PublicationSampleId id_2 = mock(PublicationSampleId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);


        when(pbSample_1.id()).thenReturn(id_1);
        when(pbSample_2.id()).thenReturn(id_2);

        when(pbSample_1.isInUserLibrary()).thenReturn(true);
        when(pbSample_2.isInUserLibrary()).thenReturn(true);

        when(pbSample_1.isOwnedByUser(userID_Double)).thenReturn(true);
        when(pbSample_2.isOwnedByUser(userId_Other)).thenReturn(true);


        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample_1);
        when(factory.create(bibliographicalDataID_Double, userId_Other, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_2);


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);
        repo.createAndAdd(bibliographicalDataID_Double, userId_Other, PublicationStatus.READ, editionID_Double, Condition.POOR);


        // Act
        List<PublicationSample> result = repo.getAllPublicationsInUserLibrary(userID_Double);

        // Assert
        assertEquals(1, result.size());
        assertEquals(result.get(0),pbSample_1);
    }

    @Test
    void getAllPublicationsInUserLibrary_shouldNotReturn_publicationsNotInALibrary() throws InstanceAlreadyExistsException {

        //Arrange
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


        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample_1);
        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_2);


        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);


        // Act
        List<PublicationSample> result = repo.getAllPublicationsInUserLibrary(userID_Double);

        // Assert
        assertEquals(1, result.size());
        assertEquals(result.get(0),pbSample_1);
    }

    @Test
    void getAllPublicationsInUserLibrary_shouldThrowException_whenUserIdIsNull() throws InstanceAlreadyExistsException {

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample_1);
        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_2);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);


        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.getAllPublicationsInUserLibrary(null));
    }

    // ===========================================================
    // =========== Tests for getPublicationsByLibraryId ==========
    // ===========================================================

    @Test
    void getPublicationsByLibraryID_shouldReturnPublicationsBelongingToGivenLibrary() {
        // Arrange
        LibraryId libraryId_1 = mock(LibraryId.class);
        LibraryId libraryId_2 = mock(LibraryId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        PublicationSample pbSample_3 = mock(PublicationSample.class);

        when(pbSample_1.getLibraryID()).thenReturn(libraryId_1);
        when(pbSample_2.getLibraryID()).thenReturn(libraryId_1);
        when(pbSample_3.getLibraryID()).thenReturn(libraryId_2);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        // Act
        List<PublicationSample> result = repo.getPublicationsByLibraryID(libraryId_1);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pbSample_1));
        assertTrue(result.contains(pbSample_2));
        assertFalse(result.contains(pbSample_3));
    }

    @Test
    void getPublicationsByLibraryID_shouldReturnEmptyList_whenNoPublicationBelongsToLibrary() {
        // Arrange
        LibraryId libraryId_1 = mock(LibraryId.class);
        LibraryId libraryId_2 = mock(LibraryId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);

        when(pbSample_1.getLibraryID()).thenReturn(libraryId_1);
        when(pbSample_2.getLibraryID()).thenReturn(libraryId_1);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.save(pbSample_1);
        repo.save(pbSample_2);

        // Act
        List<PublicationSample> result = repo.getPublicationsByLibraryID(libraryId_2);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getPublicationsByLibraryID_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange
        LibraryId libraryId = mock(LibraryId.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act
        List<PublicationSample> result = repo.getPublicationsByLibraryID(libraryId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getPublicationsByLibraryID_shouldReturnOnlyPublicationsWithMatchingLibraryId() {
        // Arrange
        LibraryId libraryId = mock(LibraryId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        PublicationSample pbSample_3 = mock(PublicationSample.class);

        when(pbSample_1.getLibraryID()).thenReturn(libraryId);
        when(pbSample_2.getLibraryID()).thenReturn(null);
        when(pbSample_3.getLibraryID()).thenReturn(mock(LibraryId.class));

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        // Act
        List<PublicationSample> result = repo.getPublicationsByLibraryID(libraryId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(pbSample_1, result.get(0));
    }

    @Test
    void getPublicationsByLibraryID_shouldReturnPublicationsWithNullLibraryId_whenArgumentIsNull() {
        // Arrange
        LibraryId libraryId = mock(LibraryId.class);

        PublicationSample pbSample_1 = mock(PublicationSample.class);
        PublicationSample pbSample_2 = mock(PublicationSample.class);
        PublicationSample pbSample_3 = mock(PublicationSample.class);

        when(pbSample_1.getLibraryID()).thenReturn(null);
        when(pbSample_2.getLibraryID()).thenReturn(libraryId);
        when(pbSample_3.getLibraryID()).thenReturn(null);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.save(pbSample_1);
        repo.save(pbSample_2);
        repo.save(pbSample_3);

        // Act
        List<PublicationSample> result = repo.getPublicationsByLibraryID(null);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pbSample_1));
        assertTrue(result.contains(pbSample_3));
        assertFalse(result.contains(pbSample_2));
    }

    // ===========================================================
    // == Tests for findPublicationSamplesByBiblioDataIds ========
    // ===========================================================

    @Test
    void findPublicationSamplesByBiblioDataIds_shouldReturnListWithMultipleIds_whenMultiplePublicationSamplesIdsMatchGivenBdIds() throws InstanceAlreadyExistsException {

        // Arrange
        BibliographicalDataID bdId_1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId_2 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId_3 = mock(BibliographicalDataID.class);

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

        when(factory.create(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample_1);
        when(factory.create(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_2);
        when(factory.create(bdId_3, userID_Double, PublicationStatus.READ, editionID_Double, Condition.LIKE_NEW))
                .thenReturn(pbSample_3);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);
        repo.createAndAdd(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);
        repo.createAndAdd(bdId_3, userID_Double, PublicationStatus.READ, editionID_Double, Condition.LIKE_NEW);

        List<BibliographicalDataID> bds = List.of(bdId_1, bdId_2);

        // Act
        List<PublicationSampleId> result = repo.findPublicationSamplesIdsByBiblioDataIds(bds);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(psId_1));
        assertTrue(result.contains(psId_2));
        assertFalse(result.contains(psId_3));
    }

    @Test
    void findPublicationSamplesByBiblioDataIds_shouldReturnListWithOneId_whenOnePublicationSampleMatchesGivenBdIdsIds() throws InstanceAlreadyExistsException {

        // Arrange
        BibliographicalDataID bdId_1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId_2 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId_3 = mock(BibliographicalDataID.class);

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

        when(factory.create(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample_1);
        when(factory.create(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_2);
        when(factory.create(bdId_3, userID_Double, PublicationStatus.READ, editionID_Double, Condition.LIKE_NEW))
                .thenReturn(pbSample_3);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);
        repo.createAndAdd(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);
        repo.createAndAdd(bdId_3, userID_Double, PublicationStatus.READ, editionID_Double, Condition.LIKE_NEW);

        List<BibliographicalDataID> bds = List.of(bdId_2);

        // Act
        List<PublicationSampleId> result = repo.findPublicationSamplesIdsByBiblioDataIds(bds);

        // Assert
        assertEquals(1, result.size());
        assertEquals(psId_2, result.get(0));
    }

    @Test
    void findPublicationSamplesByBiblioDataIds_shouldReturnEmptyList_whenNoPublicationSampleMatchesGivenBdIdsIds() throws InstanceAlreadyExistsException {

        // Arrange
        BibliographicalDataID bdId_1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId_2 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId_3 = mock(BibliographicalDataID.class);

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

        when(factory.create(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample_1);
        when(factory.create(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR))
                .thenReturn(pbSample_2);
        when(factory.create(bdId_3, userID_Double, PublicationStatus.READ, editionID_Double, Condition.LIKE_NEW))
                .thenReturn(pbSample_3);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bdId_1, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);
        repo.createAndAdd(bdId_2, userID_Double, PublicationStatus.READ, editionID_Double, Condition.POOR);
        repo.createAndAdd(bdId_3, userID_Double, PublicationStatus.READ, editionID_Double, Condition.LIKE_NEW);

        List<BibliographicalDataID> bds = List.of(mock(BibliographicalDataID.class));

        // Act
        List<PublicationSampleId> result = repo.findPublicationSamplesIdsByBiblioDataIds(bds);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findPublicationSamplesIdsByBiblioDataIds_shouldThrowException_whenBdIdsListIsNull() {

        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.findPublicationSamplesIdsByBiblioDataIds(null));
    }

    @Test
    void findAllItemsIdByBibliographicalDataId_shouldThrowException_whenListIsNull() {

        // Arrange
        IPublicationSampleRepository repo =
                new PublicationSampleRepositoryMem(factory);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findAllItemsIdByBibliographicalDataId(null));
    }

    @Test
    void findAllItemsIdByBibliographicalDataId_shouldReturnMatchingIds() {

        // Arrange
        IPublicationSampleRepository repo =
                new PublicationSampleRepositoryMem(factory);


        PublicationSample psDouble = mock(PublicationSample.class);
        PublicationSampleId psDoubleId = mock(PublicationSampleId.class);

        when(psDouble.getBibliographicalDataID()).thenReturn(bibliographicalDataID_Double);
        when(psDouble.id()).thenReturn(psDoubleId);

        repo.save(psDouble);

        // Act
        List<PublicationSampleId> result =
                repo.findAllItemsIdByBibliographicalDataId(List.of(bibliographicalDataID_Double));

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(psDoubleId));
    }

    @Test
    void findAllItemsIdByBibliographicalDataId_shouldReturnEmptyList_whenNoMatch() {

        // Arrange
        IPublicationSampleRepository repo =
                new PublicationSampleRepositoryMem(factory);

        BibliographicalDataID bdIdDouble1 = mock(BibliographicalDataID.class);

        PublicationSample psDouble = mock(PublicationSample.class);

        when(psDouble.getBibliographicalDataID()).thenReturn(bdIdDouble1);

        repo.save(psDouble);

        // Act
        List<PublicationSampleId> result =
                repo.findAllItemsIdByBibliographicalDataId(List.of(bibliographicalDataID_Double));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllItemsIdByBibliographicalDataId_shouldReturnMultipleMatches() {

        // Arrange
        IPublicationSampleRepository repo =
                new PublicationSampleRepositoryMem(factory);

        PublicationSample psDouble1 = mock(PublicationSample.class);
        PublicationSample psDouble2 = mock(PublicationSample.class);

        PublicationSampleId idDouble1 = mock(PublicationSampleId.class);
        PublicationSampleId idDouble2 = mock(PublicationSampleId.class);

        when(psDouble1.getBibliographicalDataID()).thenReturn(bibliographicalDataID_Double);
        when(psDouble2.getBibliographicalDataID()).thenReturn(bibliographicalDataID_Double);

        when(psDouble1.id()).thenReturn(idDouble1);
        when(psDouble2.id()).thenReturn(idDouble2);

        repo.save(psDouble1);
        repo.save(psDouble2);

        // Act
        List<PublicationSampleId> result =
                repo.findAllItemsIdByBibliographicalDataId(List.of(bibliographicalDataID_Double));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(idDouble1));
        assertTrue(result.contains(idDouble2));
    }


    @Test
    void shouldReturnPublicationsByCollectionId() {

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem(factory);

        PublicationSample p1 = mock(PublicationSample.class);
        PublicationSample p2 = mock(PublicationSample.class);

        CollectionOfPublicationsID id = mock(CollectionOfPublicationsID.class);

        when(p1.getCollecOfPubID()).thenReturn(id);
        when(p2.getCollecOfPubID()).thenReturn(null);

        repo.save(p1);
        repo.save(p2);

        List<PublicationSample> result = repo.getPublicationsByCollectionID(id);

        assertEquals(1, result.size());
        assertTrue(result.contains(p1));
    }

    @Test
    void shouldReturnEmptyWhenNoPublicationMatchesCollection() {

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem(factory);

        PublicationSample p = mock(PublicationSample.class);
        when(p.getCollecOfPubID()).thenReturn(null);

        repo.save(p);

        List<PublicationSample> result =
                repo.getPublicationsByCollectionID(mock(CollectionOfPublicationsID.class));

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldAssignPublicationsToCollection() {

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem(factory);

        PublicationSample p = mock(PublicationSample.class);
        PublicationSampleId id = mock(PublicationSampleId.class);
        CollectionOfPublicationsID collectionId = mock(CollectionOfPublicationsID.class);

        when(p.id()).thenReturn(id);

        repo.save(p);

        boolean result = repo.assignPublicationsToCollection(
                collectionId,
                List.of(id)
        );

        assertTrue(result);
        verify(p).assignToCollection(collectionId);
    }

    @Test
    void shouldThrowWhenPublicationNotFound() {

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem(factory);

        assertThrows(IllegalArgumentException.class,
                () -> repo.assignPublicationsToCollection(
                        mock(CollectionOfPublicationsID.class),
                        List.of(mock(PublicationSampleId.class))
                ));
    }

    @Test
    void shouldThrowWhenCollectionIdIsNull() {

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem(factory);

        assertThrows(IllegalArgumentException.class,
                () -> repo.assignPublicationsToCollection(null, List.of(mock(PublicationSampleId.class))));
    }

    @Test
    void shouldThrowWhenPublicationListIsNull() {

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem(factory);

        assertThrows(IllegalArgumentException.class,
                () -> repo.assignPublicationsToCollection(
                        mock(CollectionOfPublicationsID.class),
                        null
                ));
    }

    @Test
    void shouldThrowWhenPublicationListIsEmpty() {

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);
        PublicationSampleRepositoryMem repo = new PublicationSampleRepositoryMem(factory);

        assertThrows(IllegalArgumentException.class,
                () -> repo.assignPublicationsToCollection(
                        mock(CollectionOfPublicationsID.class),
                        new ArrayList<>()
                ));
    }




    // ===========================================================
    // =================== Helper Methods ========================
    // ===========================================================


    private static BibliographicalDataID createDoubleBibliographicalDataID(){
        BibliographicalDataID bdID = mock(BibliographicalDataID.class);
        when(bdID.getID()).thenReturn(UUID.randomUUID().toString());
        return bdID;
    }

    private static UserId createDoubleUserID(){
        UserId userID = mock(UserId.class);
        when(userID.getUsername()).thenReturn(UUID.randomUUID().toString());
        return userID;
    }

    private static EditionID createDoubleEditionID(){
        EditionID editionID = mock(EditionID.class);
        when(editionID.getID()).thenReturn(UUID.randomUUID().toString());
        return editionID;
    }
}
