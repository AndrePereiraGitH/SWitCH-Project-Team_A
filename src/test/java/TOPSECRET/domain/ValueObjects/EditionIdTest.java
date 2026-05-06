package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditionIdTest {

    final String editionID1 = "1";
    final String editionID2 = "2";

    @Test
    void ShouldCreateAValidEditionID() {
        EditionId editionID = new EditionId(editionID1);

    }


    @Test
    void ShouldReturnInvalidEditionIDWhenIsNull(){
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new EditionId(null));
    }

    @Test
    void ShouldReturnInvalidEditionIDWhenIsEmpty(){
        assertThrows(IllegalArgumentException.class, () ->
                new EditionId(""));
    }

   @Test
   void ShouldReturnFalseWhenTheIDsAreDifferent(){
        // Arrange
       EditionId eID1 = new EditionId(editionID1);
       EditionId eID2 = new EditionId(editionID2);

       // Act
       boolean result = eID1.equals(eID2);

       // Assert
       assertFalse(result);
   }

   @Test
    void ShouldReturnTrueWhenTheIDsAreEqual(){
        // Arrange
       EditionId eID1 = new EditionId(editionID1);
       EditionId eID2 = new EditionId(editionID1);

       // Act
       boolean result = eID1.equals(eID2);

       // Assert
       assertTrue(result);
   }

    @Test
    void ShouldReturnFalseWhenComparedWithNull(){
        // Arrange
        EditionId eID = new EditionId(editionID1);

        // Act
        boolean result = eID.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void ShouldReturnFalseWhenComparedWithDifferentType(){
        // Arrange
        EditionId eID = new EditionId(editionID1);

        // Act
        boolean result = eID.equals("1");

        // Assert
        assertFalse(result);
    }

    @Test
    void ShouldReturnTrueWhenComparedWithItself() {
        // Arrange
        EditionId eID = new EditionId(editionID1);

        // Act
        boolean result = eID.equals(eID);

        // Assert
        assertTrue(result);
    }

}