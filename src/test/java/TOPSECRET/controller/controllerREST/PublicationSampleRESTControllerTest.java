package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublicationSampleLinkProvider;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.publicationSample.PublicationSampleMapper;
import TOPSECRET.dto.publicationSample.request.RegisterPublicationSampleDTO;
import TOPSECRET.dto.publicationSample.response.PublicationDTO;
import TOPSECRET.services.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
 */
@MockitoSettings(strictness = Strictness.LENIENT)
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

    @Mock private EditionService editionService;
    @Mock private EditionId editionId;
    @Mock private Edition edition;

    @Mock private GenreId genreId;
    @Mock private AuthorId authorId;
    @Mock private Genre genre;
    @Mock private Author author;

    @InjectMocks
    private PublicationSampleRESTController sut;

    private static final String USER_ID_STRING = "user-001";
    private static final String BD_ID_STRING = "bd-001";
    private static final String EDITION_ID_STRING = "edition-001";
    private static final String COLLECTION_ID_STRING = "collection-001";
    private static final String PS_ID_STRING = "ps-001";

    private RegisterPublicationSampleDTO dto;

    @BeforeEach
    void setup() {
        dto = new RegisterPublicationSampleDTO("bd-001", "edition-001", "NEW");
    }

    private void setupPublicationSampleMocks() throws InstanceNotFoundException {
        when(publicationSampleId.toString()).thenReturn("ps-001");
        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getEditionID()).thenReturn(editionId);
        when(publicationSample.getCondition()).thenReturn(Condition.NEW);
        when(publicationSample.getOwnerID()).thenReturn(userId);
        when(bibliographicalData.getOriginalLanguage()).thenReturn(Locale.ENGLISH);
        when(userService.findByID(any())).thenReturn(user);
        when(editionService.findById(any())).thenReturn(edition);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any()))
                .thenReturn(new ArrayList<>());

        when(bibliographicalData.getTitle()).thenReturn("The Gospel According to Jesus Christ");
        when(bibliographicalData.getAuthors()).thenReturn(List.of());
        when(bibliographicalData.getGenres()).thenReturn(List.of());
        when(user.getUserName()).thenReturn("jose.saramago");
    }

    // =========================================================================
    // createPublicationSample — success
    // =========================================================================

    @Test
    void createPublicationSample_shouldReturnCreated_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBdId(any())).thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toEditionId(any())).thenReturn(mock(EditionId.class));
            mapper.when(() -> PublicationSampleMapper.toCondition(any())).thenReturn(Condition.NEW);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                    .thenReturn(publicationSample);
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.CREATED, result.getStatusCode());
            assertNotNull(result.getBody());
        }
    }

    @Test
    void createPublicationSample_shouldAddSelfLink() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBdId(any())).thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toEditionId(any())).thenReturn(mock(EditionId.class));
            mapper.when(() -> PublicationSampleMapper.toCondition(any())).thenReturn(Condition.NEW);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                    .thenReturn(publicationSample);
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().hasLink("self"));
        }
    }

    @Test
    void createPublicationSample_shouldAddNavigationLinks_whenProviderReturnsLinks() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        Link navLink = Link.of("/publicationSamples").withRel("create-publication-sample");
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any()))
                .thenReturn(new ArrayList<>(List.of(navLink)));
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBdId(any())).thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toEditionId(any())).thenReturn(mock(EditionId.class));
            mapper.when(() -> PublicationSampleMapper.toCondition(any())).thenReturn(Condition.NEW);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                    .thenReturn(publicationSample);
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().hasLink("create-publication-sample"));
            assertTrue(result.getBody().hasLink("self"));
        }
    }

    // =========================================================================
    // createPublicationSample — exceptions
    // =========================================================================

    @Test
    void createPublicationSample_shouldReturnNotFound_whenEntityNotFound() throws Exception {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBdId(any())).thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toEditionId(any())).thenReturn(mock(EditionId.class));
            mapper.when(() -> PublicationSampleMapper.toCondition(any())).thenReturn(Condition.NEW);
            when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void createPublicationSample_shouldReturnForbidden_whenAccessDenied() throws Exception {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBdId(any())).thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toEditionId(any())).thenReturn(mock(EditionId.class));
            mapper.when(() -> PublicationSampleMapper.toCondition(any())).thenReturn(Condition.NEW);
            when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new AccessDeniedException("denied"));

            // Act
            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void createPublicationSample_shouldReturnBadRequest_whenIllegalArgument() throws Exception {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBdId(any())).thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toEditionId(any())).thenReturn(mock(EditionId.class));
            mapper.when(() -> PublicationSampleMapper.toCondition(any())).thenReturn(Condition.NEW);
            when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void createPublicationSample_shouldReturnConflict_whenInstanceAlreadyExists() throws Exception {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBdId(any())).thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toEditionId(any())).thenReturn(mock(EditionId.class));
            mapper.when(() -> PublicationSampleMapper.toCondition(any())).thenReturn(Condition.NEW);
            when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new InstanceAlreadyExistsException());

            // Act
            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void createPublicationSample_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBdId(any())).thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toEditionId(any())).thenReturn(mock(EditionId.class));
            mapper.when(() -> PublicationSampleMapper.toCondition(any())).thenReturn(Condition.NEW);
            when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findById — success
    // =========================================================================

    @Test
    void findById_shouldReturnOk_whenPublicationSampleExists() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);

            // FIXED: Mock the static mapper to return the expected domain object
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);

            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findById(publicationSampleId)).thenReturn(publicationSample);
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            // FIXED: Passing PS_ID_STRING instead of the mock object
            ResponseEntity<PublicationDTO> result = sut.findById(PS_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
        }
    }

    @Test
    void findById_shouldAddSelfLink() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.findById(publicationSampleId)).thenReturn(publicationSample);
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<PublicationDTO> result = sut.findById(PS_ID_STRING, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().hasLink("self"));
        }
    }

    // =========================================================================
    // findById — exceptions
    // =========================================================================

    @Test
    void findById_shouldReturnNotFound_whenEntityNotFound() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.findById(publicationSampleId))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<PublicationDTO> result = sut.findById(PS_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findById_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.findById(publicationSampleId))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<PublicationDTO> result = sut.findById(PS_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findById_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.findById(publicationSampleId))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<PublicationDTO> result = sut.findById(PS_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }


    // =========================================================================
    // findAll — success
    // =========================================================================

    @Test
    void findAll_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findAll()).thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result = sut.findAll(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoSamplesExist() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findAll()).thenReturn(List.of());

            // Act
            ResponseEntity<List<PublicationDTO>> result = sut.findAll(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findAll_shouldAddSelfLinkToEachItem() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.findAll()).thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result = sut.findAll(USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().get(0).hasLink("self"));
        }
    }

    @Test
    void findAll_shouldReturnNotFound_whenEntityNotFound() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findAll())
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<List<PublicationDTO>> result = sut.findAll(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // getAuthors and getGenres — mutation coverage
    // =========================================================================

    @Test
    void findAll_shouldReturnCorrectAuthors() throws Exception {

        // Arrange
        when(publicationSampleId.toString()).thenReturn("ps-001");
        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getEditionID()).thenReturn(editionId);
        when(editionService.findById(any())).thenReturn(edition);
        when(publicationSample.getOwnerID()).thenReturn(userId);
        when(bibliographicalData.getAuthors()).thenReturn(List.of(authorId));
        when(bibliographicalData.getGenres()).thenReturn(List.of());
        when(authorService.findById(authorId)).thenReturn(author);
        when(author.getName()).thenReturn("José Saramago");
        when(userService.findByID(any())).thenReturn(user);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(new ArrayList<>());
        when(publicationSampleService.findAll()).thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenAnswer(invocation -> {
                        List<Genre> genres = invocation.getArgument(4);
                        List<Author> authors = invocation.getArgument(3);
                        return new PublicationDTO(
                                "ps-001",
                                "The Gospel According to Jesus Christ",
                                authors.stream().map(Author::getName).toList(),
                                "en",
                                genres.stream().map(Genre::getGenreType).toList(),
                                "jose.saramago",
                                null,
                                "NEW",
                                null,
                                null);
                    });

            // Act
            ResponseEntity<List<PublicationDTO>> result = sut.findAll(USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertEquals(List.of("José Saramago"), result.getBody().get(0).getAuthors());
        }
    }

    @Test
    void findAll_shouldReturnCorrectGenres() throws Exception {

        // Arrange
        when(publicationSampleId.toString()).thenReturn("ps-001");
        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getEditionID()).thenReturn(editionId);
        when(editionService.findById(any())).thenReturn(edition);
        when(publicationSample.getOwnerID()).thenReturn(userId);
        when(bibliographicalData.getGenres()).thenReturn(List.of(genreId));
        when(bibliographicalData.getAuthors()).thenReturn(List.of());
        when(genreService.findById(genreId)).thenReturn(genre);
        when(genre.getGenreType()).thenReturn("Fiction");
        when(userService.findByID(any())).thenReturn(user);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(new ArrayList<>());
        when(publicationSampleService.findAll()).thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenAnswer(invocation -> {
                        List<Genre> genres = invocation.getArgument(4);
                        List<Author> authors = invocation.getArgument(3);
                        return new PublicationDTO(
                                "ps-001",
                                "The Gospel According to Jesus Christ",
                                authors.stream().map(Author::getName).toList(),
                                "en",
                                genres.stream().map(Genre::getGenreType).toList(),
                                "jose.saramago",
                                null,
                                "NEW",
                                null,
                                null
                        );
                    });

            // Act
            ResponseEntity<List<PublicationDTO>> result = sut.findAll(USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertEquals(List.of("Fiction"), result.getBody().get(0).getGenres());
        }
    }

    // =========================================================================
    // findAll — exceptions
    // =========================================================================

    @Test
    void findAll_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findAll()).thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<PublicationDTO>> result = sut.findAll(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findAll_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findAll()).thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<PublicationDTO>> result = sut.findAll(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findByBibliographicalDataId — success
    // =========================================================================

    @Test
    void findByBibliographicalDataId_shouldReturnCorrectAuthors() throws Exception {

        // Arrange
        when(publicationSampleId.toString()).thenReturn("ps-001");
        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getEditionID()).thenReturn(editionId);
        when(editionService.findById(any())).thenReturn(edition);
        when(publicationSample.getOwnerID()).thenReturn(userId);
        when(bibliographicalData.getAuthors()).thenReturn(List.of(authorId));
        when(bibliographicalData.getGenres()).thenReturn(List.of());
        when(authorService.findById(authorId)).thenReturn(author);
        when(author.getName()).thenReturn("José Saramago");
        when(userService.findByID(any())).thenReturn(user);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(new ArrayList<>());
        when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                .thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenAnswer(invocation -> {
                        List<Genre> genres = invocation.getArgument(4);
                        List<Author> authors = invocation.getArgument(3);
                        return new PublicationDTO(
                                "ps-001",
                                "The Gospel According to Jesus Christ",
                                authors.stream().map(Author::getName).toList(),
                                "en",
                                genres.stream().map(Genre::getGenreType).toList(),
                                "jose.saramago",
                                null,
                                "NEW",
                                null,
                                null
                        );
                    });

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertEquals(List.of("José Saramago"), result.getBody().get(0).getAuthors());
        }
    }

    @Test
    void findByBibliographicalDataId_shouldReturnCorrectGenres() throws Exception {

        // Arrange
        when(publicationSampleId.toString()).thenReturn("ps-001");
        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getEditionID()).thenReturn(editionId);
        when(editionService.findById(any())).thenReturn(edition);
        when(publicationSample.getOwnerID()).thenReturn(userId);
        when(bibliographicalData.getGenres()).thenReturn(List.of(genreId));
        when(bibliographicalData.getAuthors()).thenReturn(List.of());
        when(genreService.findById(genreId)).thenReturn(genre);
        when(genre.getGenreType()).thenReturn("Fiction");
        when(userService.findByID(any())).thenReturn(user);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(new ArrayList<>());
        when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                .thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenAnswer(invocation -> {
                        List<Genre> genres = invocation.getArgument(4);
                        List<Author> authors = invocation.getArgument(3);
                        return new PublicationDTO(
                                "ps-001",
                                "The Gospel According to Jesus Christ",
                                authors.stream().map(Author::getName).toList(),
                                "en",
                                genres.stream().map(Genre::getGenreType).toList(),
                                "jose.saramago",
                                null,
                                "NEW",
                                null,
                                null
                        );
                    });

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertEquals(List.of("Fiction"), result.getBody().get(0).getGenres());
        }
    }

    @Test
    void findByBibliographicalDataId_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findByBibliographicalDataId_shouldReturnEmptyList_whenNoMatchExists() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                    .thenReturn(List.of());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findByBibliographicalDataId_shouldAddSelfLinkToEachItem() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().get(0).hasLink("self"));
        }
    }

    // =========================================================================
    // findByBibliographicalDataId — exceptions
    // =========================================================================

    @Test
    void findByBibliographicalDataId_shouldReturnNotFound_whenEntityNotFound() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByBibliographicalDataId_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByBibliographicalDataId_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findByBibliographicalDataIdIn — success
    // =========================================================================

    @Test
    void findByBibliographicalDataIdIn_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        List<String> bdIdStrings = List.of(BD_ID_STRING);
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataIds(any())).thenReturn(bdIds);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataIdIn(bdIdStrings, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnEmptyList_whenNoMatchExists() {

        // Arrange
        List<String> bdIdStrings = List.of(BD_ID_STRING);
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataIds(any())).thenReturn(bdIds);
            when(publicationSampleService.findByBibliographicalDataIdIn(bdIds)).thenReturn(List.of());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataIdIn(bdIdStrings, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findByBibliographicalDataIdIn_shouldAddSelfLinkToEachItem() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        List<String> bdIdStrings = List.of(BD_ID_STRING);
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataIds(any())).thenReturn(bdIds);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataIdIn(bdIdStrings, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().get(0).hasLink("self"));
        }
    }

    // =========================================================================
    // findByBibliographicalDataIdIn — exceptions
    // =========================================================================

    @Test
    void findByBibliographicalDataIdIn_shouldReturnNotFound_whenEntityNotFound() {

        // Arrange
        List<String> bdIdStrings = List.of(BD_ID_STRING);
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataIds(any())).thenReturn(bdIds);
            when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataIdIn(bdIdStrings, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        List<String> bdIdStrings = List.of(BD_ID_STRING);
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataIds(any())).thenReturn(bdIds);
            when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataIdIn(bdIdStrings, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        List<String> bdIdStrings = List.of(BD_ID_STRING);
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataIds(any())).thenReturn(bdIds);
            when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataIdIn(bdIdStrings, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findByEditionIdIn — success
    // =========================================================================

    @Test
    void findByEditionIdIn_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        EditionId editionId = mock(EditionId.class);
        List<String> editionIdStrings = List.of(EDITION_ID_STRING);
        List<EditionId> editionIds = List.of(editionId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toEditionIds(any())).thenReturn(editionIds);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findByEditionIdIn(editionIds))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByEditionIdIn(editionIdStrings, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findByEditionIdIn_shouldReturnEmptyList_whenNoMatchExists() {

        // Arrange
        EditionId editionId = mock(EditionId.class);
        List<String> editionIdStrings = List.of(EDITION_ID_STRING);
        List<EditionId> editionIds = List.of(editionId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toEditionIds(any())).thenReturn(editionIds);
            when(publicationSampleService.findByEditionIdIn(editionIds)).thenReturn(List.of());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByEditionIdIn(editionIdStrings, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findByEditionIdIn_shouldAddSelfLinkToEachItem() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        EditionId editionId = mock(EditionId.class);
        List<String> editionIdStrings = List.of(EDITION_ID_STRING);
        List<EditionId> editionIds = List.of(editionId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toEditionIds(any())).thenReturn(editionIds);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.findByEditionIdIn(editionIds))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByEditionIdIn(editionIdStrings, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().get(0).hasLink("self"));
        }
    }

    // =========================================================================
    // findByEditionIdIn — exceptions
    // =========================================================================

    @Test
    void findByEditionIdIn_shouldReturnNotFound_whenEntityNotFound() {

        // Arrange
        EditionId editionId = mock(EditionId.class);
        List<String> editionIdStrings = List.of(EDITION_ID_STRING);
        List<EditionId> editionIds = List.of(editionId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toEditionIds(any())).thenReturn(editionIds);
            when(publicationSampleService.findByEditionIdIn(editionIds))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByEditionIdIn(editionIdStrings, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByEditionIdIn_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        EditionId editionId = mock(EditionId.class);
        List<String> editionIdStrings = List.of(EDITION_ID_STRING);
        List<EditionId> editionIds = List.of(editionId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toEditionIds(any())).thenReturn(editionIds);
            when(publicationSampleService.findByEditionIdIn(editionIds))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByEditionIdIn(editionIdStrings, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByEditionIdIn_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        EditionId editionId = mock(EditionId.class);
        List<String> editionIdStrings = List.of(EDITION_ID_STRING);
        List<EditionId> editionIds = List.of(editionId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toEditionIds(any())).thenReturn(editionIds);
            when(publicationSampleService.findByEditionIdIn(editionIds))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByEditionIdIn(editionIdStrings, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findByCollectionOfPublicationsId — success
    // =========================================================================

    @Test
    void findByCollectionOfPublicationsId_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findByCollectionOfPublicationsId(collectionId, userId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findByCollectionOfPublicationsId_shouldReturnEmptyList_whenNoMatchExists() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findByCollectionOfPublicationsId(collectionId, userId))
                    .thenReturn(List.of());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findByCollectionOfPublicationsId_shouldAddSelfLinkToEachItem() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.findByCollectionOfPublicationsId(collectionId, userId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().get(0).hasLink("self"));
        }
    }

    // =========================================================================
    // findByCollectionOfPublicationsId — exceptions
    // =========================================================================

    @Test
    void findByCollectionOfPublicationsId_shouldReturnNotFound_whenEntityNotFound() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findByCollectionOfPublicationsId(collectionId, userId))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByCollectionOfPublicationsId_shouldReturnForbidden_whenAccessDenied() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findByCollectionOfPublicationsId(collectionId, userId))
                    .thenThrow(new AccessDeniedException("denied"));

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByCollectionOfPublicationsId_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findByCollectionOfPublicationsId(collectionId, userId))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByCollectionOfPublicationsId_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findByCollectionOfPublicationsId(collectionId, userId))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findByUserIdAndLibraryIdIsNotNull — success
    // =========================================================================

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findByUserIdAndLibraryIdIsNotNull(userId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNotNull(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldReturnEmptyList_whenNoMatchExists() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByUserIdAndLibraryIdIsNotNull(userId))
                    .thenReturn(List.of());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNotNull(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldAddSelfLinkToEachItem() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.findByUserIdAndLibraryIdIsNotNull(userId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNotNull(USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().get(0).hasLink("self"));
        }
    }

    // =========================================================================
    // findByUserIdAndLibraryIdIsNotNull — exceptions
    // =========================================================================

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldReturnNotFound_whenEntityNotFound() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByUserIdAndLibraryIdIsNotNull(userId))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNotNull(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldReturnForbidden_whenAccessDenied() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByUserIdAndLibraryIdIsNotNull(userId))
                    .thenThrow(new AccessDeniedException("denied"));

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNotNull(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByUserIdAndLibraryIdIsNotNull(userId))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNotNull(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByUserIdAndLibraryIdIsNotNull_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByUserIdAndLibraryIdIsNotNull(userId))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNotNull(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findByUserIdAndLibraryIdIsNull — success
    // =========================================================================

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findByUserIdAndLibraryIdIsNull(userId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNull(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldReturnEmptyList_whenNoMatchExists() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByUserIdAndLibraryIdIsNull(userId))
                    .thenReturn(List.of());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNull(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldAddSelfLinkToEachItem() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.findByUserIdAndLibraryIdIsNull(userId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNull(USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().get(0).hasLink("self"));
        }
    }

    // =========================================================================
    // findByUserIdAndLibraryIdIsNull — exceptions
    // =========================================================================

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldReturnNotFound_whenEntityNotFound() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByUserIdAndLibraryIdIsNull(userId))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNull(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldReturnForbidden_whenAccessDenied() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByUserIdAndLibraryIdIsNull(userId))
                    .thenThrow(new AccessDeniedException("denied"));

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNull(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByUserIdAndLibraryIdIsNull(userId))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNull(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByUserIdAndLibraryIdIsNull_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByUserIdAndLibraryIdIsNull(userId))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByUserIdAndLibraryIdIsNull(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findByLibraryIdAndPublicationStatusNotIn — success
    // =========================================================================

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(userId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByLibraryIdAndPublicationStatusNotIn(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnEmptyList_whenNoMatchExists() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(userId))
                    .thenReturn(List.of());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByLibraryIdAndPublicationStatusNotIn(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldAddSelfLinkToEachItem() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(userId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByLibraryIdAndPublicationStatusNotIn(USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().get(0).hasLink("self"));
        }
    }

    // =========================================================================
    // findByLibraryIdAndPublicationStatusNotIn — exceptions
    // =========================================================================

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnNotFound_whenEntityNotFound() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(userId))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByLibraryIdAndPublicationStatusNotIn(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnForbidden_whenAccessDenied() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(userId))
                    .thenThrow(new AccessDeniedException("denied"));

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByLibraryIdAndPublicationStatusNotIn(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(userId))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByLibraryIdAndPublicationStatusNotIn(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(userId))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByLibraryIdAndPublicationStatusNotIn(USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findAvailableForCollection — success
    // =========================================================================

    @Test
    void findAvailableForCollection_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findAvailableForCollection(userId, collectionId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findAvailableForCollection_shouldReturnEmptyList_whenNoMatchExists() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findAvailableForCollection(userId, collectionId))
                    .thenReturn(List.of());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findAvailableForCollection_shouldAddSelfLinkToEachItem() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.findAvailableForCollection(userId, collectionId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().get(0).hasLink("self"));
        }
    }

    // =========================================================================
    // findAvailableForCollection — exceptions
    // =========================================================================

    @Test
    void findAvailableForCollection_shouldReturnNotFound_whenEntityNotFound() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findAvailableForCollection(userId, collectionId))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findAvailableForCollection_shouldReturnForbidden_whenAccessDenied() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findAvailableForCollection(userId, collectionId))
                    .thenThrow(new AccessDeniedException("denied"));

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findAvailableForCollection_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findAvailableForCollection(userId, collectionId))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findAvailableForCollection_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findAvailableForCollection(userId, collectionId))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<PublicationDTO>> result =
                    sut.findAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // addPublicationToLibrary — success
    // =========================================================================

    @Test
    void addPublicationToLibrary_shouldReturnCorrectAuthors() throws Exception {

        // Arrange
        when(publicationSampleId.toString()).thenReturn("ps-001");
        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getEditionID()).thenReturn(editionId);
        when(editionService.findById(any())).thenReturn(edition);
        when(publicationSample.getOwnerID()).thenReturn(userId);
        when(bibliographicalData.getAuthors()).thenReturn(List.of(authorId));
        when(bibliographicalData.getGenres()).thenReturn(List.of());
        when(authorService.findById(authorId)).thenReturn(author);
        when(author.getName()).thenReturn("José Saramago");
        when(userService.findByID(any())).thenReturn(user);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(new ArrayList<>());
        when(publicationSampleService.addPublicationToLibrary(publicationSampleId, userId))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenAnswer(invocation -> {
                        List<Genre> genres = invocation.getArgument(4);
                        List<Author> authors = invocation.getArgument(3);
                        return new PublicationDTO(
                                "ps-001",
                                "The Gospel According to Jesus Christ",
                                authors.stream().map(Author::getName).toList(),
                                "en",
                                genres.stream().map(Genre::getGenreType).toList(),
                                "jose.saramago",
                                null,
                                "NEW",
                                null,
                                null
                        );
                    });

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.addPublicationToLibrary(PS_ID_STRING, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertEquals(List.of("José Saramago"), result.getBody().getAuthors());
        }
    }

    @Test
    void addPublicationToLibrary_shouldReturnCorrectGenres() throws Exception {

        // Arrange
        when(publicationSampleId.toString()).thenReturn("ps-001");
        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getEditionID()).thenReturn(editionId);
        when(editionService.findById(any())).thenReturn(edition);
        when(publicationSample.getOwnerID()).thenReturn(userId);
        when(bibliographicalData.getGenres()).thenReturn(List.of(genreId));
        when(bibliographicalData.getAuthors()).thenReturn(List.of());
        when(genreService.findById(genreId)).thenReturn(genre);
        when(genre.getGenreType()).thenReturn("Fiction");
        when(userService.findByID(any())).thenReturn(user);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(new ArrayList<>());
        when(publicationSampleService.addPublicationToLibrary(publicationSampleId, userId))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenAnswer(invocation -> {
                        List<Genre> genres = invocation.getArgument(4);
                        List<Author> authors = invocation.getArgument(3);
                        return new PublicationDTO(
                                "ps-001",
                                "The Gospel According to Jesus Christ",
                                authors.stream().map(Author::getName).toList(),
                                "en",
                                genres.stream().map(Genre::getGenreType).toList(),
                                "jose.saramago",
                                null,
                                "NEW",
                                null,
                                null
                        );
                    });

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.addPublicationToLibrary(PS_ID_STRING, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertEquals(List.of("Fiction"), result.getBody().getGenres());
        }
    }

    @Test
    void addPublicationToLibrary_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.addPublicationToLibrary(publicationSampleId, userId))
                    .thenReturn(publicationSample);
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.addPublicationToLibrary(PS_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
        }
    }

    @Test
    void addPublicationToLibrary_shouldAddSelfLink() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.addPublicationToLibrary(publicationSampleId, userId))
                    .thenReturn(publicationSample);
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.addPublicationToLibrary(PS_ID_STRING, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().hasLink("self"));
        }
    }

    // =========================================================================
    // addPublicationToLibrary — exceptions
    // =========================================================================

    @Test
    void addPublicationToLibrary_shouldReturnForbidden_whenAccessDenied() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.addPublicationToLibrary(publicationSampleId, userId))
                    .thenThrow(new AccessDeniedException("denied"));

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.addPublicationToLibrary(PS_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void addPublicationToLibrary_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.addPublicationToLibrary(publicationSampleId, userId))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.addPublicationToLibrary(PS_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void addPublicationToLibrary_shouldReturnNotFound_whenEntityNotFound() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.addPublicationToLibrary(publicationSampleId, userId))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.addPublicationToLibrary(PS_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void addPublicationToLibrary_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.addPublicationToLibrary(publicationSampleId, userId))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.addPublicationToLibrary(PS_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // assignPublicationToCollection — success
    // =========================================================================

    @Test
    void assignPublicationToCollection_shouldReturnCorrectAuthors() throws Exception {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        when(publicationSampleId.toString()).thenReturn("ps-001");
        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getEditionID()).thenReturn(editionId);
        when(editionService.findById(any())).thenReturn(edition);
        when(publicationSample.getOwnerID()).thenReturn(userId);
        when(bibliographicalData.getAuthors()).thenReturn(List.of(authorId));
        when(bibliographicalData.getGenres()).thenReturn(List.of());
        when(authorService.findById(authorId)).thenReturn(author);
        when(author.getName()).thenReturn("José Saramago");
        when(userService.findByID(any())).thenReturn(user);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(new ArrayList<>());
        when(publicationSampleService.assignPublicationToCollection(collectionId, publicationSampleId, userId))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenAnswer(invocation -> {
                        List<Genre> genres = invocation.getArgument(4);
                        List<Author> authors = invocation.getArgument(3);
                        return new PublicationDTO(
                                "ps-001",
                                "The Gospel According to Jesus Christ",
                                authors.stream().map(Author::getName).toList(),
                                "en",
                                genres.stream().map(Genre::getGenreType).toList(),
                                "jose.saramago",
                                null,
                                "NEW",
                                null,
                                null
                        );
                    });

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.assignPublicationToCollection(PS_ID_STRING, COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertEquals(List.of("José Saramago"), result.getBody().getAuthors());
        }
    }

    @Test
    void assignPublicationToCollection_shouldReturnCorrectGenres() throws Exception {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        when(publicationSampleId.toString()).thenReturn("ps-001");
        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getEditionID()).thenReturn(editionId);
        when(editionService.findById(any())).thenReturn(edition);
        when(publicationSample.getOwnerID()).thenReturn(userId);
        when(bibliographicalData.getGenres()).thenReturn(List.of(genreId));
        when(bibliographicalData.getAuthors()).thenReturn(List.of());
        when(genreService.findById(genreId)).thenReturn(genre);
        when(genre.getGenreType()).thenReturn("Fiction");
        when(userService.findByID(any())).thenReturn(user);
        when(publicationSampleLinkProvider.getPublicationSampleLinks(any())).thenReturn(new ArrayList<>());
        when(publicationSampleService.assignPublicationToCollection(collectionId, publicationSampleId, userId))
                .thenReturn(publicationSample);
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenAnswer(invocation -> {
                        List<Genre> genres = invocation.getArgument(4);
                        List<Author> authors = invocation.getArgument(3);
                        return new PublicationDTO(
                                "ps-001",
                                "The Gospel According to Jesus Christ",
                                authors.stream().map(Author::getName).toList(),
                                "en",
                                genres.stream().map(Genre::getGenreType).toList(),
                                "jose.saramago",
                                null,
                                "NEW",
                                null,
                                null
                        );
                    });

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.assignPublicationToCollection(PS_ID_STRING, COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertEquals(List.of("Fiction"), result.getBody().getGenres());
        }
    }

    @Test
    void assignPublicationToCollection_shouldReturnOk_whenSuccess() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.assignPublicationToCollection(collectionId, publicationSampleId, userId))
                    .thenReturn(publicationSample);
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.assignPublicationToCollection(PS_ID_STRING, COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
        }
    }

    @Test
    void assignPublicationToCollection_shouldAddSelfLink() throws Exception {

        // Arrange
        setupPublicationSampleMocks();
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.assignPublicationToCollection(collectionId, publicationSampleId, userId))
                    .thenReturn(publicationSample);
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.assignPublicationToCollection(PS_ID_STRING, COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertNotNull(result.getBody());
            assertTrue(result.getBody().hasLink("self"));
        }
    }

    // =========================================================================
    // assignPublicationToCollection — exceptions
    // =========================================================================

    @Test
    void assignPublicationToCollection_shouldReturnForbidden_whenAccessDenied() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.assignPublicationToCollection(collectionId, publicationSampleId, userId))
                    .thenThrow(new AccessDeniedException("denied"));

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.assignPublicationToCollection(PS_ID_STRING, COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void assignPublicationToCollection_shouldReturnBadRequest_whenIllegalArgument() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.assignPublicationToCollection(collectionId, publicationSampleId, userId))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.assignPublicationToCollection(PS_ID_STRING, COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void assignPublicationToCollection_shouldReturnNotFound_whenEntityNotFound() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.assignPublicationToCollection(collectionId, publicationSampleId, userId))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.assignPublicationToCollection(PS_ID_STRING, COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void assignPublicationToCollection_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.assignPublicationToCollection(collectionId, publicationSampleId, userId))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<PublicationDTO> result =
                    sut.assignPublicationToCollection(PS_ID_STRING, COLLECTION_ID_STRING, USER_ID_STRING);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }
}