package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.dto.PublicationSampleDTO_US018;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import javax.management.InstanceAlreadyExistsException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PublicationSampleRepositoryTest {

    // ===========================================================
    // ======================== Set Up ===========================
    // ===========================================================

    Listing listing1Double;
    Listing listing2Double;
    Listing listing3Double;
    PublicationSample pubSample1Double;
    PublicationSample pubSample2Double;
    PublicationSample pubSample3Double;
    PublicationSample pubSample4Double;
    BibliographicalData bd1Double;
    BibliographicalData bd2Double;
    BibliographicalData bd3Double;
    PublicationSampleRepository repo;
    Publisher publisher1Double;
    Publisher publisher2Double;

    @BeforeEach
    void setUp() {

        listing1Double = mock(Listing.class);                                           // contains pubSample1
        listing2Double = mock(Listing.class);                                           // contains pubSample2
        listing3Double = mock(Listing.class);                                           // contains pubSample3 e 4

        pubSample1Double = mock(PublicationSample.class);                               // contains bd1
        pubSample2Double = mock(PublicationSample.class);                               // contains bd2
        pubSample3Double = mock(PublicationSample.class);                               // contains bd3
        pubSample4Double = mock(PublicationSample.class);                               // contains bd1

        bd1Double = mock(BibliographicalData.class);
        bd2Double = mock(BibliographicalData.class);
        bd3Double = mock(BibliographicalData.class);

        Edition edition1Double = mock(Edition.class);
        Edition edition2Double = mock(Edition.class);

        // listings
        when(listing1Double.getItems()).thenReturn(List.of(pubSample1Double));
        when(listing2Double.getItems()).thenReturn(List.of(pubSample2Double));
        when(listing3Double.getItems()).thenReturn(List.of(pubSample3Double, pubSample4Double));

        when(listing1Double.getItems()).thenReturn(List.of(pubSample1Double));
        when(listing2Double.getItems()).thenReturn(List.of(pubSample2Double));
        when(listing3Double.getItems()).thenReturn(List.of(pubSample3Double, pubSample4Double));

        // pubSample1
        when(pubSample1Double.getBibliographicalData()).thenReturn(bd1Double);

        // pubSample2
        when(pubSample2Double.getBibliographicalData()).thenReturn(bd2Double);

        // pubSample3
        when(pubSample3Double.getBibliographicalData()).thenReturn(bd3Double);

        // pubSample4
        when(pubSample4Double.getBibliographicalData()).thenReturn(bd1Double);

        // Repository
        repo = new PublicationSampleRepository();

        // Publisher
        publisher1Double = mock(Publisher.class);
        publisher2Double = mock(Publisher.class);

        when(pubSample1Double.getEdition()).thenReturn(edition1Double);
        when(pubSample2Double.getEdition()).thenReturn(edition2Double);
        when(pubSample3Double.getEdition()).thenReturn(edition1Double);
        when(pubSample4Double.getEdition()).thenReturn(edition2Double);

        when(edition1Double.getPublisher()).thenReturn(publisher1Double);
        when(edition2Double.getPublisher()).thenReturn(publisher2Double);
    }


    // ===========================================================
    // ===== Tests for  findItemsByListingAndPublication =========
    // ===========================================================

    @Test
    void findItemsByListingAndPublication_ShouldReturnSamplesMatchingPublication() {
        // Arrange
        List<Listing> listOfListings = List.of(listing1Double, listing2Double, listing3Double);

        // Act
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);

        // Assert
        assertEquals(2, itemsList.size());
        assertEquals(pubSample1Double, itemsList.get(0));
        assertEquals(pubSample4Double, itemsList.get(1));
    }

    @Test
    void findItemsByListingAndPublication_ShouldReturnEmptyListWhenNoMatches() {
        // Arrange
        List<Listing> listOfListings = List.of(listing1Double, listing3Double);

        // Act
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd2Double);

        assertTrue(itemsList.isEmpty());
    }

    @Test
    void findItemsByListingAndPublication_ShouldReturnEmptyListWhenListingsEmpty() {
        // Arrange
        List<Listing> listOfListings = List.of();

        // Act
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);

        // Assert
        assertTrue(itemsList.isEmpty());
    }

    @Test
    void findItemsByListingAndPublication_ShouldReturnImmutableList() {
        // Arrange
        List<Listing> listOfListings = List.of(listing1Double);

        // Act
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> itemsList.add(pubSample1Double));
    }

    // Test for listings with more than 1 item (listing3)
    @Test
    void findItemsByListingAndPublication_ShouldHandleMultipleSamplesInListing() {
        // Arrange
        List<Listing> listOfListings = List.of(listing3Double);

        // Act
        List<PublicationSample> itemsList = repo.findItemsByListingAndPublication(listOfListings, bd1Double);

        assertEquals(1, itemsList.size());
        assertTrue(itemsList.contains(pubSample4Double));
    }

    // ===========================================================
    // ===== Tests for  findItemsByListingAndPublisher ===========
    // ===========================================================

    @Test
    void findItemsByListingAndPublisher_ShouldReturnOnlySamplesFromGivenPublisher() {
        // Arrange
        List<Listing> listings = List.of(listing1Double, listing2Double, listing3Double);

        // Act
        List<PublicationSample> result = repo.findItemsByListingAndPublisher(listings, publisher1Double);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pubSample1Double));
        assertTrue(result.contains(pubSample3Double));
    }

    @Test
    void findItemsByListingAndPublisher_ShouldReturnEmptyListWhenNoSamplesMatchPublisher() {
        // Arrange
        Publisher publisherDouble = mock(Publisher.class);                  // new Publisher

        List<Listing> listings = List.of(listing1Double, listing2Double, listing3Double);

        // Act
        List<PublicationSample> result = repo.findItemsByListingAndPublisher(listings, publisherDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findItemsByListingAndPublisher_ShouldHandleEmptyListingsList() {
        // Arrange
        List<Listing> listings = List.of();

        // Act
        List<PublicationSample> result =  repo.findItemsByListingAndPublisher(List.of(), publisher1Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findItemsByListingAndPublisher_ShouldReturnImmutableList() {
        // Arrange
        List<Listing> listings = List.of(listing1Double);

        // Act
        List<PublicationSample> result = repo.findItemsByListingAndPublisher(listings, publisher1Double);

        // Assert
        assertThrows(UnsupportedOperationException.class,
                () -> result.add(pubSample2Double));
    }

    @Test
    void findItemsByListingAndPublisher_ShouldCallGetItemsOnEachListing() {
        // Arrange
        List<Listing> listings = List.of(listing1Double, listing2Double, listing3Double);

        // Act
        repo.findItemsByListingAndPublisher(listings, publisher1Double);

        // Assert
        verify(listing1Double).getItems();
        verify(listing2Double).getItems();
        verify(listing3Double).getItems();
    }

    @Test
    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenListingsIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findItemsByListingAndPublisher(null, publisher1Double));
    }

    @Test
    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenPublisherIsNull() {
        // Arrange
        when(listing1Double.getItems()).thenReturn(List.of());
        when(listing2Double.getItems()).thenReturn(List.of());

        List<Listing> listings = List.of(listing1Double, listing2Double);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findItemsByListingAndPublisher(listings, null));
    }

    @Test
    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenListingsContainNull() {
        // Arrange
        List<Listing> listings = new ArrayList<>();
        listings.add(listing1Double);
        listings.add(null);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findItemsByListingAndPublisher(listings, publisher1Double));
    }

    @Test
    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenListingItemsIsNull() {
        // Arrange
        when(listing1Double.getItems()).thenReturn(null);

        List<Listing> listings = List.of(listing1Double);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> repo.findItemsByListingAndPublisher(listings, publisher1Double));
    }

    @Test
    void findItemsByListingAndPublisher_ShouldThrowExceptionWhenListingContainsNullItem() {
        // Arrange
        List<PublicationSample> items = new ArrayList<>();
        items.add(pubSample1Double);
        items.add(null);

        when(listing1Double.getItems()).thenReturn(items);

        List<Listing> listings = List.of(listing1Double);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findItemsByListingAndPublisher(listings, publisher1Double));
    }

    // US018 STARTS MOCK TESTS -------------------------------------------------------

    /**
     * Ensures exception is thrown when library is null.
     */
    @Test
    void shouldThrowExceptionWhenLibraryIsNull() {

        // ARRANGE
        PublicationSampleRepository sut = new PublicationSampleRepository();
        PublicationSampleDTO_US018 dto = mock(PublicationSampleDTO_US018.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.PublicationSampleDtoToDomain(null, dto));
    }

    /**
     * Ensures exception is thrown when DTO is null.
     */
    @Test
    void shouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        PublicationSampleRepository sut = new PublicationSampleRepository();
        Library library = mock(Library.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.PublicationSampleDtoToDomain(library, null));
    }

    /**
     * Ensures correct PublicationSample is returned
     * when DTO matches publication in user's library.
     */
    @Test
    void shouldReturnMatchingPublicationSample() {

        // ARRANGE
        PublicationSampleRepository sut = new PublicationSampleRepository();

        Library library = mock(Library.class);
        PublicationSampleDTO_US018 dto = mock(PublicationSampleDTO_US018.class);

        PublicationSample publication = mock(PublicationSample.class);
        BibliographicalData bd = mock(BibliographicalData.class);
        Edition edition = mock(Edition.class);
        User owner = mock(User.class);

        when(library.getAllPublications()).thenReturn(List.of(publication));

        when(publication.getBibliographicalData()).thenReturn(bd);
        when(publication.getEdition()).thenReturn(edition);
        when(publication.getOwner()).thenReturn(owner);

        when(bd.getTitle()).thenReturn("Clean Code");
        when(edition.getEditionNumber()).thenReturn("1");
        when(owner.getUserName()).thenReturn("john");

        when(dto.getTitle()).thenReturn("Clean Code");
        when(dto.getEditionNumber()).thenReturn("1");
        when(dto.getOwnerUsername()).thenReturn("john");

        // ACT
        PublicationSample result =
                sut.PublicationSampleDtoToDomain(library, dto);

        // ASSERT
        assertEquals(publication, result);
    }

    /**
     * Ensures exception is thrown when
     * publication does not exist in library.
     */
    @Test
    void shouldThrowExceptionWhenPublicationNotFound() {

        // ARRANGE
        PublicationSampleRepository sut = new PublicationSampleRepository();

        Library library = mock(Library.class);
        PublicationSampleDTO_US018 dto = mock(PublicationSampleDTO_US018.class);

        PublicationSample publication = mock(PublicationSample.class);
        BibliographicalData bd = mock(BibliographicalData.class);
        Edition edition = mock(Edition.class);
        User owner = mock(User.class);

        when(library.getAllPublications()).thenReturn(List.of(publication));

        when(publication.getBibliographicalData()).thenReturn(bd);
        when(publication.getEdition()).thenReturn(edition);
        when(publication.getOwner()).thenReturn(owner);

        when(bd.getTitle()).thenReturn("Different");
        when(edition.getEditionNumber()).thenReturn("2");
        when(owner.getUserName()).thenReturn("mary");

        when(dto.getTitle()).thenReturn("Clean Code");
        when(dto.getEditionNumber()).thenReturn("1");
        when(dto.getOwnerUsername()).thenReturn("john");

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.PublicationSampleDtoToDomain(library, dto));
    }

    // ENDS --------------------------------------------------------------------------

    //============================================================
    // TESTING CREATE AND ADD WITH FACTORY
    @Test
    void shouldReturnValidNewPublicationSampleWithFactory () throws InstanceAlreadyExistsException {

        // Arrange
        PublicationSample publicationSampleDouble = mock(PublicationSample.class);
        BibliographicalData bdDouble = mock(BibliographicalData.class);
        User ownerDouble = mock(User.class);
        PublicationStatus status = PublicationStatus.READ;
        Edition eDouble = mock(Edition.class);
        Condition condition = Condition.LIKE_NEW;

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);
        PublicationSampleRepository publicationSampleRepository = new PublicationSampleRepository(factory);


        when(factory.create(bdDouble, ownerDouble, status, eDouble, condition)).thenReturn(publicationSampleDouble);

        // Act
        PublicationSample result = publicationSampleRepository.createAndAdd(bdDouble, ownerDouble, status, eDouble, condition);

        // Assert
        verify(factory).create(bdDouble, ownerDouble, status, eDouble, condition);
        assertEquals(publicationSampleDouble, result);
        assertTrue(publicationSampleRepository.getAllPublications().contains(publicationSampleDouble));
        assertEquals(1, publicationSampleRepository.getAllPublications().size());
    }

    @Test
    void createAndAdd_shouldThrowException_whenBibliographicalDataIsNull() {
        // Arrange

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);

        User user = mock(User.class);
        Edition edition = mock(Edition.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        null,
                        user,
                        PublicationStatus.READ,
                        edition,
                        Condition.NEW
                )
        );

        verify(factory, never()).create(any(), any(), any(), any(), any());
    }

    @Test
    void createAndAdd_shouldThrowException_whenUserIsNull() {
        // Arrange

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);

        BibliographicalData bd = mock(BibliographicalData.class);
        Edition edition = mock(Edition.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        bd,
                        null,
                        PublicationStatus.READ,
                        edition,
                        Condition.NEW
                )
        );

        verify(factory, never()).create(any(), any(), any(), any(), any());
    }

    @Test
    void createAndAdd_shouldThrowException_whenStatusIsNull() {
        // Arrange

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);

        BibliographicalData bd = mock(BibliographicalData.class);
        User user = mock(User.class);
        Edition edition = mock(Edition.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        bd,
                        user,
                        null,
                        edition,
                        Condition.NEW
                )
        );

        verify(factory, never()).create(any(), any(), any(), any(), any());
    }

    @Test
    void createAndAdd_shouldThrowException_whenEditionIsNull() {
        // Arrange

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);

        BibliographicalData bd = mock(BibliographicalData.class);
        User user = mock(User.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        bd,
                        user,
                        PublicationStatus.READ,
                        null,
                        Condition.NEW
                )
        );

        verify(factory, never()).create(any(), any(), any(), any(), any());
    }

    @Test
    void createAndAdd_shouldThrowException_whenConditionIsNull() {
        // Arrange

        PublicationSampleFactory factory = mock(PublicationSampleFactory.class);

        BibliographicalData bd = mock(BibliographicalData.class);
        User user = mock(User.class);
        Edition edition = mock(Edition.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        bd,
                        user,
                        PublicationStatus.READ,
                        edition,
                        null
                )
        );

        verify(factory, never()).create(any(), any(), any(), any(), any());
    }






}