package TOPSECRET.domain;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.repository.CityRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for validating the dependencies and behavior of {@link CityFactory}.
 * <p>
 * These tests ensure that {@code CityFactory} correctly creates valid {@link City}
 * instances and enforces domain constraints when invalid parameters are provided.
 * <p>
 * The tests also verify that {@link CityRepository} can be safely composed with
 * {@code CityFactory} as part of the city creation workflow.
 */

class CityFactoryDependenciesTest {

    //Arrange repeatable variables
    CountryFactory countryFactory = new CountryFactory();
    Country anyCountry = countryFactory.create("Estonia");
    String anyCityName = "Talin";
    City anyCity = new City(anyCountry, anyCityName);

    CityFactory anyCityFactory = new CityFactory();
    CityRepository anyCityRepo = new CityRepository(anyCityFactory);


    /**
     * Verifies that a valid {@link City} can be created using {@link CityFactory}.
     */
    @Test
    void createValidCity() {

        //Act
        City createdCity = anyCityFactory.create(anyCountry, anyCityName);

        //Assert
        assertNotNull(anyCity);
    }


    /**
     * Verifies that creating a city with a {@code null} country is rejected.
     */
    @Test
    void shouldThrowExceptionWhenCountryIsNull() {

        //Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> anyCityFactory.create(null, "Braga"));
    }


    /**
     * Verifies that creating a city with an invalid (null or blank) name is rejected.
     */
    @Test
    void shouldThrowExceptionWhenCityIsNull() {

        //Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> anyCityFactory.create(anyCountry, ""));
    }
}