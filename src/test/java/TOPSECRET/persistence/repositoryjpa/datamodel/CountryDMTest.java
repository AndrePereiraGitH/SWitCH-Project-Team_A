package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryDMTest {

    // -------------------------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------------------------

    @Test
    void constructor_validData() {

        // Arrange
        CountryId countryId = new CountryId("PORTUGAL");
        String countryName = "Portugal";

        // Act
        CountryDM countryDM = new CountryDM(countryId, countryName);

        // Assert
        assertNotNull(countryDM);
        assertEquals(countryId, countryDM.getCountryID());
        assertEquals(countryName, countryDM.getCountryName());
    }

    @Test
    void constructor_allowsNullCountryId() {

        // Arrange
        String countryName = "Portugal";

        // Act
        CountryDM countryDM = new CountryDM(null, countryName);

        // Assert
        assertNotNull(countryDM);
        assertNull(countryDM.getCountryID());
        assertEquals(countryName, countryDM.getCountryName());
    }

    @Test
    void constructor_allowsNullCountryName() {

        // Arrange
        CountryId countryId = new CountryId("PORTUGAL");

        // Act
        CountryDM countryDM = new CountryDM(countryId, null);

        // Assert
        assertNotNull(countryDM);
        assertEquals(countryId, countryDM.getCountryID());
        assertNull(countryDM.getCountryName());
    }

    // -------------------------------------------------------------------------
    // Getter tests
    // -------------------------------------------------------------------------

    @Test
    void getCountryID_returnsCorrectValue() {

        // Arrange
        CountryId countryId = new CountryId("PORTUGAL");
        CountryDM countryDM = new CountryDM(countryId, "Portugal");

        // Act
        CountryId result = countryDM.getCountryID();

        // Assert
        assertEquals(countryId, result);
    }

    @Test
    void getCountryName_returnsCorrectValue() {

        // Arrange
        CountryDM countryDM = new CountryDM(
                new CountryId("FRANCE"),
                "France"
        );

        // Act
        String result = countryDM.getCountryName();

        // Assert
        assertEquals("France", result);
    }
}