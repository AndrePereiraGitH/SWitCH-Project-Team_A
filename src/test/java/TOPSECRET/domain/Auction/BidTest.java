package TOPSECRET.domain.Auction;

import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BidTest {

    // ------------- TEST DATA SETUP -------------

    private BidId bidId1;
    private BidId bidId2;
    private AuctionId auctionId1;
    private AuctionId auctionId2;
    private UserId bidderId1;
    private UserId bidderId2;
    private Price value1;
    private Price value2;
    private LocalDateTime timeOfBid1;
    private LocalDateTime timeOfBid2;

    @BeforeEach
    void setUp() {
        bidId1 = new BidId("bid1");
        bidId2 = new BidId("bid2");
        auctionId1 = new AuctionId("auction1");
        auctionId2 = new AuctionId("auction2");
        bidderId1 = new UserId("bidder1");
        bidderId2 = new UserId("bidder2");
        value1 = new Price(BigDecimal.valueOf(10.00));
        value2 = new Price(BigDecimal.valueOf(5.50));
        timeOfBid1 = LocalDateTime.of(2026,4,1,10,0,0);
        timeOfBid2 = LocalDateTime.of(2026,4,1,11,0,0);
    }


    // ------------- CONSTRUCTOR VALIDATION TESTS -------------
    @Test
    void constructorShouldCreateBidWhenValid() {

        //Act
        Bid bid1 = new Bid(bidId1, auctionId1, bidderId1, value1, timeOfBid1);

        //Assert
        assertEquals(bidId1, bid1.id());
        assertEquals(auctionId1, bid1.getAuctionId());
        assertEquals(bidderId1, bid1.getBidderId());
        assertEquals(value1, bid1.getValue());
        assertEquals(timeOfBid1, bid1.getTimeOfBid());
    }

    @Test
    void constructorShouldThrowExceptionWhenBidIdIsNull() {

        // Assert
        assertThrows(IllegalArgumentException.class, () -> new Bid(null, auctionId1, bidderId1, value1, timeOfBid1));
    }

    @Test
    void constructorShouldThrowExceptionWhenAuctionIdIsNull() {

        // Assert
        assertThrows(IllegalArgumentException.class, () -> new Bid(bidId1, null, bidderId1, value1, timeOfBid1));
    }

    @Test
    void constructorShouldThrowExceptionWhenBidderIsNull() {

        // Assert
        assertThrows(IllegalArgumentException.class, () -> new Bid(bidId1, auctionId1, null, value1, timeOfBid1));
    }

    @Test
    void constructorShouldThrowExceptionWhenValueIsNull() {

        // Assert
        assertThrows(IllegalArgumentException.class, () -> new Bid(bidId1, auctionId1, bidderId1, null, timeOfBid1));
    }

    @Test
    void constructorShouldThrowExceptionWhenTimeOfBidIsNull() {

        // Assert
        assertThrows(IllegalArgumentException.class, () -> new Bid(bidId1, auctionId1, bidderId1, value1, null));
    }


    // ------------- EQUALS METHOD TESTS -------------

    @Test
    void equalsShouldReturnTrueForSameBidId() {

        // Act
        Bid bid1 = new Bid(bidId1, auctionId1, bidderId1, value1, timeOfBid1);
        Bid bid2 = new Bid(bidId1, auctionId1, bidderId1, value1, timeOfBid1);

        // Assert
        assertEquals(bid1, bid2);
    }

    @Test
    void equalsShouldReturnFalseForDifferentBidId() {

        // Act
        Bid bid1 = new Bid(bidId1, auctionId1, bidderId1, value1, timeOfBid1);
        Bid bid2 = new Bid(bidId2, auctionId1, bidderId1, value1, timeOfBid1);

        // Assert
        assertNotEquals(bid1, bid2);
    }

    @Test
    void equalsShouldReturnFalseWhenObjectIsNull() {

        // Act
        Bid bid = new Bid(bidId1, auctionId1, bidderId1, value1, timeOfBid1);

        // Assert
        assertNotEquals(bid, null);
    }

    @Test
    void equalsShouldReturnFalseWhenObjectIsNotBid() {

        // Act
        Bid bid = new Bid(bidId1, auctionId1, bidderId1, value1, timeOfBid1);

        // Assert
        assertNotEquals(bid, "not a bid");
    }


    // ------------- SAME AS METHOD TESTS -------------

    @Test
    void sameAsShouldReturnTrueWhenSemanticAttributesMatch() {

        // Act
        Bid bid1 = new Bid(bidId1, auctionId1, bidderId1, value1, timeOfBid1);
        Bid bid2 = new Bid(bidId2, auctionId1, bidderId1, value1, timeOfBid1);

        // Assert
        assertTrue(bid1.sameAs(bid2));
    }

    @Test
    void sameAsShouldReturnFalseWhenAuctionIdDoesNotMatch() {

        // Act
        Bid bid1 = new Bid(bidId1, auctionId1, bidderId1, value1, timeOfBid1);
        Bid bid2 = new Bid(bidId2, auctionId2, bidderId1, value1, timeOfBid1);

        // Assert
        assertFalse(bid1.sameAs(bid2));
    }

    @Test
    void sameAsShouldReturnFalseWhenBidderIdDoesNotMatch() {

        // Act
        Bid bid1 = new Bid(bidId1, auctionId1, bidderId1, value1, timeOfBid1);
        Bid bid2 = new Bid(bidId2, auctionId1, bidderId2, value1, timeOfBid1);

        // Assert
        assertFalse(bid1.sameAs(bid2));
    }

    @Test
    void sameAsShouldReturnFalseWhenValueDoesNotMatch() {

        // Act
        Bid bid1 = new Bid(bidId1, auctionId1, bidderId1, value1, timeOfBid1);
        Bid bid2 = new Bid(bidId2, auctionId1, bidderId1, value2, timeOfBid1);

        // Assert
        assertFalse(bid1.sameAs(bid2));
    }

    @Test
    void sameAsShouldReturnFalseWhenTimeOfBidDoesNotMatch() {

        // Act
        Bid bid1 = new Bid(bidId1, auctionId1, bidderId1, value1, timeOfBid1);
        Bid bid2 = new Bid(bidId2, auctionId1, bidderId1, value1, timeOfBid2);

        // Assert
        assertFalse(bid1.sameAs(bid2));
    }

    @Test
    void sameAsShouldReturnFalseWhenObjectIsNull() {

        // Act
        Bid bid = new Bid(bidId1, auctionId1, bidderId1, value1, timeOfBid1);

        // Assert
        assertFalse(bid.sameAs(null));
    }

}