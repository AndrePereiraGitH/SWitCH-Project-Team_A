package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.ValueObjects.CityID;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.persistence.repositoryjpa.datamodel.CityDM;
import org.junit.jupiter.api.BeforeEach;
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

    private CityFactory doubleCityFactory;
    private CityAssembler assembler;

    @Test
    void shouldConvertDomainToDM() {

        // Arrange
        CityFactory doubleCityFactory = mock(CityFactory.class);
        CityAssembler assembler = new CityAssembler(doubleCityFactory);
        CityID cityID = new CityID("test-uuid");
        CountryId countryID = new CountryId("PORTUGAL_ID");
        City doubleCity = mock(City.class);
        when(doubleCity.id()).thenReturn(cityID);
        when(doubleCity.getCityCountryID()).thenReturn(countryID);
        when(doubleCity.getCityName()).thenReturn("Porto");

        // Act
        CityDM result = assembler.toDM(doubleCity);

        // Assert
        assertNotNull(result);
        assertEquals(cityID, result.getCityID());
        assertEquals(countryID, result.getCountryID());
        assertEquals("Porto", result.getCityName());
    }

    @Test
    void shouldConvertDMToDomain() {

        // Arrange
        CityFactory doubleCityFactory = mock(CityFactory.class);
        CityAssembler assembler = new CityAssembler(doubleCityFactory);
        CityID cityID = new CityID("test-uuid");
        CountryId countryID = new CountryId("PORTUGAL_ID");
        CityDM cityDM = new CityDM(cityID, countryID, "Porto");
        City doubleCity = mock(City.class);
        when(doubleCityFactory.create(cityID, countryID, "Porto")).thenReturn(doubleCity);

        // Act
        City result = assembler.toDomain(cityDM);

        // Assert
        assertEquals(doubleCity, result);
        verify(doubleCityFactory).create(cityID, countryID, "Porto");
    }
}