package TOPSECRET.controller;

import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.dto.US028ListingDTO;
import TOPSECRET.dto.US028ListingMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToGetListOfItemsOnAuctionOfAGivenPublisherControllerTest {

    private IPublisherRepository publisherRepo;
    private IEditionRepository editionRepo;
    private IPublicationSampleRepository pubSampleRepo;
    private IListingRepository listingRepo;
    private US028ListingMapper listingMapper;

    private ToGetListOfItemsOnAuctionOfAGivenPublisherController sut;

    @BeforeEach
    void setUp() {
        publisherRepo = mock(IPublisherRepository.class);
        editionRepo = mock(IEditionRepository.class);
        pubSampleRepo = mock(IPublicationSampleRepository.class);
        listingRepo = mock(IListingRepository.class);
        listingMapper = mock(US028ListingMapper.class);

        sut = new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                publisherRepo,
                editionRepo,
                pubSampleRepo,
                listingRepo,
                listingMapper
        );
    }

    @Test
    void constructor_shouldThrowException_whenPublisherRepoIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        null,
                        editionRepo,
                        pubSampleRepo,
                        listingRepo,
                        listingMapper
                )
        );

        assertEquals("publisherRepo cannot be null", exception.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenEditionRepoIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepo,
                        null,
                        pubSampleRepo,
                        listingRepo,
                        listingMapper
                )
        );

        assertEquals("editionRepo cannot be null", exception.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenPubSampleRepoIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepo,
                        editionRepo,
                        null,
                        listingRepo,
                        listingMapper
                )
        );

        assertEquals("pubSampleRepo cannot be null", exception.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenListingRepoIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepo,
                        editionRepo,
                        pubSampleRepo,
                        null,
                        listingMapper
                )
        );

        assertEquals("listingRepo cannot be null", exception.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenListingMapperIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepo,
                        editionRepo,
                        pubSampleRepo,
                        listingRepo,
                        null
                )
        );

        assertEquals("listingMapper cannot be null", exception.getMessage());
    }

    @Test
    void findListOfItemsOnAuctionByPublisherId_shouldThrowException_whenPublisherIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> sut.findListOfItemsOnAuctionByPublisherId(null)
        );

        assertEquals("publisherId cannot be null", exception.getMessage());
    }

    @Test
    void findListOfItemsOnAuctionByPublisherId_shouldReturnListingDTOs_whenPublisherIdIsValid() {
        PublisherID publisherId = mock(PublisherID.class);
        EditionID editionId = mock(EditionID.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Listing listing = mock(Listing.class);
        US028ListingDTO listingDTO = mock(US028ListingDTO.class);

        List<EditionID> editionIds = List.of(editionId);
        List<PublicationSampleId> publicationSampleIds = List.of(publicationSampleId);
        List<Listing> listings = List.of(listing);
        List<US028ListingDTO> expected = List.of(listingDTO);

        when(editionRepo.findIdsByPublisherId(publisherId)).thenReturn(editionIds);
        when(pubSampleRepo.findIdsByEditionIdIn(editionIds)).thenReturn(publicationSampleIds);
        when(listingRepo.findAllByAuctionAndPublicationSampleIdIn(publicationSampleIds)).thenReturn(listings);
        when(listingMapper.toDTO(listings)).thenReturn(expected);

        List<US028ListingDTO> result =
                sut.findListOfItemsOnAuctionByPublisherId(publisherId);

        assertEquals(expected, result);

        verify(editionRepo).findIdsByPublisherId(publisherId);
        verify(pubSampleRepo).findIdsByEditionIdIn(editionIds);
        verify(listingRepo).findAllByAuctionAndPublicationSampleIdIn(publicationSampleIds);
        verify(listingMapper).toDTO(listings);
    }
}