package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.ListingAuthorizationPolicy;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;import java.util.List;
import java.util.Objects;

/**
 * Application service responsible for {@link Listing} use cases.
 *
 * <p>Orchestrates interactions between the domain layer ({@link ListingFactory})
 * and the persistence layer ({@link IListingRepository}). All public methods
 * validate their inputs before delegating to domain or repository operations.</p>
 */
@Service
public class ListingService {

    private final IListingRepository listingRepository;
    private final ListingFactory listingFactory;
    private final IUserRepository userRepository;
    private final ListingAuthorizationPolicy policy;

    /**
     * Constructs a {@code ListingService} with the required collaborators.
     *
     * @param listingRepository the repository used to persist listings; must not be {@code null}
     * @param listingFactory    the factory used to create listing instances; must not be {@code null}
     */
    public ListingService(IListingRepository listingRepository, ListingFactory listingFactory, IUserRepository userRepository, ListingAuthorizationPolicy policy) {
        this.listingRepository = Objects.requireNonNull(listingRepository, "listingRepository must not be null");
        this.listingFactory = Objects.requireNonNull(listingFactory, "listingFactory must not be null");
        this.userRepository = Objects.requireNonNull(userRepository, "User repository cannot be null");
        this.policy = Objects.requireNonNull(policy, "Listing Authorization Policy cannot be null");
    }


    /**
     * Creates a new {@link Listing} from the supplied parameters and persists it.
     *
     * @param listingTitle     the display title of the listing; must not be {@code null}
     * @param itemsId          the publication sample identifiers associated with this listing; must not be {@code null}
     * @param saleType         the sale type (direct sale or auction); must not be {@code null}
     * @param price            the asking price; must not be {@code null}
     * @param shortDescription a brief description of the listing; must not be {@code null}
     * @param pictures         the pictures attached to the listing; must not be {@code null}
     * @return the persisted {@link Listing}
     */
    public Listing createAndSave(String listingTitle,
                                 List<PublicationSampleId> itemsId,
                                 SaleType saleType,
                                 Price price,
                                 String shortDescription,
                                 UserId userId,
                                 List<Picture> pictures
                                 ) throws AccessDeniedException {

        User user = userRepository.findByID(userId).orElseThrow(() -> new EntityNotFoundException("User not found id = " + userId));

        if (!policy.canCreateListing(user)) {
            throw new AccessDeniedException("You do not have permissions to create Listing");
        }

        Listing createdListing = listingFactory.create(listingTitle, itemsId, saleType, price, shortDescription, userId, pictures);

        return listingRepository.save(createdListing);
    }

    /**
     * Finds a listing by its identifier.
     *
     * @param listingId the identifier of the listing to find; must not be {@code null}
     * @return the listing associated with the given identifier
     * @throws EntityNotFoundException if no listing exists with the given identifier
     */
    public Listing findById(ListingId listingId) {
        return listingRepository.findById(listingId).orElseThrow(() -> new EntityNotFoundException("Listing not found"));
    }

    /**
     * Retrieves all listings.
     *
     * @return a list containing all existing listings; may be empty if no listings exist
     */
    public List<Listing> findAll() {
        return listingRepository.findAll();
    }

    /**
     * Retrieves all listings available for direct sale.
     *
     * @return a list containing all direct-sale listings; may be empty if none exist
     */
    public List<Listing> findAllDirectSale() {
        return listingRepository.findAllByDirectSale();
    }

    /**
     * Retrieves all listings available as auctions.
     *
     * @return a list containing all auction listings; may be empty if none exist
     */
    public List<Listing> findAllAuction() {
        return listingRepository.findAllByAuction();
    }

    // =========================================================================
    // Lookup — by associated identifiers
    // =========================================================================

    /**
     * Finds the identifiers of listings that contain any of the given publication sample identifiers.
     *
     * @param publicationSamplesIds the publication sample identifiers to search for; must not be {@code null}
     * @return a list of listing identifiers associated with the given publication samples;
     *         may be empty if no matching listings exist
     */
    public List<ListingId> findIdsByPublicationSampleIdIn(List<PublicationSampleId> publicationSamplesIds) {
        if (publicationSamplesIds == null || publicationSamplesIds.isEmpty()) {
            throw new IllegalArgumentException("publicationSamplesIds cannot be null");
        }
        return listingRepository.findIdsByPublicationSampleIdIn(publicationSamplesIds);
    }

    /**
     * Retrieves all auction listings associated with any of the given publication sample identifiers.
     *
     * @param publicationSampleIds the publication sample identifiers to search for; must not be {@code null}
     * @return a list of auction listings associated with the given publication samples;
     *         may be empty if no matching listings exist
     */
    public List<Listing> findByAuctionAndPublicationSampleIdIn(List<PublicationSampleId> publicationSampleIds) {
        if (publicationSampleIds == null || publicationSampleIds.isEmpty()) {
            throw new IllegalArgumentException("publicationSamplesIds cannot be nul or empty");
        }
        return listingRepository.findAllByAuctionAndPublicationSampleIdIn(publicationSampleIds);
    }

    /**
     * Retrieves all direct-sale listings associated with any of the given
     * publication sample identifiers.
     *
     * @param publicationSampleIds the publication sample identifiers to search for;
     *                             must not be {@code null}
     * @return a list of direct-sale listings associated with the given publication samples;
     *         may be empty if no matching listings exist
     */
    public List<Listing> findAllDirectSaleAndPublicationSampleIdIn(List<PublicationSampleId> publicationSampleIds) {
        if (publicationSampleIds == null || publicationSampleIds.isEmpty()) {
            throw new IllegalArgumentException("publicationSamplesIds cannot be nul or empty");
        }
        return listingRepository.findAllByDirectSaleAndPublicationSampleIdIn(publicationSampleIds);
    }


}