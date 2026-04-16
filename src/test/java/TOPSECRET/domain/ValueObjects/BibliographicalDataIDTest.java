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


    @Test
    void shouldCreateValidID() {
        //Arrange
        BibliographicalDataID id = new BibliographicalDataID("1234");

        //Assert
        assertEquals("1234", id.getID());
    }

    @Test
    void shouldThrowExceptionWhenIDIsNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new BibliographicalDataID(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenIDIsBlank() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new BibliographicalDataID("   ");
        });
    }

    @Test
    void shouldNotBeEqualWhenObjectIsNull() {
        BibliographicalDataID id = new BibliographicalDataID("1234");
        assertNotEquals(null, id);
    }

    @Test
    void shouldBeEqualWhenIDsAreSame() {
        //Arrange
        BibliographicalDataID id1 = new BibliographicalDataID("1234");
        BibliographicalDataID id2 = new BibliographicalDataID("1234");

        //Assert
        assertEquals(id1, id2);
    }

    @Test
    void shouldNotBeEqualWhenIDsAreDifferent() {
        //Arrange
        BibliographicalDataID id1 = new BibliographicalDataID("1234");
        BibliographicalDataID id2 = new BibliographicalDataID("5678");

        //Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        //Arrange
        BibliographicalDataID id = new BibliographicalDataID("1234");

        //Assert
        assertNotEquals(null, id);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        //Arrange
        BibliographicalDataID id = new BibliographicalDataID("1234");
        String other = "1234";

        //Assert
        assertNotEquals(other, id);
    }

    @Test
    void getterShouldReturnCorrectID() {
        //Arrange
        BibliographicalDataID id = new BibliographicalDataID("1234");

        //Assert
        assertEquals("1234", id.getID());
    }

    @Test
    void hashCodeShouldBeEqualForSameID() {
        //Arrange
        BibliographicalDataID id1 = new BibliographicalDataID("1234");
        BibliographicalDataID id2 = new BibliographicalDataID("1234");

        //Assert
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void hashCodeShouldNotBeEqualForDifferentID() {
        //Arrange
        BibliographicalDataID id1 = new BibliographicalDataID("1234");
        BibliographicalDataID id2 = new BibliographicalDataID("5678");

        //Assert
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

}