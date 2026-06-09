package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AppraisalEntityLinkProvider;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.appraisalEntity.response.ResponseAppraisalEntityDTO;
import TOPSECRET.dto.appraisalEntity.request.RequestAppraisalEntityDTO;
import TOPSECRET.services.AppraisalEntityService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppraisalEntityRESTController}.
 *
 * Each test instantiates the SUT directly to guarantee full isolation.
 * Tests follow the AAA pattern and cover all branches of registerAppraisalEntity(),
 * findAll(), and findById().
 */
@ExtendWith(MockitoExtension.class)
class AppraisalEntityRESTControllerTest {

    @Mock
    private AppraisalEntityService service;
    @Mock
    private AppraisalEntityLinkProvider provider;

    @InjectMocks
    private AppraisalEntityRESTController sut;

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static final String VALID_USER_ID = "user-123";
    private static final String VALID_APPRAISAL_ENTITY_ID = "ae-456";

    private RequestAppraisalEntityDTO validDto() {
        return new RequestAppraisalEntityDTO(
                "name", "999999999", "351", "test@mail.com",
                "street", "10", "1234-000", "cityId",
                List.of("pub1"), List.of("genre1")
        );
    }

    private AppraisalEntity realEntity() {
        Contact contact = mock(Contact.class);
        return new AppraisalEntityFactory().create(
                "name",
                contact,
                List.of(mock(PublicationTypeId.class)),
                List.of(mock(GenreId.class))
        );
    }

    // -------------------------------------------------------------------------
    // registerAppraisalEntity
    // -------------------------------------------------------------------------

    @Test
    void registerAppraisalEntity_shouldReturnCreatedStatus_whenEntityIsSuccessfullyCreated() {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any())).thenReturn(realEntity());
        when(provider.getAdminLinks(any())).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), VALID_USER_ID);

        // ASSERT
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void registerAppraisalEntity_shouldReturnNonNullBody_whenEntityIsSuccessfullyCreated() {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any())).thenReturn(realEntity());
        when(provider.getAdminLinks(any())).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), VALID_USER_ID);

        // ASSERT
        assertNotNull(result.getBody());
    }

    @Test
    void registerAppraisalEntity_shouldAddLinksToBody_whenProviderReturnsLinks() {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any())).thenReturn(realEntity());
        when(provider.getAdminLinks(any())).thenReturn(
                new ArrayList<>(List.of(Link.of("http://example.com/appraisal-entities", "register-appraisal-entity")))
        );

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), VALID_USER_ID);

        // ASSERT
        assertNotNull(result.getBody());
        assertTrue(result.getBody().hasLinks());
    }

    @Test
    void registerAppraisalEntity_shouldReturnEmptyLinks_whenProviderReturnsNoLinks() {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any())).thenReturn(realEntity());
        when(provider.getAdminLinks(any())).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), VALID_USER_ID);

        // ASSERT
        assertNotNull(result.getBody());
        assertFalse(result.getBody().hasLinks());
    }

    @Test
    void registerAppraisalEntity_shouldReturnNotFound_whenEntityNotFoundExceptionIsThrown() {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new EntityNotFoundException("not found"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), VALID_USER_ID);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void registerAppraisalEntity_shouldReturnNullBody_whenEntityNotFoundExceptionIsThrown() {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new EntityNotFoundException("not found"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), VALID_USER_ID);

        // ASSERT
        assertNull(result.getBody());
    }

    @Test
    void registerAppraisalEntity_shouldReturnForbidden_whenAccessDeniedExceptionIsThrown() {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new AccessDeniedException("forbidden"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), VALID_USER_ID);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void registerAppraisalEntity_shouldReturnNullBody_whenAccessDeniedExceptionIsThrown() {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new AccessDeniedException("forbidden"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), VALID_USER_ID);

        // ASSERT
        assertNull(result.getBody());
    }

    @Test
    void registerAppraisalEntity_shouldReturnBadRequest_whenIllegalArgumentExceptionIsThrown() {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new IllegalArgumentException("invalid"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), VALID_USER_ID);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void registerAppraisalEntity_shouldReturnNullBody_whenIllegalArgumentExceptionIsThrown() {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new IllegalArgumentException("invalid"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), VALID_USER_ID);

        // ASSERT
        assertNull(result.getBody());
    }

    @Test
    void registerAppraisalEntity_shouldReturnInternalServerError_whenUnexpectedExceptionIsThrown() {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new RuntimeException("unexpected"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), VALID_USER_ID);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void registerAppraisalEntity_shouldReturnNullBody_whenUnexpectedExceptionIsThrown() {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new RuntimeException("unexpected"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), VALID_USER_ID);

        // ASSERT
        assertNull(result.getBody());
    }

    // -------------------------------------------------------------------------
    // findAll
    // -------------------------------------------------------------------------

    @Test
    void findAll_shouldReturnOkStatus_whenEntitiesExist() {
        // ARRANGE
        when(service.findAll()).thenReturn(List.of(realEntity()));
        when(provider.getPublicLinks()).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<List<ResponseAppraisalEntityDTO>> result = sut.findAll();

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void findAll_shouldReturnNonNullBody_whenEntitiesExist() {
        // ARRANGE
        when(service.findAll()).thenReturn(List.of(realEntity()));
        when(provider.getPublicLinks()).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<List<ResponseAppraisalEntityDTO>> result = sut.findAll();

        // ASSERT
        assertNotNull(result.getBody());
    }

    @Test
    void findAll_shouldReturnOkStatus_whenNoEntitiesExist() {
        // ARRANGE
        when(service.findAll()).thenReturn(new ArrayList<>());
        when(provider.getPublicLinks()).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<List<ResponseAppraisalEntityDTO>> result = sut.findAll();

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoEntitiesExist() {
        // ARRANGE
        when(service.findAll()).thenReturn(new ArrayList<>());
        when(provider.getPublicLinks()).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<List<ResponseAppraisalEntityDTO>> result = sut.findAll();

        // ASSERT
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void findAll_shouldAddLinksToEachItem_whenProviderReturnsLinks() {
        // ARRANGE
        when(service.findAll()).thenReturn(List.of(realEntity(), realEntity()));
        when(provider.getPublicLinks()).thenReturn(
                new ArrayList<>(List.of(Link.of("http://example.com/appraisal-entities", "find-all")))
        );

        // ACT
        ResponseEntity<List<ResponseAppraisalEntityDTO>> result = sut.findAll();

        // ASSERT
        assertNotNull(result.getBody());
        result.getBody().forEach(dto -> assertTrue(dto.hasLinks()));
    }

    @Test
    void findAll_shouldReturnBadRequest_whenIllegalArgumentExceptionIsThrown() {
        // ARRANGE
        when(service.findAll()).thenThrow(new IllegalArgumentException("invalid"));

        // ACT
        ResponseEntity<List<ResponseAppraisalEntityDTO>> result = sut.findAll();

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void findAll_shouldReturnNotFound_whenEntityNotFoundExceptionIsThrown() {
        // ARRANGE
        when(service.findAll()).thenThrow(new EntityNotFoundException("not found"));

        // ACT
        ResponseEntity<List<ResponseAppraisalEntityDTO>> result = sut.findAll();

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findAll_shouldReturnInternalServerError_whenUnexpectedExceptionIsThrown() {
        // ARRANGE
        when(service.findAll()).thenThrow(new RuntimeException("unexpected"));

        // ACT
        ResponseEntity<List<ResponseAppraisalEntityDTO>> result = sut.findAll();

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------

    @Test
    void findById_shouldReturnOkStatus_whenEntityExists() {
        // ARRANGE
        when(service.findById(any())).thenReturn(realEntity());
        when(provider.getPublicLinks()).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.findById(VALID_APPRAISAL_ENTITY_ID);

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void findById_shouldReturnNonNullBody_whenEntityExists() {
        // ARRANGE
        when(service.findById(any())).thenReturn(realEntity());
        when(provider.getPublicLinks()).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.findById(VALID_APPRAISAL_ENTITY_ID);

        // ASSERT
        assertNotNull(result.getBody());
    }

    @Test
    void findById_shouldAddLinksToBody_whenProviderReturnsLinks() {
        // ARRANGE
        when(service.findById(any())).thenReturn(realEntity());
        when(provider.getPublicLinks()).thenReturn(
                new ArrayList<>(List.of(Link.of("http://example.com/appraisal-entities", "find-all")))
        );

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.findById(VALID_APPRAISAL_ENTITY_ID);

        // ASSERT
        assertNotNull(result.getBody());
        assertTrue(result.getBody().hasLinks());
    }

    @Test
    void findById_shouldReturnBadRequest_whenIllegalArgumentExceptionIsThrown() {
        // ARRANGE
        when(service.findById(any())).thenThrow(new IllegalArgumentException("invalid id"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.findById("invalid-id");

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void findById_shouldReturnNullBody_whenIllegalArgumentExceptionIsThrown() {
        // ARRANGE
        when(service.findById(any())).thenThrow(new IllegalArgumentException("invalid id"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.findById("invalid-id");

        // ASSERT
        assertNull(result.getBody());
    }

    @Test
    void findById_shouldReturnNotFound_whenEntityNotFoundExceptionIsThrown() {
        // ARRANGE
        when(service.findById(any())).thenThrow(new EntityNotFoundException("not found"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.findById(VALID_APPRAISAL_ENTITY_ID);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findById_shouldReturnNullBody_whenEntityNotFoundExceptionIsThrown() {
        // ARRANGE
        when(service.findById(any())).thenThrow(new EntityNotFoundException("not found"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.findById(VALID_APPRAISAL_ENTITY_ID);

        // ASSERT
        assertNull(result.getBody());
    }

    @Test
    void findById_shouldReturnInternalServerError_whenUnexpectedExceptionIsThrown() {
        // ARRANGE
        when(service.findById(any())).thenThrow(new RuntimeException("unexpected"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.findById(VALID_APPRAISAL_ENTITY_ID);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void findById_shouldReturnNullBody_whenUnexpectedExceptionIsThrown() {
        // ARRANGE
        when(service.findById(any())).thenThrow(new RuntimeException("unexpected"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.findById(VALID_APPRAISAL_ENTITY_ID);

        // ASSERT
        assertNull(result.getBody());
    }
}