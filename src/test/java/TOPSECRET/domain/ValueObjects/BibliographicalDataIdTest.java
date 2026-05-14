package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the {@link BibliographicalDataId} value object.
 *
 * <p>This test class verifies the correct behavior of the BibliographicalDataID,
 * including object creation, validation rules, equality logic, hash code consistency,
 * and getter method functionality.</p>
 *
 * <p>The tests ensure that the value object follows the rules of a DDD Value Object:
 * immutability, equality based on value, and validation of construction parameters.</p>
 */
class BibliographicalDataIdTest {


    @Test
    void shouldCreateValidID() {
        //Arrange
        BibliographicalDataId id = new BibliographicalDataId("1234");

        //Assert
        assertEquals("1234", id.getID());
    }

    @Test
    void shouldThrowExceptionWhenIDIsNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new BibliographicalDataId(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenIDIsBlank() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new BibliographicalDataId("   ");
        });
    }

    @Test
    void shouldNotBeEqualWhenObjectIsNull() {
        BibliographicalDataId id = new BibliographicalDataId("1234");
        assertNotEquals(null, id);
    }

    @Test
    void shouldBeEqualWhenIDsAreSame() {
        //Arrange
        BibliographicalDataId id1 = new BibliographicalDataId("1234");
        BibliographicalDataId id2 = new BibliographicalDataId("1234");

        //Assert
        assertEquals(id1, id2);
    }

    @Test
    void shouldNotBeEqualWhenIDsAreDifferent() {
        //Arrange
        BibliographicalDataId id1 = new BibliographicalDataId("1234");
        BibliographicalDataId id2 = new BibliographicalDataId("5678");

        //Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        //Arrange
        BibliographicalDataId id = new BibliographicalDataId("1234");

        //Assert
        assertNotEquals(null, id);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        //Arrange
        BibliographicalDataId id = new BibliographicalDataId("1234");
        String other = "1234";

        //Assert
        assertNotEquals(other, id);
    }

    @Test
    void getterShouldReturnCorrectID() {
        //Arrange
        BibliographicalDataId id = new BibliographicalDataId("1234");

        //Assert
        assertEquals("1234", id.getID());
    }

    @Test
    void hashCodeShouldBeEqualForSameID() {
        //Arrange
        BibliographicalDataId id1 = new BibliographicalDataId("1234");
        BibliographicalDataId id2 = new BibliographicalDataId("1234");

        //Assert
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void hashCodeShouldNotBeEqualForDifferentID() {
        //Arrange
        BibliographicalDataId id1 = new BibliographicalDataId("1234");
        BibliographicalDataId id2 = new BibliographicalDataId("5678");

        //Assert
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

}