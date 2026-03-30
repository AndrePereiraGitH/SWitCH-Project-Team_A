package TOPSECRET.domain.ValueObjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


/**
 * Tests for AppraisalEntityId.
 *
 * Validates constructor constraints, exception handling, equality,
 * hashCode consistency, string representation, and ID generation.
 */
public class AppraisalEntityIdTest {

    final String expectedMessage = "ID can't be null or blank";
    final String id1 = "abc123";
    final String id2 = "xyz789";

    @Test
    public void shouldCreateAValidAppraisalEntityId() {

        new AppraisalEntityId(id1);
    }

    @Test
    public void shouldThrowExceptionWithNullId() {

        Exception exception = assertThrows(Exception.class, () -> {
            new AppraisalEntityId(null);
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowExceptionWithEmptyId() {

        Exception exception = assertThrows(Exception.class, () -> {
            new AppraisalEntityId("");
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowExceptionWithBlankId() {

        Exception exception = assertThrows(Exception.class, () -> {
            new AppraisalEntityId("  ");
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowExceptionWithTabId() {

        Exception exception = assertThrows(Exception.class, () -> {
            new AppraisalEntityId("\t");
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowExceptionWithReturnId() {

        Exception exception = assertThrows(Exception.class, () -> {
            new AppraisalEntityId("\r");
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldReturnFalseEqualsWithNull() {

        AppraisalEntityId id = new AppraisalEntityId(id1);

        boolean isEquals = id.equals(null);

        assertFalse(isEquals);
    }

    @Test
    public void shouldReturnTrueEqualsWithSameObject() {

        AppraisalEntityId id = new AppraisalEntityId(id1);

        boolean isEquals = id.equals(id);

        assertTrue(isEquals);
    }

    @Test
    public void shouldReturnTrueEqualsWithSameIds() {

        AppraisalEntityId idA = new AppraisalEntityId(id1);
        AppraisalEntityId idB = new AppraisalEntityId(id1);

        boolean isEquals = idA.equals(idB);

        assertTrue(isEquals);
    }

    @Test
    public void shouldReturnFalseEqualsWithDifferentIds() {

        AppraisalEntityId idA = new AppraisalEntityId(id1);
        AppraisalEntityId idB = new AppraisalEntityId(id2);

        boolean isEquals = idA.equals(idB);

        assertFalse(isEquals);
    }

    @Test
    public void hashCodeShouldBeEqualForSameIds() {

        AppraisalEntityId idA = new AppraisalEntityId(id1);
        AppraisalEntityId idB = new AppraisalEntityId(id1);

        assertEquals(idA.hashCode(), idB.hashCode());
    }

    @Test
    public void toStringShouldReturnTheIdString() {

        AppraisalEntityId id = new AppraisalEntityId(id1);

        assertEquals(id.toString(), id1);
    }

    @Test
    public void generateShouldCreateDifferentIds() {

        AppraisalEntityId idA = AppraisalEntityId.generate();
        AppraisalEntityId idB = AppraisalEntityId.generate();

        assertFalse(idA.equals(idB));
    }
}