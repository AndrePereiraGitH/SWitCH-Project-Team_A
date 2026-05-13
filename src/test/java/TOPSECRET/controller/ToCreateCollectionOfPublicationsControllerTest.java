package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US017GenreDTO;
import TOPSECRET.services.CollectionOfPublicationsService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ToCreateCollectionOfPublicationsController} — US017.
 *
 * <p>All dependencies are replaced by mocks so only the controller logic is tested.
 * Each test follows the Arrange / Act / Assert structure.
 */
@ExtendWith(MockitoExtension.class)
class ToCreateCollectionOfPublicationsControllerTest {

    @Mock
    IGenreRepository genreRepo;

    @Mock
    CollectionOfPublicationsService collectionService;

    @InjectMocks
    ToCreateCollectionOfPublicationsController controller;


    // ============================== Constructor ==============================

    @Test
    void shouldThrowWhenGenreRepositoryIsNull() {
        CollectionOfPublicationsService serviceLocal = mock(CollectionOfPublicationsService.class);

        assertThrows(IllegalArgumentException.class,
                () -> new ToCreateCollectionOfPublicationsController(null, serviceLocal));
    }

    @Test
    void shouldThrowWhenCollectionServiceIsNull() {
        IGenreRepository genreRepoLocal = mock(IGenreRepository.class);

        assertThrows(IllegalArgumentException.class,
                () -> new ToCreateCollectionOfPublicationsController(genreRepoLocal, null));
    }


    // ============================== getAllGenres() ==============================

    @Test
    void getAllGenres_shouldReturnDTOsFromMapper() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("Fiction");
        when(genreRepo.findAll()).thenReturn(List.of(genreDouble));

        // Act
        List<US017GenreDTO> result = controller.getAllGenres();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Fiction", result.get(0).name());
    }

    @Test
    void getAllGenres_shouldReturnEmptyListWhenNoGenresExist() {
        // Arrange
        when(genreRepo.findAll()).thenReturn(List.of());

        // Act & Assert
        assertTrue(controller.getAllGenres().isEmpty());
    }


    // ============================== createCollection() ==============================

    @Test
    void shouldThrowWhenNameIsNull() {
        GenreId genreId = mock(GenreId.class);
        UserId userId = mock(UserId.class);

        assertThrows(IllegalArgumentException.class,
                () -> controller.createCollection(null, genreId, userId));
    }

    @Test
    void shouldThrowWhenUserIdIsNull() {
        GenreId genreId = mock(GenreId.class);

        assertThrows(IllegalArgumentException.class,
                () -> controller.createCollection("Favorites", genreId, null));
    }

    @Test
    void shouldThrowWhenGenreIdIsNull() {
        UserId userId = mock(UserId.class);

        assertThrows(IllegalArgumentException.class,
                () -> controller.createCollection("Favorites", null, userId));
    }

    @Test
    void shouldCreateCollectionSuccessfully() {
        // Arrange
        UserId userId = mock(UserId.class);
        GenreId genreId = mock(GenreId.class);
        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);

        when(collectionService.createAndSave("Favorites", genreId, userId)).thenReturn(collectionDouble);

        // Act
        CollectionOfPublications result = controller.createCollection("Favorites", genreId, userId);

        // Assert
        assertEquals(collectionDouble, result);
    }

    @Test
    void shouldPropagateExceptionWhenDuplicateExists() {
        // Arrange
        UserId userId = mock(UserId.class);
        GenreId genreId = mock(GenreId.class);

        when(collectionService.createAndSave("Favorites", genreId, userId))
                .thenThrow(new IllegalArgumentException("Collection already exists"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createCollection("Favorites", genreId, userId));
    }
}