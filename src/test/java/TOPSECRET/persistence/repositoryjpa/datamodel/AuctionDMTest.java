package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link AuctionDM}. These tests ensure that the constructor
 * correctly stores all provided values and that the exposed getters
 * return the expected data.
 */
class AuctionDMTest {

    // ===========================================
    // ============= Set Up ======================
    // ===========================================

    private AuctionId auctionIdDouble;
    private ListingId listingIdDouble;
    private LocalDateTime start;
    private LocalDateTime end;
    private BigDecimal initialPrice;
    private BigDecimal buyoutPrice;
    private BidDM bidDM1Double;
    private BidDM bidDM2Double;
    private List<BidDM> bidHistoryDM;
    private BidDM winningBidDM;
    private BigDecimal finalPrice;
    private LocalDateTime closedAt;

    @BeforeEach
    void setUp() {
        auctionIdDouble = mock(AuctionId.class);
        listingIdDouble = mock(ListingId.class);

        start = LocalDateTime.now();
        end = start.plusDays(1);

        initialPrice = new BigDecimal("10.00");
        buyoutPrice = new BigDecimal("100.00");

        bidDM1Double = mock(BidDM.class);
        bidDM2Double = mock(BidDM.class);
        bidHistoryDM = List.of(bidDM1Double, bidDM2Double);

        winningBidDM = mock(BidDM.class);

        finalPrice = new BigDecimal("75.00");
        closedAt = start.plusDays(10);
    }

    // ===========================================
    // =========== Constructor ===================
    // ===========================================

    @Test
    void constructor_shouldStoreAllFieldsCorrectly() {
        // Act
        AuctionDM dm = new AuctionDM(auctionIdDouble, listingIdDouble, start, end, initialPrice, buyoutPrice,
                bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Assert
        assertEquals(auctionIdDouble, dm.getAuctionId());
        assertEquals(listingIdDouble, dm.getListingId());
        assertEquals(start, dm.getAuctionStartDateTime());
        assertEquals(end, dm.getAuctionEndDateTime());
        assertEquals(initialPrice, dm.getInitialPrice());
        assertEquals(buyoutPrice, dm.getBuyoutPrice());
        assertEquals(bidHistoryDM, dm.getBidHistory());
        assertEquals(winningBidDM, dm.getWinningBid());
        assertEquals(AuctionStatus.ACTIVE, dm.getAuctionStatus());
        assertEquals(finalPrice, dm.getFinalPrice());
        assertEquals(closedAt, dm.getClosedAt());
    }

    @Test
    void constructor_shouldAllowNullOptionalFields() {
        // Act
        AuctionDM dm = new AuctionDM(auctionIdDouble, listingIdDouble, start, end, initialPrice, buyoutPrice,
                null, null,null,null, null);

        // Assert
        assertNull(dm.getBidHistory());
        assertNull(dm.getWinningBid());
        assertNull(dm.getAuctionStatus());
        assertNull(dm.getFinalPrice());
        assertNull(dm.getClosedAt());
    }
}