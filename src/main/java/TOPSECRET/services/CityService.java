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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Application service responsible for coordinating the creation and
 * persistence of {@link City} domain objects.
 *
 * <p>Delegates city creation to {@link CityFactory} and persistence
 * to {@link ICityRepository}, keeping domain logic out of this layer.</p>
 */
@Service
public class CityService {

    private final CityFactory cityFactory;
    private final ICityRepository cityRepository;
    private final ICountryRepository countryRepository;
    private final CityAuthorizationPolicy cityAuthorizationPolicy;
    private final IUserRepository userRepository;

    public CityService(CityFactory cityFactory,
                       ICityRepository cityRepository,
                       ICountryRepository countryRepository,
                       CityAuthorizationPolicy cityAuthorizationPolicy,
                       IUserRepository userRepository) {
        this.cityFactory = Objects.requireNonNull(cityFactory, "City Factory must not be null");
        this.cityRepository = Objects.requireNonNull(cityRepository, "City Repository must not be null");
        this.countryRepository = Objects.requireNonNull(countryRepository, "Country Repository must not be null");
        this.cityAuthorizationPolicy = Objects.requireNonNull(cityAuthorizationPolicy, "City Authorization Policy must not be null");
        this.userRepository = Objects.requireNonNull(userRepository, "User Repository must not be null");
    }

    /**
     * Creates and persists a new City for the given country and city name.
     *
     * @param countryName the name of the country; must not be null or blank
     * @param cityName    the name of the city; must not be null or blank
     * @return the persisted {@link City}
     * @throws IllegalArgumentException if the country does not exist
     *                                  or if the city already exists
     */
    public City createAndSave(String countryName, String cityName, UserId userId) throws InstanceAlreadyExistsException, AccessDeniedException {

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new IllegalArgumentException("City not found with id: " + userId));

        if(!cityAuthorizationPolicy.canCreateCity(user)) {
            throw new AccessDeniedException("You don't have permission to create City");
        }

        CountryId countryId = countryRepository.findIdByName(countryName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Country not found: " + countryName));

        if (cityRepository.existsByCountryIdAndCityName(countryId, cityName)) {
            throw new IllegalArgumentException("City already exists in the system.");
        }

        City newCity = cityFactory.create(countryId, cityName);
        return cityRepository.save(newCity);
    }

    /**
     * Returns all cities available in the system.
     *
     * @return a list of all {@link City} domain objects; never {@code null}
     */
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    /**
     * Returns the city matching the given identifier.
     *
     * @param cityId the identifier to search for; must not be {@code null}
     * @return an {@link Optional} containing the matching {@link City},
     *         or {@link Optional#empty()} if not found
     * @throws IllegalArgumentException if {@code cityId} is {@code null}
     */
    public Optional<City> findById(CityId cityId) {
        if (cityId == null) {
            throw new IllegalArgumentException("CityId cannot be null");
        }
        return cityRepository.findById(cityId);
    }
}