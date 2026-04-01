package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PublicationSampleID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.PublicationSampleDTO_US016;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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
    // ========== Tests for findById =============================
    // ===========================================================

    @Test
    void findById_shouldThrowException_whenIdIsNull() {
        // Arrange
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findById(null));
    }

    @Test
    void findById_shouldReturnPublicationSample_whenIdExists() throws InstanceAlreadyExistsException {
        // Arrange
        PublicationSampleID id = mock(PublicationSampleID.class);
        PublicationSample pbSample = mock(PublicationSample.class);
        when(pbSample.id()).thenReturn(id);
        when(factory.create(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW))
                .thenReturn(pbSample);

        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
        repo.createAndAdd(bibliographicalDataID_Double, userID_Double, PublicationStatus.READ, editionID_Double, Condition.NEW);

        // Act
        Optional<PublicationSample> result = repo.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertSame(pbSample, result.get());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() {
        // Arrange
        PublicationSampleID id = mock(PublicationSampleID.class);
        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);

        // Act
        Optional<PublicationSample> result = repo.findById(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldReturnCorrectSample_whenMultipleSamplesExist() throws InstanceAlreadyExistsException {
        // Arrange
        PublicationSampleID id_1 = mock(PublicationSampleID.class);
        PublicationSampleID id_2 = mock(PublicationSampleID.class);

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
        Optional<PublicationSample> result = repo.findById(id_2);

        // Assert
        assertTrue(result.isPresent());
        assertSame(pbSample_2, result.get());
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
    // ===== Tests for getListOfDifferentPublications ============
    // ===========================================================

//    @Test
//    void getListOfDifferentPublications_whenLibraryContainsSome_returnsOnlyMissingOnes() {
//        PublicationSample p1 = mock(PublicationSample.class);
//        PublicationSample p2 = mock(PublicationSample.class);
//        PublicationSample p3 = mock(PublicationSample.class);
//
//        // repo: p1, p2, p3
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        repo.add(p1);
//        repo.add(p2);
//        repo.add(p3);
//
//        // library tem p2
//        List<PublicationSample> library = List.of(p2);
//
//        List<PublicationSample> result = repo.getListOfDifferentPublications(library);
//
//        // deve devolver p1 e p3
//        assertEquals(2, result.size());
//        assertTrue(result.contains(p1));
//        assertTrue(result.contains(p3));
//        assertFalse(result.contains(p2));
//    }
//
//    @Test
//    void getListOfDifferentPublications_whenLibraryContainsAll_returnsEmpty() {
//        PublicationSample p1 = mock(PublicationSample.class);
//        PublicationSample p2 = mock(PublicationSample.class);
//
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        repo.add(p1);
//        repo.add(p2);
//
//        List<PublicationSample> result = repo.getListOfDifferentPublications(List.of(p1, p2));
//
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void getListOfDifferentPublications_whenLibraryContainsNone_returnsAllRepoPublications() {
//        PublicationSample p1 = mock(PublicationSample.class);
//        PublicationSample p2 = mock(PublicationSample.class);
//
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        repo.add(p1);
//        repo.add(p2);
//
//        List<PublicationSample> result = repo.getListOfDifferentPublications(List.of());
//
//        assertEquals(2, result.size());
//        assertEquals(List.of(p1, p2), result); // mesma ordem do repo
//    }
//
//    @Test
//    void getListOfDifferentPublications_returnsImmutableCopy() {
//        PublicationSample p1 = mock(PublicationSample.class);
//
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        repo.add(p1);
//
//        List<PublicationSample> result = repo.getListOfDifferentPublications(List.of());
//
//        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(PublicationSample.class)));
//    }


    // ===========================================================
    // ===== Tests for findItemsByListingAndPublication ==========
    // ===========================================================

//    @Test
//    void findItemsByListingAndPublication_ShouldReturnSamplesMatchingPublication() {
//        // Arrange
//        List<Listing> listOfListings = List.of(listing1Double, listing2Double, listing3Double);
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//
//        // Act
//        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);
//
//        // Assert
//        assertEquals(2, itemsList.size());
//        assertEquals(pubSample1Double, itemsList.get(0));
//        assertEquals(pubSample4Double, itemsList.get(1));
//    }

//    @Test
//    void findItemsByListingAndPublication_ShouldReturnEmptyListWhenNoMatches() {
//        // Arrange
//        List<Listing> listOfListings = List.of(listing1Double, listing3Double);
//
//        // Act
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd2Double);
//
//        assertTrue(itemsList.isEmpty());
//    }

//    @Test
//    void findItemsByListingAndPublication_ShouldReturnEmptyListWhenListingsEmpty() {
//        // Arrange
//        List<Listing> listOfListings = List.of();
//
//        // Act
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);
//
//        // Assert
//        assertTrue(itemsList.isEmpty());
//    }

//    @Test
//    void findItemsByListingAndPublication_ShouldReturnImmutableList() {
//        // Arrange
//        List<Listing> listOfListings = List.of(listing1Double);
//
//        // Act
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);
//
//        // Assert
//        assertThrows(UnsupportedOperationException.class, () -> itemsList.add(pubSample1Double));
//    }

//    @Test
//    void findItemsByListingAndPublication_ShouldHandleMultipleSamplesInListing() {
//        // Arrange
//        List<Listing> listOfListings = List.of(listing3Double);
//
//        // Act
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);
//
//        assertEquals(1, itemsList.size());
//        assertTrue(itemsList.contains(pubSample4Double));
//    }


    // ===========================================================
    // ===== Tests for findItemsByListingAndPublisher ============
    // ===========================================================

//    @Test
//    void findItemsByListingAndPublisher_ShouldReturnOnlySamplesFromGivenPublisher() {
//        // Arrange
//        List<Listing> listings = List.of(listing1Double, listing2Double, listing3Double);
//
//        // Act
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        List<PublicationSample> result = repo.findItemsByListingAndPublisher(listings, publisher1Double);
//
//        // Assert
//        assertEquals(2, result.size());
//        assertTrue(result.contains(pubSample1Double));
//        assertTrue(result.contains(pubSample3Double));
//    }

//    @Test
//    void findItemsByListingAndPublisher_ShouldReturnEmptyListWhenNoSamplesMatchPublisher() {
//        // Arrange
//        Publisher publisherDouble = mock(Publisher.class);                  // new Publisher
//
//        List<Listing> listings = List.of(listing1Double, listing2Double, listing3Double);
//
//        // Act
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        List<PublicationSample> result = repo.findItemsByListingAndPublisher(listings, publisherDouble);
//
//        // Assert
//        assertTrue(result.isEmpty());
//    }

//    @Test
//    void findItemsByListingAndPublisher_ShouldHandleEmptyListingsList() {
//        // Arrange
//        List<Listing> listings = List.of();
//
//        // Act
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        List<PublicationSample> result =  repo.findItemsByListingAndPublisher(List.of(), publisher1Double);
//
//        // Assert
//        assertTrue(result.isEmpty());
//    }

//    @Test
//    void findItemsByListingAndPublisher_ShouldReturnImmutableList() {
//        // Arrange
//        List<Listing> listings = List.of(listing1Double);
//
//        // Act
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        List<PublicationSample> result = repo.findItemsByListingAndPublisher(listings, publisher1Double);
//
//        // Assert
//        assertThrows(UnsupportedOperationException.class,
//                () -> result.add(pubSample2Double));
//    }

//    @Test
//    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenListingsIsNull() {
//        //Arrange
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> repo.findItemsByListingAndPublisher(null, publisher1Double));
//    }

//    @Test
//    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenPublisherIsNull() {
//        // Arrange
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        when(listing1Double.getItems()).thenReturn(List.of());
//        when(listing2Double.getItems()).thenReturn(List.of());
//
//        List<Listing> listings = List.of(listing1Double, listing2Double);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> repo.findItemsByListingAndPublisher(listings, null));
//    }

//    @Test
//    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenListingsContainNull() {
//        // Arrange
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        List<Listing> listings = new ArrayList<>();
//        listings.add(listing1Double);
//        listings.add(null);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> repo.findItemsByListingAndPublisher(listings, publisher1Double));
//    }

//    @Test
//    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenListingItemsIsNull() {
//        // Arrange
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        when(listing1Double.getItems()).thenReturn(null);
//
//        List<Listing> listings = List.of(listing1Double);
//
//        // Act + Assert
//        assertThrows(NullPointerException.class,
//                () -> repo.findItemsByListingAndPublisher(listings, publisher1Double));
//    }

//    @Test
//    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenListingContainsNullItem() {
//        // Arrange
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        List<PublicationSample> items = new ArrayList<>();
//        items.add(pubSample1Double);
//        items.add(null);
//
//        when(listing1Double.getItems()).thenReturn(items);
//
//        List<Listing> listings = List.of(listing1Double);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> repo.findItemsByListingAndPublisher(listings, publisher1Double));
//    }


    // ===========================================================
    // ===== Tests for getPublicationsByDTO ======================
    // ===========================================================

//    @Test
//    void getPublicationByDTO_shouldThrow_whenDTOList_Empty() {
//
//        // arrange
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        List<PublicationSampleDTO_US016> pubsDTOList = new ArrayList<>();
//
//        // act + assert
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repo.getPublicationsByDTO(pubsDTOList));
//
//        assertEquals("list of PublicationSampleDTO cannot be null or empty", exception.getMessage());
//    }
//
//    @Test
//    void getPublicationByDTO_shouldThrow_whenDTOList_Null() {
//
//        //Arrange
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//
//        // act + assert
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repo.getPublicationsByDTO(null));
//
//        assertEquals("list of PublicationSampleDTO cannot be null or empty", exception.getMessage());
//    }
//
//    @Test
//    void getPublicationByDTO_shouldReturn_EmptyList_whenNoPublicationSampleMatchesDTO() {
//
//        // arrange
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        List<PublicationSample> matchedList = List.of();
//
//
//        PublicationSampleDTO_US016 dtoDouble1 = mock(PublicationSampleDTO_US016.class);
//        PublicationSampleDTO_US016 dtoDouble2 = mock(PublicationSampleDTO_US016.class);
//        List<PublicationSampleDTO_US016> dtoList = List.of(dtoDouble1, dtoDouble2);
//
//        // act
//        List<PublicationSample> result = repo.getPublicationsByDTO(dtoList);
//
//        // assert
//        assertEquals(0, result.size());
//
//    }

//    @Test
//    void getPublicationsByDTO_allFieldsMatch_returnsPublication() {
//        //Arrange
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//
//        PublicationSample ps = mockPublicationSample(
//                "Title",
//                List.of("Author1"),
//                Locale.ENGLISH,
//                List.of("Genre1"),
//                "owner",
//                "New"
//        );
//        repo.add(ps);
//
//        PublicationSampleDTO_US016 dto = mockDto(
//                "Title",
//                List.of("Author1"),
//                Locale.ENGLISH,
//                List.of("Genre1"),
//                "owner",
//                "New"
//        );
//
//        //Act
//        List<PublicationSample> result = repo.getPublicationsByDTO(List.of(dto));
//
//        //Assert
//        assertEquals(1, result.size());
//        assertSame(ps, result.get(0));
//    }
//
//    @Test
//    void getPublicationsByDTO_authorOrderDifferent_returnsEmpty_becauseListEqualsIsOrderSensitive() {
//        //Arrange
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        PublicationSample ps = mockPublicationSample(
//                "Title",
//                List.of("Author1", "Author2"),
//                Locale.ENGLISH,
//                List.of("Genre1"),
//                "owner",
//                "New"
//        );
//        repo.add(ps);
//
//        PublicationSampleDTO_US016 dto = mockDto(
//                "Title",
//                List.of("Author2", "Author1"),
//                Locale.ENGLISH,
//                List.of("Genre1"),
//                "owner",
//                "New"
//        );
//
//        //Act
//        List<PublicationSample> result = repo.getPublicationsByDTO(List.of(dto));
//
//        //Arrange
//        assertTrue(result.isEmpty());
//    }


    // ===========================================================
    // ===== Tests for US018 - PublicationSampleDtoToDomain ======
    // ===========================================================

    /**
     * Ensures exception is thrown when library is null.
     */
//    @Test
//    void shouldThrowExceptionWhenLibraryIsNull() {
//
//        // ARRANGE
//        IPublicationSampleRepository sut = new PublicationSampleRepositoryMem(factory);
//        PublicationSampleDTO_US018 dto = mock(PublicationSampleDTO_US018.class);
//
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class,
//                () -> sut.PublicationSampleDtoToDomain(null, dto));
//    }

    /**
     * Ensures exception is thrown when DTO is null.
     */
//    @Test
//    void shouldThrowExceptionWhenDtoIsNull() {
//
//        // ARRANGE
//        IPublicationSampleRepository sut = new PublicationSampleRepositoryMem(factory);
//        Library library = mock(Library.class);
//
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class,
//                () -> sut.PublicationSampleDtoToDomain(library, null));
//    }

    /**
     * Ensures correct PublicationSample is returned
     * when DTO matches publication in user's library.
     */
//    @Test
//    void shouldReturnMatchingPublicationSample() {
//
//        // ARRANGE
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        Library library = mock(Library.class);
//        PublicationSampleDTO_US018 dto = mock(PublicationSampleDTO_US018.class);
//
//        when(library.getAllPublications()).thenReturn(List.of(pubSample1Double));
//
//        when(pubSample1Double.getBibliographicalData()).thenReturn(bd1Double);
//
//        when(bd1Double.getTitle()).thenReturn("Clean Code");
//
//        Edition editionDouble = mock(Edition.class);
//        when(pubSample1Double.getEdition()).thenReturn(editionDouble);
//        when(editionDouble.getEditionNumber()).thenReturn("1");
//
//        User ownerDouble = mock(User.class);
//        when(pubSample1Double.getOwnerId()).thenReturn(ownerDouble);
//        when(ownerDouble.getUserName()).thenReturn("john");
//
//        when(dto.getTitle()).thenReturn("Clean Code");
//        when(dto.getEditionNumber()).thenReturn("1");
//        when(dto.getOwnerUsername()).thenReturn("john");
//
//        // ACT
//        PublicationSample result =
//                repo.PublicationSampleDtoToDomain(library, dto);
//
//        // ASSERT
//        assertEquals(pubSample1Double, result);
//    }

    /**
     * Ensures exception is thrown when
     * publication does not exist in library.
     */
//    @Test
//    void shouldThrowExceptionWhenPublicationNotFound() {
//
//        // ARRANGE
//        IPublicationSampleRepository repo = new PublicationSampleRepositoryMem(factory);
//        Library library = mock(Library.class);
//        PublicationSampleDTO_US018 dto = mock(PublicationSampleDTO_US018.class);
//
//        when(library.getAllPublications()).thenReturn(List.of(pubSample1Double));
//
//        when(bd1Double.getTitle()).thenReturn("Different");
//        when(pubSample1Double.getEdition().getEditionNumber()).thenReturn("2");
//
//        User ownerDouble = mock(User.class);
//        when(pubSample1Double.getOwnerId()).thenReturn(ownerDouble);
//        when(ownerDouble.getUserName()).thenReturn("mary");
//
//        when(dto.getTitle()).thenReturn("Clean Code");
//        when(dto.getEditionNumber()).thenReturn("1");
//        when(dto.getOwnerUsername()).thenReturn("john");
//
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class,
//                () -> repo.PublicationSampleDtoToDomain(library, dto));
//    }


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



    private static PublicationSample mockPublicationSample(
            String title,
            List<String> authorNames,
            Locale language,
            List<String> genreTypes,
            String ownerName,
            String conditionDescription
    ) {
        PublicationSample ps = mock(PublicationSample.class);

        BibliographicalData bdDouble = mock(BibliographicalData.class);
//        when(ps.getBibliographicalData()).thenReturn(bdDouble);

        when(bdDouble.getTitle()).thenReturn(title);
        when(bdDouble.getOriginalLanguage()).thenReturn(language);

        List<Author> authors = authorNames.stream().map(n -> {
            Author authorDouble = mock(Author.class);
            when(authorDouble.getName()).thenReturn(n);
            return authorDouble;
        }).toList();
        when(bdDouble.getAuthors()).thenReturn(authors);

        // Genres -> genreType
        List<Genre> genres = genreTypes.stream().map(gt -> {
            Genre g = mock(Genre.class);
            when(g.getGenreType()).thenReturn(gt);
            return g;
        }).toList();
        when(bdDouble.getGenres()).thenReturn(genres);

        // owner username
        User ownerDouble = mock(User.class);
        when(ownerDouble.getUserName()).thenReturn(ownerName);
//        when(ps.getOwnerId()).thenReturn(ownerDouble);

        // condition description
        Condition conditionDouble = mock(Condition.class);
        when(conditionDouble.getDescription()).thenReturn(conditionDescription);
        when(ps.getCondition()).thenReturn(conditionDouble);

        return ps;
    }

    private static PublicationSampleDTO_US016 mockDto(
            String title,
            List<String> authors,
            Locale language,
            List<String> genreTypes,
            String ownerName,
            String condition
    ) {
        PublicationSampleDTO_US016 dtoDouble = mock(PublicationSampleDTO_US016.class);
        when(dtoDouble.getTitle()).thenReturn(title);
        when(dtoDouble.getAuthorName()).thenReturn(authors);
        when(dtoDouble.getLanguage()).thenReturn(language);          // Locale
        when(dtoDouble.getGenreType()).thenReturn(genreTypes);
        when(dtoDouble.getOwnerName()).thenReturn(ownerName);
        when(dtoDouble.getCondition()).thenReturn(condition);
        return dtoDouble;
    }
}