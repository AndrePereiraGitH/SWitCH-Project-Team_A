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
     * Creates a new city and persists it via the repository.
     *
     * @param countryName the name of the country; must not be {@code null} or blank
     * @param cityName  the name of the city; must not be {@code null} or blank
     * @return the persisted {@link City}
     * @throws IllegalArgumentException if any domain constraint is violated
     */
    public City createAndSave(String countryName, String cityName) {
        CountryId countryId = countryRepository.findIdByName(countryName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Country not found: " + countryName));

        City newCity = cityFactory.create(countryId, cityName);
        return cityRepository.save(newCity);
    }
}