package TOPSECRET.dto.country;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.dto.country.response.CountryResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CountryMapper}.
 *
 * <p>This test class verifies the correct mapping behavior between
 * {@link Country} domain objects and {@link CountryResponseDTO} response DTOs.</p>
 *
 * <p>The tests validate:</p>
 * <ul>
 *     <li>Conversion of a single domain object into a DTO</li>
 *     <li>Conversion of collections of domain objects</li>
 *     <li>Correct transfer of identifier and name values</li>
 *     <li>Handling of empty collections</li>
 *     <li>Validation of null arguments</li>
 * </ul>
 *
 * <p>Mockito is used to isolate the mapper from domain object implementations.</p>
 */
class CountryMapperTest {

    @Test
    void toDTOShouldConvertCountryToDTO() {

        // Arrange
        Country countryDouble = mock(Country.class);
        CountryId idDouble = mock(CountryId.class);

        when(idDouble.getCountryID()).thenReturn("PORTUGAL");
        when(countryDouble.id()).thenReturn(idDouble);
        when(countryDouble.getCountryName()).thenReturn("Portugal");

        CountryMapper mapper = new CountryMapper();

        // Act
        CountryResponseDTO result = mapper.toDTO(countryDouble);

        // Assert
        assertEquals("PORTUGAL", result.getCountryId());
        assertEquals("Portugal", result.getCountryName());
    }

    @Test
    void toDTOShouldThrowExceptionWhenCountryIsNull() {

        // Arrange
        CountryMapper mapper = new CountryMapper();

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> mapper.toDTO(null));
    }

    @Test
    void toDTOsShouldConvertFirstCountryInList() {

        // Arrange
        Country countryDouble = mock(Country.class);
        CountryId idDouble = mock(CountryId.class);

        when(idDouble.getCountryID()).thenReturn("PORTUGAL");
        when(countryDouble.id()).thenReturn(idDouble);
        when(countryDouble.getCountryName()).thenReturn("Portugal");

        CountryMapper mapper = new CountryMapper();

        // Act
        List<CountryResponseDTO> result =
                mapper.toDTOs(List.of(countryDouble));

        // Assert
        assertEquals("PORTUGAL", result.get(0).getCountryId());
        assertEquals("Portugal", result.get(0).getCountryName());
    }

    @Test
    void toDTOsShouldConvertListOfCountriesToListOfDTOs() {

        // Arrange
        Country countryDouble1 = mock(Country.class);
        Country countryDouble2 = mock(Country.class);

        CountryId idDouble1 = mock(CountryId.class);
        CountryId idDouble2 = mock(CountryId.class);

        when(idDouble1.getCountryID()).thenReturn("PORTUGAL");
        when(idDouble2.getCountryID()).thenReturn("SPAIN");

        when(countryDouble1.id()).thenReturn(idDouble1);
        when(countryDouble2.id()).thenReturn(idDouble2);

        when(countryDouble1.getCountryName()).thenReturn("Portugal");
        when(countryDouble2.getCountryName()).thenReturn("Spain");

        CountryMapper mapper = new CountryMapper();

        // Act
        List<CountryResponseDTO> result =
                mapper.toDTOs(List.of(countryDouble1, countryDouble2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("PORTUGAL", result.get(0).getCountryId());
        assertEquals("PORTUGAL", result.get(0).getCountryId());
    }

    @Test
    void toDTOsShouldReturnEmptyListWhenInputIsEmpty() {

        // Arrange
        CountryMapper mapper = new CountryMapper();

        // Act
        List<CountryResponseDTO> result = mapper.toDTOs(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOsShouldThrowExceptionWhenListIsNull() {

        // Arrange
        CountryMapper mapper = new CountryMapper();

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> mapper.toDTOs(null));
    }
}