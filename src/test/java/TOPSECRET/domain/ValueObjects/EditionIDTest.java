package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditionIDTest {

    final String editionID1 = "1";
    final String editionID2 = "2";

    @Test
    void ShouldCreateAValidEditionID() {
        EditionID editionID = new EditionID(editionID1);

    }


    @Test
    void ShouldReturnInvalidEditionIDWhenIsNull(){
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new EditionID(null));
    }

    @Test
    void ShouldReturnInvalidEditionIDWhenIsEmpty(){
        assertThrows(IllegalArgumentException.class, () ->
                new EditionID(""));
    }

   @Test
   void ShouldReturnFalseWhenTheIDsAreDifferent(){
        // Arrange
       EditionID eID1 = new EditionID(editionID1);
       EditionID eID2 = new EditionID(editionID2);

       // Act
       boolean result = eID1.equals(eID2);

       // Assert
       assertFalse(result);
   }

   @Test
    void ShouldReturnTrueWhenTheIDsAreEqual(){
        // Arrange
       EditionID eID1 = new EditionID(editionID1);
       EditionID eID2 = new EditionID(editionID1);

       // Act
       boolean result = eID1.equals(eID2);

       // Assert
       assertTrue(result);
   }

    @Test
    void ShouldReturnFalseWhenComparedWithNull(){
        // Arrange
        EditionID eID = new EditionID(editionID1);

        // Act
        boolean result = eID.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void ShouldReturnFalseWhenComparedWithDifferentType(){
        // Arrange
        EditionID eID = new EditionID(editionID1);

        // Act
        boolean result = eID.equals("1");

        // Assert
        assertFalse(result);
    }

    @Test
    void ShouldReturnTrueWhenComparedWithItself() {
        // Arrange
        EditionID eID = new EditionID(editionID1);

        // Act
        boolean result = eID.equals(eID);

        // Assert
        assertTrue(result);
    }

}