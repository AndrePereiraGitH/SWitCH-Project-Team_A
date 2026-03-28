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

        //Act
        boolean result = pubSampleID_1.equals(pubSampleID_2);

        //Assert
        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueWhenTheIDs_areEqual(){
        //Arrange

        //Act
        boolean result = pubSampleID_1.equals(pubSampleID_1);

        //Assert
        assertTrue(result);
    }

}