package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.CityLinkProvider;
import TOPSECRET.domain.City.City;
import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.city.request.RequestCityDTO;
import TOPSECRET.dto.city.response.ResponseCityDTO;
import TOPSECRET.services.CityService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CityRESTController}.
 *
 * <p>Validates HTTP responses, exception handling and DTO mapping
 * for both the city creation and city retrieval endpoints.</p>
 *
 * <p>Tests are grouped into three categories:</p>
 * <ul>
 *   <li><b>POST</b> — creation of a new city, including authorization and error scenarios.</li>
 *   <li><b>GET (collection)</b> — retrieval of all cities, including empty and error scenarios.</li>
 *   <li><b>GET (by identity)</b> — retrieval of a single city by identifier, including error scenarios.</li>
 *   <li><b>OPTIONS</b> — verification of allowed HTTP methods in headers (API discoverability).</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class CityRESTControllerTest {

    @Mock
    private CityService service;

    @Mock
    private CityLinkProvider provider;

    @Mock
    private City city;

    @Mock
    private UserId userId;

    private RequestCityDTO dto;

    @Test
    void createCity_shouldReturnCreated_whenSuccess() throws Exception {

        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        RequestCityDTO dto = mock(RequestCityDTO.class);
        when(dto.countryName()).thenReturn("Portugal");
        when(dto.cityName()).thenReturn("Porto");

        CityId doubleCityId = mock(CityId.class);
        when(doubleCityId.getCityID()).thenReturn("1");
        when(city.id()).thenReturn(doubleCityId);
        when(city.getCityName()).thenReturn("Porto");
        when(service.createAndSave("Portugal", "Porto", userId)).thenReturn(city);
        when(provider.getLinks(userId)).thenReturn(List.of());

        // ACT
        ResponseEntity<ResponseCityDTO> result = sut.createCity(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(result.getBody().hasLink("self"));
    }

    @Test
    void createCity_shouldAddLinks_whenProviderReturnsLinks() throws Exception {

        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        RequestCityDTO dto = mock(RequestCityDTO.class);
        when(dto.countryName()).thenReturn("Portugal");
        when(dto.cityName()).thenReturn("Porto");

        CityId doubleCityId = mock(CityId.class);
        when(doubleCityId.getCityID()).thenReturn("1");
        when(city.id()).thenReturn(doubleCityId);
        when(city.getCityName()).thenReturn("Porto");
        when(service.createAndSave("Portugal", "Porto", userId)).thenReturn(city);
        when(provider.getLinks(userId)).thenReturn(List.of(Link.of("/cities")));

        // ACT
        ResponseEntity<ResponseCityDTO> result = sut.createCity(dto, userId);

        // ASSERT
        assertEquals(2, result.getBody().getLinks().toList().size());
    }

    @Test
    void createCity_shouldReturnForbidden_whenAccessDenied() throws Exception {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        RequestCityDTO dto = mock(RequestCityDTO.class);
        when(dto.countryName()).thenReturn("Portugal");
        when(dto.cityName()).thenReturn("Porto");

        when(service.createAndSave("Portugal", "Porto", userId))
                .thenThrow(new AccessDeniedException("denied"));

        // ACT
        ResponseEntity<ResponseCityDTO> result = sut.createCity(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createCity_shouldReturnBadRequest_whenIllegalArgument() throws Exception {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        RequestCityDTO dto = mock(RequestCityDTO.class);
        when(dto.countryName()).thenReturn("Portugal");
        when(dto.cityName()).thenReturn("Porto");

        when(service.createAndSave("Portugal", "Porto", userId))
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<ResponseCityDTO> result = sut.createCity(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createCity_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        RequestCityDTO dto = mock(RequestCityDTO.class);
        when(dto.countryName()).thenReturn("Portugal");
        when(dto.cityName()).thenReturn("Porto");

        when(service.createAndSave("Portugal", "Porto", userId))
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<ResponseCityDTO> result = sut.createCity(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createCity_shouldReturnInternalServerError_whenInstanceNotFound() throws Exception {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        RequestCityDTO dto = mock(RequestCityDTO.class);
        when(dto.countryName()).thenReturn("Portugal");
        when(dto.cityName()).thenReturn("Porto");

        CityId doubleCityId = mock(CityId.class);
        when(doubleCityId.getCityID()).thenReturn("1");
        when(city.id()).thenReturn(doubleCityId);
        when(city.getCityName()).thenReturn("Porto");
        when(service.createAndSave("Portugal", "Porto", userId)).thenReturn(city);
        when(provider.getLinks(userId)).thenThrow(new InstanceNotFoundException("User not found"));

        // ACT
        ResponseEntity<ResponseCityDTO> result = sut.createCity(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void getCities_shouldReturnOk_whenCitiesExist() {

        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);

        CityId doubleCityId = mock(CityId.class);
        when(city.id()).thenReturn(doubleCityId);
        when(city.getCityName()).thenReturn("Porto");
        when(service.findAll()).thenReturn(List.of(city));

        // ACT
        ResponseEntity<List<ResponseCityDTO>> result = sut.getCities();

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void getCities_shouldReturnEmptyList_whenNoCitiesExist() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        when(service.findAll()).thenReturn(List.of());

        // ACT
        ResponseEntity<List<ResponseCityDTO>> result = sut.getCities();

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void getCities_shouldReturnBadRequest_whenIllegalArgument() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        when(service.findAll()).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<List<ResponseCityDTO>> result = sut.getCities();

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void getCities_shouldReturnInternalServerError_whenUnexpectedException() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        when(service.findAll()).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<List<ResponseCityDTO>> result = sut.getCities();

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void getCityById_shouldReturnOk_whenCityExists() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);

        CityId doubleCityId = mock(CityId.class);
        when(doubleCityId.getCityID()).thenReturn("1");
        when(city.id()).thenReturn(doubleCityId);
        when(city.getCityName()).thenReturn("Porto");
        when(service.findById(any(CityId.class))).thenReturn(city);

        // ACT
        ResponseEntity<ResponseCityDTO> result = sut.getCityById("1");

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().hasLink("self"));
        assertTrue(result.getBody().hasLink("cities"));
    }

    @Test
    void getCityById_shouldReturnBadRequest_whenIllegalArgument() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        when(service.findById(any(CityId.class))).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<ResponseCityDTO> result = sut.getCityById("1");

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void getCityById_shouldReturnInternalServerError_whenUnexpectedException() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        when(service.findById(any(CityId.class))).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<ResponseCityDTO> result = sut.getCityById("1");

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    // ==========================================
    // OPTIONS /cities (Root)
    // ==========================================

    @Test
    void optionsRoot_shouldReturnOkStatus() {
        CityRESTController sut = new CityRESTController(service, provider);
        ResponseEntity<Void> response = sut.optionsRoot();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void optionsRoot_shouldAllowGet() {
        CityRESTController sut = new CityRESTController(service, provider);
        ResponseEntity<Void> response = sut.optionsRoot();
        assertNotNull(response.getHeaders().getAllow());
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.GET));
    }

    @Test
    void optionsRoot_shouldAllowPost() {
        CityRESTController sut = new CityRESTController(service, provider);
        ResponseEntity<Void> response = sut.optionsRoot();
        assertNotNull(response.getHeaders().getAllow());
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.POST));
    }

    @Test
    void optionsRoot_shouldAllowOptions() {
        CityRESTController sut = new CityRESTController(service, provider);
        ResponseEntity<Void> response = sut.optionsRoot();
        assertNotNull(response.getHeaders().getAllow());
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
    }

    // ==========================================
    // OPTIONS /cities/{id} (By ID)
    // ==========================================

    @Test
    void optionsById_shouldReturnOkStatus() {
        CityRESTController sut = new CityRESTController(service, provider);
        ResponseEntity<Void> response = sut.optionsById("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void optionsById_shouldAllowGet() {
        CityRESTController sut = new CityRESTController(service, provider);
        ResponseEntity<Void> response = sut.optionsById("1");
        assertNotNull(response.getHeaders().getAllow());
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.GET));
    }

    @Test
    void optionsById_shouldAllowOptions() {
        CityRESTController sut = new CityRESTController(service, provider);
        ResponseEntity<Void> response = sut.optionsById("1");
        assertNotNull(response.getHeaders().getAllow());
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
    }

    @Test
    void optionsById_shouldNotAllowPost() {
        CityRESTController sut = new CityRESTController(service, provider);
        ResponseEntity<Void> response = sut.optionsById("1");
        assertNotNull(response.getHeaders().getAllow());
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.POST));
    }
}