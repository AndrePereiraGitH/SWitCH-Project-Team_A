package TOPSECRET.services;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.ValueObjects.CountryID;

/**
 * Application service responsible for coordinating the creation and
 * persistence of {@link City} domain objects.
 *
 * <p>Delegates city creation to {@link CityFactory} and persistence
 * to {@link ICityRepository}, keeping domain logic out of this layer.</p>
 */
public class CityService {

    private final CityFactory cityFactory;
    private final ICityRepository cityRepository;

    public CityService(CityFactory cityFactory, ICityRepository cityRepository) {
        this.cityFactory = cityFactory;
        this.cityRepository = cityRepository;
    }

    /**
     * Creates a new city and persists it via the repository.
     *
     * @param countryID the country the city belongs to; must not be {@code null}
     * @param cityName  the name of the city; must not be {@code null} or blank
     * @return the persisted {@link City}
     * @throws IllegalArgumentException if any domain constraint is violated
     */
    public City createAndSave(CountryID countryID, String cityName) {
        City newCity = cityFactory.create(countryID, cityName);
        return cityRepository.save(newCity);
    }
}