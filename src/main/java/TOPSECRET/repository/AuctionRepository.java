package TOPSECRET.repository;

import TOPSECRET.domain.Auction;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.Listing;
import TOPSECRET.domain.Price;
import TOPSECRET.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionRepository {

    private List<Auction> auctionsList;

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

        for (Auction auction : auctionsList) {

            List<Genre> genres = auction.getListing().getMainItem().getPublication().getBibliographicalData().getGenres();

            if (genres.contains(genre)) {
                result.add(auction);
            }
        }

        return result;
    }

    private boolean isExistent(Auction auction) {
        return auctionsList.contains(auction);
    }

    public List<Item> getListOfItemsOnAuctionByGivenPublication(Publication publication) {
        List<Item> listOfItemsOnAuction = new ArrayList<>();

        for (Auction auc : auctionsList) {
            Listing listing = auc.getListing();

            if (listing != null && listing.getItems() != null) {

                for (Item item : listing.getItems()) {

                    if (item.getPublication() != null && item.getPublication().equals(publication)) {
                        listOfItemsOnAuction.add(item);
                    }
                }
            }
        }

        return listOfItemsOnAuction;
    }




}
