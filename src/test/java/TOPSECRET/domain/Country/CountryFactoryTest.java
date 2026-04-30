package TOPSECRET.domain.Country;

import TOPSECRET.domain.ValueObjects.CountryId;
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

//    @Test
//    void create_caseInsensitiveInput_shouldStillWork_ifEnumAllows() {
//        Country country = factory.create("portugal");
//
//        assertEquals("Portugal", country.getCountryName());
//        assertEquals("PORTUGAL", country.getCountryID().getCountryID());
//    }

//    @Test
//    void create_compoundName_validEnum() {
//        // Só funciona se existir CZECH_REPUBLIC no enum
//        Country country = factory.create("Czech Republic");
//
//        assertNotNull(country);
//        assertEquals("Czech Republic", country.getCountryName());
//        assertEquals("CZECH_REPUBLIC", country.getCountryID().getCountryID());
//    }

    // -------------------------------------------------------------------------
    // create(CountryId, String)
    // -------------------------------------------------------------------------

    @Test
    void create_withCountryId_valid() {
        CountryId id = new CountryId("PORTUGAL");

        Country country = factory.create(id, "Portugal");

        assertNotNull(country);
        assertEquals(id, country.getCountryID());
        assertEquals("Portugal", country.getCountryName());
    }

    @Test
    void create_withCountryId_nullName() {
        CountryId id = new CountryId("PORTUGAL");

        assertThrows(IllegalArgumentException.class,
                () -> factory.create(id, null));
    }

    @Test
    void create_withCountryId_blankName() {
        CountryId id = new CountryId("PORTUGAL");

        assertThrows(IllegalArgumentException.class,
                () -> factory.create(id, "   "));
    }

    @Test
    void create_withCountryId_nullId() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, "Portugal"));
    }

    @Test
    void create_withCountryId_preservesProvidedId() {
        CountryId id = new CountryId("FRANCE");

        Country country = factory.create(id, "France");

        assertEquals(id, country.getCountryID());
    }
}
