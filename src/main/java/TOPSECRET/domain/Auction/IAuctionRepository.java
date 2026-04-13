package TOPSECRET.domain.Auction;

import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;
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
     * Creates a new {@link Auction} and adds it to the repository if no auction
     * already exists for the given listing.
     *
     * <p>This method delegates all invariant validation to the {@link Auction}
     * constructor. If an auction for the given listing already exists, the method
     * does not create a new instance and instead throws an exception.</p>
     *
     * @param listingId the identifier of the listing associated with the auction (must not be {@code null})
     * @param initialDate the auction start date and time (must not be {@code null})
     * @param finalDate the auction end date and time (must not be {@code null})
     * @param initialPrice the initial price of the auction (must not be {@code null})
     * @param buyoutPrice the buyout price that immediately closes the auction if matched (can be {@code null})
     *
     * @return the newly created {@link Auction}
     *
     * @throws IllegalArgumentException if any parameter violates the invariants of {@link Auction}
     * @throws InstanceAlreadyExistsException if an auction for the given listing already exists
     */

    Auction createAndAdd(ListingId listingId, LocalDateTime initialDate, LocalDateTime finalDate, Price initialPrice, Price buyoutPrice) throws InstanceAlreadyExistsException;

    /**
     * Returns the complete list of available auctions.
     *
     * @return a copy of the list containing all auctions
     */
    List<Auction> getAllAuctions();

    /**
     * Retrieves an {@link Auction} instance by its identifier.
     * <p>
     * This method searches the in-memory list of stored auctions and returns
     * the first element whose {@code id()} matches the provided {@link AuctionId}.
     * If no matching listing is found, an empty {@link Optional} is returned.
     * </p>
     *
     * @param id the identifier of the listing to retrieve; must not be {@code null}
     * @return an {@link Optional} containing the matching auction if found,
     *         or an empty {@link Optional} otherwise
     *
     * @throws IllegalArgumentException if {@code id} is {@code null}
     */
    Optional<Auction> findAuctionById(AuctionId id);

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
    List<Auction> getListOfItemsOnAuctionGivenGenre(List<ListingId> listingId);

    /**
     * Returns the list of auctions containing at least one item whose publication
     * is associated with the given author.
     * Retrieves all auctions whose listing identifier is contained in the given list.
     *
     * @param listingIds the list of listing identifiers to search for
     * @return a list of auctions associated with the given listing identifiers;
     *         returns an empty list if no matching auctions are found
     * @throws IllegalArgumentException if {@code listingIds} is {@code null}
     */
    List<Auction> findAuctionsByListingId(List<ListingId> listingIds);

}
