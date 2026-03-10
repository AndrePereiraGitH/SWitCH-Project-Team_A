package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.dto.PublicationSampleDTO_US028;
import TOPSECRET.dto.PublisherDTO_US028;
import TOPSECRET.repository.ListingRepository;
import TOPSECRET.repository.PublicationSampleRepository;
import TOPSECRET.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherControllerTest {

    // ===============================================
    // ================== Set Up =====================
    // ===============================================

    PublisherRepository publisherRepoDouble;
    ListingRepository listingRepoDouble;
    PublicationSampleRepository pubSampleRepoDouble;

    US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller;

    @BeforeEach
    void setUp() {
        publisherRepoDouble = mock(PublisherRepository.class);
        listingRepoDouble = mock(ListingRepository.class);
        pubSampleRepoDouble = mock(PublicationSampleRepository.class);
    }

    // ===============================================
    // ================== Tests ======================
    // ===============================================

    // ======= getAllPublishers ========================================================================================

    @Test
    void getAllPublishers_ShouldCallRepositoryOnce() {
        // Arrange
        controller = new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act
        controller.getAllPublishers();

        // Assert
        verify(publisherRepoDouble, times(1)).getAllPublishers();
    }

    @Test
    void getAllPublishers_ShouldConvertPublishersToDTOs() {
        // Arrange
        Publisher pub1 = mock(Publisher.class);
        Publisher pub2 = mock(Publisher.class);
        when(publisherRepoDouble.getAllPublishers()).thenReturn(List.of(pub1, pub2));

        controller = new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act
        List<PublisherDTO_US028> result = controller.getAllPublishers();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getAllPublishers_ShouldReturnEmptyListWhenRepositoryReturnsEmpty() {
        // Arrange
        when(publisherRepoDouble.getAllPublishers()).thenReturn(List.of());

        controller = new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act
        List<PublisherDTO_US028> result = controller.getAllPublishers();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublishers_ShouldPropagateExceptionFromRepository() {
        // Arrange
        when(publisherRepoDouble.getAllPublishers()).thenThrow(new RuntimeException("DB error"));

        controller = new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act + Assert
        assertThrows(RuntimeException.class, () -> controller.getAllPublishers());
    }

    // ======= findListOfItemsOnAuctionByPublisher ==================================================================

    @Test
    void findListOfItemsOnAuctionByPublisher_ShouldCalllistingRepoDouble() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        when(listingRepoDouble.findAllAuctionListings()).thenReturn(List.of());
        when(pubSampleRepoDouble.findItemsByListingAndPublisher(any(), any())).thenReturn(List.of());

        controller = new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act
        controller.findListOfItemsOnAuctionByPublisher(publisher);

        // Assert
        verify(listingRepoDouble, times(1)).findAllAuctionListings();
    }

    @Test
    void findListOfItemsOnAuctionByPublisher_ShouldCallPubSampleRepoWithCorrectArguments() {
        // Arrange
        Publisher publisher = mock(Publisher.class);

        Listing l1 = mock(Listing.class);
        Listing l2 = mock(Listing.class);
        List<Listing> listings = List.of(l1, l2);

        when(listingRepoDouble.findAllAuctionListings()).thenReturn(listings);
        when(pubSampleRepoDouble.findItemsByListingAndPublisher(listings, publisher))
                .thenReturn(List.of());

        controller = new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act
        controller.findListOfItemsOnAuctionByPublisher(publisher);

        // Assert
        verify(pubSampleRepoDouble).findItemsByListingAndPublisher(listings, publisher);
    }
    @Test
    void findListOfItemsOnAuctionByPublisher_ShouldReturnEmptyListWhenNoItemsFound() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        when(listingRepoDouble.findAllAuctionListings()).thenReturn(List.of());
        when(pubSampleRepoDouble.findItemsByListingAndPublisher(any(), any()))
                .thenReturn(List.of());

        controller = new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act
        List<PublicationSampleDTO_US028> result =
                controller.findListOfItemsOnAuctionByPublisher(publisher);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findListOfItemsOnAuctionByPublisher_ShouldPropagateExceptionFromListingRepoDouble() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        when(listingRepoDouble.findAllAuctionListings())
                .thenThrow(new RuntimeException("DB error"));

        controller = new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnAuctionByPublisher(publisher));
    }

    @Test
    void findListOfItemsOnAuctionByPublisher_ShouldPropagateExceptionFromPubSampleRepo() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        when(listingRepoDouble.findAllAuctionListings()).thenReturn(List.of());
        when(pubSampleRepoDouble.findItemsByListingAndPublisher(any(), any()))
                .thenThrow(new RuntimeException("DB error"));

        controller = new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnAuctionByPublisher(publisher));
    }

    @Test
    void findListOfItemsOnAuctionByPublisher_ShouldCallMethodsInCorrectOrder() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        when(listingRepoDouble.findAllAuctionListings()).thenReturn(List.of());
        when(pubSampleRepoDouble.findItemsByListingAndPublisher(any(), any()))
                .thenReturn(List.of());

        controller = new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act
        controller.findListOfItemsOnAuctionByPublisher(publisher);

        // Assert
        InOrder order = inOrder(listingRepoDouble, pubSampleRepoDouble);
        order.verify(listingRepoDouble).findAllAuctionListings();
        order.verify(pubSampleRepoDouble).findItemsByListingAndPublisher(any(), eq(publisher));
    }

    @Test
    void findListOfItemsOnAuctionByPublisher_ShouldReturnConvertedDTOs() {
        // Arrange
        Publisher publisher = mock(Publisher.class);

        Listing listing = mock(Listing.class);
        PublicationSample sample = mock(PublicationSample.class);

        when(listingRepoDouble.findAllAuctionListings()).thenReturn(List.of(listing));
        when(pubSampleRepoDouble.findItemsByListingAndPublisher(List.of(listing), publisher))
                .thenReturn(List.of(sample));

        BibliographicalData bd = mock(BibliographicalData.class);
        when(sample.getBibliographicalData()).thenReturn(bd);
        when(bd.getTitle()).thenReturn("title1");
        when(bd.getAuthors()).thenReturn(List.of());
        when(bd.getGenres()).thenReturn(List.of());

        User owner = mock(User.class);
        when(sample.getOwner()).thenReturn(owner);
        when(owner.getUserName()).thenReturn("u");

        PublicationStatus status = mock(PublicationStatus.class);
        when(sample.getPublicationStatus()).thenReturn(status);
        when(status.toString()).thenReturn("S");

        Edition edition = mock(Edition.class);
        when(sample.getEdition()).thenReturn(edition);
        when(edition.getEditionNumber()).thenReturn("1");
        Code code = mock(Code.class);
        when(edition.getCode()).thenReturn(code);
        when(code.toString()).thenReturn("C");
        PublicationType type = mock(PublicationType.class);
        when(edition.getPublicationType()).thenReturn(type);
        MediaFormat format = mock(MediaFormat.class);
        when(type.getMediaFormat()).thenReturn(format);
        when(format.toString()).thenReturn("F");
        when(edition.getEditionLanguage()).thenReturn(Locale.US);
        Publisher pub = mock(Publisher.class);
        when(edition.getPublisher()).thenReturn(pub);
        when(pub.getName()).thenReturn("P");

        Condition cond = mock(Condition.class);
        when(sample.getCondition()).thenReturn(cond);
        when(cond.toString()).thenReturn("NEW");

        controller = new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act
        List<PublicationSampleDTO_US028> result =
                controller.findListOfItemsOnAuctionByPublisher(publisher);

        // Assert
        assertEquals(1, result.size());
    }
}
