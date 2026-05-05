package TOPSECRET.domain.Country;

import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CountryFactory}.
 * <p>
 * This test class validates the behavior of the factory responsible for
 * creating {@link Country} aggregate instances.
 * </p>
 * <p>
 * The tests ensure that:
 * <ul>
 *     <li>Countries are correctly created with valid input</li>
 *     <li>Country names are properly stored and returned</li>
 *     <li>Deterministic {@link CountryId} generation is respected</li>
 *     <li>Equal input values produce consistent identifiers</li>
 *     <li>Invalid inputs (null, blank, invalid names) are rejected</li>
 *     <li>Reconstruction using {@link CountryId} behaves correctly</li>
 * </ul>
 * </p>
 * <p>
 * This test suite covers both creation paths:
 * <ul>
 *     <li>Standard creation using only country name</li>
 *     <li>Reconstruction using {@link CountryId} and name</li>
 * </ul>
 * </p>
 */
class CountryFactoryTest {

    private CountryFactory factory;

    @BeforeEach
    void preparation() {
        factory = new CountryFactory();
    }

    // -------------------------------------------------------------------------
    // create() tests
    // -------------------------------------------------------------------------

    @Test
    void create_validCountry() {

        //Act
        Country country = factory.create("Portugal");

        //Assert
        assertNotNull(country);
    }

    @Test
    void create_returnsCorrectCountryName() {

        //Act
        Country country = factory.create("Portugal");

        //Assert
        assertEquals("Portugal", country.getCountryName());
    }

    @Test
    void create_generatesDeterministicID() {

        //Act
        Country country = factory.create("Portugal");

        //Assert
        assertEquals("PORTUGAL", country.id().getCountryID());
    }

    @Test
    void create_sameNameGeneratesSameID() {

        //Act
        Country country1 = factory.create("Portugal");
        Country country2 = factory.create("Portugal");

        //Assert
        assertEquals(country1.id(), country2.id());
    }

    @Test
    void create_differentNameGeneratesDifferentID() {

        //Act
        Country country1 = factory.create("Portugal");
        Country country2 = factory.create("France");

        //Assert
        assertNotEquals(country1.id(), country2.id());
    }

    @Test
    void create_nullCountryName() {

        //Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null));
    }

    @Test
    void create_blankCountryName() {

        //Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("   "));
    }

    @Test
    void create_invalidCountryName() {

        //Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("Mordor"));
    }

    // -------------------------------------------------------------------------
    // create(CountryId, String)
    // -------------------------------------------------------------------------


    @Test
    void create_withCountryId_valid() {

        // Arrange
        CountryId id = new CountryId("PORTUGAL");

        // Act
        Country country = factory.create(id, "Portugal");

        // Assert
        assertNotNull(country);
        assertEquals(id, country.id());
        assertEquals("Portugal", country.getCountryName());
    }


    @Test
    void create_withCountryId_nullName() {

        // Arrange
        CountryId id = new CountryId("PORTUGAL");

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(id, null));
    }


    @Test
    void create_withCountryId_blankName() {

        // Arrange
        CountryId id = new CountryId("PORTUGAL");

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(id, "   "));
    }


    @Test
    void create_withCountryId_nullId() {

        // Arrange

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, "Portugal"));
    }

    @Test
    void create_withCountryId_preservesProvidedId() {

        // Arrange
        CountryId id = new CountryId("FRANCE");

        // Act
        Country country = factory.create(id, "France");

        // Assert
        assertEquals(id, country.id());
    }
}
