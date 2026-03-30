package TOPSECRET.domain.ValueObjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.UUID;


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

        // ARRANGE

        // ACT
        AppraisalEntityId id = new AppraisalEntityId(id1);

        // ASSERT
        assertNotNull(id);
    }

    @Test
    public void shouldThrowExceptionWithNullId() {

        // ARRANGE

        // ACT
        Exception exception = assertThrows(Exception.class, () -> {
            new AppraisalEntityId(null);
        });

        // ACT
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowExceptionWithEmptyId() {

        // ARRANGE

        // ACT
        Exception exception = assertThrows(Exception.class, () -> {
            new AppraisalEntityId("");
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
            new AppraisalEntityId("  ");
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
            new AppraisalEntityId("\t");
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
            new AppraisalEntityId("\r");
        });

        // ASSERT
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldReturnFalseEqualsWithNull() {

        // ARRANGE
        AppraisalEntityId id = new AppraisalEntityId(id1);

        // ACT
        boolean isEquals = id.equals(null);

        // ASSERT
        assertFalse(isEquals);
    }

    @Test
    public void shouldReturnTrueEqualsWithSameObject() {

        // ARRANGE
        AppraisalEntityId id = new AppraisalEntityId(id1);

        // ACT
        boolean isEquals = id.equals(id);

        // ASSERT
        assertTrue(isEquals);
    }

    @Test
    public void shouldReturnTrueEqualsWithSameIds() {

        // ARRANGE
        AppraisalEntityId idA = new AppraisalEntityId(id1);
        AppraisalEntityId idB = new AppraisalEntityId(id1);

        // ACT
        boolean isEquals = idA.equals(idB);

        // ASSERT
        assertTrue(isEquals);
    }

    @Test
    public void shouldReturnFalseEqualsWithDifferentIds() {

        // ARRANGE
        AppraisalEntityId idA = new AppraisalEntityId(id1);
        AppraisalEntityId idB = new AppraisalEntityId(id2);

        // ACT
        boolean isEquals = idA.equals(idB);

        // ASSERT
        assertFalse(isEquals);
    }

    @Test
    public void hashCodeShouldBeEqualForSameIds() {

        // ARRANGE
        AppraisalEntityId idA = new AppraisalEntityId(id1);
        AppraisalEntityId idB = new AppraisalEntityId(id1);

        // ACT + ASSERT
        assertEquals(idA.hashCode(), idB.hashCode());
    }

    @Test
    public void toStringShouldReturnTheIdString() {

        // ARRANGE
        AppraisalEntityId id = new AppraisalEntityId(id1);

        // ACT
        String result = id.getId();

        // ASSERT
        assertEquals(id1, result);
    }

    @Test
    public void generateShouldCreateDifferentIds() {

        // ARRANGE
        AppraisalEntityId idA = new AppraisalEntityId (UUID.randomUUID().toString());
        AppraisalEntityId idB = new AppraisalEntityId (UUID.randomUUID().toString());

        // ACT + ASSERT
        assertFalse(idA.equals(idB));
    }
}