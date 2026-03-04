package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.CollectionOfPublicationsDTO;
import TOPSECRET.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US020PublicListByGenreControllerTest {

    CollectionOfPublicationsRepository collectionRepo;
    US020PublicListByGenreController controller;
    GenreRepository genreRepo = new GenreRepository(new ArrayList<>(), new GenreFactory());

    @BeforeEach
    void setup() {
        collectionRepo = mock(CollectionOfPublicationsRepository.class);
        controller = new US020PublicListByGenreController(genreRepo, collectionRepo);
    }

    @Test
    void shouldReturnAllGenresFromGenreRepository()
    {
        Genre genre1 = genreRepo.createAndAddGenre("Sci-Fi");
        Genre genre2 = genreRepo.createAndAddGenre("Action");
        Genre genre3 = genreRepo.createAndAddGenre("History");

        List<Genre> genreList = controller.getGenres();

        assertNotNull(genreList);
        assertEquals(3, genreList.size());
        assertEquals(genre1, genreList.get(0));
        assertEquals(genre2, genreList.get(1));
        assertEquals(genre3, genreList.get(2));
    }

    @Test
    void shouldThrownWhenGenreIsNull()
    {
        assertThrows(IllegalArgumentException.class, () -> controller.getPublicListsByGenre(null)); // Checks if exception is thrown when genre is null
    }

    @Test
    void getPublicListsByGenre_delegatesToCollectionRepo_andReturnsResult()
    {
        Genre desiredGenre = new Genre("Action");

        List<CollectionOfPublicationsDTO> expected =
                List.of(mock(CollectionOfPublicationsDTO.class));

        when(collectionRepo.findPublicByGenre(desiredGenre)).thenReturn(expected);

        List<CollectionOfPublicationsDTO> result = controller.getPublicListsByGenre(desiredGenre);

        assertEquals(expected, result);
    }

}