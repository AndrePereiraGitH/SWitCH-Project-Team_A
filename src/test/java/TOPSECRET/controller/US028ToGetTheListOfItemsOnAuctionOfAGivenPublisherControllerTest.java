package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.dto.PublicationSampleDTO_US028;
import TOPSECRET.dto.PublisherDTO_US028;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.repository.IPublisherRepository;
import TOPSECRET.repository.IListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherControllerTest {

    // ===============================================
    // ================== Set Up =====================
    // ===============================================

    IPublisherRepository publisherRepoDouble;
    IListingRepository listingRepoDouble;
    IPublicationSampleRepository pubSampleRepoDouble;

    US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller;

    @BeforeEach
    void setUp() {
        publisherRepoDouble = mock(IPublisherRepository.class);
        listingRepoDouble = mock(IListingRepository.class);
        pubSampleRepoDouble = mock(IPublicationSampleRepository.class);
    }

    // ===============================================
    // ================== Tests ======================
    // ===============================================

    // ======= getAllPublishers ========================================================================================

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
        String format = "Book";
        when(type.getMediaFormat()).thenReturn(format);
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