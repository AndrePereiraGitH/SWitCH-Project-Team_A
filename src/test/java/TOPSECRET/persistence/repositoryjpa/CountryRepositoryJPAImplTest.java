package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.persistence.repositoryjpa.assembler.CountryAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.CountryDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ICountryRepositoryJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CountryRepositoryJPAImpl}.
 *
 * <p>
 * This test class verifies the behavior of the JPA-based implementation
 * of {@link TOPSECRET.domain.Country.ICountryRepository}, using Mockito
 * to double dependencies such as {@link TOPSECRET.persistence.repositoryjpa.jpa.ICountryRepositoryJPA}
 * and {@link TOPSECRET.persistence.repositoryjpa.assembler.CountryAssembler}.
 * </p>
 *
 * <p>
 * The tests cover:
 * <ul>
 *     <li>Saving a country</li>
 *     <li>Finding a country by ID</li>
 *     <li>Retrieving all countries</li>
 *     <li>Finding a country ID by country name</li>
 * </ul>
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class CountryRepositoryJPAImplTest {

    @Mock
    private ICountryRepositoryJPA jpaRepository;

    @Mock
    private CountryAssembler assembler;

    private Country country;
    private CountryDM countryDM;
    private CountryId countryId;

    @BeforeEach
    void setup() {
        countryId = new CountryId("PORTUGAL");

        country = mock(Country.class);
        countryDM = new CountryDM(countryId, "Portugal");
    }

    // -------------------------------------------------------------------------
    // save()
    // -------------------------------------------------------------------------

    @Test
    void save_validCountry() {

        // Arrange
        CountryRepositoryJPAImpl repository =
                new CountryRepositoryJPAImpl(jpaRepository, assembler);

        when(assembler.ToDM(country)).thenReturn(countryDM);
        when(jpaRepository.save(countryDM)).thenReturn(countryDM);

        // Act
        Country result = repository.save(country);

        // Assert
        assertNotNull(result);
        assertEquals(country, result);
    }

    // -------------------------------------------------------------------------
    // findByID()
    // -------------------------------------------------------------------------

    @Test
    void findByID_existingCountry() {

        // Arrange
        CountryRepositoryJPAImpl repository =
                new CountryRepositoryJPAImpl(jpaRepository, assembler);

        when(jpaRepository.findById(countryId))
                .thenReturn(Optional.of(countryDM));

        when(assembler.ToDomain(countryDM))
                .thenReturn(country);

        // Act
        Optional<Country> result = repository.findByID(countryId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(country, result.get());
    }

    @Test
    void findByID_notFound() {

        // Arrange
        CountryRepositoryJPAImpl repository =
                new CountryRepositoryJPAImpl(jpaRepository, assembler);

        when(jpaRepository.findById(countryId))
                .thenReturn(Optional.empty());

        // Act
        Optional<Country> result = repository.findByID(countryId);

        // Assert
        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // getAllCountries()
    // -------------------------------------------------------------------------

    @Test
    void getAllCountries_returnsList() {

        // Arrange
        CountryRepositoryJPAImpl repository =
                new CountryRepositoryJPAImpl(jpaRepository, assembler);

        when(jpaRepository.findAll())
                .thenReturn(List.of(countryDM));

        when(assembler.ToDomain(countryDM))
                .thenReturn(country);

        // Act
        List<Country> result = repository.getAllCountries();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(country, result.get(0));
    }

    @Test
    void getAllCountries_emptyList() {

        // Arrange
        CountryRepositoryJPAImpl repository =
                new CountryRepositoryJPAImpl(jpaRepository, assembler);

        when(jpaRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<Country> result = repository.getAllCountries();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // findCountryIdByCountryName()
    // -------------------------------------------------------------------------

    @Test
    void findCountryIdByCountryName_found() {

        // Arrange
        CountryRepositoryJPAImpl repository =
                new CountryRepositoryJPAImpl(jpaRepository, assembler);

        when(jpaRepository.findAll())
                .thenReturn(List.of(countryDM));

        when(assembler.ToDomain(countryDM))
                .thenReturn(country);

        when(country.getCountryName())
                .thenReturn("Portugal");

        when(country.id())
                .thenReturn(countryId);

        // Act
        Optional<CountryId> result =
                repository.findCountryIdByCountryName("Portugal");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(countryId, result.get());
    }

    @Test
    void findCountryIdByCountryName_notFound() {

        // Arrange
        CountryRepositoryJPAImpl repository =
                new CountryRepositoryJPAImpl(jpaRepository, assembler);

        when(jpaRepository.findAll())
                .thenReturn(List.of());

        // Act
        Optional<CountryId> result =
                repository.findCountryIdByCountryName("Portugal");

        // Assert
        assertTrue(result.isEmpty());
    }
}