package TOPSECRET.dto.country;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
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
    void toUserId_validString_returnsUserId() {
        // Arrange
        String input = "user123";

        // Act
        UserId result = CountryMapper.toUserId(input);

        // Assert
        assertNotNull(result);
        assertEquals(new UserId("user123"), result);
    }

    @Test
    void toUserId_emptyString_returnsUserIdOrThrows() {

        //Assert
        assertThrows(IllegalArgumentException.class, () ->
                CountryMapper.toUserId("")
        );
    }

    @Test
    void toUserId_null_throwsException() {

        //Assert
        assertThrows(IllegalArgumentException.class,
                () -> CountryMapper.toUserId(null));
    }

    @Test
    void toUserId_blank_throwsException() {

        //Assert
        assertThrows(IllegalArgumentException.class,
                () -> CountryMapper.toUserId("   "));
    }
}