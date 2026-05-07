package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CityDM}.
 *
 * <p>Verifies correct construction, field assignment, and validation behavior
 * of the JPA data model used for persisting {@link TOPSECRET.domain.City.City} entities.</p>
 *
 * <p>These tests focus exclusively on object integrity and constructor constraints,
 * without involving persistence or Spring context.</p>
 */
class CityDMTest {

    @Test
    void shouldCreateCityDMWithValidData() {
        //Arrange
        CityId cityID = new CityId("1");
        CountryId countryID = new CountryId("PT");

        CityDM cityDM = new CityDM(cityID, countryID, "Lisbon");

        //Act + assert
        assertEquals(cityID, cityDM.getCityID());
        assertEquals(countryID, cityDM.getCountryID());
        assertEquals("Lisbon", cityDM.getCityName());
    }

    @Test
    void shouldThrowWhenCityIdIsNull() {
        //Arrange
        CountryId countryID = new CountryId("PT");

        //Act
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new CityDM(null, countryID, "Lisbon")
        );

        //Assert
        assertEquals("City ID cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCountryIdIsNull() {
        //Arrange
        CityId cityID = new CityId("1");

        //Act + assert
        assertThrows(NullPointerException.class,
                () -> new CityDM(cityID, null, "Lisbon"));
    }

    @Test
    void shouldThrowWhenCityNameIsNull() {
        //Arrange
        CityId cityID = new CityId("1");
        CountryId countryID = new CountryId("PT");

        //Act + assert
        assertThrows(NullPointerException.class,
                () -> new CityDM(cityID, countryID, null));
    }
}