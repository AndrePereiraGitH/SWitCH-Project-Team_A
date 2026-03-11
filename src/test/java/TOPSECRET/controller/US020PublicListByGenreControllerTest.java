package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US020;
import TOPSECRET.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US020PublicListByGenreController}.
 * Has {@link GenreRepository} and {@link CollectionOfPublicationsRepository} as dependencies to be mocked
 */

class US020PublicListByGenreControllerTest {

    /**
     * Initial setup for tests
     * Creates mocks for {@link CollectionOfPublicationsRepository}. and {@link GenreRepository}.
     */
    CollectionOfPublicationsRepository collectionRepo;
    US020PublicListByGenreController controller;
    GenreRepository genreRepo;

    @BeforeEach
    void setup() {
        collectionRepo = mock(CollectionOfPublicationsRepository.class);
        genreRepo = mock(GenreRepository.class);
        controller = new US020PublicListByGenreController(genreRepo, collectionRepo);
    }

    /**
     * Ensures genre repo returns all genres
     */
    @Test
    void getAllGenresDelegatesToGenreRepoAndReturnsResult() {

        List<Genre> expected = List.of(new GenreFactory().create("Action"), new GenreFactory().create("Drama"));

        when(genreRepo.getAllGenres()).thenReturn(expected);

        List<Genre> result = controller.getGenres();

        assertSame(expected, result);
        verify(genreRepo).getAllGenres();
    }

    /**
     * If genre is null, controller should throw exception
     */
    @Test
    void shouldThrowWhenGenreIsNull()
    {
        assertThrows(IllegalArgumentException.class, () -> controller.getPublicListsByGenre(null)); // Checks if exception is thrown when genre is null
    }

    /**
     * Controller delegates to collection repo, and should return list
     */
    @Test
    void getPublicListsByGenreDelegatesToCollectionRepoAndReturnsResult()
    {
        Genre desiredGenre = new GenreFactory().create("Action");

        List<CollectionOfPublicationsDTO_US020> expected =
                List.of(mock(CollectionOfPublicationsDTO_US020.class));

        when(collectionRepo.findPublicByGenre(desiredGenre)).thenReturn(expected);

        List<CollectionOfPublicationsDTO_US020> result = controller.getPublicListsByGenre(desiredGenre);

        assertEquals(expected, result);
    }

    /**
     * Should return empty list when no public lists exist in collection repo
     */
    @Test
    void shouldReturnEmptyListWhenNoListsExist()
    {
        Genre desiredGenre = new GenreFactory().create("Sci-Fi");

        when(collectionRepo.findPublicByGenre(desiredGenre)).thenReturn(List.of());

        List<CollectionOfPublicationsDTO_US020> result = controller.getPublicListsByGenre(desiredGenre);

        assertTrue(result.isEmpty());
    }

}