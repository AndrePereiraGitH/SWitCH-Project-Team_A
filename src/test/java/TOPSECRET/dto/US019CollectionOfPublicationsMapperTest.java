package TOPSECRET.dto;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US019CollectionOfPublicationsMapper}.
 *
 * <p>Verifies the correct behaviour of the mapping methods:
 * <ul>
 *   <li>{@link US019CollectionOfPublicationsMapper#toDTO(CollectionOfPublications)} - maps a single collection to a DTO</li>
 *   <li>{@link US019CollectionOfPublicationsMapper#toDTOs(List)} - maps a list of collections to a list of DTOs</li>
 * </ul>
 *
 * <p>All {@link CollectionOfPublications} dependencies are replaced with Mockito doubles
 * to ensure tests are isolated from domain logic.
 */
class US019CollectionOfPublicationsMapperTest {

    // -------------------------
    // Constructor
    // -------------------------

    @Test
    void constructor_ShouldBeAccessible_WhenInstantiatedViaReflection() throws Exception {
        // Arrange
        Constructor<US019CollectionOfPublicationsMapper> constructor =
                US019CollectionOfPublicationsMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // Act
        US019CollectionOfPublicationsMapper instance = constructor.newInstance();

        // Assert
        assertNotNull(instance);
    }

    // -------------------------
    // toDTO
    // -------------------------

    @Test
    void toDTO_ShouldReturnDTO_WhenCollectionIsValid() {
        // Arrange
        CollectionOfPublications collectionDouble = createCollectionDouble(
                "My Favorites",
                "fiction",
                "john_doe"
        );

        // Act
        US019CollectionOfPublicationsDTO result = US019CollectionOfPublicationsMapper.toDTO(collectionDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDTO_ShouldThrowIllegalArgumentException_WhenCollectionIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                US019CollectionOfPublicationsMapper.toDTO(null)
        );
    }

    @Test
    void toDTO_ShouldMapAllFieldsCorrectly_WhenCollectionIsValid() {
        // Arrange
        String expectedName = "Sci-Fi Collection";
        String expectedGenreId = "science fiction";
        String expectedUsername = "jane_smith";

        CollectionOfPublications collectionDouble = createCollectionDouble(
                expectedName,
                expectedGenreId,
                expectedUsername
        );

        // Act
        US019CollectionOfPublicationsDTO result = US019CollectionOfPublicationsMapper.toDTO(collectionDouble);

        // Assert
        assertEquals(expectedName, result.name());
        assertEquals(expectedGenreId, result.genreName());
        assertEquals(expectedUsername, result.ownerName());
    }

    // -------------------------
    // toDTOs
    // -------------------------

    @Test
    void toDTOs_ShouldThrowIllegalArgumentException_WhenListIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                US019CollectionOfPublicationsMapper.toDTOs(null)
        );
    }

    @Test
    void toDTOs_ShouldReturnEmptyList_WhenListIsEmpty() {
        // Arrange
        List<CollectionOfPublications> collections = List.of();

        // Act
        List<US019CollectionOfPublicationsDTO> result = US019CollectionOfPublicationsMapper.toDTOs(collections);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void toDTOs_ShouldReturnListWithOneDTO_WhenListHasOneCollection() {
        // Arrange
        CollectionOfPublications collectionDouble = createCollectionDouble(
                "My Favorites",
                "fiction",
                "john_doe"
        );
        List<CollectionOfPublications> collections = List.of(collectionDouble);

        // Act
        List<US019CollectionOfPublicationsDTO> result = US019CollectionOfPublicationsMapper.toDTOs(collections);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("My Favorites", result.get(0).name());
        assertEquals("fiction", result.get(0).genreName());
        assertEquals("john_doe", result.get(0).ownerName());
    }

    @Test
    void toDTOs_ShouldReturnListWithMultipleDTOs_WhenListHasMultipleCollections() {
        // Arrange
        CollectionOfPublications collectionDouble_1 = createCollectionDouble(
                "Favorites", "fiction", "user1"
        );
        CollectionOfPublications collectionDouble_2 = createCollectionDouble(
                "To Read", "mystery", "user1"
        );
        CollectionOfPublications collectionDouble_3 = createCollectionDouble(
                "Classics", "literature", "user1"
        );
        List<CollectionOfPublications> collections = List.of(
                collectionDouble_1,
                collectionDouble_2,
                collectionDouble_3
        );

        // Act
        List<US019CollectionOfPublicationsDTO> result = US019CollectionOfPublicationsMapper.toDTOs(collections);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Favorites", result.get(0).name());
        assertEquals("fiction", result.get(0).genreName());
        assertEquals("user1", result.get(0).ownerName());

        assertEquals("To Read", result.get(1).name());
        assertEquals("mystery", result.get(1).genreName());
        assertEquals("user1", result.get(1).ownerName());

        assertEquals("Classics", result.get(2).name());
        assertEquals("literature", result.get(2).genreName());
        assertEquals("user1", result.get(2).ownerName());
    }

    @Test
    void toDTOs_ShouldPreserveOrder_WhenListHasMultipleCollections() {
        // Arrange
        CollectionOfPublications first = createCollectionDouble("A", "genreA", "user");
        CollectionOfPublications second = createCollectionDouble("B", "genreB", "user");
        CollectionOfPublications third = createCollectionDouble("C", "genreC", "user");
        List<CollectionOfPublications> collections = List.of(first, second, third);

        // Act
        List<US019CollectionOfPublicationsDTO> result = US019CollectionOfPublicationsMapper.toDTOs(collections);

        // Assert
        assertEquals(3, result.size());
        assertEquals("A", result.get(0).name());
        assertEquals("B", result.get(1).name());
        assertEquals("C", result.get(2).name());
    }

    // -------------------------
    // Helpers
    // -------------------------

    private CollectionOfPublications createCollectionDouble(
            String collectionName,
            String genreIdValue,
            String username) {

        // CollectionOfPublicationsID
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);
        when(collectionIdDouble.toString()).thenReturn(UUID.randomUUID().toString());

        // GenreID
        GenreId genreIdDouble = mock(GenreId.class);
        when(genreIdDouble.getID()).thenReturn(genreIdValue);

        // UserId
        UserId userIdDouble = mock(UserId.class);
        when(userIdDouble.getUserId()).thenReturn(username);

        // CollectionOfPublications
        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
        when(collectionDouble.id()).thenReturn(collectionIdDouble);
        when(collectionDouble.getCollectionName()).thenReturn(collectionName);
        when(collectionDouble.getCollectionGenre()).thenReturn(genreIdDouble);
        when(collectionDouble.getOwnerId()).thenReturn(userIdDouble);

        return collectionDouble;
    }
}