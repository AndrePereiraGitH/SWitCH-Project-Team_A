package TOPSECRET.dto.Country.Response;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CountryResponseDTO}.
 *
 * <p>This test class verifies the behavior of the response DTO used to expose
 * country information through the API layer.</p>
 *
 * <p>The tests validate:</p>
 * <ul>
 *     <li>Correct storage and retrieval of DTO field values</li>
 *     <li>Equality and hash code consistency</li>
 *     <li>Comparison behavior against null and different object types</li>
 *     <li>Impact of HATEOAS links on equality through {@link org.springframework.hateoas.RepresentationModel}</li>
 * </ul>
 *
 * <p>Since {@link CountryResponseDTO} extends {@link org.springframework.hateoas.RepresentationModel},
 * link collections are also considered during equality checks.</p>
 */
class CountryResponseDTOTest {

    @Test
    void shouldCreateDTOWithValidData() {

        // Arrange
        CountryResponseDTO dto = new CountryResponseDTO("PORTUGAL", "Portugal");

        // Act
        String countryId = dto.getCountryId();
        String countryName = dto.getCountryName();

        // Assert
        assertEquals("PORTUGAL", countryId);
        assertEquals("Portugal", countryName);
    }

    @Test
    void shouldSupportEqualityWithSameData() {

        // Arrange
        CountryResponseDTO dto1 = new CountryResponseDTO("PORTUGAL", "Portugal");
        CountryResponseDTO dto2 = new CountryResponseDTO("PORTUGAL", "Portugal");

        // Act
        boolean result = dto1.equals(dto2);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldSupportHashCodeWithSameData() {

        // Arrange
        CountryResponseDTO dto1 = new CountryResponseDTO("PORTUGAL", "Portugal");
        CountryResponseDTO dto2 = new CountryResponseDTO("PORTUGAL", "Portugal");

        // Act
        int hash1 = dto1.hashCode();
        int hash2 = dto2.hashCode();

        // Assert
        assertEquals(hash1, hash2);
    }

    @Test
    void shouldBeEqualToItself() {

        // Arrange
        CountryResponseDTO dto = new CountryResponseDTO("PORTUGAL", "Portugal");

        // Act
        boolean result = dto.equals(dto);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldNotBeEqualToNull() {

        // Arrange
        CountryResponseDTO dto = new CountryResponseDTO("PORTUGAL", "Portugal");

        // Act
        boolean result = dto.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {

        // Arrange
        CountryResponseDTO dto = new CountryResponseDTO("PORTUGAL", "Portugal");

        // Act
        boolean result = dto.equals("PORTUGAL");

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldNotBeEqualToDTOWithDifferentData() {

        // Arrange
        CountryResponseDTO dto1 = new CountryResponseDTO("PORTUGAL", "Portugal");
        CountryResponseDTO dto2 = new CountryResponseDTO("SPAIN", "Spain");

        // Act
        boolean result = dto1.equals(dto2);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldNotBeEqualToDTOWithSameDataButDifferentLinks() {

        // Arrange
        CountryResponseDTO dto1 = new CountryResponseDTO("PORTUGAL", "Portugal");
        CountryResponseDTO dto2 = new CountryResponseDTO("PORTUGAL", "Portugal");

        dto2.add(Link.of("http://localhost/countries/PORTUGAL", "self"));

        // Act
        boolean result = dto1.equals(dto2);

        // Assert
        assertFalse(result);
    }
}