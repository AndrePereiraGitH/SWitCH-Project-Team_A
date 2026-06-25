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
    void optionsRoot_shouldReturnOk_whenUserExists() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        when(service.canCreateCity(userId)).thenReturn(true);

        // ACT
        ResponseEntity<Void> result = sut.optionsRoot(userId);

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void optionsRoot_shouldAlwaysIncludeGetAndOptions() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        when(service.canCreateCity(userId)).thenReturn(false);

        // ACT
        ResponseEntity<Void> result = sut.optionsRoot(userId);

        // ASSERT
        assertTrue(result.getHeaders().getAllow().contains(HttpMethod.GET));
        assertTrue(result.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
    }

    @Test
    void optionsRoot_shouldIncludePost_whenUserCanCreateCity() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        when(service.canCreateCity(userId)).thenReturn(true);

        // ACT
        ResponseEntity<Void> result = sut.optionsRoot(userId);

        // ASSERT
        assertTrue(result.getHeaders().getAllow().contains(HttpMethod.POST));
    }

    @Test
    void optionsRoot_shouldNotIncludePost_whenUserCannotCreateCity() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        when(service.canCreateCity(userId)).thenReturn(false);

        // ACT
        ResponseEntity<Void> result = sut.optionsRoot(userId);

        // ASSERT
        assertFalse(result.getHeaders().getAllow().contains(HttpMethod.POST));
    }

    @Test
    void optionsRoot_shouldReturnBadRequest_whenIllegalArgument() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        when(service.canCreateCity(userId)).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<Void> result = sut.optionsRoot(userId);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void optionsRoot_shouldReturnInternalServerError_whenUnexpectedException() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        when(service.canCreateCity(userId)).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<Void> result = sut.optionsRoot(userId);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void getCityById_shouldReturnBadRequest_whenIdIsEmpty() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);

        // ACT
        ResponseEntity<ResponseCityDTO> result = sut.getCityById("");

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void getCityById_shouldReturnBadRequest_whenIdIsBlank() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);

        // ACT
        ResponseEntity<ResponseCityDTO> result = sut.getCityById("   ");

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void getCities_shouldReturnAllCitiesWithLinks_whenMultipleCitiesExist() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);

        City doubleCity1 = mock(City.class);
        City doubleCity2 = mock(City.class);
        CityId doubleCityId1 = mock(CityId.class);
        CityId doubleCityId2 = mock(CityId.class);

        when(doubleCityId1.getCityID()).thenReturn("1");
        when(doubleCityId2.getCityID()).thenReturn("2");
        when(doubleCity1.id()).thenReturn(doubleCityId1);
        when(doubleCity2.id()).thenReturn(doubleCityId2);
        when(doubleCity1.getCityName()).thenReturn("Porto");
        when(doubleCity2.getCityName()).thenReturn("Lisboa");
        when(service.findAll()).thenReturn(List.of(doubleCity1, doubleCity2));

        // ACT
        ResponseEntity<List<ResponseCityDTO>> result = sut.getCities();

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        assertTrue(result.getBody().get(0).hasLink("self"));
        assertTrue(result.getBody().get(0).hasLink("cities"));
        assertTrue(result.getBody().get(1).hasLink("self"));
        assertTrue(result.getBody().get(1).hasLink("cities"));
    }

    @Test
    void createCity_shouldReturnBadRequest_whenCountryNameIsNull() throws Exception {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        RequestCityDTO dto = mock(RequestCityDTO.class);
        when(dto.countryName()).thenReturn(null);
        when(dto.cityName()).thenReturn("Porto");
        when(service.createAndSave(null, "Porto", userId)).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<ResponseCityDTO> result = sut.createCity(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createCity_shouldReturnBadRequest_whenCityNameIsNull() throws Exception {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        RequestCityDTO dto = mock(RequestCityDTO.class);
        when(dto.countryName()).thenReturn("Portugal");
        when(dto.cityName()).thenReturn(null);
        when(service.createAndSave("Portugal", null, userId)).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<ResponseCityDTO> result = sut.createCity(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void optionsRoot_shouldAllowExactlyTwoMethods_whenUserCannotCreateCity() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        when(service.canCreateCity(userId)).thenReturn(false);

        // ACT
        ResponseEntity<Void> result = sut.optionsRoot(userId);

        // ASSERT
        assertEquals(2, result.getHeaders().getAllow().size());
        assertTrue(result.getHeaders().getAllow().contains(HttpMethod.GET));
        assertTrue(result.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
    }

    @Test
    void optionsRoot_shouldAllowExactlyThreeMethods_whenUserCanCreateCity() {
        // ARRANGE
        CityRESTController sut = new CityRESTController(service, provider);
        when(service.canCreateCity(userId)).thenReturn(true);

        // ACT
        ResponseEntity<Void> result = sut.optionsRoot(userId);

        // ASSERT
        assertEquals(3, result.getHeaders().getAllow().size());
        assertTrue(result.getHeaders().getAllow().contains(HttpMethod.GET));
        assertTrue(result.getHeaders().getAllow().contains(HttpMethod.POST));
        assertTrue(result.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
    }
}