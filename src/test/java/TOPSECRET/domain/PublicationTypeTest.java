package TOPSECRET.domain;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
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
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;
        String details = "Hardback book with gold engravings";

        PublicationType pt1 = new PublicationType(mediaFormat, typeOfBinding, details);

        assertNotNull(pt1);
        assertEquals(mediaFormat, pt1.getMediaFormat());
        assertEquals(pt1.getTypeOfBinding(), typeOfBinding);
        assertEquals(pt1.getDetails(), details);

    }

    /**
     * Verifies that comparing a {@link PublicationType} object with {@code null}
     * using {@code equals()} returns {@code false}.
     */
    @Test
    void shouldReturnFalseWhenComparedWithNull() {
        PublicationType pt = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "A");
        assertFalse(pt.equals(null));
    }

    /**
     * Verifies that comparing a {@link PublicationType} object with
     * an instance of a different class returns {@code false}.
     */
    @Test
    void shouldReturnFalseWhenComparedWithDifferentClass() {
        PublicationType pt = new PublicationType(MediaFormat.MAGAZINE, TypeOfBinding.HARDBACK, "A");
        assertFalse(pt.equals("not a PublicationType"));
    }

    /**
     * Tests that the constructor throws {@link IllegalArgumentException}
     * when invalid parameters are provided, such as null values or empty details.
     */
    @Test
    void TestInvalidPublicationTypes()
    {
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(null, TypeOfBinding.HARDBACK, "A"));
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, ""));
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(MediaFormat.BOOK, null,"Book with gold engravings"));
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
        PublicationType a = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "A");
        PublicationType b = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "A");
        PublicationType c = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "C");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a.hashCode(), c.hashCode());
    }

}