package TOPSECRET.controller;

import TOPSECRET.domain.Listing;
import TOPSECRET.domain.PublicationSample;
import TOPSECRET.domain.Publisher;
import TOPSECRET.dto.PublicationSampleDTO_US027;
import TOPSECRET.dto.PublisherDTO_US027;
import TOPSECRET.repository.ListingRepository;
import TOPSECRET.repository.PublicationSampleRepository;
import TOPSECRET.repository.PublisherRepository;

import java.util.List;

public class US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController {

    private final PublisherRepository _publisherRepo;
    private final ListingRepository _listingRepo;
    private final PublicationSampleRepository _pubSampleRepo;

    // ========= Constructor ===========================================================================================
    public US027ToGetListOfItemsOnDirectSaleOfGivenPublisherController(PublisherRepository publisherRepo,
                                                                       ListingRepository listingRepo,
                                                                       PublicationSampleRepository pubSampleRepo) {

        this._publisherRepo = publisherRepo;
        this._listingRepo = listingRepo;
        this._pubSampleRepo = pubSampleRepo;
    }

     // ================================================================================================================

    public List<PublisherDTO_US027> getAllPublishers() {

        // Gets all the publishers on repository
        List<Publisher> listOfAllPublishers = _publisherRepo.getAllPublishers();

        // Converts to DTOs
        List<PublisherDTO_US027> listOfAllPublishersDTO = PublisherDTO_US027.toDTOs(listOfAllPublishers);

        return listOfAllPublishersDTO;
    }

    public List<PublicationSampleDTO_US027> findListOfItemsOnDirectSaleByPublisher(Publisher publisher) {

        // Gets all the listings for direct sale
        List<Listing> listOfListings = _listingRepo.findAllDirectSaleListings();

        // Gets all the publication samples related to the that publisher
        List<PublicationSample> listOfItems = _pubSampleRepo.findItemsByListingAndPublisher(listOfListings, publisher);

        // Converts to DTOs
        List<PublicationSampleDTO_US027> listOfPublicationSampleDTO = PublicationSampleDTO_US027.toDTOs(listOfItems);

        return listOfPublicationSampleDTO;
    }
}
