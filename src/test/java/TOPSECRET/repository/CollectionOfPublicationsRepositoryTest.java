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
}