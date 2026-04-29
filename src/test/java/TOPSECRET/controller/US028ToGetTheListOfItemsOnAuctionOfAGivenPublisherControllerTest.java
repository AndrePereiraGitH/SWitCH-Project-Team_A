package TOPSECRET.controller;

import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.dto.US028ListingDTO;
import TOPSECRET.dto.US028ListingMapper;
import TOPSECRET.dto.US028PublisherDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherControllerTest {

    private IPublisherRepository publisherRepoDouble;
    private IListingRepository listingRepoDouble;
    private IPublicationSampleRepository pubSampleRepoDouble;
    private IEditionRepository editionRepoDouble;
    private US028ListingMapper listingMapperDouble;

    @BeforeEach
    void setUp() {
        publisherRepoDouble = mock(IPublisherRepository.class);
        listingRepoDouble = mock(IListingRepository.class);
        pubSampleRepoDouble = mock(IPublicationSampleRepository.class);
        editionRepoDouble = mock(IEditionRepository.class);
        listingMapperDouble = mock(US028ListingMapper.class);
    }

    // ===============================================
    // =============== getAllPublishers ==============
    // ===============================================

    @Test
    void getAllPublishers_ShouldConvertPublishersToDTOs() {
        // Arrange
        Publisher pub1 = mock(Publisher.class);
        Publisher pub2 = mock(Publisher.class);

        PublisherID publisherId1 = mock(PublisherID.class);
        PublisherID publisherId2 = mock(PublisherID.class);

        when(pub1.getName()).thenReturn("Publisher One");
        when(pub1.id()).thenReturn(publisherId1);

        when(pub2.getName()).thenReturn("Publisher Two");
        when(pub2.id()).thenReturn(publisherId2);

        when(publisherRepoDouble.getAllPublishers()).thenReturn(List.of(pub1, pub2));

        US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller =
                new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        listingMapperDouble, editionRepoDouble);

        // Act
        List<US028PublisherDTO> result = controller.getAllPublishers();

        // Assert
        assertEquals(2, result.size());

        assertEquals("Publisher One", result.get(0).name());
        assertEquals(publisherId1, result.get(0).publisherId());

        assertEquals("Publisher Two", result.get(1).name());
        assertEquals(publisherId2, result.get(1).publisherId());
    }

    @Test
    void getAllPublishers_ShouldReturnEmptyListWhenRepositoryReturnsEmpty() {
        // Arrange
        when(publisherRepoDouble.getAllPublishers()).thenReturn(List.of());

        US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller =
                new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        listingMapperDouble, editionRepoDouble);

        // Act
        List<US028PublisherDTO> result = controller.getAllPublishers();

        // Assert
        assertTrue(result.isEmpty());
    }

    // ===============================================
    // ===== findListOfItemsOnAuctionByPublisher =====
    // ===============================================

    @Test
    void findListOfItemsOnAuctionByPublisherId_ShouldThrowWhenPublisherIdIsNull() {
        // Arrange
        US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller =
                new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        listingMapperDouble, editionRepoDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.findListOfItemsOnAuctionByPublisherId(null));
    }

    @Test
    void findListOfItemsOnAuctionByPublisherId_ShouldReturnEmptyListWhenNoEditionsFound()  throws InstanceNotFoundException {
        // Arrange
        PublisherID publisherIdDouble = mock(PublisherID.class);

        when(editionRepoDouble.findIdsByPublisherId(publisherIdDouble))
                .thenReturn(List.of());
        when(pubSampleRepoDouble.findPublicationSampleIdsByEditionIds(List.of()))
                .thenReturn(List.of());
        when(listingRepoDouble.getAuctionListingsContaining(List.of()))
                .thenReturn(List.of());
        when(listingMapperDouble.toDTO(List.of()))
                .thenReturn(List.of());

        US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller =
                new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        listingMapperDouble, editionRepoDouble);

        // Act
        List<US028ListingDTO> result = controller.findListOfItemsOnAuctionByPublisherId(publisherIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findListOfItemsOnAuctionByPublisherId_ShouldReturnEmptyListWhenNoListingsFound() throws InstanceNotFoundException {
        // Arrange
        PublisherID publisherIdDouble = mock(PublisherID.class);
        EditionID editionIdDouble = mock(EditionID.class);
        List<EditionID> editionIds = List.of(editionIdDouble);
        PublicationSampleId sampleIdDouble = mock(PublicationSampleId.class);
        List<PublicationSampleId> sampleIds = List.of(sampleIdDouble);

        when(editionRepoDouble.findIdsByPublisherId(publisherIdDouble))
                .thenReturn(editionIds);
        when(pubSampleRepoDouble.findPublicationSampleIdsByEditionIds(editionIds))
                .thenReturn(sampleIds);
        when(listingRepoDouble.getAuctionListingsContaining(sampleIds))
                .thenReturn(List.of());
        when(listingMapperDouble.toDTO(List.of()))
                .thenReturn(List.of());

        US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller =
                new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        listingMapperDouble, editionRepoDouble);

        // Act
        List<US028ListingDTO> result = controller.findListOfItemsOnAuctionByPublisherId(publisherIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findListOfItemsOnAuctionByPublisherId_ShouldReturnConvertedDTOs()  throws InstanceNotFoundException {
        // Arrange
        PublisherID publisherIdDouble = mock(PublisherID.class);
        EditionID editionIdDouble = mock(EditionID.class);
        List<EditionID> editionIds = List.of(editionIdDouble);
        PublicationSampleId sampleIdDouble = mock(PublicationSampleId.class);
        List<PublicationSampleId> sampleIds = List.of(sampleIdDouble);
        Listing listingDouble = mock(Listing.class);
        US028ListingDTO listingDTODouble = mock(US028ListingDTO.class);

        when(editionRepoDouble.findIdsByPublisherId(publisherIdDouble))
                .thenReturn(editionIds);
        when(pubSampleRepoDouble.findPublicationSampleIdsByEditionIds(editionIds))  // whole list, not single EditionID
                .thenReturn(sampleIds);
        when(listingRepoDouble.getAuctionListingsContaining(sampleIds))
                .thenReturn(List.of(listingDouble));
        when(listingMapperDouble.toDTO(List.of(listingDouble)))
                .thenReturn(List.of(listingDTODouble));

        US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller =
                new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        listingMapperDouble, editionRepoDouble);

        // Act
        List<US028ListingDTO> result = controller.findListOfItemsOnAuctionByPublisherId(publisherIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertSame(listingDTODouble, result.get(0));
    }

    @Test
    void findListOfItemsOnAuctionByPublisherId_MultipleEditions_ShouldReturnAllMatchingDTOs() throws InstanceNotFoundException {
        // Arrange
        PublisherID publisherIdDouble = mock(PublisherID.class);

        EditionID editionId1 = mock(EditionID.class);
        EditionID editionId2 = mock(EditionID.class);
        List<EditionID> editionIds = List.of(editionId1, editionId2);  // whole list passed at once

        PublicationSampleId sampleId1 = mock(PublicationSampleId.class);
        PublicationSampleId sampleId2 = mock(PublicationSampleId.class);
        List<PublicationSampleId> sampleIds = List.of(sampleId1, sampleId2);

        Listing listing1 = mock(Listing.class);
        Listing listing2 = mock(Listing.class);

        US028ListingDTO dto1 = mock(US028ListingDTO.class);
        US028ListingDTO dto2 = mock(US028ListingDTO.class);

        when(editionRepoDouble.findIdsByPublisherId(publisherIdDouble))
                .thenReturn(editionIds);
        when(pubSampleRepoDouble.findPublicationSampleIdsByEditionIds(editionIds))
                .thenReturn(sampleIds);
        when(listingRepoDouble.getAuctionListingsContaining(sampleIds))
                .thenReturn(List.of(listing1, listing2));
        when(listingMapperDouble.toDTO(List.of(listing1, listing2)))
                .thenReturn(List.of(dto1, dto2));

        US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller =
                new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        listingMapperDouble, editionRepoDouble);

        // Act
        List<US028ListingDTO> result = controller.findListOfItemsOnAuctionByPublisherId(publisherIdDouble);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(dto1, result.get(0));
        assertSame(dto2, result.get(1));
    }

    @Test
    void findListOfItemsOnAuctionByPublisherId_ShouldPropagateExceptionFromEditionRepo() {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);

        when(editionRepoDouble.findIdsByPublisherId(publisherId))
                .thenThrow(new RuntimeException("DB error"));

        US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller =
                new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        listingMapperDouble, editionRepoDouble);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnAuctionByPublisherId(publisherId));

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void findListOfItemsOnAuctionByPublisherId_ShouldPropagateExceptionFromPublicationSampleRepo() {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);
        EditionID editionId = mock(EditionID.class);
        List<EditionID> editionIds = List.of(editionId);

        when(editionRepoDouble.findIdsByPublisherId(publisherId))
                .thenReturn(editionIds);
        when(pubSampleRepoDouble.findPublicationSampleIdsByEditionIds(editionIds))
                .thenThrow(new RuntimeException("DB error"));

        US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller =
                new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        listingMapperDouble, editionRepoDouble);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnAuctionByPublisherId(publisherId));

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void findListOfItemsOnAuctionByPublisherId_ShouldPropagateExceptionFromListingRepo() {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);
        EditionID editionId = mock(EditionID.class);
        List<EditionID> editionIds = List.of(editionId);
        PublicationSampleId sampleId = mock(PublicationSampleId.class);
        List<PublicationSampleId> sampleIds = List.of(sampleId);

        when(editionRepoDouble.findIdsByPublisherId(publisherId))
                .thenReturn(editionIds);
        when(pubSampleRepoDouble.findPublicationSampleIdsByEditionIds(editionIds))
                .thenReturn(sampleIds);
        when(listingRepoDouble.getAuctionListingsContaining(sampleIds))
                .thenThrow(new RuntimeException("DB error"));

        US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller =
                new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        listingMapperDouble, editionRepoDouble);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnAuctionByPublisherId(publisherId));

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void findListOfItemsOnAuctionByPublisherId_ShouldPropagateExceptionFromMapper() {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);
        EditionID editionId = mock(EditionID.class);
        List<EditionID> editionIds = List.of(editionId);
        PublicationSampleId sampleId = mock(PublicationSampleId.class);
        List<PublicationSampleId> sampleIds = List.of(sampleId);
        Listing listing = mock(Listing.class);

        when(editionRepoDouble.findIdsByPublisherId(publisherId))
                .thenReturn(editionIds);
        when(pubSampleRepoDouble.findPublicationSampleIdsByEditionIds(editionIds))
                .thenReturn(sampleIds);
        when(listingRepoDouble.getAuctionListingsContaining(sampleIds))
                .thenReturn(List.of(listing));
        when(listingMapperDouble.toDTO(List.of(listing)))
                .thenThrow(new RuntimeException("Mapping error"));

        US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller =
                new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble, listingRepoDouble, pubSampleRepoDouble,
                        listingMapperDouble, editionRepoDouble);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> controller.findListOfItemsOnAuctionByPublisherId(publisherId));

        assertEquals("Mapping error", ex.getMessage());
    }
}