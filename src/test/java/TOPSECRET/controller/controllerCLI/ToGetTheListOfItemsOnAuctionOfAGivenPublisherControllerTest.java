package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.dto.US028ListingMapper;
import TOPSECRET.dto.US028PublisherDTO;
import TOPSECRET.dto.US028PublisherMapper;
import TOPSECRET.services.ListingService;
import TOPSECRET.services.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToGetTheListOfItemsOnAuctionOfAGivenPublisherControllerTest {

    private PublisherService publisherServiceDouble;
    private IEditionRepository editionRepoDouble;
    private IPublicationSampleRepository pubSampleRepoDouble;
    private ListingService listingService;
    private US028ListingMapper listingMapperDouble;
    private US028PublisherMapper publisherMapperDouble;

    private ToGetListOfItemsOnAuctionOfAGivenPublisherController sut;

    @BeforeEach
    void setUp() {

        publisherServiceDouble = mock(PublisherService.class);
        editionRepoDouble = mock(IEditionRepository.class);
        pubSampleRepoDouble = mock(IPublicationSampleRepository.class);
        listingService = mock(ListingService.class);
        listingMapperDouble = mock(US028ListingMapper.class);
        publisherMapperDouble = mock(US028PublisherMapper.class);
    }

    // --------- getAllPublishers() -------------

    @Test
    void getAllPublishers_shouldReturnPublisherDTOs_whenPublishersExist() throws AccessDeniedException {

        // Arrange
        sut = new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                publisherServiceDouble,
                editionRepoDouble,
                pubSampleRepoDouble,
                listingService,
                listingMapperDouble,
                publisherMapperDouble
        );

        Publisher publisherDouble = mock(Publisher.class);
        US028PublisherDTO publisherDTO = mock(US028PublisherDTO.class);

        List<Publisher> publishers = List.of(publisherDouble);
        List<US028PublisherDTO> expected = List.of(publisherDTO);

        when(publisherServiceDouble.findAll()).thenReturn(publishers);
        when(publisherMapperDouble.toDTOs(publishers)).thenReturn(expected);

        // Act
        List<US028PublisherDTO> result = sut.getAllPublishers();

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void getAllPublishers_shouldReturnEmptyList_whenNoPublishersExist() throws AccessDeniedException {

        // Arrange
        sut = new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                publisherServiceDouble,
                editionRepoDouble,
                pubSampleRepoDouble,
                listingService,
                listingMapperDouble,
                publisherMapperDouble
        );

        List<Publisher> publishers = List.of();
        List<US028PublisherDTO> expected = List.of();

        when(publisherServiceDouble.findAll()).thenReturn(publishers);
        when(publisherMapperDouble.toDTOs(publishers)).thenReturn(expected);

        // Act
        List<US028PublisherDTO> result = sut.getAllPublishers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(expected, result);
    }

    // --------- Constructor validations -------------

    @Test
    void constructor_shouldThrowException_whenPublisherServiceIsNull() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        null,
                        editionRepoDouble,
                        pubSampleRepoDouble,
                        listingService,
                        listingMapperDouble,
                        publisherMapperDouble
                )
        );

        assertEquals("publisherService cannot be null", exception.getMessage());
    }
}