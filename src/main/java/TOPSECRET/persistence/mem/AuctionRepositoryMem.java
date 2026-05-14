package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.Comparator;
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

    // ============================================================
    // ================ CONSTRUCTOR ===============================
    // ============================================================

    public AuctionRepositoryMem() {
        _auctionsList = new ArrayList<>();
    }

    // ============================================================


    /**
     * {@inheritDoc}
     */
    @Override
    public Auction save(Auction auction) throws InstanceAlreadyExistsException {

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
    public List<Auction> findAll() {
        return List.copyOf(_auctionsList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Auction> findById(AuctionId id) {
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
    public List<Auction> findByListingIdIn(List<ListingId> listingIds) {

        if (listingIds == null) {
            throw new IllegalArgumentException("List of listingIds cannot be null.");
        }

        return _auctionsList.stream()
                .filter(auction -> listingIds.contains(auction.getListingId()))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Auction> findByListingIdInOrderByAuctionDeadlineAsc(List<ListingId> listingIds) {
        if (listingIds == null) {
            throw new IllegalArgumentException("List of listingIds cannot be null.");
        }

        return _auctionsList.stream()
                .filter(auction -> listingIds.contains(auction.getListingId()))
                .sorted(Comparator.comparing(Auction::getAuctionEndDateTime))
                .toList();

    }
}
