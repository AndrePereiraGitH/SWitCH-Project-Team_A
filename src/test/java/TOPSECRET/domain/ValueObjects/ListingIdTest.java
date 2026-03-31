package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ListingIdTest {

    // ========== ID VALIDATION TESTS =========
    @Test
    void shouldNotAllowNullId() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new ListingId(null));
    }

    @Test
    void shouldNotAllowEmptyId() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new ListingId(""));
    }

    @Test
    void shouldNotAllowBlankId() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new ListingId(" "));
    }

    @Test
    void shouldBeEqualWhenIdIsTheSame() {
        // Arrange
        String  id = UUID.randomUUID().toString();

        // Act
        ListingId id1 = new ListingId(id);
        ListingId id2 = new ListingId(id);

        // Assert
        assertEquals(id1, id2);
    }

    @Test
    void shouldNotBeEqualWhenIdIsDifferent() {
        // Arrange
        String uuid1 = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString();

        // Act
        ListingId id1 = new ListingId(uuid1);
        ListingId id2 = new ListingId(uuid2);

        // Assert
        assertNotEquals(id1, id2);
    }

    // ========== CONSTRUCTOR VALIDATION TESTS =========
    @Test
    void idShouldReturnTheExactUUIDPassedToConstructor() {
        // Arrange
        String uuid = UUID.randomUUID().toString();

        // Act
        ListingId listingId = new ListingId(uuid);

        // Assert
        assertEquals(uuid, listingId.getId());
    }

    // ========== EQUALS METHOD TESTS =========
    @Test
    void equalsShouldReturnFalseWhenComparedWithNull() {
        // Arrange
        String uuid = UUID.randomUUID().toString();

        // Act
        ListingId id = new ListingId(uuid);

        // Assert
        assertNotEquals(null, id);
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithDifferentType() {
        // Arrange
        String uuid = UUID.randomUUID().toString();

        // Act
        ListingId id = new ListingId(uuid);

        // Assert
        assertNotEquals("not a ListingId", id);
    }

    @Test
    void equalsShouldReturnTrueWhenSameInstance() {
        // Arrange
        String uuid = UUID.randomUUID().toString();

        // Act
        ListingId id = new ListingId(uuid);

        // Assert
        assertEquals(id, id);
    }

    @Test
    void equalsShouldReturnFalseForDifferentUuids() {
        // Arrange
        String uuid1 = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString();

        // Act
        ListingId id1 = new ListingId(uuid1);
        ListingId id2 = new ListingId(uuid2);

        // Assert
        assertFalse(id1.equals(id2));
    }
}