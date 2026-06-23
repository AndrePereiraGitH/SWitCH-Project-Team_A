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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublicationSampleRESTController}.
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

    private void setupPublicationSampleMocks() {
        when(publicationSampleId.toString()).thenReturn("ps-001");
        when(publicationSample.id()).thenReturn(publicationSampleId);
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(publicationSample.getEditionID()).thenReturn(editionId);
        when(publicationSample.getCondition()).thenReturn(Condition.NEW);
        when(publicationSample.getOwnerID()).thenReturn(userId);
        when(bibliographicalData.getOriginalLanguage()).thenReturn(Locale.ENGLISH);
        when(userService.findByID(any())).thenReturn(user);
        when(editionService.findById(any())).thenReturn(edition);
        when(publicationSampleLinkProvider.getPublicLinks())
                .thenReturn(new ArrayList<>());

        when(bibliographicalData.getTitle()).thenReturn("The Gospel According to Jesus Christ");
        when(bibliographicalData.getAuthors()).thenReturn(List.of());
        when(bibliographicalData.getGenres()).thenReturn(List.of());
        when(user.getUserName()).thenReturn("jose.saramago");
    }

    // =========================================================================
    // createPublicationSample
    // =========================================================================

    @Test
    void createPublicationSample_shouldReturnCreated_whenSuccess() throws Exception {
        setupPublicationSampleMocks();
        when(publicationSampleLinkProvider.getOwnerLinks(any())).thenReturn(new ArrayList<>());
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

            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            assertEquals(HttpStatus.CREATED, result.getStatusCode());
            assertNotNull(result.getBody());
        }
    }

    @Test
    void createPublicationSample_shouldAddSelfLink() throws Exception {
        setupPublicationSampleMocks();
        when(publicationSampleLinkProvider.getOwnerLinks(any())).thenReturn(new ArrayList<>());
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

            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            assertNotNull(result.getBody());
            assertTrue(result.getBody().hasLink("self"));
        }
    }

    @Test
    void createPublicationSample_shouldAddNavigationLinks_whenProviderReturnsLinks() throws Exception {
        setupPublicationSampleMocks();
        Link navLink = Link.of("/publicationSamples").withRel("create-publication-sample");
        when(publicationSampleLinkProvider.getOwnerLinks(any()))
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

            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            assertNotNull(result.getBody());
            assertTrue(result.getBody().hasLink("create-publication-sample"));
            assertTrue(result.getBody().hasLink("self"));
        }
    }

    @Test
    void createPublicationSample_shouldReturnNotFound_whenEntityNotFound() throws Exception {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBdId(any())).thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toEditionId(any())).thenReturn(mock(EditionId.class));
            mapper.when(() -> PublicationSampleMapper.toCondition(any())).thenReturn(Condition.NEW);
            when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new EntityNotFoundException());

            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void createPublicationSample_shouldReturnForbidden_whenAccessDenied() throws Exception {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBdId(any())).thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toEditionId(any())).thenReturn(mock(EditionId.class));
            mapper.when(() -> PublicationSampleMapper.toCondition(any())).thenReturn(Condition.NEW);
            when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new AccessDeniedException("denied"));

            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void createPublicationSample_shouldReturnBadRequest_whenIllegalArgument() throws Exception {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBdId(any())).thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toEditionId(any())).thenReturn(mock(EditionId.class));
            mapper.when(() -> PublicationSampleMapper.toCondition(any())).thenReturn(Condition.NEW);
            when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void createPublicationSample_shouldReturnConflict_whenInstanceAlreadyExists() throws Exception {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBdId(any())).thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toEditionId(any())).thenReturn(mock(EditionId.class));
            mapper.when(() -> PublicationSampleMapper.toCondition(any())).thenReturn(Condition.NEW);
            when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new InstanceAlreadyExistsException());

            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void createPublicationSample_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toBdId(any())).thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toEditionId(any())).thenReturn(mock(EditionId.class));
            mapper.when(() -> PublicationSampleMapper.toCondition(any())).thenReturn(Condition.NEW);
            when(publicationSampleService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new RuntimeException());

            ResponseEntity<PublicationDTO> result = sut.createPublicationSample(dto, USER_ID_STRING);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findById
    // =========================================================================

    @Test
    void findById_shouldReturnOk_whenPublicationSampleExists() throws Exception {
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findById(publicationSampleId)).thenReturn(publicationSample);
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<PublicationDTO> result = sut.findById(PS_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
        }
    }

    @Test
    void findById_shouldAddSelfLink() throws Exception {
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.findById(publicationSampleId)).thenReturn(publicationSample);
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<PublicationDTO> result = sut.findById(PS_ID_STRING);

            assertNotNull(result.getBody());
            assertTrue(result.getBody().hasLink("self"));
        }
    }

    @Test
    void findById_shouldReturnNotFound_whenEntityNotFound() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.findById(publicationSampleId))
                    .thenThrow(new EntityNotFoundException());

            ResponseEntity<PublicationDTO> result = sut.findById(PS_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findById_shouldReturnBadRequest_whenIllegalArgument() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.findById(publicationSampleId))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<PublicationDTO> result = sut.findById(PS_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findById_shouldReturnInternalServerError_whenUnexpectedException() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.findById(publicationSampleId))
                    .thenThrow(new RuntimeException());

            ResponseEntity<PublicationDTO> result = sut.findById(PS_ID_STRING);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findAll
    // =========================================================================

    @Test
    void findAll_shouldReturnOk_whenSuccess() throws Exception {
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findAll()).thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<List<PublicationDTO>> result = sut.findAll();

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoSamplesExist() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            when(publicationSampleService.findAll()).thenReturn(List.of());

            ResponseEntity<List<PublicationDTO>> result = sut.findAll();

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findAll_shouldAddSelfLinkToEachItem() throws Exception {
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.findAll()).thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<List<PublicationDTO>> result = sut.findAll();

            assertNotNull(result.getBody());
            assertTrue(result.getBody().get(0).hasLink("self"));
        }
    }

    @Test
    void findAll_shouldReturnCorrectAuthors() throws Exception {
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
        when(publicationSampleLinkProvider.getPublicLinks()).thenReturn(new ArrayList<>());
        when(publicationSampleService.findAll()).thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenAnswer(invocation -> {
                        List<Author> authors = invocation.getArgument(3);
                        List<Genre> genres = invocation.getArgument(4);
                        return new PublicationDTO(
                                "ps-001",
                                "The Gospel According to Jesus Christ",
                                authors.stream().map(Author::getName).toList(),
                                "en",
                                genres.stream().map(Genre::getGenreType).toList(),
                                "jose.saramago",
                                null, "NEW", null, null);
                    });

            ResponseEntity<List<PublicationDTO>> result = sut.findAll();

            assertNotNull(result.getBody());
            assertEquals(List.of("José Saramago"), result.getBody().get(0).getAuthors());
        }
    }

    @Test
    void findAll_shouldReturnCorrectGenres() throws Exception {
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
        when(publicationSampleLinkProvider.getPublicLinks()).thenReturn(new ArrayList<>());
        when(publicationSampleService.findAll()).thenReturn(List.of(publicationSample));
        when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenAnswer(invocation -> {
                        List<Author> authors = invocation.getArgument(3);
                        List<Genre> genres = invocation.getArgument(4);
                        return new PublicationDTO(
                                "ps-001",
                                "The Gospel According to Jesus Christ",
                                authors.stream().map(Author::getName).toList(),
                                "en",
                                genres.stream().map(Genre::getGenreType).toList(),
                                "jose.saramago",
                                null, "NEW", null, null);
                    });

            ResponseEntity<List<PublicationDTO>> result = sut.findAll();

            assertNotNull(result.getBody());
            assertEquals(List.of("Fiction"), result.getBody().get(0).getGenres());
        }
    }

    @Test
    void findAll_shouldReturnNotFound_whenEntityNotFound() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            when(publicationSampleService.findAll()).thenThrow(new EntityNotFoundException());

            ResponseEntity<List<PublicationDTO>> result = sut.findAll();

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findAll_shouldReturnBadRequest_whenIllegalArgument() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            when(publicationSampleService.findAll()).thenThrow(new IllegalArgumentException());

            ResponseEntity<List<PublicationDTO>> result = sut.findAll();

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findAll_shouldReturnInternalServerError_whenUnexpectedException() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            when(publicationSampleService.findAll()).thenThrow(new RuntimeException());

            ResponseEntity<List<PublicationDTO>> result = sut.findAll();

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findByBibliographicalDataId
    // =========================================================================

    @Test
    void findByBibliographicalDataId_shouldReturnOk_whenSuccess() throws Exception {
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findByBibliographicalDataId_shouldReturnEmptyList_whenNoMatchExists() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                    .thenReturn(List.of());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findByBibliographicalDataId_shouldAddSelfLinkToEachItem() throws Exception {
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(new PublicationDTO(null, null, null, null, null, null, null, null, null, null));
            when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING);

            assertNotNull(result.getBody());
            assertTrue(result.getBody().get(0).hasLink("self"));
        }
    }

    @Test
    void findByBibliographicalDataId_shouldReturnNotFound_whenEntityNotFound() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                    .thenThrow(new EntityNotFoundException());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByBibliographicalDataId_shouldReturnBadRequest_whenIllegalArgument() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByBibliographicalDataId_shouldReturnInternalServerError_whenUnexpectedException() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataId(any()))
                    .thenReturn(bibliographicalDataId);
            when(publicationSampleService.findByBibliographicalDataId(bibliographicalDataId))
                    .thenThrow(new RuntimeException());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByBibliographicalDataId(BD_ID_STRING);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findByBibliographicalDataIdIn
    // =========================================================================

    @Test
    void findByBibliographicalDataIdIn_shouldReturnOk_whenSuccess() throws Exception {
        setupPublicationSampleMocks();
        List<String> bdIdStrings = List.of(BD_ID_STRING);
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataIds(any())).thenReturn(bdIds);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<List<PublicationDTO>> result = sut.findByBibliographicalDataIdIn(bdIdStrings);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnEmptyList_whenNoMatchExists() {
        List<String> bdIdStrings = List.of(BD_ID_STRING);
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataIds(any())).thenReturn(bdIds);
            when(publicationSampleService.findByBibliographicalDataIdIn(bdIds)).thenReturn(List.of());

            ResponseEntity<List<PublicationDTO>> result = sut.findByBibliographicalDataIdIn(bdIdStrings);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnNotFound_whenEntityNotFound() {
        List<String> bdIdStrings = List.of(BD_ID_STRING);
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataIds(any())).thenReturn(bdIds);
            when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                    .thenThrow(new EntityNotFoundException());

            ResponseEntity<List<PublicationDTO>> result = sut.findByBibliographicalDataIdIn(bdIdStrings);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnBadRequest_whenIllegalArgument() {
        List<String> bdIdStrings = List.of(BD_ID_STRING);
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataIds(any())).thenReturn(bdIds);
            when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<List<PublicationDTO>> result = sut.findByBibliographicalDataIdIn(bdIdStrings);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByBibliographicalDataIdIn_shouldReturnInternalServerError_whenUnexpectedException() {
        List<String> bdIdStrings = List.of(BD_ID_STRING);
        List<BibliographicalDataId> bdIds = List.of(bibliographicalDataId);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toBibliographicalDataIds(any())).thenReturn(bdIds);
            when(publicationSampleService.findByBibliographicalDataIdIn(bdIds))
                    .thenThrow(new RuntimeException());

            ResponseEntity<List<PublicationDTO>> result = sut.findByBibliographicalDataIdIn(bdIdStrings);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findByEditionIdIn
    // =========================================================================

    @Test
    void findByEditionIdIn_shouldReturnOk_whenSuccess() throws Exception {
        setupPublicationSampleMocks();
        List<String> editionIdStrings = List.of(EDITION_ID_STRING);
        List<EditionId> editionIds = List.of(mock(EditionId.class));
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toEditionIds(any())).thenReturn(editionIds);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findByEditionIdIn(editionIds))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<List<PublicationDTO>> result = sut.findByEditionIdIn(editionIdStrings);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findByEditionIdIn_shouldReturnEmptyList_whenNoMatchExists() {
        List<String> editionIdStrings = List.of(EDITION_ID_STRING);
        List<EditionId> editionIds = List.of(mock(EditionId.class));
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toEditionIds(any())).thenReturn(editionIds);
            when(publicationSampleService.findByEditionIdIn(editionIds)).thenReturn(List.of());

            ResponseEntity<List<PublicationDTO>> result = sut.findByEditionIdIn(editionIdStrings);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findByEditionIdIn_shouldReturnNotFound_whenEntityNotFound() {
        List<String> editionIdStrings = List.of(EDITION_ID_STRING);
        List<EditionId> editionIds = List.of(mock(EditionId.class));
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toEditionIds(any())).thenReturn(editionIds);
            when(publicationSampleService.findByEditionIdIn(editionIds))
                    .thenThrow(new EntityNotFoundException());

            ResponseEntity<List<PublicationDTO>> result = sut.findByEditionIdIn(editionIdStrings);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByEditionIdIn_shouldReturnBadRequest_whenIllegalArgument() {
        List<String> editionIdStrings = List.of(EDITION_ID_STRING);
        List<EditionId> editionIds = List.of(mock(EditionId.class));
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toEditionIds(any())).thenReturn(editionIds);
            when(publicationSampleService.findByEditionIdIn(editionIds))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<List<PublicationDTO>> result = sut.findByEditionIdIn(editionIdStrings);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByEditionIdIn_shouldReturnInternalServerError_whenUnexpectedException() {
        List<String> editionIdStrings = List.of(EDITION_ID_STRING);
        List<EditionId> editionIds = List.of(mock(EditionId.class));
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toEditionIds(any())).thenReturn(editionIds);
            when(publicationSampleService.findByEditionIdIn(editionIds))
                    .thenThrow(new RuntimeException());

            ResponseEntity<List<PublicationDTO>> result = sut.findByEditionIdIn(editionIdStrings);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findByCollectionOfPublicationsId
    // =========================================================================

    @Test
    void findByCollectionOfPublicationsId_shouldReturnOk_whenSuccess() throws Exception {
        setupPublicationSampleMocks();
        when(publicationSampleLinkProvider.getCollectionLinks(any())).thenReturn(new ArrayList<>());
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

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findByCollectionOfPublicationsId_shouldReturnEmptyList_whenNoMatchExists() throws Exception {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findByCollectionOfPublicationsId(collectionId, userId))
                    .thenReturn(List.of());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findByCollectionOfPublicationsId_shouldReturnNotFound_whenEntityNotFound() throws Exception {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findByCollectionOfPublicationsId(collectionId, userId))
                    .thenThrow(new EntityNotFoundException());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByCollectionOfPublicationsId_shouldReturnForbidden_whenAccessDenied() throws Exception {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findByCollectionOfPublicationsId(collectionId, userId))
                    .thenThrow(new AccessDeniedException("denied"));

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByCollectionOfPublicationsId_shouldReturnBadRequest_whenIllegalArgument() throws Exception {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findByCollectionOfPublicationsId(collectionId, userId))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByCollectionOfPublicationsId_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findByCollectionOfPublicationsId(collectionId, userId))
                    .thenThrow(new RuntimeException());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findAllPublicationsOwnedByUser
    // =========================================================================

    @Test
    void findAllPublicationsOwnedByUser_shouldReturnOk_whenSuccess() throws Exception {
        setupPublicationSampleMocks();
        when(publicationSampleLinkProvider.getOwnerLinks(any())).thenReturn(new ArrayList<>());
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findAllPublicationsOwnedByUser(userId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<List<PublicationDTO>> result = sut.findAllPublicationsOwnedByUser(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findAllPublicationsOwnedByUser_shouldReturnEmptyList_whenUserHasNoPublications() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findAllPublicationsOwnedByUser(userId))
                    .thenReturn(List.of());

            ResponseEntity<List<PublicationDTO>> result = sut.findAllPublicationsOwnedByUser(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findAllPublicationsOwnedByUser_shouldReturnForbidden_whenAccessDenied() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findAllPublicationsOwnedByUser(userId))
                    .thenThrow(new AccessDeniedException("denied"));

            ResponseEntity<List<PublicationDTO>> result = sut.findAllPublicationsOwnedByUser(USER_ID_STRING);

            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findAllPublicationsOwnedByUser_shouldReturnNotFound_whenEntityNotFound() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findAllPublicationsOwnedByUser(userId))
                    .thenThrow(new EntityNotFoundException());

            ResponseEntity<List<PublicationDTO>> result = sut.findAllPublicationsOwnedByUser(USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findAllPublicationsOwnedByUser_shouldReturnBadRequest_whenIllegalArgument() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findAllPublicationsOwnedByUser(userId))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<List<PublicationDTO>> result = sut.findAllPublicationsOwnedByUser(USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findAllPublicationsOwnedByUser_shouldReturnInternalServerError_whenUnexpectedException() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findAllPublicationsOwnedByUser(userId))
                    .thenThrow(new RuntimeException());

            ResponseEntity<List<PublicationDTO>> result = sut.findAllPublicationsOwnedByUser(USER_ID_STRING);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findPublicationsInLibrary
    // =========================================================================

    @Test
    void findPublicationsInLibrary_shouldReturnOk_whenSuccess() throws Exception {
        setupPublicationSampleMocks();
        when(publicationSampleLinkProvider.getLibraryLinks(any())).thenReturn(new ArrayList<>());
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findPublicationsInLibrary(userId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<List<PublicationDTO>> result = sut.findPublicationsInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findPublicationsInLibrary_shouldReturnEmptyList_whenNoMatchExists() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findPublicationsInLibrary(userId))
                    .thenReturn(List.of());

            ResponseEntity<List<PublicationDTO>> result = sut.findPublicationsInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findPublicationsInLibrary_shouldReturnNotFound_whenEntityNotFound() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findPublicationsInLibrary(userId))
                    .thenThrow(new EntityNotFoundException());

            ResponseEntity<List<PublicationDTO>> result = sut.findPublicationsInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findPublicationsInLibrary_shouldReturnForbidden_whenAccessDenied() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findPublicationsInLibrary(userId))
                    .thenThrow(new AccessDeniedException("denied"));

            ResponseEntity<List<PublicationDTO>> result = sut.findPublicationsInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findPublicationsInLibrary_shouldReturnBadRequest_whenIllegalArgument() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findPublicationsInLibrary(userId))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<List<PublicationDTO>> result = sut.findPublicationsInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findPublicationsInLibrary_shouldReturnInternalServerError_whenUnexpectedException() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findPublicationsInLibrary(userId))
                    .thenThrow(new RuntimeException());

            ResponseEntity<List<PublicationDTO>> result = sut.findPublicationsInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findPublicationsNotInLibrary
    // =========================================================================

    @Test
    void findPublicationsNotInLibrary_shouldReturnOk_whenSuccess() throws Exception {
        setupPublicationSampleMocks();
        when(publicationSampleLinkProvider.getLibraryLinks(any())).thenReturn(new ArrayList<>());
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findPublicationsNotInLibrary(userId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<List<PublicationDTO>> result = sut.findPublicationsNotInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findPublicationsNotInLibrary_shouldReturnEmptyList_whenNoMatchExists() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findPublicationsNotInLibrary(userId))
                    .thenReturn(List.of());

            ResponseEntity<List<PublicationDTO>> result = sut.findPublicationsNotInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findPublicationsNotInLibrary_shouldReturnNotFound_whenEntityNotFound() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findPublicationsNotInLibrary(userId))
                    .thenThrow(new EntityNotFoundException());

            ResponseEntity<List<PublicationDTO>> result = sut.findPublicationsNotInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findPublicationsNotInLibrary_shouldReturnForbidden_whenAccessDenied() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findPublicationsNotInLibrary(userId))
                    .thenThrow(new AccessDeniedException("denied"));

            ResponseEntity<List<PublicationDTO>> result = sut.findPublicationsNotInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findPublicationsNotInLibrary_shouldReturnBadRequest_whenIllegalArgument() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findPublicationsNotInLibrary(userId))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<List<PublicationDTO>> result = sut.findPublicationsNotInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findPublicationsNotInLibrary_shouldReturnInternalServerError_whenUnexpectedException() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findPublicationsNotInLibrary(userId))
                    .thenThrow(new RuntimeException());

            ResponseEntity<List<PublicationDTO>> result = sut.findPublicationsNotInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findByLibraryIdAndPublicationStatusNotIn
    // =========================================================================

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnOk_whenSuccess() throws Exception {
        setupPublicationSampleMocks();
        when(publicationSampleLinkProvider.getLibraryLinks(any())).thenReturn(new ArrayList<>());
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(userId))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByLibraryIdAndPublicationStatusNotIn(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnEmptyList_whenNoMatchExists() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(userId))
                    .thenReturn(List.of());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByLibraryIdAndPublicationStatusNotIn(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnNotFound_whenEntityNotFound() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(userId))
                    .thenThrow(new EntityNotFoundException());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByLibraryIdAndPublicationStatusNotIn(USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnForbidden_whenAccessDenied() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(userId))
                    .thenThrow(new AccessDeniedException("denied"));

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByLibraryIdAndPublicationStatusNotIn(USER_ID_STRING);

            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnBadRequest_whenIllegalArgument() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(userId))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByLibraryIdAndPublicationStatusNotIn(USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findByLibraryIdAndPublicationStatusNotIn_shouldReturnInternalServerError_whenUnexpectedException() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(userId))
                    .thenThrow(new RuntimeException());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findByLibraryIdAndPublicationStatusNotIn(USER_ID_STRING);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findAvailableForCollection
    // =========================================================================

    @Test
    void findAvailableForCollection_shouldReturnOk_whenSuccess() throws Exception {
        setupPublicationSampleMocks();
        when(publicationSampleLinkProvider.getCollectionLinks(any())).thenReturn(new ArrayList<>());
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

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findAvailableForCollection_shouldReturnEmptyList_whenNoMatchExists() throws Exception {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findAvailableForCollection(userId, collectionId))
                    .thenReturn(List.of());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findAvailableForCollection_shouldReturnNotFound_whenEntityNotFound() throws Exception {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findAvailableForCollection(userId, collectionId))
                    .thenThrow(new EntityNotFoundException());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findAvailableForCollection_shouldReturnForbidden_whenAccessDenied() throws Exception {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findAvailableForCollection(userId, collectionId))
                    .thenThrow(new AccessDeniedException("denied"));

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findAvailableForCollection_shouldReturnBadRequest_whenIllegalArgument() throws Exception {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findAvailableForCollection(userId, collectionId))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void findAvailableForCollection_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            when(publicationSampleService.findAvailableForCollection(userId, collectionId))
                    .thenThrow(new RuntimeException());

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // findPublicationSampleInSharedCollection
    // =========================================================================

    @Test
    void findPublicationSampleInSharedCollection_shouldReturnForbidden_whenAccessDenied() throws Exception {
        when(publicationSampleService.findPublicationsInPublicCollection(any(), any()))
                .thenThrow(new AccessDeniedException("forbidden"));

        ResponseEntity<List<PublicationDTO>> result =
                sut.findPublicationSampleInSharedCollection("collectionId", "userId");

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void findPublicationSampleInSharedCollection_shouldReturnBadRequest_whenIllegalArgument() throws Exception {
        when(publicationSampleService.findPublicationsInPublicCollection(any(), any()))
                .thenThrow(new IllegalArgumentException());

        ResponseEntity<List<PublicationDTO>> result =
                sut.findPublicationSampleInSharedCollection("collectionId", "userId");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void findPublicationSampleInSharedCollection_shouldReturnNotFound_whenEntityNotFound() throws Exception {
        when(publicationSampleService.findPublicationsInPublicCollection(any(), any()))
                .thenThrow(new EntityNotFoundException());

        ResponseEntity<List<PublicationDTO>> result =
                sut.findPublicationSampleInSharedCollection("collectionId", "userId");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findPublicationSampleInSharedCollection_shouldReturnNotFound_whenInstanceNotFound() throws Exception {
        when(publicationSampleService.findPublicationsInPublicCollection(any(), any()))
                .thenThrow(new InstanceNotFoundException("not found"));

        ResponseEntity<List<PublicationDTO>> result =
                sut.findPublicationSampleInSharedCollection("collectionId", "userId");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findPublicationSampleInSharedCollection_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {
        when(publicationSampleService.findPublicationsInPublicCollection(any(), any()))
                .thenThrow(new RuntimeException());

        ResponseEntity<List<PublicationDTO>> result =
                sut.findPublicationSampleInSharedCollection("collectionId", "userId");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void findPublicationSampleInSharedCollection_shouldReturnOk_whenNoPublicationsExist() throws Exception {
        when(publicationSampleService.findPublicationsInPublicCollection(any(), any()))
                .thenReturn(List.of());

        ResponseEntity<List<PublicationDTO>> result =
                sut.findPublicationSampleInSharedCollection(
                        "collection-id",
                        "user-id");

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void findPublicationSampleInSharedCollection_shouldReturnOk_whenPublicationsExist() throws Exception {
        setupPublicationSampleMocks();
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.findPublicationsInPublicCollection(any(), any()))
                    .thenReturn(List.of(publicationSample));
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<List<PublicationDTO>> result =
                    sut.findPublicationSampleInSharedCollection("collection-id", "user-id");

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    // =========================================================================
    // addPublicationToLibrary
    // =========================================================================

    @Test
    void addPublicationToLibrary_shouldReturnOk_whenSuccess() throws Exception {
        setupPublicationSampleMocks();
        when(publicationSampleLinkProvider.getLibraryLinks(any())).thenReturn(new ArrayList<>());
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            mapper.when(() -> PublicationSampleMapper.toPublicationDTO(any(), any(), any(), any(), any(), any()))
                    .thenReturn(mock(PublicationDTO.class));
            when(publicationSampleService.addPublicationToLibrary(publicationSampleId, userId))
                    .thenReturn(publicationSample);
            when(bibliographicalDataService.findById(any())).thenReturn(bibliographicalData);

            ResponseEntity<PublicationDTO> result =
                    sut.addPublicationToLibrary(PS_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
        }
    }

    @Test
    void addPublicationToLibrary_shouldReturnForbidden_whenAccessDenied() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.addPublicationToLibrary(publicationSampleId, userId))
                    .thenThrow(new AccessDeniedException("denied"));

            ResponseEntity<PublicationDTO> result =
                    sut.addPublicationToLibrary(PS_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void addPublicationToLibrary_shouldReturnBadRequest_whenIllegalArgument() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.addPublicationToLibrary(publicationSampleId, userId))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<PublicationDTO> result =
                    sut.addPublicationToLibrary(PS_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void addPublicationToLibrary_shouldReturnNotFound_whenEntityNotFound() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.addPublicationToLibrary(publicationSampleId, userId))
                    .thenThrow(new EntityNotFoundException());

            ResponseEntity<PublicationDTO> result =
                    sut.addPublicationToLibrary(PS_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void addPublicationToLibrary_shouldReturnInternalServerError_whenUnexpectedException() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.addPublicationToLibrary(publicationSampleId, userId))
                    .thenThrow(new RuntimeException());

            ResponseEntity<PublicationDTO> result =
                    sut.addPublicationToLibrary(PS_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // assignPublicationToCollection
    // =========================================================================

    @Test
    void assignPublicationToCollection_shouldReturnOk_whenSuccess() throws Exception {
        setupPublicationSampleMocks();
        when(publicationSampleLinkProvider.getCollectionLinks(any())).thenReturn(new ArrayList<>());
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

            ResponseEntity<PublicationDTO> result =
                    sut.assignPublicationToCollection(PS_ID_STRING, COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
        }
    }

    @Test
    void assignPublicationToCollection_shouldReturnForbidden_whenAccessDenied() {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.assignPublicationToCollection(collectionId, publicationSampleId, userId))
                    .thenThrow(new AccessDeniedException("denied"));

            ResponseEntity<PublicationDTO> result =
                    sut.assignPublicationToCollection(PS_ID_STRING, COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void assignPublicationToCollection_shouldReturnBadRequest_whenIllegalArgument() {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.assignPublicationToCollection(collectionId, publicationSampleId, userId))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<PublicationDTO> result =
                    sut.assignPublicationToCollection(PS_ID_STRING, COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void assignPublicationToCollection_shouldReturnNotFound_whenEntityNotFound() {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.assignPublicationToCollection(collectionId, publicationSampleId, userId))
                    .thenThrow(new EntityNotFoundException());

            ResponseEntity<PublicationDTO> result =
                    sut.assignPublicationToCollection(PS_ID_STRING, COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Test
    void assignPublicationToCollection_shouldReturnInternalServerError_whenUnexpectedException() {
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(any()))
                    .thenReturn(collectionId);
            mapper.when(() -> PublicationSampleMapper.toPubId(any())).thenReturn(publicationSampleId);
            when(publicationSampleService.assignPublicationToCollection(collectionId, publicationSampleId, userId))
                    .thenThrow(new RuntimeException());

            ResponseEntity<PublicationDTO> result =
                    sut.assignPublicationToCollection(PS_ID_STRING, COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    // =========================================================================
    // OPTIONS — root ("")
    // =========================================================================

    @Test
    void optionsRoot_whenUserIsNormalUser_shouldReturnGetPostAndOptions() {
        Set<HttpMethod> expected = Set.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canCreatePublicationSample(userId)).thenReturn(true);

            ResponseEntity<Void> result = sut.optionsRoot(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getHeaders().getAllow().containsAll(expected));
        }
    }

    @Test
    void optionsRoot_whenUserIsNotNormalUser_shouldReturnGetAndOptionsOnly() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canCreatePublicationSample(userId)).thenReturn(false);

            ResponseEntity<Void> result = sut.optionsRoot(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getHeaders().getAllow().containsAll(Set.of(HttpMethod.GET, HttpMethod.OPTIONS)));
            assertFalse(result.getHeaders().getAllow().contains(HttpMethod.POST));
        }
    }

    @Test
    void optionsRoot_whenUserNotFound_shouldReturn404() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canCreatePublicationSample(userId))
                    .thenThrow(new EntityNotFoundException("User not found"));

            ResponseEntity<Void> result = sut.optionsRoot(USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }
    }

    @Test
    void optionsRoot_whenUserIdIsInvalid_shouldReturn400() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<Void> result = sut.optionsRoot(USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
    }

    // =========================================================================
    // OPTIONS — /{collectionId}/publications
    // =========================================================================

    @Test
    void optionsByCollectionOfPublicationsId_whenUserOwnsCollection_shouldReturnGetAndOptions() {
        Set<HttpMethod> expected = Set.of(HttpMethod.GET, HttpMethod.OPTIONS);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(COLLECTION_ID_STRING))
                    .thenReturn(mock(CollectionOfPublicationsId.class));
            when(publicationSampleService.canQueryByCollection(any(), any())).thenReturn(true);

            ResponseEntity<Void> result = sut.optionsByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getHeaders().getAllow().containsAll(expected));
        }
    }

    @Test
    void optionsByCollectionOfPublicationsId_whenUserDoesNotOwnCollection_shouldReturnOptionsOnly() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(COLLECTION_ID_STRING))
                    .thenReturn(mock(CollectionOfPublicationsId.class));
            when(publicationSampleService.canQueryByCollection(any(), any())).thenReturn(false);

            ResponseEntity<Void> result = sut.optionsByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertFalse(result.getHeaders().getAllow().contains(HttpMethod.GET));
        }
    }

    @Test
    void optionsByCollectionOfPublicationsId_whenUserNotFound_shouldReturn404() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(COLLECTION_ID_STRING))
                    .thenReturn(mock(CollectionOfPublicationsId.class));
            when(publicationSampleService.canQueryByCollection(any(), any()))
                    .thenThrow(new EntityNotFoundException("User not found"));

            ResponseEntity<Void> result = sut.optionsByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }
    }

    @Test
    void optionsByCollectionOfPublicationsId_whenCollectionNotFound_shouldReturn404() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(COLLECTION_ID_STRING))
                    .thenReturn(mock(CollectionOfPublicationsId.class));
            when(publicationSampleService.canQueryByCollection(any(), any()))
                    .thenThrow(new EntityNotFoundException("Collection not found"));

            ResponseEntity<Void> result = sut.optionsByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }
    }

    @Test
    void optionsByCollectionOfPublicationsId_whenUserIdIsInvalid_shouldReturn400() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<Void> result = sut.optionsByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
    }

    @Test
    void optionsByCollectionOfPublicationsId_whenCollectionIdIsInvalid_shouldReturn400() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(COLLECTION_ID_STRING))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<Void> result = sut.optionsByCollectionOfPublicationsId(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
    }

    // =========================================================================
    // OPTIONS — /my-publications
    // =========================================================================

    @Test
    void optionsMyPublications_whenUserIsAuthorized_shouldReturnGetAndOptions() {
        Set<HttpMethod> expected = Set.of(HttpMethod.GET, HttpMethod.OPTIONS);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindOwnPublications(userId)).thenReturn(true);

            ResponseEntity<Void> result = sut.optionsMyPublications(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getHeaders().getAllow().containsAll(expected));
        }
    }

    @Test
    void optionsMyPublications_whenUserIsNotAuthorized_shouldReturnOptionsOnly() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindOwnPublications(userId)).thenReturn(false);

            ResponseEntity<Void> result = sut.optionsMyPublications(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertFalse(result.getHeaders().getAllow().contains(HttpMethod.GET));
        }
    }

    @Test
    void optionsMyPublications_whenUserNotFound_shouldReturn404() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindOwnPublications(userId))
                    .thenThrow(new EntityNotFoundException("User not found"));

            ResponseEntity<Void> result = sut.optionsMyPublications(USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }
    }

    @Test
    void optionsMyPublications_whenUserIdIsInvalid_shouldReturn400() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<Void> result = sut.optionsMyPublications(USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
    }

    // =========================================================================
    // OPTIONS — /in-library
    // =========================================================================

    @Test
    void optionsInLibrary_whenUserIsAuthorized_shouldReturnGetAndOptions() {
        Set<HttpMethod> expected = Set.of(HttpMethod.GET, HttpMethod.OPTIONS);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindPublicationsInLibrary(userId)).thenReturn(true);

            ResponseEntity<Void> result = sut.optionsInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getHeaders().getAllow().containsAll(expected));
        }
    }

    @Test
    void optionsInLibrary_whenUserIsNotAuthorized_shouldReturnOptionsOnly() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindPublicationsInLibrary(userId)).thenReturn(false);

            ResponseEntity<Void> result = sut.optionsInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertFalse(result.getHeaders().getAllow().contains(HttpMethod.GET));
        }
    }

    @Test
    void optionsInLibrary_whenUserNotFound_shouldReturn404() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindPublicationsInLibrary(userId))
                    .thenThrow(new EntityNotFoundException("User not found"));

            ResponseEntity<Void> result = sut.optionsInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }
    }

    @Test
    void optionsInLibrary_whenUserIdIsInvalid_shouldReturn400() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<Void> result = sut.optionsInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
    }

    // =========================================================================
    // OPTIONS — /not-in-library
    // =========================================================================

    @Test
    void optionsNotInLibrary_whenUserIsAuthorized_shouldReturnGetAndOptions() {
        Set<HttpMethod> expected = Set.of(HttpMethod.GET, HttpMethod.OPTIONS);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindPublicationsNotInLibrary(userId)).thenReturn(true);

            ResponseEntity<Void> result = sut.optionsNotInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getHeaders().getAllow().containsAll(expected));
        }
    }

    @Test
    void optionsNotInLibrary_whenUserIsNotAuthorized_shouldReturnOptionsOnly() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindPublicationsNotInLibrary(userId)).thenReturn(false);

            ResponseEntity<Void> result = sut.optionsNotInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertFalse(result.getHeaders().getAllow().contains(HttpMethod.GET));
        }
    }

    @Test
    void optionsNotInLibrary_whenUserNotFound_shouldReturn404() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindPublicationsNotInLibrary(userId))
                    .thenThrow(new EntityNotFoundException("User not found"));

            ResponseEntity<Void> result = sut.optionsNotInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }
    }

    @Test
    void optionsNotInLibrary_whenUserIdIsInvalid_shouldReturn400() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<Void> result = sut.optionsNotInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
    }

    // =========================================================================
    // OPTIONS — /available-in-library
    // =========================================================================

    @Test
    void optionsAvailableInLibrary_whenUserIsAuthorized_shouldReturnGetAndOptions() {
        Set<HttpMethod> expected = Set.of(HttpMethod.GET, HttpMethod.OPTIONS);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindAvailableInLibrary(userId)).thenReturn(true);

            ResponseEntity<Void> result = sut.optionsAvailableInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getHeaders().getAllow().containsAll(expected));
        }
    }

    @Test
    void optionsAvailableInLibrary_whenUserIsNotAuthorized_shouldReturnOptionsOnly() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindAvailableInLibrary(userId)).thenReturn(false);

            ResponseEntity<Void> result = sut.optionsAvailableInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertFalse(result.getHeaders().getAllow().contains(HttpMethod.GET));
        }
    }

    @Test
    void optionsAvailableInLibrary_whenUserNotFound_shouldReturn404() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindAvailableInLibrary(userId))
                    .thenThrow(new EntityNotFoundException("User not found"));

            ResponseEntity<Void> result = sut.optionsAvailableInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }
    }

    @Test
    void optionsAvailableInLibrary_whenUserIdIsInvalid_shouldReturn400() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<Void> result = sut.optionsAvailableInLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
    }

    // =========================================================================
    // OPTIONS — /{collectionId}/available
    // =========================================================================

    @Test
    void optionsAvailableForCollection_whenUserOwnsCollection_shouldReturnGetAndOptions() throws Exception {
        Set<HttpMethod> expected = Set.of(HttpMethod.GET, HttpMethod.OPTIONS);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(COLLECTION_ID_STRING))
                    .thenReturn(mock(CollectionOfPublicationsId.class));
            when(publicationSampleService.canFindAvailableForCollection(any(), any())).thenReturn(true);

            ResponseEntity<Void> result = sut.optionsAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getHeaders().getAllow().containsAll(expected));
        }
    }

    @Test
    void optionsAvailableForCollection_whenUserDoesNotOwnCollection_shouldReturnOptionsOnly() throws Exception {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(COLLECTION_ID_STRING))
                    .thenReturn(mock(CollectionOfPublicationsId.class));
            when(publicationSampleService.canFindAvailableForCollection(any(), any())).thenReturn(false);

            ResponseEntity<Void> result = sut.optionsAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertFalse(result.getHeaders().getAllow().contains(HttpMethod.GET));
        }
    }

    @Test
    void optionsAvailableForCollection_whenUserNotFound_shouldReturn404() throws Exception {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(COLLECTION_ID_STRING))
                    .thenReturn(mock(CollectionOfPublicationsId.class));
            when(publicationSampleService.canFindAvailableForCollection(any(), any()))
                    .thenThrow(new EntityNotFoundException("User not found"));

            ResponseEntity<Void> result = sut.optionsAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }
    }

    @Test
    void optionsAvailableForCollection_whenCollectionNotFound_shouldReturn404() throws Exception {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(COLLECTION_ID_STRING))
                    .thenReturn(mock(CollectionOfPublicationsId.class));
            when(publicationSampleService.canFindAvailableForCollection(any(), any()))
                    .thenThrow(new EntityNotFoundException("Collection not found"));

            ResponseEntity<Void> result = sut.optionsAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }
    }

    @Test
    void optionsAvailableForCollection_whenUserIdIsInvalid_shouldReturn400() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<Void> result = sut.optionsAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
    }

    @Test
    void optionsAvailableForCollection_whenCollectionIdIsInvalid_shouldReturn400() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            mapper.when(() -> PublicationSampleMapper.toCollectionOfPublicationsId(COLLECTION_ID_STRING))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<Void> result = sut.optionsAvailableForCollection(COLLECTION_ID_STRING, USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
    }

    // =========================================================================
    // OPTIONS — /{collectionId}/shared-publications
    // =========================================================================

    @Test
    void optionsPublicationSampleInSharedCollection_whenUserIsAuthorized_shouldReturnGetAndOptions() {
        Set<HttpMethod> expected = Set.of(HttpMethod.GET, HttpMethod.OPTIONS);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindPublicationsInPublicCollection(userId)).thenReturn(true);

            ResponseEntity<Void> result = sut.optionsPublicationSampleInSharedCollection(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getHeaders().getAllow().containsAll(expected));
        }
    }

    @Test
    void optionsPublicationSampleInSharedCollection_whenUserIsNotAuthorized_shouldReturnOptionsOnly() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindPublicationsInPublicCollection(userId)).thenReturn(false);

            ResponseEntity<Void> result = sut.optionsPublicationSampleInSharedCollection(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertFalse(result.getHeaders().getAllow().contains(HttpMethod.GET));
        }
    }

    @Test
    void optionsPublicationSampleInSharedCollection_whenUserNotFound_shouldReturn404() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canFindPublicationsInPublicCollection(userId))
                    .thenThrow(new EntityNotFoundException("User not found"));

            ResponseEntity<Void> result = sut.optionsPublicationSampleInSharedCollection(USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }
    }

    @Test
    void optionsPublicationSampleInSharedCollection_whenUserIdIsInvalid_shouldReturn400() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<Void> result = sut.optionsPublicationSampleInSharedCollection(USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
    }

    // =========================================================================
    // OPTIONS — /{publicationSampleId}/library
    // =========================================================================

    @Test
    void optionsLibrary_whenUserIsAuthorized_shouldReturnPatchAndOptions() {
        Set<HttpMethod> expected = Set.of(HttpMethod.PATCH, HttpMethod.OPTIONS);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canAddPublicationToLibrary(userId)).thenReturn(true);

            ResponseEntity<Void> result = sut.optionsLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getHeaders().getAllow().containsAll(expected));
        }
    }

    @Test
    void optionsLibrary_whenUserIsNotAuthorized_shouldReturnOptionsOnly() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canAddPublicationToLibrary(userId)).thenReturn(false);

            ResponseEntity<Void> result = sut.optionsLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertFalse(result.getHeaders().getAllow().contains(HttpMethod.PATCH));
        }
    }

    @Test
    void optionsLibrary_whenUserNotFound_shouldReturn404() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canAddPublicationToLibrary(userId))
                    .thenThrow(new EntityNotFoundException("User not found"));

            ResponseEntity<Void> result = sut.optionsLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }
    }

    @Test
    void optionsLibrary_whenUserIdIsInvalid_shouldReturn400() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<Void> result = sut.optionsLibrary(USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
    }

    // =========================================================================
    // OPTIONS — /{publicationSampleId}/collection
    // =========================================================================

    @Test
    void optionsCollection_whenUserIsAuthorized_shouldReturnPatchAndOptions() {
        Set<HttpMethod> expected = Set.of(HttpMethod.PATCH, HttpMethod.OPTIONS);

        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canAssignPublicationToCollection(userId)).thenReturn(true);

            ResponseEntity<Void> result = sut.optionsCollection(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getHeaders().getAllow().containsAll(expected));
        }
    }

    @Test
    void optionsCollection_whenUserIsNotAuthorized_shouldReturnOptionsOnly() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canAssignPublicationToCollection(userId)).thenReturn(false);

            ResponseEntity<Void> result = sut.optionsCollection(USER_ID_STRING);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertFalse(result.getHeaders().getAllow().contains(HttpMethod.PATCH));
        }
    }

    @Test
    void optionsCollection_whenUserNotFound_shouldReturn404() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING)).thenReturn(userId);
            when(publicationSampleService.canAssignPublicationToCollection(userId))
                    .thenThrow(new EntityNotFoundException("User not found"));

            ResponseEntity<Void> result = sut.optionsCollection(USER_ID_STRING);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }
    }

    @Test
    void optionsCollection_whenUserIdIsInvalid_shouldReturn400() {
        try (MockedStatic<PublicationSampleMapper> mapper = mockStatic(PublicationSampleMapper.class)) {
            mapper.when(() -> PublicationSampleMapper.toUserId(USER_ID_STRING))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<Void> result = sut.optionsCollection(USER_ID_STRING);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }
    }
}