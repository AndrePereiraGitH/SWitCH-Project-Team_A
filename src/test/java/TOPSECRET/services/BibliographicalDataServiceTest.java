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
import TOPSECRET.dto.bibliographicalData.response.responseBibliographicalDataDTO;
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

    //
    // createAndSave Tests
    //

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

    //
    // getAuthors Tests
    //

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

    //
    // findAll Tests
    //

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

    //
    // createAllDTOs Tests
    //

    @Test
    void createAllDTOs_whenRepositoryIsEmpty_returnsEmptyList() throws InstanceNotFoundException {
        // Arrange
        when(bibliographicalDataRepo.findAll()).thenReturn(List.of());

        // Act
        List<responseBibliographicalDataDTO> result =
                service.createDTOlist(service.findAll());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bibliographicalDataRepo).findAll();
    }

    @Test
    void createAllDTOs_whenBibliographicalDataExists_returnsDTOList() throws InstanceNotFoundException {
        // Arrange
        BibliographicalData bd1 = mock(BibliographicalData.class);
        BibliographicalData bd2 = mock(BibliographicalData.class);

        BibliographicalDataId bdId1 = mock(BibliographicalDataId.class);
        BibliographicalDataId bdId2 = mock(BibliographicalDataId.class);

        when(bd1.id()).thenReturn(bdId1);
        when(bd2.id()).thenReturn(bdId2);

        when(bibliographicalDataRepo.findAll())
                .thenReturn(List.of(bd1, bd2));

        // Act
        List<responseBibliographicalDataDTO> result =
                service.createDTOlist(service.findAll());

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(bibliographicalDataRepo).findAll();
    }
}