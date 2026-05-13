package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US017GenreDTO;

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
    ICollectionOfPublicationsRepository collectionRepo;

    @Mock
    CollectionOfPublicationsFactory factory;

    @InjectMocks
    ToCreateCollectionOfPublicationsController controller;


    // ============================== Constructor ==============================

    @Test
    void shouldThrowWhenGenreRepositoryIsNull() {
        ICollectionOfPublicationsRepository collectionRepoLocal = mock(ICollectionOfPublicationsRepository.class);
        CollectionOfPublicationsFactory factoryLocal = mock(CollectionOfPublicationsFactory.class);

        assertThrows(IllegalArgumentException.class,
                () -> new ToCreateCollectionOfPublicationsController(null, collectionRepoLocal, factoryLocal));
    }

    @Test
    void shouldThrowWhenCollectionRepositoryIsNull() {
        IGenreRepository genreRepoLocal = mock(IGenreRepository.class);
        CollectionOfPublicationsFactory factoryLocal = mock(CollectionOfPublicationsFactory.class);

        assertThrows(IllegalArgumentException.class,
                () -> new ToCreateCollectionOfPublicationsController(genreRepoLocal, null, factoryLocal));
    }

    @Test
    void shouldThrowWhenFactoryIsNull() {
        IGenreRepository genreRepoLocal = mock(IGenreRepository.class);
        ICollectionOfPublicationsRepository collectionRepoLocal = mock(ICollectionOfPublicationsRepository.class);

        assertThrows(IllegalArgumentException.class,
                () -> new ToCreateCollectionOfPublicationsController(genreRepoLocal, collectionRepoLocal, null));
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

        when(factory.create("Favorites", genreId, userId)).thenReturn(collectionDouble);
        when(collectionRepo.save(collectionDouble)).thenReturn(collectionDouble);

        // Act
        CollectionOfPublications result = controller.createCollection("Favorites", genreId, userId);

        // Assert
        assertEquals(collectionDouble, result);
        verify(factory).create("Favorites", genreId, userId);
        verify(collectionRepo).save(collectionDouble);
    }

    @Test
    void shouldPropagateExceptionWhenDuplicateExists() {
        // Arrange
        UserId userId = mock(UserId.class);
        GenreId genreId = mock(GenreId.class);
        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);

        when(factory.create("Favorites", genreId, userId)).thenReturn(collectionDouble);
        when(collectionRepo.save(collectionDouble))
                .thenThrow(new IllegalArgumentException("Collection already exists"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.createCollection("Favorites", genreId, userId));
    }
}