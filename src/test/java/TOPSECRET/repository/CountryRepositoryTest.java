package TOPSECRET.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import TOPSECRET.domain.Country;
import TOPSECRET.domain.CountryFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Unit tests for the {@link CountryRepository} class.
 *
 * <p>This test class uses Mockito to isolate the System Under Test (SUT),
 * which is the {@link CountryRepository}. Its collaborators
 * ({@link CountryFactory} and {@link Country}) are mocked in order to
 * focus exclusively on testing the repository behavior.</p>
 *
 * <p>The tests validate the following responsibilities of the repository:</p>
 * <ul>
 *     <li>Adding valid countries</li>
 *     <li>Rejecting invalid country inputs</li>
 *     <li>Rejecting null inputs</li>
 *     <li>Preventing duplicate countries</li>
 *     <li>Returning an immutable list of registered countries</li>
 * </ul>
 */
class CountryRepositoryTest {

    /** System Under Test (SUT): the repository responsible for storing countries. */
    private CountryRepository repository; //Our SUT

    /** Mocked collaborator responsible for creating Country objects. */
    private CountryFactory factoryMock;

    /** Mocked Country object used in tests. */
    private Country countryMock1;

    /** Another mocked Country object used in tests. */
    private Country countryMock2;

    /**
     * Initializes the test environment before each test execution.
     *
     * <p>Creates mocks for {@link CountryFactory} and {@link Country}
     * objects and injects the mocked factory into the repository.</p>
     */
    @BeforeEach
    void preparation(){
        factoryMock = mock(CountryFactory.class);
        countryMock1 = mock(Country.class);
        countryMock2 = mock(Country.class);
        repository = new CountryRepository(factoryMock);
    }

    /**
     * Tests the successful creation and addition of valid countries.
     *
     * <p>The {@link CountryFactory} mock is configured to return mocked
     * {@link Country} objects when valid country names are provided.
     * The test verifies that both countries are added to the repository
     * and that the factory's create method is called for each one.</p>
     */
    @Test
    void createAndAddANewCountry_validCountry(){

        when(factoryMock.create("Portugal")).thenReturn(countryMock1);
        when(factoryMock.create("France")).thenReturn(countryMock2);

        repository.createAndAddNewCountry("Portugal");
        repository.createAndAddNewCountry("France");

        //assert
        assertEquals(2, repository.getAllCountries().size ());;

        verify(factoryMock).create("Portugal");
        verify(factoryMock).create("France");
    }

    /**
     * Tests the behavior when attempting to add an invalid country.
     *
     * <p>The factory mock is configured to throw an
     * {@link IllegalArgumentException} when the invalid country
     * "Mordor" is requested. The repository should propagate the
     * exception and remain unchanged.</p>
     */
    @Test
    void createAndAddANewCountry_invalidCountry(){

        when(factoryMock.create("Mordor")).thenThrow(new IllegalArgumentException());

        //assert
        assertThrows(IllegalArgumentException.class, () -> repository.createAndAddNewCountry("Mordor"));
        assertEquals(0, repository.getAllCountries().size());

        verify(factoryMock).create("Mordor");
    }

    /**
     * Tests the repository behavior when a null country name is provided.
     *
     * <p>The factory mock is configured to throw an
     * {@link IllegalArgumentException} when null is passed. The test
     * verifies that the repository propagates the exception and that
     * no country is added to the internal list.</p>
     */
    @Test
    void createAndAddANewCountry_nullCountry(){

        when(factoryMock.create(null)).thenThrow(new IllegalArgumentException());

        //assert
        assertThrows(IllegalArgumentException.class, () -> repository.createAndAddNewCountry(null));
        assertEquals(0, repository.getAllCountries().size());

        verify(factoryMock).create(null);
    }

    /**
     * Tests that the repository prevents duplicate countries.
     *
     * <p>The factory returns the same mocked country for repeated
     * creation requests. The repository should detect the duplicate
     * using the equals comparison and throw an
     * {@link IllegalArgumentException}.</p>
     */
    @Test
    void createAndAddANewCountry_duplicateCountry(){

        when(factoryMock.create("Portugal")).thenReturn(countryMock1);

        repository.createAndAddNewCountry("Portugal");

        when(factoryMock.create("Portugal")).thenReturn(countryMock1);

        //assert
        assertThrows(IllegalArgumentException.class, () -> repository.createAndAddNewCountry("Portugal"));
        assertEquals(1, repository.getAllCountries().size());
    }

    /**
     * Tests that the list returned by {@link CountryRepository#getAllCountries()}
     * is immutable.
     *
     * <p>The repository returns a copy of the internal list using
     * {@code List.copyOf()}, which should prevent external modifications.
     * Attempting to add a new country to the returned list should
     * throw an {@link UnsupportedOperationException}.</p>
     */
    @Test
    void createAndAddANewCountry_returnsReadOnlyList(){

        when(factoryMock.create("Portugal")).thenReturn(countryMock1);

        repository.createAndAddNewCountry("Portugal");

        List<Country> countryList = repository.getAllCountries();

        //assert
        assertThrows(UnsupportedOperationException.class, () -> countryList.add(countryMock2));
    }
}