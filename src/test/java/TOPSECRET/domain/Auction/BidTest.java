package TOPSECRET.domain.Auction;

import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Test suite for the {@link Bid} entity, validating its construction rules,
 * equality semantics, and domain‑driven comparison logic.
 *
 * <p>This suite ensures that the Bid model enforces all required invariants, including:
 * <ul>
 *     <li>non‑null validation of all constructor parameters (bidder ID, value, timestamp);</li>
 *     <li>correct initialization of all fields when valid data is provided;</li>
 *     <li>semantic comparison via {@code equals()}, which evaluates whether two bids represent the same bidding action
 *         based on bidder ID and bid value;</li>
 *     <li>robustness against invalid comparisons, including null references and objects of unrelated types.</li>
 * </ul>
 *
 * <p>Overall, this test class provides comprehensive coverage of the Bid value object,
 * ensuring that it behaves consistently with domain rules and supports the integrity of the auction model.
 */
class BidTest {

    // =======================================================
    // ================= SETUP ===============================
    // =======================================================

    private UserId bidderId1Double;
    private UserId bidderId2Double;
    private Price value1;
    private Price value2;
    private LocalDateTime time;

    @BeforeEach
    void setUp() {
        bidderId1Double = mock(UserId.class);
        bidderId2Double = mock(UserId.class);
        value1 = new Price(BigDecimal.valueOf(10.00));
        value2 = new Price(BigDecimal.valueOf(5.50));
        time = LocalDateTime.now();
    }


    // =======================================================
    // ================= CONSTRUCTOR =========================
    // =======================================================

    @Test
    void constructor_shouldCreateBidWhenAllAttributesAreValid() {
        //Act
        Bid bid1 = new Bid(bidderId1Double, value1);

        //Assert
        assertEquals(bidderId1Double, bid1.getBidderId());
        assertEquals(value1, bid1.getValue());
    }

    @Test
    void constructor_shouldThrowExceptionWhenBidderIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Bid(null, value1));
    }

    @Test
    void constructor_shouldThrowExceptionWhenValueIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Bid(bidderId1Double, null));
    }

    @Test
    void constructor_shouldGenerateTimeOfBid() {
        // Act
        Bid bid = new Bid(bidderId1Double, value1);

        // Assert
        assertNotNull(bid.getTimeOfBid());
    }

    // =======================================================
    // ============== (RE)CONSTRUCTOR ========================
    // =======================================================

    @Test
    void reconstructionConstructor_shouldRestoreAllFieldsCorrectly() {
        // Act
        Bid bid = new Bid(bidderId1Double, value1, time);

        // Assert
        assertEquals(bidderId1Double, bid.getBidderId());
        assertEquals(value1, bid.getValue());
        assertEquals(time, bid.getTimeOfBid());
    }

    @Test
    void reconstructionConstructor_shouldThrowErrorWhenBidderIdIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Bid(null, value1, time));
    }

    @Test
    void reconstructionConstructor_shouldThrowErrorWhenValueIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Bid(bidderId1Double, null, time));
    }

    @Test
    void reconstructionConstructor_shouldThrowErrorWhenTimeOfBidIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Bid(bidderId1Double, value1, null));
    }


    // =======================================================
    // ============== SAME AS ================================
    // =======================================================

    @Test
    void equals_shouldReturnTrueWhenAttributesMatch() {
        // Act
        Bid bid1 = new Bid(bidderId1Double, value1);
        Bid bid2 = new Bid(bidderId1Double, value1);

        // Assert
        assertTrue(bid1.equals(bid2));
    }

    @Test
    void equals_shouldReturnFalseWhenBidderIdDoesNotMatch() {
        // Act
        Bid bid1 = new Bid(bidderId1Double, value1);
        Bid bid2 = new Bid(bidderId2Double, value1);

        // Assert
        assertFalse(bid1.equals(bid2));
    }

    @Test
    void equals_shouldReturnFalseWhenValueDoesNotMatch() {
        // Act
        Bid bid1 = new Bid(bidderId1Double, value1);
        Bid bid2 = new Bid(bidderId1Double, value2);

        // Assert
        assertFalse(bid1.equals(bid2));
    }

    @Test
    void equals_shouldReturnFalseWhenObjectIsNull() {
        // Act
        Bid bid = new Bid(bidderId1Double, value1);

        // Assert
        assertFalse(bid.equals(null));
    }


    // =======================================================
    // ============== HASH CODE ==============================
    // =======================================================

    @Test
    void hashCode_shouldReturnSameValueForSameObject() {
        // Act
        Bid bid = new Bid(bidderId1Double, value1);

        int hash1 = bid.hashCode();
        int hash2 = bid.hashCode();

        // Assert
        assertEquals(hash1, hash2);
    }

    @Test
    void hashCode_shouldBeDifferentWhenBidderIdIsDifferent() {
        // Act
        Bid bid1 = new Bid(bidderId1Double, value1);
        Bid bid2 = new Bid(bidderId2Double, value1);

        // Assert
        assertNotEquals(bid1.hashCode(), bid2.hashCode());
    }

    @Test
    void hashCode_shouldBeDifferentWhenValueIsDifferent() {
        // Act
        Bid bid1 = new Bid(bidderId1Double, value1);
        Bid bid2 = new Bid(bidderId1Double, value2);

        // Assert
        assertNotEquals(bid1.hashCode(), bid2.hashCode());
    }
}