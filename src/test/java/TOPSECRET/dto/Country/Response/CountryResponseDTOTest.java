package TOPSECRET.dto.Country.Response;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CountryResponseDTO}.
 *
 * <p>This test class validates the behavior of the response DTO used to
 * expose country data through the API layer.</p>
 *
 * <p>Because {@link CountryResponseDTO} extends
 * {@link org.springframework.hateoas.RepresentationModel}, equality is not
 * based only on field values, but also on the presence of HATEOAS links.</p>
 *
 * <p>This test specifically verifies that two DTO instances with identical
 * business data are considered different when their HATEOAS links differ.</p>
 */
class CountryResponseDTOTest {

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