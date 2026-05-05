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
 * of {@link TOPSECRET.domain.Country.ICountryRepository}, ensuring that
 * persistence operations are correctly delegated to the underlying JPA repository
 * and that proper transformations between domain objects and data models occur.
 * </p>
 *
 * <p>
 * Mockito is used to isolate the System Under Test (SUT) by using doubles for all its
 * dependencies, including:
 * <ul>
 *     <li>{@link ICountryRepositoryJPA} — the persistence layer</li>
 *     <li>{@link CountryAssembler} — responsible for converting between domain and data model</li>
 *     <li>{@link CountryDM} — the JPA data model</li>
 *     <li>{@link CountryId} — the domain identifier</li>
 * </ul>
 * </p>
 *
 * <p>
 * The {@link CountryRepositoryJPAImpl} instance is automatically created and
 * injected with mocks using {@link org.mockito.InjectMocks}, ensuring a clean
 * and consistent setup across all tests.
 * </p>
 *
 * <p>
 * The test suite covers:
 * <ul>
 *     <li>Saving a country and verifying correct delegation to the JPA repository</li>
 *     <li>Finding a country by ID and mapping it back to the domain model</li>
 *     <li>Retrieving all countries and converting them to domain objects</li>
 *     <li>Finding a {@link CountryId} by country name using domain-level filtering logic</li>
 * </ul>
 * </p>
 *
 * <p>
 * Each test focuses on a single behavior and ensures that the repository
 * correctly orchestrates interactions between layers while remaining fully isolated.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class CountryRepositoryJPAImplTest {

    @Mock
    private ICountryRepositoryJPA jpaRepository;

    @Mock
    private CountryAssembler assembler;

    @Mock
    private CountryId countryId;

    @Mock
    private CountryDM countryDM;

    @InjectMocks
    private CountryRepositoryJPAImpl repository;

    private Country country;

    @BeforeEach
    void setup() {
        country = mock(Country.class);
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
    // findAll()
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
    // findIdByName()
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

        CountryDM portugalDM = mock(CountryDM.class);
        CountryDM franceDM = mock(CountryDM.class);

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