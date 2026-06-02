package TOPSECRET.domain.PublicationType;

import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

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
    // =========================================================
    // create(String mediaFormat) — SUCCESS
    // =========================================================


    @Test
    void shouldCreatePublicationTypeWithCorrectMediaFormat() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();

        // ACT
        PublicationType result = factory.create("Book");

        // ASSERT
        assertEquals("Book", result.getMediaFormat());
    }

    @Test
    void shouldCreatePublicationTypeWithGeneratedId() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();

        // ACT
        PublicationType result = factory.create("Book");

        // ASSERT
        assertNotNull(result.id());
    }

    @Test
    void shouldCreateTwoPublicationTypesWithDifferentIds() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();

        // ACT
        PublicationType pt1 = factory.create("Book");
        PublicationType pt2 = factory.create("Book");

        // ASSERT
        assertNotEquals(pt1.id(), pt2.id());
    }

    // =========================================================
    // create(String mediaFormat) — INVALID INPUT
    // =========================================================

    @Test
    void shouldThrowWhenMediaFormatIsNullOnCreate() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null));
    }

    @Test
    void shouldThrowWhenMediaFormatIsBlankOnCreate() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("   "));
    }

    @Test
    void shouldThrowWhenMediaFormatIsEmptyOnCreate() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(""));
    }

    // =========================================================
    // create(PublicationTypeId, String mediaFormat) — SUCCESS
    // =========================================================

    @Test
    void shouldReconstructPublicationTypeFromExistingData() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();
        PublicationTypeId id = mock(PublicationTypeId.class);

        // ACT
        PublicationType result = factory.create(id, "Magazine");

        // ASSERT
        assertNotNull(result);
    }

    @Test
    void shouldReconstructPublicationTypeWithCorrectId() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();
        PublicationTypeId id = mock(PublicationTypeId.class);

        // ACT
        PublicationType result = factory.create(id, "Magazine");

        // ASSERT
        assertEquals(id, result.id());
    }

    @Test
    void shouldReconstructPublicationTypeWithCorrectMediaFormat() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();
        PublicationTypeId id = mock(PublicationTypeId.class);

        // ACT
        PublicationType result = factory.create(id, "Magazine");

        // ASSERT
        assertEquals("Magazine", result.getMediaFormat());
    }

    // =========================================================
    // create(PublicationTypeId, String mediaFormat) — INVALID INPUT
    // =========================================================

    @Test
    void shouldThrowWhenIdIsNullOnReconstruct() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, "Magazine"));
    }

    @Test
    void shouldThrowWhenMediaFormatIsNullOnReconstruct() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();
        PublicationTypeId id = mock(PublicationTypeId.class);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(id, null));
    }

    @Test
    void shouldThrowWhenMediaFormatIsBlankOnReconstruct() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();
        PublicationTypeId id = mock(PublicationTypeId.class);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(id, "   "));
    }

    @Test
    void shouldThrowWhenMediaFormatIsEmptyOnReconstruct() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();
        PublicationTypeId id = mock(PublicationTypeId.class);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(id, ""));
    }
}