package TOPSECRET.domain.PublicationType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PublicationTypeFactory}.
 *
 * <p>
 * Validates that the factory correctly creates PublicationType
 * instances by invoking the constructor.
 * </p>
 *
 * <p>
 * Uses {@code mockConstruction} to ensure that a new instance
 * is created and returned.
 * </p>
 */
class PublicationTypeFactoryTest {

    @Test
    void shouldCreatePublicationType() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();
        String format = "Book";

        // ACT
        PublicationType result = factory.create(format);

        // ASSERT
        assertNotNull(result);
        assertEquals(format, result.getMediaFormat());
        assertNotNull(result.id());
    }

    @Test
    void shouldThrowWhenMediaFormatIsInvalid() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> factory.create(null));
        assertThrows(IllegalArgumentException.class, () -> factory.create(""));
        assertThrows(IllegalArgumentException.class, () -> factory.create(" "));
    }

    @Test
    void sameAsShouldReturnTrueWhenSameInstance() {

        // ARRANGE
        PublicationType pt = new PublicationType("Book");

        // ACT
        boolean result = pt.sameAs(pt);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenDifferentType() {

        // ARRANGE
        PublicationType pt = new PublicationType("Book");

        // ACT
        boolean result = pt.sameAs("Book");

        // ASSERT
        assertFalse(result);
    }
}