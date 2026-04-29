package TOPSECRET.services;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CityService}.
 *
 * <p>This test suite verifies the behavior of the service responsible for
 * coordinating city creation and persistence. The service depends on
 * {@link CityFactory} and {@link ICityRepository}, both of which are mocked
 * in order to isolate the service logic.</p>
 *
 * <p>The {@link City} domain object is mocked because its constructor is protected
 * and the service only needs to forward the object between the factory and the repository.</p>
 */
class CityServiceTest {

    private CityFactory doubleCityFactory;
    private ICityRepository doubleCityRepository;
    private ICountryRepository doubleCountryRepository;

    @BeforeEach
    void setUp() {
        doubleCityFactory = mock(CityFactory.class);
        doubleCityRepository = mock(ICityRepository.class);
        doubleCountryRepository = mock(ICountryRepository.class);
    }

    @Test
    void shouldCreateAndSaveCity() {

        // Arrange
        CityService cityService = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository);
        CountryID countryID = mock(CountryID.class);
        City doubleCity = mock(City.class);
        when(doubleCountryRepository.findCountryIdByCountryName("Portugal")).thenReturn(Optional.of(countryID));
        when(doubleCityFactory.create(countryID, "Braga")).thenReturn(doubleCity);
        when(doubleCityRepository.save(doubleCity)).thenReturn(doubleCity);

        // Act
        City result = cityService.createAndSave("Portugal", "Braga");

        // Assert
        assertEquals(doubleCity, result);
        verify(doubleCityFactory).create(countryID, "Braga");
        verify(doubleCityRepository).save(doubleCity);
    }

    @Test
    void shouldThrowWhenCountryNotFound() {
        // Arrange
        CityService cityService = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository);
        when(doubleCountryRepository.findCountryIdByCountryName("Unknown")).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cityService.createAndSave("Unknown", "Braga"));
        assertEquals("Country not found: Unknown", ex.getMessage());
    }

    @Test
    void shouldPropagateExceptionWhenFactoryThrows() {

        // Arrange
        CityService cityService = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository);
        CountryID countryID = mock(CountryID.class);
        when(doubleCountryRepository.findCountryIdByCountryName("Portugal")).thenReturn(Optional.of(countryID));
        when(doubleCityFactory.create(countryID, ""))
                .thenThrow(new IllegalArgumentException("A city name cannot be left blank"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cityService.createAndSave("Portugal", ""));
        assertEquals("A city name cannot be left blank", ex.getMessage());
    }

    @Test
    void shouldPropagateExceptionWhenRepositoryThrows() {

        // Arrange
        CityService cityService = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository);
        CountryID countryID = mock(CountryID.class);
        City doubleCity = mock(City.class);
        when(doubleCountryRepository.findCountryIdByCountryName("Portugal")).thenReturn(Optional.of(countryID));
        when(doubleCityFactory.create(countryID, "Braga")).thenReturn(doubleCity);
        when(doubleCityRepository.save(doubleCity))
                .thenThrow(new IllegalArgumentException("This city already exists in the system."));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cityService.createAndSave("Portugal", "Braga"));
        assertEquals("This city already exists in the system.", ex.getMessage());
    }
}