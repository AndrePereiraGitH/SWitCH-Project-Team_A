package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US020_CollectionOfPublicationsDTO;
import TOPSECRET.dto.US020_CollectionOfPublicationsDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class US020PublicListByGenreControllerTest {


    // Initial setup for tests
    ICollectionOfPublicationsRepository collectionRepo;
    IUserRepository userRepo;
    US020PublicListByGenreController controller;
    IGenreRepository genreRepo;
    US020_CollectionOfPublicationsDTOMapper mapper;

    Genre genreDouble1 = mock(Genre.class);
    Genre genreDouble2 = mock(Genre.class);

    @BeforeEach
    void setup() {
        collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        genreRepo = mock(IGenreRepository.class);
        userRepo = mock(IUserRepository.class);
        mapper = mock(US020_CollectionOfPublicationsDTOMapper.class);
    }


    @Test
    void getAllGenresDelegatesToGenreRepoAndReturnsResult() {
        //Arrange
        controller = new US020PublicListByGenreController(genreRepo, collectionRepo , userRepo, mapper);
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
        // Arrange
        controller = new US020PublicListByGenreController(genreRepo, collectionRepo , userRepo, mapper);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> controller.getPublicListsByGenre(null));
    }


    @Test
    void getPublicListsByGenreDelegatesToRepoReturnsListOfCollections() {
        // Arrange
        controller = new US020PublicListByGenreController(genreRepo, collectionRepo , userRepo, mapper);

        GenreID genreId = mock(GenreID.class);

        CollectionOfPublications c1 = mock(CollectionOfPublications.class);
        CollectionOfPublications c2 = mock(CollectionOfPublications.class);

        UserId ownerId1 = mock(UserId.class);
        UserId ownerId2 = mock(UserId.class);

        User user1 = mock(User.class);
        User user2 = mock(User.class);

        List<CollectionOfPublications> collections = List.of(c1, c2);
        List<US020_CollectionOfPublicationsDTO> expectedDtos = List.of(
                mock(US020_CollectionOfPublicationsDTO.class),
                mock(US020_CollectionOfPublicationsDTO.class)
        );

        when(collectionRepo.findPublicCollectionsByGenre(genreId)).thenReturn(collections);

        when(c1.getOwnerId()).thenReturn(ownerId1);
        when(c2.getOwnerId()).thenReturn(ownerId2);

        when(userRepo.findUserByID(ownerId1)).thenReturn(Optional.of(user1));
        when(userRepo.findUserByID(ownerId2)).thenReturn(Optional.of(user2));

        when(user1.getUserName()).thenReturn("Alice");
        when(user2.getUserName()).thenReturn("Bob");

        Map<UserId, String> expectedOwnerNames = new HashMap<>();
        expectedOwnerNames.put(ownerId1, "Alice");
        expectedOwnerNames.put(ownerId2, "Bob");

        when(mapper.toDTO(collections, expectedOwnerNames)).thenReturn(expectedDtos);

        // Act
        List<US020_CollectionOfPublicationsDTO> result = controller.getPublicListsByGenre(genreId);

        // Assert
        assertEquals(expectedDtos, result);
    }


    @Test
    void shouldReturnEmptyListWhenNoListsExist()
    {
        // Arrange
        controller = new US020PublicListByGenreController(genreRepo, collectionRepo , userRepo, mapper);
        GenreID desiredGenre = mock(GenreID.class);

        // Act
        when(collectionRepo.findPublicCollectionsByGenre(desiredGenre)).thenReturn(List.of());
        List<US020_CollectionOfPublicationsDTO> result = controller.getPublicListsByGenre(desiredGenre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}