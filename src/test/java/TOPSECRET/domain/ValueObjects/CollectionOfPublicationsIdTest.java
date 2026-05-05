package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectionOfPublicationsIdTest {

    @Test
    void equalsShouldReturnTrueWhenSameInstance() {

        //Arrange
        CollectionOfPublicationsId id = new CollectionOfPublicationsId("coolColPub");

        //Act
        assertTrue(id.equals(id));
    }

    @Test
    void equalsShouldReturnTrueWhenSameValue() {

        //Arrange
        CollectionOfPublicationsId id1 = new CollectionOfPublicationsId("veryCoolColPub");
        CollectionOfPublicationsId id2 = new CollectionOfPublicationsId("veryCoolColPub");

        //Act
        assertTrue(id1.equals(id2));
    }

    @Test
    void equalsShouldReturnFalseWhenDifferentValue() {

        //Arrange
        CollectionOfPublicationsId id1 = new CollectionOfPublicationsId("myColPub");
        CollectionOfPublicationsId id2 = new CollectionOfPublicationsId("yourColPub");

        //Act
        assertFalse(id1.equals(id2));
    }

    @Test
    void equalsShouldReturnFalseWhenNull() {

        //Arrange
        CollectionOfPublicationsId id = new CollectionOfPublicationsId("nullColPubId");

        //Act
        assertFalse(id.equals(null));
    }

    @Test
    void equalsShouldReturnFalseWhenDifferentType() {

        //Arrange
        CollectionOfPublicationsId id = new CollectionOfPublicationsId("colPub123");

        //Act
        assertFalse(id.equals("colPub123"));
    }

    @Test
    void equalShouldBeSymmetric() {

        //Arrange
        CollectionOfPublicationsId id1 = new CollectionOfPublicationsId("symmetricColPubId");
        CollectionOfPublicationsId id2 = new CollectionOfPublicationsId("symmetricColPubId");

        //Act
        assertTrue(id1.equals(id2));
        assertTrue(id2.equals(id1));
    }

    @Test
    void toStringShouldReturnIdValue() {

        //Arrange
        CollectionOfPublicationsId id = new CollectionOfPublicationsId("goToString");

        //Act
        assertEquals("goToString", id.getId());
    }

    @Test
    void constructorShouldThrowWhenIdNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> new CollectionOfPublicationsId(null));
    }
}