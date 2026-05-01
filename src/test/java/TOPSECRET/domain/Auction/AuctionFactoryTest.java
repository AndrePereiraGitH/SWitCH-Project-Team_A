package TOPSECRET.domain.Auction;

import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuctionFactory}.
 * <p>
 * This test class verifies that the factory correctly instantiates
 * {@link Auction} and {@link Bid} objects through its various creation
 * and reconstruction methods.
 * </p>
 * <p>
 * The tests ensure that the factory delegates object creation to the
 * appropriate constructors and returns the expected instances.
 * </p>
 */
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

    @Test
    void create_withAuctionId_shouldConstructAuction() {
        // Arrange
        AuctionId auctionId = mock(AuctionId.class);
        ListingId listingId = mock(ListingId.class);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5);
        Price initialPrice = new Price(BigDecimal.valueOf(10));
        Price buyoutPrice = new Price(BigDecimal.valueOf(100));

        try (MockedConstruction<Auction> mocked =
                     mockConstruction(Auction.class, (mock, context) -> {
                     })) {

            AuctionFactory factory = new AuctionFactory();

            // Act
            Auction auction = factory.create(auctionId, listingId, startDate, endDate, initialPrice, buyoutPrice);

            // Assert
            assertNotNull(auction);
            assertEquals(1, mocked.constructed().size());
            assertSame(auction, mocked.constructed().get(0));
        }
    }
}