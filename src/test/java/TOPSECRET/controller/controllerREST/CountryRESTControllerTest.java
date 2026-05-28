package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.CountryLinkProvider;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.country.CountryMapper;
import TOPSECRET.dto.country.request.RegisterCountryDTO;
import TOPSECRET.dto.country.response.CountryResponseDTO;
import TOPSECRET.services.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CountryRESTController}.
 * <p>
 * These tests validate HTTP responses, DTO mapping,
 * and interaction with service and link provider layers.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class CountryRESTControllerTest {

    @Mock
    private CountryService countryService;

    @Mock
    private CountryLinkProvider linkProvider;

    @Mock
    private CountryMapper countryMapper;

    private CountryRESTController controller;

    private Country country;
    private UserId userId;

    @BeforeEach
    void setup() {
        controller = new CountryRESTController(countryService, linkProvider);

        country = mock(Country.class);
        userId = new UserId("user-id");
    }

    // -------------------------------------------------------------------------
    // register()
    // -------------------------------------------------------------------------

    @Test
    void register_validInput_returnsCreated() throws Exception {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("Portugal");

        when(country.getCountryName()).thenReturn("Portugal");
        when(country.id()).thenReturn(new CountryId("PORTUGAL"));

        when(countryService.createAndSave("Portugal", userId))
                .thenReturn(country);

        when(linkProvider.getLinks(userId))
                .thenReturn(new ArrayList<>(List.of(
                        Link.of("/countries", "register-country")
                )));

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.register(dto, userId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Portugal", response.getBody().getCountryName());
        assertFalse(response.getBody().getLinks().isEmpty());
    }

    @Test
    void register_duplicateCountry_returnsConflict() throws Exception {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("Portugal");

        when(countryService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new InstanceAlreadyExistsException());

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.register(dto, userId);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void register_accessDenied_returnsForbidden() throws Exception {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("Portugal");

        when(countryService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new AccessDeniedException("denied"));

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.register(dto, userId);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void register_invalidInput_returnsBadRequest() throws Exception {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("Mordor");

        when(countryService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new IllegalArgumentException("invalid"));

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.register(dto, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void register_unexpectedException_returnsInternalServerError() throws Exception {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("Portugal");

        when(countryService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new RuntimeException("boom"));

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.register(dto, new UserId("user"));

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // -------------------------------------------------------------------------
    // getCountries()
    // -------------------------------------------------------------------------

    @Test
    void getCountries_returnsOk() {

        // Arrange
        when(country.getCountryName()).thenReturn("Portugal");

        when(countryService.findAll()).thenReturn(List.of(country));

        when(country.id()).thenReturn(new CountryId("PORTUGAL"));

        // Act
        ResponseEntity<List<CountryResponseDTO>> response =
                controller.getCountries();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Portugal", response.getBody().get(0).getCountryName());
    }

    @Test
    void getCountries_invalidInput_returnsBadRequest() {

        //Arrange
        when(countryService.findAll())
                .thenThrow(new IllegalArgumentException("invalid"));

        //Act
        ResponseEntity<List<CountryResponseDTO>> response =
                controller.getCountries();

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getCountries_unexpectedError_returnsInternalServerError() {

        //Arrange
        when(countryService.findAll())
                .thenThrow(new RuntimeException("boom"));

        //Act
        ResponseEntity<List<CountryResponseDTO>> response =
                controller.getCountries();

        //Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}