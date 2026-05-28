package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CountryAuthorizationPolicy;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CountryService}.
 *
 * <p>Verifies creation, validation, authorization and repository interaction
 * for Country use cases.</p>
 */
class CountryServiceTest {

    private CountryFactory factory;
    private ICountryRepository repository;
    private CountryAuthorizationPolicy policy;
    private IUserRepository userRepository;

    private CountryService service;

    private Country country;
    private User adminUser;
    private User normalUser;

    private UserId adminUserId;
    private UserId normalUserId;

    @BeforeEach
    void setup() {

        // Arrange
        factory = mock(CountryFactory.class);
        repository = mock(ICountryRepository.class);
        userRepository = mock(IUserRepository.class);

        policy = new CountryAuthorizationPolicy();

        service = new CountryService(factory, repository, policy, userRepository);

        country = mock(Country.class);
        adminUser = mock(User.class);
        normalUser = mock(User.class);

        adminUserId = new UserId("admin-id");
        normalUserId = new UserId("normal-id");

        when(adminUser.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));
        when(normalUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));
    }

    // -------------------------------------------------------------------------
    // Constructor validation
    // -------------------------------------------------------------------------

    @Test
    void constructor_nullFactory_throwsException() {

        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CountryService(null, repository, policy, userRepository));
    }

    @Test
    void constructor_nullRepository_throwsException() {

        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CountryService(factory, null, policy, userRepository));
    }

    @Test
    void constructor_nullPolicy_throwsException() {

        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CountryService(factory, repository, null, userRepository));
    }

    @Test
    void constructor_nullUserRepository_throwsException() {

        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CountryService(factory, repository, policy, null));
    }

    // -------------------------------------------------------------------------
    // createAndSave — success
    // -------------------------------------------------------------------------

    @Test
    void createAndSave_adminUser_returnsCountry() throws Exception {

        // Arrange
        when(userRepository.findByID(adminUserId)).thenReturn(Optional.of(adminUser));
        when(factory.create("Portugal")).thenReturn(country);
        when(country.id()).thenReturn(new CountryId("PORTUGAL"));
        when(repository.findByID(any(CountryId.class))).thenReturn(Optional.empty());
        when(repository.save(country)).thenReturn(country);

        // Act
        Country result = service.createAndSave("Portugal", adminUserId);

        // Assert
        assertNotNull(result);
        assertEquals(country, result);
    }

    // -------------------------------------------------------------------------
    // createAndSave — user not found
    // -------------------------------------------------------------------------

    @Test
    void createAndSave_userNotFound_throwsException() {

        // Arrange
        when(userRepository.findByID(adminUserId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> service.createAndSave("Portugal", adminUserId));
    }

    // -------------------------------------------------------------------------
    // createAndSave — access denied
    // -------------------------------------------------------------------------

    @Test
    void createAndSave_normalUser_throwsAccessDenied() {

        // Arrange
        when(userRepository.findByID(normalUserId)).thenReturn(Optional.of(normalUser));

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> service.createAndSave("Portugal", normalUserId));
    }

    // -------------------------------------------------------------------------
    // createAndSave — duplicate country
    // -------------------------------------------------------------------------

    @Test
    void createAndSave_duplicateCountry_throwsException() throws Exception {

        // Arrange
        when(userRepository.findByID(adminUserId)).thenReturn(Optional.of(adminUser));
        when(factory.create("Portugal")).thenReturn(country);
        when(country.id()).thenReturn(new CountryId("PORTUGAL"));
        when(repository.findByID(any(CountryId.class))).thenReturn(Optional.of(country));

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class,
                () -> service.createAndSave("Portugal", adminUserId));
    }

    // -------------------------------------------------------------------------
    // findAll
    // -------------------------------------------------------------------------

    @Test
    void findAll_returnsCountries() {

        // Arrange
        List<Country> countries = List.of(country);
        when(repository.findAll()).thenReturn(countries);

        // Act
        List<Country> result = service.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(country, result.get(0));
    }
}