package TOPSECRET.domain.Country;

import TOPSECRET.domain.ValueObjects.CountryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals("PORTUGAL", country.getCountryID().getCountryID());
    }

    @Test
    void create_sameNameGeneratesSameID() {

        //Act
        Country country1 = factory.create("Portugal");
        Country country2 = factory.create("Portugal");

        //Assert
        assertEquals(country1.getCountryID(), country2.getCountryID());
    }

    @Test
    void create_differentNameGeneratesDifferentID() {

        //Act
        Country country1 = factory.create("Portugal");
        Country country2 = factory.create("France");

        //Assert
        assertNotEquals(country1.getCountryID(), country2.getCountryID());
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
}