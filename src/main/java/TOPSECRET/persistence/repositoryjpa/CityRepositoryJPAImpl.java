package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.ICityRepository;
import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.persistence.repositoryjpa.assembler.CityAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.CityDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ICityRepositoryJPA;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<City> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(assembler::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<City> findById(CityId cityId) {
        return jpaRepository.findById(cityId)
                .map(assembler::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByCountryIdAndCityName(CountryId countryId, String cityName) {
        return jpaRepository.existsByCountryIDAndCityName(countryId, cityName);
    }
}