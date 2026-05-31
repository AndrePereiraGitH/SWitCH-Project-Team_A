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
 * <p>This class verifies the behaviour of the REST controller responsible
 * for managing PublicationSample resources, covering both successful operations
 * and error handling scenarios.</p>
 *
 * <p>The following scenarios are covered:</p>
 * <ul>
 *   <li>Successful publication sample creation returning HTTP 201 Created</li>
 *   <li>HATEOAS links are correctly added to the response body</li>
 *   <li>HTTP 403 Forbidden when the user lacks permission</li>
 *   <li>HTTP 400 Bad Request when invalid arguments are provided</li>
 *   <li>HTTP 409 Conflict when the publication sample already exists</li>
 *   <li>HTTP 500 Internal Server Error on unexpected failures</li>
 *   <li>Successful retrieval of a single publication sample by ID returning HTTP 200 OK</li>
 *   <li>HTTP 400 Bad Request when publication sample ID is not found</li>
 * </ul>
 *
 * <p>All dependencies are mocked using Mockito, isolating the controller
 * from the service, repository, and link provider layers.</p>
 */
@ExtendWith(MockitoExtension.class)
class PublicationSampleRESTControllerTest {

    @Mock
    private PublicationSampleService publicationSampleService;

    @Mock
    private GenreService genreService;

    @Mock
    private BibliographicalDataService bibliographicalDataService;

    @Mock
    private AuthorService authorService;

    @Mock
    private UserService userService;

    @Mock
    private PublicationSampleLinkProvider publicationSampleLinkProvider;

    @Mock
    private PublicationSample publicationSample;

    @Mock
    private PublicationSampleId publicationSampleId;

    @Mock
    private BibliographicalData bibliographicalData;

    @Mock
    private BibliographicalDataId bibliographicalDataId;

    @Mock
    private UserId userId;

    @Mock
    private User user;

    private RegisterPublicationSampleDTO dto;

    @BeforeEach
    void setup() {
        dto = new RegisterPublicationSampleDTO(
                "bd-001",
                "edition-001",
                "NEW"
        );
    }

    private void setupPublicationSampleMocks() {
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
    }

    // =========================================================================
    // createPublicationSample — success
    // =========================================================================

    @Test
    void createPublicationSample_shouldReturnCreated_whenSuccess() throws Exception {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);
        setupPublicationSampleMocks();

        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void createPublicationSample_shouldAddLinks_whenProviderReturnsLinks() throws Exception {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);
        setupPublicationSampleMocks();

        Link link = Link.of("/publicationSamples");
        List<Link> linkList = new ArrayList<>();
        linkList.add(link);

        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(linkList);

        // ACT
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // ASSERT
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().getLinks().toList().size());
    }

    // =========================================================================
    // createPublicationSample — exceptions
    // =========================================================================

    @Test
    void createPublicationSample_shouldReturnForbidden_whenAccessDenied() throws Exception {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);

        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenThrow(new AccessDeniedException("denied"));

        // ACT
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createPublicationSample_shouldReturnBadRequest_whenIllegalArgument() throws Exception {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);

        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createPublicationSample_shouldReturnConflict_whenInstanceAlreadyExists() throws Exception {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);

        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenThrow(new InstanceAlreadyExistsException());

        // ACT
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createPublicationSample_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);

        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    // =========================================================================
    // findById — success
    // =========================================================================

    @Test
    void findById_shouldReturnOk_whenPublicationSampleExists() throws InstanceNotFoundException {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);
        setupPublicationSampleMocks();

        when(publicationSampleService.findById(publicationSampleId)).thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // ACT
        ResponseEntity<PublicationDTO> result = sut.findById(publicationSampleId);

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void findById_shouldAddSelfAndFindByIdLinks() throws InstanceNotFoundException {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);
        setupPublicationSampleMocks();

        when(publicationSampleService.findById(publicationSampleId)).thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        // ACT
        ResponseEntity<PublicationDTO> result = sut.findById(publicationSampleId);

        // ASSERT
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().getLinks().toList().size());
        assertTrue(result.getBody().hasLink("self"));
        assertTrue(result.getBody().hasLink("find-by-id"));
    }

    // =========================================================================
// createPublicationSample — response body content
// =========================================================================

    @Test
    void createPublicationSample_shouldReturnCorrectTitle() throws Exception {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);
        setupPublicationSampleMocks();

        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // ASSERT
        assertNotNull(result.getBody());
        assertEquals("The Gospel According to Jesus Christ", result.getBody().getTitle());
    }

    @Test
    void createPublicationSample_shouldReturnCorrectOwner() throws Exception {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);
        setupPublicationSampleMocks();

        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // ASSERT
        assertNotNull(result.getBody());
        assertEquals("jose.saramago", result.getBody().getOwnerName());
    }

    @Test
    void createPublicationSample_shouldReturnCorrectAuthors() throws Exception {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);

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

        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // ASSERT
        assertNotNull(result.getBody());
        assertEquals(List.of("José Saramago"), result.getBody().getAuthors());
    }

    @Test
    void createPublicationSample_shouldReturnCorrectGenres() throws Exception {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);

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

        when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, userId);

        // ASSERT
        assertNotNull(result.getBody());
        assertEquals(List.of("FICTION"), result.getBody().getGenres());
    }

    // =========================================================================
    // findById — exceptions
    // =========================================================================

    @Test
    void findById_shouldReturnBadRequest_whenIllegalArgument() {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);

        when(publicationSampleService.findById(publicationSampleId))
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<PublicationDTO> result = sut.findById(publicationSampleId);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void findById_shouldReturnInternalServerError_whenUnexpectedException() {

        // ARRANGE
        PublicationSampleRESTController sut = new PublicationSampleRESTController(
                userService, publicationSampleService, genreService,
                bibliographicalDataService, authorService, publicationSampleLinkProvider);

        when(publicationSampleService.findById(publicationSampleId))
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<PublicationDTO> result = sut.findById(publicationSampleId);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }
}