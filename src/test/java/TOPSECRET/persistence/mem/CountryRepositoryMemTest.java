package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CountryRepositoryMemTest {

    private CountryRepositoryMem repository;
    private CountryFactory factory;

    @BeforeEach
    void setup() {
        repository = new CountryRepositoryMem();
        factory = new CountryFactory();
    }

    // -------------------------------------------------------------------------
    // save()
    // -------------------------------------------------------------------------

    @Test
    void save_validCountry() {
        Country country = factory.create("Portugal");

        Country result = repository.save(country);

        assertNotNull(result);
        assertEquals(1, repository.getAllCountries().size());
        assertEquals(country, result);
    }

    @Test
    void save_nullCountry() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
    }

    @Test
    void save_duplicateCountry_sameName() {
        Country c1 = factory.create("Portugal");
        Country c2 = factory.create("Portugal");

        repository.save(c1);

        assertThrows(IllegalArgumentException.class,
                () -> repository.save(c2));

        assertEquals(1, repository.getAllCountries().size());
    }

    @Test
    void save_duplicateCountry_caseInsensitive() {
        Country c1 = factory.create("Portugal");
        Country c2 = factory.create("portugal");

        repository.save(c1);

        assertThrows(IllegalArgumentException.class,
                () -> repository.save(c2));
    }

    // -------------------------------------------------------------------------
    // getAllCountries()
    // -------------------------------------------------------------------------

    @Test
    void getAllCountries_returnsUnmodifiableList() {
        Country country = factory.create("Portugal");
        repository.save(country);

        List<Country> list = repository.getAllCountries();

        assertEquals(1, list.size());

        assertThrows(UnsupportedOperationException.class,
                () -> list.add(factory.create("France")));
    }

    @Test
    void getAllCountries_empty() {
        assertTrue(repository.getAllCountries().isEmpty());
    }

    // -------------------------------------------------------------------------
    // findByID()
    // -------------------------------------------------------------------------

    @Test
    void findByID_existing() {
        Country country = factory.create("Portugal");
        repository.save(country);

        Optional<Country> result =
                repository.findByID(country.getCountryID());

        assertTrue(result.isPresent());
        assertEquals(country, result.get());
    }

    @Test
    void findByID_nonExisting() {
        CountryId id = new CountryId("FRANCE");

        Optional<Country> result = repository.findByID(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByID_null() {
        assertTrue(repository.findByID(null).isEmpty());
    }

    // -------------------------------------------------------------------------
    // findCountryIdByCountryName()
    // -------------------------------------------------------------------------

    @Test
    void findCountryIdByCountryName_valid() {
        Country country = factory.create("Portugal");
        repository.save(country);

        Optional<CountryId> result =
                repository.findCountryIdByCountryName("Portugal");

        assertTrue(result.isPresent());
        assertEquals(country.getCountryID(), result.get());
    }

//    @Test
//    void findCountryIdByCountryName_formatInsensitive() {
//        Country country = factory.create("Czech Republic");
//        repository.save(country);
//
//        Optional<CountryId> result =
//                repository.findCountryIdByCountryName("czech-republic");
//
//        assertTrue(result.isPresent());
//        assertEquals(country.getCountryID(), result.get());
//    }

    @Test
    void findCountryIdByCountryName_null() {
        assertThrows(NullPointerException.class,
                () -> repository.findCountryIdByCountryName(null));
    }

    @Test
    void findCountryIdByCountryName_blank() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.findCountryIdByCountryName(" "));
    }

    @Test
    void findCountryIdByCountryName_notFound() {
        assertTrue(
                repository.findCountryIdByCountryName("Gondor").isEmpty()
        );
    }
}