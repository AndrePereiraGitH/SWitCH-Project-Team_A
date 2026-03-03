package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.dto.PublicationSampleDTO_US027;
import TOPSECRET.dto.PublisherDTO_US027;
import TOPSECRET.repository.ListingRepository;
import TOPSECRET.repository.PublicationSampleRepository;
import TOPSECRET.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class US027ToGetListOfItemsOnDirectSaleOfGivenPublisherControllerTest {

    // ===============================================
    // ================== Set Up =====================
    // ===============================================

    PublisherRepository publisherRepoDouble;
    ListingRepository listingRepoDouble;
    PublicationSampleRepository pubSampleRepoDouble;

    US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller;

    @BeforeEach
    void setUp() {
        publisherRepoDouble = mock(PublisherRepository.class);
        listingRepoDouble = mock(ListingRepository.class);
        pubSampleRepoDouble = mock(PublicationSampleRepository.class);

        controller = new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble
        );
    }

    // ===============================================
    // ================== Tests ======================
    // ===============================================

    // ======= getAllPublishers ========================================================================================

    @Test
    void getAllPublishers_ShouldCallRepositoryOnce() {
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

        // Act
        List<PublisherDTO_US027> result = controller.getAllPublishers();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getAllPublishers_ShouldReturnEmptyListWhenRepositoryReturnsEmpty() {
        // Arrange
        when(publisherRepoDouble.getAllPublishers()).thenReturn(List.of());

        // Act
        List<PublisherDTO_US027> result = controller.getAllPublishers();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublishers_ShouldPropagateExceptionFromRepository() {
        // Arrange
        when(publisherRepoDouble.getAllPublishers()).thenThrow(new RuntimeException("DB error"));

        // Act + Assert
        assertThrows(RuntimeException.class, () -> controller.getAllPublishers());
    }

    // ======= findListOfItemsOnDirectSaleByPublisher ==================================================================

    @Test
    void findListOfItemsOnDirectSaleByPublisher_ShouldCalllistingRepoDouble() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        when(listingRepoDouble.findAllDirectSaleListings()).thenReturn(List.of());
        when(pubSampleRepoDouble.findItemsByListingAndPublisher(any(), any())).thenReturn(List.of());

        // Act
        controller.findListOfItemsOnDirectSaleByPublisher(publisher);

        // Assert
        verify(listingRepoDouble, times(1)).findAllDirectSaleListings();
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisher_ShouldCallPubSampleRepoWithCorrectArguments() {
        // Arrange
        Publisher publisher = mock(Publisher.class);

        Listing l1 = mock(Listing.class);
        Listing l2 = mock(Listing.class);
        List<Listing> listings = List.of(l1, l2);

        when(listingRepoDouble.findAllDirectSaleListings()).thenReturn(listings);
        when(pubSampleRepoDouble.findItemsByListingAndPublisher(listings, publisher))
                .thenReturn(List.of());

        // Act
        controller.findListOfItemsOnDirectSaleByPublisher(publisher);

        // Assert
        verify(pubSampleRepoDouble).findItemsByListingAndPublisher(listings, publisher);
    }
    @Test
    void findListOfItemsOnDirectSaleByPublisher_ShouldReturnEmptyListWhenNoItemsFound() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        when(listingRepoDouble.findAllDirectSaleListings()).thenReturn(List.of());
        when(pubSampleRepoDouble.findItemsByListingAndPublisher(any(), any()))
                .thenReturn(List.of());

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

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnDirectSaleByPublisher(publisher));
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisher_ShouldCallMethodsInCorrectOrder() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        when(listingRepoDouble.findAllDirectSaleListings()).thenReturn(List.of());
        when(pubSampleRepoDouble.findItemsByListingAndPublisher(any(), any()))
                .thenReturn(List.of());

        // Act
        controller.findListOfItemsOnDirectSaleByPublisher(publisher);

        // Assert
        InOrder order = inOrder(listingRepoDouble, pubSampleRepoDouble);
        order.verify(listingRepoDouble).findAllDirectSaleListings();
        order.verify(pubSampleRepoDouble).findItemsByListingAndPublisher(any(), eq(publisher));
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

        // Act
        List<PublicationSampleDTO_US027> result =
                controller.findListOfItemsOnDirectSaleByPublisher(publisher);

        // Assert
        assertEquals(1, result.size());
    }

}
