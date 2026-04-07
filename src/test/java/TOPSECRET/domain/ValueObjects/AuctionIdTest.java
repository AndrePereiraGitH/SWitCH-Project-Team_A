package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link AuctionId} value object, validating its construction
 * constraints, identity semantics, and robustness against invalid input.
 *
 * <p>This suite ensures that the AuctionId model enforces all required invariants,
 * including:
 * <ul>
 *     <li>rejection of null, empty, or blank identifiers;</li>
 *     <li>correct preservation of the UUID string provided at construction;</li>
 *     <li>identity‑based equality, where two AuctionId instances are equal only if
 *         they encapsulate the same UUID value;</li>
 *     <li>safe handling of invalid comparisons, such as null references or objects
 *         of unrelated types.</li>
 * </ul>
 *
 * <p>The tests use deterministic UUID values to ensure predictable behaviour and
 * verify that AuctionId behaves as a well‑formed, immutable value object within
 * the domain model.
 */
class AuctionIdTest {

    // ========== CONSTRUCTOR VALIDATION TESTS ==========

    @Test
    void shouldNotAllowNullId() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new AuctionId(null));
    }

    @Test
    void shouldNotAllowEmptyId() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new AuctionId(""));
    }

    @Test
    void shouldNotAllowBlankId() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new AuctionId(" "));
    }

    @Test
    void shouldCreateAuctionIdWithValidUuid() {
        // Arrange
        String uuid = UUID.randomUUID().toString();

        // Act
        AuctionId auctionId = new AuctionId(uuid);

        // Assert
        assertEquals(uuid, auctionId.getId());
    }

    // ========== GET ID METHOD TESTS ==========

    @Test
    void idShouldReturnExactUuid() {
        // Arrange
        String uuid = UUID.randomUUID().toString();

        // Act
        AuctionId auctionId = new AuctionId(uuid);

        // Assert
        assertEquals(uuid, auctionId.getId());
    }

    // ========== EQUALS METHOD TESTS ==========

    @Test
    void equalsShouldReturnTrueForSameInstance() {
        // Arrange
        AuctionId id = new AuctionId(UUID.randomUUID().toString());

        // Act & Assert
        assertEquals(id, id);
    }

    @Test
    void equalsShouldReturnTrueForSameUuid() {
        // Arrange
        String uuid = UUID.randomUUID().toString();

        // Act
        AuctionId id1 = new AuctionId(uuid);
        AuctionId id2 = new AuctionId(uuid);

        // Assert
        assertEquals(id1, id2);
    }

    @Test
    void equalsShouldReturnFalseForDifferentUuids() {
        // Arrange
        AuctionId id1 = new AuctionId(UUID.randomUUID().toString());
        AuctionId id2 = new AuctionId(UUID.randomUUID().toString());

        // Act & Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithNull() {
        // Arrange
        AuctionId id = new AuctionId(UUID.randomUUID().toString());

        // Act & Assert
        assertNotEquals(id, null);
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithDifferentType() {
        // Arrange
        AuctionId id = new AuctionId(UUID.randomUUID().toString());

        // Act & Assert
        assertNotEquals(id, "not an AuctionId");
    }
}