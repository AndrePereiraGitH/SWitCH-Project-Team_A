package TOPSECRET.dto.City.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link RequestCityDTO}.
 *
 * <p>Verifies correct storage, equality, hash code, and string
 * representation of the request DTO record.</p>
 */
class RequestCityDTOTest {

    @Test
    void record_ShouldStoreValuesCorrectly() {
        // ARRANGE
        RequestCityDTO dto = new RequestCityDTO("Porto", "PT");

        // ACT
        String cityName = dto.cityName();
        String countryId = dto.countryId();

        // ASSERT
        assertEquals("Porto", cityName);
        assertEquals("PT", countryId);
    }

    @Test
    void record_ShouldBeEqual_WhenValuesAreSame() {
        // ARRANGE
        RequestCityDTO dto1 = new RequestCityDTO("Porto", "PT");
        RequestCityDTO dto2 = new RequestCityDTO("Porto", "PT");

        // ASSERT
        assertEquals(dto1, dto2);
    }

    @Test
    void record_ShouldNotBeEqual_WhenValuesAreDifferent() {
        // ARRANGE
        RequestCityDTO dto1 = new RequestCityDTO("Porto", "PT");
        RequestCityDTO dto2 = new RequestCityDTO("Lisboa", "PT");

        // ASSERT
        assertNotEquals(dto1, dto2);
    }

    @Test
    void record_ShouldHaveConsistentHashCode() {
        // ARRANGE
        RequestCityDTO dto1 = new RequestCityDTO("Porto", "PT");
        RequestCityDTO dto2 = new RequestCityDTO("Porto", "PT");

        // ASSERT
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_ShouldContainValues() {
        // ACT
        RequestCityDTO dto = new RequestCityDTO("Porto", "PT");
        String result = dto.toString();

        // ASSERT
        assertTrue(result.contains("Porto"));
        assertTrue(result.contains("PT"));
    }
}