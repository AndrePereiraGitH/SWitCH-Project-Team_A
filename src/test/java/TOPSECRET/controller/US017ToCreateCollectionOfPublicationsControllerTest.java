package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.User;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.dto.GenreDTO_US017;
import TOPSECRET.repository.CollectionOfPublicationsRepository;
import TOPSECRET.repository.GenreRepository;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Profile;
import TOPSECRET.domain.Country;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class US017ToCreateCollectionOfPublicationsControllerTest {

    private GenreRepository genreRepository;
    private CollectionOfPublicationsRepository collectionRepository;
    private US017ToCreateCollectionOfPublicationsController controller;

    @BeforeEach
    void setup() {
        // Cria os repositórios e o controller antes de cada teste
        genreRepository = new GenreRepository(new ArrayList<>());
        collectionRepository = new CollectionOfPublicationsRepository();

        controller = new US017ToCreateCollectionOfPublicationsController(
                genreRepository,
                collectionRepository
        );
    }

    // Método auxiliar para criar um user válido
    private User createValidUser() {
        return new User(
                "validUser",
                new Contact("912345678", "351", null, null),
                new Profile("User Name", new Country("PORTUGAL"), LocalDate.of(1990, 1, 1)),
                UserRole.NORMAL_USER
        );
    }

    // Testa se o controller consegue buscar genres e convertê-los para DTOs
    @Test
    void shouldReturnAllGenresAsDTOs() {
        // Arrange
        genreRepository.createAndAddGenre("Fiction");
        genreRepository.createAndAddGenre("History");

        // Act
        List<GenreDTO_US017> result = controller.getAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Fiction", result.get(0).name);
        assertEquals("History", result.get(1).name);
    }

    // US017 - Testa criar uma coleção privada com sucesso
    @Test
    void shouldCreatePrivateCollectionSuccessfully_US017() {
        // ARRANGE
        User user = createValidUser();
        Genre genre = new Genre("Fiction");

        // ACT
        CollectionOfPublications collection =
                controller.createCollection("My Favorites", genre, user);

        // ASSERT
        assertNotNull(collection);
        assertEquals("My Favorites", collection.getCollectionName());
        assertEquals(genre, collection.getCollectionGenre());
        assertTrue(collection.getIsPrivate());
    }

    // US017 - Testa que não deixa criar duas coleções iguais
    @Test
    void shouldThrowExceptionWhenDuplicateCollectionExists_US017() {
        // ARRANGE
        User user = createValidUser();
        Genre genre = new Genre("Fiction");

        controller.createCollection("My Favorites", genre, user);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            controller.createCollection("My Favorites", genre, user);
        });
    }
}