package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.LibraryLinkProvider;
import TOPSECRET.domain.Library.Library;
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

import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    // ---------- SUCCESS ----------
    @Test
    void createLibrary_shouldReturnCreated_whenValidRequest() throws Exception {

        // Arrange
        RequestLibraryDTO request = mock(RequestLibraryDTO.class);
        UserId userId = mock(UserId.class);
        Library library = mock(Library.class);
        ResponseLibraryDTO responseDTO = mock(ResponseLibraryDTO.class);
        Link link = mock(Link.class);

        when(request.libraryName()).thenReturn("Test Library");
        when(libService.createAndSave(anyString(), eq(userId))).thenReturn(library);
        when(libLinkProvider.getLinksForLibraries(userId)).thenReturn(List.of(link));

        try (MockedStatic<LibraryMapper> mapperMock = mockStatic(LibraryMapper.class)) {
            mapperMock.when(() -> LibraryMapper.toDTO(library)).thenReturn(responseDTO);

            // Act
            ResponseEntity<ResponseLibraryDTO> response =
                    controller.createLibrary(request, userId);

            // Assert
            assertEquals(CREATED, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

    // ---------- BAD REQUEST ----------
    @Test
    void createLibrary_shouldReturnBadRequest_whenIllegalArgumentException() throws Exception {

        // Arrange
        RequestLibraryDTO request = mock(RequestLibraryDTO.class);
        UserId userId = mock(UserId.class);

        when(request.libraryName()).thenReturn("Test Library");
        when(libService.createAndSave(any(), any())).thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(request, userId);

        // Assert
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    // ---------- FORBIDDEN ----------
    @Test
    void createLibrary_shouldReturnForbidden_whenAccessDenied() throws Exception {

        // Arrange
        RequestLibraryDTO request = mock(RequestLibraryDTO.class);
        UserId userId = mock(UserId.class);

        when(request.libraryName()).thenReturn("Test Library");
        when(libService.createAndSave(any(), any())).thenThrow(new AccessDeniedException("denied"));

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(request, userId);

        // Assert
        assertEquals(FORBIDDEN, response.getStatusCode());
    }

    // ---------- INTERNAL ERROR ----------
    @Test
    void createLibrary_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {

        // Arrange
        RequestLibraryDTO request = mock(RequestLibraryDTO.class);
        UserId userId = mock(UserId.class);

        when(request.libraryName()).thenReturn("Test Library");
        when(libService.createAndSave(any(), any())).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<ResponseLibraryDTO> response =
                controller.createLibrary(request, userId);

        // Assert
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}