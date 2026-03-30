package TOPSECRET.repository;

import TOPSECRET.domain.City;
import TOPSECRET.domain.CityFactory;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.ValueObjects.CityID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Unit tests for {@link CityRepository}.
 *
 * <p>These tests verify the behavior of creating, adding, and preventing
 * duplicate {@link City} instances. The repository depends on a
 * {@link CityFactory} and a {@link CountryFactory}, which are mocked
 * to isolate the repository logic.</p>
 *
 * <p>The tests follow an opaque testing approach, meaning that only the
 * observable results of operations (returned values and exceptions)
 * are verified rather than interactions with dependencies.</p>
 */
class CityRepositoryTest {

    // Factories
    private final CityFactory cityFactory = mock(CityFactory.class);
    private final CountryFactory countryFactory = mock(CountryFactory.class);

    // Domain objects
    private final Country anyCountry = mock(Country.class);
    private final String anyName = "Braga";

    // Mocked city returned by factory
    private final City anyCity = mock(City.class);
    private final CityID anyCityID = mock(CityID.class);

    @Test
    void shouldCreateAndAddCity() {

        // Arrange
        CityRepository repository = new CityRepository(cityFactory);

        when(cityFactory.create(anyCountry, anyName, anyCityID)).thenReturn(anyCity);

        // Act
        City result = repository.createAndAdd(anyCountry, anyName, anyCityID);

        // Assert
        assertEquals(anyCity, result);
    }

    @Test
    void shouldThrowWhenCityAlreadyExists() {

        // Arrange
        CityRepository repository = new CityRepository(cityFactory);

        when(cityFactory.create(anyCountry, anyName, anyCityID)).thenReturn(anyCity);

        repository.createAndAdd(anyCountry, anyName, anyCityID);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(anyCountry, anyName, anyCityID));
    }

    @Test
    void shouldThrowExceptionIfCountryIsNull() {

        // Arrange
        CityRepository repository = new CityRepository(cityFactory);

        when(cityFactory.create(null, anyName, anyCityID))
                .thenThrow(new IllegalArgumentException());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(null, anyName, anyCityID));
    }

    @Test
    void shouldStoreOnlyOneInstanceOfCity() {

        // Arrange
        CityRepository repository = new CityRepository(cityFactory);

        when(cityFactory.create(anyCountry, anyName, anyCityID)).thenReturn(anyCity);

        repository.createAndAdd(anyCountry, anyName, anyCityID);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(anyCountry, anyName, anyCityID));
    }
}