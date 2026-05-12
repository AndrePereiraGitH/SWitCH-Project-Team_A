package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import TOPSECRET.persistence.repositoryjpa.datamodel.CityDM;

/**
 * Spring Data JPA interface for persisting {@link CityDM} instances.
 *
 * <p>Extends {@link CrudRepository} to provide standard CRUD operations.
 * The Spring framework generates the implementation automatically at runtime.</p>
 *
 * <p>This interface operates exclusively with {@link CityDM} and is not
 * exposed to the domain layer.</p>
 */
public interface ICityRepositoryJPA extends JpaRepository<CityDM, CityId> {

    /**
     * Checks whether a city exists for the given country and city name.
     *
     * <p>This method is a Spring Data derived query. The implementation
     * is automatically generated at runtime based on the method name.</p>
     *
     * @param countryID the identifier of the country
     * @param cityName  the name of the city
     * @return {@code true} if a matching city exists, {@code false} otherwise
     */
    boolean existsByCountryIDAndCityName(CountryId countryID, String cityName);
}