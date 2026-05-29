package TOPSECRET.dataInitializers;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.management.InstanceAlreadyExistsException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data initializer that populates the database with sample Auction
 * entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Auctions are built via {@link AuctionFactory}
 * to respect domain invariants, and persisted through
 * {@link IAuctionRepository}.
 *
 * <p><b>Note:</b> This initializer always inserts on startup. It is intended
 * for development / demo environments only — disable or remove it for production.
 */
@Component
public class AuctionDataInitializer implements ApplicationRunner {

    private static final Logger log =
            LoggerFactory.getLogger(AuctionDataInitializer.class);

    private final IAuctionRepository auctionRepository;
    private final AuctionFactory auctionFactory;

    public AuctionDataInitializer(
            IAuctionRepository auctionRepository,
            AuctionFactory auctionFactory) {

        this.auctionRepository = auctionRepository;
        this.auctionFactory = auctionFactory;
    }

    // =========================================================================
    // ApplicationRunner
    // =========================================================================

    @Override
    public void run(ApplicationArguments args) throws InstanceAlreadyExistsException {

        log.info("[AuctionDataInitializer] Seeding auctions...");

        auctionRepository.save(buildBookAuction());

        auctionRepository.save(buildMagazineAuction());

        auctionRepository.save(buildCollectorsEditionAuction());

        log.info("[AuctionDataInitializer] Done — {} auctions seeded.", 3);
    }

    // =========================================================================
    // Sample Auctions
    // =========================================================================

    /**
     * Auction for a programming book listing.
     */
    private Auction buildBookAuction() {

        ListingId listingId = new ListingId("listing-001");

        LocalDateTime start = LocalDateTime.now().plusDays(1);

        LocalDateTime end = start.plusDays(7);

        Price initialPrice = new Price(BigDecimal.valueOf(20.00));

        Price buyOutPrice = new Price(BigDecimal.valueOf(50.00));

        return auctionFactory.create(
                listingId,
                start,
                end,
                initialPrice,
                buyOutPrice
        );
    }

    /**
     * Auction for vintage magazines.
     */
    private Auction buildMagazineAuction() {

        ListingId listingId = new ListingId("listing-002");

        LocalDateTime start = LocalDateTime.now().plusHours(12);

        LocalDateTime end = start.plusDays(5);

        Price initialPrice = new Price(BigDecimal.valueOf(35.00));

        Price buyOutPrice = new Price(BigDecimal.valueOf(80.00));

        return auctionFactory.create(
                listingId,
                start,
                end,
                initialPrice,
                buyOutPrice
        );
    }

    /**
     * Auction for a collector's edition item.
     */
    private Auction buildCollectorsEditionAuction() {

        ListingId listingId = new ListingId("listing-003");

        LocalDateTime start = LocalDateTime.now().plusDays(2);

        LocalDateTime end = start.plusDays(10);

        Price initialPrice = new Price(BigDecimal.valueOf(100.00));

        Price buyOutPrice = new Price(BigDecimal.valueOf(250.00));

        return auctionFactory.create(
                listingId,
                start,
                end,
                initialPrice,
                buyOutPrice
        );
    }
}