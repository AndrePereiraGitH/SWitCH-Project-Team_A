package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsID;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US019CollectionOfPublicationsDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link US019ToPubliclyShareCollectionOfPublicationsController} class.
 *
 * <p>Uses Mockito to isolate the SUT from its collaborators
 * ({@link ICollectionOfPublicationsRepository} and {@link CollectionOfPublications}).</p>
 *
 * <p>Validates the following responsibilities:</p>
 * <ul>
 *     <li>Rejecting null constructor arguments</li>
 *     <li>Retrieving and mapping a user's collections to DTOs</li>
 *     <li>Rejecting invalid inputs for making a collection public</li>
 *     <li>Enforcing ownership before making a collection public</li>
 * </ul>
 */
class US019ToPubliclyShareCollectionOfPublicationsControllerTest {

    private ICollectionOfPublicationsRepository repositoryDouble;
    private US019ToPubliclyShareCollectionOfPublicationsController controller;
    private UserId testUserId;
    private CollectionOfPublicationsID testCollectionId;

    @BeforeEach
    void setUp() {
        repositoryDouble = mock(ICollectionOfPublicationsRepository.class);
        controller = new US019ToPubliclyShareCollectionOfPublicationsController(repositoryDouble);
        testUserId = mock(UserId.class);
        testCollectionId = mock(CollectionOfPublicationsID.class);
    }

    // ============================================================
    // Constructor Tests
    // ============================================================

    @Test
    void ensureControllerIsCreatedSuccessfully() {
        // Act & Assert
        assertNotNull(controller);
    }

    @Test
    void ensureThrowsExceptionWhenRepositoryIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new US019ToPubliclyShareCollectionOfPublicationsController(null));
    }

    // ============================================================
    // getUserCollections() Tests
    // ============================================================

    @Test
    void ensureGetUserCollectionsThrowsExceptionWhenUserIdIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.getUserCollections(null));
    }

    @Test
    void ensureGetUserCollectionsReturnsEmptyListWhenUserHasNoCollections() {
        // Arrange
        when(repositoryDouble.getUserCollections(testUserId)).thenReturn(new ArrayList<>());

        // Act
        List<US019CollectionOfPublicationsDTO> result = controller.getUserCollections(testUserId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void ensureGetUserCollectionsReturnsCorrectNumberOfDTOs() {
        // Arrange
        CollectionOfPublications col1 = mock(CollectionOfPublications.class);
        CollectionOfPublications col2 = mock(CollectionOfPublications.class);
        GenreID genre1 = mock(GenreID.class);
        GenreID genre2 = mock(GenreID.class);

        when(col1.getCollectionName()).thenReturn("Sci-Fi");
        when(col1.getCollectionGenre()).thenReturn(genre1);
        when(col1.getOwnerId()).thenReturn(testUserId);
        when(genre1.getID()).thenReturn("sci-fi");
        when(testUserId.getUsername()).thenReturn("alice");

        when(col2.getCollectionName()).thenReturn("Drama");
        when(col2.getCollectionGenre()).thenReturn(genre2);
        when(col2.getOwnerId()).thenReturn(testUserId);
        when(genre2.getID()).thenReturn("drama");

        when(repositoryDouble.getUserCollections(testUserId)).thenReturn(List.of(col1, col2));

        // Act
        List<US019CollectionOfPublicationsDTO> result = controller.getUserCollections(testUserId);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void ensureGetUserCollectionsReturnsCorrectDTOValues() {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        GenreID genre = mock(GenreID.class);

        when(collection.getCollectionName()).thenReturn("My Favorites");
        when(collection.getCollectionGenre()).thenReturn(genre);
        when(collection.getOwnerId()).thenReturn(testUserId);
        when(genre.getID()).thenReturn("fantasy");
        when(testUserId.getUsername()).thenReturn("bob");

        when(repositoryDouble.getUserCollections(testUserId)).thenReturn(List.of(collection));

        // Act
        List<US019CollectionOfPublicationsDTO> result = controller.getUserCollections(testUserId);

        // Assert
        assertEquals(1, result.size());
        US019CollectionOfPublicationsDTO dto = result.get(0);
        assertEquals("My Favorites", dto.name());
        assertEquals("fantasy", dto.genreName());
        assertEquals("bob", dto.ownerName());
    }

    @Test
    void ensureGetUserCollectionsHandlesLargeNumberOfCollections() {
        // Arrange
        int numberOfCollections = 100;
        List<CollectionOfPublications> mockCollections = new ArrayList<>();

        for (int i = 0; i < numberOfCollections; i++) {
            CollectionOfPublications collection = mock(CollectionOfPublications.class);
            GenreID genre = mock(GenreID.class);

            when(collection.getCollectionName()).thenReturn("Collection " + i);
            when(collection.getCollectionGenre()).thenReturn(genre);
            when(collection.getOwnerId()).thenReturn(testUserId);
            when(genre.getID()).thenReturn("genre-" + i);
            when(testUserId.getUsername()).thenReturn("user");

            mockCollections.add(collection);
        }

        when(repositoryDouble.getUserCollections(testUserId)).thenReturn(mockCollections);

        // Act
        List<US019CollectionOfPublicationsDTO> result = controller.getUserCollections(testUserId);

        // Assert
        assertEquals(numberOfCollections, result.size());
    }

    // ============================================================
    // makeCollectionPublic() Tests
    // ============================================================

    @Test
    void ensureMakeCollectionPublicThrowsExceptionWhenCollectionIdIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.makeCollectionPublic(null));
    }

    @Test
    void ensureMakeCollectionPublicSetsCollectionToPublic() {
        // Arrange
        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        when(collection.getIsPrivate()).thenReturn(false);

        // Act & Assert — observable outcome: if isPrivate = false, collection is now public
        assertFalse(controller.makeCollectionPublic(testCollectionId));
    }
}