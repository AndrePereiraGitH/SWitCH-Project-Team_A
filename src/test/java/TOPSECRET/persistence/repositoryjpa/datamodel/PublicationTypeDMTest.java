package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeDMTest {

    // HELPER
    private PublicationTypeId validId() {
        return new PublicationTypeId("1");
    }

    // CONSTRUCTOR TESTS
    @Test
    void shouldCreateValidDM() {

        // ARRANGE
        PublicationTypeId id = validId();

        // ACT
        PublicationTypeDM dm = new PublicationTypeDM(id, "Book");

        // ASSERT
        assertNotNull(dm);
        assertEquals(id, dm.getId());
        assertEquals("Book", dm.getMediaFormat());
    }

    @Test
    void shouldThrowWhenIdIsNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new PublicationTypeDM(null, "Book"));
    }

    @Test
    void shouldThrowWhenMediaFormatIsNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new PublicationTypeDM(validId(), null));
    }

    @Test
    void shouldThrowWhenMediaFormatIsBlank() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new PublicationTypeDM(validId(), " "));
    }

    // GETTERS
    @Test
    void shouldReturnId() {

        // ARRANGE
        PublicationTypeId id = validId();
        PublicationTypeDM dm = new PublicationTypeDM(id, "Book");

        // ACT
        PublicationTypeId result = dm.getId();

        // ASSERT
        assertEquals(id, result);
    }

    @Test
    void shouldReturnMediaFormat() {

        // ARRANGE
        PublicationTypeDM dm = new PublicationTypeDM(validId(), "Book");

        // ACT
        String result = dm.getMediaFormat();

        // ASSERT
        assertEquals("Book", result);
    }

    // EQUALS & HASHCODE
    @Test
    void shouldBeEqualWhenSameId() {

        // ARRANGE
        PublicationTypeId id = validId();

        PublicationTypeDM dm1 = new PublicationTypeDM(id, "Book");
        PublicationTypeDM dm2 = new PublicationTypeDM(id, "Different");

        // ACT
        boolean result = dm1.equals(dm2);

        // ASSERT
        assertTrue(result);
        assertEquals(dm1.hashCode(), dm2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {

        // ARRANGE
        PublicationTypeDM dm1 = new PublicationTypeDM(new PublicationTypeId("1"), "Book");
        PublicationTypeDM dm2 = new PublicationTypeDM(new PublicationTypeId("2"), "Book");

        // ACT
        boolean result = dm1.equals(dm2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {

        // ARRANGE
        PublicationTypeDM dm = new PublicationTypeDM(validId(), "Book");

        // ACT
        boolean result = dm.equals(null);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenDifferentType() {

        // ARRANGE
        PublicationTypeDM dm = new PublicationTypeDM(validId(), "Book");

        // ACT
        boolean result = dm.equals("not a DM");

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenSameInstance() {

        // ARRANGE
        PublicationTypeDM dm = new PublicationTypeDM(validId(), "Book");

        // ACT
        boolean result = dm.equals(dm);

        // ASSERT
        assertTrue(result);
    }
}