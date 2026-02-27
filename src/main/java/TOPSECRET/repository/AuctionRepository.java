package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.AuctionFactory;
import TOPSECRET.dto.AuthorDTO_US026;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionRepository {

    private static List<Auction> _auctionsList;
    private final AuctionFactory _factoryAuction;


    public AuctionRepository(AuctionFactory auctionFactory) {
        _factoryAuction = auctionFactory;
        _auctionsList = new ArrayList<>();
    }

    public AuctionRepository(List<Auction> auctionsList, AuctionFactory auctionFactory) {
        this._auctionsList = auctionsList;
        this._factoryAuction = auctionFactory;
    }


    public List<Auction> getAllAuctions() {
        return _auctionsList;
    }

    /**
     * Creates a new auction and adds it to the repository if it does not already exist.
     *
     * @param listing the listing associated with the auction
     * @param initialDate the auction start date and time
     * @param finalDate the auction end date and time
     * @param buyOutPrice the buyout price of the auction
     * @return {@code true} if the auction was created and added, {@code false} if it already exists
     */
    public Auction createAndAdd(Listing listing, LocalDateTime initialDate, LocalDateTime finalDate, Price buyOutPrice) throws InstanceAlreadyExistsException {


        Auction auction = _factoryAuction.create(listing, initialDate, finalDate, buyOutPrice);

        if (isExistent(auction)) {
            throw new InstanceAlreadyExistsException("this auction already exists");
        }

        _auctionsList.add(auction);

        return auction;
    }

    /**
     * Returns the list of auctions containing at least one item whose publication
     * belongs to the given genre.
     *
     * @param genre the genre used to filter auctions
     * @return a list of auctions that contain at least one item with the given genre
     * @throws IllegalArgumentException if {@code genre} is {@code null}
     */
    public List<Auction> getListOfItemsOnAuctionGivenGenre(Genre genre) {

        if (genre == null) {
            throw new IllegalArgumentException("genre cannot be null");
        }

        List<Auction> result = new ArrayList<>();
        // will iterate over all auctions in the repository
        for (Auction auction : _auctionsList) {
           // for each auction, will iterate over all items that are in Listing
            for(PublicationSample publicationSample : auction.getListing().getItems()) {
                //this condition means that we are getting throw all the information necessary to get the final list of genres
                if(publicationSample.getBibliographicalData()
                        .getGenres().contains(genre)) {
                    result.add(auction);
                   // break when finds one, prevents the duplication
                    break;
                }
            }
        }

        return result;
    }


    /**
     * Returns the list of auctions containing at least one item whose publication
     * is associated with the given author.
     *
     * @param author the author DTO used to filter auctions
     * @return a list of auctions that contain at least one item by the given author
     * @throws IllegalArgumentException if {@code author} is {@code null} or its name is blank
     */
    public List<Auction> getListOfItemsOnAuctionGivenAuthor(AuthorDTO_US026 author) {

        if (author == null || author.name().isBlank())
            throw new IllegalArgumentException("Author cannot be null or blank");

        List<Auction> authorAuctions = _auctionsList.stream().filter(auction -> auction.getListing().getItems()
                .stream()
                .anyMatch( item ->
                        item.getBibliographicalData()
                                .getAuthors().stream()
                                .anyMatch(a -> a.getProfile().getName().equals(author.name()))
                )
        ).toList();

        return authorAuctions;
    }


    private boolean isExistent(Auction auction) {
        return _auctionsList.contains(auction);
    }

//    public List<Item> getListOfItemsOnAuctionByGivenPublication(Publication publication) {
//
//        List<Item> listOfItemsOnAuctionByPublication = new ArrayList<>();
//
//        for (Auction auc : auctionsList) {
//            Listing listing = auc.getListing();
//
//            if (listing != null && listing.getItems() != null) {
//
//                for (Item item : listing.getItems()) {
//
//                    if (item.getPublication() != null && item.getPublication().equals(publication)) {
//                        listOfItemsOnAuctionByPublication.add(item);
//                    }
//                }
//            }
//        }
//
//        return listOfItemsOnAuctionByPublication;
//    }

    /**
     * Returns all items that are currently on auction
     * and belong to the given publisher.
     *
     * @param publisher the publisher to search for
     * @return list of items on auction for that publisher
     */
//    public List<Item> getItemsOnAuctionGivenPublisher(Publisher publisher) {
//
//        // Check that the publisher was provided
//        if (publisher == null) {
//            throw new IllegalArgumentException("publisher cannot be null");
//        }
//
//        // List to store the matching items
//        List<Item> result = new ArrayList<>();
//
//        // Go through all auctions in the system
//        for (Auction auction : auctionsList) {
//
//            // Get the listing associated with the auction
//            Listing listing = auction.getListing();
//
//            // If the listing or its items are missing, skip this auction
//            if (listing == null || listing.getItems() == null) continue;
//
//            // Go through each item in the listing
//            for (Item item : listing.getItems()) {
//
//                // Get the publication of the item
//                Publication pub = item.getPublication();
//                if (pub == null) continue;
//
//                // Get bibliographical data of the publication
//                BibliographicalData bd = pub.getBibliographicalData();
//                if (bd == null) continue;

                // Get the publisher of the publication
//                Publisher pubPublisher = pub.getEdition().getPublisher();
//
//
//                // If the item's publisher matches the given publisher,
//                // add the item to the result list
//                if (publisher.equals(pubPublisher)) {
//                    result.add(item);   // ✅ add item, not auction
//                }
//                Edition edition = pub.getEdition();
//                if (edition == null) continue;
//
//                Publisher pubPublisher = edition.getPublisher();
//                if (pubPublisher == null) continue;
//
//                if (publisher.equals(pubPublisher)) {
//                    result.add(item);
//                }
//
//            }
//        }
//
//        // Return all matching items
//        return result;
//    }


}
