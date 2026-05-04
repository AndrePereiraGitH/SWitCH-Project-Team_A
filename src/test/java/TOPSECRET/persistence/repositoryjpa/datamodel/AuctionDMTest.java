package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link AuctionDM}. These tests ensure that the constructor
 * correctly stores all provided values and that the exposed getters
 * return the expected data.
 */
@ExtendWith(MockitoExtension.class)
class AuctionDMTest {

    // ===========================================
    // ============= Set Up ======================
    // ===========================================

    @Mock
    private AuctionId auctionId;

    @Mock
    private ListingId listingId;

    @Mock
    private BidDM bidDM1;

    @Mock
    private BidDM bidDM2;

    @Mock
    private BidDM winningBidDM;


    private LocalDateTime start;
    private LocalDateTime end;
    private BigDecimal initialPrice;
    private BigDecimal buyoutPrice;
    private List<BidDM> bidHistoryDM;
    private BigDecimal finalPrice;
    private LocalDateTime closedAt;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.now();
        end = start.plusDays(1);

        initialPrice = new BigDecimal("10.00");
        buyoutPrice = new BigDecimal("100.00");

        bidHistoryDM = List.of(bidDM1, bidDM2);

        finalPrice = new BigDecimal("75.00");
        closedAt = start.plusDays(10);
    }

    // =========================================================
    // ================ CONSTRUCTOR TESTS ======================
    // =========================================================

    @Test
    void constructor_shouldStoreAuctionId() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Act + Assert
        assertEquals(auctionId, dm.getAuctionId());
    }

    @Test
    void constructor_shouldStoreListingId() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Act + Assert
        assertEquals(listingId, dm.getListingId());
    }

    @Test
    void constructor_shouldStoreStartDate() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Act + Assert
        assertEquals(start, dm.getAuctionStartDateTime());
    }

    @Test
    void constructor_shouldStoreEndDate() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Act + Assert
        assertEquals(end, dm.getAuctionEndDateTime());
    }

    @Test
    void constructor_shouldStoreInitialPrice() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Act + Assert
        assertEquals(initialPrice, dm.getInitialPrice());
    }

    @Test
    void constructor_shouldStoreBuyoutPrice() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Act + Assert
        assertEquals(buyoutPrice, dm.getBuyoutPrice());
    }

    @Test
    void constructor_shouldStoreBidHistoryDM() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Act + Assert
        assertEquals(bidHistoryDM, dm.getBidHistory());
    }

    @Test
    void constructor_shouldStoreWinningBid() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Act + Assert
        assertEquals(winningBidDM, dm.getWinningBid());
    }

    @Test
    void constructor_shouldStoreAuctionStatus() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Act + Assert
        assertEquals(AuctionStatus.ACTIVE, dm.getAuctionStatus());
    }

    @Test
    void constructor_shouldStoreFinalPrice() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Act + Assert
        assertEquals(finalPrice, dm.getFinalPrice());
    }

    @Test
    void constructor_shouldStoreClosedAt() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Act + Assert
        assertEquals(closedAt, dm.getClosedAt());
    }

    // =========================================================
    // ========== NULL FIELDS BEHAVIOR =========================
    // =========================================================

    @Test
    void constructor_shouldAllowNullBidHistoryDM() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, null, winningBidDM, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Act + Assert
        assertNull(dm.getBidHistory());
    }

    @Test
    void constructor_shouldAllowNullWinningBid() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, null, AuctionStatus.ACTIVE, finalPrice, closedAt);

        // Act + Assert
        assertNull(dm.getWinningBid());
    }

    @Test
    void constructor_shouldAllowNullAuctionStatus() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, null, finalPrice, closedAt);

        // Act + Assert
        assertNull(dm.getAuctionStatus());
    }

    @Test
    void constructor_shouldAllowNullFinalPrice() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, null, closedAt);

        // Act + Assert
        assertNull(dm.getFinalPrice());
    }

    @Test
    void constructor_shouldAllowNullClosedAt() {
        // Arrange
        AuctionDM dm = new AuctionDM(auctionId, listingId, start, end, initialPrice,
                buyoutPrice, bidHistoryDM, winningBidDM, AuctionStatus.ACTIVE, finalPrice, null);

        // Act + Assert
        assertNull(dm.getClosedAt());
    }
}