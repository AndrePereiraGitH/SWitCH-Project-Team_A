package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.persistence.repositoryjpa.datamodel.CityDM;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CityAssembler}.
 *
 * <p>This test suite verifies the correct conversion between the {@link City}
 * domain object and the {@link CityDM} data model, in both directions.
 * The {@link CityFactory} is mocked to isolate the assembler logic.</p>
 */
class CityAssemblerTest {

    @Test
    void shouldConvertDomainToDM() {

        // Arrange
        CityFactory doubleCityFactory = mock(CityFactory.class);
        CityAssembler assembler = new CityAssembler(doubleCityFactory);

        CityId cityId = mock(CityId.class);
        CountryId countryId = mock(CountryId.class);

        City doubleCity = mock(City.class);
        when(doubleCity.id()).thenReturn(cityId);
        when(doubleCity.getCityCountryID()).thenReturn(countryId);
        when(doubleCity.getCityName()).thenReturn("Porto");

        // Act
        CityDM result = assembler.toDM(doubleCity);

        // Assert
        assertNotNull(result);
        assertEquals(cityId, result.getCityID());
        assertEquals(countryId, result.getCountryID());
        assertEquals("Porto", result.getCityName());
    }

    @Test
    void shouldConvertDMToDomain() {

        // Arrange
        CityFactory doubleCityFactory = mock(CityFactory.class);
        CityAssembler assembler = new CityAssembler(doubleCityFactory);

        CityId cityID = mock(CityId.class);
        CountryId countryID = mock(CountryId.class);

        CityDM cityDM = new CityDM(cityID, countryID, "Porto");

        City doubleCity = mock(City.class);
        when(doubleCityFactory.create(cityID, countryID, "Porto")).thenReturn(doubleCity);

        // Act
        City result = assembler.toDomain(cityDM);

        // Assert
        assertEquals(doubleCity, result);
    }
}