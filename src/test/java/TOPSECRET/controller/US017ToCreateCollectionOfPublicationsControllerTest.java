package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.GenreDTO_US017;
import TOPSECRET.dto.GenreMapper_US017;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US017ToCreateCollectionOfPublicationsController} — US017.
 *
 * <p>All dependencies are replaced by mocks so only the controller logic is tested.
 * Each test follows the Arrange / Act / Assert structure.
 */
class US017ToCreateCollectionOfPublicationsControllerTest {

    // ============================== Constructor ==============================

    @Test
    void shouldThrowWhenGenreRepositoryIsNull() {
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        GenreMapper_US017 mapper = mock(GenreMapper_US017.class);

        assertThrows(IllegalArgumentException.class,
                () -> new US017ToCreateCollectionOfPublicationsController(null, collectionRepo, mapper));
    }

    @Test
    void shouldThrowWhenCollectionRepositoryIsNull() {
        IGenreRepository genreRepo = mock(IGenreRepository.class);
        GenreMapper_US017 mapper = mock(GenreMapper_US017.class);

        assertThrows(IllegalArgumentException.class,
                () -> new US017ToCreateCollectionOfPublicationsController(genreRepo, null, mapper));
    }

    @Test
    void shouldThrowWhenMapperIsNull() {
        IGenreRepository genreRepo = mock(IGenreRepository.class);
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);

        assertThrows(IllegalArgumentException.class,
                () -> new US017ToCreateCollectionOfPublicationsController(genreRepo, collectionRepo, null));
    }


    // ============================== getAllGenres() ==============================

    @Test
    void getAllGenres_shouldReturnDTOsFromMapper() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.getGenreType()).thenReturn("Fiction");

        IGenreRepository genreRepo = mock(IGenreRepository.class);
        when(genreRepo.getAllGenres()).thenReturn(List.of(genreDouble));

        GenreDTO_US017 dtoDouble = new GenreDTO_US017("Fiction");
        GenreMapper_US017 mapper = mock(GenreMapper_US017.class);
        when(mapper.toDTOList(List.of(genreDouble))).thenReturn(List.of(dtoDouble));

        US017ToCreateCollectionOfPublicationsController controller =
                createController(genreRepo, mock(ICollectionOfPublicationsRepository.class), mapper);

        // Act
        List<GenreDTO_US017> result = controller.getAllGenres();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Fiction", result.get(0).name());
    }

    @Test
    void getAllGenres_shouldReturnEmptyListWhenNoGenresExist() {
        // Arrange
        IGenreRepository genreRepo = mock(IGenreRepository.class);
        when(genreRepo.getAllGenres()).thenReturn(List.of());

        GenreMapper_US017 mapper = mock(GenreMapper_US017.class);
        when(mapper.toDTOList(List.of())).thenReturn(List.of());

        US017ToCreateCollectionOfPublicationsController controller =
                createController(genreRepo, mock(ICollectionOfPublicationsRepository.class), mapper);

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

        US017ToCreateCollectionOfPublicationsController controller =
                createController(mock(IGenreRepository.class), collectionRepo, mock(GenreMapper_US017.class));

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

        US017ToCreateCollectionOfPublicationsController controller =
                createController(mock(IGenreRepository.class), collectionRepo, mock(GenreMapper_US017.class));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createCollection("Favorites", genreId, userId));
    }


    // ============================== helpers ==============================

    private US017ToCreateCollectionOfPublicationsController createController() {
        return createController(
                mock(IGenreRepository.class),
                mock(ICollectionOfPublicationsRepository.class),
                mock(GenreMapper_US017.class));
    }

    private US017ToCreateCollectionOfPublicationsController createController(
            IGenreRepository genreRepo,
            ICollectionOfPublicationsRepository collectionRepo,
            GenreMapper_US017 mapper) {
        return new US017ToCreateCollectionOfPublicationsController(genreRepo, collectionRepo, mapper);
    }
}
