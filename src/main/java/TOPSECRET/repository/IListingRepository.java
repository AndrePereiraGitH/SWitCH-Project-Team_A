package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;


/**
 * Contract for managing {@link Listing} objects.
 * <p>
 * Defines operations for creating, storing, retrieving, and filtering listings
 * according to their sale type and associated publication metadata.
 * </p>
 */
public interface IListingRepository {


    /**
     * Creates a new listing and adds it to the repository if it does not already exist.
     *
     * @param listingTitle the title of the listing
     * @param items the publication samples associated with the listing
     * @param saleType the type of sale for the listing
     * @param price the price defined for the listing
     * @param shortDescription a short description of the listing
     * @param pictures the pictures associated with the listing
     * @return the created and stored listing
     * @throws InstanceAlreadyExistsException if an equivalent listing already exists
     */
    Listing createAndAdd(String listingTitle,
                         List<PublicationSample> items,
                         SaleType saleType,
                         Price price,
                         String shortDescription,
                         List<Picture> pictures) throws InstanceAlreadyExistsException;


    /**
     * Returns all publication samples in direct-sale listings that belong to the given genre.
     *
     * @param genre the genre used to filter publication samples
     * @return a list of publication samples in direct-sale listings with the given genre
     * @throws IllegalArgumentException if {@code genre} is {@code null}
     */
    List<PublicationSample> getListOfItemsOnDirectSaleOfAGivenGenre(Genre genre);

    /**
     * Returns all publication samples in direct-sale listings that are associated with the given author.
     *
     * @param author the author used to filter publication samples
     * @return a list of publication samples in direct-sale listings associated with the given author
     * @throws IllegalArgumentException if {@code author} is {@code null}
     */
    List<PublicationSample> getListOfItemsOnDirectSaleOfAGivenAuthor(Author author);

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
    List<Listing> getAllListings();

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
    List<Listing> findAllDirectSaleListings();

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
    List<Listing> findAllAuctionListings();


}
