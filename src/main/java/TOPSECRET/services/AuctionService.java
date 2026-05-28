package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AuctionAuthorizationPolicy;
import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.Bid;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Application service responsible for managing {@link Auction} lifecycle operations,
 * including creation, persistence, and retrieval.
 *
 * <p>This service coordinates the creation of {@link Auction} instances using
 * the {@link AuctionFactory} and persists them through the {@link IAuctionRepository}.
 * It also exposes query methods to retrieve auctions by different criteria.</p>
 *
 * <p>It acts as a bridge between the domain layer and the persistence layer,
 * encapsulating the workflow required to create, store, and retrieve auctions.</p>
 */
@Service
public class AuctionService {

    private final AuctionFactory auctionFactory;
    private final IAuctionRepository auctionRepo;
    private final IListingRepository listingRepo;
    private final IUserRepository userRepo;
    private final AuctionAuthorizationPolicy policy;

    /**
     * Constructs a new {@code AuctionService} with the required dependencies.
     *
     * @param auctionFactory the factory used to create {@link Auction} instances;
     *                       must not be {@code null}
     * @param auctionRepo the repository used to persist auctions;
     *                    must not be {@code null}
     * @param listingRepo    the repository used to retrieve listings for business
     *                       rule validation; must not be {@code null}
     * @param userRepo        the repository used to retrieve users for authorization;
     *                        must not be {@code null}
     * @param policy          the authorization policy for auction operations;
     *                        must not be {@code null}
     * @throws IllegalArgumentException if any of the dependencies is {@code null}
     */
    public AuctionService(AuctionFactory auctionFactory, IAuctionRepository auctionRepo, IListingRepository listingRepo,
                          IUserRepository userRepo, AuctionAuthorizationPolicy policy) {

        if (auctionFactory == null) {
            throw new IllegalArgumentException("AuctionFactory cannot be null");
        }
        if (auctionRepo == null) {
            throw new IllegalArgumentException("AuctionRepository cannot be null");
        }
        if (listingRepo == null) {
            throw new IllegalArgumentException("ListingRepository cannot be null");
        }
        if (userRepo == null) {
            throw new IllegalArgumentException("UserRepository cannot be null");
        }
        if (policy == null) {
            throw new IllegalArgumentException("AuctionAuthorizationPolicy cannot be null");
        }

        this.auctionFactory = auctionFactory;
        this.auctionRepo = auctionRepo;
        this.listingRepo = listingRepo;
        this.userRepo = userRepo;
        this.policy = policy;
    }


    /**
     * Creates a new {@link Auction} and persists it in the repository.
     * <p>
     * This method delegates the creation of the auction to the
     * {@link AuctionFactory} and then stores the resulting instance
     * using the {@link IAuctionRepository}.
     * </p>
     *
     * @param listingId the identifier of the listing associated with the auction; must not be {@code null}
     * @param auctionStartDateTime the date and time when the auction starts; must not be {@code null}
     * @param finalDate the date and time when the auction ends; must not be {@code null}
     * @param initialPrice the starting price of the auction; must not be {@code null}
     * @param buyOutPrice the buyout price of the auction; must not be {@code null}
     * @param userId the identifier of the user requesting the operation; must not be {@code null}
     *
     * @return the persisted {@link Auction} instance
     *
     * @throws IllegalArgumentException if any of the arguments are {@code null}
     *                                  (validation may be delegated to the factory)
     * @throws InstanceAlreadyExistsException if an equivalent auction already exists
     *                                        and cannot be persisted again
     * @throws AccessDeniedException if the user does not have permission to create an auction
     * @throws InstanceNotFoundException if the user or listing does not exist
     */
    public Auction createAndSave(ListingId listingId, LocalDateTime auctionStartDateTime, LocalDateTime finalDate,
                                 Price initialPrice, Price buyOutPrice, UserId userId)
            throws InstanceAlreadyExistsException, InstanceNotFoundException, AccessDeniedException {

        User user = userRepo.findByID(userId)
                .orElseThrow(() -> new InstanceNotFoundException("User with ID " + userId + " not found"));
        if(!policy.canCreateAuction(user)){
            throw new AccessDeniedException("You do not have permission to create an auction");
        }

        Listing listing = listingRepo.findById(listingId)
                .orElseThrow(() -> new InstanceNotFoundException("Listing with ID " + listingId + " not found"));

        if (!listing.getSaleType().equals(SaleType.AUCTION))
            throw new IllegalArgumentException("Listing is not of sale type AUCTION");

        Auction auction = auctionFactory.create(listingId, auctionStartDateTime, finalDate, initialPrice, buyOutPrice);

        return auctionRepo.save(auction);
    }

    /**
     * Places a new {@link Bid} on an existing {@link Auction}.
     *
     * <p>Validates that the auction and its associated listing exist, and that
     * the bidder is not the seller of the listing. Delegates bidding rules
     * to the {@link Auction} aggregate and persists the updated state.</p>
     *
     * @param auctionId the identifier of the auction to bid on; must not be {@code null}
     * @param bidderId  the identifier of the user placing the bid; must not be {@code null}
     * @param value     the monetary value of the bid; must not be {@code null}
     * @return the newly created {@link Bid}
     * @throws InstanceNotFoundException if the auction or listing does not exist
     * @throws IllegalArgumentException  if the bidder is the seller of the listing,
     *                                   or if the bid violates auction bidding rules
     */
    public Bid placeBid(AuctionId auctionId, UserId bidderId, Price value)
            throws InstanceNotFoundException, AccessDeniedException {

        User user = userRepo.findByID(bidderId)
                .orElseThrow(() -> new InstanceNotFoundException("User with ID " + bidderId + " not found"));
        if(!policy.canPlaceBid(user)){
            throw new AccessDeniedException("You do not have permission to place a bid");
        }

        Auction auction = auctionRepo.findById(auctionId)
                .orElseThrow(() -> new InstanceNotFoundException("Auction with ID " + auctionId + " not found"));

        Listing listing = listingRepo.findById(auction.getListingId())
                .orElseThrow(() -> new InstanceNotFoundException("Listing with ID " + auction.getListingId() + " not found"));
        if (bidderId.equals(listing.getSellerId()))
            throw new IllegalArgumentException("Seller cannot bid on their own auction");

        Bid bid = auction.placeBid(bidderId, value);

        auctionRepo.update(auction);

        return bid;
    }

    /**
     * Retrieves all persisted {@link Auction} instances.
     *
     * @return an unmodifiable list of all auctions; empty if none exist
     */
    public List<Auction> findAll() {
        return auctionRepo.findAll();
    }

    /**
     * Retrieves an {@link Auction} by its unique identifier.
     *
     * @param auctionId the unique identifier of the auction to retrieve;
     *                  must not be {@code null}
     * @return the corresponding {@link Auction}
     * @throws InstanceNotFoundException if no auction exists with the given {@code auctionId}
     */
    public Auction findById(AuctionId auctionId) throws InstanceNotFoundException {
        return auctionRepo.findById(auctionId)
                .orElseThrow(() -> new InstanceNotFoundException("Auction with ID " + auctionId + " not found"));
    }

    /**
     * Retrieves all auctions associated with any of the given listing identifiers.
     *
     * @param listingIds the identifiers of the listings to search auctions for;
     *                   must not be {@code null}
     * @return a list of auctions associated with the given listings;
     *         empty if none are found
     * @throws NullPointerException if {@code listingIds} is {@code null}
     */
    public List<Auction> findByListingIdIn(List<ListingId> listingIds) {
        Objects.requireNonNull(listingIds, "ListingIds can't be null");
        return auctionRepo.findByListingIdIn(listingIds);
    }

    /**
     * Retrieves all auctions associated with any of the given listing identifiers,
     * sorted in ascending order by auction end date.
     *
     * @param listingIds the identifiers of the listings to search auctions for;
     *                   must not be {@code null}
     * @return a list of auctions sorted by auction end date ascending;
     *         empty if none are found
     * @throws NullPointerException if {@code listingIds} is {@code null}
     */
    public List<Auction> findByListingIdInOrderByAuctionDeadlineAsc(List<ListingId> listingIds) {
        Objects.requireNonNull(listingIds, "ListingIds can't be null");
        return auctionRepo.findByListingIdInOrderByAuctionDeadlineAsc(listingIds);
    }
}
