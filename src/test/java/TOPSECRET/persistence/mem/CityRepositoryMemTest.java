package TOPSECRET.persistence.mem;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.ValueObjects.CountryID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Unit tests for {@link CityRepositoryMem}.
 *
 * <p>These tests verify the behavior of creating, adding, and preventing
 * duplicate {@link City} instances. *
 * <p>The tests follow an opaque testing approach, meaning that only the
 * observable results of operations (returned values and exceptions)
 * are verified rather than interactions with dependencies.</p>
 */
class CityRepositoryMemTest {

    private final CityFactory cityFactory = mock(CityFactory.class);
    private final CountryID anyCountryID = mock(CountryID.class);
    private final String anyName = "Braga";
    private final City anyCity = mock(City.class);

    @Test
    void shouldCreateAndAddCity() {

        // Arrange
        ICityRepository repository = new CityRepositoryMem(cityFactory);

        when(cityFactory.create(anyCountryID, anyName)).thenReturn(anyCity);

        // Act
        City result = repository.createAndAdd(anyCountryID, anyName);

        // Assert
        assertEquals(anyCity, result);
    }

    @Test
    void shouldThrowWhenCityAlreadyExists() {

        // Arrange
        ICityRepository repository = new CityRepositoryMem(cityFactory);

        when(cityFactory.create(anyCountryID, anyName)).thenReturn(anyCity);
        when(anyCity.sameAs(anyCity)).thenReturn(true);

        repository.createAndAdd(anyCountryID, anyName);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(anyCountryID, anyName));
    }

    @Test
    void shouldThrowExceptionIfCountryIsNull() {

        // Arrange
        ICityRepository repository = new CityRepositoryMem(cityFactory);

        when(cityFactory.create(null, anyName))
                .thenThrow(new IllegalArgumentException());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(null, anyName));
    }

    @Test
    void shouldStoreOnlyOneInstanceOfCity() {

        // Arrange
        ICityRepository repository = new CityRepositoryMem(cityFactory);

        when(cityFactory.create(anyCountryID, anyName)).thenReturn(anyCity);
        when(anyCity.sameAs(anyCity)).thenReturn(true);

        repository.createAndAdd(anyCountryID, anyName);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(anyCountryID, anyName));
    }
}