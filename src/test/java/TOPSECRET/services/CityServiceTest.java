package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CityAuthorizationPolicy;
import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CityService}.
 *
 * <p>Validates the coordination logic for city creation and retrieval,
 * including authorization, duplicate detection, and error propagation.</p>
 *
 * <p>Tests are grouped into three categories:</p>
 * <ul>
 *   <li><b>createAndSave</b> — creation and persistence of a new city, including
 *       authorization, country lookup, and duplicate scenarios.</li>
 *   <li><b>findAll</b> — retrieval of all cities, including the empty collection scenario.</li>
 *   <li><b>findById</b> — retrieval of a single city by identifier, including null and not-found scenarios.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class CityServiceTest {
    @Mock
    private CityFactory doubleCityFactory;
    @Mock
    private ICityRepository doubleCityRepository;
    @Mock
    private ICountryRepository doubleCountryRepository;
    @Mock
    private CityAuthorizationPolicy doubleCityAuthorizationPolicy;
    @Mock
    private IUserRepository doubleUserRepository;

    // ---------------
    // createAndSave
    // ---------------

    @Test
    void shouldCreateAndSaveCity() throws Exception {

        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        UserId doubleUserId = mock(UserId.class);
        User doubleUser = mock(User.class);
        CountryId countryID = mock(CountryId.class);
        City doubleCity = mock(City.class);

        when(doubleUserRepository.findByID(doubleUserId)).thenReturn(Optional.of(doubleUser));
        when(doubleCityAuthorizationPolicy.canCreateCity(doubleUser)).thenReturn(true);
        when(doubleCountryRepository.findIdByName("Portugal")).thenReturn(Optional.of(countryID));
        when(doubleCityRepository.existsByCountryIdAndCityName(countryID, "Braga")).thenReturn(false);
        when(doubleCityFactory.create(countryID, "Braga")).thenReturn(doubleCity);
        when(doubleCityRepository.save(doubleCity)).thenReturn(doubleCity);


        // Act
        City result = sut.createAndSave("Portugal", "Braga", doubleUserId);

        // Assert
        assertEquals(doubleCity, result);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        UserId doubleUserId = mock(UserId.class);

        when(doubleUserRepository.findByID(doubleUserId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> sut.createAndSave("Portugal", "Braga", doubleUserId));
    }

    @Test
    void shouldThrowWhenUserHasNoPermission() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        UserId doubleUserId = mock(UserId.class);
        User doubleUser = mock(User.class);

        when(doubleUserRepository.findByID(doubleUserId)).thenReturn(Optional.of(doubleUser));
        when(doubleCityAuthorizationPolicy.canCreateCity(doubleUser)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> sut.createAndSave("Portugal", "Braga", doubleUserId));
    }

    @Test
    void shouldThrowWhenCountryNotFound() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        UserId doubleUserId = mock(UserId.class);
        User doubleUser = mock(User.class);

        when(doubleUserRepository.findByID(doubleUserId)).thenReturn(Optional.of(doubleUser));
        when(doubleCityAuthorizationPolicy.canCreateCity(doubleUser)).thenReturn(true);
        when(doubleCountryRepository.findIdByName("Unknown")).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sut.createAndSave("Unknown", "Braga", doubleUserId));
        assertEquals("Country not found: Unknown", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCityAlreadyExists() throws Exception {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        UserId doubleUserId = mock(UserId.class);
        User doubleUser = mock(User.class);
        CountryId countryID = mock(CountryId.class);

        when(doubleUserRepository.findByID(doubleUserId)).thenReturn(Optional.of(doubleUser));
        when(doubleCityAuthorizationPolicy.canCreateCity(doubleUser)).thenReturn(true);
        when(doubleCountryRepository.findIdByName("Portugal")).thenReturn(Optional.of(countryID));
        when(doubleCityRepository.existsByCountryIdAndCityName(countryID, "Braga")).thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sut.createAndSave("Portugal", "Braga", doubleUserId));
        assertEquals("City already exists in the system.", ex.getMessage());
    }

    @Test
    void shouldPropagateExceptionWhenFactoryThrows() {

        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        UserId doubleUserId = mock(UserId.class);
        User doubleUser = mock(User.class);
        CountryId countryID = mock(CountryId.class);

        when(doubleUserRepository.findByID(doubleUserId)).thenReturn(Optional.of(doubleUser));
        when(doubleCityAuthorizationPolicy.canCreateCity(doubleUser)).thenReturn(true);
        when(doubleCountryRepository.findIdByName("Portugal")).thenReturn(Optional.of(countryID));
        when(doubleCityRepository.existsByCountryIdAndCityName(countryID, "")).thenReturn(false);
        when(doubleCityFactory.create(countryID, ""))
                .thenThrow(new IllegalArgumentException("A city name cannot be left blank"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sut.createAndSave("Portugal", "", doubleUserId));
        assertEquals("A city name cannot be left blank", ex.getMessage());
    }

    // -------
    // findAll
    // -------

    @Test
    void shouldReturnAllCities() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        City doubleCity = mock(City.class);
        when(doubleCityRepository.findAll()).thenReturn(List.of(doubleCity));

        // Act
        List<City> result = sut.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(doubleCity, result.get(0));
    }

    @Test
    void shouldReturnEmptyList_whenNoCitiesExist() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        when(doubleCityRepository.findAll()).thenReturn(List.of());

        // Act
        List<City> result = sut.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    // ----------
    // findById
    // ----------

    @Test
    void shouldReturnCity_whenCityExists() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        CityId doubleCityId = mock(CityId.class);
        City doubleCity = mock(City.class);
        when(doubleCityRepository.findById(doubleCityId)).thenReturn(Optional.of(doubleCity));

        // Act
        City result = sut.findById(doubleCityId);

        // Assert
        assertEquals(doubleCity, result);
    }

    @Test
    void shouldThrowWhenCityNotFound() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        CityId doubleCityId = mock(CityId.class);
        when(doubleCityRepository.findById(doubleCityId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> sut.findById(doubleCityId));
    }

    @Test
    void shouldThrowWhenCityIdIsNull() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> sut.findById(null));
    }

    // ---------------
    // canCreateCity
    // ---------------

    @Test
    void canCreateCity_shouldReturnTrue_whenUserIsAuthorized() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        UserId doubleUserId = mock(UserId.class);
        User doubleUser = mock(User.class);

        when(doubleUserRepository.findByID(doubleUserId)).thenReturn(Optional.of(doubleUser));
        when(doubleCityAuthorizationPolicy.canCreateCity(doubleUser)).thenReturn(true);

        // Act
        boolean result = sut.canCreateCity(doubleUserId);

        // Assert
        assertTrue(result);
    }

    @Test
    void canCreateCity_shouldReturnFalse_whenUserIsNotAuthorized() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        UserId doubleUserId = mock(UserId.class);
        User doubleUser = mock(User.class);

        when(doubleUserRepository.findByID(doubleUserId)).thenReturn(Optional.of(doubleUser));
        when(doubleCityAuthorizationPolicy.canCreateCity(doubleUser)).thenReturn(false);

        // Act
        boolean result = sut.canCreateCity(doubleUserId);

        // Assert
        assertFalse(result);
    }

    @Test
    void canCreateCity_shouldThrow_whenUserNotFound() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        UserId doubleUserId = mock(UserId.class);

        when(doubleUserRepository.findByID(doubleUserId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> sut.canCreateCity(doubleUserId));
    }

    @Test
    void createAndSave_shouldThrow_whenRepositoryFailsOnSave() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        UserId doubleUserId = mock(UserId.class);
        User doubleUser = mock(User.class);
        CountryId doubleCountryId = mock(CountryId.class);
        City doubleCity = mock(City.class);

        when(doubleUserRepository.findByID(doubleUserId)).thenReturn(Optional.of(doubleUser));
        when(doubleCityAuthorizationPolicy.canCreateCity(doubleUser)).thenReturn(true);
        when(doubleCountryRepository.findIdByName("Portugal")).thenReturn(Optional.of(doubleCountryId));
        when(doubleCityRepository.existsByCountryIdAndCityName(doubleCountryId, "Braga")).thenReturn(false);
        when(doubleCityFactory.create(doubleCountryId, "Braga")).thenReturn(doubleCity);
        when(doubleCityRepository.save(doubleCity)).thenThrow(new RuntimeException("Database error"));

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> sut.createAndSave("Portugal", "Braga", doubleUserId));
    }

    @Test
    void createAndSave_shouldThrow_whenRepositoryFailsOnExistsCheck() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        UserId doubleUserId = mock(UserId.class);
        User doubleUser = mock(User.class);
        CountryId doubleCountryId = mock(CountryId.class);

        when(doubleUserRepository.findByID(doubleUserId)).thenReturn(Optional.of(doubleUser));
        when(doubleCityAuthorizationPolicy.canCreateCity(doubleUser)).thenReturn(true);
        when(doubleCountryRepository.findIdByName("Portugal")).thenReturn(Optional.of(doubleCountryId));
        when(doubleCityRepository.existsByCountryIdAndCityName(doubleCountryId, "Braga"))
                .thenThrow(new RuntimeException("Database error"));

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> sut.createAndSave("Portugal", "Braga", doubleUserId));
    }

    @Test
    void findById_shouldThrow_whenRepositoryThrowsUnexpectedException() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        CityId doubleCityId = mock(CityId.class);
        when(doubleCityRepository.findById(doubleCityId)).thenThrow(new RuntimeException("Database error"));

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> sut.findById(doubleCityId));
    }

    @Test
    void findAll_shouldThrow_whenRepositoryThrowsUnexpectedException() {
        // Arrange
        CityService sut = new CityService(doubleCityFactory, doubleCityRepository, doubleCountryRepository, doubleCityAuthorizationPolicy, doubleUserRepository);
        when(doubleCityRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> sut.findAll());
    }
}