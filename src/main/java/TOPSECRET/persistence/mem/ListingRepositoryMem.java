package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.ValueObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of {@link IListingRepository}.
 * <p>
 * This repository stores {@link Listing} instances in an internal list and
 * provides operations for creating, retrieving, and filtering listings.
 * </p>
 * <p>
 * Listings are created through a {@link ListingFactory} and are kept only
 * during the application's runtime.
 * </p>
 */
public class ListingRepositoryMem implements IListingRepository {

    private final List<Listing> listings;
    private final ListingFactory listingFactory;

    /**
     * Constructs an in-memory listing repository with the given listing factory.
     *
     * @param listingFactory the factory used to create {@link Listing} instances
     */
    public ListingRepositoryMem(ListingFactory listingFactory) {

        if (listingFactory == null)
            throw new IllegalArgumentException("ListingFactory cannot be null");

        this.listings = new ArrayList<>();
        this.listingFactory = listingFactory;
    }


    // =========================================================================
    // Persistence
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public Listing save(Listing listing) {
        if (existsById(listing)) {
            throw new IllegalArgumentException("PublicationSample already exists in the repository");
        }
        listings.add(listing);
        return listing;
    }

    private boolean existsById(Listing listing) {
        return listings.contains(listing);
    }


    // =========================================================================
    // Lookup — by identity
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Listing> findById(ListingId id) {
        if (id == null) {
            throw new IllegalArgumentException("ListingId cannot be null");
        }

        return listings.stream()
                .filter(listing -> listing.id().equals(id))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Listing> findAll() {
        return List.copyOf(listings);
    }


    // =========================================================================
    // Lookup — by sale type
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Listing> findAllByDirectSale() {

        List<Listing> dsListings = new ArrayList<>();
        for (Listing listing : listings) {
            if (listing.getSaleType() == SaleType.DIRECT_SALE) {
                dsListings.add(listing);
            }
        }
        return dsListings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Listing> findAllByAuction() {

        List<Listing> auctionListings = new ArrayList<>();
        for (Listing listing : listings) {
            if (listing.getSaleType() == SaleType.AUCTION) {
                auctionListings.add(listing);
            }
        }
        return auctionListings;
    }


    // =========================================================================
    // Lookup — by associated identifiers
    // =========================================================================

    @Override
    public List<ListingId> findIdsByPublicationSampleIdIn(List<PublicationSampleId> itemsId) {
        if(itemsId == null){
            throw new IllegalArgumentException("itemsId cannot be null");
        }
        List<ListingId> listingIds = new ArrayList<>();
        for (Listing listing : listings) {

            List<PublicationSampleId> listingItems = listing.getItemsId();

            for(PublicationSampleId itemId : itemsId){
                if(listingItems.contains(itemId)){
                    listingIds.add(listing.id());
                    break;
                }
            }
        }
        return listingIds;
    }


    // =========================================================================
    // Domain Operations
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Listing> findAllByAuctionAndPublicationSampleIdIn(List<PublicationSampleId> listOfPubSampleIds) {

        if(listOfPubSampleIds == null){
            throw new NullPointerException("The list can not be null");
        }

        if(listOfPubSampleIds.isEmpty()){
            throw new IllegalArgumentException("The list can not be empty");
        }

        List<Listing> listOfAuctionListingsOfGivenPublication = new ArrayList<>();

        for (Listing listing : listings) {
            if (listing.isListingLinkedToAnAuction()) {

                for(PublicationSampleId id: listOfPubSampleIds){
                    if(listing.containsPubSampleId(id)){
                        listOfAuctionListingsOfGivenPublication.add(listing);
                        break;
                    }
                }
            }
        }
        return listOfAuctionListingsOfGivenPublication;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Listing> findAllByDirectSaleAndPublicationSampleIdIn(List<PublicationSampleId> listOfPubSampleIds) {

        if (listOfPubSampleIds == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }

        if (listOfPubSampleIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Listing> listOfListings = new ArrayList<>();

        for (Listing listing : listings) {
            if (listing.isListingLinkedToADirectSale()) {
                for(PublicationSampleId id: listOfPubSampleIds){
                    if(listing.containsPubSampleId(id)){
                        listOfListings.add(listing);
                        break;
                    }
                }
            }
        }
        return listOfListings;
    }
}