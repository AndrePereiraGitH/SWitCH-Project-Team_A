package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US013GenreDTO;
import TOPSECRET.dto.US020CollectionOfPublicationsDTO;
import TOPSECRET.services.CollectionOfPublicationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Unit tests for {@link PublicListByGenreController}.
 *
 * <p>Verifies correct retrieval of genres and public collections of publications
 * filtered by genre, including null handling and delegation to the service layer.</p>
 */
class PublicListByGenreControllerTest {


    // Initial setup for tests
    CollectionOfPublicationsService collectionService;
    IUserRepository userRepo;
    PublicListByGenreController controller;
    IGenreRepository genreRepo;

    Genre genreDouble1 = mock(Genre.class);
    Genre genreDouble2 = mock(Genre.class);

    @BeforeEach
    void setup() {
        collectionService = mock(CollectionOfPublicationsService.class);
        genreRepo = mock(IGenreRepository.class);
        userRepo = mock(IUserRepository.class);
    }


    @Test
    void getAllGenresDelegatesToGenreRepoAndReturnsListOfGenres() {
        // Arrange
        controller = new PublicListByGenreController(genreRepo, collectionService, userRepo);

        GenreId genreId1 = mock(GenreId.class);
        GenreId genreId2 = mock(GenreId.class);

        when(genreDouble1.id()).thenReturn(genreId1);
        when(genreDouble2.id()).thenReturn(genreId2);

        when(genreDouble1.getGenreType()).thenReturn("Fantasy");
        when(genreDouble2.getGenreType()).thenReturn("Sci-Fi");

        when(genreRepo.findAll()).thenReturn(List.of(genreDouble1, genreDouble2));

        // Act
        List<US013GenreDTO> result = controller.getGenres();

        // Assert
        assertEquals(2, result.size());
    }


    @Test
    void shouldThrowWhenGenreIsNull() {
        // Arrange
        controller = new PublicListByGenreController(genreRepo, collectionService, userRepo);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> controller.getPublicListsByGenre(null));
    }


    @Test
    void getPublicListsByGenreDelegatesToRepoReturnsDTOs() {
        // Arrange
        controller = new PublicListByGenreController(genreRepo, collectionService, userRepo);

        GenreId genreId = mock(GenreId.class);
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        CollectionOfPublications collection1 = mock(CollectionOfPublications.class);
        when(collection1.getCollectionName()).thenReturn("Collection 1");
        when(collection1.getOwnerId()).thenReturn(userIdDouble);

        CollectionOfPublications collection2 = mock(CollectionOfPublications.class);
        when(collection2.getCollectionName()).thenReturn("Collection 2");
        when(collection2.getOwnerId()).thenReturn(userIdDouble);

        when(userDouble.getUserName()).thenReturn("User test");

        when(userRepo.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));

        when(collectionService.findPublicCollectionsByGenreId(genreId)).thenReturn(List.of(collection1, collection2));

        // Act
        List<US020CollectionOfPublicationsDTO> result = controller.getPublicListsByGenre(genreId);

        // Assert
        assertEquals(2, result.size());
    }


    @Test
    void shouldReturnEmptyListWhenNoListsExist() {
        // Arrange
        controller = new PublicListByGenreController(genreRepo, collectionService, userRepo);
        GenreId desiredGenre = mock(GenreId.class);

        // Act
        when(collectionService.findPublicCollectionsByGenreId(desiredGenre)).thenReturn(List.of());
        List<US020CollectionOfPublicationsDTO> result = controller.getPublicListsByGenre(desiredGenre);

        // Assert
        assertTrue(result.isEmpty());
    }
}