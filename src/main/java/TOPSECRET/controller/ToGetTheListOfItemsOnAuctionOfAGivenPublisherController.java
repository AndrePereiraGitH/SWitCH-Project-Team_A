package TOPSECRET.controller;

import java.util.List;


import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.dto.US028ListingDTO;
import TOPSECRET.dto.US028ListingMapper;
import TOPSECRET.dto.US028PublisherDTO;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.dto.US028PublisherMapper;

import javax.management.InstanceNotFoundException;


/**
 * Controller responsible for use case US028:
 * "Get the list of items currently on auction for a given publisher".
 * This controller receives requests from the UI and
 * * gets the required data from the repositories.
 */
public class ToGetTheListOfItemsOnAuctionOfAGivenPublisherController {

    // attributes
    private final IPublisherRepository _publisherRepo;
    private final IListingRepository _listingRepo;
    private final IPublicationSampleRepository _pubSampleRepo;
    private final IEditionRepository _editionRepo;
    private final US028ListingMapper _us028ListingMapper;

    /**
     * Constructor that receives all dependencies required by the controller.
     * These objects are saved so the controller can use them later.
     *
     * @param publisherRepository repository for publisher-related operations
     * @param listingRepo         repository for listing-related operations
     * @param pubSampleRepo       repository for publicationSample-related operations
     *
     */
    public ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(IPublisherRepository publisherRepository,
                                                                   IListingRepository listingRepo,
                                                                   IPublicationSampleRepository pubSampleRepo,
                                                                   US028ListingMapper publicationSampleMapper,
                                                                   IEditionRepository editionRepo) {

        // Store injected dependencies for later use
        this._publisherRepo = publisherRepository;
        this._listingRepo = listingRepo;
        this._pubSampleRepo = pubSampleRepo;
        this._us028ListingMapper = publicationSampleMapper;
        this._editionRepo = editionRepo;

    }

    /**
     * Returns a list of all publishers.
     * * This is used by the UI so the user can choose one publisher
     *
     * @return a list of PublisherDTO objects for presentation
     */
    public List<US028PublisherDTO> getAllPublishers() {

        List<Publisher> listOfAllPublishers = _publisherRepo.getAllPublishers();

        List<US028PublisherDTO> listOfAllPublishersDTO = US028PublisherMapper.toDTOs(listOfAllPublishers);

        return listOfAllPublishersDTO;
    }

    /**
     * Returns a list of publication samples (as DTOs) that are on auction
     * and belong to the specified publisher.
     *
     * <p>This method first retrieves all auction listings, then filters the
     * publication samples associated with those listings and the given publisher,
     * and finally converts the results into DTOs.</p>
     *
     * @param publisherId the publisher whose items should be retrieved; must not be null
     * @return a list of DTOs representing the publisher's items on auction;
     * never null, but may be empty.
     */


    public List<US028ListingDTO> findListOfItemsOnAuctionByPublisherId(PublisherID publisherId) throws InstanceNotFoundException {

        if (publisherId == null) throw new IllegalArgumentException("publisherId cannot be null");

        List<EditionID> listOfEditionIds = _editionRepo.findIdsByPublisherId(publisherId);

        List<PublicationSampleId> listOfPubSampleId =_pubSampleRepo.findIdsByEditionIdIn(listOfEditionIds);

        List<Listing> listOfListings = _listingRepo.findAllByAuctionAndPublicationSampleIdIn(listOfPubSampleId);

        return _us028ListingMapper.toDTO(listOfListings);
    }
}