package TOPSECRET.controller;

import java.util.List;

import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.dto.US028ListingDTO;
import TOPSECRET.dto.US028ListingMapper;
import TOPSECRET.dto.US028PublisherDTO;
import TOPSECRET.dto.US028PublisherMapper;

public class ToGetListOfItemsOnAuctionOfAGivenPublisherController {

    private final IPublisherRepository publisherRepo;
    private final IEditionRepository editionRepo;
    private final IPublicationSampleRepository pubSampleRepo;
    private final IListingRepository listingRepo;
    private final US028ListingMapper listingMapper;
    private final US028PublisherMapper publisherMapper;

    public ToGetListOfItemsOnAuctionOfAGivenPublisherController(
            IPublisherRepository publisherRepo,
            IEditionRepository editionRepo,
            IPublicationSampleRepository pubSampleRepo,
            IListingRepository listingRepo,
            US028ListingMapper listingMapper,
            US028PublisherMapper publisherMapper
    ) {
        if (publisherRepo == null) {
            throw new IllegalArgumentException("publisherRepo cannot be null");
        }

        if (editionRepo == null) {
            throw new IllegalArgumentException("editionRepo cannot be null");
        }

        if (pubSampleRepo == null) {
            throw new IllegalArgumentException("pubSampleRepo cannot be null");
        }

        if (listingRepo == null) {
            throw new IllegalArgumentException("listingRepo cannot be null");
        }

        if (listingMapper == null) {
            throw new IllegalArgumentException("listingMapper cannot be null");
        }

        if (publisherMapper == null) {
            throw new IllegalArgumentException("publisherMapper cannot be null");
        }

        this.publisherRepo = publisherRepo;
        this.editionRepo = editionRepo;
        this.pubSampleRepo = pubSampleRepo;
        this.listingRepo = listingRepo;
        this.listingMapper = listingMapper;
        this.publisherMapper = publisherMapper;
    }

    /**
     * Returns all publishers so the UI can let the user choose one.
     *
     * @return list of publisher DTOs
     */
    public List<US028PublisherDTO> getAllPublishers() {
        List<Publisher> listOfPublishers = publisherRepo.findAll();

        return publisherMapper.toDTOs(listOfPublishers);
    }

    /**
     * Returns the list of items/listings currently on auction
     * for a given publisher.
     *
     * @param publisherId the selected publisher id
     * @return list of auction listing DTOs
     */
    public List<US028ListingDTO> findListOfItemsOnAuctionByPublisherId(PublisherID publisherId) {

        if (publisherId == null) {
            throw new IllegalArgumentException("publisherId cannot be null");
        }

        List<EditionID> listOfEditionIds =
                editionRepo.findIdsByPublisherId(publisherId);

        List<PublicationSampleId> listOfPublicationSampleIds =
                pubSampleRepo.findIdsByEditionIdIn(listOfEditionIds);

        List<Listing> auctionListings =
                listingRepo.findAllByAuctionAndPublicationSampleIdIn(listOfPublicationSampleIds);

        return listingMapper.toDTO(auctionListings);
    }
}