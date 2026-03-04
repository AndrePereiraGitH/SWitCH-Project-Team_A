package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.GenreFactory;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class US003ToRegisterGenreControllerTest {

    @Test
    void registerValidGenre() {

        // arrange
        GenreRepository genreRepository = new GenreRepository(new ArrayList<>(), new GenreFactory());
        US003ToRegisterGenreController controller = new US003ToRegisterGenreController(genreRepository);

        String genreType = "Drama";

        // act
        Genre genre = controller.registerGenre(genreType);

        // assert
        assertNotNull(genre);
        assertEquals("Drama", genre.getGenreType());
    }

    @Test
    void registerDuplicateGenre() {
        // arrange
        GenreRepository genreRepository = new GenreRepository(new ArrayList<>(), new GenreFactory());
        US003ToRegisterGenreController controller =
                new US003ToRegisterGenreController(genreRepository);

        controller.registerGenre("Science");

        // act + assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.registerGenre("Science")
        );
    }

}
