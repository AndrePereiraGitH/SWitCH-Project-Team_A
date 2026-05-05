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

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToGetListOfItemsOnAuctionOfAGivenPublisherControllerTest {

    @Test
    void findListOfItemsOnAuctionByPublisherId_shouldThrowException_whenPublisherIdIsNull() {
        // Arrange
        IPublisherRepository publisherRepo = mock(IPublisherRepository.class);
        IEditionRepository editionRepo = mock(IEditionRepository.class);
        IPublicationSampleRepository pubSampleRepo = mock(IPublicationSampleRepository.class);
        IListingRepository listingRepo = mock(IListingRepository.class);
        US028ListingMapper listingMapper = mock(US028ListingMapper.class);

        ToGetListOfItemsOnAuctionOfAGivenPublisherController sut =
                new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepo, editionRepo, pubSampleRepo, listingRepo, listingMapper
                );

        // Act
        Exception result = assertThrows(
                IllegalArgumentException.class,
                () -> sut.findListOfItemsOnAuctionByPublisherId(null)
        );

        // Assert
        assertEquals("publisherId cannot be null", result.getMessage());
    }

    @Test
    void findListOfItemsOnAuctionByPublisherId_shouldReturnListingDTOs() {
        // Arrange
        IPublisherRepository publisherRepo = mock(IPublisherRepository.class);
        IEditionRepository editionRepo = mock(IEditionRepository.class);
        IPublicationSampleRepository pubSampleRepo = mock(IPublicationSampleRepository.class);
        IListingRepository listingRepo = mock(IListingRepository.class);
        US028ListingMapper listingMapper = mock(US028ListingMapper.class);

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

        ToGetListOfItemsOnAuctionOfAGivenPublisherController sut =
                new ToGetListOfItemsOnAuctionOfAGivenPublisherController(
                        publisherRepo, editionRepo, pubSampleRepo, listingRepo, listingMapper
                );

        // Act
        List<US028ListingDTO> result =
                sut.findListOfItemsOnAuctionByPublisherId(publisherId);

        // Assert
        assertEquals(expected, result);
    }
}