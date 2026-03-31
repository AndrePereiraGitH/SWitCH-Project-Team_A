package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PublicationTypeId.
 *
 * Validates constructor constraints, exception handling, equality,
 * hashCode consistency, string representation, and ID generation.
 */
class PublicationTypeIdTest {

    final String expectedMessage = "ID can't be null or blank";
    final String id1 = "type1";
    final String id2 = "type2";

    @Test
    public void shouldCreateAValidPublicationTypeId() {
        // ARRANGE

        // ACT
        PublicationTypeId id = new PublicationTypeId(id1);

        // ASSERT
        assertNotNull(id);
    }

    @Test
    public void shouldThrowExceptionWithNullId() {

        // ARRANGE

        // ACT
        Exception exception = assertThrows(Exception.class, () -> {
            new PublicationTypeId(null);
        });

        // ASSERT
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowExceptionWithEmptyId() {

        // ARRANGE

        // ACT
        Exception exception = assertThrows(Exception.class, () -> {
            new PublicationTypeId("");
        });

        // ASSERT
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowExceptionWithBlankId() {

        // ARRANGE

        // ACT
        Exception exception = assertThrows(Exception.class, () -> {
            new PublicationTypeId("  ");
        });

        // ASSERT
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowExceptionWithTabId() {

        // ARRANGE

        // ACT
        Exception exception = assertThrows(Exception.class, () -> {
            new PublicationTypeId("\t");
        });

        // ASSERT
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowExceptionWithReturnId() {

        // ARRANGE

        // ACT
        Exception exception = assertThrows(Exception.class, () -> {
            new PublicationTypeId("\r");
        });

        // ASSERT
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldReturnFalseEqualsWithNull() {

        // ARRANGE
        PublicationTypeId id = new PublicationTypeId(id1);

        // ACT
        boolean isEquals = id.equals(null);

        // ASSERT
        assertFalse(isEquals);
    }

    @Test
    public void shouldReturnTrueEqualsWithSameObject() {

        // ARRANGE
        PublicationTypeId id = new PublicationTypeId(id1);

        // ACT
        boolean isEquals = id.equals(id);

        // ASSERT
        assertTrue(isEquals);
    }

    @Test
    public void shouldReturnTrueEqualsWithSameIds() {

        // ARRANGE
        PublicationTypeId idA = new PublicationTypeId(id1);
        PublicationTypeId idB = new PublicationTypeId(id1);

        // ACT
        boolean isEquals = idA.equals(idB);

        // ASSERT
        assertTrue(isEquals);
    }

    @Test
    public void shouldReturnFalseEqualsWithDifferentIds() {

        // ARRANGE
        PublicationTypeId idA = new PublicationTypeId(id1);
        PublicationTypeId idB = new PublicationTypeId(id2);

        boolean isEquals = idA.equals(idB);

        // ACT + ASSERT
        assertFalse(isEquals);
    }

    @Test
    public void hashCodeShouldBeEqualForSameIds() {

        // ACT + ASSERT
        PublicationTypeId idA = new PublicationTypeId(id1);
        PublicationTypeId idB = new PublicationTypeId(id1);

        // ACT + ASSERT
        assertEquals(idA.hashCode(), idB.hashCode());
    }

    @Test
    public void toStringShouldReturnTheIdString() {

        // ARRANGE
        PublicationTypeId id = new PublicationTypeId(id1);

        // ACT
        String result = id.getId();

        // ASSERT
        assertEquals(id1, result);
    }

    @Test
    public void generateShouldCreateDifferentIds() {

        // ARRANGE
        PublicationTypeId idA = new PublicationTypeId(UUID.randomUUID().toString());
        PublicationTypeId idB = new PublicationTypeId(UUID.randomUUID().toString());

        // ACT + ASSERT
        assertFalse(idA.equals(idB));
    }

}