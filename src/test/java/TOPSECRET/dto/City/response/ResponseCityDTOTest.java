package TOPSECRET.dto.City.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ResponseCityDTO}.
 *
 * <p>Verifies correct storage, equality, hash code, and string
 * representation of the response DTO.</p>
 */
class ResponseCityDTOTest {

    @Test
    void constructor_ShouldStoreValuesCorrectly() {
        // ARRANGE
        ResponseCityDTO dto = new ResponseCityDTO("Porto", "Portugal");

        // ACT
        String cityName = dto.getCityName();
        String countryName = dto.getCountryName();

        // ASSERT
        assertEquals("Porto", cityName);
        assertEquals("Portugal", countryName);
    }

    @Test
    void dto_ShouldBeEqual_WhenValuesAreSame() {
        // ARRANGE
        ResponseCityDTO dto1 = new ResponseCityDTO("Porto", "Portugal");
        ResponseCityDTO dto2 = new ResponseCityDTO("Porto", "Portugal");

        // ACT
        boolean result = dto1.equals(dto2);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void dto_ShouldNotBeEqual_WhenValuesAreDifferent() {
        // ARRANGE
        ResponseCityDTO dto1 = new ResponseCityDTO("Porto", "Portugal");
        ResponseCityDTO dto2 = new ResponseCityDTO("Lisboa", "Portugal");

        // ACT
        boolean result = dto1.equals(dto2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void dto_ShouldHaveConsistentHashCode() {
        // ARRANGE
        ResponseCityDTO dto1 = new ResponseCityDTO("Porto", "Portugal");
        ResponseCityDTO dto2 = new ResponseCityDTO("Porto", "Portugal");

        // ACT
        int hashCode1 = dto1.hashCode();
        int hashCode2 = dto2.hashCode();

        // ASSERT
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void toString_ShouldContainValues() {
        // ARRANGE
        ResponseCityDTO dto = new ResponseCityDTO("Porto", "Portugal");

        // ACT
        String result = dto.toString();

        // ASSERT
        assertTrue(result.contains("Porto"));
        assertTrue(result.contains("Portugal"));
    }
}