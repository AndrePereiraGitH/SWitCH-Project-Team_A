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


class ToGetListOfItemsOnDirectSaleOfGivenPublisherControllerTest {

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
    PublisherID publisherId1Double;
    PublisherID publisherId2Double;

    Edition editionDouble1;
    Edition editionDouble2;
    EditionID editionId1Double;
    EditionID editionId2Double;

    PublicationSample pubSampleDouble1;
    PublicationSample pubSampleDouble2;
    PublicationSampleId pubSampleId1Double;
    PublicationSampleId pubSampleId2Double;

    Listing listingDouble1;
    Listing listingDouble2;
    ListingId listingId1Double;
    ListingId listingId2Double;


    @BeforeEach
    void setUp() {
        publisherRepoDouble = mock(IPublisherRepository.class);
        listingRepoDouble = mock(IListingRepository.class);
        pubSampleRepoDouble = mock(IPublicationSampleRepository.class);
        editionRepoDouble = mock(IEditionRepository.class);
        mapperDouble = mock(US027ListingMapper.class);

        publisherDouble1 = mock(Publisher.class);
        publisherDouble2 = mock(Publisher.class);
        publisherId1Double = mock(PublisherID.class);
        publisherId2Double = mock(PublisherID.class);

        editionDouble1 = mock(Edition.class);
        editionDouble2 = mock(Edition.class);
        editionId1Double = mock(EditionID.class);
        editionId2Double = mock(EditionID.class);

        pubSampleDouble1 = mock(PublicationSample.class);
        pubSampleDouble2 = mock(PublicationSample.class);
        pubSampleId1Double = mock(PublicationSampleId.class);
        pubSampleId2Double = mock(PublicationSampleId.class);

        listingDouble1 = mock(Listing.class);
        listingDouble2 = mock(Listing.class);
        listingId1Double = mock(ListingId.class);
        listingId2Double = mock(ListingId.class);
    }

    // =================================================================================================================
    // ================== getAllPublishers =============================================================================
    // =================================================================================================================

    @Test
    void getAllPublishers_ShouldConvertPublishersToDTOs() {
        // Arrange
        when(publisherDouble1.id()).thenReturn(publisherId1Double);
        when(publisherDouble1.getName()).thenReturn("publisherName1");
        when(publisherDouble2.id()).thenReturn(publisherId2Double);
        when(publisherDouble2.getName()).thenReturn("publisherName2");

        when(publisherRepoDouble.getAllPublishers()).thenReturn(List.of(publisherDouble1, publisherDouble2));

        ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
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

        ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
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

        ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
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
        ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> controller.findListOfItemsOnDirectSaleByPublisherId(null));
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldReturnEmptyListWhenNoEditionIdsAreFound() throws InstanceNotFoundException {
        // Arrange
        when(editionRepoDouble.findIdsByPublisherId(publisherId1Double)).thenReturn(List.of());

        ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act
        List<US027ListingDTO> result =
                controller.findListOfItemsOnDirectSaleByPublisherId(publisherId1Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldReturnEmptyListWhenNoPublicationSampleIdsAreFound() throws InstanceNotFoundException {
        // Arrange
        Publisher pubDouble = mock(Publisher.class);
        PublisherID pubId1 = new PublisherID("publisherId1Double");

        Edition editionDouble = mock(Edition.class);
        EditionID editionId1 = new EditionID("editionId1");

        when(editionRepoDouble.findIdsByPublisherId(pubId1)).thenReturn(List.of(editionId1));
        when(pubSampleRepoDouble.findIdsByEditionIdIn(List.of(editionId1))).thenReturn(List.of());

        ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
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
        when(editionRepoDouble.findIdsByPublisherId(publisherId1Double)).thenReturn(List.of(editionId1Double));
        when(pubSampleRepoDouble.findIdsByEditionIdIn(List.of(editionId1Double))).thenReturn(List.of(pubSampleId1Double));
        when(listingRepoDouble.findAllByDirectSaleAndPublicationSampleIdIn(List.of(pubSampleId1Double))).thenReturn(List.of());

        ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act
        List<US027ListingDTO> result =
                controller.findListOfItemsOnDirectSaleByPublisherId(publisherId1Double);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldPropagateExceptionFromEditionRepoDouble() throws InstanceNotFoundException {
        // Arrange
        when(editionRepoDouble.findIdsByPublisherId(publisherId1Double)).thenThrow(new RuntimeException("DB error"));

        ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnDirectSaleByPublisherId(publisherId1Double));
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldPropagateExceptionFromPublicationSampleRepoDouble() throws InstanceNotFoundException {
        // Arrange
        when(editionRepoDouble.findIdsByPublisherId(publisherId1Double)).thenReturn(List.of(editionId1Double));
        when(pubSampleRepoDouble.findIdsByEditionIdIn(List.of(editionId1Double))).thenThrow(new RuntimeException("DB error"));

        ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnDirectSaleByPublisherId(publisherId1Double));
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldPropagateExceptionFromListingRepoDouble() {
        // Arrange
        when(editionRepoDouble.findIdsByPublisherId(publisherId1Double)).thenReturn(List.of(editionId1Double));
        when(pubSampleRepoDouble.findIdsByEditionIdIn(List.of(editionId1Double))).thenReturn(List.of(pubSampleId1Double));
        when(listingRepoDouble.findAllByDirectSaleAndPublicationSampleIdIn(List.of(pubSampleId1Double))).thenThrow(new RuntimeException("DB error"));

        ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnDirectSaleByPublisherId(publisherId1Double));
    }

    @Test
    void findListOfItemsOnDirectSaleByPublisherId_ShouldReturnListOfDTOs() throws InstanceNotFoundException {
        // Arrange
        US027ListingDTO listingDTODouble1 = mock(US027ListingDTO.class);
        US027ListingDTO listingDTODouble2 = mock(US027ListingDTO.class);

        when(editionRepoDouble.findIdsByPublisherId(publisherId1Double)).thenReturn(List.of(editionId1Double, editionId2Double));
        when(pubSampleRepoDouble.findIdsByEditionIdIn(List.of(editionId1Double, editionId2Double)))
            .thenReturn(List.of(pubSampleId1Double, pubSampleId2Double));
        when(listingRepoDouble.findAllByDirectSaleAndPublicationSampleIdIn(List.of(pubSampleId1Double, pubSampleId2Double)))
                .thenReturn(List.of(listingDouble1, listingDouble2));
        when(mapperDouble.toDTOs(List.of(listingDouble1, listingDouble2))).thenReturn(List.of(listingDTODouble1, listingDTODouble2));

        ToGetListOfItemsOnDirectSaleOfGivenPublisherController controller =
                new ToGetListOfItemsOnDirectSaleOfGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble, editionRepoDouble, mapperDouble);

        // Act
        List<US027ListingDTO> result =
                controller.findListOfItemsOnDirectSaleByPublisherId(publisherId1Double);

        // Assert
        assertEquals(2, result.size());
    }
}
