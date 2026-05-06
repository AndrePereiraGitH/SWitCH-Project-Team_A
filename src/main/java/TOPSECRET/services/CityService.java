package TOPSECRET.services;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.springframework.stereotype.Service;

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

    public CityService(CityFactory cityFactory,
                       ICityRepository cityRepository,
                       ICountryRepository countryRepository) {
        this.cityFactory = cityFactory;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    /**
     * Creates and persists a new City for the given country and city name.
     *
     * <p>This method performs the full use-case orchestration:
     * resolving the country identifier, validating business rules,
     * creating the domain object, and persisting it.</p>
     *
     * <p>Business rules enforced:</p>
     * <ul>
     *     <li>The country must exist</li>
     *     <li>The city must not already exist for the given country</li>
     * </ul>
     *
     * @param countryName the name of the country; must not be null or blank
     * @param cityName    the name of the city; must not be null or blank
     * @return the persisted {@link City}
     * @throws IllegalArgumentException if the country does not exist
     *                                  or if the city already exists
     */
    public City createAndSave(String countryName, String cityName) {
        CountryId countryId = countryRepository.findIdByName(countryName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Country not found: " + countryName));

        if (cityRepository.existsByCountryIdAndCityName(countryId, cityName)) {
            throw new IllegalArgumentException("City already exists in the system.");
        }

        City newCity = cityFactory.create(countryId, cityName);
        return cityRepository.save(newCity);
    }
}