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
        when(assembler.DomainToDM(country)).thenReturn(countryDM);
        when(jpaRepository.save(countryDM)).thenReturn(countryDM);

        // Act
        Country result = repository.save(country);

        // Assert
        assertNotNull(result);
        assertEquals(country, result);

        verify(assembler).DomainToDM(country);
        verify(jpaRepository).save(countryDM);
    }

    // -------------------------------------------------------------------------
    // findByID()
    // -------------------------------------------------------------------------

    @Test
    void findByID_existingCountry() {

        // Arrange
        when(jpaRepository.findById(countryId))
                .thenReturn(Optional.of(countryDM));

        when(assembler.DMToDomain(countryDM))
                .thenReturn(country);

        // Act
        Optional<Country> result = repository.findByID(countryId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(country, result.get());

        verify(jpaRepository).findById(countryId);
        verify(assembler).DMToDomain(countryDM);
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

        verify(jpaRepository).findById(countryId);
        verify(assembler, never()).DMToDomain(any());
    }

    // -------------------------------------------------------------------------
    // getAllCountries()
    // -------------------------------------------------------------------------

    @Test
    void getAllCountries_returnsList() {

        // Arrange
        when(jpaRepository.findAll())
                .thenReturn(List.of(countryDM));

        when(assembler.DMToDomain(countryDM))
                .thenReturn(country);

        // Act
        List<Country> result = repository.getAllCountries();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(country, result.get(0));

        verify(jpaRepository).findAll();
        verify(assembler).DMToDomain(countryDM);
    }

    @Test
    void getAllCountries_emptyList() {

        // Arrange
        when(jpaRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<Country> result = repository.getAllCountries();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(jpaRepository).findAll();
        verifyNoInteractions(assembler);
    }

    // -------------------------------------------------------------------------
    // findCountryIdByCountryName()
    // -------------------------------------------------------------------------

    @Test
    void findCountryIdByCountryName_found() {

        // Arrange
        when(jpaRepository.findAll())
                .thenReturn(List.of(countryDM));

        when(assembler.DMToDomain(countryDM))
                .thenReturn(country);

        when(country.getCountryName())
                .thenReturn("Portugal");

        when(country.getCountryID())
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
        when(jpaRepository.findAll())
                .thenReturn(List.of());

        // Act
        Optional<CountryId> result =
                repository.findCountryIdByCountryName("Portugal");

        // Assert
        assertTrue(result.isEmpty());
    }
}