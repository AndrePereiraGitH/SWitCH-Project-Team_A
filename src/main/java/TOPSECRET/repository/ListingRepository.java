package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.SaleType;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

public class ListingRepository {

    private List<Listing> listings;
    private ListingFactory listingFactory;

    public ListingRepository() {
        this.listings = new ArrayList<>();
        this.listingFactory = new ListingFactory();
    }

    public ListingRepository(ListingFactory listingFactory) {
        this.listings = new ArrayList<>();
        this.listingFactory = listingFactory;
    }

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
    public List<PublicationSample> getListOfItemsOnDirectSaleOfAGivenGenre(Genre genre) {

        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }

        List<PublicationSample> listOfPublicationSample = new ArrayList<>();

        for (Listing listing : listings) {
            if (listing.getSaleType() == SaleType.DIRECT_SALE) {
                for (PublicationSample PublicationSample : listing.getItems()) {
                    if (PublicationSample.hasGenre(genre)) {
                        listOfPublicationSample.add(PublicationSample);
                    }
                }
            }
        }

        return listOfPublicationSample;
    }

    public List<PublicationSample> getListOfItemsOnDirectSaleofAGivenAuthor(Author author) {

        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }

        List<PublicationSample> listOfPublicationSample = new ArrayList<>();

        for (Listing listing : listings) {
            if (listing.getSaleType() == SaleType.DIRECT_SALE) {
                for (PublicationSample PublicationSample : listing.getItems()) {
                    if (PublicationSample.getBibliographicalData().getAuthors().contains(author)) {
                        listOfPublicationSample.add(PublicationSample);
                    }
                }
            }
        }

        return listOfPublicationSample;
    }

    /**
     * Returns the complete list of listings stored in the repository.
     *
     * <p>The returned list reflects the repository's current internal state and
     * contains all listings that have been previously added. Modifications to the
     * returned list will affect the repository's internal collection, as no
     * defensive copy is created.</p>
     *
     * @return the internal list of all stored listings; never {@code null}
     */

    public List<Listing> getAllListings() {
        return listings;
    }

    /**
     * Returns all listings that are marked as direct-sale listings.
     *
     * <p>This method scans the repository's internal collection of listings and
     * selects only those whose {@link Listing#getSaleType()} is equal to
     * {@link SaleType#DIRECT_SALE}. The returned list is a new collection and
     * does not expose or modify the repository's internal state.</p>
     *
     * @return a list containing all listings with sale type {@code DIRECT_SALE};
     *         never {@code null}, but may be empty if no such listings exist
     */

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
     * Returns all listings that are marked as auction listings.
     *
     * <p>This method scans the repository's internal collection of listings and
     * selects only those whose {@link Listing#getSaleType()} is equal to
     * {@link SaleType#AUCTION}. The returned list is a new collection and
     * does not expose or modify the repository's internal state.</p>
     *
     * @return a list containing all listings with sale type {@code AUCTION};
     *         never {@code null}, but may be empty if no such listings exist
     */

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