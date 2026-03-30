package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionIDTest {

    @Test
    void equalsShouldReturnTrueWhenSameInstance() {

        //Arrange
        TransactionID id = new TransactionID("lotsOfMoney");

        //Act
        assertTrue(id.equals(id));
    }

    @Test
    void equalsShouldReturnTrueWhenSameValue() {

        //Arrange
        TransactionID id1 = new TransactionID("sameID");
        TransactionID id2 = new TransactionID("sameID");

        //Act
        assertTrue(id1.equals(id2));
    }

    @Test
    void equalsShouldReturnFalseWhenDifferentValues() {

        //Arrange
        TransactionID id1 = new TransactionID("gold");
        TransactionID id2 = new TransactionID("silver");

        //Act
        assertFalse(id1.equals(id2));
    }

    @Test
    void equalsShouldReturnFalseWhenNull() {

        //Arrange
        TransactionID id = new TransactionID("notNull");

        //Act
        assertFalse(id.equals(null));
    }

    @Test
    void equalsShouldReturnFalseWhenDifferentType() {

        //Arrange
        TransactionID id = new TransactionID("xptoMoney");

        //Act
        assertFalse(id.equals("moneyXpto"));
    }

    @Test
    void equalsShouldBeSymmetric() {

        //Arrange
        TransactionID id1 = new TransactionID("symmetric");
        TransactionID id2 = new TransactionID("symmetric");

        //Act
        assertTrue(id1.equals(id2));
        assertTrue(id2.equals(id1));
    }

    @Test
    void toStringShouldReturnTransactionId() {

        //Arrange
        TransactionID id = new TransactionID("turnMeIntoString");

        //Act
        assertEquals("turnMeIntoString", id.toString());
    }

    @Test
    void constructorShouldThrowWhenIdNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> new TransactionID(null));
    }

}
