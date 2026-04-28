package TOPSECRET.services;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.ValueObjects.CountryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private CityService cityService;

    @BeforeEach
    void setUp() {
        doubleCityFactory = mock(CityFactory.class);
        doubleCityRepository = mock(ICityRepository.class);
        cityService = new CityService(doubleCityFactory, doubleCityRepository);
    }

    @Test
    void shouldCreateAndSaveCity() {

        // Arrange
        CountryID countryID = mock(CountryID.class);
        City doubleCity = mock(City.class);
        when(doubleCityFactory.create(countryID, "Braga")).thenReturn(doubleCity);
        when(doubleCityRepository.save(doubleCity)).thenReturn(doubleCity);

        // Act
        City result = cityService.createAndSave(countryID, "Braga");

        // Assert
        assertEquals(doubleCity, result);
        verify(doubleCityFactory).create(countryID, "Braga");
        verify(doubleCityRepository).save(doubleCity);
    }

    @Test
    void shouldPropagateExceptionWhenFactoryThrows() {

        // Arrange
        CountryID countryID = mock(CountryID.class);
        when(doubleCityFactory.create(countryID, ""))
                .thenThrow(new IllegalArgumentException("A city name cannot be left blank"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cityService.createAndSave(countryID, ""));
        assertEquals("A city name cannot be left blank", ex.getMessage());
    }

    @Test
    void shouldPropagateExceptionWhenRepositoryThrows() {

        // Arrange
        CountryID countryID = mock(CountryID.class);
        City doubleCity = mock(City.class);
        when(doubleCityFactory.create(countryID, "Braga")).thenReturn(doubleCity);
        when(doubleCityRepository.save(doubleCity))
                .thenThrow(new IllegalArgumentException("This city already exists in the system."));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cityService.createAndSave(countryID, "Braga"));
        assertEquals("This city already exists in the system.", ex.getMessage());
    }
}