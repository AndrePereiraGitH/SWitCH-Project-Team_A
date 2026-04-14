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


    @Test
    void shouldCreateValidID() {
        //Arrange
        DirectSaleID id = new DirectSaleID("1234");

        //Assert
        assertEquals("1234", id.getID());
    }

    @Test
    void shouldThrowExceptionWhenIDIsNull() {
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new DirectSaleID(null);
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
    void shouldBeEqualWhenIDsAreSame() {
        //Arrange
        DirectSaleID id1 = new DirectSaleID("1234");
        DirectSaleID id2 = new DirectSaleID("1234");

        //Assert
        assertEquals(id1, id2);
    }

    @Test
    void shouldNotBeEqualWhenIDsAreDifferent() {
        //Arrange
        DirectSaleID id1 = new DirectSaleID("1234");
        DirectSaleID id2 = new DirectSaleID("5678");

        //Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        //Arrange
        DirectSaleID id = new DirectSaleID("1234");

        //Assert
        assertNotEquals(null, id);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        //Arrange
        DirectSaleID id = new DirectSaleID("1234");
        String other = "1234";

        //Assert
        assertNotEquals(other, id);
    }

    @Test
    void getterShouldReturnCorrectID() {
        //Arrange
        DirectSaleID id = new DirectSaleID("1234");

        //Assert
        assertEquals("1234", id.getID());
    }

    @Test
    void hashCodeShouldBeEqualForSameID() {
        //Arrange
        DirectSaleID id1 = new DirectSaleID("1234");
        DirectSaleID id2 = new DirectSaleID("1234");

        //Assert
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void hashCodeShouldNotBeEqualForDifferentID() {
        //Arrange
        DirectSaleID id1 = new DirectSaleID("1234");
        DirectSaleID id2 = new DirectSaleID("5678");

        //Assert
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

}