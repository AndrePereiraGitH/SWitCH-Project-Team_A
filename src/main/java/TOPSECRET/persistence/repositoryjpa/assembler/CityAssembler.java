package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.CityDM;

/**
 * Assembler responsible for converting between the {@link City} domain object
 * and the {@link CityDM} data model.
 *
 * <p>Uses {@link CityFactory} to reconstitute domain objects from persistence data.</p>
 */
public class CityAssembler {

    private final CityFactory cityFactory;

    public CityAssembler(CityFactory cityFactory) {
        this.cityFactory = cityFactory;
    }

    /**
     * Converts a {@link City} domain object to a {@link CityDM} for persistence.
     *
     * @param city the domain object to convert; must not be {@code null}
     * @return a {@link CityDM} populated with the city's data
     */
    public CityDM DomainToDM(City city) {
        return new CityDM(city.id(), city.getCityCountryID(), city.getCityName());
    }

    /**
     * Converts a {@link CityDM} data model to a {@link City} domain object.
     *
     * @param cityDM the data model to convert; must not be {@code null}
     * @return a reconstituted {@link City} domain object
     */
    public City DMToDomain(CityDM cityDM) {
        return cityFactory.create(cityDM.getCityID(), cityDM.getCountryID(), cityDM.getCityName());
    }
}