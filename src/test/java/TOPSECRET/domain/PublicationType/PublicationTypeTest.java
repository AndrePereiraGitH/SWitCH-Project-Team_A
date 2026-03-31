package TOPSECRET.domain.PublicationType;

import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PublicationType.
 *
 * Validates constructor rules, equality, hashCode,
 * and business identity behavior.
 */
class PublicationTypeTest {

    @Test
    void TestValidPublicationType()
    {
        // ARRANGE
        String mediaFormat = "Book";

        // ACT
        PublicationType pt1 = new PublicationType(mediaFormat);

        // ASSERT
        assertNotNull(pt1);
        assertEquals(mediaFormat, pt1.getMediaFormat());

    }


    @Test
    void shouldReturnFalseWhenComparedWithNull() {

        // ARRANGE
        PublicationType pt = new PublicationType("Book");

        // ACT + ASSERT
        assertFalse(pt.equals(null));
    }


    @Test
    void shouldReturnFalseWhenDifferentInstancesEvenWithSameMediaFormat() {

        // ARRANGE

        PublicationType a = new PublicationType("Book");
        PublicationType b = new PublicationType("Book");

        // ACT
        boolean result = a.equals(b);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenSameInstance() {

        // ARRANGE
        PublicationType pt = new PublicationType("Book");

        // ACT + ASSERT
        assertTrue(pt.equals(pt));
    }

    @Test
    void TestInvalidPublicationTypes()
    {
        // ARRANGE

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(null));
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(""));
        assertThrows(IllegalArgumentException.class, () -> new PublicationType("   "));
    }


    @Test
    void shouldHaveSameHashCodeWhenSameInstance() {

        PublicationType pt = new PublicationType("Book");

        assertEquals(pt.hashCode(), pt.hashCode());
    }

    @Test
    void shouldHaveDifferentHashCodesForDifferentInstances() {

        // ARRANGE
        PublicationType a = new PublicationType("Book");
        PublicationType b = new PublicationType("Book");

        // ACT + ASSERT
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void shouldReturnTrueWhenSameMediaFormat() {

        // ARRANGE
        PublicationType a = new PublicationType("Book");
        PublicationType b = new PublicationType("Book");

        // ACT + ASSERT
        assertTrue(a.sameAs(b));
    }

    @Test
    void shouldReturnFalseWhenDifferentMediaFormat() {

        // ARRANGE
        PublicationType a = new PublicationType("Book");
        PublicationType b = new PublicationType("Magazine");

        // ACT + ASSERT
        assertFalse(a.sameAs(b));
    }

    @Test
    void shouldReturnSameIdUsingIdMethod() {

        // ARRANGE
        PublicationType pt = new PublicationType("Book");

        // ACT
        PublicationTypeId id = pt.id();

        // ASSERT
        assertEquals(pt.identity(), id);
    }

    @Test
    void shouldReturnSameIdUsingIdentityMethod() {

        // ARRANGE
        PublicationType pt = new PublicationType("Book");

        // ACT
        PublicationTypeId id = pt.identity();

        // ASSERT
        assertNotNull(id);
        assertEquals(pt.id(), id);
    }

    @Test
    void shouldReturnFalseWhenDifferentClass() {

        // ARRANGE
        PublicationType pt = new PublicationType("Book");
        String other = "Book";

        // ACT
        boolean result = pt.equals(other);

        // ASSERT
        assertFalse(result);
    }

}