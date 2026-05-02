package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link BidDM}. These tests ensure that the constructor
 * correctly stores the provided values and that the exposed getters
 * return the expected data.
 */
@ExtendWith(MockitoExtension.class)
class BidDMTest {

    // ==============================================
    // ============= Set Up =========================
    // ==============================================

    @Mock
    private UserId bidderId;

    private BigDecimal value;
    private LocalDateTime timeOfBid;

    @BeforeEach
    void setUp() {
        value = new BigDecimal("50.00");
        timeOfBid = LocalDateTime.now();
    }

    // =========================================
    // ============ CONSTRUCTOR =================
    // =========================================

    @Test
    void constructor_shouldStoreBidderId() {
        // Arrange
        BidDM bidDM = new BidDM(bidderId, value, timeOfBid);

        // Act + Assert
        assertEquals(bidderId, bidDM.getBidderId());
    }

    @Test
    void constructor_shouldStoreValue() {
        // Arrange
        BidDM bidDM = new BidDM(bidderId, value, timeOfBid);

        // Act + Assert
        assertEquals(value, bidDM.getValue());
    }

    @Test
    void constructor_shouldStoreTimeOfBid() {
        // Arrange
        BidDM bidDM = new BidDM(bidderId, value, timeOfBid);

        // Act + Assert
        assertEquals(timeOfBid, bidDM.getTimeOfBid());
    }

    // =========================================
    // ============ NULL BEHAVIOR ==============
    // =========================================

    @Test
    void constructor_shouldAllowNullBidderId() {
        // Arrange
        BidDM bidDM = new BidDM(null, value, timeOfBid);

        // Act + Assert
        assertNull(bidDM.getBidderId());
    }

    @Test
    void constructor_shouldAllowNullValue() {
        // Arrange
        BidDM bidDM = new BidDM(bidderId, null, timeOfBid);

        // Act + Assert
        assertNull(bidDM.getValue());
    }

    @Test
    void constructor_shouldAllowNullTimeOfBid() {
        // Arrange
        BidDM bidDM = new BidDM(bidderId, value, null);

        // Act + Assert
        assertNull(bidDM.getTimeOfBid());
    }
}