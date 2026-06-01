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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class LibraryRESTControllerTest {

    @Mock
    private LibraryLinkProvider libLinkProvider;

    @Mock
    private LibraryService libService;

    @InjectMocks
    private LibraryRESTController controller;

    // =========================================================
    // CREATE - SUCCESS
    // =========================================================
    @Test
    void createLibrary_shouldReturnCreated_whenValidRequest() throws InstanceAlreadyExistsException {

        // ARRANGE
        RequestLibraryDTO request = mock(RequestLibraryDTO.class);
        UserId userId = mock(UserId.class);

        Library library = mock(Library.class);
        LibraryId libraryId = mock(LibraryId.class);

        when(request.libraryName()).thenReturn("Test Library");
        when(library.id()).thenReturn(libraryId);

        when(libService.createAndSave(anyString(), eq(userId)))
                .thenReturn(library);

        Link providerLink = Link.of("/libraries", "all-libraries");

        when(libLinkProvider.getLinksForLibraries(userId))
                .thenReturn(List.of(providerLink));

        ResponseLibraryDTO dto = mock(ResponseLibraryDTO.class);

        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {

            mapper.when(() -> LibraryMapper.toDTO(library))
                    .thenReturn(dto);

            // ACT
            ResponseEntity<ResponseLibraryDTO> response =
                    controller.createLibrary(request, userId);

            // ASSERT
            assertEquals(CREATED, response.getStatusCode());
            assertEquals(dto, response.getBody());

            assertDoesNotThrow(() -> {
                List<Link> links = libLinkProvider.getLinksForLibraries(userId);
                assertFalse(links.isEmpty());
            });
        }
    }

    // =========================================================
    // CREATE - BAD REQUEST
    // =========================================================
    @Test
    void createLibrary_shouldReturnBadRequest_whenIllegalArgument() throws InstanceAlreadyExistsException {

        // ARRANGE
        RequestLibraryDTO request = mock(RequestLibraryDTO.class);
        UserId userId = mock(UserId.class);

        when(request.libraryName()).thenReturn("Test Library");

        when(libService.createAndSave(anyString(), any()))
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(request, userId);

        // ASSERT
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // CREATE - FORBIDDEN
    // =========================================================
    @Test
    void createLibrary_shouldReturnForbidden_whenAccessDenied() throws InstanceAlreadyExistsException {

        // ARRANGE
        RequestLibraryDTO request = mock(RequestLibraryDTO.class);
        UserId userId = mock(UserId.class);

        when(request.libraryName()).thenReturn("Test Library");

        when(libService.createAndSave(anyString(), any()))
                .thenThrow(new AccessDeniedException("denied"));

        // ACT
        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(request, userId);

        // ASSERT
        assertEquals(FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // CREATE - INTERNAL ERROR
    // =========================================================
    @Test
    void createLibrary_shouldReturnInternalError_whenException() throws InstanceAlreadyExistsException {

        // ARRANGE
        RequestLibraryDTO request = mock(RequestLibraryDTO.class);
        UserId userId = mock(UserId.class);

        when(request.libraryName()).thenReturn("Test Library");

        when(libService.createAndSave(anyString(), any()))
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(request, userId);

        // ASSERT
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET ALL - SUCCESS
    // =========================================================
    @Test
    void getAllLibraries_shouldReturnOk_whenExists() {

        // ARRANGE
        Library library = mock(Library.class);
        LibraryId id = mock(LibraryId.class);

        when(library.id()).thenReturn(id);
        when(libService.findAll()).thenReturn(List.of(library));

        ResponseLibraryDTO dto = mock(ResponseLibraryDTO.class);

        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {

            mapper.when(() -> LibraryMapper.toDTO(library))
                    .thenReturn(dto);

            // ACT
            ResponseEntity<List<ResponseLibraryDTO>> response =
                    controller.getAllLibraries();

            // ASSERT
            assertEquals(OK, response.getStatusCode());

            List<ResponseLibraryDTO> body = response.getBody();

            assertNotNull(body);
            assertEquals(1, body.size());
            assertEquals(dto, body.get(0));
        }
    }

    // =========================================================
    // GET ALL - EMPTY
    // =========================================================
    @Test
    void getAllLibraries_shouldReturnNoContent_whenEmpty() {

        // ARRANGE
        when(libService.findAll()).thenReturn(List.of());

        // ACT
        ResponseEntity<List<ResponseLibraryDTO>> response =
                controller.getAllLibraries();

        // ASSERT
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET ALL - BAD REQUEST
    // =========================================================
    @Test
    void getAllLibraries_shouldReturnBadRequest_whenIllegalArgument() {

        // ARRANGE
        when(libService.findAll())
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<List<ResponseLibraryDTO>> response =
                controller.getAllLibraries();

        // ASSERT
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET ALL - INTERNAL ERROR
    // =========================================================
    @Test
    void getAllLibraries_shouldReturnInternalError_whenException() {

        // ARRANGE
        when(libService.findAll())
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<List<ResponseLibraryDTO>> response =
                controller.getAllLibraries();

        // ASSERT
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET BY ID - SUCCESS
    // =========================================================
    @Test
    void getLibrary_shouldReturnOk_whenFound() {

        // ARRANGE
        LibraryId id = mock(LibraryId.class);
        Library library = mock(Library.class);

        when(libService.findById(id)).thenReturn(library);

        ResponseLibraryDTO dto = mock(ResponseLibraryDTO.class);

        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {

            mapper.when(() -> LibraryMapper.toDTO(library))
                    .thenReturn(dto);

            // ACT
            ResponseEntity<ResponseLibraryDTO> response =
                    controller.getLibrary(id);

            // ASSERT
            assertEquals(OK, response.getStatusCode());
            assertEquals(dto, response.getBody());
        }
    }

    // =========================================================
    // GET BY ID - NOT FOUND
    // =========================================================
    @Test
    void getLibrary_shouldReturnNotFound_whenMissing() {

        // ARRANGE
        LibraryId id = mock(LibraryId.class);

        when(libService.findById(id))
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<ResponseLibraryDTO> response =
                controller.getLibrary(id);

        // ASSERT
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET BY ID - INTERNAL ERROR
    // =========================================================
    @Test
    void getLibrary_shouldReturnInternalError_whenException() {

        // ARRANGE
        LibraryId id = mock(LibraryId.class);

        when(libService.findById(id))
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<ResponseLibraryDTO> response =
                controller.getLibrary(id);

        // ASSERT
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}