package TOPSECRET.controller;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryID;
import TOPSECRET.repository.CityRepositoryMem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US008ToRegisterACityCtrl}.
 *
 * <p>This test suite verifies the behavior of the controller responsible for
 * registering new cities in the system. The controller depends on
 * {@link CityRepositoryMem}, which is mocked in order to isolate the controller logic.</p>
 *
 * <p>The {@link City} domain object is mocked because its constructor is protected
 * and the controller only needs to forward the object returned by the repository.</p>
 */
class US008ToRegisterACityCtrlTest {
    private ICityRepository doubleCityRepo;
    private ICountryRepository doubleCountryRepo;
    private US008ToRegisterACityCtrl controller;

    @BeforeEach
    void setUp() {
        doubleCityRepo = mock(ICityRepository.class);
        doubleCountryRepo = mock(ICountryRepository.class);
    }

    @Test
    void throwsConstructorWhenNullCityRepository() {

        //Arrange
        controller = new US008ToRegisterACityCtrl(doubleCityRepo, doubleCountryRepo);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new US008ToRegisterACityCtrl(null, doubleCountryRepo)
        );

        //Act + assert
        assertEquals("ICityRepository must not be left null", ex.getMessage());
    }

    @Test
    void throwsConstructorIfNullCountryRepository() {

        //Arrange
        controller = new US008ToRegisterACityCtrl(doubleCityRepo, doubleCountryRepo);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new US008ToRegisterACityCtrl(doubleCityRepo, null)
        );

        //Act + assert
        assertEquals("ICountryRepository must not be left null", ex.getMessage());
    }

    @Test
    void registerNewCitySuccess() {

        //Arrange
        controller = new US008ToRegisterACityCtrl(doubleCityRepo, doubleCountryRepo);
        CountryID doubleCountryId = new CountryID("PORTUGAL_ID");

        // Configure country repo to return a CountryID
        when(doubleCountryRepo.findCountryIdByCountryName("Portugal"))
                .thenReturn(Optional.of(doubleCountryId));

        // Configure city repo to return a new City when createAndAdd is called
        City doubleCity = mock(City.class);
        when(doubleCityRepo.createAndAdd(doubleCountryId, "Lisbon"))
                .thenReturn(doubleCity);

        //Act
        boolean result = controller.registerNewCity("Portugal", "Lisbon");

        //Assert
        assertTrue(result);
    }

    @Test
    void registerNewCitySuccess2() {

        //Arrange
        controller = new US008ToRegisterACityCtrl(doubleCityRepo, doubleCountryRepo);
        CountryID doubleCountryId = new CountryID("CZECH_REPUBLIC_ID");

        when(doubleCountryRepo.findCountryIdByCountryName("Czech Republic"))
                .thenReturn(Optional.of(doubleCountryId));

        City doubleCity = mock(City.class);
        when(doubleCityRepo.createAndAdd(doubleCountryId, "Prague"))
                .thenReturn(doubleCity);

        //Act
        boolean result = controller.registerNewCity("Czech Republic", "Prague");

        //Assert
        assertTrue(result);
    }

    @Test
    void registerNewCityWhenCountryNotFound() {

        //Arrange
        controller = new US008ToRegisterACityCtrl(doubleCityRepo, doubleCountryRepo);
        // Country repo returns empty optional
        when(doubleCountryRepo.findCountryIdByCountryName("Gondor"))
                .thenReturn(Optional.empty());

        //Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                controller.registerNewCity("Gondor", "anyCity")
        );

        //Assert
        assertEquals("Country with name Gondor not found", ex.getMessage());
    }
}
