package TOPSECRET.repository;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of {@link IAuctionRepository}.
 * <p>
 * This repository stores {@link Auction} instances in a static list and
 * provides operations for creating, retrieving, and filtering auctions.
 * </p>
 * <p>
 * Auctions are created through an {@link AuctionFactory} and kept only
 * during the application's runtime.
 * </p>
 */
public class AuctionRepositoryMem implements IAuctionRepository {

    private static List<Auction> _auctionsList;
    private final AuctionFactory _factoryAuction;

    // ============================================================
    // ================ CONSTRUCTOR ===============================
    // ============================================================

    /**
     * Constructs an in-memory auction repository with the given auction factory.
     * <p>
     * Initializes the repository with an empty auction list.
     * </p>
     *
     * @param auctionFactory the factory used to create {@link Auction} instances
     */
    public AuctionRepositoryMem(AuctionFactory auctionFactory) {
        _factoryAuction = auctionFactory;
        _auctionsList = new ArrayList<>();
    }

    // ============================================================



    /**
     * {@inheritDoc}
     */
    @Override
    public Auction createAndAdd(ListingId listingId, LocalDateTime initialDate, LocalDateTime finalDate, Price initialPrice, Price buyoutPrice)
            throws InstanceAlreadyExistsException {

        Auction auction = _factoryAuction.create(listingId, initialDate, finalDate, initialPrice, buyoutPrice);

        if (isExistent(auction)) {
            throw new InstanceAlreadyExistsException("This auction already exists");
        }

        _auctionsList.add(auction);

        return auction;
    }

    private boolean isExistent(Auction auction) {
        return _auctionsList.contains(auction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Auction> getAllAuctions() {
        return List.copyOf(_auctionsList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Auction> findAuctionById(AuctionId id) {
        if (id == null) {
            throw new IllegalArgumentException("AuctionId cannot be null");
        }

        return _auctionsList.stream()
                .filter(auction -> auction.id().equals(id))
                .findFirst();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Auction> getListOfItemsOnAuctionGivenGenre(List<ListingId> listingId) {

        if (listingId == null) {
            throw new IllegalArgumentException("ListingId cannot be null");
        }

        List<Auction> result = new ArrayList<>();

        for(Auction auction : _auctionsList){
            if(listingId.contains(auction.getListingId())){
                result.add(auction);
            }
        }

        return result;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<Auction> findAuctionsByListingId(List<ListingId> listingIds) {

        if (listingIds == null) {
            throw new IllegalArgumentException("List of listingIds cannot be null.");
        }

        return _auctionsList.stream()
                .filter(auction -> listingIds.contains(auction.getListingId()))
                .toList();
    }

}
