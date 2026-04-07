package TOPSECRET.controller;

import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.dto.PublicationSampleDTO_US027;
import TOPSECRET.dto.PublisherDTO_US027;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.Publisher.IPublisherRepository;


import java.util.List;

/**
 * Controller responsible for the execution of the US027 use case.
 * <p>
 * This controller coordinates the retrieval of publishers and the list of
 * publication samples that are available for direct sale from a given publisher.
 * It interacts with the {@link IPublisherRepository}, {@link IListingRepository},
 * and {@link IPublicationSampleRepository} to obtain the necessary domain data
 * and converts it into DTOs to be used by the application layer.
 */

public class US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController {

    private final IPublisherRepository _publisherRepo;
    private final IListingRepository _listingRepo;
    private final IPublicationSampleRepository _pubSampleRepo;

    // ========= Constructor ===========================================================================================
    public US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(IPublisherRepository publisherRepo,
                                                                       IListingRepository listingRepo,
                                                                       IPublicationSampleRepository pubSampleRepo) {

        this._publisherRepo = publisherRepo;
        this._listingRepo = listingRepo;
        this._pubSampleRepo = pubSampleRepo;
    }

     // ================================================================================================================

    /**
     * Retrieves all publishers stored in the system and converts them into DTOs.
     *
     * @return a list of {@link PublisherDTO_US027} representing all publishers
     */

    public List<PublisherDTO_US027> getAllPublishers() {

        // Gets all the publishers on repository
        List<Publisher> listOfAllPublishers = _publisherRepo.getAllPublishers();

        // Converts to DTOs
        List<PublisherDTO_US027> listOfAllPublishersDTO = PublisherDTO_US027.toDTOs(listOfAllPublishers);

        return listOfAllPublishersDTO;
    }

    /**
     * Retrieves all items on direct sale associated with the given publisher.
     *
     * <p>
     * The process includes:
     * <ol>
     *     <li>Retrieving all listings that correspond to direct sale</li>
     *     <li>Filtering publication samples that belong to the specified publisher</li>
     *     <li>Converting the resulting domain objects into DTOs</li>
     * </ol>
     * </p>
     *
     * @param publisher the publisher whose direct-sale items should be retrieved
     * @return a list of {@link PublicationSampleDTO_US027} associated with the given publisher
     */

//    public List<PublicationSampleDTO_US027> findListOfItemsOnDirectSaleByPublisher(Publisher publisher) {
//
//        // Gets all the listings for direct sale
//        List<Listing> listOfListings = _listingRepo.findAllDirectSaleListings();
//
//        // Gets all the publication samples related to the that publisher
//        List<PublicationSample> listOfItems = _pubSampleRepo.findItemsByListingAndPublisher(listOfListings, publisher);
//
//        // Converts to DTOs
//        List<PublicationSampleDTO_US027> listOfPublicationSampleDTO = PublicationSampleDTO_US027.toDTOs(listOfItems);
//
//        return listOfPublicationSampleDTO;
//    }
}
