package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;

import javax.management.InstanceAlreadyExistsException;
import java.util.*;

/**
 * In-memory implementation of {@link IAuctionRepository}.
 * <p>
 * This repository stores {@link Auction} instances in a {@link Map} keyed by
 * {@link AuctionId}, providing efficient lookup, creation, and update operations.
 * </p>
 * <p>
 * Data is kept only during the application's runtime.
 * </p>
 */
public class AuctionRepositoryMem implements IAuctionRepository {

    private final Map<AuctionId, Auction> _auctionsMap;

    // ============================================================
    // ================ CONSTRUCTOR ===============================
    // ============================================================

    public AuctionRepositoryMem() {
        _auctionsMap = new HashMap<>();
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

        _auctionsMap.put(auction.id(), auction);

        return auction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Auction update(Auction auction) {

        if (!isExistent(auction)) {
            throw new IllegalArgumentException("Auction does not exist");
        }

        _auctionsMap.put(auction.id(), auction);

        return auction;
    }

    private boolean isExistent(Auction auction) {
        return _auctionsMap.containsKey(auction.id());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Auction> findAll() {
        return List.copyOf(_auctionsMap.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Auction> findById(AuctionId id) {

        if (id == null) {
            throw new IllegalArgumentException("AuctionId cannot be null");
        }

        return Optional.ofNullable(_auctionsMap.get(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Auction> findByListingIdIn(List<ListingId> listingIds) {

        if (listingIds == null) {
            throw new IllegalArgumentException("List of listingIds cannot be null.");
        }

        return _auctionsMap.values().stream()
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

        return _auctionsMap.values().stream()
                .filter(auction -> listingIds.contains(auction.getListingId()))
                .sorted(Comparator.comparing(Auction::getAuctionEndDateTime))
                .toList();

    }
}
