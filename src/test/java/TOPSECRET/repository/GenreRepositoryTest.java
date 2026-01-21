package TOPSECRET.repository;

import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreRepositoryTest {

    @Test
    void shouldCreateGenre() {
        //Arrange
        Genre expected = new Genre("Sci-fi");
        //Act
        Genre result = GenreRepository.getINSTANCE().createAndAddGenre("Sci-fi");
        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldThrowWhenGenreAlreadyExists() {
        Genre expected = new Genre("repeatedName");
        //Act
        Genre result = GenreRepository.getINSTANCE().createAndAddGenre("repeatedName");
        //Assert
        assertEquals(expected, result);
    }
}