package TOPSECRET.domain;

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

        Listing listingDouble = mock(Listing.class);
        LocalDateTime startDate = LocalDateTime.now().plusMinutes(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(5);
        Price buyoutDouble = mockPrice("150");


        try (MockedConstruction<Auction> mocked =
                     mockConstruction(Auction.class, (mock, context) -> {
                     })) {

            AuctionFactory auctionFactory = new AuctionFactory();

            // act
            Auction auction = auctionFactory.create(listingDouble, startDate, endDate, buyoutDouble);

            // assert
            assertNotNull(auction);
            assertEquals(1, mocked.constructed().size());

            Auction constructedAuction = mocked.constructed().get(0);
            assertSame(auction, constructedAuction);
        }


    }

    @Test
    void createWithClock_returnsNewConstructedAuctionInstance() {

        Listing listingDouble = mock(Listing.class);
        LocalDateTime startDate = LocalDateTime.now().plusMinutes(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(5);
        Price buyoutDouble = mockPrice("150");
        Clock clock = mock(Clock.class);


        try (MockedConstruction<Auction> mocked =
                     mockConstruction(Auction.class, (mock, context) -> {
                     })) {

            AuctionFactory auctionFactory = new AuctionFactory();

            // act
            Auction auction = auctionFactory.create(listingDouble, startDate, endDate, buyoutDouble, clock);

            // assert
            assertNotNull(auction);
            assertEquals(1, mocked.constructed().size());

            Auction constructedAuction = mocked.constructed().get(0);
            assertSame(auction, constructedAuction);
        }

    }
    private Price mockPrice(String value){
        Price price = mock(Price.class);
        when(price.getPrice()).thenReturn(new BigDecimal(value));

        return price;
    }
}