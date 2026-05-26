package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.dto.US028ListingDTO;
import TOPSECRET.dto.US028ListingMapper;
import TOPSECRET.dto.US028PublisherDTO;
import TOPSECRET.dto.US028PublisherMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToGetTheListOfItemsOnAuctionOfAGivenPublisherControllerTest {

    private IPublisherRepository publisherRepoDouble;
    private IEditionRepository editionRepoDouble;
    private IPublicationSampleRepository pubSampleRepoDouble;
    private IListingRepository listingRepoDouble;
    private US028ListingMapper listingMapperDouble;
    private US028PublisherMapper publisherMapperDouble;

    private ToGetListOfItemsOnAuctionOfAGivenPublisherController sut;

    @BeforeEach
    void setUp() {

        // Arrange
        publisherRepoDouble = mock(IPublisherRepository.class);
        editionRepoDouble = mock(IEditionRepository.class);
        pubSampleRepoDouble = mock(IPublicationSampleRepository.class);
        listingRepoDouble = mock(IListingRepository.class);
        listingMapperDouble = mock(US028ListingMapper.class);
        publisherMapperDouble = mock(US028PublisherMapper.class);
    }

    // --------- getAllPublishers() -------------

    @Test
    void getAllPublishers_shouldReturnPublisherDTOs_whenPublishersExist() {

        // Arrange
        sut = new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble,
                editionRepoDouble,
                pubSampleRepoDouble,
                listingRepoDouble,
                listingMapperDouble,
                publisherMapperDouble
        );

        Publisher publisherDouble = mock(Publisher.class);

        List<Publisher> publishers = List.of(publisherDouble);

        when(publisherRepoDouble.findAll()).thenReturn(publishers);

        // Act
        List<US028PublisherDTO> result = sut.getAllPublishers();

        // Assert
        assertNotNull(result);
    }

    @Test
    void getAllPublishers_shouldReturnEmptyList_whenNoPublishersExist() {

        // Arrange
        sut = new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble,
                editionRepoDouble,
                pubSampleRepoDouble,
                listingRepoDouble,
                listingMapperDouble,
                publisherMapperDouble
        );

        List<Publisher> publishers = List.of();

        when(publisherRepoDouble.findAll()).thenReturn(publishers);

        // Act
        List<US028PublisherDTO> result = sut.getAllPublishers();

        // Assert
        assertTrue(result.isEmpty());
    }

    // --------- Constructor validations -------------

    @Test
    void constructor_shouldThrowException_whenPublisherRepoIsNull() {

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        null,
                        editionRepoDouble,
                        pubSampleRepoDouble,
                        listingRepoDouble,
                        listingMapperDouble,
                        publisherMapperDouble
                )
        );

        // Assert
        assertEquals("publisherRepo cannot be null", exception.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenEditionRepoIsNull() {

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble,
                        null,
                        pubSampleRepoDouble,
                        listingRepoDouble,
                        listingMapperDouble,
                        publisherMapperDouble
                )
        );

        // Assert
        assertEquals("editionRepo cannot be null", exception.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenPubSampleRepoIsNull() {

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble,
                        editionRepoDouble,
                        null,
                        listingRepoDouble,
                        listingMapperDouble,
                        publisherMapperDouble
                )
        );

        // Assert
        assertEquals("pubSampleRepo cannot be null", exception.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenListingRepoIsNull() {

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble,
                        editionRepoDouble,
                        pubSampleRepoDouble,
                        null,
                        listingMapperDouble,
                        publisherMapperDouble
                )
        );

        // Assert
        assertEquals("listingRepo cannot be null", exception.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenListingMapperIsNull() {

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble,
                        editionRepoDouble,
                        pubSampleRepoDouble,
                        listingRepoDouble,
                        null,
                        publisherMapperDouble
                )
        );

        // Assert
        assertEquals("listingMapper cannot be null", exception.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenPublisherMapperIsNull() {

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepoDouble,
                        editionRepoDouble,
                        pubSampleRepoDouble,
                        listingRepoDouble,
                        listingMapperDouble,
                        null
                )
        );

        // Assert
        assertEquals("publisherMapper cannot be null", exception.getMessage());
    }

    // --------- findListOfItemsOnAuctionByPublisherId() -------------

    @Test
    void findListOfItemsOnAuctionByPublisherId_shouldThrowException_whenPublisherIdIsNull() {

        // Arrange
        sut = new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble,
                editionRepoDouble,
                pubSampleRepoDouble,
                listingRepoDouble,
                listingMapperDouble,
                publisherMapperDouble
        );

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> sut.findListOfItemsOnAuctionByPublisherId(null)
        );

        // Assert
        assertEquals("publisherId cannot be null", exception.getMessage());
    }

    @Test
    void findListOfItemsOnAuctionByPublisherId_shouldReturnListingDTOs_whenPublisherIdIsValid() {

        // Arrange
        sut = new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepoDouble,
                editionRepoDouble,
                pubSampleRepoDouble,
                listingRepoDouble,
                listingMapperDouble,
                publisherMapperDouble
        );

        PublisherID publisherIdDouble = mock(PublisherID.class);
        EditionId editionIdDouble = mock(EditionId.class);
        PublicationSampleId publicationSampleIdDouble = mock(PublicationSampleId.class);
        Listing listingDouble = mock(Listing.class);
        US028ListingDTO listingDTODouble = mock(US028ListingDTO.class);

        List<EditionId> editionIds = List.of(editionIdDouble);
        List<PublicationSampleId> publicationSampleIds = List.of(publicationSampleIdDouble);
        List<Listing> listings = List.of(listingDouble);
        List<US028ListingDTO> expected = List.of(listingDTODouble);

        when(editionRepoDouble.findIdsByPublisherId(publisherIdDouble)).thenReturn(editionIds);
        when(pubSampleRepoDouble.findIdsByEditionIdIn(editionIds)).thenReturn(publicationSampleIds);
        when(listingRepoDouble.findAllByAuctionAndPublicationSampleIdIn(publicationSampleIds)).thenReturn(listings);
        when(listingMapperDouble.toDTO(listings)).thenReturn(expected);

        // Act
        List<US028ListingDTO> result =
                sut.findListOfItemsOnAuctionByPublisherId(publisherIdDouble);

        // Assert
        assertEquals(expected, result);
    }
}