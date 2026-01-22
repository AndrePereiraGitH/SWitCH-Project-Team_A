package TOPSECRET.repository;

import TOPSECRET.domain.Auction;
import TOPSECRET.domain.Listing;
import TOPSECRET.domain.Price;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionRepository {

    private static List<Auction> auctionsList;

    public AuctionRepository() {
        auctionsList = new ArrayList<>();
    }

    public List<Auction> getAllAuctions() {
        return auctionsList;
    }

    public boolean createAndAdd(Listing listing, LocalDateTime initialDate, LocalDateTime finalDate, Price buyOutPrice) throws InstanceAlreadyExistsException {
        Auction auction = new Auction(listing, initialDate, finalDate, buyOutPrice);

        if (isExistent(auction)) {
            return false;
        }

        auctionsList.add(auction);

        return true;
    }

    private boolean isExistent(Auction auction) {
        return auctionsList.contains(auction);
    }
}
