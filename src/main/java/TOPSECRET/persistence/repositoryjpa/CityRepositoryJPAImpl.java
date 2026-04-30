package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.ICityRepository;
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
     * Converts the given {@link City} to a {@link CityDM} and persists it.
     *
     * @param newcity the city to persist; must not be {@code null}
     * @return the original {@link City} after persistence
     */
    @Override
    public City save(City newcity) {
        CityDM cityDM = assembler.DomainToDM(newcity);
        jpaRepository.save(cityDM);
        return newcity;
    }
}