package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;

import TOPSECRET.dto.US019CollectionOfPublicationsDTO;
import TOPSECRET.services.CollectionOfPublicationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link ToPubliclyShareCollectionOfPublicationsController} class.
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
class ToPubliclyShareCollectionOfPublicationsControllerTest {

    private ICollectionOfPublicationsRepository repositoryDouble;
    private CollectionOfPublicationsService collectionService;
    private ToPubliclyShareCollectionOfPublicationsController controller;


    @BeforeEach
    void setUp() {
        repositoryDouble = mock(ICollectionOfPublicationsRepository.class);
        collectionService = mock(CollectionOfPublicationsService.class);
        controller = new ToPubliclyShareCollectionOfPublicationsController(repositoryDouble, collectionService);
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
        NullPointerException ex = assertThrows(NullPointerException.class, () ->
                new ToPubliclyShareCollectionOfPublicationsController(null, collectionService));

        assertEquals("CollectionOfPublicationsRepository cannot be null", ex.getMessage());
    }

    @Test
    void ensureThrowsExceptionWhenServiceIsNull() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ToPubliclyShareCollectionOfPublicationsController(repositoryDouble, null));
        assertEquals("CollectionOfPublicationService cannot be null", ex.getMessage());
    }

    // ===========================================================================
    // ----------------- GET USER COLLECTIONS TESTS ------------------------------
    // ===========================================================================

    @Test
    void getUserCollections_ShouldThrowException_WhenUserIdIsNull() {
        // Arrange
        UserId userId = null;

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.getUserCollections(userId)
        );

        // Assert
        assertEquals("UserId cannot be null", exception.getMessage());
    }

    @Test
    void getUserCollections_ShouldReturnEmptyList_WhenUserHasNoCollections() {
        // Arrange
        UserId userId = mock(UserId.class);

        when(repositoryDouble.findAllByOwnerId(userId))
                .thenReturn(List.of());

        // Act
        List<US019CollectionOfPublicationsDTO> result =
                controller.getUserCollections(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    void getUserCollections_ShouldReturnDTOList_WhenUserHasCollections() {
        // Arrange
        UserId userId = mock(UserId.class);

        CollectionOfPublications collection1 = mock(CollectionOfPublications.class);
        CollectionOfPublications collection2 = mock(CollectionOfPublications.class);

        CollectionOfPublicationsId collectionId1 = mock(CollectionOfPublicationsId.class);
        CollectionOfPublicationsId collectionId2 = mock(CollectionOfPublicationsId.class);

        GenreId genreId1 = mock(GenreId.class);
        GenreId genreId2 = mock(GenreId.class);

        when(collection1.id()).thenReturn(collectionId1);
        when(collection1.getCollectionGenre()).thenReturn(genreId1);

        when(collection2.id()).thenReturn(collectionId2);
        when(collection2.getCollectionGenre()).thenReturn(genreId2);

        when(collectionId1.getId()).thenReturn("collection-1");
        when(collectionId2.getId()).thenReturn("collection-2");

        when(genreId1.getID()).thenReturn("genre-1");
        when(genreId2.getID()).thenReturn("genre-2");

        when(collection1.getOwnerId()).thenReturn(userId);
        when(collection2.getOwnerId()).thenReturn(userId);

        when(collection1.getCollectionName()).thenReturn("Collection1");
        when(collection2.getCollectionName()).thenReturn("Collection2");

        when(collection1.getOwnerId().getUsername()).thenReturn("owner name1");
        when(collection2.getOwnerId().getUsername()).thenReturn("ner name2");


        when(repositoryDouble.findAllByOwnerId(userId))
                .thenReturn(List.of(collection1, collection2));

        // Act
        List<US019CollectionOfPublicationsDTO> result =
                controller.getUserCollections(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

    }

    // =============================================================================
    // ---------------------- MAKE COLLECTION PUBLIC -------------------------------
    // =============================================================================

    @Test
    void makeCollectionPublic_ShouldThrowException_WhenDTOIsNull() {
        // Arrange
        US019CollectionOfPublicationsDTO selectedCollectionDTO = null;

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.makeCollectionPublic(selectedCollectionDTO)
        );

        // Assert
        assertEquals("CollectionId cannot be null", exception.getMessage());
    }

    @Test
    void makeCollectionPublic_ShouldReturnTrue_WhenServiceReturnsTrue()
            throws InstanceNotFoundException {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        US019CollectionOfPublicationsDTO selectedCollectionDTO =
                mock(US019CollectionOfPublicationsDTO.class);

        when(selectedCollectionDTO.id()).thenReturn(collectionId);
        when(collectionService.makeCollectionPublic(collectionId)).thenReturn(true);

        // Act
        boolean result = controller.makeCollectionPublic(selectedCollectionDTO);

        // Assert
        assertTrue(result);
    }

    @Test
    void makeCollectionPublic_ShouldReturnFalse_WhenServiceReturnsFalse()
            throws InstanceNotFoundException {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        US019CollectionOfPublicationsDTO selectedCollectionDTO =
                mock(US019CollectionOfPublicationsDTO.class);

        when(selectedCollectionDTO.id()).thenReturn(collectionId);
        when(collectionService.makeCollectionPublic(collectionId)).thenReturn(false);

        // Act
        boolean result = controller.makeCollectionPublic(selectedCollectionDTO);

        // Assert
        assertFalse(result);
    }

    @Test
    void makeCollectionPublic_ShouldThrowException_WhenServiceThrowsInstanceNotFoundException()
            throws InstanceNotFoundException {

        // Arrange
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        US019CollectionOfPublicationsDTO selectedCollectionDTO =
                mock(US019CollectionOfPublicationsDTO.class);

        when(selectedCollectionDTO.id()).thenReturn(collectionId);

        InstanceNotFoundException expectedException =
                new InstanceNotFoundException("Collection not found");

        when(collectionService.makeCollectionPublic(collectionId))
                .thenThrow(expectedException);

        // Act
        InstanceNotFoundException exception = assertThrows(
                InstanceNotFoundException.class,
                () -> controller.makeCollectionPublic(selectedCollectionDTO)
        );

        // Assert
        assertEquals("Collection not found", exception.getMessage());
    }

    // =============================================================================
// ---------------------- SHARE COLLECTION UNTIL -------------------------------
// =============================================================================

    @Test
    void shareCollectionUntil_ShouldReturnTrue_WhenServiceReturnsTrue()
            throws InstanceNotFoundException {

        // Arrange
        US019CollectionOfPublicationsDTO selectedCollectionDto =
                mock(US019CollectionOfPublicationsDTO.class);

        CollectionOfPublicationsId selectedCollectionId =
                mock(CollectionOfPublicationsId.class);

        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);

        when(selectedCollectionDto.id()).thenReturn(selectedCollectionId);

        when(collectionService.makeCollectionPublicUntil(
                selectedCollectionId,
                publicUntil
        )).thenReturn(true);

        // Act
        boolean result = controller.shareCollectionUntil(
                selectedCollectionDto,
                publicUntil
        );

        // Assert
        assertTrue(result);
    }

    @Test
    void shareCollectionUntil_ShouldReturnFalse_WhenServiceReturnsFalse()
            throws InstanceNotFoundException {

        // Arrange
        US019CollectionOfPublicationsDTO selectedCollectionDto =
                mock(US019CollectionOfPublicationsDTO.class);

        CollectionOfPublicationsId selectedCollectionId =
                mock(CollectionOfPublicationsId.class);

        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);

        when(selectedCollectionDto.id()).thenReturn(selectedCollectionId);

        when(collectionService.makeCollectionPublicUntil(
                selectedCollectionId,
                publicUntil
        )).thenReturn(false);

        // Act
        boolean result = controller.shareCollectionUntil(
                selectedCollectionDto,
                publicUntil
        );

        // Assert
        assertFalse(result);
    }

    @Test
    void shareCollectionUntil_ShouldThrowException_WhenSelectedCollectionDtoIsNull() {

        // Arrange
        US019CollectionOfPublicationsDTO selectedCollectionDto = null;
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);

        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> controller.shareCollectionUntil(selectedCollectionDto, publicUntil)
        );

        // Assert
        assertEquals("selected collection Id cannot be null", exception.getMessage());
    }

    @Test
    void shareCollectionUntil_ShouldThrowException_WhenPublicUntilIsNull() {

        // Arrange
        US019CollectionOfPublicationsDTO selectedCollectionDto =
                mock(US019CollectionOfPublicationsDTO.class);

        LocalDateTime publicUntil = null;

        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> controller.shareCollectionUntil(selectedCollectionDto, publicUntil)
        );

        // Assert
        assertEquals("public until date time cannot be null", exception.getMessage());
    }

    @Test
    void shareCollectionUntil_ShouldThrowInstanceNotFoundException_WhenServiceThrows()
            throws InstanceNotFoundException {

        // Arrange
        US019CollectionOfPublicationsDTO selectedCollectionDto =
                mock(US019CollectionOfPublicationsDTO.class);

        CollectionOfPublicationsId selectedCollectionId =
                mock(CollectionOfPublicationsId.class);

        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);

        when(selectedCollectionDto.id()).thenReturn(selectedCollectionId);

        when(collectionService.makeCollectionPublicUntil(
                selectedCollectionId,
                publicUntil
        )).thenThrow(new InstanceNotFoundException("Collection not found"));

        // Act
        InstanceNotFoundException exception = assertThrows(
                InstanceNotFoundException.class,
                () -> controller.shareCollectionUntil(selectedCollectionDto, publicUntil)
        );

        // Assert
        assertEquals("Collection not found", exception.getMessage());
    }



}