package TOPSECRET.domain.Auction;

import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;

/**
 * Contract for managing {@link Auction} objects.
 * <p>
 * Defines operations for creating, retrieving, and verifying auctions.
 * </p>
 */
public interface IAuctionRepository {

    /**
     * Persists the given {@link Auction} in the repository.
     * <p>
     * If the auction does not already exist, it will be created.
     * If it already exists (based on its identifier), the existing record
     * may be updated with the new state.
     * </p>
     *
     * @param auction the auction to be persisted; must not be {@code null}
     * @return the persisted {@link Auction} instance, which may include
     *         generated or updated fields (e.g., identifiers or timestamps)
     *
     * @throws IllegalArgumentException if {@code auction} is {@code null}
     * @throws InstanceAlreadyExistsException if the repository enforces uniqueness
     *         and an equivalent auction already exists and cannot be updated
     */
    Auction save(Auction auction) throws InstanceAlreadyExistsException;

    /**
     * Returns the complete list of available auctions.
     *
     * @return a copy of the list containing all auctions
     */
    List<Auction> findAll();

    /**
     * Retrieves an {@link Auction} instance by its identifier.
     * <p>
     * This method searches the list of stored auctions and returns
     * the first element whose {@code auctionId()} matches the provided {@link AuctionId}.
     * If no matching listing is found, an empty {@link Optional} is returned.
     * </p>
     *
     * @param id the identifier of the listing to retrieve; must not be {@code null}
     * @return an {@link Optional} containing the matching auction if found,
     *         or an empty {@link Optional} otherwise
     *
     * @throws IllegalArgumentException if {@code auctionId} is {@code null}
     */
    Optional<Auction> findById(AuctionId id);

    /**
     * Retrieves all {@link Auction} instances associated with the given
     * {@link ListingId}.
     *
     * <p>This method searches the repository for auctions whose listing identifier
     * matches any of the provided listing identifiers.</p>
     *
     * @param listingId the list of listing identifiers used to filter auctions;
     *                   must not be {@code null}
     * @return a list of {@link Auction} associated with the given listings;
     *         may be empty if no matches are found
     *
     * @throws IllegalArgumentException if {@code listingIds} is {@code null}
     */
//    List<Auction> getListOfItemsOnAuctionGivenGenre(List<ListingId> listingId);
    // This method seems similar to the following one, and the name isn't descriptive.

    /**
     * Retrieves all {@link Auction} instances whose {@link ListingId}
     * matches any of the provided listing identifiers.
     * <p>
     * This method filters the stored auctions and returns those whose
     * {@code getListingId()} value is contained in the given list.
     * </p>
     *
     * @param listingIds the list of listing identifiers used for filtering;
     *                   must not be {@code null}
     * @return a list of matching {@link Auction} instances; may be empty
     *         if no auctions match the provided identifiers
     *
     * @throws IllegalArgumentException if {@code listingIds} is {@code null}
     */
    List<Auction> findByListingId(List<ListingId> listingIds);
}
