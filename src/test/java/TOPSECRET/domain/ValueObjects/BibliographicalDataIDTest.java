package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the {@link BibliographicalDataID} value object.
 *
 * <p>This test class verifies the correct behavior of the BibliographicalDataID,
 * including object creation, validation rules, equality logic, hash code consistency,
 * and getter method functionality.</p>
 *
 * <p>The tests ensure that the value object follows the rules of a DDD Value Object:
 * immutability, equality based on value, and validation of construction parameters.</p>
 */
class BibliographicalDataIDTest {


    /**
     * Tests if a BibliographicalDataID is successfully created
     * when a valid non-null and non-blank string is provided.
     */
    @Test
    void shouldCreateValidID() {
        //Arrange
        BibliographicalDataID id = new BibliographicalDataID("1234");

        //Assert
        assertEquals("1234", id.getID());
    }


    /**
     * Tests if the constructor throws IllegalArgumentException
     * when the provided ID is null.
     */
    @Test
    void shouldThrowExceptionWhenIDIsNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new BibliographicalDataID(null);
        });
    }


    /**
     * Tests if the constructor throws IllegalArgumentException
     * when the provided ID is blank.
     */
    @Test
    void shouldThrowExceptionWhenIDIsBlank() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new BibliographicalDataID("   ");
        });
    }


    /**
     * Tests if two BibliographicalDataID objects are considered equal
     * when they contain the same ID value.
     */
    @Test
    void shouldBeEqualWhenIDsAreSame() {
        //Arrange
        BibliographicalDataID id1 = new BibliographicalDataID("1234");
        BibliographicalDataID id2 = new BibliographicalDataID("1234");

        //Assert
        assertEquals(id1, id2);
    }


    /**
     * Tests if two BibliographicalDataID objects are not equal
     * when they contain different ID values.
     */
    @Test
    void shouldNotBeEqualWhenIDsAreDifferent() {
        //Arrange
        BibliographicalDataID id1 = new BibliographicalDataID("1234");
        BibliographicalDataID id2 = new BibliographicalDataID("5678");

        //Assert
        assertNotEquals(id1, id2);
    }


    /**
     * Tests if a BibliographicalDataID object is not equal to null.
     */
    @Test
    void shouldNotBeEqualToNull() {
        //Arrange
        BibliographicalDataID id = new BibliographicalDataID("1234");

        //Assert
        assertNotEquals(null, id);
    }


    /**
     * Tests if a BibliographicalDataID object is not equal
     * to an object of a different class.
     */
    @Test
    void shouldNotBeEqualToDifferentClass() {
        //Arrange
        BibliographicalDataID id = new BibliographicalDataID("1234");
        String other = "1234";

        //Assert
        assertNotEquals(other, id);
    }


    /**
     * Tests if the getID() method returns the correct ID value.
     */
    @Test
    void getterShouldReturnCorrectID() {
        //Arrange
        BibliographicalDataID id = new BibliographicalDataID("1234");

        //Assert
        assertEquals("1234", id.getID());
    }


    /**
     * Tests if two equal BibliographicalDataID objects
     * produce the same hash code.
     */
    @Test
    void hashCodeShouldBeEqualForSameID() {
        //Arrange
        BibliographicalDataID id1 = new BibliographicalDataID("1234");
        BibliographicalDataID id2 = new BibliographicalDataID("1234");

        //Assert
        assertEquals(id1.hashCode(), id2.hashCode());
    }

}