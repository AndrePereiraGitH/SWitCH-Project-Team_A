package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.LibraryLinkProvider;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.library.LibraryMapper;
import TOPSECRET.dto.library.request.RequestLibraryDTO;
import TOPSECRET.dto.library.response.ResponseLibraryDTO;
import TOPSECRET.dto.user.UserMapper;
import TOPSECRET.services.LibraryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
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
    private final String REQUESTING_USER_ID = "user-456";

    @Mock
    private LibraryLinkProvider libLinkProvider;

    @Mock
    private LibraryService libService;

    @Mock
    private RequestLibraryDTO requestDTO;

    @Mock
    ResponseLibraryDTO responseDTO;

    @Mock
    private Library library;

    @Mock
    private LibraryId libraryId;

    @Mock
    UserId userId;

    @InjectMocks
    private LibraryRESTController controller;

    // =========================================================
    // CREATE - SUCCESS
    // =========================================================

    @Test
    void createLibrary_shouldReturnCreated_whenValidRequest()
            throws InstanceAlreadyExistsException, InstanceNotFoundException {

        // Arrange
        String expectedId = "id";
        String expectedLibraryName = "libraryName";
        Link links = Link.of("/libraries/provider", "provider-rel");

        when(library.id()).thenReturn(libraryId);

        Link selfRel = linkTo(methodOn(LibraryRESTController.class)
                .getLibrary(library.id().toString(), USER_ID))
                .withSelfRel();

        Links expectedLinks = Links.of(links, selfRel);

        when(requestDTO.libraryName()).thenReturn(expectedLibraryName);
        when(libService.createAndSave(expectedLibraryName, USER_ID_DOMAIN))
                .thenReturn(library);
        when(libLinkProvider.getOwnerLinks(USER_ID_DOMAIN))
                .thenReturn(List.of(links));
        when(libraryId.getId()).thenReturn(expectedId);
        when(library.getLibraryName()).thenReturn(expectedLibraryName);

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(requestDTO, USER_ID);

        // Assert
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

        // Arrange
        when(requestDTO.libraryName()).thenReturn("Test Library");
        when(libService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(requestDTO, USER_ID);

        // Assert
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // CREATE - FORBIDDEN
    // =========================================================

    @Test
    void createLibrary_shouldReturnForbidden_whenAccessDenied()
            throws InstanceAlreadyExistsException {

        // Arrange
        when(requestDTO.libraryName()).thenReturn("Test Library");
        when(libService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new AccessDeniedException("denied"));

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(requestDTO, USER_ID);

        // Assert
        assertEquals(FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // CREATE - INTERNAL ERROR
    // =========================================================

    @Test
    void createLibrary_shouldReturnInternalError_whenException()
            throws InstanceAlreadyExistsException {

        // Arrange
        when(requestDTO.libraryName()).thenReturn("Test Library");
        when(libService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new RuntimeException());

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(requestDTO, USER_ID);

        // Assert
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // CREATE - NOT FOUND
    // =========================================================

    @Test
    void createLibrary_shouldReturnNotFound_whenUserDoesNotExist()
            throws InstanceAlreadyExistsException, InstanceNotFoundException {

        // Arrange
        when(requestDTO.libraryName()).thenReturn("Test Library");
        when(libService.createAndSave(anyString(), any(UserId.class)))
                .thenReturn(library);
        when(library.id()).thenReturn(libraryId);
        when(libLinkProvider.getOwnerLinks(any(UserId.class)))
                .thenThrow(new InstanceNotFoundException("User not found"));

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(requestDTO, USER_ID);

        // Assert
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // CREATE - CONFLICT
    // =========================================================

    @Test
    void createLibrary_shouldReturnConflict_whenLibraryAlreadyExists()
            throws InstanceAlreadyExistsException {

        // Arrange
        when(requestDTO.libraryName()).thenReturn("Test Library");
        when(libService.createAndSave(anyString(), any(UserId.class)))
                .thenThrow(new InstanceAlreadyExistsException());

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(requestDTO, USER_ID);

        // Assert
        assertEquals(CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET ALL - SUCCESS
    // =========================================================

    @Test
    void getAllLibraries_shouldReturnOk_whenExists() throws InstanceNotFoundException {

        // Arrange
        ResponseLibraryDTO dto = mock(ResponseLibraryDTO.class);
        when(libService.findAll(any(UserId.class))).thenReturn(List.of(library));
        when(library.id()).thenReturn(libraryId);

        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {
            mapper.when(() -> LibraryMapper.toDTO(library)).thenReturn(dto);

            // Act
            ResponseEntity<List<ResponseLibraryDTO>> response =
                    controller.getAllLibraries(REQUESTING_USER_ID);

            // Assert
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertSame(dto, response.getBody().get(0));
        }
    }

    // =========================================================
    // GET ALL - EMPTY
    // =========================================================

    @Test
    void getAllLibraries_shouldReturnOK_whenEmpty() throws InstanceNotFoundException {

        // Arrange
        when(libService.findAll(any(UserId.class))).thenReturn(List.of());

        // Act
        ResponseEntity<List<ResponseLibraryDTO>> response =
                controller.getAllLibraries(REQUESTING_USER_ID);

        // Assert
        assertEquals(OK, response.getStatusCode());
    }

    // =========================================================
    // GET ALL - FORBIDDEN
    // =========================================================

    @Test
    void getAllLibraries_shouldReturnForbidden_whenAccessDenied() throws InstanceNotFoundException {

        // Arrange
        when(libService.findAll(any(UserId.class)))
                .thenThrow(new AccessDeniedException("denied"));

        // Act
        ResponseEntity<List<ResponseLibraryDTO>> response =
                controller.getAllLibraries(REQUESTING_USER_ID);

        // Assert
        assertEquals(FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET ALL - BAD REQUEST
    // =========================================================

    @Test
    void getAllLibraries_shouldReturnBadRequest_whenIllegalArgument() throws InstanceNotFoundException {

        // Arrange
        when(libService.findAll(any(UserId.class)))
                .thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<List<ResponseLibraryDTO>> response =
                controller.getAllLibraries(REQUESTING_USER_ID);

        // Assert
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET ALL - INTERNAL ERROR
    // =========================================================

    @Test
    void getAllLibraries_shouldReturnInternalError_whenException() throws InstanceNotFoundException {

        // Arrange
        when(libService.findAll(any(UserId.class)))
                .thenThrow(new RuntimeException());

        // Act
        ResponseEntity<List<ResponseLibraryDTO>> response =
                controller.getAllLibraries(REQUESTING_USER_ID);

        // Assert
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
// GET BY ID - SUCCESS
// =========================================================

    @Test
    void getLibrary_shouldReturnOk_whenFound() throws InstanceNotFoundException {

        // Arrange
        when(libService.findById(any(LibraryId.class), any(UserId.class))).thenReturn(library);

        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {
            mapper.when(() -> LibraryMapper.toLibraryId(LIBRARY_ID)).thenReturn(libraryId);
            mapper.when(() -> LibraryMapper.toDTO(library)).thenReturn(responseDTO);

            // Act
            ResponseEntity<ResponseLibraryDTO> response =
                    controller.getLibrary(LIBRARY_ID, REQUESTING_USER_ID);

            // Assert
            assertEquals(OK, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

// =========================================================
// GET BY ID - FORBIDDEN
// =========================================================

    @Test
    void getLibrary_shouldReturnForbidden_whenAccessDenied() throws InstanceNotFoundException {

        // Arrange
        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {
            mapper.when(() -> LibraryMapper.toLibraryId(LIBRARY_ID)).thenReturn(libraryId);
            when(libService.findById(any(LibraryId.class), any(UserId.class)))
                    .thenThrow(new AccessDeniedException("denied"));

            // Act
            ResponseEntity<ResponseLibraryDTO> response =
                    controller.getLibrary(LIBRARY_ID, REQUESTING_USER_ID);

            // Assert
            assertEquals(FORBIDDEN, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

// =========================================================
// GET BY ID - NOT FOUND
// =========================================================

    @Test
    void getLibrary_shouldReturnNotFound_whenMissing() throws InstanceNotFoundException {

        // Arrange
        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {
            mapper.when(() -> LibraryMapper.toLibraryId(LIBRARY_ID)).thenReturn(libraryId);
            when(libService.findById(any(LibraryId.class), any(UserId.class)))
                    .thenThrow(new InstanceNotFoundException());

            // Act
            ResponseEntity<ResponseLibraryDTO> response =
                    controller.getLibrary(LIBRARY_ID, REQUESTING_USER_ID);

            // Assert
            assertEquals(NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

// =========================================================
// GET BY ID - BAD REQUEST
// =========================================================

    @Test
    void getLibrary_shouldReturnBadRequest_whenIdFormatIsInvalid() {

        // Arrange
        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {
            mapper.when(() -> LibraryMapper.toLibraryId(LIBRARY_ID))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<ResponseLibraryDTO> response =
                    controller.getLibrary(LIBRARY_ID, REQUESTING_USER_ID);

            // Assert
            assertEquals(BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

// =========================================================
// GET BY ID - INTERNAL ERROR
// =========================================================

    @Test
    void getLibrary_shouldReturnInternalError_whenException() throws InstanceNotFoundException {

        // Arrange
        when(libService.findById(any(LibraryId.class), any(UserId.class)))
                .thenThrow(new RuntimeException());

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.getLibrary(LIBRARY_ID, REQUESTING_USER_ID);

        // Assert
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET BY USER ID - SUCCESS
    // =========================================================

    @Test
    void getLibraryByUserId_shouldReturnOk_whenFound() throws InstanceNotFoundException {

        // Arrange
        when(libService.findByUserId(any(UserId.class), any(UserId.class))).thenReturn(library);

        try (MockedStatic<LibraryMapper> libraryMapper = mockStatic(LibraryMapper.class);
             MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {

            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(userId);
            libraryMapper.when(() -> LibraryMapper.toDTO(library)).thenReturn(responseDTO);

            // Act
            ResponseEntity<ResponseLibraryDTO> response =
                    controller.getLibraryByUserId(USER_ID, REQUESTING_USER_ID);

            // Assert
            assertEquals(OK, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

    // =========================================================
    // GET BY USER ID - FORBIDDEN
    // =========================================================

    @Test
    void getLibraryByUserId_shouldReturnForbidden_whenAccessDenied() throws InstanceNotFoundException {

        // Arrange
        when(libService.findByUserId(any(UserId.class), any(UserId.class)))
                .thenThrow(new AccessDeniedException("denied"));

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.getLibraryByUserId(USER_ID, REQUESTING_USER_ID);

        // Assert
        assertEquals(FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET BY USER ID - BAD REQUEST
    // =========================================================

    @Test
    void getLibraryByUserId_shouldReturnBadRequest_whenIllegalArgument() throws InstanceNotFoundException {

        // Arrange
        when(libService.findByUserId(any(UserId.class), any(UserId.class)))
                .thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.getLibraryByUserId(USER_ID, REQUESTING_USER_ID);

        // Assert
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET BY USER ID - NOT FOUND
    // =========================================================

    @Test
    void getLibraryByUserId_shouldReturnNotFound_whenLibraryDoesNotExist() throws InstanceNotFoundException {

        // Arrange
        when(libService.findByUserId(any(UserId.class), any(UserId.class)))
                .thenThrow(new InstanceNotFoundException("Library not found"));

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.getLibraryByUserId(USER_ID, REQUESTING_USER_ID);

        // Assert
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
    // GET BY USER ID - INTERNAL ERROR
    // =========================================================

    @Test
    void getLibraryByUserId_shouldReturnInternalError_whenException() throws InstanceNotFoundException {

        // Arrange
        when(libService.findByUserId(any(UserId.class), any(UserId.class)))
                .thenThrow(new RuntimeException());

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.getLibraryByUserId(USER_ID, REQUESTING_USER_ID);

        // Assert
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =========================================================
// OPTIONS - /libraries
// =========================================================

    @Test
    void optionsLibraries_shouldReturnGetAndPost_whenAdmin() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGetAll(any(UserId.class))).thenReturn(true);
        when(libService.canCreate(any(UserId.class))).thenReturn(false);

        // Act
        ResponseEntity<?> response = controller.optionsLibraries(REQUESTING_USER_ID);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.GET));
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.POST));
    }

    @Test
    void optionsLibraries_shouldReturnPost_whenNormalUser() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGetAll(any(UserId.class))).thenReturn(false);
        when(libService.canCreate(any(UserId.class))).thenReturn(true);

        // Act
        ResponseEntity<?> response = controller.optionsLibraries(REQUESTING_USER_ID);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.POST));
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.GET));
    }

    @Test
    void optionsLibraries_shouldReturnOnlyOptions_whenNoPermissions() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGetAll(any(UserId.class))).thenReturn(false);
        when(libService.canCreate(any(UserId.class))).thenReturn(false);

        // Act
        ResponseEntity<?> response = controller.optionsLibraries(REQUESTING_USER_ID);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.GET));
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.POST));
    }

    @Test
    void optionsLibraries_shouldReturnNotFound_whenUserDoesNotExist() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGetAll(any(UserId.class)))
                .thenThrow(new InstanceNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = controller.optionsLibraries(REQUESTING_USER_ID);

        // Assert
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void optionsLibraries_shouldReturnBadRequest_whenIllegalArgument() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGetAll(any(UserId.class)))
                .thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<?> response = controller.optionsLibraries(REQUESTING_USER_ID);

        // Assert
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void optionsLibraries_shouldReturnInternalError_whenException() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGetAll(any(UserId.class)))
                .thenThrow(new RuntimeException());

        // Act
        ResponseEntity<?> response = controller.optionsLibraries(REQUESTING_USER_ID);

        // Assert
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void optionsLibraries_shouldNeverAllowDelete() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGetAll(any(UserId.class))).thenReturn(true);
        when(libService.canCreate(any(UserId.class))).thenReturn(true);

        // Act
        ResponseEntity<?> response = controller.optionsLibraries(REQUESTING_USER_ID);

        // Assert
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.DELETE));
    }

    @Test
    void optionsLibraries_shouldNeverAllowPut() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGetAll(any(UserId.class))).thenReturn(true);
        when(libService.canCreate(any(UserId.class))).thenReturn(true);

        // Act
        ResponseEntity<?> response = controller.optionsLibraries(REQUESTING_USER_ID);

        // Assert
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.PUT));
    }

    @Test
    void optionsLibraries_shouldNeverAllowPatch() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGetAll(any(UserId.class))).thenReturn(true);
        when(libService.canCreate(any(UserId.class))).thenReturn(true);

        // Act
        ResponseEntity<?> response = controller.optionsLibraries(REQUESTING_USER_ID);

        // Assert
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.PATCH));
    }

// =========================================================
// OPTIONS - /libraries/{libraryId}
// =========================================================

    @Test
    void optionsLibraryById_shouldReturnGet_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(libService.findById(any(LibraryId.class), any(UserId.class))).thenReturn(library);

        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {
            mapper.when(() -> LibraryMapper.toLibraryId(LIBRARY_ID)).thenReturn(libraryId);

            // Act
            ResponseEntity<?> response = controller.optionsLibraryById(LIBRARY_ID, REQUESTING_USER_ID);

            // Assert
            assertEquals(OK, response.getStatusCode());
            assertTrue(response.getHeaders().getAllow().contains(HttpMethod.GET));
            assertTrue(response.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
        }
    }

    @Test
    void optionsLibraryById_shouldReturnForbidden_whenAccessDenied() throws InstanceNotFoundException {

        // Arrange
        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {
            mapper.when(() -> LibraryMapper.toLibraryId(LIBRARY_ID)).thenReturn(libraryId);
            when(libService.findById(any(LibraryId.class), any(UserId.class)))
                    .thenThrow(new AccessDeniedException("denied"));

            // Act
            ResponseEntity<?> response = controller.optionsLibraryById(LIBRARY_ID, REQUESTING_USER_ID);

            // Assert
            assertEquals(FORBIDDEN, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void optionsLibraryById_shouldReturnNotFound_whenLibraryDoesNotExist() throws InstanceNotFoundException {

        // Arrange
        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {
            mapper.when(() -> LibraryMapper.toLibraryId(LIBRARY_ID)).thenReturn(libraryId);
            when(libService.findById(any(LibraryId.class), any(UserId.class)))
                    .thenThrow(new InstanceNotFoundException());

            // Act
            ResponseEntity<?> response = controller.optionsLibraryById(LIBRARY_ID, REQUESTING_USER_ID);

            // Assert
            assertEquals(NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void optionsLibraryById_shouldReturnBadRequest_whenIdFormatIsInvalid() {

        // Arrange
        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {
            mapper.when(() -> LibraryMapper.toLibraryId(LIBRARY_ID))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<?> response = controller.optionsLibraryById(LIBRARY_ID, REQUESTING_USER_ID);

            // Assert
            assertEquals(BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void optionsLibraryById_shouldReturnInternalError_whenException() throws InstanceNotFoundException {

        // Arrange
        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {
            mapper.when(() -> LibraryMapper.toLibraryId(LIBRARY_ID)).thenReturn(libraryId);
            when(libService.findById(any(LibraryId.class), any(UserId.class)))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<?> response = controller.optionsLibraryById(LIBRARY_ID, REQUESTING_USER_ID);

            // Assert
            assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void optionsLibraryById_shouldNeverAllowPost() throws InstanceNotFoundException {

        // Arrange
        when(libService.findById(any(LibraryId.class), any(UserId.class))).thenReturn(library);

        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {
            mapper.when(() -> LibraryMapper.toLibraryId(LIBRARY_ID)).thenReturn(libraryId);

            // Act
            ResponseEntity<?> response = controller.optionsLibraryById(LIBRARY_ID, REQUESTING_USER_ID);

            // Assert
            assertFalse(response.getHeaders().getAllow().contains(HttpMethod.POST));
        }
    }

    @Test
    void optionsLibraryById_shouldNeverAllowDelete() throws InstanceNotFoundException {

        // Arrange
        when(libService.findById(any(LibraryId.class), any(UserId.class))).thenReturn(library);

        try (MockedStatic<LibraryMapper> mapper = mockStatic(LibraryMapper.class)) {
            mapper.when(() -> LibraryMapper.toLibraryId(LIBRARY_ID)).thenReturn(libraryId);

            // Act
            ResponseEntity<?> response = controller.optionsLibraryById(LIBRARY_ID, REQUESTING_USER_ID);

            // Assert
            assertFalse(response.getHeaders().getAllow().contains(HttpMethod.DELETE));
        }
    }

// =========================================================
// OPTIONS - /libraries/by-user/{userId}
// =========================================================

    @Test
    void optionsLibraryByUser_shouldReturnGet_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGet(any(UserId.class), any(UserId.class))).thenReturn(true);

        // Act
        ResponseEntity<?> response = controller.optionsLibraryByUser(USER_ID, REQUESTING_USER_ID);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.GET));
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
    }

    @Test
    void optionsLibraryByUser_shouldReturnForbidden_whenAccessDenied() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGet(any(UserId.class), any(UserId.class))).thenReturn(false);

        // Act
        ResponseEntity<?> response = controller.optionsLibraryByUser(USER_ID, REQUESTING_USER_ID);

        // Assert
        assertEquals(FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void optionsLibraryByUser_shouldReturnNotFound_whenUserDoesNotExist() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGet(any(UserId.class), any(UserId.class)))
                .thenThrow(new InstanceNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = controller.optionsLibraryByUser(USER_ID, REQUESTING_USER_ID);

        // Assert
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void optionsLibraryByUser_shouldReturnBadRequest_whenIllegalArgument() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGet(any(UserId.class), any(UserId.class)))
                .thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<?> response = controller.optionsLibraryByUser(USER_ID, REQUESTING_USER_ID);

        // Assert
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void optionsLibraryByUser_shouldReturnInternalError_whenException() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGet(any(UserId.class), any(UserId.class)))
                .thenThrow(new RuntimeException());

        // Act
        ResponseEntity<?> response = controller.optionsLibraryByUser(USER_ID, REQUESTING_USER_ID);

        // Assert
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void optionsLibraryByUser_shouldNeverAllowPost() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGet(any(UserId.class), any(UserId.class))).thenReturn(true);

        // Act
        ResponseEntity<?> response = controller.optionsLibraryByUser(USER_ID, REQUESTING_USER_ID);

        // Assert
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.POST));
    }

    @Test
    void optionsLibraryByUser_shouldNeverAllowDelete() throws InstanceNotFoundException {

        // Arrange
        when(libService.canGet(any(UserId.class), any(UserId.class))).thenReturn(true);

        // Act
        ResponseEntity<?> response = controller.optionsLibraryByUser(USER_ID, REQUESTING_USER_ID);

        // Assert
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.DELETE));
    }
}