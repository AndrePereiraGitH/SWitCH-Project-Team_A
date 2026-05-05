package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.persistence.repositoryjpa.assembler.CityAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.CityDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ICityRepositoryJPA;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation of {@link ICityRepository}.
 *
 * <p>Acts as a bridge between the domain layer and the database, converting
 * {@link City} objects to {@link CityDM} via {@link CityAssembler} before
 * delegating persistence to {@link ICityRepositoryJPA}.</p>
 */
@Primary
@Repository
public class CityRepositoryJPAImpl implements ICityRepository {

    private final ICityRepositoryJPA jpaRepository;
    private final CityAssembler assembler;

    public CityRepositoryJPAImpl(ICityRepositoryJPA jpaRepository, CityAssembler assembler) {
        this.jpaRepository = jpaRepository;
        this.assembler = assembler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public City save(City newcity) {
        CityDM cityDM = assembler.toDM(newcity);
        CityDM saved = jpaRepository.save(cityDM);
        return assembler.toDomain(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByCountryIDAndCityName(CountryId countryId, String cityName) {
        return jpaRepository.existsByCountryIDAndCityName(countryId, cityName);
    }
}