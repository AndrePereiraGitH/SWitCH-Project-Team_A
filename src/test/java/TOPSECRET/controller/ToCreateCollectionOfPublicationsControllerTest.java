package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US017GenreDTO;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ToCreateCollectionOfPublicationsController} — US017.
 *
 * <p>All dependencies are replaced by mocks so only the controller logic is tested.
 * Each test follows the Arrange / Act / Assert structure.
 */
class ToCreateCollectionOfPublicationsControllerTest {

    // ============================== Constructor ==============================

    @Test
    void shouldThrowWhenGenreRepositoryIsNull() {
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);

        assertThrows(IllegalArgumentException.class,
                () -> new ToCreateCollectionOfPublicationsController(null, collectionRepo));
    }

    @Test
    void shouldThrowWhenCollectionRepositoryIsNull() {
        IGenreRepository genreRepo = mock(IGenreRepository.class);

        assertThrows(IllegalArgumentException.class,
                () -> new ToCreateCollectionOfPublicationsController(genreRepo, null));
    }


    // ============================== getAllGenres() ==============================

    @Test
    void getAllGenres_shouldReturnDTOsFromMapper() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("Fiction");

        IGenreRepository genreRepo = mock(IGenreRepository.class);
        when(genreRepo.getAllGenres()).thenReturn(List.of(genreDouble));

        ToCreateCollectionOfPublicationsController controller =
                createController(genreRepo, mock(ICollectionOfPublicationsRepository.class));

        // Act
        List<US017GenreDTO> result = controller.getAllGenres();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Fiction", result.get(0).name());
    }

    @Test
    void getAllGenres_shouldReturnEmptyListWhenNoGenresExist() {
        // Arrange
        IGenreRepository genreRepo = mock(IGenreRepository.class);
        when(genreRepo.getAllGenres()).thenReturn(List.of());

        ToCreateCollectionOfPublicationsController controller =
                createController(genreRepo, mock(ICollectionOfPublicationsRepository.class));

        // Act & Assert
        assertTrue(controller.getAllGenres().isEmpty());
    }


    // ============================== createCollection() ==============================

    @Test
    void shouldThrowWhenUserIdIsNull() {
        GenreID genreId = mock(GenreID.class);

        assertThrows(IllegalArgumentException.class,
                () -> createController().createCollection("Favorites", genreId, null));
    }

    @Test
    void shouldThrowWhenGenreIdIsNull() {
        UserId userId = mock(UserId.class);

        assertThrows(IllegalArgumentException.class,
                () -> createController().createCollection("Favorites", null, userId));
    }

    @Test
    void shouldCreateCollectionSuccessfully() {
        // Arrange
        UserId userId = mock(UserId.class);
        GenreID genreId = mock(GenreID.class);

        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        when(collectionRepo.createAndAdd(userId, "Favorites", genreId)).thenReturn(collectionDouble);

        ToCreateCollectionOfPublicationsController controller =
                createController(mock(IGenreRepository.class), collectionRepo);

        // Act
        CollectionOfPublications result = controller.createCollection("Favorites", genreId, userId);

        // Assert
        assertEquals(collectionDouble, result);
    }

    @Test
    void shouldPropagateExceptionWhenDuplicateExists() {
        // Arrange
        UserId userId = mock(UserId.class);
        GenreID genreId = mock(GenreID.class);

        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        when(collectionRepo.createAndAdd(userId, "Favorites", genreId))
                .thenThrow(new IllegalArgumentException("Collection already exists"));

        ToCreateCollectionOfPublicationsController controller =
                createController(mock(IGenreRepository.class), collectionRepo);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createCollection("Favorites", genreId, userId));
    }


    // ============================== helpers ==============================

    private ToCreateCollectionOfPublicationsController createController() {
        return createController(
                mock(IGenreRepository.class),
                mock(ICollectionOfPublicationsRepository.class));
    }

    private ToCreateCollectionOfPublicationsController createController(
            IGenreRepository genreRepo,
            ICollectionOfPublicationsRepository collectionRepo) {
        return new ToCreateCollectionOfPublicationsController(genreRepo, collectionRepo);
    }
}
