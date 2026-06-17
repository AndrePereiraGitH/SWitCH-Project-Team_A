package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.ListingAuthorizationPolicy;
import TOPSECRET.domain.Enum.ListingStatus;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;
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
    private final IPublicationSampleRepository publicationSampleRepository;
    private final ListingAuthorizationPolicy policy;

    /**
     * Constructs a {@code ListingService} with the required collaborators.
     *
     * @param listingRepository the repository used to persist listings; must not be {@code null}
     * @param listingFactory    the factory used to create listing instances; must not be {@code null}
     */
    public ListingService(IListingRepository listingRepository, ListingFactory listingFactory, IUserRepository userRepository, IPublicationSampleRepository publicationSampleRepository, ListingAuthorizationPolicy policy) {
        this.listingRepository = Objects.requireNonNull(listingRepository, "listingRepository must not be null");
        this.listingFactory = Objects.requireNonNull(listingFactory, "listingFactory must not be null");
        this.userRepository = Objects.requireNonNull(userRepository, "User repository cannot be null");
        this.publicationSampleRepository = publicationSampleRepository;
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
                                 UserId sellerId,
                                 List<Picture> pictures
                                 ) throws AccessDeniedException {

        User user = userRepository.findByID(sellerId).orElseThrow(() -> new EntityNotFoundException("User not found id = " + sellerId));

        if (!policy.canCreateListing(user)) {
            throw new AccessDeniedException("You do not have permissions to create Listing");
        }

        if (!verifyPublicationsOwner(itemsId, sellerId)) {
            throw new AccessDeniedException("You just have permissions to sell your own Publications");
        }

        Listing createdListing = listingFactory.create(listingTitle, itemsId, saleType, price, shortDescription, sellerId, pictures);

        return listingRepository.save(createdListing);
    }

    private boolean verifyPublicationsOwner(List<PublicationSampleId> itemIds, UserId sellerId) {

        for (PublicationSampleId itemId : itemIds) {
           PublicationSample pub = publicationSampleRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
           if (!pub.getOwnerID().equals(sellerId) ){
               return false;
            }
        }
        return true;
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
     * Retrieves all listings whose item list contains any of the given
     * publication sample identifiers, regardless of sale type.
     *
     * <p>Both direct-sale and auction listings are included in the result.
     * Use this method when filtering listings by a specific publication
     * across all sale types.</p>
     *
     * @param publicationSamplesIds the list of publication sample identifiers to filter by;
     *                              must not be {@code null} or empty
     * @return a list of {@link Listing} instances matching any of the given identifiers;
     *         never {@code null}, may be empty
     * @throws IllegalArgumentException if {@code publicationSamplesIds} is {@code null} or empty
     */
    public List<Listing> findByPublicationSampleIdIn(List<PublicationSampleId> publicationSamplesIds) {
        if (publicationSamplesIds == null || publicationSamplesIds.isEmpty()) {
            throw new IllegalArgumentException("publicationSamplesIds cannot be null");
        }

        List<Listing> allListingsWithSpecificPubIds = listingRepository.findByPublicationSampleIdIn(publicationSamplesIds);

        return allListingsWithSpecificPubIds;
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

    // =========================================================================
    // Business methods
    // =========================================================================

    /**
     * Marks the given {@link Listing} as sold and updates the status of all
     * associated {@link PublicationSample} instances to {@link PublicationStatus#SOLD}.
     *
     * <p>Triggered by a buyer purchasing the listing. The requesting user must not
     * be the seller of the listing and must be authorized to buy.</p>
     *
     * @param listingId the identifier of the listing to mark as sold; must not be {@code null}
     * @param buyerId   the identifier of the requesting user (the buyer); must not be {@code null}
     * @return the updated, persisted {@link Listing}
     * @throws EntityNotFoundException if no listing, user, or publication sample is found
     * @throws IllegalStateException   if the listing is already marked as sold
     * @throws AccessDeniedException   if the requesting user is the seller, or is not authorized to buy
     * @throws NullPointerException    if {@code listingId} or {@code buyerId} is {@code null}
     */
    @Transactional
    public Listing markListingAsSold(ListingId listingId, UserId buyerId) {

        Objects.requireNonNull(listingId, "listingId cannot be null");
        Objects.requireNonNull(buyerId, "buyerId cannot be null");

        Listing listing = listingRepository.findById(listingId).orElseThrow(EntityNotFoundException::new);

        if (listing.getListingStatus() == ListingStatus.SOLD) {
            throw new IllegalStateException("Listing is already sold");
        }

        if (listing.getSellerId().equals(buyerId)) {
            throw new AccessDeniedException("You cannot buy your own listing");
        }

        User user = userRepository.findByID(buyerId).orElseThrow(EntityNotFoundException::new);

        if (!policy.canBuyListing(user)) {
            throw new AccessDeniedException("You do not have permissions to buy this listing");
        }

        listing.markListingAsSold(buyerId);

        List<PublicationSampleId> itemIds = listing.getItemsId();

        for (PublicationSampleId itemId : itemIds) {
            PublicationSample item = publicationSampleRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);
            item.changePublicationStatus(PublicationStatus.SOLD);
            publicationSampleRepository.save(item);
        }

        return listingRepository.save(listing);
    }


}