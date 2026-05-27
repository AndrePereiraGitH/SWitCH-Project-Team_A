package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AppraisalEntityLinkProvider;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.appraisalEntity.response.ResponseAppraisalEntityDTO;
import TOPSECRET.dto.appraisalEntity.request.RequestAppraisalEntityDTO;
import TOPSECRET.services.AppraisalEntityService;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppraisalEntityRESTController}.
 *
 * Each test instantiates the SUT directly to guarantee full isolation.
 * Tests follow the AAA pattern and cover all three branches of the create()
 * method: success, IllegalArgumentException, AccessDeniedException, and generic Exception.
 */
@ExtendWith(MockitoExtension.class)
class AppraisalEntityRESTControllerTest {

    @Mock
    private AppraisalEntityService service;
    @Mock
    private AppraisalEntityLinkProvider provider;

    @InjectMocks
    private AppraisalEntityRESTController sut;

    // Helpers
    private RequestAppraisalEntityDTO validDto() {
        return new RequestAppraisalEntityDTO(
                "name", "999999999", "351", "test@mail.com",
                "street", "10", "1234-000", "cityId",
                List.of("pub1"), List.of("genre1")
        );
    }

    private UserId adminUserId() {
        return new UserId("admin-user-id");
    }

    private AppraisalEntity realEntity() {
        Email email = new Email("test", "mail.com");
        Address address = new Address("street", "10", "1234-000", new CityId("cityId"));
        Contact contact = new Contact("999999999", "351", email, address);

        return new AppraisalEntityFactory().create(
                "name",
                contact,
                List.of(new PublicationTypeId("pub1")),
                List.of(new GenreId("genre1"))
        );
    }

    @Test
    void registerAppraisalEntity_shouldReturnCreatedStatus_whenEntityIsSuccessfullyCreated() throws Exception {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any())).thenReturn(realEntity());
        when(provider.getLinks(any())).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), adminUserId());

        // ASSERT
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void registerAppraisalEntity_shouldReturnNonNullBody_whenEntityIsSuccessfullyCreated() throws Exception {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any())).thenReturn(realEntity());
        when(provider.getLinks(any())).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), adminUserId());

        // ASSERT
        assertNotNull(result.getBody());
    }

    @Test
    void registerAppraisalEntity_shouldAddLinksToBody_whenProviderReturnsLinks() throws Exception {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any())).thenReturn(realEntity());
        when(provider.getLinks(any())).thenReturn(
                new ArrayList<>(List.of(Link.of("http://example.com", "self")))
        );

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), adminUserId());

        // ASSERT
        assertNotNull(result.getBody());
        assertTrue(result.getBody().hasLinks());
    }

    @Test
    void registerAppraisalEntity_shouldReturnBadRequest_whenIllegalArgumentExceptionIsThrown() throws Exception {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new IllegalArgumentException("invalid"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), adminUserId());

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void registerAppraisalEntity_shouldReturnNullBody_whenIllegalArgumentExceptionIsThrown() throws Exception {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new IllegalArgumentException("invalid"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), adminUserId());

        // ASSERT
        assertNull(result.getBody());
    }

    @Test
    void registerAppraisalEntity_shouldReturnForbidden_whenAccessDeniedExceptionIsThrown() throws Exception {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new AccessDeniedException("forbidden"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), adminUserId());

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void registerAppraisalEntity_shouldReturnNullBody_whenAccessDeniedExceptionIsThrown() throws Exception {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new AccessDeniedException("forbidden"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), adminUserId());

        // ASSERT
        assertNull(result.getBody());
    }

    @Test
    void registerAppraisalEntity_shouldReturnInternalServerError_whenUnexpectedExceptionIsThrown() throws Exception {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new RuntimeException("unexpected"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), adminUserId());

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void registerAppraisalEntity_shouldReturnNullBody_whenUnexpectedExceptionIsThrown() throws Exception {
        // ARRANGE
        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new RuntimeException("unexpected"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.registerAppraisalEntity(validDto(), adminUserId());

        // ASSERT
        assertNull(result.getBody());
    }
}