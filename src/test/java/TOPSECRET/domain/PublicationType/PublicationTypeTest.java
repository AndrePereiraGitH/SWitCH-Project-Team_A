package TOPSECRET.domain.PublicationType;

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
        String mediaFormat = "Book";

        PublicationType pt1 = new PublicationType(mediaFormat);

        assertNotNull(pt1);
        assertEquals(mediaFormat, pt1.getMediaFormat());

    }


    @Test
    void shouldReturnFalseWhenComparedWithNull() {
        PublicationType pt = new PublicationType("Book");
        assertFalse(pt.equals(null));
    }


    @Test
    void shouldReturnFalseWhenDifferentInstancesEvenWithSameMediaFormat() {

        PublicationType a = new PublicationType("Book");
        PublicationType b = new PublicationType("Book");

        assertNotEquals(a, b);
    }

    @Test
    void shouldReturnTrueWhenSameInstance() {

        PublicationType pt = new PublicationType("Book");

        assertTrue(pt.equals(pt));
    }

    @Test
    void TestInvalidPublicationTypes()
    {
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

        PublicationType a = new PublicationType("Book");
        PublicationType b = new PublicationType("Book");

        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void shouldReturnTrueWhenSameMediaFormat() {

        PublicationType a = new PublicationType("Book");
        PublicationType b = new PublicationType("Book");

        assertTrue(a.sameAs(b));
    }

    @Test
    void shouldReturnFalseWhenDifferentMediaFormat() {

        PublicationType a = new PublicationType("Book");
        PublicationType b = new PublicationType("Magazine");

        assertFalse(a.sameAs(b));
    }

}