package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CountryDM}, the JPA data model representing
 * a {@link TOPSECRET.domain.Country.Country} entity in the persistence layer.
 *
 * <p>This test suite verifies the correct behavior of the data model,
 * focusing on object construction and data retrieval through getters.</p>
 *
 * <p>The tests cover:</p>
 * <ul>
 *     <li>Successful creation of {@link CountryDM} instances with valid data</li>
 *     <li>Handling of {@code null} values for both {@link CountryId} and country name</li>
 *     <li>Correct exposure of internal state via getter methods</li>
 * </ul>
 *
 * <p>Mockito is used to mock {@link CountryId} in order to isolate the unit under test
 * and maintain consistency with the use of test doubles across the test suite.</p>
 *
 * <p>Note: No domain validation logic is tested here, as such rules belong to the
 * domain layer and not to the persistence data model.</p>
 */
@ExtendWith(MockitoExtension.class)
class CountryDMTest {

    @Mock
    private CountryId countryId;

    // -------------------------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------------------------

    @Test
    void constructor_validData() {

        // Arrange
        String countryName = "Portugal";

        // Act
        CountryDM countryDM = new CountryDM(countryId, countryName);

        // Assert
        assertNotNull(countryDM);
        assertEquals(countryId, countryDM.getCountryID());
        assertEquals(countryName, countryDM.getCountryName());
    }

    @Test
    void constructor_allowsNullCountryId() {

        // Arrange
        String countryName = "Portugal";

        // Act
        CountryDM countryDM = new CountryDM(null, countryName);

        // Assert
        assertNotNull(countryDM);
        assertNull(countryDM.getCountryID());
        assertEquals(countryName, countryDM.getCountryName());
    }

    @Test
    void constructor_allowsNullCountryName() {

        // Act
        CountryDM countryDM = new CountryDM(countryId, null);

        // Assert
        assertNotNull(countryDM);
        assertEquals(countryId, countryDM.getCountryID());
        assertNull(countryDM.getCountryName());
    }

    // -------------------------------------------------------------------------
    // Getter tests
    // -------------------------------------------------------------------------

    @Test
    void getCountryID_returnsCorrectValue() {

        // Arrange
        CountryDM countryDM = new CountryDM(countryId, "Portugal");

        // Act
        CountryId result = countryDM.getCountryID();

        // Assert
        assertEquals(countryId, result);
    }

    @Test
    void getCountryName_returnsCorrectValue() {

        // Arrange
        CountryDM countryDM = new CountryDM(countryId, "France");

        // Act
        String result = countryDM.getCountryName();

        // Assert
        assertEquals("France", result);
    }
}