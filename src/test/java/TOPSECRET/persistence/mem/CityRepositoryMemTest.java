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
    private final CityFactory factory = new CityFactory();

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
                new CountryId("PT"), "Lisbon");

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenCityExists() {
        //Arrange
        CityRepositoryMem repo = new CityRepositoryMem();

        CountryId country = new CountryId("PT");
        City city = factory.create(country, "Lisbon");

        repo.save(city);

        //Act
        boolean result = repo.existsByCountryIdAndCityName(country, "Lisbon");

        //Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenCityNameDiffers() {
        //Arrange
        CityRepositoryMem repo = new CityRepositoryMem();

        CountryId country = new CountryId("PT");
        repo.save(factory.create(country, "Porto"));

        //Act
        boolean result = repo.existsByCountryIdAndCityName(country, "Lisbon");

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenCountryDiffers() {
        //Arrange
        CityRepositoryMem repo = new CityRepositoryMem();

        repo.save(factory.create(new CountryId("PT"), "Lisbon"));

        //Act
        boolean result = repo.existsByCountryIdAndCityName(
                new CountryId("ES"), "Lisbon");

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldThrowWhenSavingDuplicateCity() {
        //Arrange
        CityRepositoryMem repo = new CityRepositoryMem();

        CountryId country = new CountryId("PT");
        repo.save(factory.create(country, "Lisbon"));

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.save(factory.create(country, "Lisbon"))
        );
    }
}