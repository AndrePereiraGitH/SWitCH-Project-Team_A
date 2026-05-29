package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.BibliographicalDataLinkProvider;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.bibliographicalData.request.requestBibliographicalDataDTO;
import TOPSECRET.dto.bibliographicalData.response.responseBibliographicalDataDTO;
import TOPSECRET.services.BibliographicalDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.management.InstanceNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BibliographicalDataRESTControllerTest {

    @Mock
    BibliographicalDataService service;

    @Mock
    BibliographicalDataLinkProvider provider;

    @InjectMocks
    BibliographicalDataRESTController controller;

    //
    // getAllBibliographicalData Tests
    //

    @Test
    void getAllBibliographicalDataShouldReturnOkWithDTOList() throws InstanceNotFoundException {
        // Arrange
        List<BibliographicalData> bibliographicalData = List.of(mock(BibliographicalData.class));

        List<responseBibliographicalDataDTO> dto = List.of(mock(responseBibliographicalDataDTO.class));

        when(service.findAll()).thenReturn(bibliographicalData);
        when(service.createDTOlist(bibliographicalData)).thenReturn(dto);

        // Act
        ResponseEntity<List<responseBibliographicalDataDTO>> response = controller.getAllBibliographicalData();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getAllBibliographicalDataShouldReturnBadRequestWhenIllegalArgumentExceptionIsThrown() throws InstanceNotFoundException {
        // Arrange
        when(service.findAll()).thenThrow(new IllegalArgumentException("Invalid request"));

        // Act
        ResponseEntity<List<responseBibliographicalDataDTO>> response = controller.getAllBibliographicalData();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllBibliographicalDataShouldReturnInternalServerErrorWhenUnexpectedExceptionIsThrown() throws InstanceNotFoundException {
        // Arrange
        when(service.findAll()).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<List<responseBibliographicalDataDTO>> response = controller.getAllBibliographicalData();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    //
    // createBibliographicalData Tests
    //

    @Test
    void createBibliographicalDataShouldReturnCreatedWhenValidRequest() throws Exception {
        // Arrange
        UserId userId = mock(UserId.class);
        requestBibliographicalDataDTO request = mock(requestBibliographicalDataDTO.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        when(bibliographicalData.id()).thenReturn(bdId);
        when(bdId.getID()).thenReturn("1234");
        when(bibliographicalData.getTitle()).thenReturn("Title");

        when(request.title()).thenReturn("Title");
        when(request.authorName()).thenReturn(List.of("Author One"));
        when(request.originalLanguage()).thenReturn(Locale.ENGLISH);
        when(request.publicationDate()).thenReturn(LocalDate.of(2025, 1, 1));
        when(request.genreName()).thenReturn(List.of("Drama"));
        when(request.chunksOfText()).thenReturn("Chunks of text");

        when(service.createAndSave(
                eq("Title"),
                anyList(),
                eq(Locale.ENGLISH),
                eq(LocalDate.of(2025, 1, 1)),
                anyList(),
                eq("Chunks of text"),
                eq(userId)
        )).thenReturn(bibliographicalData);

        when(service.getAuthors(bibliographicalData)).thenReturn(List.of("Author One"));

        when(provider.getLinksForBibliographicalData(userId)).thenReturn(List.of());

        // Act
        ResponseEntity<responseBibliographicalDataDTO> response = controller.createBibliographicalData(request, userId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void createBibliographicalDataShouldReturnBadRequestWhenIllegalArgumentExceptionIsThrown() throws Exception {
        // Arrange
        UserId userId = new UserId("user-1");

        requestBibliographicalDataDTO request = mock(requestBibliographicalDataDTO.class);

        when(request.title()).thenReturn("Title");
        when(request.authorName()).thenReturn(List.of("Author One"));
        when(request.originalLanguage()).thenReturn(Locale.ENGLISH);
        when(request.publicationDate()).thenReturn(LocalDate.of(2025, 1, 1));
        when(request.genreName()).thenReturn(List.of("Drama"));
        when(request.chunksOfText()).thenReturn("Chunks of text");

        when(service.createAndSave(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                eq(userId)
        )).thenThrow(new IllegalArgumentException("Invalid data"));

        // Act
        ResponseEntity<responseBibliographicalDataDTO> response = controller.createBibliographicalData(request, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createBibliographicalDataShouldReturnForbiddenWhenAccessDeniedExceptionIsThrown()
            throws Exception {

        // Arrange
        UserId userId = new UserId("user-1");

        requestBibliographicalDataDTO request = mock(requestBibliographicalDataDTO.class);

        when(request.title()).thenReturn("Title");
        when(request.authorName()).thenReturn(List.of("Author One"));
        when(request.originalLanguage()).thenReturn(Locale.ENGLISH);
        when(request.publicationDate()).thenReturn(LocalDate.of(2025, 1, 1));
        when(request.genreName()).thenReturn(List.of("Drama"));
        when(request.chunksOfText()).thenReturn("Chunks of text");

        when(service.createAndSave(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                eq(userId)
        )).thenThrow(new AccessDeniedException("Not allowed"));

        // Act
        ResponseEntity<responseBibliographicalDataDTO> response =
                controller.createBibliographicalData(request, userId);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createBibliographicalDataShouldReturnInternalServerErrorWhenUnexpectedExceptionIsThrown()
            throws Exception {

        // Arrange
        UserId userId = new UserId("user-1");

        requestBibliographicalDataDTO request = mock(requestBibliographicalDataDTO.class);

        when(request.title()).thenReturn("Title");
        when(request.authorName()).thenReturn(List.of("Author One"));
        when(request.originalLanguage()).thenReturn(Locale.ENGLISH);
        when(request.publicationDate()).thenReturn(LocalDate.of(2025, 1, 1));
        when(request.genreName()).thenReturn(List.of("Drama"));
        when(request.chunksOfText()).thenReturn("Chunks of text");

        when(service.createAndSave(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                eq(userId)
        )).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<responseBibliographicalDataDTO> response =
                controller.createBibliographicalData(request, userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}