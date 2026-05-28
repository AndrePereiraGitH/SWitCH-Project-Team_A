package TOPSECRET;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.ValueObjects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data initializer that populates the database with sample {@link Listing} entries on application startup.
 *
 * <p>Implements {@link ApplicationRunner} so it runs after the Spring context
 * is fully loaded. Listings are built via {@link ListingFactory} to respect
 * domain invariants, and persisted through {@link IListingRepository}.
 *
 * <p><b>Note:</b> This initializer always inserts on startup. It is intended
 * for development / demo environments only — disable or remove it for production.
 */
@Component
public class ListingDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ListingDataInitializer.class);

    private final IListingRepository listingRepository;
    private final ListingFactory listingFactory;

    public ListingDataInitializer(IListingRepository listingRepository,
                                  ListingFactory listingFactory) {
        this.listingRepository = listingRepository;
        this.listingFactory = listingFactory;
    }

    // =========================================================================
    // ApplicationRunner
    // =========================================================================

    @Override
    public void run(ApplicationArguments args) {
        log.info("[ListingDataInitializer] Seeding listing data...");

        listingRepository.save(buildDirectSaleListing());
        listingRepository.save(buildAuctionListingMultipleItems());
        listingRepository.save(buildDirectSaleListingWithPictures());

        log.info("[ListingDataInitializer] Done — {} listings seeded.", 3);
    }

    // =========================================================================
    // Sample Listings
    // =========================================================================

    /**
     * A simple direct-sale listing with a single book sample.
     */
    private Listing buildDirectSaleListing() {
        UserId seller = new UserId("user-001");

        PublicationSampleId pubId = new PublicationSampleId("pub-sample-001");

        Price price = new Price(BigDecimal.valueOf(24.99));

        List<Picture> pictures = List.of(
                new Picture("https://example.com/images/book-clean-code.jpg")
        );

        return listingFactory.create(
                "Clean Code by Robert C. Martin",
                List.of(pubId),
                SaleType.DIRECT_SALE,
                price,
                "Paperback edition in excellent condition with minimal signs of use.",
                seller,
                pictures
        );
    }

    /**
     * An auction listing bundling multiple magazine samples.
     */
    private Listing buildAuctionListingMultipleItems() {
        UserId seller = new UserId("user-002");

        List<PublicationSampleId> pubIds = List.of(
                new PublicationSampleId("pub-sample-002"),
                new PublicationSampleId("pub-sample-003")
        );

        Price price = new Price(BigDecimal.valueOf(45.00));

        List<Picture> pictures = List.of(
                new Picture("https://example.com/images/magazine-bundle-front.jpg"),
                new Picture("https://example.com/images/magazine-bundle-inside.jpg")
        );

        return listingFactory.create(
                "National Geographic Vintage Magazine Bundle",
                pubIds,
                SaleType.AUCTION,
                price,
                "Collection of rare National Geographic magazines from the 1980s in very good condition.",
                seller,
                pictures
        );
    }

    /**
     * A direct-sale listing with multiple pictures and a collector's edition book.
     */
    private Listing buildDirectSaleListingWithPictures() {
        UserId seller = new UserId("user-003");

        PublicationSampleId pubId = new PublicationSampleId("pub-sample-004");

        Price price = new Price(BigDecimal.valueOf(89.99));

        List<Picture> pictures = List.of(
                new Picture("https://example.com/images/lotr-front.jpg"),
                new Picture("https://example.com/images/lotr-side.jpg"),
                new Picture("https://example.com/images/lotr-open.jpg")
        );

        return listingFactory.create(
                "The Lord of the Rings Collector's Edition",
                List.of(pubId),
                SaleType.DIRECT_SALE,
                price,
                "Hardcover collector's edition with illustrated cover and original slipcase. Excellent condition.",
                seller,
                pictures
        );
    }
}