package TOPSECRET.dataInitializers;

import TOPSECRET.domain.Enum.ListingStatus;
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
        listingRepository.save(buildAuctionListingWithBids());
        listingRepository.save(buildAuctionListingToPostAuction());
        log.info("[ListingDataInitializer] Done — {} listings seeded.", 5);
    }

    /**
     * Direct sale of The Lord of the Rings (pub-sample-001, bd-001).
     */
    private Listing buildDirectSaleListing() {
        return listingFactory.create(
                new ListingId("listing-001"),
                "The Lord of the Rings - Paperback Edition",
                List.of(new PublicationSampleId("pub-sample-001")),
                SaleType.DIRECT_SALE,
                new Price(BigDecimal.valueOf(24.99)),
                "Paperback edition in poor condition.",
                new UserId("user-001"),
                List.of(new Picture("https://example.com/images/lotr.jpg")),
                LocalDate.now(),
                ListingStatus.ACTIVE);
    }

    /**
     * Auction bundle of Dom Quixote + Os Lusíadas (pub-sample-002 + pub-sample-003, bd-002 + bd-003).
     */
    private Listing buildAuctionListingMultipleItems() {
        return listingFactory.create(
                new ListingId("listing-002"),
                "Dom Quixote + Os Lusíadas Bundle",
                List.of(
                        new PublicationSampleId("pub-sample-002"),
                        new PublicationSampleId("pub-sample-003")
                ),
                SaleType.AUCTION,
                new Price(BigDecimal.valueOf(45.00)),
                "Collection of classic Portuguese and Spanish literature.",
                new UserId("user-002"),
                List.of(new Picture("https://example.com/images/bundle.jpg")),
                LocalDate.now(),
                ListingStatus.ACTIVE);
    }

    /**
     * Direct sale of Foundation collector's edition (pub-sample-004, bd-004).
     */
    private Listing buildDirectSaleListingWithPictures() {
        return listingFactory.create(
                new ListingId("listing-003"),
                "Foundation - Collector's Edition",
                List.of(new PublicationSampleId("pub-sample-004")),
                SaleType.DIRECT_SALE,
                new Price(BigDecimal.valueOf(89.99)),
                "Hardcover collector's edition in poor condition.",
                new UserId("user-003"),
                List.of(new Picture("https://example.com/images/foundation.jpg")),
                LocalDate.now(),
                ListingStatus.ACTIVE);
    }

    /**
     * Auction of Dom Quixote (pub-sample-002, bd-002). Used by AuctionDataInitializer.
     */
    private Listing buildAuctionListingWithBids() {
        return listingFactory.create(
                new ListingId("listing-004"),
                "Dom Quixote - First Edition Auction",
                List.of(new PublicationSampleId("pub-sample-002")),
                SaleType.AUCTION,
                new Price(BigDecimal.valueOf(10.00)),
                "Starting bid is low for this classic.",
                new UserId("user-002"),
                List.of(),
                LocalDate.now(),
                ListingStatus.ACTIVE
                );
    }

    /**
     * Auction of a second copy of The Lord of the Rings (pub-sample-005, bd-001).
     */
    private Listing buildAuctionListingToPostAuction() {
        return listingFactory.create(
                new ListingId("listing-005"),
                "The Lord of the Rings - Very Good Condition Auction",
                List.of(new PublicationSampleId("pub-sample-005")),
                SaleType.AUCTION,
                new Price(BigDecimal.valueOf(10.00)),
                "Very good condition copy.",
                new UserId("user-001"),
                List.of(new Picture("https://example.com/images/lotr-auction.jpg")),
                LocalDate.now(),
                ListingStatus.ACTIVE
        );
    }
}