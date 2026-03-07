package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.User;
import TOPSECRET.dto.GenreDTO_US017;
import TOPSECRET.repository.CollectionOfPublicationsRepository;
import TOPSECRET.repository.GenreRepository;

import org.junit.jupiter.api.Test;

import java.util.List;

// Unit tests for US017ToCreateCollectionOfPublicationsController using mocks - US017
class US017ToCreateCollectionOfPublicationsControllerTest {


    // ============ Constructor Tests ============


    // Tests that an IllegalArgumentException is thrown when genreRepository is null
    @Test
    void shouldThrowExceptionWhenGenreRepositoryIsNull() {
        // Arrange
        CollectionOfPublicationsRepository collectionRepoDouble =
                mock(CollectionOfPublicationsRepository.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US017ToCreateCollectionOfPublicationsController(null, collectionRepoDouble));
    }

    // Tests that an IllegalArgumentException is thrown when collectionRepository is null
    @Test
    void shouldThrowExceptionWhenCollectionRepositoryIsNull() {
        // Arrange
        GenreRepository genreRepoDouble = mock(GenreRepository.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US017ToCreateCollectionOfPublicationsController(genreRepoDouble, null));
    }


    // ============ getAllGenres Tests ============


    // Tests that getAllGenres returns a DTO list matching the genres in the repository
    @Test
    void getAllGenres_shouldReturnGenresAsDTOs() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("Fiction");

        GenreRepository genreRepoDouble = mock(GenreRepository.class);
        when(genreRepoDouble.getAllGenres()).thenReturn(List.of(genreDouble));

        US017ToCreateCollectionOfPublicationsController controller =
                new US017ToCreateCollectionOfPublicationsController(
                        genreRepoDouble, mock(CollectionOfPublicationsRepository.class));

        // Act
        List<GenreDTO_US017> result = controller.getAllGenres();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Fiction", result.get(0).name);
    }

    // Tests that getAllGenres returns an empty list when the repository has no genres
    @Test
    void getAllGenres_shouldReturnEmptyListWhenNoGenres() {
        // Arrange
        GenreRepository genreRepoDouble = mock(GenreRepository.class);
        when(genreRepoDouble.getAllGenres()).thenReturn(List.of());

        US017ToCreateCollectionOfPublicationsController controller =
                new US017ToCreateCollectionOfPublicationsController(
                        genreRepoDouble, mock(CollectionOfPublicationsRepository.class));

        // Act
        List<GenreDTO_US017> result = controller.getAllGenres();

        // Assert
        assertTrue(result.isEmpty());
    }


    // ============ createCollection Tests ============


    // Tests that an IllegalArgumentException is thrown when user is null
    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        // Arrange
        Genre genreDouble = mock(Genre.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> createController().createCollection("Favorites", genreDouble, null));
    }

    // Tests that an IllegalArgumentException is thrown when genre is null
    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        // Arrange
        User userDouble = mock(User.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> createController().createCollection("Favorites", null, userDouble));
    }

    // Tests that createCollection delegates to the repository and returns the created collection
    @Test
    void shouldCreateCollectionSuccessfully() {
        // Arrange
        User userDouble = mock(User.class);
        Genre genreDouble = mock(Genre.class);
        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);

        CollectionOfPublicationsRepository collectionRepoDouble =
                mock(CollectionOfPublicationsRepository.class);
        when(collectionRepoDouble.createIfNotExists(userDouble, "Favorites", genreDouble))
                .thenReturn(collectionDouble);

        US017ToCreateCollectionOfPublicationsController controller =
                new US017ToCreateCollectionOfPublicationsController(
                        mock(GenreRepository.class), collectionRepoDouble);

        // Act
        CollectionOfPublications result =
                controller.createCollection("Favorites", genreDouble, userDouble);

        // Assert
        assertEquals(collectionDouble, result);
    }


    // Tests that the controller propagates the exception thrown by the repository when a duplicate exists
    @Test
    void shouldPropagateExceptionWhenDuplicateExists_US017() {
        // Arrange
        User userDouble = mock(User.class);
        Genre genreDouble = mock(Genre.class);

        CollectionOfPublicationsRepository repoDouble =
                mock(CollectionOfPublicationsRepository.class);

        when(repoDouble.createIfNotExists(userDouble, "Favorites", genreDouble))
                .thenThrow(new IllegalArgumentException("Collection already exists"));

        US017ToCreateCollectionOfPublicationsController controller =
                new US017ToCreateCollectionOfPublicationsController(
                        mock(GenreRepository.class), repoDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createCollection("Favorites", genreDouble, userDouble));
    }

    // -------------------------------------------------------------------------
    // Test helpers
    // -------------------------------------------------------------------------

    private US017ToCreateCollectionOfPublicationsController createController() {
        return new US017ToCreateCollectionOfPublicationsController(
                mock(GenreRepository.class),
                mock(CollectionOfPublicationsRepository.class));
    }
}
