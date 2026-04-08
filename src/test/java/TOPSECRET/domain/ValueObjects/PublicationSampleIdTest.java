package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublicationSampleIdTest {

    final String pubSampleID_1 = "1";
    final String pubSampleID_2 = "2";

    @Test
    public void shouldCreateAValidPubSampleID(){
        new PublicationSampleId(pubSampleID_1);
    }

    @Test
    public void shouldThrowAnExceptionWhenID_IsNull(){

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSampleId(null));
    }

    @Test
    public void shouldThrowAnExceptionWhenID_IsBlank(){

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSampleId(" "));
    }

    @Test
    public void shouldReturnFalseWhenTheIDs_areDifferent(){
        //Arrange
        PublicationSampleId id1 = new PublicationSampleId(pubSampleID_1);
        PublicationSampleId id2 = new PublicationSampleId(pubSampleID_2);

        //Act
        boolean result = id1.equals(id2);

        //Assert
        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueWhenTheIDs_areEqual(){
        //Arrange
        PublicationSampleId id1 = new PublicationSampleId(pubSampleID_1);
        PublicationSampleId id2 = new PublicationSampleId(pubSampleID_1);

        //Act
        boolean result = id1.equals(id2);

        //Assert
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenComparedToNull() {
        //Arrange
        PublicationSampleId id = new PublicationSampleId(pubSampleID_1);

        //Act
        boolean result = id.equals(null);

        //Assert
        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueWhenComparedToItself() {
        //Arrange

        //Act
        PublicationSampleId id = new PublicationSampleId(pubSampleID_1);

        //Assert
        assertEquals(id, id);
    }



    @Test
    public void getId_shouldReturnPublicationID(){
        //Arrange

        //Act
        PublicationSampleId id = new PublicationSampleId(pubSampleID_1);
        String result = id.getID();

        //Assert
        assertEquals(result,pubSampleID_1);
    }

}