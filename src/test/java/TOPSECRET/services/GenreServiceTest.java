package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.GenreAuthorizationPolicy;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceAlreadyExistsException;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GenreService}.
 *
 * <p>
 * Validates the main business rules of genre creation and retrieval,
 * including authorization checks, duplication handling, and repository interaction.
 * </p>
 *
 * <p>
 * All dependencies are mocked using Mockito and tests follow the AAA pattern.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private IGenreRepository genreRepository;

    @Mock
    private GenreFactory genreFactory;

    @Mock
    private GenreAuthorizationPolicy policy;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private GenreService genreService;

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private UserId userId() { return new UserId("user-1"); }
    private User user() { return mock(User.class); }
    private Genre genre() { return mock(Genre.class); }

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowNullPointerException_whenRepositoryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new GenreService(null, genreFactory, policy, userRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new GenreService(genreRepository, null, policy, userRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenPolicyIsNull() {
        assertThrows(NullPointerException.class,
                () -> new GenreService(genreRepository, genreFactory, null, userRepository));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenUserRepositoryIsNull() {
        assertThrows(NullPointerException.class,
                () -> new GenreService(genreRepository, genreFactory, policy, null));
    }

    // =========================================================================
    // createAndSave — success
    // =========================================================================

    @Test
    void createAndSave_shouldReturnSavedGenre() throws AccessDeniedException, InstanceAlreadyExistsException {
        // ARRANGE
        User user = user();
        Genre genre = genre();
        Genre savedGenre = genre();

        when(userRepository.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canCreateGenre(user)).thenReturn(true);
        when(genreRepository.findByTitle("Fiction")).thenReturn(Optional.empty());
        when(genreFactory.create("Fiction")).thenReturn(genre);
        when(genreRepository.save(genre)).thenReturn(savedGenre);

        // ACT
        Genre result = genreService.createAndSave("Fiction", userId());

        // ASSERT
        assertSame(savedGenre, result);
    }

    @Test
    void createAndSave_shouldCallRepositorySave_whenUserIsAdmin() throws Exception {
        // ARRANGE
        User user = user();
        Genre genre = genre();

        when(userRepository.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canCreateGenre(user)).thenReturn(true);
        when(genreRepository.findByTitle("Fiction")).thenReturn(Optional.empty());
        when(genreFactory.create("Fiction")).thenReturn(genre);
        when(genreRepository.save(genre)).thenReturn(genre);

        // ACT
        Genre result = genreService.createAndSave("Fiction", userId());

        // ASSERT
        assertSame(genre, result);
    }

    // =========================================================================
    // createAndSave — access denied
    // =========================================================================

    @Test
    void createAndSave_shouldThrowAccessDenied_whenUserIsNotAdmin() {
        // ARRANGE
        User user = user();

        when(userRepository.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canCreateGenre(user)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> genreService.createAndSave("Fiction", userId()));
    }


    // =========================================================================
    // createAndSave — genre already exists
    // =========================================================================

    @Test
    void createAndSave_shouldThrowIllegalArgument_whenGenreAlreadyExists() {
        // ARRANGE
        User user = user();

        when(userRepository.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canCreateGenre(user)).thenReturn(true);
        when(genreRepository.findByTitle("Fiction")).thenReturn(Optional.of(genre()));

        // ACT + ASSERT
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> genreService.createAndSave("Fiction", userId()));

        assertEquals("Genre with type 'Fiction' already exists", ex.getMessage());
    }

    @Test
    void createAndSave_shouldPropagateException_whenRepositoryThrowsOnSave() {
        // ARRANGE
        User user = user();
        Genre genre = genre();

        when(userRepository.findByID(userId())).thenReturn(Optional.of(user));
        when(policy.canCreateGenre(user)).thenReturn(true);
        when(genreRepository.findByTitle("Fiction")).thenReturn(Optional.empty());
        when(genreFactory.create("Fiction")).thenReturn(genre);
        when(genreRepository.save(genre)).thenThrow(new RuntimeException("DB error"));

        // ACT + ASSERT
        assertThrows(RuntimeException.class,
                () -> genreService.createAndSave("Fiction", userId()));
    }

    // =========================================================================
    // findByTitle
    // =========================================================================

    @Test
    void findByTitle_shouldReturnGenre_whenFound() {
        // ARRANGE
        Genre genre = genre();
        when(genreRepository.findByTitle("Fiction")).thenReturn(Optional.of(genre));

        // ACT
        Optional<Genre> result = genreService.findByTitle("Fiction");

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(genre, result.get());
    }

    @Test
    void findByTitle_shouldReturnEmpty_whenNotFound() {
        // ARRANGE
        when(genreRepository.findByTitle("Fiction")).thenReturn(Optional.empty());

        // ACT
        Optional<Genre> result = genreService.findByTitle("Fiction");

        // ASSERT
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findAll
    // =========================================================================

    @Test
    void findAll_shouldReturnAllGenres() {
        // ARRANGE
        Genre genre = genre();
        when(genreRepository.findAll()).thenReturn(List.of(genre));

        // ACT
        List<Genre> result = genreService.findAll();

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(genre, result.get(0));
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoGenresExist() {
        // ARRANGE
        when(genreRepository.findAll()).thenReturn(List.of());

        // ACT
        List<Genre> result = genreService.findAll();

        // ASSERT
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findById
    // =========================================================================

    @Test
    void findById_shouldReturnGenre_whenFound() {
        // ARRANGE
        GenreId genreId = new GenreId("genre-1");
        Genre genre = genre();
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));

        // ACT
        Optional<Genre> result = genreService.findById(genreId);

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(genre, result.get());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        // ARRANGE
        GenreId genreId = new GenreId("genre-1");
        when(genreRepository.findById(genreId)).thenReturn(Optional.empty());

        // ACT
        Optional<Genre> result = genreService.findById(genreId);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldThrowIllegalArgument_whenIdIsNull() {
        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> genreService.findById(null));
    }
}