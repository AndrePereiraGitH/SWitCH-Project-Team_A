package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BidIdTest {

    // ========== CONSTRUCTOR VALIDATION TESTS ==========

    @Test
    void shouldNotAllowNullId() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new BidId(null));
    }

    @Test
    void shouldNotAllowEmptyId() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new BidId(""));
    }

    @Test
    void shouldNotAllowBlankId() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new BidId(" "));
    }

    @Test
    void shouldCreateBidIdWithValidUuid() {
        // Arrange
        String uuid = UUID.randomUUID().toString();

        // Act
        BidId bidId = new BidId(uuid);

        // Assert
        assertEquals(uuid, bidId.getId());
    }

    // ========== ID METHOD TESTS ==========

    @Test
    void idShouldReturnExactUuid() {
        // Arrange
        String uuid = UUID.randomUUID().toString();

        // Act
        BidId bidId = new BidId(uuid);

        // Assert
        assertEquals(uuid, bidId.getId());
    }

    // ========== EQUALS METHOD TESTS ==========

    @Test
    void equalsShouldReturnTrueForSameInstance() {
        // Arrange
        BidId id = new BidId(UUID.randomUUID().toString());

        // Act & Assert
        assertEquals(id, id);
    }

    @Test
    void equalsShouldReturnTrueForSameUuid() {
        // Arrange
        String uuid = UUID.randomUUID().toString();

        // Act
        BidId id1 = new BidId(uuid);
        BidId id2 = new BidId(uuid);

        // Assert
        assertEquals(id1, id2);
    }

    @Test
    void equalsShouldReturnFalseForDifferentUuids() {
        // Arrange
        BidId id1 = new BidId(UUID.randomUUID().toString());
        BidId id2 = new BidId(UUID.randomUUID().toString());

        // Act & Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithNull() {
        // Arrange
        BidId id = new BidId(UUID.randomUUID().toString());

        // Act & Assert
        assertNotEquals(id, null);
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithDifferentType() {
        // Arrange
        BidId id = new BidId(UUID.randomUUID().toString());

        // Act & Assert
        assertNotEquals(id, "not a BidId");
    }
}