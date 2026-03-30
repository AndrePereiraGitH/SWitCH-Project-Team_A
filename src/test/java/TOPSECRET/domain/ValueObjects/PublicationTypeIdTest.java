package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

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

        new PublicationTypeId(id1);
    }

    @Test
    public void shouldThrowExceptionWithNullId() {

        Exception exception = assertThrows(Exception.class, () -> {
            new PublicationTypeId(null);
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowExceptionWithEmptyId() {

        Exception exception = assertThrows(Exception.class, () -> {
            new PublicationTypeId("");
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowExceptionWithBlankId() {

        Exception exception = assertThrows(Exception.class, () -> {
            new PublicationTypeId("  ");
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowExceptionWithTabId() {

        Exception exception = assertThrows(Exception.class, () -> {
            new PublicationTypeId("\t");
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowExceptionWithReturnId() {

        Exception exception = assertThrows(Exception.class, () -> {
            new PublicationTypeId("\r");
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldReturnFalseEqualsWithNull() {

        PublicationTypeId id = new PublicationTypeId(id1);

        boolean isEquals = id.equals(null);

        assertFalse(isEquals);
    }

    @Test
    public void shouldReturnTrueEqualsWithSameObject() {

        PublicationTypeId id = new PublicationTypeId(id1);

        boolean isEquals = id.equals(id);

        assertTrue(isEquals);
    }

    @Test
    public void shouldReturnTrueEqualsWithSameIds() {

        PublicationTypeId idA = new PublicationTypeId(id1);
        PublicationTypeId idB = new PublicationTypeId(id1);

        boolean isEquals = idA.equals(idB);

        assertTrue(isEquals);
    }

    @Test
    public void shouldReturnFalseEqualsWithDifferentIds() {

        PublicationTypeId idA = new PublicationTypeId(id1);
        PublicationTypeId idB = new PublicationTypeId(id2);

        boolean isEquals = idA.equals(idB);

        assertFalse(isEquals);
    }

    @Test
    public void hashCodeShouldBeEqualForSameIds() {

        PublicationTypeId idA = new PublicationTypeId(id1);
        PublicationTypeId idB = new PublicationTypeId(id1);

        assertEquals(idA.hashCode(), idB.hashCode());
    }

    @Test
    public void toStringShouldReturnTheIdString() {

        PublicationTypeId id = new PublicationTypeId(id1);

        assertEquals(id.toString(), id1);
    }

    @Test
    public void generateShouldCreateDifferentIds() {

        PublicationTypeId idA = PublicationTypeId.generate();
        PublicationTypeId idB = PublicationTypeId.generate();

        assertFalse(idA.equals(idB));
    }

}