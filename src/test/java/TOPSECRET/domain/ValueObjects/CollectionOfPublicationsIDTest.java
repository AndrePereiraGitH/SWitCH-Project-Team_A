package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectionOfPublicationsIDTest {

    @Test
    void equalsShouldReturnTrueWhenSameInstance() {

        //Arrange
        CollectionOfPublicationsID id = new CollectionOfPublicationsID("coolColPub");

        //Act
        assertTrue(id.equals(id));
    }

    @Test
    void equalsShouldReturnTrueWhenSameValue() {

        //Arrange
        CollectionOfPublicationsID id1 = new CollectionOfPublicationsID("veryCoolColPub");
        CollectionOfPublicationsID id2 = new CollectionOfPublicationsID("veryCoolColPub");

        //Act
        assertTrue(id1.equals(id2));
    }

    @Test
    void equalsShouldReturnFalseWhenDifferentValue() {

        //Arrange
        CollectionOfPublicationsID id1 = new CollectionOfPublicationsID("myColPub");
        CollectionOfPublicationsID id2 = new CollectionOfPublicationsID("yourColPub");

        //Act
        assertFalse(id1.equals(id2));
    }

    @Test
    void equalsShouldReturnFalseWhenNull() {

        //Arrange
        CollectionOfPublicationsID id = new CollectionOfPublicationsID("nullColPubId");

        //Act
        assertFalse(id.equals(null));
    }

    @Test
    void equalsShouldReturnFalseWhenDifferentType() {

        //Arrange
        CollectionOfPublicationsID id = new CollectionOfPublicationsID("colPub123");

        //Act
        assertFalse(id.equals("colPub123"));
    }

    @Test
    void equalShouldBeSymmetric() {

        //Arrange
        CollectionOfPublicationsID id1 = new CollectionOfPublicationsID("symmetricColPubId");
        CollectionOfPublicationsID id2 = new CollectionOfPublicationsID("symmetricColPubId");

        //Act
        assertTrue(id1.equals(id2));
        assertTrue(id2.equals(id1));
    }

    @Test
    void toStringShouldReturnIdValue() {

        //Arrange
        CollectionOfPublicationsID id = new CollectionOfPublicationsID("goToString");

        //Act
        assertEquals("goToString", id.toString());
    }

    @Test
    void constructorShouldThrowWhenIdNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> new CollectionOfPublicationsID(null));
    }
}