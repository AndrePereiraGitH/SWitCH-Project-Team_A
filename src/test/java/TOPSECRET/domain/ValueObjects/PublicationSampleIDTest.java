package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublicationSampleIDTest {

    final String pubSampleID_1 = "1";
    final String pubSampleID_2 = "2";

    @Test
    public void shouldCreateAValidPubSampleID(){
        new PublicationSampleID(pubSampleID_1);
    }

    @Test
    public void shouldThrowAnExceptionWhenID_IsNull(){

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSampleID(null));
    }

    @Test
    public void shouldThrowAnExceptionWhenID_IsBlank(){

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSampleID(" "));
    }

    @Test
    public void shouldReturnFalseWhenTheIDs_areDifferent(){
        //Arrange
        PublicationSampleID id1 = new PublicationSampleID(pubSampleID_1);
        PublicationSampleID id2 = new PublicationSampleID(pubSampleID_2);

        //Act
        boolean result = id1.equals(id2);

        //Assert
        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueWhenTheIDs_areEqual(){
        //Arrange
        PublicationSampleID id1 = new PublicationSampleID(pubSampleID_1);
        PublicationSampleID id2 = new PublicationSampleID(pubSampleID_1);

        //Act
        boolean result = id1.equals(id2);

        //Assert
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenComparedToNull() {
        //Arrange
        PublicationSampleID id = new PublicationSampleID(pubSampleID_1);

        //Act
        boolean result = id.equals(null);

        //Assert
        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueWhenComparedToItself() {
        //Arrange

        //Act
        PublicationSampleID id = new PublicationSampleID(pubSampleID_1);

        //Assert
        assertEquals(id, id);
    }



    @Test
    public void getId_shouldReturnPublicationID(){
        //Arrange

        //Act
        PublicationSampleID id = new PublicationSampleID(pubSampleID_1);
        String result = id.getID();

        //Assert
        assertEquals(result,pubSampleID_1);
    }

}