package TOPSECRET.controller;

import java.util.List;
import java.util.ArrayList;

import TOPSECRET.domain.*;

import TOPSECRET.dto.AuctionDTO_US028;
import TOPSECRET.dto.PublisherDTO_US028;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.PublisherRepository;
import java.util.ArrayList;

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


    /**
     * Constructor that receives all dependencies required by the controller.
     * These objects are saved so the controller can use them later.
     *
     * @param auctionRepository   repository for auction-related operations
     * @param publisherRepository repository for publisher-related operations
     *
     */
    public US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
            AuctionRepository auctionRepository,
            PublisherRepository publisherRepository) {

        // Store injected dependencies for later use
        this.publisherRepo = publisherRepository;
        this.auctionRepo = auctionRepository;
    }

    // methods

    /**
     * Step 2 of the use case:
     * Returns a list of all publishers.
     * * This is used by the UI so the user can choose one publisher
     *
     * @return a list of PublisherDTO objects for presentation
     */
    public List<PublisherDTO_US028> getAllPublishers() {

        // Retrieve all publishers from the repository
        List<Publisher> publishers =
                publisherRepo.getAllPublishers();
        List<PublisherDTO_US028> result = new ArrayList<>();
        for (Publisher p : publishers) {
            result.add(new PublisherDTO_US028(p));
        }

        return result;
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
    public List<AuctionDTO_US028> getListOfItemsOnAuction(String publisherVatNumber) {

        // Find the publisher using the provided VAT number
        Publisher publisher =
                publisherRepo.findByVatNumber(publisherVatNumber);

        // If the publisher does not exist, return an empty list
        if (publisher == null) return List.of();

        // Retrieve all auction items associated with the given publisher
        List<PublicationSample> items = auctionRepo.getItemsOnAuctionGivenPublisher(publisher);

        List<AuctionDTO_US028> result = new ArrayList<>();

        for (PublicationSample item : items) {

            List<String> publisherNames = new ArrayList<>();
            // Use owner username as publisher name
            publisherNames.add(item.getOwner().getUserName());

            AuctionDTO_US028 dto =
                    new AuctionDTO_US028(item.getBibliographicalData().getTitle(), publisherNames);

            result.add(dto);
        }

        return result;
    }
}