package TOPSECRET.dto.city;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.dto.city.request.RequestCityDTO;
import TOPSECRET.dto.city.response.ResponseCityDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CityMapper}.
 *
 * <p>Verifies correct conversion between {@link City} domain objects
 * and their DTO representations, and correct wrapping of primitives
 * into domain Value Objects.</p>
 *
 * <p>As {@link CityMapper} is stateless with only static methods,
 * no instance is created — all methods are called directly on the class.</p>
 * <p>A {@link City} double is used instead of a real domain object so that
 * the mapper tests remain isolated from factory or domain logic changes.</p>
 */
class CityMapperTest {

    // -----
    // toDTO
    // -----

    @Test
    void toDTO_ShouldReturnCorrectResponseCityDTO_WhenCityIsValid() {
        // ARRANGE
        CityId cityIdDouble = mock(CityId.class);
        when(cityIdDouble.getCityID()).thenReturn("1");

        City cityDouble = mock(City.class);
        when(cityDouble.id()).thenReturn(cityIdDouble);
        when(cityDouble.getCityName()).thenReturn("Porto");

        // ACT
        ResponseCityDTO result = CityMapper.toDTO(cityDouble, "Portugal");

        // ASSERT
        assertEquals("1", result.getCityId());
        assertEquals("Porto", result.getCityName());
        assertEquals("Portugal", result.getCountryName());
    }

    @Test
    void toDTO_ShouldThrowNullPointerException_WhenCountryNameIsNull() {
        // ARRANGE
        City cityDouble = mock(City.class);
        when(cityDouble.getCityName()).thenReturn("Porto");

        // ACT & ASSERT
        assertThrows(NullPointerException.class, () -> CityMapper.toDTO(cityDouble, null));
    }

    @Test
    void toDTO_ShouldThrowNullPointerException_WhenCityIsNull() {
        // ARRANGE
        City cityDouble = mock(City.class);
        when(cityDouble.getCityName()).thenReturn("Porto");

        // ACT & ASSERT
        assertThrows(NullPointerException.class, () -> CityMapper.toDTO(null, "Portugal"));
    }

    // -----------
    // toCountryId
    // -----------

    @Test
    void toCountryId_ShouldReturnCountryId_WhenDTOIsValid() {
        // ARRANGE
        RequestCityDTO dto = new RequestCityDTO("Porto", "PT","Portugal");

        // ACT
        CountryId result = CityMapper.toCountryId(dto);

        // ASSERT
        assertEquals("PT", result.getCountryID());
    }

    @Test
    void toCountryId_ShouldThrowNullPointerException_WhenDTOIsNull() {
        // ACT & ASSERT
        assertThrows(NullPointerException.class, () -> CityMapper.toCountryId(null));
    }

    // -------
    // toDTOs
    // -------

    @Test
    void toDTOs_ShouldReturnListOfResponseCityDTO_WhenCitiesAreValid() {
        // ARRANGE
        CityId cityIdDouble = mock(CityId.class);
        when(cityIdDouble.getCityID()).thenReturn("1");

        City cityDouble = mock(City.class);
        when(cityDouble.id()).thenReturn(cityIdDouble);
        when(cityDouble.getCityName()).thenReturn("Porto");

        CountryId countryIdDouble = mock(CountryId.class);
        when(countryIdDouble.toString()).thenReturn("PT-001");
        when(cityDouble.getCityCountryID()).thenReturn(countryIdDouble);

        // ACT
        List<ResponseCityDTO> result = CityMapper.toDTOs(List.of(cityDouble));

        // ASSERT
        assertEquals(1, result.size());
        assertEquals("Porto", result.get(0).getCityName());
    }

    @Test
    void toDTOs_ShouldReturnEmptyList_WhenCitiesListIsEmpty() {
        // ACT
        List<ResponseCityDTO> result = CityMapper.toDTOs(List.of());

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOs_ShouldThrowNullPointerException_WhenCitiesIsNull() {
        // ACT & ASSERT
        assertThrows(NullPointerException.class, () -> CityMapper.toDTOs(null));
    }
}