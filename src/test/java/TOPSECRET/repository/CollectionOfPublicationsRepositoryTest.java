package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US018;

import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for US018 functionality in {@link CollectionOfPublicationsRepository}.
 * SUT: CollectionOfPublicationsRepository
 * All domain collaborators are mocked to isolate repository behavior.
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
                new CollectionOfPublicationsRepository();

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
                new CollectionOfPublicationsRepository();

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
                new CollectionOfPublicationsRepository();

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
                new CollectionOfPublicationsRepository();

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
                new CollectionOfPublicationsRepository();

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
                new CollectionOfPublicationsRepository();

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
                new CollectionOfPublicationsRepository();

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
                new CollectionOfPublicationsRepository();

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
                new CollectionOfPublicationsRepository();

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
    void shouldReturnListOfPublicListByGivenGenre(){

        CollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepository();

        User owner = mock(User.class);
        Genre desiredGenre = mock(Genre.class);

        when(owner.getUserName()).thenReturn("john");
        when(desiredGenre.getGenreType()).thenReturn("Drama");

        CollectionOfPublications collection1 = new CollectionOfPublications("test", desiredGenre, owner);
        collection1.set_isPrivate(false);
        CollectionOfPublications collection2 = new CollectionOfPublications("test2", new Genre("Action"), owner);
        collection2.set_isPrivate(false);
        CollectionOfPublications collection3 = new CollectionOfPublications("test3", desiredGenre, owner);


        sut.addCollectionToRepo(collection1);
        sut.addCollectionToRepo(collection2);
        sut.addCollectionToRepo(collection3);

        assertEquals(1, sut.findPublicByGenre(desiredGenre).size());

    }

    /**
     * When a user inputs an invalid genre, method should throw illegal argument exception
     */
    @Test
    void shouldThrowExceptionWhenGenreIsNull() {

        CollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepository();

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

        CollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepository();

        Genre desiredGenre = mock(Genre.class);
        when(desiredGenre.getGenreType()).thenReturn(" ");

        assertThrows(IllegalArgumentException.class, () -> sut.findPublicByGenre(desiredGenre));
    }

    /**
     * Ensures the return list is empty when there are no public lists of the user inputed genre
     */
    @Test
    void shouldReturnEmptyListWhenNoPublicListsExist() {

        CollectionOfPublicationsRepository sut = new CollectionOfPublicationsRepository();

        // Genre setup
        Genre desiredGenre = mock(Genre.class);
        when(desiredGenre.getGenreType()).thenReturn("Action");


        // Collection setup
        CollectionOfPublications c1 = mock(CollectionOfPublications.class);
        CollectionOfPublications c2 = mock(CollectionOfPublications.class);
        CollectionOfPublications c3 = mock(CollectionOfPublications.class);

        // Collection privacy
        when(c1.getIsPrivate()).thenReturn(true);
        when(c2.getIsPrivate()).thenReturn(true);
        when(c3.getIsPrivate()).thenReturn(false);

        // Collection genre
        when(c1.getCollectionName()).thenReturn("Action");
        when(c2.getCollectionName()).thenReturn("Drama");
        when(c3.getCollectionName()).thenReturn("Sci-fi");

        // Adding collections to repo
        sut.addCollectionToRepo(c1);
        sut.addCollectionToRepo(c2);
        sut.addCollectionToRepo(c3);

        // Assert must be 0
        assertEquals(0, sut.findPublicByGenre(desiredGenre).size());
    }

}