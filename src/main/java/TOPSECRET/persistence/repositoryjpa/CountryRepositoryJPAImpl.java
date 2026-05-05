package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.persistence.repositoryjpa.assembler.CountryAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.CountryDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ICountryRepositoryJPA;
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
     * Persists a {@link Country} in the database.
     * <p>
     * Before saving, this method verifies whether a country with the same
     * {@link CountryId} already exists in the database to prevent duplicates.
     * </p>
     * <p>
     * The given {@link Country} is first converted into its corresponding
     * {@link CountryDM} representation using the {@link CountryAssembler},
     * then persisted via the underlying JPA repository.
     * </p>
     *
     * @param country the {@link Country} to be saved; must not be {@code null}
     * @return the persisted {@link Country} instance
     * @throws IllegalArgumentException if a country with the same
     *                                  {@link CountryId} already exists
     */
    @Override
    public Country save(Country country) {

        CountryDM dm = assembler.ToDM(country);

        if (jpaRepository.existsById(dm.getCountryID())) {
            throw new IllegalArgumentException("Country is already registered");
        }

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
    public List<Country> findAll() {
        return ((List<CountryDM>) jpaRepository.findAll())
                .stream()
                .map(assembler::ToDomain)
                .collect(Collectors.toList());
    }

    /**
     * Finds CountryID by country name.
     */
    @Override
    public Optional<CountryId> findIdByName(String name) {

        return findAll().stream()
                .filter(c -> c.getCountryName().equalsIgnoreCase(name))
                .map(Country::id)
                .findFirst();
    }
}