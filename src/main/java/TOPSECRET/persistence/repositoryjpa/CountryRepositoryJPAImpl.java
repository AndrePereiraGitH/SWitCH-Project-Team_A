package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.persistence.repositoryjpa.assembler.CountryAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.CountryDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ICountryRepositoryJPA;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JPA implementation of {@link ICountryRepository}.
 */
@Repository
public class CountryRepositoryJPAImpl implements ICountryRepository {

    private final ICountryRepositoryJPA jpaRepository;
    private final CountryAssembler assembler;

    public CountryRepositoryJPAImpl(ICountryRepositoryJPA jpaRepository,
                                    CountryAssembler assembler) {
        this.jpaRepository = jpaRepository;
        this.assembler = assembler;
    }

    /**
     * Saves a Country in the database.
     */
    @Override
    public Country save(Country country) {
        CountryDM dm = assembler.ToDM(country);
        jpaRepository.save(dm);
        return country;
    }

    /**
     * Finds a Country by ID.
     */
    @Override
    public Optional<Country> findByID(CountryId id) {
        return jpaRepository.findById(id)
                .map(assembler::ToDomain);
    }

    /**
     * Returns all Countries.
     */
    @Override
    public List<Country> getAllCountries() {
        return ((List<CountryDM>) jpaRepository.findAll())
                .stream()
                .map(assembler::ToDomain)
                .collect(Collectors.toList());
    }

    /**
     * Finds CountryID by country name.
     */
    @Override
    public Optional<CountryId> findCountryIdByCountryName(String name) {

        return getAllCountries().stream()
                .filter(c -> c.getCountryName().equalsIgnoreCase(name))
                .map(Country::id)
                .findFirst();
    }
}