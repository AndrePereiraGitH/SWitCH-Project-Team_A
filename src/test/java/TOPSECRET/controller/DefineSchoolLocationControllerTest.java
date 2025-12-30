package TOPSECRET.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import TOPSECRET.domain.School;
import TOPSECRET.domain.Location;

class DefineSchoolLocationControllerTest {

    @Test
    void defineValidSchoolLocation() throws InstantiationException
    {
        // arrange
        School school = new School("ISEP");

        String strStreet = "Rua de S. Tomé, s/n";
        String strPostalCode = "4200 Porto";
        Location expectedLocation = new Location(strStreet, strPostalCode);

        DefineSchoolLocationController controller = new DefineSchoolLocationController(school);

        // act
        Location schoolLocation = controller.defineHouseLocation( strStreet, strPostalCode );

        // assert
        assertEquals(schoolLocation, expectedLocation);
    }

    @Test
    void defineEmptyStreetSchoolLocation()
    {
        // arrange
        School school = new School("ISEP");

        String strStreet = "";
        String strPostalCode = "4200 Porto";
        String expectedMessage = "Invalid Street or Postal Code";

        DefineSchoolLocationController controller = new DefineSchoolLocationController(school);

        // act + assert
        Exception exception = assertThrows(InstantiationException.class, () ->
            controller.defineHouseLocation( strStreet, strPostalCode )
        );

        // assert
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void defineEmptyPostalCodeSchoolLocation()
    {
        // arrange
        School school = new School("Something");

        String strStreet = "Rua de S. Tomé, s/n";
        String strPostalCode = "";

        String expectedMessage = "Invalid Street or Postal Code";

        DefineSchoolLocationController controller = new DefineSchoolLocationController(school);

        // act + assert
        Exception exception = assertThrows(InstantiationException.class, () ->
            controller.defineHouseLocation( strStreet, strPostalCode )
        );

        // assert
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void defineNullStreetAndPostalCodeSchoolLocation()
    {
        // arrange
        School school = new School("ISEP");

        String strStreet = null;
        String strPostalCode = null;

        String expectedMessage = "Invalid Street or Postal Code";

        DefineSchoolLocationController controller = new DefineSchoolLocationController(school);

        // act + assert
        Exception exception = assertThrows(InstantiationException.class, () ->
            controller.defineHouseLocation( strStreet, strPostalCode )
        );

        // assert
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}