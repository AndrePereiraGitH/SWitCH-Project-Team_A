package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

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
public class ListingRepositoryMem implements IListingRepository{

    private List<Listing> listings;
    private ListingFactory listingFactory;

    /**
     * Constructs an in-memory listing repository with the given listing factory.
     *
     * @param listingFactory the factory used to create {@link Listing} instances
     */
    public ListingRepositoryMem(ListingFactory listingFactory) {
        this.listings = new ArrayList<>();
        this.listingFactory = listingFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Listing createAndAdd(
            String listingTitle,
            List<PublicationSample> items,
            SaleType saleType,
            Price price,
            String shortDescription,
            List<Picture> pictures
    ) throws InstanceAlreadyExistsException {

        Listing newListing = listingFactory.create(listingTitle, items, saleType, price, shortDescription, pictures);

        if (isExistent(newListing))
            throw new InstanceAlreadyExistsException("Listing already exists.");

        listings.add(newListing);

        return newListing;
    }

    // for tests purpose
    void addListing(Listing listing) {
        listings.add(listing);
    }

    private boolean isExistent(Listing listing) {
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
        return listings;
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
}