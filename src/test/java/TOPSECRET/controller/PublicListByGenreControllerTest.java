package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.dto.US013GenreDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        // Arrange
        controller = new PublicListByGenreController(genreRepo, collectionRepo, userRepo);

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
        assertNotNull(result);
        assertEquals(2, result.size());
    }

//
//    @Test
//    void shouldThrowWhenGenreIsNull() {
//        // Arrange
//        controller = new PublicListByGenreController(genreRepo, collectionRepo , userRepo);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class, () -> controller.getPublicListsByGenre(null));
//    }
//
//
//    @Test
//    void getPublicListsByGenreDelegatesToRepoReturnsDTOs() {
//        // Arrange
//        controller = new PublicListByGenreController(genreRepo, collectionRepo , userRepo);
//
//        GenreID genreId = mock(GenreID.class);
//        UserId userIdDouble = mock(UserId.class);
//        User userDouble = mock(User.class);
//
//        CollectionOfPublications collection1 = mock(CollectionOfPublications.class);
//        when(collection1.getCollectionName()).thenReturn("Collection 1");
//        when(collection1.getOwnerId()).thenReturn(userIdDouble);
//
//        CollectionOfPublications collection2 = mock(CollectionOfPublications.class);
//        when(collection2.getCollectionName()).thenReturn("Collection 2");
//        when(collection2.getOwnerId()).thenReturn(userIdDouble);
//
//        when(userDouble.getUserName()).thenReturn("User test");
//
//        when(userRepo.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
//
//        when(collectionRepo.findPublicCollectionsByGenreId(genreId)).thenReturn(List.of(collection1, collection2));
//
//        // Act
//        List<US020CollectionOfPublicationsDTO> result = controller.getPublicListsByGenre(genreId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//    }
//
//
//    @Test
//    void shouldReturnEmptyListWhenNoListsExist()
//    {
//        // Arrange
//        controller = new PublicListByGenreController(genreRepo, collectionRepo , userRepo);
//        GenreID desiredGenre = mock(GenreID.class);
//
//        // Act
//        when(collectionRepo.findPublicCollectionsByGenreId(desiredGenre)).thenReturn(List.of());
//        List<US020CollectionOfPublicationsDTO> result = controller.getPublicListsByGenre(desiredGenre);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }

}