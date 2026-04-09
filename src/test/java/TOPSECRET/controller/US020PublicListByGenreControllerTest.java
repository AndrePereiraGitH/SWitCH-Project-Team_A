package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US020;
import TOPSECRET.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class US020PublicListByGenreControllerTest {


    // Initial setup for tests
    ICollectionOfPublicationsRepository collectionRepo;
    IUserRepository userRepo;
    US020PublicListByGenreController controller;
    GenreRepositoryMem genreRepo;

    Genre genreDouble1 = mock(Genre.class);
    Genre genreDouble2 = mock(Genre.class);

    @BeforeEach
    void setup() {
        collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        genreRepo = mock(GenreRepositoryMem.class);
        userRepo = mock(IUserRepository.class);
        controller = new US020PublicListByGenreController(genreRepo, collectionRepo , userRepo);
    }


    @Test
    void getAllGenresDelegatesToGenreRepoAndReturnsResult() {
        //Arrange
        List<Genre> expected = List.of(genreDouble1, genreDouble2);

        //Act
        when(genreRepo.getAllGenres()).thenReturn(expected);
        List<Genre> result = controller.getGenres();

        //Assert
        assertEquals(2, result.size());
        assertEquals(expected, result);
    }


    @Test
    void shouldThrowWhenGenreIsNull() {
        assertThrows(IllegalArgumentException.class, () -> controller.getPublicListsByGenre(null));
    }


    @Test
    void getPublicListsByGenreDelegatesToMapperReturnsListOfCollections() {
        // Arrange
        GenreID desiredGenre = mock(GenreID.class);

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        UserId ownerId = mock(UserId.class);
        User user = mock(User.class);

        when(collectionRepo.findPublicByGenre(desiredGenre)).thenReturn(List.of(collection));
        when(collection.getCollectionName()).thenReturn("Collection test");
        when(collection.getOwnerId()).thenReturn(ownerId);
        when(userRepo.findUserByID(ownerId)).thenReturn(Optional.of(user));
        when(user.getUserName()).thenReturn("Alice");

        // Act
        List<CollectionOfPublicationsDTO_US020> result = controller.getPublicListsByGenre(desiredGenre);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Collection test", result.get(0).getListName());
        assertEquals("Alice", result.get(0).getOwnerUsername());
    }


    @Test
    void shouldReturnEmptyListWhenNoListsExist()
    {
        // Arrange
        GenreID desiredGenre = mock(GenreID.class);

        // Act
        when(collectionRepo.findPublicByGenre(desiredGenre)).thenReturn(List.of());
        List<CollectionOfPublicationsDTO_US020> result = controller.getPublicListsByGenre(desiredGenre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}