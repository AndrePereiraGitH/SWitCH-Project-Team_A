package TOPSECRET.dto.collectionOfPublications;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.collectionOfPublications.response.PublicCollectionOfPublicationsDTO;
import TOPSECRET.dto.collectionOfPublications.response.ResponseCollectionOfPublicationsDTO;
import org.hibernate.annotations.CollectionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CollectionOfPublicationsMapper}.
 *
 * <p>Verifies correct mapping from {@link CollectionOfPublications} domain objects
 * to {@link ResponseCollectionOfPublicationsDTO}, including single and list mappings,
 * null handling, and filtering of null elements within lists.</p>
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
    }

    // -------------------------------------------------------------------------
    // toDTO
    // -------------------------------------------------------------------------

    @Test
    void toDTO_ShouldReturnCollectionNameCorrectly() {
        // ACT
        ResponseCollectionOfPublicationsDTO result = CollectionOfPublicationsMapper.toDTO(collectionDouble);

        // ASSERT
        assertEquals("My Collection", result.getCollectionName());
    }

    @Test
    void toDTO_ShouldReturnCollectionIdCorrectly() {
        // ACT
        ResponseCollectionOfPublicationsDTO result = CollectionOfPublicationsMapper.toDTO(collectionDouble);

        // ASSERT
        assertEquals("collection-123", result.getCollectionId());
    }

    @Test
    void toDTO_ShouldThrowNullPointerException_WhenCollectionIsNull() {
        // ACT & ASSERT
        assertThrows(NullPointerException.class, () -> CollectionOfPublicationsMapper.toDTO(null));
    }

    // -------------------------------------------------------------------------
    // toDTOList
    // -------------------------------------------------------------------------

    @Test
    void toDTOList_ShouldReturnCorrectListSize() {
        // ARRANGE
        List<CollectionOfPublications> collections = List.of(collectionDouble);

        // ACT
        List<ResponseCollectionOfPublicationsDTO> result = CollectionOfPublicationsMapper.toDTOList(collections);

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void toDTOList_ShouldReturnEmptyList_WhenListIsEmpty() {
        // ARRANGE
        List<CollectionOfPublications> collections = List.of();

        // ACT
        List<ResponseCollectionOfPublicationsDTO> result = CollectionOfPublicationsMapper.toDTOList(collections);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOList_ShouldThrowNullPointerException_WhenListIsNull() {
        // ACT & ASSERT
        assertThrows(NullPointerException.class, () -> CollectionOfPublicationsMapper.toDTOList(null));
    }

    @Test
    void toDTOList_ShouldIgnoreNullElements_WhenListContainsNulls() {
        // ARRANGE
        List<CollectionOfPublications> collections = new java.util.ArrayList<>();
        collections.add(collectionDouble);
        collections.add(null);

        // ACT
        List<ResponseCollectionOfPublicationsDTO> result = CollectionOfPublicationsMapper.toDTOList(collections);

        // ASSERT
        assertEquals(1, result.size());
    }

    // -------------------------------------------------------------------------
    // toGenreId
    // -------------------------------------------------------------------------

    @Test
    void toGenreIdShouldReturnGenreIdWhenStringIsValid() {
        // ARRANGE
        String genreIdString = "genre-123";

        // ACT
        GenreId result = CollectionOfPublicationsMapper.toGenreId(genreIdString);

        // ASSERT
        assertEquals(genreIdString, result.getID());
    }

    @Test
    void toGenreIdShouldThrowIllegalArgumentExceptionWhenStringIsNull() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> CollectionOfPublicationsMapper.toGenreId(null));
    }

    @Test
    void toGenreIdShouldThrowIllegalArgumentExceptionWhenStringIsBlank() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> CollectionOfPublicationsMapper.toGenreId("   "));
    }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    @Test
    void constructor_ShouldThrowUnsupportedOperationException_WhenInstantiatedViaReflection() {
        // ACT & ASSERT
        assertThrows(InvocationTargetException.class, () -> {
            java.lang.reflect.Constructor<CollectionOfPublicationsMapper> constructor =
                    CollectionOfPublicationsMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }

    // -------------------------------------------------------------------------
    // PublicDTO
    // -------------------------------------------------------------------------

    @Test
    void toPublicDTO_ShouldMapCollectionCorrectly() {

        // ARRANGE
        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        CollectionOfPublicationsId collectionIdDouble =
                mock(CollectionOfPublicationsId.class);

        UserId ownerIdDouble =
                mock(UserId.class);

        LocalDateTime publicUntil =
                LocalDateTime.now().plusDays(5);

        when(collectionDouble.id()).thenReturn(collectionIdDouble);
        when(collectionDouble.getCollectionName()).thenReturn("My Collection");
        when(collectionDouble.getOwnerId()).thenReturn(ownerIdDouble);
        when(collectionDouble.getPublicUntil()).thenReturn(publicUntil);

        when(collectionIdDouble.toString()).thenReturn("collection-123");

        // ACT
        PublicCollectionOfPublicationsDTO result =
                CollectionOfPublicationsMapper.toPublicDTO(
                        collectionDouble,
                        "owner-123"
                );

        // ASSERT
        assertEquals("collection-123", result.getCollectionId());
        assertEquals("My Collection", result.getCollectionName());
        assertEquals("owner-123", result.getOwnerName());
        assertEquals(publicUntil, result.getPublicUntil());
    }

    @Test
    void toPublicDTO_ShouldThrowNullPointerException_WhenCollectionIsNull() {

        assertThrows(
                NullPointerException.class,
                () -> CollectionOfPublicationsMapper.toPublicDTO(
                        null,
                        "owner-123"
                )
        );
    }

    @Test
    void toPublicDTO_ShouldAllowNullPublicUntil() {

        CollectionOfPublications collection =
                mock(CollectionOfPublications.class);

        CollectionOfPublicationsId id =
                mock(CollectionOfPublicationsId.class);

        UserId owner =
                mock(UserId.class);

        when(collection.id()).thenReturn(id);
        when(collection.getCollectionName()).thenReturn("A");
        when(collection.getOwnerId()).thenReturn(owner);
        when(collection.getPublicUntil()).thenReturn(null);

        when(id.toString()).thenReturn("id-1");

        PublicCollectionOfPublicationsDTO dto =
                CollectionOfPublicationsMapper.toPublicDTO(
                        collection,
                        "owner-1"
                );

        assertNull(dto.getPublicUntil());
    }

    // -------------------------------------------------------------------------
    // toUserId
    // -------------------------------------------------------------------------

    @Test
    void toUserId_ShouldReturnUserId_WhenStringIsValid() {

        // ARRANGE
        String userId = "user-123";

        // ACT
        UserId result = CollectionOfPublicationsMapper.toUserId(userId);

        // ASSERT
        assertEquals(new UserId("user-123"), result);
    }

    @Test
    void toUserId_ShouldReturnEquivalentValueObject_WhenStringIsValid() {
        // ACT
        UserId result = CollectionOfPublicationsMapper.toUserId("user-123");

        // ASSERT
        assertNotNull(result);
        assertEquals("user-123", result.getUserId());
    }

    @Test
    void toUserId_ShouldThrowException_WhenStringIsNull() {

        assertThrows(
                Exception.class,
                () -> CollectionOfPublicationsMapper.toUserId(null)
        );
    }

    // -------------------------------------------------------------------------
    // toCollectionId
    // -------------------------------------------------------------------------

    @Test
    void toCollectionId_ShouldReturnCollectionId_WhenStringIsValid() {

        // ARRANGE
        String collectionId = "collection-123";

        // ACT
        CollectionOfPublicationsId result =
                CollectionOfPublicationsMapper.toCollectionId(collectionId);

        // ASSERT
        assertEquals(
                new CollectionOfPublicationsId("collection-123"),
                result
        );
    }

    @Test
    void toCollectionId_ShouldReturnEquivalentValueObject_WhenStringIsValid() {
        // ACT
        CollectionOfPublicationsId result =
                CollectionOfPublicationsMapper.toCollectionId("collection-123");

        // ASSERT
        assertEquals(new CollectionOfPublicationsId("collection-123"), result);
    }

    @Test
    void toCollectionId_ShouldThrowException_WhenStringIsNull() {

        assertThrows(
                Exception.class,
                () -> CollectionOfPublicationsMapper.toCollectionId(null)
        );
    }
}