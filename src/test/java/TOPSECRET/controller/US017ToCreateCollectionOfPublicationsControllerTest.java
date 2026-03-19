package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.User;
import TOPSECRET.dto.GenreDTO_US017;
import TOPSECRET.repository.CollectionOfPublicationsRepository;
import TOPSECRET.repository.GenreRepositoryMem;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Unit tests for {@link US017ToCreateCollectionOfPublicationsController} — US017.
 *
 * <p>These tests use <b>mocks</b> (fake objects) instead of real repositories.
 * A mock is an object that pretends to be the real thing but only does what we tell it to.
 * We use the Mockito library to create mocks and control what they return.
 *
 * <p>Because everything around the controller is fake, if a test fails here,
 * the bug must be inside the controller — not in the repository or the domain class.
 * This is the main advantage of unit tests with mocks: they isolate the class being tested.
 *
 * <p>Each test follows the Arrange / Act / Assert structure:
 * <ul>
 *   <li><b>Arrange</b> — set up the mocks and the controller</li>
 *   <li><b>Act</b> — call the method being tested</li>
 *   <li><b>Assert</b> — check that the result is correct</li>
 * </ul>
 */
class US017ToCreateCollectionOfPublicationsControllerTest {


    // ============================== US017 ==============================


    // --- Constructor tests ---

    // The controller needs both repositories to work.
    // If the genre repository is missing (null), the controller cannot give genres to the user.
    // So the constructor must throw an exception right away — not later when a method is called.
    @Test
    void shouldThrowExceptionWhenGenreRepositoryIsNull() {
        // Arrange — only provide the collection repo; genre repo is null on purpose
        CollectionOfPublicationsRepository collectionRepoDouble =
                mock(CollectionOfPublicationsRepository.class);

        // Act & Assert — passing null must fail at construction time
        assertThrows(IllegalArgumentException.class,
                () -> new US017ToCreateCollectionOfPublicationsController(null, collectionRepoDouble));
    }

    // Same idea as above, but for the collection repository.
    // Without it the controller cannot create or save collections.
    // Both dependencies are required — missing either one must throw.
    @Test
    void shouldThrowExceptionWhenCollectionRepositoryIsNull() {
        // Arrange — only provide the genre repo; collection repo is null on purpose
        GenreRepositoryMem genreRepoDouble = mock(GenreRepositoryMem.class);

        // Act & Assert — passing null must fail at construction time
        assertThrows(IllegalArgumentException.class,
                () -> new US017ToCreateCollectionOfPublicationsController(genreRepoDouble, null));
    }


    // --- getAllGenres tests ---

    // The controller asks the genre repository for all genres,
    // then converts each Genre into a GenreDTO_US017 before giving them to the UI.
    // We use a mock Genre that returns "Fiction" when asked for its type.
    // This way we control the input and check that the controller converts it correctly.
    // If the DTO has the right name, the controller did its job — it converted, not just returned.
    @Test
    void getAllGenres_shouldReturnGenresAsDTOs() {
        // Arrange — fake genre that returns "Fiction", fake repo that returns that genre
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("Fiction");

        GenreRepositoryMem genreRepoDouble = mock(GenreRepositoryMem.class);
        when(genreRepoDouble.getAllGenres()).thenReturn(List.of(genreDouble));

        US017ToCreateCollectionOfPublicationsController controller =
                new US017ToCreateCollectionOfPublicationsController(
                        genreRepoDouble, mock(CollectionOfPublicationsRepository.class));

        // Act — controller fetches genres and converts them to DTOs
        List<GenreDTO_US017> result = controller.getAllGenres();

        // Assert — one DTO returned with the correct name
        assertEquals(1, result.size());
        assertEquals("Fiction", result.get(0).name);
    }

    // When the repository has no genres, the controller must return an empty list.
    // It must NOT return null — that would crash the UI.
    // The mock repo returns an empty list, so we can check the controller handles it safely.
    @Test
    void getAllGenres_shouldReturnEmptyListWhenNoGenres() {
        // Arrange — fake repo that has no genres
        GenreRepositoryMem genreRepoDouble = mock(GenreRepositoryMem.class);
        when(genreRepoDouble.getAllGenres()).thenReturn(List.of());

        US017ToCreateCollectionOfPublicationsController controller =
                new US017ToCreateCollectionOfPublicationsController(
                        genreRepoDouble, mock(CollectionOfPublicationsRepository.class));

        // Act — controller gets genres (none exist)
        List<GenreDTO_US017> result = controller.getAllGenres();

        // Assert — result is empty, not null
        assertTrue(result.isEmpty());
    }


    // --- createCollection tests ---

    // The controller must check that the user is not null before calling the repository.
    // If user is null, the collection has no owner — that makes no sense.
    // The controller catches this early so the repository does not receive bad data.
    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        // Arrange — valid genre, but no user
        Genre genreDouble = mock(Genre.class);

        // Act & Assert — null user must be rejected before reaching the repo
        assertThrows(IllegalArgumentException.class,
                () -> createController().createCollection("Favorites", genreDouble, null));
    }

    // Same idea as above, but for genre.
    // A collection without a genre is not valid — US017 requires a genre to be chosen.
    // The controller must stop here and not forward the null to the repository.
    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        // Arrange — valid user, but no genre
        User userDouble = mock(User.class);

        // Act & Assert — null genre must be rejected before reaching the repo
        assertThrows(IllegalArgumentException.class,
                () -> createController().createCollection("Favorites", null, userDouble));
    }

    // The controller does not create collections itself — it tells the repository to do it.
    // This test checks that the controller passes the right data to the repository
    // and then returns exactly what the repository gives back.
    // We use thenReturn so the mock repo gives back a known collection object.
    // If the controller returns that same object, we know it delegated correctly.
    @Test
    void shouldCreateCollectionSuccessfully() {
        // Arrange — set up fake objects and tell the mock repo what to return
        User userDouble = mock(User.class);
        Genre genreDouble = mock(Genre.class);
        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);

        CollectionOfPublicationsRepository collectionRepoDouble =
                mock(CollectionOfPublicationsRepository.class);
        when(collectionRepoDouble.createIfNotExists(userDouble, "Favorites", genreDouble))
                .thenReturn(collectionDouble);

        US017ToCreateCollectionOfPublicationsController controller =
                new US017ToCreateCollectionOfPublicationsController(
                        mock(GenreRepositoryMem.class), collectionRepoDouble);

        // Act — controller calls the repo and returns its result
        CollectionOfPublications result =
                controller.createCollection("Favorites", genreDouble, userDouble);

        // Assert — the returned object is exactly what the repo gave back
        assertEquals(collectionDouble, result);
    }

    // US017 rule: a user cannot have two collections with the same name and genre.
    // The duplicate check is done by the repository — not by the controller.
    // We use thenThrow to make the mock repo act as if a duplicate was found.
    // The test checks that the controller does NOT catch this exception — it must let it go up.
    // This way the UI or the caller knows the creation failed.
    @Test
    void shouldPropagateExceptionWhenDuplicateExists_US017() {
        // Arrange — mock repo is told to throw when createIfNotExists is called
        User userDouble = mock(User.class);
        Genre genreDouble = mock(Genre.class);

        CollectionOfPublicationsRepository repoDouble =
                mock(CollectionOfPublicationsRepository.class);

        when(repoDouble.createIfNotExists(userDouble, "Favorites", genreDouble))
                .thenThrow(new IllegalArgumentException("Collection already exists"));

        US017ToCreateCollectionOfPublicationsController controller =
                new US017ToCreateCollectionOfPublicationsController(
                        mock(GenreRepositoryMem.class), repoDouble);

        // Act & Assert — the exception from the repo must reach the caller
        assertThrows(IllegalArgumentException.class,
                () -> controller.createCollection("Favorites", genreDouble, userDouble));
    }


    // ============================== end US017 ==============================


    // --- Test helper ---

    // Helper method used by tests that just need a valid controller with no special repo behaviour.
    // Both repos are plain mocks — they do nothing by default.
    // This avoids repeating the same setup code in every test.
    private US017ToCreateCollectionOfPublicationsController createController() {
        return new US017ToCreateCollectionOfPublicationsController(
                mock(GenreRepositoryMem.class),
                mock(CollectionOfPublicationsRepository.class));
    }
}