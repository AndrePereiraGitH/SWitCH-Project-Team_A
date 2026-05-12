package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

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
        CityId cityId = mock(CityId.class);
        CountryId countryId = mock(CountryId.class);

        CityDM cityDM = new CityDM(cityId, countryId, "Lisbon");

        //Act + assert
        assertEquals(cityId, cityDM.getCityID());
        assertEquals(countryId, cityDM.getCountryID());
        assertEquals("Lisbon", cityDM.getCityName());
    }

    @Test
    void shouldThrowWhenCityIdIsNull() {
        //Arrange
        CountryId countryId = mock(CountryId.class);

        //Act
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new CityDM(null, countryId, "Lisbon")
        );

        //Assert
        assertEquals("City ID cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCountryIdIsNull() {
        //Arrange
        CityId cityId = mock(CityId.class);

        //Act + assert
        assertThrows(NullPointerException.class,
                () -> new CityDM(cityId, null, "Lisbon"));
    }

    @Test
    void shouldThrowWhenCityNameIsNull() {
        //Arrange
        CityId cityId = mock(CityId.class);
        CountryId countryId = mock(CountryId.class);

        //Act + assert
        assertThrows(NullPointerException.class,
                () -> new CityDM(cityId, countryId, null));
    }
}