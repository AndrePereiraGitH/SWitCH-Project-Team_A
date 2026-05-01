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

/**
 * Unit tests for {@link CountryAssembler}, responsible for converting
 * between the {@link TOPSECRET.domain.Country.Country} domain object
 * and the {@link CountryDM} persistence data model.
 *
 * <p>This test suite validates:</p>
 * <ul>
 *     <li>Correct mapping from Domain to Data Model (DM)</li>
 *     <li>Correct mapping from Data Model (DM) to Domain</li>
 *     <li>Proper handling of null inputs in both directions</li>
 *     <li>Interaction with {@link CountryFactory} during reconstruction of domain objects</li>
 * </ul>
 *
 * <p>Mockito is used to mock dependencies such as {@link CountryFactory}
 * and {@link TOPSECRET.domain.Country.Country} to isolate the assembler logic.</p>
 */
@ExtendWith(MockitoExtension.class)
class CountryAssemblerTest {

    @Mock
    private CountryFactory countryFactory;

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
    void toDM_validCountry() {

        // Arrange
        CountryAssembler assembler = new CountryAssembler(countryFactory);

        when(country.getCountryID()).thenReturn(countryId);
        when(country.getCountryName()).thenReturn("Portugal");

        // Act
        CountryDM result = assembler.ToDM(country);

        // Assert
        assertNotNull(result);
        assertEquals(countryId, result.getCountryID());
        assertEquals("Portugal", result.getCountryName());
    }

    @Test
    void toDM_nullCountry_throwsException() {

        // Arrange
        CountryAssembler assembler = new CountryAssembler(countryFactory);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> assembler.ToDM(null)
        );

        assertEquals("Country cannot be null", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // DM -> Domain
    // -------------------------------------------------------------------------

    @Test
    void dmToDomain_validCountry() {

        // Arrange
        CountryAssembler assembler = new CountryAssembler(countryFactory);

        when(countryFactory.create(countryId, "Portugal"))
                .thenReturn(country);

        // Act
        Country result = assembler.ToDomain(countryDM);

        // Assert
        assertNotNull(result);
        assertEquals(country, result);
    }

    @Test
    void dmToDomain_nullCountry_throwsException() {

        // Arrange
        CountryAssembler assembler = new CountryAssembler(countryFactory);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> assembler.ToDomain(null)
        );

        assertEquals("CountryDM cannot be null", exception.getMessage());
    }
}