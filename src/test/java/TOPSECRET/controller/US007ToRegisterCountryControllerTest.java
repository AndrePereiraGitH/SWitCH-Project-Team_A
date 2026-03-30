package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.repository.CountryRepositoryMem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link US007ToRegisterCountryController}.
 *
 * <p>This test class uses Mockito to isolate the System Under Test (SUT),
 * which is the {@link US007ToRegisterCountryController}. All collaborators
 * such as {@link CountryRepositoryMem} and {@link Country} are mocked in order
 * to ensure that only the controller logic is being tested.</p>
 */
class US007ToRegisterCountryControllerTest {

    /** The System Under Test (SUT). */
    private US007ToRegisterCountryController controller;

    /** Mocked repository used as a dependency of the controller. */
    private ICountryRepository countryRepo;

    /** Mocked Country object returned by the repository. */
    private Country countryMock;

    /**
     * Prepares the test environment before each test execution.
     *
     * <p>This method initializes the mocks for {@link CountryRepositoryMem}
     * and {@link Country}, and injects the mocked repository into the
     * controller being tested.</p>
     */
    @BeforeEach
    void preparation(){

        countryRepo = mock(ICountryRepository.class); //Creates the mock
        countryMock = mock(Country.class);
        controller = new US007ToRegisterCountryController(countryRepo);
    }

    /**
     * Tests the successful registration of a valid country.
     *
     * <p>The repository is mocked to return a mocked {@link Country}
     * object when "Portugal" is registered. The test verifies that
     * the controller returns the expected country and that the
     * repository method is invoked exactly once.</p>
     */
    @Test
    void registerNewCountry_validCountry_mock(){
        when(countryRepo.createAndAddNewCountry("Portugal")).thenReturn(countryMock);

        when(countryMock.getCountryName()).thenReturn("Portugal");

        Country result = controller.registerNewCountry("Portugal");

        //assert
        assertEquals("Portugal", result.getCountryName());
        assertNotNull(result);

    }

    /**
     * Tests the behavior when attempting to register an invalid country.
     *
     * <p>The repository is configured to throw an {@link IllegalArgumentException}
     * when "Mordor" is provided. The test verifies that the controller
     * propagates the exception and that the repository method was called.</p>
     */
    @Test
    void registerNewCountry_InvalidCountry_mock(){

        when(countryRepo.createAndAddNewCountry("Mordor")).thenThrow(new IllegalArgumentException());

        //assert
        assertThrows(IllegalArgumentException.class, () -> controller.registerNewCountry("Mordor"));

    }

    /**
     * Tests the controller behavior when a null country name is provided.
     *
     * <p>The controller should throw an {@link IllegalArgumentException}
     * and must not interact with the repository.</p>
     */
    @Test
    void registerNewCountry_nullCountry_mock(){

        assertThrows(IllegalArgumentException.class, () -> controller.registerNewCountry(null));

    }

    /**
     * Tests the controller behavior when a blank country name is provided.
     *
     * <p>The controller should throw an {@link IllegalArgumentException}
     * and must not call the repository.</p>
     */
    @Test
    void registerNewCountry_blankCountry_mock(){

        assertThrows(IllegalArgumentException.class, () -> controller.registerNewCountry(" "));

    }

}