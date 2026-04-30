package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.persistence.repositoryjpa.datamodel.CountryDM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryAssemblerTest {

    @Mock
    private CountryFactory countryFactory;

    @InjectMocks
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
    // Domain -> DM
    // -------------------------------------------------------------------------

    @Test
    void domainToDM_validCountry() {

        // Arrange
        when(country.getCountryID()).thenReturn(countryId);
        when(country.getCountryName()).thenReturn("Portugal");

        // Act
        CountryDM result = assembler.DomainToDM(country);

        // Assert
        assertNotNull(result);
        assertEquals(countryId, result.getCountryID());
        assertEquals("Portugal", result.getCountryName());
    }

    @Test
    void domainToDM_nullCountry_throwsException() {

        // Arrange + Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> assembler.DomainToDM(null)
        );

        assertEquals("Country cannot be null", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // DM -> Domain
    // -------------------------------------------------------------------------

    @Test
    void dmToDomain_validCountryDM() {

        // Arrange
        when(countryFactory.create(countryId, "Portugal"))
                .thenReturn(country);

        // Act
        Country result = assembler.DMToDomain(countryDM);

        // Assert
        assertNotNull(result);
        assertEquals(country, result);

        verify(countryFactory, times(1))
                .create(countryId, "Portugal");
    }

    @Test
    void dmToDomain_nullCountryDM_throwsException() {

        // Arrange + Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> assembler.DMToDomain(null)
        );

        assertEquals("CountryDM cannot be null", exception.getMessage());
    }
}