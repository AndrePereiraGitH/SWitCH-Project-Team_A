package TOPSECRET.dataInitializers;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data initializer that populates the database with sample {@link Auction} entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Auctions are built via {@link AuctionFactory} to respect
 * domain invariants, and persisted through {@link IAuctionRepository}.
 *
 * <p>The initializer seeds four sample auctions:
 * <ul>
 *   <li>A scheduled auction starting in the future with no buyout price.</li>
 *   <li>An active-bound auction starting in one minute with no buyout price.</li>
 *   <li>A scheduled auction starting in the future with a buyout price.</li>
 *   <li>An auction with pre-seeded bids, built via the reconstruction overload of
 *       {@link AuctionFactory#create(AuctionId, ListingId, LocalDateTime, LocalDateTime, Price, Price)}.
 *       This overload bypasses the start-date validation enforced by the creation constructor,
 *       allowing a past start date and enabling {@link Auction#placeBid} to succeed
 *       after {@link Auction#refreshStatus()} transitions the auction to {@code ACTIVE}.</li>
 * </ul>
 */
@Component
public class AuctionDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AuctionDataInitializer.class);

    private final IAuctionRepository auctionRepository;
    private final AuctionFactory auctionFactory;

    public AuctionDataInitializer(IAuctionRepository auctionRepository, AuctionFactory auctionFactory) {
        this.auctionRepository = auctionRepository;
        this.auctionFactory = auctionFactory;
    }

    // =========================================================================
    // ApplicationRunner
    // =========================================================================

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("[AuctionDataInitializer] Seeding auction data...");

        auctionRepository.save(buildScheduledAuction());
        auctionRepository.save(buildActiveAuction());
        auctionRepository.save(buildAuctionWithBuyout());
        auctionRepository.save(buildAuctionWithBids());

        log.info("[AuctionDataInitializer] Done — {} auctions seeded.", 4);
    }

    // =========================================================================
    // Sample Auctions
    // =========================================================================

    /**
     * A scheduled auction that starts in the future with no buyout price.
     */
    private Auction buildScheduledAuction() {
        ListingId listingId = new ListingId("listing-001");
        Price initialPrice = new Price(BigDecimal.valueOf(10.00));
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(7);

        return auctionFactory.create(listingId, start, end, initialPrice, null);
    }

    /**
     * An active auction that started in the past and ends in the future.
     */
    private Auction buildActiveAuction() {
        ListingId listingId = new ListingId("listing-002");
        Price initialPrice = new Price(BigDecimal.valueOf(25.00));
        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusDays(3);

        return auctionFactory.create(listingId, start, end, initialPrice, null);
    }

    /**
     * A scheduled auction with a buyout price set.
     */
    private Auction buildAuctionWithBuyout() {
        ListingId listingId = new ListingId("listing-003");
        Price initialPrice = new Price(BigDecimal.valueOf(50.00));
        Price buyoutPrice = new Price(BigDecimal.valueOf(150.00));
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(10);

        return auctionFactory.create(listingId, start, end, initialPrice, buyoutPrice);
    }

    /**
     * An auction with pre-seeded bids, reconstructed with a past start date.
     *
     * <p>Uses the reconstruction overload of
     * {@link AuctionFactory#create(AuctionId, ListingId, LocalDateTime, LocalDateTime, Price, Price)}
     * to bypass the start-date validation that prevents past dates in the creation constructor.
     * After construction, {@link Auction#refreshStatus()} is called to transition
     * the auction to {@code ACTIVE}, allowing bids to be placed.</p>
     */
    private Auction buildAuctionWithBids() throws Exception {
        ListingId listingId = new ListingId("listing-004");
        Price initialPrice = new Price(BigDecimal.valueOf(10.00));
        Price buyoutPrice = new Price(BigDecimal.valueOf(100.00));

        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(5);

        AuctionId auctionId = new AuctionId(UUID.randomUUID().toString());

        Auction auction = auctionFactory.create(auctionId, listingId, start, end, initialPrice, buyoutPrice);
        auction.refreshStatus();

        auction.placeBid(new UserId("user-001"), new Price(BigDecimal.valueOf(15.00)));
        auction.placeBid(new UserId("user-002"), new Price(BigDecimal.valueOf(20.00)));

        return auction;
    }
}