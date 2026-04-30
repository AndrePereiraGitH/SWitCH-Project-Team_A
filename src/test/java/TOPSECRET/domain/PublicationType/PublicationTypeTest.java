package TOPSECRET.domain.PublicationType;

import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PublicationType}.
 *
 * <p>
 * Validates domain entity behavior including:
 * </p>
 *
 * <ul>
 *     <li>Creation of valid instances</li>
 *     <li>Validation of constructor arguments</li>
 *     <li>Equality and identity based on {@link PublicationTypeId}</li>
 *     <li>hashCode consistency</li>
 *     <li>Business comparison using {@code sameAs}</li>
 * </ul>
 *
 * <p>
 * Ensures that domain invariants are enforced and that identity
 * and comparison logic behave as expected.
 * </p>
 *
 * <p>
 * Tests focus on entity integrity, validation rules, and correct
 * implementation of equality and business equivalence.
 * </p>
 */
class PublicationTypeTest {

    // CONSTRUCTOR
    @Test
    void shouldCreateValidPublicationType() {

        // ARRANGE
        String format = "Book";

        // ACT
        PublicationType pt = new PublicationType(format);

        // ASSERT
        assertNotNull(pt);
        assertEquals(format, pt.getMediaFormat());
        assertNotNull(pt.id());
    }

    @Test
    void shouldThrowWhenMediaFormatIsInvalid() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(null));
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(""));
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(" "));
    }

    // EQUALS
    @Test
    void shouldReturnTrueWhenSameInstance() {

        // ARRANGE
        PublicationType pt = new PublicationType("Book");

        // ACT
        boolean result = pt.equals(pt);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {

        // ARRANGE
        PublicationType pt = new PublicationType("Book");

        // ACT
        boolean result = pt.equals(null);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentClass() {

        // ARRANGE
        PublicationType pt = new PublicationType("Book");

        // ACT
        boolean result = pt.equals("Book");

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentIds() {

        // ARRANGE
        PublicationType a = new PublicationType("Book");
        PublicationType b = new PublicationType("Book");

        // ACT
        boolean result = a.equals(b);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenSameId() {

        // ARRANGE
        PublicationTypeId id = new PublicationTypeId("1");

        PublicationType a = new PublicationType(id, "Book");
        PublicationType b = new PublicationType(id, "Magazine");

        // ACT
        boolean result = a.equals(b);

        // ASSERT
        assertTrue(result);
    }

    // HASHCODE
    @Test
    void shouldHaveSameHashCodeWhenSameId() {

        // ARRANGE
        PublicationTypeId id = new PublicationTypeId("1");

        PublicationType a = new PublicationType(id, "Book");
        PublicationType b = new PublicationType(id, "Magazine");

        // ACT + ASSERT
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void shouldHaveDifferentHashCodeWhenDifferentId() {

        // ARRANGE
        PublicationType a = new PublicationType("Book");
        PublicationType b = new PublicationType("Book");

        // ACT + ASSERT
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    // SAMEAS (BUSINESS EQUALITY)
    @Test
    void shouldReturnTrueWhenSameMediaFormat() {

        // ARRANGE
        PublicationType a = new PublicationType("Book");
        PublicationType b = new PublicationType("Book");

        // ACT
        boolean result = a.sameAs(b);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentMediaFormat() {

        // ARRANGE
        PublicationType a = new PublicationType("Book");
        PublicationType b = new PublicationType("Magazine");

        // ACT
        boolean result = a.sameAs(b);

        // ASSERT
        assertFalse(result);
    }

    // ===============================
    // ID
    @Test
    void shouldReturnId() {

        // ARRANGE
        PublicationType pt = new PublicationType("Book");

        // ACT
        PublicationTypeId id = pt.id();

        // ASSERT
        assertNotNull(id);
        assertEquals(id, pt.id());
    }
}