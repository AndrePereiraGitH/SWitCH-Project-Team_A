package TOPSECRET.controller;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.dto.US027ListingDTO;
import TOPSECRET.dto.US027ListingMapper;
import TOPSECRET.dto.US027PublisherDTO;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.Publisher.IPublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class US027ToGetListOfItemsOnDirectSaleOfGivenPublisherControllerTest {

    // =================================================================================================================
    // ================== Set Up =======================================================================================
    // =================================================================================================================

    IPublisherRepository publisherRepoDouble;
    IListingRepository listingRepoDouble;
    IPublicationSampleRepository pubSampleRepoDouble;
    IEditionRepository editionRepoDouble;
    US027ListingMapper mapperDouble;

    Publisher publisherDouble1;
    Publisher publisherDouble2;
    PublisherID publisherId1;
    PublisherID publisherId2;

    Edition editionDouble1;
    Edition editionDouble2;
    EditionID editionId1;
    EditionID editionId2;

    PublicationSample pubSampleDouble1;
    PublicationSample pubSampleDouble2;
    PublicationSampleId pubSampleId1;
    PublicationSampleId pubSampleId2;

    Listing listingDouble1;
    Listing listingDouble2;
    ListingId listingId1;
    ListingId listingId2;


    @BeforeEach
    void setUp() {
        publisherRepoDouble = mock(IPublisherRepository.class);
        listingRepoDouble = mock(IListingRepository.class);
        pubSampleRepoDouble = mock(IPublicationSampleRepository.class);
        editionRepoDouble = mock(IEditionRepository.class);
        mapperDouble = mock(US027ListingMapper.class);

        publisherDouble1 = mock(Publisher.class);
        publisherDouble2 = mock(Publisher.class);
        publisherId1 = new PublisherID("publisherId1");
        publisherId2 = new PublisherID("publisherId2");

        editionDouble1 = mock(Edition.class);
        editionDouble2 = mock(Edition.class);
        editionId1 = new EditionID("editionId1");
        editionId2 = new EditionID("editionId2");

        pubSampleDouble1 = mock(PublicationSample.class);
        pubSampleDouble2 = mock(PublicationSample.class);
        pubSampleId1 = new PublicationSampleId("pubSampleId1");
        pubSampleId2 = new PublicationSampleId("pubSampleId2");

        listingDouble1 = mock(Listing.class);
        listingDouble2 = mock(Listing.class);
        listingId1 = new ListingId("listingId1");
        listingId2 = new ListingId("listingId2");
    }

    // =================================================================================================================
    // ================== getAllPublishers =============================================================================
    // =================================================================================================================

    @Test
    void getAllPublishers_ShouldConvertPublishersToDTOs() {
        // Arrange
        when(publisherDouble1.id()).thenReturn(publisherId1);
        when(publisherDouble1.getName()).thenReturn("publisherName1");
        when(publisherDouble2.id()).thenReturn(publisherId2);
        when(publisherDouble2.getName()).thenReturn("publisherName2");

        when(publisherRepoDouble.getAllPublishers()).thenReturn(List.of(publisherDouble1, publisherDouble2));

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act
        List<US027PublisherDTO> result = controller.getAllPublishers();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getAllPublishers_ShouldReturnEmptyListWhenRepositoryReturnsEmpty() {
        // Arrange
        when(publisherRepoDouble.getAllPublishers()).thenReturn(List.of());

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act
        List<US027PublisherDTO> result = controller.getAllPublishers();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublishers_ShouldPropagateExceptionFromRepository() {
        // Arrange
        when(publisherRepoDouble.getAllPublishers()).thenThrow(new RuntimeException("DB error"));

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(RuntimeException.class, () -> controller.getAllPublishers());
    }

    // =================================================================================================================
    // ======== findListOfItemsOnDirectSaleByPublisherId ===============================================================
    // =================================================================================================================

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldThrowExceptionWhenPublisherIdIsNull() {
        // Arrange
        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> controller.findListOfItemsOnDirectSaleByPublisherId(null));
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldReturnEmptyListWhenNoEditionIdsAreFound() throws InstanceNotFoundException {
        // Arrange
        when(editionRepoDouble.findEditionIdsByPublisherId(publisherId1)).thenReturn(List.of());

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act
        List<US027ListingDTO> result =
                controller.findListOfItemsOnDirectSaleByPublisherId(publisherId1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldReturnEmptyListWhenNoPublicationSampleIdsAreFound() throws InstanceNotFoundException {
        // Arrange
        Publisher pubDouble = mock(Publisher.class);
        PublisherID pubId1 = new PublisherID("publisherId1");

        Edition editionDouble = mock(Edition.class);
        EditionID editionId1 = new EditionID("editionId1");

        when(editionRepoDouble.findEditionIdsByPublisherId(pubId1)).thenReturn(List.of(editionId1));
        when(pubSampleRepoDouble.findPublicationSampleIdsByEditionIds(List.of(editionId1))).thenReturn(List.of());

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act
        List<US027ListingDTO> result =
                controller.findListOfItemsOnDirectSaleByPublisherId(pubId1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldReturnEmptyListWhenNoListingsAreFound() throws InstanceNotFoundException {
        // Arrange
        when(editionRepoDouble.findEditionIdsByPublisherId(publisherId1)).thenReturn(List.of(editionId1));
        when(pubSampleRepoDouble.findPublicationSampleIdsByEditionIds(List.of(editionId1))).thenReturn(List.of(pubSampleId1));
        when(listingRepoDouble.findDirectSaleListingsContaining(List.of(pubSampleId1))).thenReturn(List.of());

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act
        List<US027ListingDTO> result =
                controller.findListOfItemsOnDirectSaleByPublisherId(publisherId1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldPropagateExceptionFromEditionRepoDouble() throws InstanceNotFoundException {
        // Arrange
        when(editionRepoDouble.findEditionIdsByPublisherId(publisherId1)).thenThrow(new RuntimeException("DB error"));

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnDirectSaleByPublisherId(publisherId1));
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldPropagateExceptionFromPublicationSampleRepoDouble() throws InstanceNotFoundException {
        // Arrange
        when(editionRepoDouble.findEditionIdsByPublisherId(publisherId1)).thenReturn(List.of(editionId1));
        when(pubSampleRepoDouble.findPublicationSampleIdsByEditionIds(List.of(editionId1))).thenThrow(new RuntimeException("DB error"));

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnDirectSaleByPublisherId(publisherId1));
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldPropagateExceptionFromListingRepoDouble() {
        // Arrange
        when(editionRepoDouble.findEditionIdsByPublisherId(publisherId1)).thenReturn(List.of(editionId1));
        when(pubSampleRepoDouble.findPublicationSampleIdsByEditionIds(List.of(editionId1))).thenReturn(List.of(pubSampleId1));
        when(listingRepoDouble.findDirectSaleListingsContaining(List.of(pubSampleId1))).thenThrow(new RuntimeException("DB error"));

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnDirectSaleByPublisherId(publisherId1));
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldReturnListOfDTOs() throws InstanceNotFoundException {
        // Arrange
        US027ListingDTO listingDTODouble1 = mock(US027ListingDTO.class);
        US027ListingDTO listingDTODouble2 = mock(US027ListingDTO.class);

        when(editionRepoDouble.findEditionIdsByPublisherId(publisherId1)).thenReturn(List.of(editionId1, editionId2));
        when(pubSampleRepoDouble.findPublicationSampleIdsByEditionIds(List.of(editionId1, editionId2)))
            .thenReturn(List.of(pubSampleId1, pubSampleId2));
        when(listingRepoDouble.findDirectSaleListingsContaining(List.of(pubSampleId1, pubSampleId2)))
                .thenReturn(List.of(listingDouble1, listingDouble2));
        when(mapperDouble.toDTOs(List.of(listingDouble1, listingDouble2))).thenReturn(List.of(listingDTODouble1, listingDTODouble2));

        US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act
        List<US027ListingDTO> result =
                controller.findListOfItemsOnDirectSaleByPublisherId(publisherId1);

        // Assert
        assertEquals(2, result.size());
    }
}
