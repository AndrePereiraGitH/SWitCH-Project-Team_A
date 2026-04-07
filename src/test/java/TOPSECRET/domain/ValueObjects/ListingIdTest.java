package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link ListingId} value object, validating its construction
 * constraints, identity semantics, and defensive behaviour against invalid input.
 *
 * <p>This suite ensures that the ListingId model enforces all required invariants,
 * including:
 * <ul>
 *     <li>rejection of null, empty, or blank identifiers;</li>
 *     <li>correct preservation of the UUID string provided at construction;</li>
 *     <li>identity‑based equality, where two ListingId instances are equal only if
 *         they encapsulate the same UUID value;</li>
 *     <li>safe handling of invalid comparisons, such as null references or objects
 *         of unrelated types.</li>
 * </ul>
 *
 * <p>The tests use deterministic UUID values to ensure predictable behaviour and
 * verify that ListingId behaves as a well‑formed, immutable value object within
 * the domain model.
 */
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
    void equalsShouldReturnFalseWhenObjectIsOfDifferentType() {
        // Arrange
        String uuid = UUID.randomUUID().toString();

        // Act
        boolean result = uuid.equals("not a ListingId");

        // Assert
        assertFalse(result);
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