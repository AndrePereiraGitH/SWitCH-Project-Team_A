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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @InjectMocks
    private CountryRESTController controller;

    private Country country;
    private UserId userId;


    @BeforeEach
    void setup() {

        country = mock(Country.class);
        userId = mock(UserId.class);
    }

    // -------------------------------------------------------------------------
    // register()
    // -------------------------------------------------------------------------

    @Test
    void register_Country_validInput_returnsCreatedAndCorrectBody() throws Exception {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("Portugal");

        when(country.getCountryName()).thenReturn("Portugal");
        when(country.id()).thenReturn(new CountryId("PORTUGAL"));

        when(countryService.createAndSave("Portugal", userId))
                .thenReturn(country);

        when(linkProvider.getLinks(userId))
                .thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.registerCountry(dto, userId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Portugal", response.getBody().getCountryName());
    }

    @Test
    void register_Country_validInput_addsHateoasLinks() throws Exception {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("Portugal");

        when(country.getCountryName()).thenReturn("Portugal");
        when(country.id()).thenReturn(new CountryId("PORTUGAL"));

        when(countryService.createAndSave("Portugal", userId))
                .thenReturn(country);

        List<Link> links = List.of(
                Link.of("/countries", "register-country")
        );

        when(linkProvider.getLinks(userId))
                .thenReturn(new ArrayList<>(links));

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.registerCountry(dto, userId);

        // Assert
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getLinks().isEmpty());
        assertTrue(
                response.getBody().getLinks().stream()
                        .anyMatch(l -> l.getRel().value().equals("register-country"))
        );
    }

    @Test
    void register_Country_duplicateCountry_returnsConflict() throws Exception {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("Portugal");

        when(countryService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new InstanceAlreadyExistsException());

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.registerCountry(dto, userId);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void register_Country_accessDenied_returnsForbidden() throws Exception {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("Portugal");

        when(countryService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new AccessDeniedException("denied"));

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.registerCountry(dto, userId);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void register_Country_invalidInput_returnsBadRequest() throws Exception {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("Mordor");

        when(countryService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new IllegalArgumentException("invalid"));

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.registerCountry(dto, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void register_Country_unexpectedException_returnsInternalServerError() throws Exception {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("Portugal");

        when(countryService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new RuntimeException("boom"));

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.registerCountry(dto, userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // -------------------------------------------------------------------------
    // getCountries()
    // -------------------------------------------------------------------------

    @Test
    void getCountries_returnsOk_andCorrectData() {

        // Arrange
        when(country.getCountryName()).thenReturn("Portugal");
        when(country.id()).thenReturn(new CountryId("PORTUGAL"));

        when(countryService.findAll()).thenReturn(List.of(country));

        // Act
        ResponseEntity<List<CountryResponseDTO>> response =
                controller.getCountries();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
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

    // -------------------------------------------------------------------------
    // getCountry
    // -------------------------------------------------------------------------

    @Test
    void getCountry_returnsOk_andBody() {

        // Arrange
        when(countryService.findById(any(CountryId.class)))
                .thenReturn(Optional.of(country));

        when(country.getCountryName()).thenReturn("Portugal");

        when(country.id()).thenReturn(new CountryId("PORTUGAL"));

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.getCountry("PORTUGAL");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getCountry_returnsCorrectCountryName() {

        // Arrange
        when(countryService.findById(any(CountryId.class)))
                .thenReturn(Optional.of(country));

        when(country.getCountryName()).thenReturn("Portugal");

        when(country.id()).thenReturn(new CountryId ("PORTUGAL"));

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.getCountry("PORTUGAL");

        // Assert
        assertEquals("Portugal", response.getBody().getCountryName());
    }

    @Test
    void getCountry_returnsNotFound() {

        // Arrange
        when(countryService.findById(any(CountryId.class)))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<CountryResponseDTO> response =
                controller.getCountry("INVALID");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}