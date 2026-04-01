package TOPSECRET.domain.Auction;

import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionFactoryTest {

    @Test
    void create_returnsNewConstructedAuctionInstance() {

        // Arrange
        ListingId listingId = new ListingId("listingId");
        LocalDateTime startDate = LocalDateTime.now().plusMinutes(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(5);
        Price initialPrice = new Price(new BigDecimal(10.00));
        Price buyoutPrice = new Price(new BigDecimal(100.00));


        try (MockedConstruction<Auction> mocked =
                     mockConstruction(Auction.class, (mock, context) -> {
                     })) {

            AuctionFactory auctionFactory = new AuctionFactory();

            // Act
            Auction auction = auctionFactory.create(listingId, startDate, endDate, initialPrice, buyoutPrice);

            // Assert
            assertNotNull(auction);
            assertEquals(1, mocked.constructed().size());

            Auction constructedAuction = mocked.constructed().get(0);
            assertSame(auction, constructedAuction);
        }
    }

    @Test
    void createWithClock_returnsNewConstructedAuctionInstance() {

        // Arrange
        ListingId listingId = new ListingId("listingId");
        LocalDateTime startDate = LocalDateTime.now().plusMinutes(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(5);
        Price initialPrice = new Price(new BigDecimal(10.00));
        Price buyoutPrice = new Price(new BigDecimal(100.00));
        Clock clock = mock(Clock.class);


        try (MockedConstruction<Auction> mocked =
                     mockConstruction(Auction.class, (mock, context) -> {
                     })) {

            AuctionFactory auctionFactory = new AuctionFactory();

            // Act
            Auction auction = auctionFactory.create(listingId, startDate, endDate, initialPrice, buyoutPrice, clock);

            // Assert
            assertNotNull(auction);
            assertEquals(1, mocked.constructed().size());

            Auction constructedAuction = mocked.constructed().get(0);
            assertSame(auction, constructedAuction);
        }
    }
}