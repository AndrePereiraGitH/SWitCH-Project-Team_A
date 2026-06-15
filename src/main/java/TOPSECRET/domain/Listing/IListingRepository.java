package TOPSECRET.domain.Listing;

import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface responsible for managing {@link Listing} aggregates.
 *
 * <p>Provides operations grouped into three categories:</p>
 * <ul>
 *   <li><b>Persistence</b> — low-level save operations.</li>
 *   <li><b>Lookup</b> — queries by identity, sale type, or associated identifiers.</li>
 *   <li><b>Domain Operations</b> — filtered queries combining multiple domain criteria.</li>
 * </ul>
 */
public interface IListingRepository {

    // =========================================================================
    // Persistence
    // =========================================================================

    /**
     * Saves the given {@link Listing} to the repository.
     *
     * @param listing the listing to save; must not be {@code null}
     * @return the saved {@link Listing}
     * @throws IllegalArgumentException if the listing already exists in the repository
     */
    Listing save(Listing listing);


    // =========================================================================
    // Lookup — by identity
    // =========================================================================

    /**
     * Finds a {@link Listing} by its unique identifier.
     *
     * @param id the ID to search for; must not be {@code null}
     * @return an {@link Optional} containing the matching listing, or empty if none found
     */
    Optional<Listing> findById(ListingId id);

    /**
     * Retrieves all {@link Listing} instances stored in the repository.
     *
     * @return a list of all listings; never {@code null}, but may be empty
     */
    List<Listing> findAll();


    // =========================================================================
    // Lookup — by sale type
    // =========================================================================

    /**
     * Retrieves all listings linked to a direct sale.
     *
     * @return a list of direct-sale {@link Listing}s; never {@code null}, may be empty
     */
    List<Listing> findAllByDirectSale();

    /**
     * Retrieves all listings linked to an auction.
     *
     * @return a list of auction {@link Listing}s; never {@code null}, may be empty
     */
    List<Listing> findAllByAuction();


    // =========================================================================
    // Lookup — by associated identifiers
    // =========================================================================

    /**
     * Retrieves all listing IDs that contain at least one of the given publication sample IDs.
     *
     * @param publicationSampleIds the list of publication sample identifiers; must not be {@code null}
     * @return a list of matching {@link ListingId}s; never {@code null}, may be empty
     */
    List<ListingId> findIdsByPublicationSampleIdIn(List<PublicationSampleId> publicationSampleIds);

    /**
     * Retrieves all listings that contain at least one of the given publication sample IDs.
     *
     * @param publicationSampleIds the list of publication sample identifiers; must not be {@code null}
     * @return a list of matching {@link Listing}s; never {@code null}, may be empty
     */
    List<Listing> findByPublicationSampleIdIn(List<PublicationSampleId> publicationSampleIds);


    // =========================================================================
    // Domain Operations
    // =========================================================================

    /**
     * Retrieves all auction listings that contain at least one of the given publication sample IDs.
     *
     * @param publicationSampleIds the list of publication sample identifiers to match against;
     *                             must not be {@code null}
     * @return a list of auction {@link Listing}s containing at least one matching ID;
     *         never {@code null}, may be empty
     */
    List<Listing> findAllByAuctionAndPublicationSampleIdIn(List<PublicationSampleId> publicationSampleIds);

    /**
     * Retrieves all direct-sale listings that contain at least one of the given publication sample IDs.
     *
     * @param publicationSampleIds the list of publication sample identifiers to match against;
     *                             must not be {@code null}
     * @return a list of direct-sale {@link Listing}s containing at least one matching ID;
     *         never {@code null}, may be empty
     */
    List<Listing> findAllByDirectSaleAndPublicationSampleIdIn(List<PublicationSampleId> publicationSampleIds);
}