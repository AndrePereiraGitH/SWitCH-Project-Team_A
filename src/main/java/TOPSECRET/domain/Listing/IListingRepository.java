package TOPSECRET.domain.Listing;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;


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
     * @param itemsId the publication samples IDs associated with the listing
     * @param saleType the type of sale for the listing
     * @param price the price defined for the listing
     * @param shortDescription a short description of the listing
     * @param pictures the pictures associated with the listing
     * @return the created and stored listing
     * @throws InstanceAlreadyExistsException if an equivalent listing already exists
     */
    Listing createAndAdd(String listingTitle,
                         List<PublicationSampleId> itemsId,
                         SaleType saleType,
                         Price price,
                         String shortDescription,
                         UserId sellerId,
                         List<Picture> pictures) throws InstanceAlreadyExistsException;


    /**
     * Returns all publication samples in direct-sale listings that belong to the given genre.
     *
     * @param genre the genre used to filter publication samples
     * @return a list of publication samples in direct-sale listings with the given genre
     * @throws IllegalArgumentException if {@code genre} is {@code null}
     */
//    List<PublicationSample> getListOfItemsOnDirectSaleOfAGivenGenre(Genre genre);


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

    /**
     * Retrieves an {@link Listing} instance by its identifier.
     * <p>
     * This method searches the in-memory list of stored listings and returns
     * the first element whose {@code id} matches the provided {@link ListingId}.
     * If no matching listing is found, an empty {@link Optional} is returned.
     * </p>
     *
     * @param id the identifier of the listing to retrieve; must not be {@code null}
     * @return an {@link Optional} containing the matching listing if found,
     *         or an empty {@link Optional} otherwise
     *
     * @throws IllegalArgumentException if {@code id} is {@code null}
     */
    Optional<Listing> findListingById(ListingId id);


    /**
     * Retrieves all {@link ListingId} that contain at least one of the given
     * {@link PublicationSampleId}.
     *
     * <p>This method searches the repository for all {@link Listing} instances
     * whose collection of item identifiers includes at least one of the provided
     * publication sample identifiers.</p>
     *
     * @param itemsId the list of publication sample identifiers used to filter listings;
     *                must not be {@code null}
     * @return a list of {@link ListingId} corresponding to listings that contain
     *         at least one of the specified items; may be empty if no matches are found
     *
     * @throws IllegalArgumentException if {@code itemsId} is {@code null}
     */
    List<ListingId> findAllListingIdByPublicationSampleId(List<PublicationSampleId> itemsId);


    /**
     * Returns all auction listings that contain at least one item matching
     * any of the provided publication sample IDs.
     *
     * <p>A listing is included in the result if it is linked to an auction
     * and contains at least one of the given {@link PublicationSampleId}s.
     * Each matching listing appears at most once in the returned list.</p>
     *
     * @param listOfPubSampleIds the list of {@link PublicationSampleId} to search for;
     *                           if {@code null} or empty, an empty list is returned
     * @return a list of {@link Listing} linked to an auction and matching at least one
     *         of the provided IDs; never {@code null}, may be empty
     */
    List<Listing> getAuctionListingsContaining(List<PublicationSampleId> listOfPubSampleIds);


    /**
     * Retrieves the identifiers of all listings that contain at least one of the
     * given publication sample identifiers.
     *
     * @param publicationSamplesIds the list of publication sample identifiers to search for
     * @return a list of listing identifiers for listings that contain at least one
     *         of the given publication sample identifiers; returns an empty list if
     *         no matching listings are found
     * @throws IllegalArgumentException if {@code publicationSamplesIds} is {@code null}
     */
    List<ListingId> findListingIdsByPublicationSampleIds(List<PublicationSampleId> publicationSamplesIds);


    /**
     * Returns all direct sale listings that contain at least one item matching
     * any of the provided publication sample IDs.
     *
     * <p>A listing is included in the result if it is linked to a direct sale
     * and contains at least one of the given {@link PublicationSampleId}s.
     * Each matching listing appears at most once in the returned list.</p>
     *
     * @param listOfPubSampleIds the list of {@link PublicationSampleId} to search for;
     *                           if {@code null} or empty, an empty list is returned
     * @return a list of {@link Listing} linked to a direct sale and matching at least one
     *         of the provided IDs; never {@code null}, may be empty
     */
    List<Listing> findDirectSaleListingsContaining(List<PublicationSampleId> listOfPubSampleIds);
}
