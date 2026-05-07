package TOPSECRET.services;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
@ExtendWith(MockitoExtension.class)
class CityServiceTest {
    @Mock
    private CityFactory doubleCityFactory;
    @Mock
    private ICityRepository doubleCityRepository;
    @Mock
    private ICountryRepository doubleCountryRepository;
    @InjectMocks
    private CityService cityService;

    @Test
    void shouldCreateAndSaveCity() {

        // Arrange
        CityService cityService = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository);
        CountryId countryID = mock(CountryId.class);
        City doubleCity = mock(City.class);
        when(doubleCountryRepository.findIdByName("Portugal")).thenReturn(Optional.of(countryID));
        when(doubleCityFactory.create(countryID, "Braga")).thenReturn(doubleCity);
        when(doubleCityRepository.save(doubleCity)).thenReturn(doubleCity);

        // Act
        City result = cityService.createAndSave("Portugal", "Braga");

        // Assert
        assertEquals(doubleCity, result);
    }

    @Test
    void shouldThrowWhenCountryNotFound() {
        // Arrange
        cityService = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository);
        when(doubleCountryRepository.findIdByName("Unknown")).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cityService.createAndSave("Unknown", "Braga"));
        assertEquals("Country not found: Unknown", ex.getMessage());
    }

    @Test
    void shouldPropagateExceptionWhenFactoryThrows() {

        // Arrange
        CityService cityService = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository);
        CountryId countryID = mock(CountryId.class);
        when(doubleCountryRepository.findIdByName("Portugal")).thenReturn(Optional.of(countryID));
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
        CountryId countryID = mock(CountryId.class);
        City doubleCity = mock(City.class);
        when(doubleCountryRepository.findIdByName("Portugal")).thenReturn(Optional.of(countryID));
        when(doubleCityFactory.create(countryID, "Braga")).thenReturn(doubleCity);
        when(doubleCityRepository.save(doubleCity))
                .thenThrow(new IllegalArgumentException("This city already exists in the system."));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cityService.createAndSave("Portugal", "Braga"));
        assertEquals("This city already exists in the system.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCityAlreadyExists() {
        // Arrange
        CityService cityService = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository);
        CountryId countryID = mock(CountryId.class);

        when(doubleCountryRepository.findIdByName("Portugal"))
                .thenReturn(Optional.of(countryID));

        when(doubleCityRepository.existsByCountryIdAndCityName(countryID, "Braga"))
                .thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cityService.createAndSave("Portugal", "Braga")
        );

        assertEquals("City already exists in the system.", ex.getMessage());
    }
}