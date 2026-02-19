package TOPSECRET.controller;

import java.util.List;
import java.util.ArrayList;

import TOPSECRET.domain.*;

import TOPSECRET.dto.AuctionDTO_US028;
import TOPSECRET.dto.AuctionMapper_US028;
import TOPSECRET.dto.PublisherDTO_US028;
import TOPSECRET.dto.PublisherMapper_US028;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.PublisherRepository;

/**
 * Controller responsible for use case US028:
 * "Get the list of items currently on auction for a given publisher".
 *
 * This controller receives requests from the UI and
 *  * gets the required data from the repositories.
 */
public class US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController {

    // attributes
    // Repository that stores and manages publishers
    private final PublisherRepository publisherRepo;

    // Repository that stores and manages auctions and items
    private final AuctionRepository auctionRepo;

    // Converts auction items into objects that the UI can display
    private final AuctionMapper_US028 auctionMapper;

    // Converts publishers into objects that the UI can display
    private final PublisherMapper_US028 publisherMapper;

    /**
     * Constructor that receives all dependencies required by the controller.
     * These objects are saved so the controller can use them later.
     * @param auctionRepository repository for auction-related operations
     * @param publisherRepository repository for publisher-related operations
     * @param auctionMapper mapper for converting auction items to DTOs
     * @param publisherMapper mapper for converting publishers to DTOs
     */
    public US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
            AuctionRepository auctionRepository,
            PublisherRepository publisherRepository,
            AuctionMapper_US028 auctionMapper,
            PublisherMapper_US028 publisherMapper) {

        // Store injected dependencies for later use
        this.publisherRepo = publisherRepository;
        this.auctionRepo = auctionRepository;
        this.auctionMapper = auctionMapper;
        this.publisherMapper = publisherMapper;
    }

    // methods
    /**
     * Step 2 of the use case:
     * Returns a list of all publishers.
     *      * This is used by the UI so the user can choose one publisher
     *
     * @return a list of PublisherDTO objects for presentation
     */
    public List<PublisherDTO_US028> getAllPublishers() {

        // Retrieve all publishers from the repository
        List<Publisher> publishers =
                publisherRepo.getAllPublishers();

        // Convert domain objects into DTOs before returning to the UI
        return publisherMapper.toDTO(publishers);
    }


    /**
     * Step 8 of the use case:
     * The UI provides the selected publisher's VAT number,
     * and the controller retrieves all items currently on auction
     * for that publisher.
     *
     * @param publisherVatNumber VAT number of the selected publisher
     * @return list of auction items as DTOs (empty list if publisher not found)
     */
//    public List<AuctionDTO_US028> getListOfItemsOnAuction(String publisherVatNumber) {
//
//        // Find the publisher using the provided VAT number
//        Publisher publisher =
//                publisherRepo.findByVatNumber(publisherVatNumber);
//
//        // If the publisher does not exist, return an empty list
//        if (publisher == null) return List.of();
//
//        // Retrieve all auction items associated with the given publisher
//        List<Item> items = auctionRepo.getItemsOnAuctionGivenPublisher(publisher);
//
//        // Convert domain items into DTOs suitable for the UI
//        return auctionMapper.toDTO(items);
//
//    }
}
