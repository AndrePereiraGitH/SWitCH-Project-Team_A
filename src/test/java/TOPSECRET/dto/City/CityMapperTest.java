package TOPSECRET.dto.City;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.dto.City.request.RequestCityDTO;
import TOPSECRET.dto.City.response.ResponseCityDTO;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CityMapper}.
 *
 * <p>Verifies correct conversion between {@link City} domain objects
 * and their DTO representations in both directions.</p>
 */
class CityMapperTest {

    private final CityFactory cityFactoryDouble = mock(CityFactory.class);
    private final ICountryRepository countryRepositoryDouble = mock(ICountryRepository.class);
    private final CityMapper cityMapper = new CityMapper(cityFactoryDouble,  countryRepositoryDouble);

    // -------------------------------------------------------------------------
    // toDTO
    // -------------------------------------------------------------------------

    @Test
    void toDTO_ShouldReturnCorrectResponseCityDTO_WhenCityIsValid() {
        // ARRANGE
        CityFactory realFactory = new CityFactory();
        City city = realFactory.create(new CountryId("PT"), "Porto");
        Country countryDouble = mock(Country.class);
        when(countryDouble.getCountryName()).thenReturn("Portugal");
        when(countryRepositoryDouble.findByID(new CountryId("PT"))).thenReturn(Optional.of(countryDouble));

        // ACT
        ResponseCityDTO result = cityMapper.toDTO(city);

        // ASSERT
        assertEquals("Porto", result.getCityName());
        assertEquals("Portugal", result.getCountryName());
    }

    @Test
    void toDTO_ShouldThrowIllegalArgumentException_WhenCountryNotFound() {
        // ARRANGE
        CityFactory realFactory = new CityFactory();
        City city = realFactory.create(new CountryId("PT"), "Porto");
        when(countryRepositoryDouble.findByID(new CountryId("PT"))).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> cityMapper.toDTO(city));
    }

    @Test
    void toDTO_ShouldThrowNullPointerException_WhenCityIsNull() {
        // ACT & ASSERT
        assertThrows(NullPointerException.class, () -> cityMapper.toDTO(null));
    }

    // -------------------------------------------------------------------------
    // toDomain
    // -------------------------------------------------------------------------

    @Test
    void toDomain_ShouldReturnCity_WhenDTOIsValid() {
        // ARRANGE
        CityFactory realFactory = new CityFactory();
        City expectedCity = realFactory.create(new CountryId("PT"), "Porto");
        when(cityFactoryDouble.create(new CountryId("PT"), "Porto")).thenReturn(expectedCity);
        RequestCityDTO dto = new RequestCityDTO("Porto", "PT");

        // ACT
        City result = cityMapper.toDomain(dto);

        // ASSERT
        assertNotNull(result);
        assertEquals("Porto", result.getCityName());
        assertEquals("PT", result.getCityCountryID().getCountryID());
    }

    @Test
    void toDomain_ShouldReturnCity_WhenDTOHasDifferentValidValues() {
        // ARRANGE
        CityFactory realFactory = new CityFactory();
        City expectedCity = realFactory.create(new CountryId("PT"), "Lisboa");
        when(cityFactoryDouble.create(new CountryId("PT"), "Lisboa")).thenReturn(expectedCity);
        RequestCityDTO dto = new RequestCityDTO("Lisboa", "PT");

        // ACT
        City result = cityMapper.toDomain(dto);

        // ASSERT
        assertNotNull(result);
        assertEquals("Lisboa", result.getCityName());
        assertEquals("PT", result.getCityCountryID().getCountryID());
    }

    @Test
    void toDomain_ShouldThrowNullPointerException_WhenDTOIsNull() {
        // ACT & ASSERT
        assertThrows(NullPointerException.class, () -> cityMapper.toDomain(null));
    }
}