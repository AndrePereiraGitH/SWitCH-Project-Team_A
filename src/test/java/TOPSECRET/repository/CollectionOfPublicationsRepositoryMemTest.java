package TOPSECRET.repository;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;

import TOPSECRET.dto.US020CollectionOfPublicationsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CollectionOfPublicationsRepositoryMem} — US017.
 *
 * <p>Verifies that the repository correctly stores collections, retrieves them by owner,
 * and rejects invalid arguments.
 */
class CollectionOfPublicationsRepositoryMemTest {

    ICollectionOfPublicationsRepository sut;
    CollectionOfPublicationsFactory factory;

    @BeforeEach
    void setUp() {
        factory = mock(CollectionOfPublicationsFactory.class);
    }

    @Test
    void shouldThrowExceptionWhenFactoryIsNull_US017() {
        assertThrows(IllegalArgumentException.class,
                () -> new CollectionOfPublicationsRepositoryMem(null));
    }

    /**
     * Ensures exception is thrown when user is null.
     */
    @Test
    void shouldThrowExceptionWhenUserIsNull() {

        // ARRANGE
        CollectionOfPublicationsRepositoryMem sut =
                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.getUserCollections(null));
    }


    /**
     * Ensures user collections are retrieved correctly.
     */
    @Test
    void shouldReturnCollectionsOwnedByUser() throws Exception {

        // Arrange
        UserId user = mock(UserId.class);
        UserId otherUser = mock(UserId.class);
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        CollectionOfPublications collection1 = mock(CollectionOfPublications.class);
        CollectionOfPublications collection2 = mock(CollectionOfPublications.class);

        when(collection1.getOwnerId()).thenReturn(user);
        when(collection2.getOwnerId()).thenReturn(otherUser);

        // Act
        // the list is private with no setter, so reflection is the only way to pre-populate it in a unit test
        Field field = CollectionOfPublicationsRepositoryMem.class
                .getDeclaredField("_collectionOfPublicationsList");
        field.setAccessible(true);

        List<CollectionOfPublications> list =
                (List<CollectionOfPublications>) field.get(sut);

        list.add(collection1);
        list.add(collection2);

        // ACT
        List<CollectionOfPublications> result =
                sut.getUserCollections(user);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(collection1, result.get(0));
    }


    // ==== US020 tests ====

    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        // Arrange
        sut = new CollectionOfPublicationsRepositoryMem(factory);
        IUserRepository userRepo = mock(IUserRepository.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.findPublicCollectionsByGenreId(null, userRepo));
    }


    @Test
    void shouldReturnAllPublicCollectionsOfAGivenGenre() {
        // Arrange
        GenreID desiredGenre = mock(GenreID.class);
        UserId userId = mock(UserId.class);
        IUserRepository userRepo = mock(IUserRepository.class);
        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
        User user = mock(User.class);

        sut = new CollectionOfPublicationsRepositoryMem(factory);

        when(factory.create("Collection test", desiredGenre, userId)).thenReturn(collectionDouble);
        when(collectionDouble.getIsPrivate()).thenReturn(false);
        when(collectionDouble.getCollectionGenre()).thenReturn(desiredGenre);
        when(collectionDouble.getOwnerId()).thenReturn(userId);
        when(collectionDouble.getCollectionName()).thenReturn("Collection test");

        when(userRepo.findUserByID(userId)).thenReturn(Optional.of(user));
        when(user.getUserName()).thenReturn("Owner test");

        // Act
        sut.createAndAdd(userId, "Collection test", desiredGenre);
        List<US020CollectionOfPublicationsDTO> result = sut.findPublicCollectionsByGenreId(desiredGenre, userRepo);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Collection test", result.get(0).collectionName());
        assertEquals("Owner test", result.get(0).userName());
    }


    @Test
    void shouldReturnEmptyListPublicWhenNoCollectionsExist() {
        // Arrange
        GenreID desiredGenres = mock(GenreID.class);
        IUserRepository userRepo = mock(IUserRepository.class);
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        // Act
        List<US020CollectionOfPublicationsDTO> result = sut.findPublicCollectionsByGenreId(desiredGenres, userRepo);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}