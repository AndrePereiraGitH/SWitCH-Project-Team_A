package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.GenreFactory;
import TOPSECRET.dto.GenreDTO_US013;
import TOPSECRET.dto.GenreMapper_US013;
import TOPSECRET.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToGetListOfGenreControllerTest {
    // ========================================
    // CONSTRUCTOR TESTS
    // ========================================

    @Test
    void shouldCreateControllerWithValidRepositoryAndMapper() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>(), new GenreFactory());
        GenreMapper_US013 mapper = new GenreMapper_US013();

        // Act
        ToGetListOfGenreController controller = new ToGetListOfGenreController(repository, mapper);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void shouldThrowExceptionWhenRepositoryIsNull() {
        // Arrange
        GenreMapper_US013 mapper = new GenreMapper_US013();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new ToGetListOfGenreController(null, mapper)
        );
        assertEquals("Genre Repository can't be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMapperIsNull() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>(),  new GenreFactory());

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new ToGetListOfGenreController(repository, null)
        );
        assertEquals("Genre Mapper can't be null", exception.getMessage());
    }

    // ========================================
    // GET GENRES LIST TESTS
    // ========================================

    @Test
    void shouldReturnEmptyListWhenNoGenres() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>(),  new GenreFactory());
        GenreMapper_US013 mapper = new GenreMapper_US013();
        ToGetListOfGenreController controller = new ToGetListOfGenreController(repository, mapper);

        // Act
        List<GenreDTO_US013> result = controller.getGenresList();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnGenreDTOsFromRepository() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>(), new GenreFactory());
        repository.createAndAddGenre("Romance");
        repository.createAndAddGenre("Horror");

        GenreMapper_US013 mapper = new GenreMapper_US013();
        ToGetListOfGenreController controller = new ToGetListOfGenreController(repository, mapper);

        // Act
        List<GenreDTO_US013> result = controller.getGenresList();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Romance", result.get(0).genreName);
        assertEquals("Horror", result.get(1).genreName);
    }

    @Test
    void shouldReturnDTOsWithSameContentAsRepository() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>(), new GenreFactory());
        repository.createAndAddGenre("Mystery");
        repository.createAndAddGenre("Thriller");

        GenreMapper_US013 mapper = new GenreMapper_US013();
        ToGetListOfGenreController controller = new ToGetListOfGenreController(repository, mapper);

        // Act
        List<GenreDTO_US013> controllerResult = controller.getGenresList();
        List<Genre> repositoryGenres = repository.getAllGenres();

        // Assert - same number and content
        assertEquals(repositoryGenres.size(), controllerResult.size());
        assertEquals("Mystery", controllerResult.get(0).genreName);
        assertEquals("Thriller", controllerResult.get(1).genreName);
    }

    @Test
    void shouldMapGenresToDTOsCorrectly() {
        // Arrange
        GenreRepository repository = new GenreRepository(new ArrayList<>(), new GenreFactory());
        repository.createAndAddGenre("Action");
        repository.createAndAddGenre("Comedy");
        repository.createAndAddGenre("Drama");

        GenreMapper_US013 mapper = new GenreMapper_US013();
        ToGetListOfGenreController controller = new ToGetListOfGenreController(repository, mapper);

        // Act
        List<GenreDTO_US013> result = controller.getGenresList();

        // Assert
        assertEquals(3, result.size());

        // Verify each DTO has the correct genreName
        List<String> genreNames = result.stream()
                .map(dto -> dto.genreName)
                .toList();

        assertTrue(genreNames.contains("Action"));
        assertTrue(genreNames.contains("Comedy"));
        assertTrue(genreNames.contains("Drama"));
    }
}