package TOPSECRET.dto.collectionOfPublications;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.dto.collectionOfPublications.response.ResponseCollectionOfPublicationsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
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
}