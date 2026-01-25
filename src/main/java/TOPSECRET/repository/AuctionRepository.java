package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.dto.AuthorDTO_US026;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionRepository {

    private static List<Auction> auctionsList;

    public AuctionRepository() {
        auctionsList = new ArrayList<>();
    }

    public AuctionRepository(List<Auction> auctionsList) {
        this.auctionsList = auctionsList;
    }

    public List<Auction> getAllAuctions() {
        return auctionsList;
    }

    public boolean createAndAdd(Listing listing, LocalDateTime initialDate, LocalDateTime finalDate, Price buyOutPrice) {

        Auction auction = new Auction(listing, initialDate, finalDate, buyOutPrice);

        if (isExistent(auction)) {
            return false;
        }

        auctionsList.add(auction);

        return true;
    }

    public List<Auction> getListOfItemsOnAuctionGivenGenre(Genre genre) {

        if (genre == null) {
            throw new IllegalArgumentException("genre cannot be null");
        }

        List<Auction> result = new ArrayList<>();
        // will iterate over all auctions in the repository
        for (Auction auction : auctionsList) {
           // for each auction, will iterate over all items that are in Listing
            for(Item item : auction.getListing().getItems()) {
                //this condition means that we are getting throw all the information necessary to get the final list of genres
                if(item.getPublication()
                        .getBibliographicalData()
                        .getGenres().contains(genre)) {
                    result.add(auction);
                   // break when finds one, prevents the duplication
                    break;
                }
            }
        }

        return result;
    }


    public List<Auction> getListOfItemsOnAuctionGivenAuthor(AuthorDTO_US026 author) {

        if (author == null || author.name().isBlank())
            throw new IllegalArgumentException("Author cannot be null or blank");

        List<Auction> authorAuctions = auctionsList.stream().filter(auction -> auction.getListing().getItems()
                .stream()
                .anyMatch( item ->
                        item.getPublication()
                                .getBibliographicalData()
                                .getAuthors().stream()
                                .anyMatch(a -> a.getProfile().getName().equals(author.name()))
                )
        ).toList();

        return authorAuctions;
    }


    private boolean isExistent(Auction auction) {
        return auctionsList.contains(auction);
    }

    public List<Item> getListOfItemsOnAuctionByGivenPublication(Publication publication) {

        List<Item> listOfItemsOnAuctionByPublication = new ArrayList<>();

        for (Auction auc : auctionsList) {
            Listing listing = auc.getListing();

            if (listing != null && listing.getItems() != null) {

                for (Item item : listing.getItems()) {

                    if (item.getPublication() != null && item.getPublication().equals(publication)) {
                        listOfItemsOnAuctionByPublication.add(item);
                    }
                }
            }
        }

        return listOfItemsOnAuctionByPublication;
    }

    public List<Item> getItemsOnAuctionGivenPublisher(Publisher publisher) {
        if (publisher == null) {
            throw new IllegalArgumentException("publisher cannot be null");
        }

        List<Item> result = new ArrayList<>();

        for (Auction auction : auctionsList) {
            Listing listing = auction.getListing();
            if (listing == null || listing.getItems() == null) continue;

            for (Item item : listing.getItems()) {
                Publication pub = item.getPublication();
                if (pub == null) continue;

                BibliographicalData bd = pub.getBibliographicalData();
                if (bd == null) continue;

                Publisher pubPublisher = bd.getPublisher();
                if (publisher.equals(pubPublisher)) {
                    result.add(item);   // ✅ add item, not auction
                }
            }
        }

        return result;
    }


}
