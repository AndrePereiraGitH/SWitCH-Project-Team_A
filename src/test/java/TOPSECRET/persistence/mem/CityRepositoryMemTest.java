package TOPSECRET.persistence.mem;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Unit tests for {@link CityRepositoryMem}.
 *
 * <p>These tests verify the behavior of saving and preventing
 * duplicate {@link City} instances in the in-memory repository.</p>
 *
 * <p>The tests follow an opaque testing approach, meaning that only the
 * observable results of operations (returned values and exceptions)
 * are verified rather than interactions with dependencies.</p>
 */
class CityRepositoryMemTest {

    private final City anyCity = mock(City.class);
    private final CityFactory factory = mock(CityFactory.class);
    private final CountryId anyCountryId = mock(CountryId.class);

    @Test
    void shouldSaveCity() {
        // Arrange
        ICityRepository repository = new CityRepositoryMem();

        // Act
        City result = repository.save(anyCity);

        // Assert
        assertEquals(anyCity, result);
    }

    @Test
    void shouldReturnFalseWhenRepositoryIsEmpty() {
        //Arrange
        CityRepositoryMem repo = new CityRepositoryMem();

        //Act
        boolean result = repo.existsByCountryIdAndCityName(
                anyCountryId, "Lisbon");

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenCityExists() {

        // Arrange
        CityRepositoryMem repo = new CityRepositoryMem();

        CountryId country = mock(CountryId.class);
        City city = mock(City.class);

        when(factory.create(country, "Lisbon")).thenReturn(city);
        when(city.getCityCountryID()).thenReturn(country);
        when(city.getCityName()).thenReturn("Lisbon");

        repo.save(city);

        // Act
        boolean result = repo.existsByCountryIdAndCityName(country, "Lisbon");

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenCityNameDiffers() {

        // Arrange
        CityRepositoryMem repo = new CityRepositoryMem();

        CountryId country = mock(CountryId.class);

        City city = mock(City.class);

        when(factory.create(country, "Porto")).thenReturn(city);
        when(city.getCityCountryID()).thenReturn(country);
        when(city.getCityName()).thenReturn("Porto");

        repo.save(city);

        // Act
        boolean result = repo.existsByCountryIdAndCityName(country, "Lisbon");

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenCountryDiffers() {

        // Arrange
        CityRepositoryMem repo = new CityRepositoryMem();

        CountryId otherCountry = mock(CountryId.class);

        City city = mock(City.class);

        when(factory.create(anyCountryId, "Lisbon")).thenReturn(city);
        when(city.getCityCountryID()).thenReturn(anyCountryId);
        when(city.getCityName()).thenReturn("Lisbon");

        repo.save(city);

        // Act
        boolean result = repo.existsByCountryIdAndCityName(
                otherCountry,
                "Lisbon"
        );

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldThrowWhenSavingDuplicateCity() {
        //Arrange
        CityRepositoryMem repo = new CityRepositoryMem();

        when(factory.create(anyCountryId, "Lisbon")).thenReturn(anyCity);

        when(anyCity.getCityCountryID()).thenReturn(anyCountryId);
        when(anyCity.getCityName()).thenReturn("Lisbon");

        repo.save(anyCity);

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.save(factory.create(anyCountryId, "Lisbon"))
        );
    }
}