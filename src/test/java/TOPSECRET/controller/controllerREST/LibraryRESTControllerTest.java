package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.LibraryLinkProvider;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.library.LibraryMapper;
import TOPSECRET.dto.library.request.RequestLibraryDTO;
import TOPSECRET.dto.library.response.ResponseLibraryDTO;
import TOPSECRET.services.LibraryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class LibraryRESTControllerTest {

    private final String USER_ID = "user-123";
    private final UserId USER_ID_DOMAIN = new UserId(USER_ID);
    private final String LIBRARY_ID = "library-123";

    @Mock
    private LibraryLinkProvider libLinkProvider;

    @Mock
    private LibraryService libService;

    @Mock
    private RequestLibraryDTO requestDTO;

    @Mock
    private Library library;

    @Mock
    private LibraryId libraryId;

    @InjectMocks
    private LibraryRESTController controller;

    // =========================================================
    // CREATE - SUCCESS
    // =========================================================

    @Test
    void createLibrary_shouldReturnCreated_whenValidRequest()
            throws InstanceAlreadyExistsException {
        // Arrange
        String expectedId = "id";
        String expectedLibraryName = "libraryName";
        Link links = Link.of("/libraries/provider", "provider-rel");
        when(library.id()).thenReturn(libraryId);
        Link selfRel = linkTo(methodOn(LibraryRESTController.class)
                .getLibrary(library.id().toString()))
                .withSelfRel();
        Links expectedLinks = Links.of(links, selfRel);
        when(requestDTO.libraryName()).thenReturn(expectedLibraryName);
        when(libService.createAndSave(expectedLibraryName, USER_ID_DOMAIN)).thenReturn(library);
        when(libLinkProvider.getLinksForLibraries(USER_ID_DOMAIN)).thenReturn(List.of(links));
        when(libraryId.getId()).thenReturn(expectedId);
        when(library.getLibraryName()).thenReturn(expectedLibraryName);

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(requestDTO, USER_ID);

        // Asserts
        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedId, response.getBody().getLibraryId());
        assertEquals(expectedLibraryName, response.getBody().getLibraryName());
        assertEquals(expectedLinks, response.getBody().getLinks());
    }


    // =========================================================
    // CREATE - BAD REQUEST
    // =========================================================

    @Test
    void createLibrary_shouldReturnBadRequest_whenIllegalArgument()
            throws InstanceAlreadyExistsException {

        when(requestDTO.libraryName()).thenReturn("Test Library");

        when(libService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new IllegalArgumentException());

        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(requestDTO, USER_ID);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // CREATE - FORBIDDEN
    // =========================================================

    @Test
    void createLibrary_shouldReturnForbidden_whenAccessDenied()
            throws InstanceAlreadyExistsException {

        when(requestDTO.libraryName()).thenReturn("Test Library");

        when(libService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new AccessDeniedException("denied"));

        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(requestDTO, USER_ID);

        assertEquals(FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // CREATE - INTERNAL ERROR
    // =========================================================

    @Test
    void createLibrary_shouldReturnInternalError_whenException()
            throws InstanceAlreadyExistsException {

        when(requestDTO.libraryName()).thenReturn("Test Library");

        when(libService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new RuntimeException());

        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(requestDTO, USER_ID);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET ALL - SUCCESS
    // =========================================================

    @Test
    void getAllLibraries_shouldReturnOk_whenExists() {


        ResponseLibraryDTO dto = mock(ResponseLibraryDTO.class);
        when(libService.findAll()).thenReturn(List.of(library));
        when(library.id()).thenReturn(libraryId);

        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {

            mapper.when(() -> LibraryMapper.toDTO(library))
                    .thenReturn(dto);

            ResponseEntity<List<ResponseLibraryDTO>> response =
                    controller.getAllLibraries();

            assertEquals(OK, response.getStatusCode());

            List<ResponseLibraryDTO> body = response.getBody();

            assertNotNull(body);
            assertEquals(1, body.size());
            assertSame(dto, body.get(0));
        }
    }


    // =========================================================
    // GET ALL - EMPTY
    // =========================================================

    @Test
    void getAllLibraries_shouldReturnNoContent_whenEmpty() {

        when(libService.findAll()).thenReturn(List.of());

        ResponseEntity<List<ResponseLibraryDTO>> response =
                controller.getAllLibraries();

        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET ALL - BAD REQUEST
    // =========================================================

    @Test
    void getAllLibraries_shouldReturnBadRequest_whenIllegalArgument() {

        when(libService.findAll())
                .thenThrow(new IllegalArgumentException());

        ResponseEntity<List<ResponseLibraryDTO>> response =
                controller.getAllLibraries();

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET ALL - INTERNAL ERROR
    // =========================================================

    @Test
    void getAllLibraries_shouldReturnInternalError_whenException() {

        when(libService.findAll())
                .thenThrow(new RuntimeException());

        ResponseEntity<List<ResponseLibraryDTO>> response =
                controller.getAllLibraries();

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET BY ID - SUCCESS
    // =========================================================

    @Test
    void getLibrary_shouldReturnOk_whenFound() {

        when(libService.findById(any(LibraryId.class)))
                .thenReturn(library);

        ResponseLibraryDTO dto = mock(ResponseLibraryDTO.class);

        try (MockedStatic<LibraryMapper> mapper =
                     mockStatic(LibraryMapper.class)) {

            mapper.when(() -> LibraryMapper.toDTO(library))
                    .thenReturn(dto);

            ResponseEntity<ResponseLibraryDTO> response =
                    controller.getLibrary(LIBRARY_ID);

            assertEquals(OK, response.getStatusCode());
            assertEquals(dto, response.getBody());
        }
    }

    // =========================================================
    // GET BY ID - NOT FOUND
    // =========================================================

    @Test
    void getLibrary_shouldReturnNotFound_whenMissing() {

        when(libService.findById(any(LibraryId.class)))
                .thenThrow(new IllegalArgumentException());

        ResponseEntity<ResponseLibraryDTO> response =
                controller.getLibrary(LIBRARY_ID);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET BY ID - INTERNAL ERROR
    // =========================================================

    @Test
    void getLibrary_shouldReturnInternalError_whenException() {

        when(libService.findById(any(LibraryId.class)))
                .thenThrow(new RuntimeException());

        ResponseEntity<ResponseLibraryDTO> response =
                controller.getLibrary(LIBRARY_ID);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET BY USER ID - SUCCESS
    // =========================================================

    @Test
    void getLibraryByUserId_shouldReturnOk_whenFound() {

        when(libService.findByUserId(any(UserId.class)))
                .thenReturn(library);

        ResponseLibraryDTO dto = mock(ResponseLibraryDTO.class);

        try (MockedStatic<LibraryMapper> mapper =
                     mockStatic(LibraryMapper.class)) {

            mapper.when(() -> LibraryMapper.toDTO(library))
                    .thenReturn(dto);

            ResponseEntity<ResponseLibraryDTO> response =
                    controller.getLibraryByUserId(USER_ID);

            assertEquals(OK, response.getStatusCode());
            assertEquals(dto, response.getBody());
        }
    }

    // =========================================================
    // GET BY USER ID - BAD REQUEST
    // =========================================================

    @Test
    void getLibraryByUserId_shouldReturnBadRequest_whenMissing() {

        when(libService.findByUserId(any(UserId.class)))
                .thenThrow(new IllegalArgumentException());

        ResponseEntity<ResponseLibraryDTO> response =
                controller.getLibraryByUserId(USER_ID);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET BY USER ID - INTERNAL ERROR
    // =========================================================

    @Test
    void getLibraryByUserId_shouldReturnInternalError_whenException() {

        when(libService.findByUserId(any(UserId.class)))
                .thenThrow(new RuntimeException());

        ResponseEntity<ResponseLibraryDTO> response =
                controller.getLibraryByUserId(USER_ID);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}