package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.CollectionOfPublicationsFactory;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US018;

import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Unit tests for {@link CollectionOfPublicationsRepository}.
 * Covers US017 (createIfNotExists) and US018 (addPublicationToCollection, getUserCollections, collectionDTOToDomain).
 * SUT: CollectionOfPublicationsRepository — all domain collaborators are mocked.
 */
class CollectionOfPublicationsRepositoryTest {

    /**
     * Ensures publication is added successfully when:
     * - All parameters are valid
     * - User owns the collection
     * - User owns the library
     * - Publication exists in user library
     */
    @Test
    void shouldAddPublicationSuccessfully() {

        // ARRANGE
        CollectionOfPublicationsRepository sut =
                new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));

        User user = mock(User.class);
        Library library = mock(Library.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        PublicationSample publication = mock(PublicationSample.class);

        when(collection.getOwner()).thenReturn(user);
        when(library.getUser()).thenReturn(user);
        when(library.containsPublication(publication)).thenReturn(true);
        when(collection.addPublication(publication)).thenReturn(true);

        // ACT
        boolean result = sut.addPublicationToCollection(
                user, library, collection, publication);

        // ASSERT
        assertTrue(result);
        verify(collection).addPublication(publication);
    }

    /**
     * Ensures method returns false when any argument is null.
     */
    @Test
    void shouldReturnFalseWhenAnyParameterIsNull() {

        // ARRANGE
        CollectionOfPublicationsRepository sut =
                new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));

        // ACT + ASSERT
        assertFalse(sut.addPublicationToCollection(null, null, null, null));
    }

    /**
     * Ensures method returns false when user does not own the collection.
     */
    @Test
    void shouldReturnFalseWhenUserIsNotOwner() {

        // ARRANGE
        CollectionOfPublicationsRepository sut =
                new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));

        User user = mock(User.class);
        User otherUser = mock(User.class);
        Library library = mock(Library.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        PublicationSample publication = mock(PublicationSample.class);

        when(collection.getOwner()).thenReturn(otherUser);

        // ACT
        boolean result = sut.addPublicationToCollection(
                user, library, collection, publication);

        // ASSERT
        assertFalse(result);
    }

    /**
     * Ensures method returns false when publication
     * is not present in user library.
     */
    @Test
    void shouldReturnFalseWhenPublicationNotInLibrary() {

        // ARRANGE
        CollectionOfPublicationsRepository sut =
                new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));

        User user = mock(User.class);
        Library library = mock(Library.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        PublicationSample publication = mock(PublicationSample.class);

        when(collection.getOwner()).thenReturn(user);
        when(library.getUser()).thenReturn(user);
        when(library.containsPublication(publication)).thenReturn(false);

        // ACT
        boolean result = sut.addPublicationToCollection(
                user, library, collection, publication);

        // ASSERT
        assertFalse(result);
    }

    /**
     * Ensures exception is thrown when user is null.
     */
    @Test
    void shouldThrowExceptionWhenUserIsNull() {

        // ARRANGE
        CollectionOfPublicationsRepository sut =
                new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.getUserCollections(null));
    }

    /**
     * Ensures user collections are retrieved correctly.
     */
    @Test
    void shouldReturnCollectionsOwnedByUser() throws Exception {

        // ARRANGE
        CollectionOfPublicationsRepository sut =
                new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));

        User user = mock(User.class);
        User otherUser = mock(User.class);

        CollectionOfPublications collection1 = mock(CollectionOfPublications.class);
        CollectionOfPublications collection2 = mock(CollectionOfPublications.class);

        when(collection1.getOwner()).thenReturn(user);
        when(collection2.getOwner()).thenReturn(otherUser);

        // Insert into private list via reflection
        Field field = CollectionOfPublicationsRepository.class
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

    /**
     * Ensures correct domain object is returned when DTO matches.
     */
    @Test
    void shouldReturnMatchingCollectionFromDTO()
            throws Exception {

        // ARRANGE
        CollectionOfPublicationsRepository sut =
                new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));

        User owner = mock(User.class);
        when(owner.getUserName()).thenReturn("john");

        CollectionOfPublications collection =
                mock(CollectionOfPublications.class);

        when(collection.getCollectionName()).thenReturn("MyCollection");
        when(collection.getOwner()).thenReturn(owner);

        // Insert mock into internal list
        Field field = CollectionOfPublicationsRepository.class
                .getDeclaredField("_collectionOfPublicationsList");
        field.setAccessible(true);

        List<CollectionOfPublications> list =
                (List<CollectionOfPublications>) field.get(sut);

        list.add(collection);

        CollectionOfPublicationsDTO_US018 dto =
                mock(CollectionOfPublicationsDTO_US018.class);

        when(dto.getName()).thenReturn("MyCollection");
        when(dto.getOwnerName()).thenReturn("john");

        // ACT
        CollectionOfPublications result =
                sut.collectionDTOToDomain(dto);

        // ASSERT
        assertEquals(collection, result);
    }

    /**
     * Ensures exception is thrown when DTO is null.
     */
    @Test
    void shouldThrowExceptionWhenDTOIsNull() {

        // ARRANGE
        CollectionOfPublicationsRepository sut =
                new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.collectionDTOToDomain(null));
    }

    /**
     * Ensures InstanceNotFoundException is thrown
     * when no collection matches the DTO.
     */
    @Test
    void shouldThrowExceptionWhenCollectionNotFound() {

        // ARRANGE
        CollectionOfPublicationsRepository sut =
                new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));

        CollectionOfPublicationsDTO_US018 dto =
                mock(CollectionOfPublicationsDTO_US018.class);

        when(dto.getName()).thenReturn("Unknown");
        when(dto.getOwnerName()).thenReturn("john");

        // ACT + ASSERT
        assertThrows(InstanceNotFoundException.class,
                () -> sut.collectionDTOToDomain(dto));
    }

    // ----- US020 START -----

    /**
     * US020 - Get list of public lists by a given genre
     */

    /**
     * Happy path
     * Ensures the method correctly lists all public collections of a user given genre
     */
    @Test
    void shouldReturnListOfPublicListByGivenGenre() {

        CollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));

        // User setup
        User owner = mock(User.class);
        when(owner.getUserName()).thenReturn("john");

        // Genre setup
        Genre desiredGenre = mock(Genre.class);
        when(desiredGenre.getGenreType()).thenReturn("Drama");

        // Collection creation
        CollectionOfPublications c1 = mock(CollectionOfPublications.class);
        CollectionOfPublications c2 = mock(CollectionOfPublications.class);
        CollectionOfPublications c3 = mock(CollectionOfPublications.class);

        // Collection user
        when(c1.getOwner()).thenReturn(owner);
        when(c2.getOwner()).thenReturn(owner);
        when(c3.getOwner()).thenReturn(owner);

        // Collection privacy
        when(c1.getIsPrivate()).thenReturn(true);
        when(c2.getIsPrivate()).thenReturn(false);
        when(c3.getIsPrivate()).thenReturn(false);

        // Collection name
        when(c1.getCollectionName()).thenReturn("Collection 1");
        when(c2.getCollectionName()).thenReturn("Collection 2");
        when(c3.getCollectionName()).thenReturn("Collection 3");

        // Collection genre
        when(c1.getCollectionGenre()).thenReturn(desiredGenre);
        when(c2.getCollectionGenre()).thenReturn(desiredGenre);
        when(c3.getCollectionGenre()).thenReturn(desiredGenre);

        // Add collections to repo
        sut.addCollectionToRepo(c1);
        sut.addCollectionToRepo(c2);
        sut.addCollectionToRepo(c3);

        assertEquals(2, sut.findPublicByGenre(desiredGenre).size());

    }

    /**
     * When a user inputs an invalid genre, method should throw illegal argument exception
     */
    @Test
    void shouldThrowExceptionWhenGenreIsNull() {

        CollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));

        Genre desiredGenre = mock(Genre.class);
        when(desiredGenre.getGenreType()).thenReturn(null);

        assertNull(desiredGenre.getGenreType());
        assertThrows(IllegalArgumentException.class, () -> sut.findPublicByGenre(desiredGenre));
    }

    /**
     * When a user input is blank, method should throw illegal argument exception
     */
    @Test
    void shouldThrowExceptionWhenGenreTypeIsBlank() {

        CollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));

        Genre desiredGenre = mock(Genre.class);
        when(desiredGenre.getGenreType()).thenReturn(" ");

        assertThrows(IllegalArgumentException.class, () -> sut.findPublicByGenre(desiredGenre));
    }

    /**
     * Ensures the return list is empty when there are no public lists of the given genre
     */
    @Test
    void shouldReturnEmptyListWhenNoPublicListsExist() {

        CollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));

        // Genre setup
        Genre desiredGenre = mock(Genre.class);
        when(desiredGenre.getGenreType()).thenReturn("Action");

        Genre otherGenre = mock(Genre.class);
        when(otherGenre.getGenreType()).thenReturn("Drama");


        // Collection setup
        CollectionOfPublications c1 = mock(CollectionOfPublications.class);
        CollectionOfPublications c2 = mock(CollectionOfPublications.class);
        CollectionOfPublications c3 = mock(CollectionOfPublications.class);

        // Collection privacy
        when(c1.getIsPrivate()).thenReturn(true);
        when(c2.getIsPrivate()).thenReturn(true);
        when(c3.getIsPrivate()).thenReturn(false);

        // Collection genre
        when(c1.getCollectionGenre()).thenReturn(desiredGenre);
        when(c2.getCollectionGenre()).thenReturn(otherGenre);
        when(c3.getCollectionGenre()).thenReturn(otherGenre);

        // Adding collections to repo
        sut.addCollectionToRepo(c1);
        sut.addCollectionToRepo(c2);
        sut.addCollectionToRepo(c3);

        // Assert must be 0
        assertEquals(0, sut.findPublicByGenre(desiredGenre).size());
    }


    // ========== US017 - createIfNotExists() ==========
    //
    // SUT: CollectionOfPublicationsRepository — User and Genre are mocked.
    // Tests cover all 5 paths: valid creation, null user, null name, null genre, duplicate.

    /**
     * US017: Verifies that an exception is thrown when factory is null.
     */
    @Test
    void shouldThrowExceptionWhenFactoryIsNull_US017() {
        assertThrows(IllegalArgumentException.class,
                () -> new CollectionOfPublicationsRepository(null));
    }

    /**
     * US017: Verifies that a valid collection is created as private with an empty publication list.
     */
    @Test
    void shouldCreateCollectionSuccessfully_US017() {
        // Arrange
        User mockUser = mock(User.class);
        Genre mockGenre = mock(Genre.class);
        CollectionOfPublications mockCollection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsFactory mockFactory = mock(CollectionOfPublicationsFactory.class);

        when(mockFactory.create("Favorites", mockGenre, mockUser)).thenReturn(mockCollection);
        when(mockCollection.getCollectionName()).thenReturn("Favorites");
        when(mockCollection.getCollectionGenre()).thenReturn(mockGenre);
        when(mockCollection.getIsPrivate()).thenReturn(true);
        when(mockCollection.getPublications()).thenReturn(List.of());

        CollectionOfPublicationsRepository sut =
                new CollectionOfPublicationsRepository(mockFactory);

        // Act
        CollectionOfPublications result =
                sut.createIfNotExists(mockUser, "Favorites", mockGenre);

        // Assert
        assertNotNull(result);
        assertEquals("Favorites", result.getCollectionName());
        assertEquals(mockGenre, result.getCollectionGenre());
        assertTrue(result.getIsPrivate());
        assertTrue(result.getPublications().isEmpty());
    }

    /**
     * US017: Verifies that an exception is thrown when user is null.
     */
    @Test
    void shouldThrowExceptionWhenUserIsNull_US017() {
        // Arrange
        CollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));
        Genre mockGenre = mock(Genre.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> sut.createIfNotExists(null, "Favorites", mockGenre));
    }

    /**
     * US017: Verifies that an exception is thrown when name is null.
     */
    @Test
    void shouldThrowExceptionWhenNameIsNull_US017() {
        // Arrange
        CollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));
        User mockUser = mock(User.class);
        Genre mockGenre = mock(Genre.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> sut.createIfNotExists(mockUser, null, mockGenre));
    }

    /**
     * US017: Verifies that an exception is thrown when genre is null.
     */
    @Test
    void shouldThrowExceptionWhenGenreIsNull_US017() {
        // Arrange
        CollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepository(mock(CollectionOfPublicationsFactory.class));
        User mockUser = mock(User.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> sut.createIfNotExists(mockUser, "Favorites", null));
    }

    /**
     * US017: Verifies that two collections with the same name but different genre are allowed.
     */
    @Test
    void shouldAllowSameNameWithDifferentGenre_US017() {
        // Arrange
        User mockUser = mock(User.class);
        Genre mockGenre1 = mock(Genre.class);
        Genre mockGenre2 = mock(Genre.class);

        CollectionOfPublications mockCollection1 = mock(CollectionOfPublications.class);
        CollectionOfPublications mockCollection2 = mock(CollectionOfPublications.class);

        CollectionOfPublicationsFactory mockFactory = mock(CollectionOfPublicationsFactory.class);
        when(mockFactory.create("Favorites", mockGenre1, mockUser)).thenReturn(mockCollection1);
        when(mockFactory.create("Favorites", mockGenre2, mockUser)).thenReturn(mockCollection2);

        when(mockCollection1.getOwner()).thenReturn(mockUser);
        when(mockCollection1.getCollectionName()).thenReturn("Favorites");
        when(mockCollection1.getCollectionGenre()).thenReturn(mockGenre1);

        CollectionOfPublicationsRepository sut =
                new CollectionOfPublicationsRepository(mockFactory);

        sut.createIfNotExists(mockUser, "Favorites", mockGenre1);

        // Act — same name, different genre — must not throw
        CollectionOfPublications result =
                sut.createIfNotExists(mockUser, "Favorites", mockGenre2);

        // Assert
        assertNotNull(result);
    }

    /**
     * US017: Verifies that an exception is thrown when a collection with the same name and genre already exists for that user.
     */
    @Test
    void shouldThrowExceptionWhenDuplicateCollectionExists_US017() {
        // Arrange
        User mockUser = mock(User.class);
        Genre mockGenre = mock(Genre.class);
        CollectionOfPublications mockCollection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsFactory mockFactory = mock(CollectionOfPublicationsFactory.class);

        when(mockFactory.create("Favorites", mockGenre, mockUser)).thenReturn(mockCollection);
        when(mockCollection.getOwner()).thenReturn(mockUser);
        when(mockCollection.getCollectionName()).thenReturn("Favorites");
        when(mockCollection.getCollectionGenre()).thenReturn(mockGenre);

        CollectionOfPublicationsRepository sut =
                new CollectionOfPublicationsRepository(mockFactory);

        sut.createIfNotExists(mockUser, "Favorites", mockGenre); // first creation

        // Act & Assert — A second creation with the same data should throw an exception
        assertThrows(IllegalArgumentException.class,
                () -> sut.createIfNotExists(mockUser, "Favorites", mockGenre));
    }



}