package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AppraisalEntityLinkProvider;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.appraisalEntity.reponse.ResponseAppraisalEntityDTO;
import TOPSECRET.dto.appraisalEntity.request.RequestAppraisalEntityDTO;
import TOPSECRET.services.AppraisalEntityService;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
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
class AppraisalEntityRESTControllerTest {

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private RequestAppraisalEntityDTO validDto() {
        return new RequestAppraisalEntityDTO(
                "name",
                "999999999",
                "351",
                "test@mail.com",
                "street",
                "10",
                "1234-000",
                "cityId",
                List.of("pub1"),
                List.of("genre1")
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

    // =========================================================================
    // Success branch
    // =========================================================================

    @Test
    void shouldReturnCreatedStatusWhenEntityIsSuccessfullyCreated() throws Exception {
        // ARRANGE
        AppraisalEntityService service = mock(AppraisalEntityService.class);
        AppraisalEntityLinkProvider provider = mock(AppraisalEntityLinkProvider.class);
        AppraisalEntityRESTController sut = new AppraisalEntityRESTController(service, provider);

        when(service.createAndSave(any(), any(), anyList(), anyList(), any())).thenReturn(realEntity());
        when(provider.getLinks(any())).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.create(validDto(), adminUserId());

        // ASSERT
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void shouldReturnNonNullBodyWhenEntityIsSuccessfullyCreated() throws Exception {
        // ARRANGE
        AppraisalEntityService service = mock(AppraisalEntityService.class);
        AppraisalEntityLinkProvider provider = mock(AppraisalEntityLinkProvider.class);
        AppraisalEntityRESTController sut = new AppraisalEntityRESTController(service, provider);

        when(service.createAndSave(any(), any(), anyList(), anyList(), any())).thenReturn(realEntity());
        when(provider.getLinks(any())).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.create(validDto(), adminUserId());

        // ASSERT
        assertNotNull(result.getBody());
    }

    @Test
    void shouldAddLinksToBodyWhenProviderReturnsLinks() throws Exception {
        // ARRANGE
        AppraisalEntityService service = mock(AppraisalEntityService.class);
        AppraisalEntityLinkProvider provider = mock(AppraisalEntityLinkProvider.class);
        AppraisalEntityRESTController sut = new AppraisalEntityRESTController(service, provider);

        when(service.createAndSave(any(), any(), anyList(), anyList(), any())).thenReturn(realEntity());
        when(provider.getLinks(any())).thenReturn(
                new ArrayList<>(List.of(Link.of("http://example.com", "self")))
        );

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.create(validDto(), adminUserId());

        // ASSERT
        assertNotNull(result.getBody());
        assertTrue(result.getBody().hasLinks());
    }

    // =========================================================================
    // IllegalArgumentException branch
    // =========================================================================

    @Test
    void shouldReturnBadRequestWhenIllegalArgumentExceptionIsThrown() throws Exception {
        // ARRANGE
        AppraisalEntityService service = mock(AppraisalEntityService.class);
        AppraisalEntityLinkProvider provider = mock(AppraisalEntityLinkProvider.class);
        AppraisalEntityRESTController sut = new AppraisalEntityRESTController(service, provider);

        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new IllegalArgumentException("invalid"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.create(validDto(), adminUserId());

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void shouldReturnNullBodyWhenIllegalArgumentExceptionIsThrown() throws Exception {
        // ARRANGE
        AppraisalEntityService service = mock(AppraisalEntityService.class);
        AppraisalEntityLinkProvider provider = mock(AppraisalEntityLinkProvider.class);
        AppraisalEntityRESTController sut = new AppraisalEntityRESTController(service, provider);

        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new IllegalArgumentException("invalid"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.create(validDto(), adminUserId());

        // ASSERT
        assertNull(result.getBody());
    }

    // =========================================================================
    // AccessDeniedException branch
    // =========================================================================

    @Test
    void shouldReturnForbiddenWhenAccessDeniedExceptionIsThrown() throws Exception {
        // ARRANGE
        AppraisalEntityService service = mock(AppraisalEntityService.class);
        AppraisalEntityLinkProvider provider = mock(AppraisalEntityLinkProvider.class);
        AppraisalEntityRESTController sut = new AppraisalEntityRESTController(service, provider);

        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new AccessDeniedException("forbidden"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.create(validDto(), adminUserId());

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void shouldReturnNullBodyWhenAccessDeniedExceptionIsThrown() throws Exception {
        // ARRANGE
        AppraisalEntityService service = mock(AppraisalEntityService.class);
        AppraisalEntityLinkProvider provider = mock(AppraisalEntityLinkProvider.class);
        AppraisalEntityRESTController sut = new AppraisalEntityRESTController(service, provider);

        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new AccessDeniedException("forbidden"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.create(validDto(), adminUserId());

        // ASSERT
        assertNull(result.getBody());
    }

    // =========================================================================
    // Generic Exception branch
    // =========================================================================

    @Test
    void shouldReturnInternalServerErrorWhenUnexpectedExceptionIsThrown() throws Exception {
        // ARRANGE
        AppraisalEntityService service = mock(AppraisalEntityService.class);
        AppraisalEntityLinkProvider provider = mock(AppraisalEntityLinkProvider.class);
        AppraisalEntityRESTController sut = new AppraisalEntityRESTController(service, provider);

        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new RuntimeException("unexpected"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.create(validDto(), adminUserId());

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void shouldReturnNullBodyWhenUnexpectedExceptionIsThrown() throws Exception {
        // ARRANGE
        AppraisalEntityService service = mock(AppraisalEntityService.class);
        AppraisalEntityLinkProvider provider = mock(AppraisalEntityLinkProvider.class);
        AppraisalEntityRESTController sut = new AppraisalEntityRESTController(service, provider);

        when(service.createAndSave(any(), any(), anyList(), anyList(), any()))
                .thenThrow(new RuntimeException("unexpected"));

        // ACT
        ResponseEntity<ResponseAppraisalEntityDTO> result = sut.create(validDto(), adminUserId());

        // ASSERT
        assertNull(result.getBody());
    }
}