package TOPSECRET.repository;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public Listing createAndAdd(
            String listingTitle,
            List<PublicationSampleId> itemsId,
            SaleType saleType,
            Price price,
            String shortDescription,
            UserId sellerId,
            List<Picture> pictures
    ) throws InstanceAlreadyExistsException {

        Listing newListing = listingFactory.create(listingTitle, itemsId, saleType, price, shortDescription, sellerId, pictures);

        if (existsById(newListing))
            throw new InstanceAlreadyExistsException("Listing already exists.");

        listings.add(newListing);

        return newListing;
    }

    private boolean existsById(Listing listing) {
        return listings.contains(listing);
    }

  // US021
    /**
     * {@inheritDoc}
     */
//    @Override
//    public List<PublicationSample> getListOfItemsOnDirectSaleOfAGivenGenre(Genre genre) {
//
//        if (genre == null) {
//            throw new IllegalArgumentException("Genre cannot be null");
//        }
//
//        List<PublicationSample> listOfPublicationSample = new ArrayList<>();
//
//        for (Listing listing : listings) {
//            if (listing.getSaleType() == SaleType.DIRECT_SALE) {
//                for (PublicationSample PublicationSample : listing.getItems()) {
//                    if (PublicationSample.hasGenre(genre)) {
//                        listOfPublicationSample.add(PublicationSample);
//                    }
//                }
//            }
//        }
//
//        return listOfPublicationSample;
//    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public List<PublicationSample> getListOfItemsOnDirectSaleOfAGivenAuthor(Author author) {
//
//        if (author == null) {
//            throw new IllegalArgumentException("Author cannot be null");
//        }
//
//        List<PublicationSample> listOfPublicationSample = new ArrayList<>();
//
//        for (Listing listing : listings) {
//            if (listing.getSaleType() == SaleType.DIRECT_SALE) {
//                for (PublicationSample PublicationSample : listing.getItems()) {
//                    if (PublicationSample.getBibliographicalData().getAuthors().contains(author)) {
//                        listOfPublicationSample.add(PublicationSample);
//                    }
//                }
//            }
//        }
//
//        return listOfPublicationSample;
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Listing> getAllListings() {
        return List.copyOf(listings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Listing> findAllDirectSaleListings() {

        List<Listing> dsListings = new ArrayList<>();
        for (Listing listing : listings) {
            if (listing.getSaleType() == SaleType.DIRECT_SALE) {
                dsListings.add(listing);
            }
        }
        return dsListings;
    }

    @Override
    public List<ListingId> findAllListingId(List<PublicationSampleId> itemsId) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Listing> findAllAuctionListings() {

        List<Listing> auctionListings = new ArrayList<>();
        for (Listing listing : listings) {
            if (listing.getSaleType() == SaleType.AUCTION) {
                auctionListings.add(listing);
            }
        }
        return auctionListings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Listing> findListingById(ListingId id) {
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
    public List<Listing> getAuctionListingsContaining(List<PublicationSampleId> listOfPubSampleIds) {

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
     *{@inheritDoc}
     */
    @Override
    public List<ListingId> findListingIdsByPublicationSampleIds(List<PublicationSampleId> publicationSamplesIds) {

        if (publicationSamplesIds == null) {
            throw new IllegalArgumentException("List of PublicationSamplesId cannot be null.");
        }

        List<ListingId> listingIdsWithSamples = listings.stream()
                .filter(listing -> listing.get_itemsId()
                        .stream()
                        .anyMatch(publicationSamplesIds::contains))
                .map(Listing::id)
                .toList();

        return listingIdsWithSamples;
    }
}