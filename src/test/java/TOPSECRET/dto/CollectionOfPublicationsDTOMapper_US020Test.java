package TOPSECRET.dto;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CollectionOfPublicationsDTOMapper_US020Test {

    private ICollectionOfPublicationsRepository repo;
    private IUserRepository userRepo;
    private GenreID genreId;
    private CollectionOfPublications collection1;
    private CollectionOfPublications collection2;
    private UserId ownerId1;
    private UserId ownerId2;
    private User user1;
    private User user2;

    private CollectionOfPublicationsDTOMapper_US020 mapper;

    @BeforeEach
    void setUp() {
        repo = mock(ICollectionOfPublicationsRepository.class);
        userRepo = mock(IUserRepository.class);
        genreId = mock(GenreID.class);
        collection1 = mock(CollectionOfPublications.class);
        collection2 = mock(CollectionOfPublications.class);
        ownerId1 = mock(UserId.class);
        ownerId2 = mock(UserId.class);
        user1 = mock(User.class);
        user2 = mock(User.class);

        mapper =  new CollectionOfPublicationsDTOMapper_US020(repo, userRepo);
    }
    @Test
    void toDTO_shouldReturnEmptyList_whenRepositoryReturnsNoCollections() {
        when(repo.findPublicByGenre(genreId)).thenReturn(List.of());

        List<CollectionOfPublicationsDTO_US020> result = mapper.toDTO(genreId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTO_shouldMapSingleCollectionSuccessfully() {
        when(repo.findPublicByGenre(genreId)).thenReturn(List.of(collection1));
        when(collection1.getCollectionName()).thenReturn("Sci-Fi Picks");
        when(collection1.getOwnerId()).thenReturn(ownerId1);
        when(userRepo.findUserByID(ownerId1)).thenReturn(Optional.of(user1));
        when(user1.getUserName()).thenReturn("alice");

        List<CollectionOfPublicationsDTO_US020> result = mapper.toDTO(genreId);

        assertEquals(1, result.size());
        assertEquals("Sci-Fi Picks", result.get(0).getListName());
        assertEquals("alice", result.get(0).getOwnerUsername());

    }

    @Test
    void toDTO_shouldMapMultipleCollectionsSuccessfully() {
        when(repo.findPublicByGenre(genreId)).thenReturn(List.of(collection1, collection2));

        when(collection1.getCollectionName()).thenReturn("Fantasy");
        when(collection1.getOwnerId()).thenReturn(ownerId1);
        when(userRepo.findUserByID(ownerId1)).thenReturn(Optional.of(user1));
        when(user1.getUserName()).thenReturn("john");

        when(collection2.getCollectionName()).thenReturn("Mystery");
        when(collection2.getOwnerId()).thenReturn(ownerId2);
        when(userRepo.findUserByID(ownerId2)).thenReturn(Optional.of(user2));
        when(user2.getUserName()).thenReturn("mary");

        List<CollectionOfPublicationsDTO_US020> result = mapper.toDTO(genreId);

        assertEquals(2, result.size());

        assertEquals("Fantasy", result.get(0).getListName());
        assertEquals("john", result.get(0).getOwnerUsername());

        assertEquals("Mystery", result.get(1).getListName());
        assertEquals("mary", result.get(1).getOwnerUsername());
    }

    @Test
    void toDTO_shouldThrowIllegalStateException_whenOwnerUserIsNotFound() {
        when(repo.findPublicByGenre(genreId)).thenReturn(List.of(collection1));
        when(collection1.getCollectionName()).thenReturn("Horror");
        when(collection1.getOwnerId()).thenReturn(ownerId1);
        when(userRepo.findUserByID(ownerId1)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> mapper.toDTO(genreId)
        );

        assertEquals("User not found: " + ownerId1, exception.getMessage());
    }

}