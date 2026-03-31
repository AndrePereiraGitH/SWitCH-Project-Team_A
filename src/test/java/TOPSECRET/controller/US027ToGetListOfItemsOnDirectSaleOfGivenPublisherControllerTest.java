package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.dto.PublicationSampleDTO_US027;
import TOPSECRET.dto.PublisherDTO_US027;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.repository.IPublisherRepository;
import TOPSECRET.repository.IListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class US027ToGetListOfItemsOnDirectSaleOfGivenPublisherControllerTest {

    // ===============================================
    // ================== Set Up =====================
    // ===============================================

    IPublisherRepository publisherRepoDouble;
    IListingRepository listingRepoDouble;
    IPublicationSampleRepository pubSampleRepoDouble;

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

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act
        List<PublisherDTO_US027> result = controller.getAllPublishers();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getAllPublishers_ShouldReturnEmptyListWhenRepositoryReturnsEmpty() {
        // Arrange
        when(publisherRepoDouble.getAllPublishers()).thenReturn(List.of());

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act
        List<PublisherDTO_US027> result = controller.getAllPublishers();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublishers_ShouldPropagateExceptionFromRepository() {
        // Arrange
        when(publisherRepoDouble.getAllPublishers()).thenThrow(new RuntimeException("DB error"));

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act + Assert
        assertThrows(RuntimeException.class, () -> controller.getAllPublishers());
    }

    // ======= findListOfItemsOnDirectSaleByPublisher ==================================================================

    @Test
    void findListOfItemsOnDirectSaleByPublisher_ShouldReturnEmptyListWhenNoItemsFound() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        when(listingRepoDouble.findAllDirectSaleListings()).thenReturn(List.of());
        when(pubSampleRepoDouble.findItemsByListingAndPublisher(any(), any()))
                .thenReturn(List.of());

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act
        List<PublicationSampleDTO_US027> result =
                controller.findListOfItemsOnDirectSaleByPublisher(publisher);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisher_ShouldPropagateExceptionFromlistingRepoDouble() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        when(listingRepoDouble.findAllDirectSaleListings())
                .thenThrow(new RuntimeException("DB error"));

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnDirectSaleByPublisher(publisher));
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisher_ShouldPropagateExceptionFromPubSampleRepo() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        when(listingRepoDouble.findAllDirectSaleListings()).thenReturn(List.of());
        when(pubSampleRepoDouble.findItemsByListingAndPublisher(any(), any()))
                .thenThrow(new RuntimeException("DB error"));

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnDirectSaleByPublisher(publisher));
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisher_ShouldReturnConvertedDTOs() {
        // Arrange
        Publisher publisher = mock(Publisher.class);

        Listing listing = mock(Listing.class);
        PublicationSample sample = mock(PublicationSample.class);

        when(listingRepoDouble.findAllDirectSaleListings()).thenReturn(List.of(listing));
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

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble);

        // Act
        List<PublicationSampleDTO_US027> result =
                controller.findListOfItemsOnDirectSaleByPublisher(publisher);

        // Assert
        assertEquals(1, result.size());
    }
}
