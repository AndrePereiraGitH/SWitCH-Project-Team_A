//package TOPSECRET.domain.Country;
//
//import TOPSECRET.domain.ValueObjects.CountryID;
//import TOPSECRET.repository.CountryRepositoryMem;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Contract tests for the {@link ICountryRepository} interface.
// *
// * <p>These tests verify the expected behavior defined by the
// * {@link ICountryRepository} contract, using {@link CountryRepositoryMem}
// * as the concrete implementation under test.</p>
// *
// * <p>Any future implementation of {@link ICountryRepository} should
// * satisfy these same tests.</p>
// */
//class ICountryRepositoryTest {
//
//    /** The repository under test, referenced via the interface. */
//    private ICountryRepository repository;
//
//    /**
//     * Initializes the repository with a real {@link CountryFactory}
//     * before each test, since the goal here is to test the contract
//     * with real collaborators.
//     */
//    @BeforeEach
//    void preparation() {
//        repository = new CountryRepositoryMem(new CountryFactory());
//    }
//
//    // -------------------------------------------------------------------------
//    // createAndAddNewCountry() contract tests
//    // -------------------------------------------------------------------------
//
//    /**
//     * Tests that a valid country is created and added successfully.
//     */
//    @Test
//    void createAndAddNewCountry_validCountry() {
//
//        //Act
//        Country country = repository.createAndAddNewCountry("Portugal");
//
//        //Assert
//        assertNotNull(country);
//        assertEquals(1, repository.getAllCountries().size());
//    }
//
//    /**
//     * Tests that the returned {@link Country} has the correct name.
//     */
//    @Test
//    void createAndAddNewCountry_returnsCorrectCountryName() {
//
//        //Act
//        Country country = repository.createAndAddNewCountry("Portugal");
//
//        //Assert
//        assertEquals("Portugal", country.getCountryName());
//    }
//
//    /**
//     * Tests that the returned {@link Country} has a {@link CountryID}
//     * derived from the country name in uppercase.
//     */
//    @Test
//    void createAndAddNewCountry_returnsCountryWithDeterministicID() {
//
//        //Act
//        Country country = repository.createAndAddNewCountry("Portugal");
//
//        //Assert
//        assertEquals("PORTUGAL", country.getCountryID().getCountryID());
//    }
//
//    /**
//     * Tests that an invalid country name throws {@link IllegalArgumentException}.
//     */
//    @Test
//    void createAndAddNewCountry_invalidCountry() {
//
//        // Arrange + Act + Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> repository.createAndAddNewCountry("Mordor"));
//    }
//
//    /**
//     * Tests that a {@code null} country name throws {@link IllegalArgumentException}.
//     */
//    @Test
//    void createAndAddNewCountry_nullCountry() {
//
//        // Arrange + Act + Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> repository.createAndAddNewCountry(null));
//    }
//
//    /**
//     * Tests that a duplicate country name throws {@link IllegalArgumentException}.
//     */
//    @Test
//    void createAndAddNewCountry_duplicateCountry() {
//
//        //Arrange
//        repository.createAndAddNewCountry("Portugal");
//
//        //Act + Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> repository.createAndAddNewCountry("Portugal"));
//    }
//
//    // -------------------------------------------------------------------------
//    // getAllCountries() contract tests
//    // -------------------------------------------------------------------------
//
//    /**
//     * Tests that an empty repository returns an empty list.
//     */
//    @Test
//    void getAllCountries_emptyRepository() {
//
//        //Arrange + Act
//        List<Country> result = repository.getAllCountries();
//
//        //Assert
//        assertEquals(0, result.size());
//    }
//
//    /**
//     * Tests that the list returned is immutable.
//     */
//    @Test
//    void getAllCountries_returnsReadOnlyList() {
//
//        //Arrange
//        repository.createAndAddNewCountry("Portugal");
//
//        //Act
//        List<Country> list = repository.getAllCountries();
//
//        //Assert
//        assertThrows(UnsupportedOperationException.class,
//                () -> list.add(null));
//    }
//
//    // -------------------------------------------------------------------------
//    // findByID() contract tests
//    // -------------------------------------------------------------------------
//
//    /**
//     * Tests that a country can be found by its {@link CountryID}.
//     */
//    @Test
//    void findByID_existingID() {
//
//        //Arrange
//        Country country = repository.createAndAddNewCountry("Portugal");
//        CountryID auctionId = country.getCountryID();
//
//        //Act
//        Optional<Country> result = repository.findByID(auctionId);
//
//        //Assert
//        assertTrue(result.isPresent());
//        assertEquals(country, result.get());
//    }
//
//    /**
//     * Tests that a non-existing {@link CountryID} returns an empty {@link Optional}.
//     */
//    @Test
//    void findByID_nonExistingID() {
//
//        //Arrange
//        CountryID auctionId = new CountryID("FRANCE");
//
//        //Act
//        Optional<Country> result = repository.findByID(auctionId);
//
//        //Assert
//        assertFalse(result.isPresent());
//    }
//}