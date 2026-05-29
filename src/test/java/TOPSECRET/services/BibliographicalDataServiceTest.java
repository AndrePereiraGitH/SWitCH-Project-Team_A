package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.BibliographicalDataAuthorizationPolicy;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BibliographicalDataServiceTest {

    private final String title = "title";
    private List<AuthorId> authors;
    private Locale language;
    private LocalDate date;
    private List<GenreId> genres;
    private final String chunks = "Chunks of text";

    @Mock
    private AuthorId authorId;

    @Mock
    private GenreId genreId;

    @Mock
    private BibliographicalDataFactory factory;

    @Mock
    private IBibliographicalDataRepository bibliographicalDataRepo;

    @Mock
    private IAuthorRepository authorRepo;

    @Mock
    private IUserRepository userRepo;

    @Mock
    private BibliographicalData createdBd;

    @Mock
    private BibliographicalData savedBd;

    @Mock
    private UserId userId;

    @Mock
    private BibliographicalDataAuthorizationPolicy authPolicy;

    @InjectMocks
    private BibliographicalDataService service;

    @BeforeEach
    void setUp() {
        authors = List.of(authorId);
        language = Locale.ENGLISH;
        date = LocalDate.of(2010, 1, 14);
        genres = List.of(genreId);
    }

    // =============================================================
    // ================== createAndSave Tests ======================
    // =============================================================

    @Test
    void createAndAdd_shouldCreateSaveAndReturnSavedBibliographicalData() throws InstanceAlreadyExistsException, AccessDeniedException {
        // Arrange
        User user = mock(User.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(factory.create(title, authors, language, date, genres, chunks)).thenReturn(createdBd);
        when(bibliographicalDataRepo.save(createdBd)).thenReturn(savedBd);

        when(authPolicy.canCreateBibliographicalData(user)).thenReturn(true);

        // Act
        BibliographicalData result = service.createAndSave(
                title, authors, language, date, genres, chunks, userId
        );

        // Assert
        assertSame(savedBd, result);
    }

    @Test
    void createShouldThrowWhenTitleIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(null, authors, language, date, genres, chunks, userId)
        );
    }

    @Test
    void createShouldThrowWhenAuthorsIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(title, null, language, date, genres, chunks, userId)
        );
    }

    @Test
    void createShouldThrowWhenGenresIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(title, authors, language, date, null, chunks, userId)
        );
    }

    @Test
    void createShouldThrowWhenUserIdIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(title, authors, language, date, genres, chunks, null)
        );
    }

    @Test
    void createShouldThrowWhenUserIsNotFound() {
        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.createAndSave(
                title, authors, language, date, genres, chunks, userId
        ));
    }

    @Test
    void createAndSaveShouldAllowOptionalFieldsToBeNull() throws InstanceAlreadyExistsException, AccessDeniedException {
        // Arrange
        User user = mock(User.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(factory.create(title, authors, null, null, genres, null)).thenReturn(createdBd);
        when(bibliographicalDataRepo.save(createdBd)).thenReturn(savedBd);

        when(authPolicy.canCreateBibliographicalData(user)).thenReturn(true);

        // Act
        BibliographicalData result = service.createAndSave(
                title, authors, null, null, genres, null, userId
        );

        // Assert
        assertSame(savedBd, result);
    }

    @Test
    void createAndSaveShouldThrowInstanceAlreadyExistsException() throws InstanceAlreadyExistsException {
        // Arrange
        User user = mock(User.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(factory.create(title, authors, language, date, genres, chunks)).thenReturn(createdBd);
        when(bibliographicalDataRepo.save(createdBd)).thenThrow(new InstanceAlreadyExistsException("Bibliographical Data already exists"));

        when(authPolicy.canCreateBibliographicalData(user)).thenReturn(true);

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class, () ->
                service.createAndSave(title, authors, language, date, genres, chunks, userId)
        );
    }

    // =============================================================
    // ==================== getAuthors Tests =======================
    // =============================================================

    @Test
    void getAuthorsShouldReturnAuthorNamesWhenAuthorsAreValid() throws InstanceNotFoundException {
        // Arrange
        BibliographicalData bd = mock(BibliographicalData.class);

        AuthorId authorId1 = mock(AuthorId.class);
        AuthorId authorId2 = mock(AuthorId.class);

        Author author1 = mock(Author.class);
        Author author2 = mock(Author.class);

        when(bd.getAuthors()).thenReturn(List.of(authorId1, authorId2));

        when(authorRepo.findById(authorId1)).thenReturn(Optional.of(author1));
        when(authorRepo.findById(authorId2)).thenReturn(Optional.of(author2));

        when(author1.getName()).thenReturn("Author One");
        when(author2.getName()).thenReturn("Author Two");

        // Act
        List<String> result = service.getAuthors(bd);

        // Assert
        assertEquals(List.of("Author One", "Author Two"), result);
    }

    @Test
    void getAuthorsShouldThrowInstanceNotFoundExceptionWhenAuthorNotFound() {
        // Arrange
        BibliographicalData bd = mock(BibliographicalData.class);
        AuthorId authorId = mock(AuthorId.class);

        when(authorId.toString()).thenReturn("A1");
        when(bd.getAuthors()).thenReturn(List.of(authorId));
        when(authorRepo.findById(authorId)).thenReturn(Optional.empty());

        // Act
        InstanceNotFoundException exception = assertThrows(
                InstanceNotFoundException.class,
                () -> service.getAuthors(bd)
        );

        // Assert
        assertTrue(exception.getMessage().contains("Author was not found:"));
    }

    @Test
    void getAuthorsShouldReturnEmptyListWhenNoAuthorsExist() throws InstanceNotFoundException {
        // Arrange
        BibliographicalData bd = mock(BibliographicalData.class);

        when(bd.getAuthors()).thenReturn(List.of());

        // Act
        List<String> result = service.getAuthors(bd);

        // Arrange
        assertTrue(result.isEmpty());
    }

    // =============================================================
    // ===================== findAll Tests =========================
    // =============================================================

    @Test
    void findAllShouldReturnList() {
        // Arrange
        BibliographicalData bd1 = mock(BibliographicalData.class);
        BibliographicalData bd2 = mock(BibliographicalData.class);

        // Act
        when(bibliographicalDataRepo.findAll()).thenReturn(List.of(bd1, bd2));

        List<BibliographicalData> result = service.findAll();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void findAllShouldReturnEmptyListWhenNoBibliographicalDataExists(){
        // Act
        when(bibliographicalDataRepo.findAll()).thenReturn(List.of());

        List<BibliographicalData> result = service.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    // =============================================================
    // =============== buildAuthorNamesMap Tests ===================
    // =============================================================

    @Test
    void shouldBuildAuthorNamesMap() throws Exception {
        // Arrange
        BibliographicalData bd1Double = mock(BibliographicalData.class);
        BibliographicalData bd2Double = mock(BibliographicalData.class);

        BibliographicalDataId id1Double = mock(BibliographicalDataId.class);
        BibliographicalDataId id2Double = mock(BibliographicalDataId.class);

        when(bd1Double.id()).thenReturn(id1Double);
        when(bd2Double.id()).thenReturn(id2Double);

        List<BibliographicalData> bibliographicalDataList = List.of(bd1Double, bd2Double);

        List<String> authors1 = List.of("Author A", "Author B");
        List<String> authors2 = List.of("Author C");

        doReturn(authors1).when(service).getAuthors(bd1Double);
        doReturn(authors2).when(service).getAuthors(bd2Double);

        // Act
        Map<BibliographicalDataId, List<String>> result = service.buildAuthorNamesMap(bibliographicalDataList);

        // Assert
        assertEquals(2, result.size());
        assertEquals(authors1, result.get(id1Double));
        assertEquals(authors2, result.get(id2Double));
    }

    @Test
    void shouldReturnEmptyMapWhenInputListIsEmpty() throws Exception {
        // Arrange
        List<BibliographicalData> bibliographicalDataList = List.of();

        // Act
        Map<BibliographicalDataId, List<String>> result = service.buildAuthorNamesMap(bibliographicalDataList);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenGetAuthorsFails() throws Exception {
        // Arrange
        BibliographicalData bdDouble = mock(BibliographicalData.class);

        List<BibliographicalData> bibliographicalDataList = List.of(bdDouble);

        doThrow(new InstanceNotFoundException())
                .when(service)
                .getAuthors(bdDouble);

        // Act + Assert
        assertThrows(InstanceNotFoundException.class, () -> service.buildAuthorNamesMap(bibliographicalDataList));
    }

    // =============================================================
    // ===================== findById Tests ========================
    // =============================================================

    @Test
    void findById_shouldReturnBibliographicalDataWhenExists() {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);

        when(bibliographicalDataRepo.findById(bdId))
                .thenReturn(Optional.of(bibliographicalData));

        // Act
        Optional<BibliographicalData> result = service.findById(bdId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(bibliographicalData, result.get());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        when(bibliographicalDataRepo.findById(bdId))
                .thenReturn(Optional.empty());

        // Act
        Optional<BibliographicalData> result = service.findById(bdId);

        // Assert
        assertTrue(result.isEmpty());
    }

    // =============================================================
    // ================== findAllByGenreId Tests ===================
    // =============================================================

    @Test
    void findAllByGenreId_shouldReturnBibliographicalDataIds() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        List<BibliographicalDataId> ids = List.of(
                mock(BibliographicalDataId.class),
                mock(BibliographicalDataId.class)
        );

        when(bibliographicalDataRepo.findAllByGenreId(genreId))
                .thenReturn(ids);

        // Act
        List<BibliographicalDataId> result = service.findAllByGenreId(genreId);

        // Assert
        assertEquals(ids, result);
    }

    // =============================================================
    // ================== findIdByTitle Tests ======================
    // =============================================================

    @Test
    void findIdByTitle_shouldReturnIdWhenTitleExists() {
        // Arrange
        String title = "Title test";
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        when(bibliographicalDataRepo.findIdByTitle(title))
                .thenReturn(Optional.of(bdId));

        // Act
        Optional<BibliographicalDataId> result = service.findIdByTitle(title);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(bdId, result.get());
    }

    @Test
    void findIdByTitle_shouldReturnEmptyWhenTitleDoesNotExist() {
        // Arrange
        String title = "Unknown Title";

        when(bibliographicalDataRepo.findIdByTitle(title))
                .thenReturn(Optional.empty());

        // Act
        Optional<BibliographicalDataId> result = service.findIdByTitle(title);

        // Assert
        assertTrue(result.isEmpty());
    }

    // =============================================================
    // ================== findIdByAuthorId Tests ===================
    // =============================================================

    @Test
    void findIdByAuthorId_shouldReturnBibliographicalDataIds() {
        // Arrange
        AuthorId authorId = mock(AuthorId.class);
        List<BibliographicalDataId> ids = List.of(
                mock(BibliographicalDataId.class),
                mock(BibliographicalDataId.class)
        );

        when(bibliographicalDataRepo.findIdByAuthorId(authorId))
                .thenReturn(ids);

        // Act
        List<BibliographicalDataId> result = service.findIdByAuthorId(authorId);

        // Assert
        assertEquals(ids, result);
    }

    // =============================================================
    // =================== getAllTitles Tests ======================
    // =============================================================

    @Test
    void getAllTitles_shouldReturnAllTitles() {
        // Arrange
        List<String> titles = List.of("title 1", "title 2");

        when(bibliographicalDataRepo.getAllTitles())
                .thenReturn(titles);

        // Act
        List<String> result = service.getAllTitles();

        // Assert
        assertEquals(titles, result);
    }
}