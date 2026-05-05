package TOPSECRET.persistence.mem;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CollectionOfPublicationsRepositoryMem} class.
 *
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
    void ensureThrowsExceptionWhenFactoryIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CollectionOfPublicationsRepositoryMem(null));
    }

    @Test
    void ensureRepositoryIsCreatedSuccessfullyWithValidFactory() {
        // Arrange
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);

        // Act & Assert
        assertDoesNotThrow(() -> new CollectionOfPublicationsRepositoryMem(factory));
    }


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
        assertThrows(IllegalArgumentException.class, () -> sut.findPublicCollectionsByGenreId(null));
    }


    @Test
    void shouldReturnAllPublicCollectionsOfAGivenGenre() {
        // Arrange
        GenreId desiredGenre = mock(GenreId.class);
        UserId userDouble = mock(UserId.class);

        CollectionOfPublications c1 = mock(CollectionOfPublications.class);
        CollectionOfPublications c2 = mock(CollectionOfPublications.class);

        when(factory.create("Collection 1 name", desiredGenre, userDouble)).thenReturn(c1);
        when(factory.create("Collection 2 name", desiredGenre, userDouble)).thenReturn(c2);

        when(c1.getOwnerId()).thenReturn(userDouble);
        when(c1.getCollectionName()).thenReturn("Collection 1 name");
        when(c1.getCollectionGenre()).thenReturn(desiredGenre);
        when(c1.getIsPrivate()).thenReturn(false);

        when(c2.getOwnerId()).thenReturn(userDouble);
        when(c2.getCollectionName()).thenReturn("Collection 2 name");
        when(c2.getCollectionGenre()).thenReturn(desiredGenre);
        when(c2.getIsPrivate()).thenReturn(false);

        sut = new CollectionOfPublicationsRepositoryMem(factory);

        // Act
        sut.createAndAdd(userDouble, "Collection 1 name", desiredGenre);
        sut.createAndAdd(userDouble, "Collection 2 name", desiredGenre);

        List<CollectionOfPublications> result = sut.findPublicCollectionsByGenreId(desiredGenre);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(c1, result.get(0));
        assertEquals(c2, result.get(1));
    }

    @Test
    void shouldReturnEmptyListPublicWhenNoCollectionsExist() {
        // Arrange
        GenreId desiredGenres = mock(GenreId.class);
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        // Act
        List<CollectionOfPublications> result = sut.findPublicCollectionsByGenreId(desiredGenres);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void shouldThrowWhenUserIdIsNullForCreateAndAdd() {

        //Arrange
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> sut.createAndAdd(null, "name", mock(GenreId.class)));
    }

    @Test
    void shouldThrowWhenNameIsNullForCreateAndAdd() {

        //Arrange
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> sut.createAndAdd(mock(UserId.class), null, mock(GenreId.class)));
    }

    @Test
    void shouldThrowWhenGenreIdIsNullForCreateAndAdd() {

        //Arrange
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> sut.createAndAdd(mock(UserId.class), "name", null));
    }

    @Test
    void shouldCreateAndAddCollectionSuccessfully() {

        //Arrange
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        UserId user = mock(UserId.class);
        GenreId genre = mock(GenreId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        //Act
        when(factory.create("Cool collection", genre, user)).thenReturn(collection);

        CollectionOfPublications result =
                sut.createAndAdd(user, "Cool collection", genre);

        //Assert
        assertEquals(collection, result);
    }

    @Test
    void shouldAllowSameNameAndGenreForDifferentUsers() {

        //Arrange
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        UserId user1 = mock(UserId.class);
        UserId user2 = mock(UserId.class);
        GenreId genre = mock(GenreId.class);

        CollectionOfPublications awesomeCollection = mock(CollectionOfPublications.class);
        CollectionOfPublications differentCollection = mock(CollectionOfPublications.class);

        //Act
        when(factory.create("Favorite books", genre, user1)).thenReturn(awesomeCollection);
        when(factory.create("Favorite books", genre, user2)).thenReturn(awesomeCollection);

        when(awesomeCollection.getOwnerId()).thenReturn(user1);
        when(awesomeCollection.getCollectionName()).thenReturn("Favorite books");
        when(awesomeCollection.getCollectionGenre()).thenReturn(genre);

        // Inserting already existing collection
        sut.createAndAdd(user1, "Favorite books", genre);

        // Second insert for a different user
        CollectionOfPublications result =
                sut.createAndAdd(user2, "Favorite books", genre);

        //Assert
        assertEquals(awesomeCollection, result);
    }

    @Test
    void shouldAllowSameNameDifferentGenre() {

        //Arrange
        sut = new CollectionOfPublicationsRepositoryMem(factory);

        UserId user = mock(UserId.class);
        GenreId genre1 = mock(GenreId.class);
        GenreId genre2 = mock(GenreId.class);

        CollectionOfPublications johnCollection1 = mock(CollectionOfPublications.class);
        CollectionOfPublications johnCollection2 = mock(CollectionOfPublications.class);

        //Act
        when(factory.create("Favorites", genre1, user)).thenReturn(johnCollection1);
        when(factory.create("Favorites", genre2, user)).thenReturn(johnCollection2);

        when(johnCollection1.getOwnerId()).thenReturn(user);
        when(johnCollection1.getCollectionName()).thenReturn("Favorites");
        when(johnCollection1.getCollectionGenre()).thenReturn(genre1);

        // First insert
        sut.createAndAdd(user, "Favorites", genre1);

        // Second insert (different genre)
        CollectionOfPublications result =
                sut.createAndAdd(user, "Favorites", genre2);

        //Act
        assertEquals(johnCollection2, result);
    }
}