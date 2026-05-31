package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublicationSampleLinkProvider;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.publicationSample.request.RegisterPublicationSampleDTO;
import TOPSECRET.dto.publicationSample.response.PublicationDTO;
import TOPSECRET.services.*;
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

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublicationSampleRESTController}.
 *
 * <p>Validates HTTP responses, exception handling, DTO content, and HATEOAS link
 * generation across all controller endpoints.</p>
 *
 * <p>Tests are organized into the following sections:</p>
 * <ul>
 *   <li><b>createPublicationSample — success</b> — HTTP 201, body content, links.</li>
 *   <li><b>createPublicationSample — exceptions</b> — 403, 400, 409, 500.</li>
 *   <li><b>findById — success</b> — HTTP 200, body content, self link.</li>
 *   <li><b>findById — exceptions</b> — 400, 500.</li>
 *   <li><b>findAll — success</b> — HTTP 200, list content.</li>
 *   <li><b>findAll — exceptions</b> — 400, 500.</li>
 *   <li><b>findByBibliographicalDataId — success</b> — HTTP 200, filtered list.</li>
 *   <li><b>findByBibliographicalDataId — exceptions</b> — 400, 500.</li>
 *   <li><b>findByBibliographicalDataIdIn — success</b> — HTTP 200, filtered list.</li>
 *   <li><b>findByBibliographicalDataIdIn — exceptions</b> — 400, 500.</li>
 *   <li><b>findByEditionIdIn — success</b> — HTTP 200, filtered list.</li>
 *   <li><b>findByEditionIdIn — exceptions</b> — 400, 500.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class PublicationSampleRESTControllerTest {

    @Mock private PublicationSampleService publicationSampleService;
    @Mock private GenreService genreService;
    @Mock private BibliographicalDataService bibliographicalDataService;
    @Mock private AuthorService authorService;
    @Mock private UserService userService;
    @Mock private PublicationSampleLinkProvider publicationSampleLinkProvider;

    @Mock private PublicationSample publicationSample;
    @Mock private PublicationSampleId publicationSampleId;
    @Mock private BibliographicalData bibliographicalData;
    @Mock private BibliographicalDataId bibliographicalDataId;
    @Mock private UserId userId;
    @Mock private User user;

    @InjectMocks
    private PublicationSampleRESTController sut;

    private RegisterPublicationSampleDTO dto;

    @BeforeEach
    void setup() {
        dto = new RegisterPublicationSampleDTO("bd-001", "edition-001", "NEW");
    }

    private void setupPublicationSampleMocks() throws InstanceNotFoundException {
        when(publicationSampleId.toString()).thenReturn("ps-001");
        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getCondition()).thenReturn(Condition.NEW);
        when(publicationSample.getOwnerID()).thenReturn(userId);
        when(bibliographicalData.getTitle()).thenReturn("The Gospel According to Jesus Christ");
        when(bibliographicalData.getAuthors()).thenReturn(List.of());
        when(bibliographicalData.getGenres()).thenReturn(List.of());
        when(bibliographicalData.getOriginalLanguage()).thenReturn(Locale.ENGLISH);
        when(userService.findByID(any())).thenReturn(user);
        when(user.getUserName()).thenReturn("jose.saramago");
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any()))
                .thenReturn(new ArrayList<>());
    }

    // =========================================================================
    // createPublicationSample — success
    // =========================================================================

    @Test
    void createPublicationSample_shouldReturnCreated_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void createPublicationSample_shouldAddSelfLink() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // Assert
        assertNotNull(result.getBody());
        assertTrue(result.getBody().hasLink("self"));
    }

    @Test
    void createPublicationSample_shouldAddNavigationLinks_whenProviderReturnsLinks() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        Link navLink = Link.of("/publicationSamples").withRel("create-publication-sample");
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any()))
                .thenReturn(new ArrayList<>(List.of(navLink)));
        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // Assert
        assertNotNull(result.getBody());
        assertTrue(result.getBody().hasLink("create-publication-sample"));
        assertTrue(result.getBody().hasLink("self"));
    }

    @Test
    void createPublicationSample_shouldReturnCorrectTitle() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // Assert
        assertNotNull(result.getBody());
        assertEquals("The Gospel According to Jesus Christ", result.getBody().getTitle());
    }

    @Test
    void createPublicationSample_shouldReturnCorrectOwner() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // Assert
        assertNotNull(result.getBody());
        assertEquals("jose.saramago", result.getBody().getOwnerName());
    }

    @Test
    void createPublicationSample_shouldReturnCorrectAuthors() throws Exception {

        // Arrange
        AuthorId authorId = mock(AuthorId.class);
        Author author = mock(Author.class);
        when(author.getName()).thenReturn("José Saramago");
        when(authorService.findById(authorId)).thenReturn(author);
        when(bibliographicalData.getAuthors()).thenReturn(List.of(authorId));
        when(bibliographicalData.getGenres()).thenReturn(List.of());
        when(publicationSampleId.toString()).thenReturn("ps-001");
        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getCondition()).thenReturn(Condition.NEW);
        when(publicationSample.getOwnerID()).thenReturn(userId);
        when(bibliographicalData.getTitle()).thenReturn("The Gospel According to Jesus Christ");
        when(bibliographicalData.getOriginalLanguage()).thenReturn(Locale.ENGLISH);
        when(userService.findByID(any())).thenReturn(user);
        when(user.getUserName()).thenReturn("jose.saramago");
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any()))
                .thenReturn(new ArrayList<>());
        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(List.of("José Saramago"), result.getBody().getAuthors());
    }

    @Test
    void createPublicationSample_shouldReturnCorrectGenres() throws Exception {

        // Arrange
        GenreId genreId = mock(GenreId.class);
        Genre genre = mock(Genre.class);
        when(genre.getGenreType()).thenReturn("FICTION");
        when(genreService.findById(genreId)).thenReturn(genre);
        when(bibliographicalData.getGenres()).thenReturn(List.of(genreId));
        when(bibliographicalData.getAuthors()).thenReturn(List.of());
        when(publicationSampleId.toString()).thenReturn("ps-001");
        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getCondition()).thenReturn(Condition.NEW);
        when(publicationSample.getOwnerID()).thenReturn(userId);
        when(bibliographicalData.getTitle()).thenReturn("The Gospel According to Jesus Christ");
        when(bibliographicalData.getOriginalLanguage()).thenReturn(Locale.ENGLISH);
        when(userService.findByID(any())).thenReturn(user);
        when(user.getUserName()).thenReturn("jose.saramago");
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any()))
                .thenReturn(new ArrayList<>());
        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(List.of("FICTION"), result.getBody().getGenres());
    }

    // =========================================================================
    // createPublicationSample — exceptions
    // =========================================================================

    @Test
    void createPublicationSample_shouldReturnForbidden_whenAccessDenied() throws Exception {

        // Arrange
        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenThrow(new AccessDeniedException("denied"));

        // Act
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createPublicationSample_shouldReturnBadRequest_whenIllegalArgument() throws Exception {

        // Arrange
        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createPublicationSample_shouldReturnConflict_whenInstanceAlreadyExists() throws Exception {

        // Arrange
        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenThrow(new InstanceAlreadyExistsException());

        // Act
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // Assert
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createPublicationSample_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {

        // Arrange
        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenThrow(new RuntimeException());

        // Act
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    // =========================================================================
    // findById — success
    // =========================================================================

    @Test
    void findById_shouldReturnOk_whenPublicationSampleExists() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        when(publicationSampleService.findById(publicationSampleId)).thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<PublicationDTO> result = sut.findById(publicationSampleId, userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void findById_shouldAddSelfLink() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        when(publicationSampleService.findById(publicationSampleId)).thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<PublicationDTO> result = sut.findById(publicationSampleId, userId);

        // Assert
        assertNotNull(result.getBody());
        assertTrue(result.getBody().hasLink("self"));
    }

    @Test
    void findById_shouldAddNavigationLinks_whenProviderReturnsLinks() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        Link navLink = Link.of("/publicationSamples").withRel("create-publication-sample");
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any()))
                .thenReturn(new ArrayList<>(List.of(navLink)));
        when(publicationSampleService.findById(publicationSampleId)).thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<PublicationDTO> result = sut.findById(publicationSampleId, userId);

        // Assert
        assertNotNull(result.getBody());
        assertTrue(result.getBody().hasLink("create-publication-sample"));
        assertTrue(result.getBody().hasLink("self"));
    }

    // =========================================================================
    // findById — exceptions
    // =========================================================================

    @Test
    void findById_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        when(publicationSampleService.findById(publicationSampleId))
                .thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<PublicationDTO> result = sut.findById(publicationSampleId, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void findById_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        when(publicationSampleService.findById(publicationSampleId))
                .thenThrow(new RuntimeException());

        // Act
        ResponseEntity<PublicationDTO> result = sut.findById(publicationSampleId, userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    // =========================================================================
    // findAll — success
    // =========================================================================

    @Test
    void findAll_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        when(publicationSampleService.findAll()).thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<List<PublicationDTO>> result = sut.findAll(userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoSamplesExist() throws Exception {

        // Arrange
        when(publicationSampleService.findAll()).thenReturn(List.of());

        // Act
        ResponseEntity<List<PublicationDTO>> result = sut.findAll(userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void findAll_shouldAddSelfLinkToEachItem() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        when(publicationSampleService.findAll()).thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<List<PublicationDTO>> result = sut.findAll(userId);

        // Assert
        assertNotNull(result.getBody());
        assertTrue(result.getBody().get(0).hasLink("self"));
    }

    // =========================================================================
    // findAll — exceptions
    // =========================================================================

    @Test
    void findAll_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        when(publicationSampleService.findAll()).thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<List<PublicationDTO>> result = sut.findAll(userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void findAll_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        when(publicationSampleService.findAll()).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<List<PublicationDTO>> result = sut.findAll(userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    // =========================================================================
    // findByBibliographicalDataId — success
    // =========================================================================

    @Test
    void findByBibliographicalDataId_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                .thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<List<PublicationDTO>> result =
                sut.findByBibliographicalDataId(bibliographicalDataId, userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void findByBibliographicalDataId_shouldReturnEmptyList_whenNoMatchExists() throws Exception {

        // Arrange
        when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                .thenReturn(List.of());

        // Act
        ResponseEntity<List<PublicationDTO>> result =
                sut.findByBibliographicalDataId(bibliographicalDataId, userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void findByBibliographicalDataId_shouldAddSelfLinkToEachItem() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                .thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<List<PublicationDTO>> result =
                sut.findByBibliographicalDataId(bibliographicalDataId, userId);

        // Assert
        assertNotNull(result.getBody());
        assertTrue(result.getBody().get(0).hasLink("self"));
    }

    // =========================================================================
    // findByBibliographicalDataId — exceptions
    // =========================================================================

    @Test
    void findByBibliographicalDataId_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                .thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<List<PublicationDTO>> result =
                sut.findByBibliographicalDataId(bibliographicalDataId, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void findByBibliographicalDataId_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                .thenThrow(new RuntimeException());

        // Act
        ResponseEntity<List<PublicationDTO>> result =
                sut.findByBibliographicalDataId(bibliographicalDataId, userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    // =========================================================================
    // findByBibliographicalDataIdIn — success
    // =========================================================================

    @Test
    void findByBibliographicalDataIdIn_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                .thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<List<PublicationDTO>> result =
                sut.findByBibliographicalDataIdIn(bdIds, userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnEmptyList_whenNoMatchExists() throws Exception {

        // Arrange
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                .thenReturn(List.of());

        // Act
        ResponseEntity<List<PublicationDTO>> result =
                sut.findByBibliographicalDataIdIn(bdIds, userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void findByBibliographicalDataIdIn_shouldAddSelfLinkToEachItem() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                .thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<List<PublicationDTO>> result =
                sut.findByBibliographicalDataIdIn(bdIds, userId);

        // Assert
        assertNotNull(result.getBody());
        assertTrue(result.getBody().get(0).hasLink("self"));
    }

    // =========================================================================
    // findByBibliographicalDataIdIn — exceptions
    // =========================================================================

    @Test
    void findByBibliographicalDataIdIn_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                .thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<List<PublicationDTO>> result =
                sut.findByBibliographicalDataIdIn(bdIds, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                .thenThrow(new RuntimeException());

        // Act
        ResponseEntity<List<PublicationDTO>> result =
                sut.findByBibliographicalDataIdIn(bdIds, userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    // =========================================================================
    // findByEditionIdIn — success
    // =========================================================================

    @Test
    void findByEditionIdIn_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        EditionId editionId = mock(EditionId.class);
        List<EditionId> editionIds = List.of(editionId);
        when(publicationSampleService.findByEditionIdIn(editionIds))
                .thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<List<PublicationDTO>> result = sut.findByEditionIdIn(editionIds, userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void findByEditionIdIn_shouldReturnEmptyList_whenNoMatchExists() throws Exception {

        // Arrange
        EditionId editionId = mock(EditionId.class);
        List<EditionId> editionIds = List.of(editionId);
        when(publicationSampleService.findByEditionIdIn(editionIds)).thenReturn(List.of());

        // Act
        ResponseEntity<List<PublicationDTO>> result = sut.findByEditionIdIn(editionIds, userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void findByEditionIdIn_shouldAddSelfLinkToEachItem() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        EditionId editionId = mock(EditionId.class);
        List<EditionId> editionIds = List.of(editionId);
        when(publicationSampleService.findByEditionIdIn(editionIds))
                .thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // Act
        ResponseEntity<List<PublicationDTO>> result = sut.findByEditionIdIn(editionIds, userId);

        // Assert
        assertNotNull(result.getBody());
        assertTrue(result.getBody().get(0).hasLink("self"));
    }

    // =========================================================================
    // findByEditionIdIn — exceptions
    // =========================================================================

    @Test
    void findByEditionIdIn_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        EditionId editionId = mock(EditionId.class);
        List<EditionId> editionIds = List.of(editionId);
        when(publicationSampleService.findByEditionIdIn(editionIds))
                .thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<List<PublicationDTO>> result = sut.findByEditionIdIn(editionIds, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void findByEditionIdIn_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        EditionId editionId = mock(EditionId.class);
        List<EditionId> editionIds = List.of(editionId);
        when(publicationSampleService.findByEditionIdIn(editionIds))
                .thenThrow(new RuntimeException());

        // Act
        ResponseEntity<List<PublicationDTO>> result = sut.findByEditionIdIn(editionIds, userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }
}