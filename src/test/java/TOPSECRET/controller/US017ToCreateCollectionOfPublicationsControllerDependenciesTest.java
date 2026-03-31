package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.CollectionOfPublicationsFactory;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.CountryFactory;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.GenreFactory;
import TOPSECRET.dto.GenreDTO_US017;
import TOPSECRET.repository.ICollectionOfPublicationsRepository;
import TOPSECRET.repository.CollectionOfPublicationsRepositoryMem;
import TOPSECRET.repository.GenreRepositoryMem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link US017ToCreateCollectionOfPublicationsController} — US017.
 *
 * <p>Unlike the unit test file (which uses mocks), these tests use <b>real objects</b>:
 * a real genre repository, a real collection repository, and a real controller.
 * This means the tests check that all the parts work correctly together, not just in isolation.
 *
 * <p>If a test fails here, the bug could be in the controller, the repository, or the domain class —
 * because all of them are running for real.
 *
 * <p>{@code @BeforeEach} creates fresh objects before every test, so one test cannot
 * affect another (no shared state between tests).
 */
class US017ToCreateCollectionOfPublicationsControllerDependenciesTest {

    // shared between tests — reset before each test by @BeforeEach
    private GenreRepositoryMem genreRepository;
    private ICollectionOfPublicationsRepository collectionRepository;
    private US017ToCreateCollectionOfPublicationsController controller;

    // runs before each test — builds real objects so tests work with the full system, not mocks
    @BeforeEach
    void setup() {
        genreRepository = new GenreRepositoryMem( new GenreFactory());
        collectionRepository = new CollectionOfPublicationsRepositoryMem(new CollectionOfPublicationsFactory());

        controller = new US017ToCreateCollectionOfPublicationsController(
                genreRepository,
                collectionRepository
        );
    }

    // helper method — builds a valid User so we don't repeat this setup in every test
    private User createValidUser() {
        CountryFactory countryFactory = new CountryFactory();
        Country country = countryFactory.create("PORTUGAL");

        return UserFactory.create(
                "validUser",
                new Contact("912345678", "351", null, null),
                new Profile("User Name", country, LocalDate.of(1990, 1, 1)),
                UserRole.NORMAL_USER
        );
    }


    // ============================== US017 ==============================

    // The controller must fetch genres from the repository and return them as DTOs, not as domain objects.
    // We add two real genres to the repo and check that the controller converts both correctly.
    // If the DTO names match, the full chain — repo → controller → DTO — is working.
    @Test
    void shouldReturnAllGenresAsDTOs() {
        // Arrange — add two real genres directly to the real repo
        genreRepository.createAndAdd("Fiction");
        genreRepository.createAndAdd("History");

        // Act — controller fetches and converts them to DTOs
        List<GenreDTO_US017> result = controller.getAllGenres();

        // Assert — two DTOs returned with the correct names
        assertEquals(2, result.size());
        assertEquals("Fiction", result.get(0).name);
        assertEquals("History", result.get(1).name);
    }

    // US017 happy path: the user picks a name and genre, and the controller creates the collection.
    // We check that the collection was created with the right data and that it starts as private.
    // Starting as private is a US017 rule — the user did not ask for it to be public.
    @Test
    void shouldCreatePrivateCollectionSuccessfully_US017() {
        // Arrange — a real user and a real genre
        User user = createValidUser();
        Genre genre = new GenreFactory().create("Fiction");

        // Act — controller asks the repository to create the collection
        CollectionOfPublications collection =
                controller.createCollection("My Favorites", genre, user);

        // Assert — collection was created with the right data and is private by default
        assertNotNull(collection);
        assertEquals("My Favorites", collection.getCollectionName());
        assertEquals(genre, collection.getCollectionGenre());
        assertTrue(collection.getIsPrivate()); // US017 rule: always starts private
    }

    // US017 rule: a user cannot have two collections with the same name and genre.
    // The first creation succeeds. The second one must fail because it is a duplicate.
    // The duplicate check is done by the repository — the controller just lets the exception go up.
    @Test
    void shouldThrowExceptionWhenDuplicateCollectionExists_US017() {
        // Arrange — first collection is created successfully
        User user = createValidUser();
        Genre genre = new GenreFactory().create("Fiction");
        controller.createCollection("My Favorites", genre, user);

        // Act & Assert — second creation with the same data must throw
        assertThrows(IllegalArgumentException.class, () -> {
            controller.createCollection("My Favorites", genre, user);
        });
    }

    // The controller must reject a null user before calling the repository.
    // A collection without an owner makes no sense — the controller catches this early.
    // We also check the exact message so we know the right exception was thrown.
    @Test
    void shouldThrowWhenUserIsNull() {
        // Arrange — valid genre, but no user
        GenreRepositoryMem genreRepo = new GenreRepositoryMem(new GenreFactory());
        ICollectionOfPublicationsRepository collectionRepo = new CollectionOfPublicationsRepositoryMem(new CollectionOfPublicationsFactory());
        US017ToCreateCollectionOfPublicationsController controller =
                new US017ToCreateCollectionOfPublicationsController(genreRepo, collectionRepo);
        Genre genre = new GenreFactory().create("Fiction");

        // Act & Assert — null user must be rejected with the correct message
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.createCollection("MyCollection", genre, null)
        );
        assertEquals("Invalid arguments", exception.getMessage());
    }

    // The controller must also reject a null genre before calling the repository.
    // US017 requires the user to choose a genre — without it the collection cannot be created.
    // We also check the exact message so we know the right exception was thrown.
    @Test
    void shouldThrowWhenGenreIsNull() {
        // Arrange — valid user, but no genre
        GenreRepositoryMem genreRepo = new GenreRepositoryMem(new GenreFactory());
        ICollectionOfPublicationsRepository collectionRepo = new CollectionOfPublicationsRepositoryMem(new CollectionOfPublicationsFactory());
        US017ToCreateCollectionOfPublicationsController controller =
                new US017ToCreateCollectionOfPublicationsController(genreRepo, collectionRepo);
        User user = createValidUser();

        // Act & Assert — null genre must be rejected with the correct message
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.createCollection("MyCollection", null, user)
        );
        assertEquals("Invalid arguments", exception.getMessage());
    }

    // The controller needs both repositories to work.
    // If the genre repository is null, the controller cannot give genres to the user.
    // The constructor must fail immediately — not later when a method is called.
    @Test
    void shouldThrowWhenGenreRepositoryIsNull() {
        // Arrange — only provide the collection repo; genre repo is null on purpose
        ICollectionOfPublicationsRepository collectionRepo = new CollectionOfPublicationsRepositoryMem(new CollectionOfPublicationsFactory());

        // Act & Assert — null genre repo must fail at construction time
        assertThrows(
                IllegalArgumentException.class,
                () -> new US017ToCreateCollectionOfPublicationsController(null, collectionRepo)
        );
    }

    // Same idea — the collection repository is also required.
    // Without it the controller cannot create or save collections.
    // The constructor must fail immediately when it is missing.
    @Test
    void shouldThrowWhenCollectionRepositoryIsNull() {
        // Arrange — only provide the genre repo; collection repo is null on purpose
        GenreRepositoryMem genreRepo = new GenreRepositoryMem(new GenreFactory());

        // Act & Assert — null collection repo must fail at construction time
        assertThrows(
                IllegalArgumentException.class,
                () -> new US017ToCreateCollectionOfPublicationsController(genreRepo, null)
        );
    }

    // ============================== end US017 ==============================

}