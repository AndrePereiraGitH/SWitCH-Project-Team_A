package TOPSECRET.dto.collectionOfPublications;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.collectionOfPublications.response.PublicCollectionOfPublicationsDTO;
import TOPSECRET.dto.collectionOfPublications.response.ResponseCollectionOfPublicationsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CollectionOfPublicationsMapper}.
 *
 * <p>Verifies correct mapping from {@link CollectionOfPublications} domain objects
 * to {@link ResponseCollectionOfPublicationsDTO} and {@link PublicCollectionOfPublicationsDTO},
 * including single and list mappings, null handling, filtering of null elements,
 * and value object conversions.</p>
 */
class CollectionOfPublicationsMapperTest {

    private CollectionOfPublications collectionDouble;
    private CollectionOfPublicationsId idDouble;

    @BeforeEach
    void setUp() {
        collectionDouble = mock(CollectionOfPublications.class);
        idDouble = mock(CollectionOfPublicationsId.class);
        when(collectionDouble.id()).thenReturn(idDouble);
        when(idDouble.getId()).thenReturn("collection-123");
        when(idDouble.toString()).thenReturn("collection-123");
        when(collectionDouble.getCollectionName()).thenReturn("My Collection");
        when(collectionDouble.getIsPrivate()).thenReturn(true);
    }

    // -------------------------------------------------------------------------
    // toDTO
    // -------------------------------------------------------------------------

    @Test
    void toDTO_ShouldReturnCollectionNameCorrectly() {
        ResponseCollectionOfPublicationsDTO result = CollectionOfPublicationsMapper.toDTO(collectionDouble);
        assertEquals("My Collection", result.getCollectionName());
    }

    @Test
    void toDTO_ShouldReturnCollectionIdCorrectly() {
        ResponseCollectionOfPublicationsDTO result = CollectionOfPublicationsMapper.toDTO(collectionDouble);
        assertEquals("collection-123", result.getCollectionId());
    }

    @Test
    void toDTO_ShouldReturnIsPrivateTrue_WhenCollectionIsPrivate() {
        when(collectionDouble.getIsPrivate()).thenReturn(true);
        ResponseCollectionOfPublicationsDTO result = CollectionOfPublicationsMapper.toDTO(collectionDouble);
        assertTrue(result.isPrivate());
    }

    @Test
    void toDTO_ShouldReturnIsPrivateFalse_WhenCollectionIsPublic() {
        when(collectionDouble.getIsPrivate()).thenReturn(false);
        ResponseCollectionOfPublicationsDTO result = CollectionOfPublicationsMapper.toDTO(collectionDouble);
        assertFalse(result.isPrivate());
    }

    @Test
    void toDTO_ShouldMapAllFieldsCorrectly() {
        when(collectionDouble.getIsPrivate()).thenReturn(false);
        ResponseCollectionOfPublicationsDTO result = CollectionOfPublicationsMapper.toDTO(collectionDouble);
        assertAll(
                () -> assertEquals("My Collection", result.getCollectionName()),
                () -> assertEquals("collection-123", result.getCollectionId()),
                () -> assertFalse(result.isPrivate())
        );
    }

    @Test
    void toDTO_ShouldThrowNullPointerException_WhenCollectionIsNull() {
        assertThrows(NullPointerException.class, () -> CollectionOfPublicationsMapper.toDTO(null));
    }

    // -------------------------------------------------------------------------
    // toDTOList
    // -------------------------------------------------------------------------

    @Test
    void toDTOList_ShouldReturnCorrectListSize() {
        List<CollectionOfPublications> collections = List.of(collectionDouble);
        List<ResponseCollectionOfPublicationsDTO> result = CollectionOfPublicationsMapper.toDTOList(collections);
        assertEquals(1, result.size());
    }

    @Test
    void toDTOList_ShouldMapEachElement() {
        CollectionOfPublications second = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId secondId = mock(CollectionOfPublicationsId.class);
        when(second.id()).thenReturn(secondId);
        when(secondId.toString()).thenReturn("collection-456");
        when(second.getCollectionName()).thenReturn("Second Collection");
        when(second.getIsPrivate()).thenReturn(false);

        List<ResponseCollectionOfPublicationsDTO> result =
                CollectionOfPublicationsMapper.toDTOList(List.of(collectionDouble, second));

        assertEquals(2, result.size());
        assertEquals("My Collection", result.get(0).getCollectionName());
        assertEquals("Second Collection", result.get(1).getCollectionName());
    }

    @Test
    void toDTOList_ShouldPreserveIsPrivate_ForEachElement() {
        when(collectionDouble.getIsPrivate()).thenReturn(true);

        CollectionOfPublications publicCollection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId publicId = mock(CollectionOfPublicationsId.class);
        when(publicCollection.id()).thenReturn(publicId);
        when(publicId.toString()).thenReturn("collection-789");
        when(publicCollection.getCollectionName()).thenReturn("Public Collection");
        when(publicCollection.getIsPrivate()).thenReturn(false);

        List<ResponseCollectionOfPublicationsDTO> result =
                CollectionOfPublicationsMapper.toDTOList(List.of(collectionDouble, publicCollection));

        assertTrue(result.get(0).isPrivate());
        assertFalse(result.get(1).isPrivate());
    }

    @Test
    void toDTOList_ShouldReturnEmptyList_WhenListIsEmpty() {
        List<ResponseCollectionOfPublicationsDTO> result =
                CollectionOfPublicationsMapper.toDTOList(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOList_ShouldThrowNullPointerException_WhenListIsNull() {
        assertThrows(NullPointerException.class, () -> CollectionOfPublicationsMapper.toDTOList(null));
    }

    @Test
    void toDTOList_ShouldIgnoreNullElements_WhenListContainsNulls() {
        List<CollectionOfPublications> collections = new ArrayList<>();
        collections.add(collectionDouble);
        collections.add(null);

        List<ResponseCollectionOfPublicationsDTO> result =
                CollectionOfPublicationsMapper.toDTOList(collections);

        assertEquals(1, result.size());
    }

    @Test
    void toDTOList_ShouldReturnEmptyList_WhenAllElementsAreNull() {
        List<CollectionOfPublications> collections = new ArrayList<>();
        collections.add(null);
        collections.add(null);

        List<ResponseCollectionOfPublicationsDTO> result =
                CollectionOfPublicationsMapper.toDTOList(collections);

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // toGenreId
    // -------------------------------------------------------------------------

    @Test
    void toGenreId_ShouldReturnGenreId_WhenStringIsValid() {
        GenreId result = CollectionOfPublicationsMapper.toGenreId("genre-123");
        assertEquals("genre-123", result.getID());
    }

    @Test
    void toGenreId_ShouldThrowIllegalArgumentException_WhenStringIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> CollectionOfPublicationsMapper.toGenreId(null));
    }

    @Test
    void toGenreId_ShouldThrowIllegalArgumentException_WhenStringIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> CollectionOfPublicationsMapper.toGenreId("   "));
    }

    @Test
    void toGenreId_ShouldThrowIllegalArgumentException_WhenStringIsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> CollectionOfPublicationsMapper.toGenreId(""));
    }

    // -------------------------------------------------------------------------
    // toUserId
    // -------------------------------------------------------------------------

    @Test
    void toUserId_ShouldReturnUserId_WhenStringIsValid() {
        UserId result = CollectionOfPublicationsMapper.toUserId("user-123");
        assertEquals(new UserId("user-123"), result);
    }

    @Test
    void toUserId_ShouldReturnEquivalentValueObject_WhenStringIsValid() {
        UserId result = CollectionOfPublicationsMapper.toUserId("user-123");
        assertNotNull(result);
        assertEquals("user-123", result.getUserId());
    }

    @Test
    void toUserId_ShouldThrowException_WhenStringIsNull() {
        assertThrows(Exception.class, () -> CollectionOfPublicationsMapper.toUserId(null));
    }

    @Test
    void toUserId_ShouldThrowException_WhenStringIsBlank() {
        assertThrows(Exception.class, () -> CollectionOfPublicationsMapper.toUserId("  "));
    }

    // -------------------------------------------------------------------------
    // toCollectionId
    // -------------------------------------------------------------------------

    @Test
    void toCollectionId_ShouldReturnCollectionId_WhenStringIsValid() {
        CollectionOfPublicationsId result =
                CollectionOfPublicationsMapper.toCollectionId("collection-123");
        assertEquals(new CollectionOfPublicationsId("collection-123"), result);
    }

    @Test
    void toCollectionId_ShouldReturnEquivalentValueObject_WhenStringIsValid() {
        CollectionOfPublicationsId result =
                CollectionOfPublicationsMapper.toCollectionId("collection-123");
        assertNotNull(result);
        assertEquals(new CollectionOfPublicationsId("collection-123"), result);
    }

    @Test
    void toCollectionId_ShouldThrowException_WhenStringIsNull() {
        assertThrows(Exception.class,
                () -> CollectionOfPublicationsMapper.toCollectionId(null));
    }

    @Test
    void toCollectionId_ShouldThrowException_WhenStringIsBlank() {
        assertThrows(Exception.class,
                () -> CollectionOfPublicationsMapper.toCollectionId("  "));
    }

    // -------------------------------------------------------------------------
    // toPublicDTO
    // -------------------------------------------------------------------------

    @Test
    void toPublicDTO_ShouldMapAllFieldsCorrectly() {
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);
        LocalDate publicUntil = LocalDate.now().plusDays(5);

        when(collection.id()).thenReturn(id);
        when(id.toString()).thenReturn("collection-123");
        when(collection.getCollectionName()).thenReturn("My Collection");
        when(collection.getPublicUntil()).thenReturn(publicUntil);

        PublicCollectionOfPublicationsDTO result =
                CollectionOfPublicationsMapper.toPublicDTO(collection, "alice_smith");

        assertAll(
                () -> assertEquals("collection-123", result.getCollectionId()),
                () -> assertEquals("My Collection", result.getCollectionName()),
                () -> assertEquals("alice_smith", result.getOwnerName()),
                () -> assertEquals(publicUntil, result.getPublicUntil())
        );
    }

    @Test
    void toPublicDTO_ShouldAllowNullPublicUntil() {
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(collection.id()).thenReturn(id);
        when(id.toString()).thenReturn("id-1");
        when(collection.getCollectionName()).thenReturn("A");
        when(collection.getPublicUntil()).thenReturn(null);

        PublicCollectionOfPublicationsDTO result =
                CollectionOfPublicationsMapper.toPublicDTO(collection, "owner-1");

        assertNull(result.getPublicUntil());
    }

    @Test
    void toPublicDTO_ShouldThrowNullPointerException_WhenCollectionIsNull() {
        assertThrows(NullPointerException.class,
                () -> CollectionOfPublicationsMapper.toPublicDTO(null, "owner-123"));
    }

    @Test
    void toPublicDTO_ShouldAllowNullOwnerName() {
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(collection.id()).thenReturn(id);
        when(id.toString()).thenReturn("id-1");
        when(collection.getCollectionName()).thenReturn("A");
        when(collection.getPublicUntil()).thenReturn(null);

        PublicCollectionOfPublicationsDTO result =
                CollectionOfPublicationsMapper.toPublicDTO(collection, null);

        assertNull(result.getOwnerName());
    }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    @Test
    void constructor_ShouldThrowUnsupportedOperationException_WhenInstantiatedViaReflection() {
        assertThrows(InvocationTargetException.class, () -> {
            java.lang.reflect.Constructor<CollectionOfPublicationsMapper> constructor =
                    CollectionOfPublicationsMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }
}