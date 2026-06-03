package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.BibliographicalDataLinkProvider;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.bibliographicalData.BibliographicalDataMapper;
import TOPSECRET.dto.bibliographicalData.request.requestBibliographicalDataDTO;
import TOPSECRET.dto.bibliographicalData.response.responseBibliographicalDataDTO;
import TOPSECRET.services.BibliographicalDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    // ================================================================
    // ============== getAllBibliographicalData Tests ================
    // ================================================================

    @Test
    void getAllBibliographicalDataShouldReturnOkWithDTOList() throws InstanceNotFoundException {
        // Arrange
        String userId = "id1";
        BibliographicalData bdDouble = mock(BibliographicalData.class);

        String bdId = "bdId1";
        BibliographicalDataId bdIdDouble = mock(BibliographicalDataId.class);

        List<BibliographicalData> bibliographicalDataList = List.of(bdDouble);

        responseBibliographicalDataDTO dto = mock(responseBibliographicalDataDTO.class);

        List<responseBibliographicalDataDTO> responseList = List.of(dto);

        Map<BibliographicalDataId, List<String>> authorNamesMap =
                Map.of(bdIdDouble, List.of("Author 1", "Author 2"));

        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toDTOs(bibliographicalDataList, authorNamesMap)
            ).thenReturn(responseList);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toBibliographicalDataId(bdId)
            ).thenReturn(bdIdDouble);

            when(service.findAll()).thenReturn(bibliographicalDataList);
            when(service.buildAuthorNamesMap(bibliographicalDataList)).thenReturn(authorNamesMap);
            when(BibliographicalDataMapper.toDTOs(bibliographicalDataList, authorNamesMap)).thenReturn(responseList);
            when(dto.getBdId()).thenReturn(bdId);


            // Act
            ResponseEntity<CollectionModel<responseBibliographicalDataDTO>> response = controller.getAllBibliographicalData(userId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(responseList, response.getBody().getContent().stream().toList());
        }
    }

    @Test
    void getAllBibliographicalDataShouldReturnBadRequestWhenIllegalArgumentExceptionIsThrown() throws InstanceNotFoundException {
        // Arrange
        String userId = "id1";
        when(service.findAll()).thenThrow(new IllegalArgumentException("Invalid request"));

        // Act
        ResponseEntity<CollectionModel<responseBibliographicalDataDTO>> response = controller.getAllBibliographicalData(userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllBibliographicalDataShouldReturnInternalServerErrorWhenUnexpectedExceptionIsThrown() throws InstanceNotFoundException {
        // Arrange
        String userId = "id1";
        when(service.findAll()).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<CollectionModel<responseBibliographicalDataDTO>> response = controller.getAllBibliographicalData(userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ================================================================
    // ============== createBibliographicalData Tests ================
    // ================================================================

    @Test
    void createBibliographicalDataShouldReturnCreatedWhenValidRequest() throws Exception {
        // Arrange
        String id = "id1";
        UserId userIdDouble = mock(UserId.class);

        requestBibliographicalDataDTO request = mock(requestBibliographicalDataDTO.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        List<AuthorId> authorIds = List.of(mock(AuthorId.class));
        List<GenreId> genreIds = List.of(mock(GenreId.class));
        responseBibliographicalDataDTO responseDto = new responseBibliographicalDataDTO("bdId", "Title", List.of("Author One"));

        when(bibliographicalData.id()).thenReturn(bdId);
        when(request.title()).thenReturn("Title");
        when(request.authorName()).thenReturn(List.of("Author One"));
        when(request.originalLanguage()).thenReturn(Locale.ENGLISH);
        when(request.publicationDate()).thenReturn(LocalDate.of(2025, 1, 1));
        when(request.genreName()).thenReturn(List.of("Drama"));
        when(request.chunksOfText()).thenReturn("Chunks of text");

        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {
            mockedMapper.when(() ->
                    BibliographicalDataMapper.toUserId(id)
            ).thenReturn(userIdDouble);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toAuthorId(List.of("Author One"))
            ).thenReturn(authorIds);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toGenreId(List.of("Drama"))
            ).thenReturn(genreIds);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toDTO(
                            eq(bibliographicalData),
                            eq(List.of("Author One"))
                    )
            ).thenReturn(responseDto);

            when(service.createAndSave(
                    eq("Title"),
                    anyList(),
                    eq(Locale.ENGLISH),
                    eq(LocalDate.of(2025, 1, 1)),
                    anyList(),
                    eq("Chunks of text"),
                    eq(userIdDouble)
            )).thenReturn(bibliographicalData);

            when(service.getAuthors(bibliographicalData)).thenReturn(List.of("Author One"));

            when(provider.getLinksForBibliographicalData(userIdDouble)).thenReturn(List.of());

            // Act
            ResponseEntity<responseBibliographicalDataDTO> response = controller.createBibliographicalData(request, id);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }

    @Test
    void createBibliographicalDataShouldReturnBadRequestWhenIllegalArgumentExceptionIsThrown() throws Exception {
        // Arrange
        String id = "id1";
        UserId userIdDouble = mock(UserId.class);

        requestBibliographicalDataDTO request = mock(requestBibliographicalDataDTO.class);

        when(request.title()).thenReturn("Title");
        when(request.authorName()).thenReturn(List.of("Author One"));
        when(request.originalLanguage()).thenReturn(Locale.ENGLISH);
        when(request.publicationDate()).thenReturn(LocalDate.of(2025, 1, 1));
        when(request.genreName()).thenReturn(List.of("Drama"));
        when(request.chunksOfText()).thenReturn("Chunks of text");

        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {
            mockedMapper.when(() ->
                    BibliographicalDataMapper.toUserId(id)
            ).thenReturn(userIdDouble);

        when(service.createAndSave(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                eq(userIdDouble)
        )).thenThrow(new IllegalArgumentException("Invalid data"));

        // Act
        ResponseEntity<responseBibliographicalDataDTO> response = controller.createBibliographicalData(request, id);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        }
    }

    @Test
    void createBibliographicalDataShouldReturnForbiddenWhenAccessDeniedExceptionIsThrown() throws Exception {
        // Arrange
        String id = "id1";
        UserId userIdDouble = mock(UserId.class);

        requestBibliographicalDataDTO request = mock(requestBibliographicalDataDTO.class);

        when(request.title()).thenReturn("Title");
        when(request.authorName()).thenReturn(List.of("Author One"));
        when(request.originalLanguage()).thenReturn(Locale.ENGLISH);
        when(request.publicationDate()).thenReturn(LocalDate.of(2025, 1, 1));
        when(request.genreName()).thenReturn(List.of("Drama"));
        when(request.chunksOfText()).thenReturn("Chunks of text");

        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {
            mockedMapper.when(() ->
                    BibliographicalDataMapper.toUserId(id)
            ).thenReturn(userIdDouble);

            when(service.createAndSave(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                eq(userIdDouble)
        )).thenThrow(new AccessDeniedException("Not allowed"));

        // Act
        ResponseEntity<responseBibliographicalDataDTO> response = controller.createBibliographicalData(request, id);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        }
    }

    @Test
    void createBibliographicalDataShouldReturnInternalServerErrorWhenUnexpectedExceptionIsThrown() throws Exception {

        // Arrange
        String id = "id1";
        UserId userIdDouble = mock(UserId.class);

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
                eq(userIdDouble)
        )).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<responseBibliographicalDataDTO> response = controller.createBibliographicalData(request, id);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createBibliographicalDataShouldReturnCreatedWithLinks()
            throws InstanceNotFoundException, AccessDeniedException, InstanceAlreadyExistsException {
        // Arrange
        String id = "id1";
        UserId userIdDouble = mock(UserId.class);

        requestBibliographicalDataDTO request = mock(requestBibliographicalDataDTO.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        BibliographicalDataId bdId = new BibliographicalDataId("1234");

        List<AuthorId> authorIds = List.of(mock(AuthorId.class));
        List<GenreId> genreIds = List.of(mock(GenreId.class));
        List<String> authors = List.of("Author 1");

        responseBibliographicalDataDTO resultDto =
                new responseBibliographicalDataDTO("1234", "Title", List.of("Author 1", "Author 2"));

        Link customLink = Link.of("/bibliographical-data", "allBibliographicalData");

        when(request.authorName()).thenReturn(List.of("Author 1"));
        when(request.genreName()).thenReturn(List.of("Genre 1"));
        when(request.title()).thenReturn("Title");
        when(request.originalLanguage()).thenReturn(Locale.ENGLISH);
        when(request.publicationDate()).thenReturn(LocalDate.of(2024, 1, 1));
        when(request.chunksOfText()).thenReturn(("Chunk 1"));

        when(service.createAndSave(
                eq("Title"),
                eq(authorIds),
                eq(Locale.ENGLISH),
                eq(LocalDate.of(2024, 1, 1)),
                eq(genreIds),
                eq(("Chunk 1")),
                eq(userIdDouble)
        )).thenReturn(bibliographicalData);

        when(bibliographicalData.id()).thenReturn(bdId);
        when(service.getAuthors(bibliographicalData)).thenReturn(authors);
        when(provider.getLinksForBibliographicalData(userIdDouble)).thenReturn(List.of(customLink));

        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toAuthorId(List.of("Author 1"))
            ).thenReturn(authorIds);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toGenreId(List.of("Genre 1"))
            ).thenReturn(genreIds);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toDTO(bibliographicalData, authors)
            ).thenReturn(resultDto);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toUserId(id)
            ).thenReturn(userIdDouble);

            // Act
            ResponseEntity<responseBibliographicalDataDTO> response =
                    controller.createBibliographicalData(request, id);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());

            responseBibliographicalDataDTO body = response.getBody();

            assertTrue(body.hasLink("self"));
            assertTrue(body.hasLink("allBibliographicalData"));

            assertEquals(
                    "/bibliographical-data",
                    body.getRequiredLink("allBibliographicalData").getHref()
            );
        }
    }

    // ================================================================
    // ============== getBibliographicalDataById Tests ================
    // ================================================================

    @Test
    void getBibliographicalDataById_returnsOkWithDtoAndLinks() throws Exception {
        // Arrange
        String id = "id1";
        UserId userId = new UserId(id);

        String bdId = "bdId1";
        BibliographicalDataId bdIdDouble = mock(BibliographicalDataId.class);

        BibliographicalData bdDouble = mock(BibliographicalData.class);

        Link extraLink = Link.of("/some-link", "someRel");

        responseBibliographicalDataDTO resultDto =
                new responseBibliographicalDataDTO("bdId1", "Title", List.of("Author 1"));

        when(service.findById(bdIdDouble)).thenReturn(bdDouble);
        when(service.getAuthors(bdDouble)).thenReturn(List.of("Author One"));
        when(provider.getLinksForBibliographicalData(userId)).thenReturn(List.of(extraLink));

        // Act
        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toBibliographicalDataId(bdId)
            ).thenReturn(bdIdDouble);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toDTO(bdDouble, List.of("Author One"))
            ).thenReturn(resultDto);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toUserId(id)
            ).thenReturn(userId);


            ResponseEntity<responseBibliographicalDataDTO> response = controller.getBibliographicalDataById(bdId, id);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());

            assertTrue(response.getBody().hasLink("self"));
            assertTrue(response.getBody().hasLink("someRel"));
        }
    }

    @Test
    void getBibliographicalDataById_whenDataDoesNotExist_returnsNotFound() throws Exception {
        String userId = "id1";
        String bdId = "bdId1";

        BibliographicalDataId bdIdDouble = mock(BibliographicalDataId.class);
        UserId userIdDouble = mock(UserId.class);

        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toBibliographicalDataId(bdId)
            ).thenReturn(bdIdDouble);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toUserId(userId)
            ).thenReturn(userIdDouble);

            when(service.findById(bdIdDouble))
                    .thenThrow(new InstanceNotFoundException("BibliographicalData was not found:"));

            ResponseEntity<responseBibliographicalDataDTO> response =
                    controller.getBibliographicalDataById(bdId, userId);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void getBibliographicalDataById_whenProviderReturnsNoLinks_stillAddsSelfLink() throws Exception {
        // Arrange
        String id = "id1";
        UserId userId = new UserId(id);

        String bdId = "bdId1";
        BibliographicalDataId bdIdDouble = mock(BibliographicalDataId.class);

        BibliographicalData bdDouble = mock(BibliographicalData.class);

        when(service.findById(bdIdDouble)).thenReturn(bdDouble);
        when(service.getAuthors(bdDouble)).thenReturn(List.of("Author One"));
        when(provider.getLinksForBibliographicalData(userId)).thenReturn(List.of());

        responseBibliographicalDataDTO resultDto =
                new responseBibliographicalDataDTO("bdId1", "Title", List.of("Author One"));

        // Act
        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toBibliographicalDataId(bdId)
            ).thenReturn(bdIdDouble);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toDTO(bdDouble, List.of("Author One"))
            ).thenReturn(resultDto);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toUserId(id)
            ).thenReturn(userId);

            ResponseEntity<responseBibliographicalDataDTO> response = controller.getBibliographicalDataById(bdId, id);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().hasLink("self"));
        }
    }
}