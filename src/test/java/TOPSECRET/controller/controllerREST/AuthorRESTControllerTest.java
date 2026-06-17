package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AuthorLinkProvider;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.author.AuthorMapper;
import TOPSECRET.dto.author.request.CreateAuthorDTO;
import TOPSECRET.dto.author.response.AuthorDTO;
import TOPSECRET.services.AuthorService;
import TOPSECRET.services.CountryService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorRESTControllerTest {

    @Mock
    private AuthorService service;

    @Mock
    private AuthorLinkProvider provider;

    @Mock
    private CountryService countryService;

    @Mock
    private CreateAuthorDTO createDTO;

    @InjectMocks
    private AuthorRESTController controller;

    // =========================================================
    // CREATE — SUCCESS
    // =========================================================

    @Test
    void shouldCreateAuthorSuccessfully() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        Author author = mock(Author.class);
        AuthorId authorId = mock(AuthorId.class);
        Country country = mock(Country.class);
        CountryId countryId = mock(CountryId.class);
        AuthorDTO responseDTO = mock(AuthorDTO.class);
        Link link = mock(Link.class);

        when(author.id()).thenReturn(authorId);
        when(authorId.toString()).thenReturn("author-1");
        when(author.getCountryID()).thenReturn(countryId);
        when(countryService.findById(countryId)).thenReturn(country);

        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {

            mapper.when(() -> AuthorMapper.toUserId(userIdStr)).thenReturn(userId);
            mapper.when(() -> AuthorMapper.toDTO(author, country)).thenReturn(responseDTO);
            stubCreateDTO(mapper, author);
            stubProviderLinks(userId, List.of(link), List.of());

            // ACT
            ResponseEntity<AuthorDTO> response =
                    controller.createAuthor(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

    @Test
    void shouldAddPublicAndPrivateLinksOnCreate() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        Author author = mock(Author.class);
        AuthorId authorId = mock(AuthorId.class);
        Country country = mock(Country.class);
        CountryId countryId = mock(CountryId.class);
        AuthorDTO responseDTO = buildAuthorDTO("author-1");

        Link publicLink = Link.of("/authors", "find-all");
        Link privateLink = Link.of("/authors", "create-author");

        when(author.id()).thenReturn(authorId);
        when(authorId.toString()).thenReturn("author-1");
        when(author.getCountryID()).thenReturn(countryId);
        when(countryService.findById(countryId)).thenReturn(country);

        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {

            mapper.when(() -> AuthorMapper.toUserId(userIdStr)).thenReturn(userId);
            mapper.when(() -> AuthorMapper.toDTO(author, country)).thenReturn(responseDTO);
            stubCreateDTO(mapper, author);
            stubProviderLinks(userId, List.of(publicLink), List.of(privateLink));

            // ACT
            controller.createAuthor(createDTO, userIdStr);

            // ASSERT — public link + private link + selfRel = 3
            assertEquals(3, responseDTO.getLinks().toList().size());
        }
    }

    @Test
    void shouldAddCreateAuthorRelOnCreate() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        Author author = mock(Author.class);
        AuthorId authorId = mock(AuthorId.class);
        Country country = mock(Country.class);
        CountryId countryId = mock(CountryId.class);
        AuthorDTO responseDTO = buildAuthorDTO("author-1");

        Link privateLink = Link.of("/authors", "create-author");

        when(author.id()).thenReturn(authorId);
        when(authorId.toString()).thenReturn("author-1");
        when(author.getCountryID()).thenReturn(countryId);
        when(countryService.findById(countryId)).thenReturn(country);

        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {

            mapper.when(() -> AuthorMapper.toUserId(userIdStr)).thenReturn(userId);
            mapper.when(() -> AuthorMapper.toDTO(author, country)).thenReturn(responseDTO);
            stubCreateDTO(mapper, author);
            stubProviderLinks(userId, List.of(), List.of(privateLink));

            // ACT
            controller.createAuthor(createDTO, userIdStr);

            // ASSERT
            assertTrue(responseDTO.getLinks().toList().stream()
                    .anyMatch(l -> l.getRel().value().equals("create-author")));
        }
    }

    // =========================================================
    // CREATE — ERROR CASES
    // =========================================================

    @Test
    void shouldReturnForbiddenWhenAccessDeniedOnCreate() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {

            mapper.when(() -> AuthorMapper.toUserId(userIdStr)).thenReturn(userId);
            stubCreateDTOThrows(mapper, new AccessDeniedException("Denied"));

            // ACT
            ResponseEntity<AuthorDTO> response =
                    controller.createAuthor(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnBadRequestWhenIllegalArgumentOnCreate() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {

            mapper.when(() -> AuthorMapper.toUserId(userIdStr)).thenReturn(userId);
            stubCreateDTOThrows(mapper, new IllegalArgumentException());

            // ACT
            ResponseEntity<AuthorDTO> response =
                    controller.createAuthor(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnConflictWhenAuthorAlreadyExistsOnCreate() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {

            mapper.when(() -> AuthorMapper.toUserId(userIdStr)).thenReturn(userId);
            stubCreateDTOThrows(mapper, new InstanceAlreadyExistsException());

            // ACT
            ResponseEntity<AuthorDTO> response =
                    controller.createAuthor(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnInternalServerErrorOnCreateWhenUnexpectedExceptionOccurs()
            throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {

            mapper.when(() -> AuthorMapper.toUserId(userIdStr)).thenReturn(userId);
            stubCreateDTOThrows(mapper, new RuntimeException());

            // ACT
            ResponseEntity<AuthorDTO> response =
                    controller.createAuthor(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================
    // FIND ALL — SUCCESS
    // =========================================================

    @Test
    void shouldReturnAllAuthorsSuccessfully() {

        // ARRANGE
        Author a1 = mock(Author.class);
        Author a2 = mock(Author.class);
        AuthorId id1 = mock(AuthorId.class);
        AuthorId id2 = mock(AuthorId.class);
        CountryId cId1 = mock(CountryId.class);
        CountryId cId2 = mock(CountryId.class);
        Country country1 = mock(Country.class);
        Country country2 = mock(Country.class);

        when(a1.id()).thenReturn(id1);
        when(a2.id()).thenReturn(id2);
        when(id1.toString()).thenReturn("author-1");
        when(id2.toString()).thenReturn("author-2");
        when(a1.getCountryID()).thenReturn(cId1);
        when(a2.getCountryID()).thenReturn(cId2);
        when(countryService.findById(cId1)).thenReturn(country1);
        when(countryService.findById(cId2)).thenReturn(country2);

        AuthorDTO dto1 = buildAuthorDTO("author-1");
        AuthorDTO dto2 = buildAuthorDTO("author-2");

        when(service.findAll()).thenReturn(List.of(a1, a2));
        when(provider.getPublicLinks()).thenReturn(List.of());

        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {

            mapper.when(() -> AuthorMapper.toDTO(a1, country1)).thenReturn(dto1);
            mapper.when(() -> AuthorMapper.toDTO(a2, country2)).thenReturn(dto2);

            // ACT
            ResponseEntity<List<AuthorDTO>> response = controller.findAll();

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            assertTrue(response.getBody().containsAll(List.of(dto1, dto2)));
        }
    }

    @Test
    void shouldAddSelfLinkToEachDTOWhenFindingAll() {

        // ARRANGE
        Author a1 = mock(Author.class);
        AuthorId id1 = mock(AuthorId.class);
        CountryId cId1 = mock(CountryId.class);
        Country country1 = mock(Country.class);

        when(a1.id()).thenReturn(id1);
        when(id1.toString()).thenReturn("author-1");
        when(a1.getCountryID()).thenReturn(cId1);
        when(countryService.findById(cId1)).thenReturn(country1);

        AuthorDTO dto1 = buildAuthorDTO("author-1");
        when(service.findAll()).thenReturn(List.of(a1));
        when(provider.getPublicLinks()).thenReturn(List.of());

        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {

            mapper.when(() -> AuthorMapper.toDTO(a1, country1)).thenReturn(dto1);

            // ACT
            controller.findAll();

            // ASSERT
            assertTrue(dto1.getLinks().toList().stream()
                    .anyMatch(l -> l.getRel().value().equals("self")));
        }
    }

    // =========================================================
    // FIND ALL — ERROR CASES
    // =========================================================

    @Test
    void shouldReturnEmptyListWhenNoAuthorsExist() {

        // ARRANGE
        when(service.findAll()).thenReturn(List.of());

        // ACT
        ResponseEntity<List<AuthorDTO>> response = controller.findAll();

        // ASSERT
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void shouldReturnBadRequestWhenFindAllThrowsIllegalArgumentException() {

        // ARRANGE
        when(service.findAll()).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<List<AuthorDTO>> response = controller.findAll();

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnInternalServerErrorWhenFindAllThrowsUnexpectedException() {

        // ARRANGE
        when(service.findAll()).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<List<AuthorDTO>> response = controller.findAll();

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // =========================================================
    // FIND BY ID — SUCCESS
    // =========================================================

    @Test
    void shouldReturnAuthorByIdSuccessfully() throws Exception {

        // ARRANGE
        String rawId = "author-1";
        AuthorId authorId = mock(AuthorId.class);
        Author author = mock(Author.class);
        AuthorId idFromAuthor = mock(AuthorId.class);
        CountryId countryId = mock(CountryId.class);
        Country country = mock(Country.class);
        AuthorDTO responseDTO = mock(AuthorDTO.class);

        when(author.id()).thenReturn(idFromAuthor);
        when(idFromAuthor.toString()).thenReturn("author-1");
        when(author.getCountryID()).thenReturn(countryId);
        when(countryService.findById(countryId)).thenReturn(country);
        when(service.findById(authorId)).thenReturn(author);
        when(provider.getPublicLinks()).thenReturn(List.of());

        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {

            mapper.when(() -> AuthorMapper.toAuthorId(rawId)).thenReturn(authorId);
            mapper.when(() -> AuthorMapper.toDTO(author, country)).thenReturn(responseDTO);

            // ACT
            ResponseEntity<AuthorDTO> response = controller.findById(rawId);

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

    @Test
    void shouldAddSelfRelAndPublicLinksWhenFindingById() throws Exception {

        // ARRANGE
        String rawId = "author-1";
        AuthorId authorId = mock(AuthorId.class);
        Author author = mock(Author.class);
        AuthorId idFromAuthor = mock(AuthorId.class);
        CountryId countryId = mock(CountryId.class);
        Country country = mock(Country.class);
        AuthorDTO responseDTO = buildAuthorDTO("author-1");

        when(author.id()).thenReturn(idFromAuthor);
        when(idFromAuthor.toString()).thenReturn("author-1");
        when(author.getCountryID()).thenReturn(countryId);
        when(countryService.findById(countryId)).thenReturn(country);
        when(service.findById(authorId)).thenReturn(author);
        when(provider.getPublicLinks()).thenReturn(
                List.of(Link.of("/authors", "find-all")));

        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {

            mapper.when(() -> AuthorMapper.toAuthorId(rawId)).thenReturn(authorId);
            mapper.when(() -> AuthorMapper.toDTO(author, country)).thenReturn(responseDTO);

            // ACT
            controller.findById(rawId);

            // ASSERT — public link + selfRel = 2
            List<Link> links = responseDTO.getLinks().toList();
            assertEquals(2, links.size());
            assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("self")));
            assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-all")));
        }
    }

    // =========================================================
    // FIND BY ID — ERROR CASES
    // =========================================================

    @Test
    void shouldReturnBadRequestWhenAuthorIdIsInvalid() {

        // ARRANGE
        String rawId = "invalid";

        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {

            mapper.when(() -> AuthorMapper.toAuthorId(rawId))
                    .thenThrow(new IllegalArgumentException("Invalid ID"));

            // ACT
            ResponseEntity<AuthorDTO> response = controller.findById(rawId);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnInternalServerErrorWhenFindByIdThrowsUnexpectedException() {

        // ARRANGE
        String rawId = "author-1";
        AuthorId authorId = mock(AuthorId.class);

        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {

            mapper.when(() -> AuthorMapper.toAuthorId(rawId)).thenReturn(authorId);
            when(service.findById(authorId)).thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<AuthorDTO> response = controller.findById(rawId);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================
    // HELPERS
    // =========================================================

    /**
     * Stubs provider public and private links for create-endpoint tests.
     * Uses doAnswer to bypass the checked InstanceNotFoundException on getPrivateLinks.
     */
    private void stubProviderLinks(UserId userId, List<Link> publicLinks, List<Link> privateLinks)
            throws Exception {
        when(provider.getPublicLinks()).thenReturn(publicLinks);
        lenient().doAnswer(inv -> privateLinks).when(provider).getPrivateLinks(userId);
        lenient().doAnswer(inv -> privateLinks).when(provider).getPrivateLinks(any());
    }

    /**
     * Stubs AuthorMapper static methods so service.createAndSave() succeeds,
     * returning the provided author.
     */
    private void stubCreateDTO(MockedStatic<AuthorMapper> mapper, Author author)
            throws Exception {

        mapper.when(() -> AuthorMapper.toCountryId(createDTO)).thenReturn(null);

        when(service.createAndSave(any(), any(), any(), any(), any()))
                .thenReturn(author);
    }

    /**
     * Stubs AuthorMapper static methods and makes service.createAndSave()
     * throw the provided exception.
     */
    private void stubCreateDTOThrows(MockedStatic<AuthorMapper> mapper,
                                     Exception exception) throws Exception {

        mapper.when(() -> AuthorMapper.toCountryId(createDTO)).thenReturn(null);

        when(service.createAndSave(any(), any(), any(), any(), any()))
                .thenThrow(exception);
    }

    /**
     * Builds an {@link AuthorDTO} with stub values for use in link-assertion tests.
     */
    private AuthorDTO buildAuthorDTO(String authorId) {
        return new AuthorDTO(
                authorId,
                "Test Author",
                "Portugal",
                "1980-01-01",
                "A biography."
        );
    }
}