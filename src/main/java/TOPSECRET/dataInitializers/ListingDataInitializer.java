package TOPSECRET.dataInitializers;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.ValueObjects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@Order(4)
public class ListingDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ListingDataInitializer.class);

    private final IListingRepository listingRepository;
    private final ListingFactory listingFactory;

    public ListingDataInitializer(IListingRepository listingRepository, ListingFactory listingFactory) {
        this.listingRepository = listingRepository;
        this.listingFactory = listingFactory;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("[ListingDataInitializer] Seeding listing data...");
        listingRepository.save(buildDirectSaleListing());
        listingRepository.save(buildAuctionListingMultipleItems());
        listingRepository.save(buildDirectSaleListingWithPictures());
        listingRepository.save(buildAuctionListingWithBids()); // Added to support AuctionDataInitializer
        log.info("[ListingDataInitializer] Done — {} listings seeded.", 4);
    }

    private Listing buildDirectSaleListing() {
        // ... (Keep existing implementation unchanged)
        return listingFactory.create(new ListingId("listing-001"), "Clean Code by Robert C. Martin",
                List.of(new PublicationSampleId("pub-sample-001")), SaleType.DIRECT_SALE,
                new Price(BigDecimal.valueOf(24.99)), "Paperback edition...", new UserId("user-001"),
                List.of(new Picture("https://example.com/images/book-clean-code.jpg")), LocalDate.now());
    }

    private Listing buildAuctionListingMultipleItems() {
        // ... (Keep existing implementation unchanged)
        return listingFactory.create(new ListingId("listing-002"), "National Geographic Vintage Magazine Bundle",
                List.of(new PublicationSampleId("pub-sample-002"), new PublicationSampleId("pub-sample-003")),
                SaleType.AUCTION, new Price(BigDecimal.valueOf(45.00)), "Collection of rare...", new UserId("user-002"),
                List.of(new Picture("https://example.com/images/magazine-bundle-front.jpg")), LocalDate.now());
    }

    private Listing buildDirectSaleListingWithPictures() {
        // ... (Keep existing implementation unchanged)
        return listingFactory.create(new ListingId("listing-003"), "The Lord of the Rings Collector's Edition",
                List.of(new PublicationSampleId("pub-sample-004")), SaleType.DIRECT_SALE,
                new Price(BigDecimal.valueOf(89.99)), "Hardcover collector's...", new UserId("user-003"),
                List.of(new Picture("https://example.com/images/lotr-front.jpg")), LocalDate.now());
    }

    /**
     * An auction listing to support buildAuctionWithBids() in AuctionDataInitializer.
     */
    private Listing buildAuctionListingWithBids() {
        ListingId id = new ListingId("listing-004");
        UserId seller = new UserId("user-002");
        PublicationSampleId pubId = new PublicationSampleId("pub-sample-002"); // Reusing sample 2
        Price price = new Price(BigDecimal.valueOf(10.00));

        return listingFactory.create(
                id,
                "Foundation First Edition (Auction)",
                List.of(pubId),
                SaleType.AUCTION,
                price,
                "Starting bid is low for this classic.",
                seller,
                List.of(),
                LocalDate.now()
        );
    }
}