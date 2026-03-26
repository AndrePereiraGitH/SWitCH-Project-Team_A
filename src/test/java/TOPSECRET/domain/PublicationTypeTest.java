package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PublicationType} class.
 *
 * <p>This test suite validates the correct behavior of the PublicationType
 * domain object, including:
 * <ul>
 *     <li>Successful creation with valid parameters</li>
 *     <li>Validation of invalid constructor arguments</li>
 *     <li>Correct implementation of {@code equals()}</li>
 *     <li>Correct implementation of {@code hashCode()}</li>
 * </ul>
 */

class PublicationTypeTest {

    /**
     * Tests that a {@link PublicationType} object is successfully created
     * when valid parameters are provided and that the getters return
     * the expected values.
     */
    @Test
    void TestValidPublicationType()
    {
        String mediaFormat = "Book";

        PublicationType pt1 = new PublicationType(mediaFormat);

        assertNotNull(pt1);
        assertEquals(mediaFormat, pt1.getMediaFormat());

    }

    /**
     * Verifies that comparing a {@link PublicationType} object with {@code null}
     * using {@code equals()} returns {@code false}.
     */
    @Test
    void shouldReturnFalseWhenComparedWithNull() {
        PublicationType pt = new PublicationType("Book");
        assertFalse(pt.equals(null));
    }

    /**
     * Verifies that comparing a {@link PublicationType} object with
     * an instance of a different class returns {@code false}.
     */
    @Test
    void shouldReturnFalseWhenComparedWithDifferentClass() {
        PublicationType pt = new PublicationType("AudioBook");
        assertFalse(pt.equals("not a PublicationType"));
    }

    /**
     * Tests that the constructor throws {@link IllegalArgumentException}
     * when invalid parameters are provided, such as null values or empty details.
     */
    @Test
    void TestInvalidPublicationTypes()
    {
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(null));
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(""));

    }

    /**
     * Verifies that two equal {@link PublicationType} objects produce
     * the same hash code, and that different objects produce different hash codes.
     *
     * <p>This ensures compliance with the general contract of {@code hashCode()}.
     */
    @Test
    void equalObjectsMustHaveSameHashCode()
    {
        PublicationType a = new PublicationType("Book");
        PublicationType b = new PublicationType("Book");
        PublicationType c = new PublicationType("Magazine");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a.hashCode(), c.hashCode());
    }

}