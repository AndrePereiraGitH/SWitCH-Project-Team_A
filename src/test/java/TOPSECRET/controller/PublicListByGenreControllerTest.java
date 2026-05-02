package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US013GenreDTO;
import TOPSECRET.dto.US020CollectionOfPublicationsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PublicListByGenreControllerTest {


    // Initial setup for tests
    ICollectionOfPublicationsRepository collectionRepo;
    IUserRepository userRepo;
    PublicListByGenreController controller;
    IGenreRepository genreRepo;

    Genre genreDouble1 = mock(Genre.class);
    Genre genreDouble2 = mock(Genre.class);

    @BeforeEach
    void setup() {
        collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        genreRepo = mock(IGenreRepository.class);
        userRepo = mock(IUserRepository.class);
    }


    @Test
    void getAllGenresDelegatesToGenreRepoAndReturnsListOfGenres() {
        //Arrange
        controller =new PublicListByGenreController(genreRepo, collectionRepo , userRepo);

        //Act
        when(genreRepo.getAllGenres()).thenReturn(List.of(genreDouble1, genreDouble2));
        List<US013GenreDTO> result = controller.getGenres();

        //Assert
        assertEquals(2, result.size());
    }


    @Test
    void shouldThrowWhenGenreIsNull() {
        // Arrange
        controller = new PublicListByGenreController(genreRepo, collectionRepo , userRepo);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> controller.getPublicListsByGenre(null));
    }


    @Test
    void getPublicListsByGenreDelegatesToRepoReturnsDTOs() {
        // Arrange
        controller = new PublicListByGenreController(genreRepo, collectionRepo , userRepo);

        GenreID genreId = mock(GenreID.class);
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

        when(collectionRepo.findPublicCollectionsByGenreId(genreId)).thenReturn(List.of(collection1, collection2));

        // Act
        List<US020CollectionOfPublicationsDTO> result = controller.getPublicListsByGenre(genreId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }


    @Test
    void shouldReturnEmptyListWhenNoListsExist()
    {
        // Arrange
        controller = new PublicListByGenreController(genreRepo, collectionRepo , userRepo);
        GenreID desiredGenre = mock(GenreID.class);

        // Act
        when(collectionRepo.findPublicCollectionsByGenreId(desiredGenre)).thenReturn(List.of());
        List<US020CollectionOfPublicationsDTO> result = controller.getPublicListsByGenre(desiredGenre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}