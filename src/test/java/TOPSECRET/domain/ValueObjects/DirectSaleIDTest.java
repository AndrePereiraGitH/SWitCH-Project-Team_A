package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the {@link DirectSaleID} value object.
 *
 * <p>This test class verifies the correct behavior of the DirectSaleID,
 * including object creation, validation rules, equality logic, hash code consistency,
 * and getter method functionality.</p>
 *
 * <p>The tests ensure that the value object follows the rules of a DDD Value Object:
 * immutability, equality based on value, and validation of construction parameters.</p>
 */
class DirectSaleIDTest {

    /**
     * Tests if a DirectSaleID is successfully created
     * when a valid non-null and non-blank string is provided.
     */
    @Test
    void shouldCreateValidID() {
        DirectSaleID id = new DirectSaleID("1234");
        assertEquals("1234", id.getID());
    }


    /**
     * Tests if the constructor throws IllegalArgumentException
     * when the provided ID is null.
     */
    @Test
    void shouldThrowExceptionWhenIDIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new DirectSaleID(null);
        });
    }


    /**
     * Tests if the constructor throws IllegalArgumentException
     * when the provided ID is blank.
     */
    @Test
    void shouldThrowExceptionWhenIDIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new BibliographicalDataID("   ");
        });
    }


    /**
     * Tests if two DirectSaleID objects are considered equal
     * when they contain the same ID value.
     */
    @Test
    void shouldBeEqualWhenIDsAreSame() {
        DirectSaleID id1 = new DirectSaleID("1234");
        DirectSaleID id2 = new DirectSaleID("1234");

        assertEquals(id1, id2);
    }


    /**
     * Tests if two DirectSaleID objects are not equal
     * when they contain different ID values.
     */
    @Test
    void shouldNotBeEqualWhenIDsAreDifferent() {
        DirectSaleID id1 = new DirectSaleID("1234");
        DirectSaleID id2 = new DirectSaleID("5678");

        assertNotEquals(id1, id2);
    }


    /**
     * Tests if a DirectSaleID object is not equal to null.
     */
    @Test
    void shouldNotBeEqualToNull() {
        DirectSaleID id = new DirectSaleID("1234");
        assertNotEquals(null, id);
    }


    /**
     * Tests if a DirectSaleID object is not equal
     * to an object of a different class.
     */
    @Test
    void shouldNotBeEqualToDifferentClass() {
        DirectSaleID id = new DirectSaleID("1234");
        String other = "1234";

        assertNotEquals(other, id);
    }


    /**
     * Tests if the getID() method returns the correct ID value.
     */
    @Test
    void getterShouldReturnCorrectID() {
        DirectSaleID id = new DirectSaleID("1234");
        assertEquals("1234", id.getID());
    }


    /**
     * Tests if two equal DirectSaleID objects
     * produce the same hash code.
     */
    @Test
    void hashCodeShouldBeEqualForSameID() {
        DirectSaleID id1 = new DirectSaleID("1234");
        DirectSaleID id2 = new DirectSaleID("1234");

        assertEquals(id1.hashCode(), id2.hashCode());
    }

}