package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.BibliographicalDataLinkProvider;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.bibliographicalData.BibliographicalDataMapper;
import TOPSECRET.dto.bibliographicalData.request.RequestBibliographicalDataDTO;
import TOPSECRET.dto.bibliographicalData.response.ResponseBibliographicalDataDTO;
import TOPSECRET.services.BibliographicalDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

        ResponseBibliographicalDataDTO dto = mock(ResponseBibliographicalDataDTO.class);

        List<ResponseBibliographicalDataDTO> responseList = List.of(dto);

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
            ResponseEntity<CollectionModel<ResponseBibliographicalDataDTO>> response = controller.getAllBibliographicalData();

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
        ResponseEntity<CollectionModel<ResponseBibliographicalDataDTO>> response = controller.getAllBibliographicalData();

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
        ResponseEntity<CollectionModel<ResponseBibliographicalDataDTO>> response = controller.getAllBibliographicalData();

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

        RequestBibliographicalDataDTO request = mock(RequestBibliographicalDataDTO.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        Locale originalLanguageDouble = mock(Locale.class);

        List<AuthorId> authorIds = List.of(mock(AuthorId.class));
        List<GenreId> genreIds = List.of(mock(GenreId.class));
        ResponseBibliographicalDataDTO responseDto = new ResponseBibliographicalDataDTO("bdId", "Title", List.of("Author One"));

        when(bibliographicalData.id()).thenReturn(bdId);
        when(request.title()).thenReturn("Title");
        when(request.authorName()).thenReturn(List.of("Author One"));
        when(request.originalLanguage()).thenReturn("English");
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

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toLocale("English")
            ).thenReturn(originalLanguageDouble);

            when(service.createAndSave(
                    eq("Title"),
                    anyList(),
                    eq(originalLanguageDouble),
                    eq(LocalDate.of(2025, 1, 1)),
                    anyList(),
                    eq("Chunks of text"),
                    eq(userIdDouble)
            )).thenReturn(bibliographicalData);

            when(service.getAuthors(bibliographicalData)).thenReturn(List.of("Author One"));

            when(provider.getRegisteredUserLinksForBibliographicalData(userIdDouble)).thenReturn(List.of());

            // Act
            ResponseEntity<ResponseBibliographicalDataDTO> response = controller.createBibliographicalData(request, id);

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

        RequestBibliographicalDataDTO request = mock(RequestBibliographicalDataDTO.class);

        when(request.title()).thenReturn("Title");
        when(request.authorName()).thenReturn(List.of("Author One"));
        when(request.originalLanguage()).thenReturn("English");
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
        ResponseEntity<ResponseBibliographicalDataDTO> response = controller.createBibliographicalData(request, id);

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

        RequestBibliographicalDataDTO request = mock(RequestBibliographicalDataDTO.class);

        when(request.title()).thenReturn("Title");
        when(request.authorName()).thenReturn(List.of("Author One"));
        when(request.originalLanguage()).thenReturn("English");
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
        ResponseEntity<ResponseBibliographicalDataDTO> response = controller.createBibliographicalData(request, id);

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

        RequestBibliographicalDataDTO request = mock(RequestBibliographicalDataDTO.class);

        when(request.title()).thenReturn("Title");
        when(request.authorName()).thenReturn(List.of("Author One"));
        when(request.originalLanguage()).thenReturn("English");
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
        ResponseEntity<ResponseBibliographicalDataDTO> response = controller.createBibliographicalData(request, id);

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

        RequestBibliographicalDataDTO request = mock(RequestBibliographicalDataDTO.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        BibliographicalDataId bdId = new BibliographicalDataId("1234");

        List<AuthorId> authorIds = List.of(mock(AuthorId.class));
        List<GenreId> genreIds = List.of(mock(GenreId.class));
        List<String> authors = List.of("Author 1");

        Locale originalLanguageDouble = mock(Locale.class);

        ResponseBibliographicalDataDTO resultDto =
                new ResponseBibliographicalDataDTO("1234", "Title", List.of("Author 1", "Author 2"));

        Link customLink = Link.of("/bibliographical-data", "allBibliographicalData");

        when(request.authorName()).thenReturn(List.of("Author 1"));
        when(request.genreName()).thenReturn(List.of("Genre 1"));
        when(request.title()).thenReturn("Title");
        when(request.originalLanguage()).thenReturn("English");
        when(request.publicationDate()).thenReturn(LocalDate.of(2024, 1, 1));
        when(request.chunksOfText()).thenReturn(("Chunk 1"));

        when(service.createAndSave(
                eq("Title"),
                eq(authorIds),
                eq(originalLanguageDouble),
                eq(LocalDate.of(2024, 1, 1)),
                eq(genreIds),
                eq(("Chunk 1")),
                eq(userIdDouble)
        )).thenReturn(bibliographicalData);

        when(bibliographicalData.id()).thenReturn(bdId);
        when(service.getAuthors(bibliographicalData)).thenReturn(authors);
        when(provider.getRegisteredUserLinksForBibliographicalData(userIdDouble)).thenReturn(List.of(customLink));

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

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toLocale("English")
            ).thenReturn(originalLanguageDouble);

            // Act
            ResponseEntity<ResponseBibliographicalDataDTO> response =
                    controller.createBibliographicalData(request, id);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());

            ResponseBibliographicalDataDTO body = response.getBody();

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
        String bdId = "bdId1";
        BibliographicalDataId bdIdDouble = mock(BibliographicalDataId.class);

        BibliographicalData bdDouble = mock(BibliographicalData.class);

        Link extraLink = Link.of("/some-link", "someRel");

        ResponseBibliographicalDataDTO resultDto =
                new ResponseBibliographicalDataDTO("bdId1", "Title", List.of("Author 1"));

        when(service.findById(bdIdDouble)).thenReturn(bdDouble);
        when(service.getAuthors(bdDouble)).thenReturn(List.of("Author One"));
        when(provider.getPublicLinksForBibliographicalData()).thenReturn(List.of(extraLink));

        // Act
        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toBibliographicalDataId(bdId)
            ).thenReturn(bdIdDouble);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toDTO(bdDouble, List.of("Author One"))
            ).thenReturn(resultDto);

            ResponseEntity<ResponseBibliographicalDataDTO> response = controller.getBibliographicalDataById(bdId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());

            assertTrue(response.getBody().hasLink("self"));
            assertTrue(response.getBody().hasLink("someRel"));
        }
    }

    @Test
    void getBibliographicalDataById_whenDataDoesNotExist_returnsNotFound() throws Exception {
        // Arrange
        String bdId = "bdId1";

        BibliographicalDataId bdIdDouble = mock(BibliographicalDataId.class);

        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toBibliographicalDataId(bdId)
            ).thenReturn(bdIdDouble);

            when(service.findById(bdIdDouble))
                    .thenThrow(new InstanceNotFoundException("BibliographicalData was not found:"));

            // Act
            ResponseEntity<ResponseBibliographicalDataDTO> response =
                    controller.getBibliographicalDataById(bdId);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void getBibliographicalDataById_whenProviderReturnsNoLinks_stillAddsSelfLink() throws Exception {
        // Arrange
        String bdId = "bdId1";
        BibliographicalDataId bdIdDouble = mock(BibliographicalDataId.class);

        BibliographicalData bdDouble = mock(BibliographicalData.class);

        when(service.findById(bdIdDouble)).thenReturn(bdDouble);
        when(service.getAuthors(bdDouble)).thenReturn(List.of("Author One"));
        when(provider.getPublicLinksForBibliographicalData()).thenReturn(List.of());

        ResponseBibliographicalDataDTO resultDto =
                new ResponseBibliographicalDataDTO("bdId1", "Title", List.of("Author One"));

        // Act
        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toBibliographicalDataId(bdId)
            ).thenReturn(bdIdDouble);

            mockedMapper.when(() ->
                    BibliographicalDataMapper.toDTO(bdDouble, List.of("Author One"))
            ).thenReturn(resultDto);

            ResponseEntity<ResponseBibliographicalDataDTO> response = controller.getBibliographicalDataById(bdId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().hasLink("self"));
        }
    }

    @Test
    void getBibliographicalDataById_whenUnexpectedException_returnsInternalServerError() throws InstanceNotFoundException {
        // Arrange
        String id = "bdId1";
        BibliographicalDataId bdId = new BibliographicalDataId(id);

        when(service.findById(bdId)).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<ResponseBibliographicalDataDTO> response =
                controller.getBibliographicalDataById(id);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getBibliographicalDataById_whenNullId_returnsBadRequest() {
        // Act
        ResponseEntity<ResponseBibliographicalDataDTO> response =
                controller.getBibliographicalDataById(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ================================================================
    // ======================= Options Tests ==========================
    // ================================================================

    @Test
    void optionsBibliographicalData_shouldReturnPublicMethods_whenRequesterIsAnonymous() {
        // Act
        ResponseEntity<?> response = controller.optionsBibliographicalData(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(
                Set.of(HttpMethod.GET, HttpMethod.OPTIONS),
                response.getHeaders().getAllow()
        );
        verifyNoInteractions(service);
    }

    @Test
    void optionsBibliographicalData_shouldIncludePost_whenRequesterCanCreate()
            throws InstanceNotFoundException {
        // Arrange
        String requesterId = "user-1";
        UserId userId = mock(UserId.class);

        when(service.canCreate(userId)).thenReturn(true);

        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {
            mockedMapper.when(() -> BibliographicalDataMapper.toUserId(requesterId))
                    .thenReturn(userId);

            // Act
            ResponseEntity<?> response = controller.optionsBibliographicalData(requesterId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(
                    Set.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS),
                    response.getHeaders().getAllow()
            );
        }
    }

    @Test
    void optionsBibliographicalData_shouldNotIncludePost_whenRequesterCannotCreate()
            throws InstanceNotFoundException {
        // Arrange
        String requesterId = "user-1";
        UserId userId = mock(UserId.class);

        when(service.canCreate(userId)).thenReturn(false);

        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {
            mockedMapper.when(() -> BibliographicalDataMapper.toUserId(requesterId))
                    .thenReturn(userId);

            // Act
            ResponseEntity<?> response = controller.optionsBibliographicalData(requesterId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(
                    Set.of(HttpMethod.GET, HttpMethod.OPTIONS),
                    response.getHeaders().getAllow()
            );
        }
    }

    @Test
    void optionsBibliographicalData_shouldReturnBadRequest_whenRequesterIdIsInvalid() {
        // Arrange
        String requesterId = "";

        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {
            mockedMapper.when(() -> BibliographicalDataMapper.toUserId(requesterId))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<?> response = controller.optionsBibliographicalData(requesterId);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void optionsBibliographicalData_shouldReturnNotFound_whenRequesterDoesNotExist()
            throws InstanceNotFoundException {
        // Arrange
        String requesterId = "missing-user";
        UserId userId = mock(UserId.class);

        when(service.canCreate(userId)).thenThrow(new InstanceNotFoundException("User not found"));

        try (MockedStatic<BibliographicalDataMapper> mockedMapper =
                     mockStatic(BibliographicalDataMapper.class)) {
            mockedMapper.when(() -> BibliographicalDataMapper.toUserId(requesterId))
                    .thenReturn(userId);

            // Act
            ResponseEntity<?> response = controller.optionsBibliographicalData(requesterId);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}
