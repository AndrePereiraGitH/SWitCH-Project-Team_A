package TOPSECRET.controller;

import java.util.List;

import TOPSECRET.domain.*;

import TOPSECRET.dto.PublicationSampleDTO_US028;
import TOPSECRET.dto.PublisherDTO_US028;
import TOPSECRET.repository.ListingRepositoryMem;
import TOPSECRET.repository.PublicationSampleRepositoryMem;
import TOPSECRET.repository.PublisherRepository;

/**
 * Controller responsible for use case US028:
 * "Get the list of items currently on auction for a given publisher".
 * This controller receives requests from the UI and
 *  * gets the required data from the repositories.
 */
public class US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController {

    // attributes
    private final PublisherRepository _publisherRepo;
    private final ListingRepositoryMem _listingRepo;
    private final PublicationSampleRepositoryMem _pubSampleRepo;

    /**
     * Constructor that receives all dependencies required by the controller.
     * These objects are saved so the controller can use them later.
     *
     * @param publisherRepository repository for publisher-related operations
     * @param listingRepo   repository for listing-related operations
     * @param pubSampleRepo   repository for publicationSample-related operations
     *
     */
    public US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController( PublisherRepository publisherRepository,
                                                                         ListingRepositoryMem listingRepo,
                                                                         PublicationSampleRepositoryMem pubSampleRepo) {

        // Store injected dependencies for later use
        this._publisherRepo = publisherRepository;
        this._listingRepo = listingRepo;
        this._pubSampleRepo = pubSampleRepo;
    }

    // methods

    /**
     * Returns a list of all publishers.
     * * This is used by the UI so the user can choose one publisher
     *
     * @return a list of PublisherDTO objects for presentation
     */
    public List<PublisherDTO_US028> getAllPublishers() {

        // Retrieve all publishers from the repository
        List<Publisher> listOfAllPublishers = _publisherRepo.getAllPublishers();

        List<PublisherDTO_US028> listOfAllPublishersDTO = PublisherDTO_US028.toDTOs(listOfAllPublishers);

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
     * @param publisher the publisher whose items should be retrieved; must not be null
     * @return a list of DTOs representing the publisher's items on auction;
     *         never null, but may be empty
     */


    public List<PublicationSampleDTO_US028> findListOfItemsOnAuctionByPublisher(Publisher publisher) {

        // Gets all the listings for auction
        List<Listing> listOfListings = _listingRepo.findAllAuctionListings();

        // Gets all the publication samples related to the that publisher
        List<PublicationSample> listOfItems = _pubSampleRepo.findItemsByListingAndPublisher(listOfListings, publisher);

        // Converts to DTOs
        List<PublicationSampleDTO_US028> listOfPublicationSampleDTO = PublicationSampleDTO_US028.toDTOs(listOfItems);

        return listOfPublicationSampleDTO;
    }
}