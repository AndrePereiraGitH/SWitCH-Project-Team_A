package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link BidDM}. These tests ensure that the constructor
 * correctly stores the provided values and that the exposed getters
 * return the expected data.
 */
class BidDMTest {

    // ==============================================
    // ============= Set Up =========================
    // ==============================================

    private UserId bidderIdDouble;
    private BigDecimal value;
    private LocalDateTime timeOfBid;

    @BeforeEach
    void setUp() {
        bidderIdDouble = mock(UserId.class);
        value = new BigDecimal("50.00");
        timeOfBid = LocalDateTime.now();
    }

    // ==============================================
    // ============= Constructor ====================
    // ==============================================

    @Test
    void constructor_shouldStoreAllFieldsCorrectly() {
        // Act
        BidDM bidDM = new BidDM(bidderIdDouble, value, timeOfBid);

        // Assert
        assertEquals(bidderIdDouble, bidDM.getBidderId());
        assertEquals(value, bidDM.getValue());
        assertEquals(timeOfBid, bidDM.getTimeOfBid());
        assertNull(bidDM.getBidId());
        assertNull(bidDM.getAuction());
    }

    @Test
    void constructor_shouldAllowNullValues() {
        // Act
        BidDM bidDM = new BidDM(null, null, null);

        // Assert
        assertNull(bidDM.getBidderId());
        assertNull(bidDM.getValue());
        assertNull(bidDM.getTimeOfBid());
        assertNull(bidDM.getBidId());
        assertNull(bidDM.getAuction());
    }

    @Test
    void getAuction_shouldReturnAssignedAuction() throws Exception {
        // Arrange
        BidDM bidDM = new BidDM(bidderIdDouble, value, timeOfBid);
        AuctionDM auctionDouble = mock(AuctionDM.class);

        // Simulates the JPA behaviour injecting the ManyToOne relationship
        var field = BidDM.class.getDeclaredField("auction");
        field.setAccessible(true);
        field.set(bidDM, auctionDouble);

        // Act
        AuctionDM result = bidDM.getAuction();

        // Assert
        assertEquals(auctionDouble, result);
    }
}