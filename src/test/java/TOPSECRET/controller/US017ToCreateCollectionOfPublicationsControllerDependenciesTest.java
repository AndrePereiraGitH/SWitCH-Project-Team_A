package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.CollectionOfPublicationsFactory;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.dto.GenreDTO_US017;
import TOPSECRET.repository.CollectionOfPublicationsRepository;
import TOPSECRET.repository.GenreRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class US017ToCreateCollectionOfPublicationsControllerDependenciesTest {

    private GenreRepository genreRepository;
    private CollectionOfPublicationsRepository collectionRepository;
    private US017ToCreateCollectionOfPublicationsController controller;

    @BeforeEach
    void setup() {
        // Cria os repositórios e o controller antes de cada teste
        genreRepository = new GenreRepository(new ArrayList<>(), new GenreFactory());
        collectionRepository = new CollectionOfPublicationsRepository(new CollectionOfPublicationsFactory());

        controller = new US017ToCreateCollectionOfPublicationsController(
                genreRepository,
                collectionRepository
        );
    }

    private User createValidUser() {

        CountryFactory countryFactory = new CountryFactory();
        Country country = countryFactory.create("PORTUGAL");

        return new User(
                "validUser",
                new Contact("912345678", "351", null, null),
                new Profile("User Name", country, LocalDate.of(1990, 1, 1)),
                UserRole.NORMAL_USER
        );
    }

    // Tests whether the controller can retrieve genres and convert them to DTOs.
    @Test
    void shouldReturnAllGenresAsDTOs() {
        // Arrange
        genreRepository.createAndAdd("Fiction");
        genreRepository.createAndAdd("History");

        // Act
        List<GenreDTO_US017> result = controller.getAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Fiction", result.get(0).name);
        assertEquals("History", result.get(1).name);
    }

    // US017 - Try creating a private collection successfully.
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

    // US017 - Test that prevents the creation of two identical collections.
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
    @Test
    void shouldThrowWhenUserIsNull() {
        GenreRepository genreRepo = new GenreRepository(new ArrayList<>(),  new GenreFactory());
        CollectionOfPublicationsRepository collectionRepo = new CollectionOfPublicationsRepository(new CollectionOfPublicationsFactory());
        US017ToCreateCollectionOfPublicationsController controller =
                new US017ToCreateCollectionOfPublicationsController(genreRepo, collectionRepo);

        Genre genre = new Genre("Fiction");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.createCollection("MyCollection", genre, null)
        );

        assertEquals("Invalid arguments", exception.getMessage());
    }

    @Test
    void shouldThrowWhenGenreIsNull() {
        GenreRepository genreRepo = new GenreRepository(new ArrayList<>(), new GenreFactory());
        CollectionOfPublicationsRepository collectionRepo = new CollectionOfPublicationsRepository(new CollectionOfPublicationsFactory());
        US017ToCreateCollectionOfPublicationsController controller =
                new US017ToCreateCollectionOfPublicationsController(genreRepo, collectionRepo);

        User user = createValidUser();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.createCollection("MyCollection", null, user)
        );

        assertEquals("Invalid arguments", exception.getMessage());
    }

    @Test
    void shouldThrowWhenGenreRepositoryIsNull() {
        CollectionOfPublicationsRepository collectionRepo = new CollectionOfPublicationsRepository(new CollectionOfPublicationsFactory());

        assertThrows(
                IllegalArgumentException.class,
                () -> new US017ToCreateCollectionOfPublicationsController(null, collectionRepo)
        );
    }

    @Test
    void shouldThrowWhenCollectionRepositoryIsNull() {
        GenreRepository genreRepo = new GenreRepository(new ArrayList<>(), new GenreFactory());

        assertThrows(
                IllegalArgumentException.class,
                () -> new US017ToCreateCollectionOfPublicationsController(genreRepo, null)
        );
    }

}