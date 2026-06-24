package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AuthorAuthorizationPolicy;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthorService}.
 *
 * <p>This class verifies the behaviour of the application service responsible
 * for managing Author creation and persistence.</p>
 *
 * <p>The following scenarios are covered:</p>
 * <ul>
 *   <li>Constructor null checks for all required dependencies</li>
 *   <li>Successful author creation and persistence</li>
 *   <li>Access denied when the user lacks permission</li>
 *   <li>Exception propagation on repository failure</li>
 *   <li>User not found during author creation</li>
 *   <li>Retrieval of all authors</li>
 *   <li>Retrieval of a single author by identifier</li>
 *   <li>Authorization check for author creation without performing it</li>
 * </ul>
 *
 * <p>All dependencies are mocked using Mockito, isolating the service
 * from the repository, factory, and policy layers.</p>
 */
@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private IAuthorRepository authorRepository;

    @Mock
    private AuthorFactory authorFactory;

    @Mock
    private AuthorAuthorizationPolicy policy;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private AuthorService authorService;

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private User user() {
        User user = mock(User.class);
        UserId userId = mock(UserId.class);
        when(user.id()).thenReturn(userId);
        return user;
    }

    private Author author() { return mock(Author.class); }
    private LocalDate dateOfBirth() { return LocalDate.of(1922, 11, 16); }

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new AuthorService(null, authorRepository, policy, userRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenRepositoryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new AuthorService(authorFactory, null, policy, userRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenPolicyIsNull() {
        assertThrows(NullPointerException.class,
                () -> new AuthorService(authorFactory, authorRepository, null, userRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenUserRepositoryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new AuthorService(authorFactory, authorRepository, policy, null));
    }

    // =========================================================================
    // createAndSave — success
    // =========================================================================

    @Test
    void createAndSave_shouldReturnSavedAuthor() throws InstanceAlreadyExistsException, AccessDeniedException {

        // ARRANGE
        User user = user();
        Author author = author();
        Author savedAuthor = author();
        CountryId countryId = mock(CountryId.class);

        when(userRepository.findByID(user.id())).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);
        when(authorFactory.create("José Saramago", countryId, dateOfBirth(), "Biography"))
                .thenReturn(author);
        when(authorRepository.save(author)).thenReturn(savedAuthor);

        // ACT
        Author result = authorService.createAndSave(
                "José Saramago", countryId, dateOfBirth(), "Biography", user.id());

        // ASSERT
        assertSame(savedAuthor, result);
    }

    @Test
    void createAndSave_shouldCallRepositorySave_whenUserIsAuthorized() throws Exception {

        // ARRANGE
        User user = user();
        Author author = author();
        CountryId countryId = mock(CountryId.class);

        when(userRepository.findByID(user.id())).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);
        when(authorFactory.create("José Saramago", countryId, dateOfBirth(), "Biography"))
                .thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);

        // ACT
        Author result = authorService.createAndSave(
                "José Saramago", countryId, dateOfBirth(), "Biography", user.id());

        // ASSERT
        assertSame(author, result);
    }

    // =========================================================================
    // createAndSave — access denied
    // =========================================================================

    @Test
    void createAndSave_shouldThrowAccessDenied_whenUserLacksPermission() {

        // ARRANGE
        User user = user();
        CountryId countryId = mock(CountryId.class);

        when(userRepository.findByID(user.id())).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> authorService.createAndSave(
                        "José Saramago", countryId, dateOfBirth(), "Biography", user.id()));
    }

    // =========================================================================
    // createAndSave — repository error
    // =========================================================================

    @Test
    void createAndSave_shouldPropagateException_whenRepositoryThrowsOnSave() throws Exception {

        // ARRANGE
        User user = user();
        Author author = author();
        CountryId countryId = mock(CountryId.class);

        when(userRepository.findByID(user.id())).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);
        when(authorFactory.create("José Saramago", countryId, dateOfBirth(), "Biography"))
                .thenReturn(author);
        when(authorRepository.save(author)).thenThrow(new RuntimeException("DB error"));

        // ACT + ASSERT
        assertThrows(RuntimeException.class,
                () -> authorService.createAndSave(
                        "José Saramago", countryId, dateOfBirth(), "Biography", user.id()));
    }

    // =========================================================================
    // createAndSave — user not found
    // =========================================================================

    @Test
    void createAndSave_shouldThrowIllegalArgument_whenUserNotFound() {

        // ARRANGE
        CountryId countryId = mock(CountryId.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> authorService.createAndSave(
                        "José Saramago", countryId, dateOfBirth(), "Biography", user().id()));
    }

    // =========================================================================
    // findAll
    // =========================================================================

    @Test
    void findAll_shouldReturnAllAuthors() {

        // ARRANGE
        Author author1 = author();
        Author author2 = author();
        when(authorRepository.findAll()).thenReturn(List.of(author1, author2));

        // ACT
        List<Author> result = authorService.findAll();

        // ASSERT
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(author1, author2)));
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoAuthorsExist() {

        // ARRANGE
        when(authorRepository.findAll()).thenReturn(List.of());

        // ACT
        List<Author> result = authorService.findAll();

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findById — success
    // =========================================================================

    @Test
    void findById_shouldReturnAuthor_whenFound() {

        // ARRANGE
        Author author = author();
        AuthorId authorId = mock(AuthorId.class);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        // ACT
        Author result = authorService.findById(authorId);

        // ASSERT
        assertSame(author, result);
    }

    // =========================================================================
    // findById — not found
    // =========================================================================

    @Test
    void findById_shouldThrowIllegalArgument_whenAuthorNotFound() {

        // ARRANGE
        AuthorId authorId = mock(AuthorId.class);
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> authorService.findById(authorId));
    }

    @Test
    void findById_shouldThrowNullPointerException_whenAuthorIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> authorService.findById(null));
    }
    // =========================================================================
// canCreateAuthor
// =========================================================================

    @Test
    void canCreateAuthor_shouldReturnTrue_whenUserIsAuthorized() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);

        // ACT
        boolean result = authorService.canCreateAuthor(userId);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canCreateAuthor_shouldReturnFalse_whenUserIsNotAuthorized() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(false);

        // ACT
        boolean result = authorService.canCreateAuthor(userId);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canCreateAuthor_shouldThrowIllegalArgument_whenUserNotFound() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> authorService.canCreateAuthor(userId));
    }
}