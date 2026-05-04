package TOPSECRET.services;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;

/**
 * Application service responsible for managing {@link Auction} creation
 * and persistence.
 * <p>
 * This service coordinates the creation of {@link Auction} instances using
 * the {@link AuctionFactory} and persists them through the
 * {@link IAuctionRepository}.
 * </p>
 * <p>
 * It acts as a bridge between the domain layer and the persistence layer,
 * encapsulating the workflow required to create and store auctions.
 * </p>
 */
@Service
public class AuctionService {

    private final AuctionFactory auctionFactory;
    private final IAuctionRepository auctionRepo;

    /**
     * Constructs a new {@code AuctionService} with the required dependencies.
     *
     * @param auctionFactory the factory used to create {@link Auction} instances;
     *                       must not be {@code null}
     * @param auctionRepo the repository used to persist auctions;
     *                    must not be {@code null}
     *
     * @throws IllegalArgumentException if {@code auctionFactory} or
     *                                  {@code auctionRepo} is {@code null}
     */
    public AuctionService(AuctionFactory auctionFactory, IAuctionRepository auctionRepo) {

        if (auctionFactory == null) {
            throw new IllegalArgumentException("auctionFactory cannot be null");
        }
        if (auctionRepo == null) {
            throw new IllegalArgumentException("auctionRepo cannot be null");
        }

        this.auctionFactory = auctionFactory;
        this.auctionRepo = auctionRepo;
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
     *
     * @return the persisted {@link Auction} instance
     *
     * @throws IllegalArgumentException if any of the arguments are {@code null}
     *                                  (validation may be delegated to the factory)
     * @throws InstanceAlreadyExistsException if an equivalent auction already exists
     *                                        and cannot be persisted again
     */
    public Auction createAndSave(ListingId listingId, LocalDateTime auctionStartDateTime, LocalDateTime finalDate,
                          Price initialPrice, Price buyOutPrice) throws InstanceAlreadyExistsException {
        Auction auction = auctionFactory.create(listingId, auctionStartDateTime, finalDate, initialPrice, buyOutPrice);
        return auctionRepo.save(auction);
    }
}
