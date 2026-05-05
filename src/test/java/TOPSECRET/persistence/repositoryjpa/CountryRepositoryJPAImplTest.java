package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.persistence.repositoryjpa.assembler.CountryAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.CountryDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ICountryRepositoryJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
 * of {@link TOPSECRET.domain.Country.ICountryRepository}, ensuring correct
 * interaction between the repository, the JPA layer, and the assembler.
 * </p>
 *
 * <p>
 * The tests use Mockito to isolate the unit under test by mocking its dependencies:
 * <ul>
 *     <li>{@link TOPSECRET.persistence.repositoryjpa.jpa.ICountryRepositoryJPA} (JPA persistence layer)</li>
 *     <li>{@link TOPSECRET.persistence.repositoryjpa.assembler.CountryAssembler} (domain ↔ data model conversion)</li>
 * </ul>
 * </p>
 *
 * <p>
 * The {@code CountryRepositoryJPAImpl} is injected using {@link org.mockito.InjectMocks},
 * ensuring a clean and automatically wired System Under Test (SUT) per test execution.
 * </p>
 *
 * <p>
 * The test suite covers:
 * <ul>
 *     <li>Saving a country and delegating persistence correctly</li>
 *     <li>Finding a country by ID and mapping it back to the domain</li>
 *     <li>Retrieving all countries with proper conversion</li>
 *     <li>Finding a country ID by country name using domain-level filtering</li>
 * </ul>
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class CountryRepositoryJPAImplTest {

    @Mock
    private ICountryRepositoryJPA jpaRepository;

    @Mock
    private CountryAssembler assembler;

    @InjectMocks
    private CountryRepositoryJPAImpl repository;

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
    void findAll_returnsList() {

        // Arrange
        when(jpaRepository.findAll())
                .thenReturn(List.of(countryDM));

        when(assembler.ToDomain(countryDM))
                .thenReturn(country);

        // Act
        List<Country> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(country, result.get(0));
    }

    @Test
    void findAll_emptyList() {

        // Arrange
        when(jpaRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<Country> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // findCountryIdByCountryName()
    // -------------------------------------------------------------------------

    @Test
    void findIdByName_found() {

        // Arrange
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
                repository.findIdByName("Portugal");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(countryId, result.get());
    }

    @Test
    void findIdByName_notFound() {

        // Arrange
        when(jpaRepository.findAll())
                .thenReturn(List.of());

        // Act
        Optional<CountryId> result =
                repository.findIdByName("Portugal");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findIdByName_multipleCountries_notMatching_shouldReturnEmpty() {

        // Arrange
        Country portugal = mock(Country.class);
        Country france = mock(Country.class);

        CountryDM portugalDM = new CountryDM(countryId, "Portugal");
        CountryDM franceDM = new CountryDM(new CountryId("FRANCE"), "France");

        when(jpaRepository.findAll())
                .thenReturn(List.of(portugalDM, franceDM));

        when(assembler.ToDomain(portugalDM)).thenReturn(portugal);
        when(assembler.ToDomain(franceDM)).thenReturn(france);

        when(portugal.getCountryName()).thenReturn("Portugal");
        when(france.getCountryName()).thenReturn("France");

        // Act
        Optional<CountryId> result =
                repository.findIdByName("Germany");

        // Assert
        assertTrue(result.isEmpty());
    }
}