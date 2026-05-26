package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US018CollectionOfPublicationsDTO;
import TOPSECRET.dto.US018PublicationSampleDTO;
import TOPSECRET.dto.US018PublicationSampleMapper;
import TOPSECRET.services.PublicationSampleService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ToAddPublicationToAListOfMineController}.
 *
 * <p>
 * These tests validate the behavior of the controller responsible for
 * the use case <b>US018 - Add Publication to Collection</b>.
 * </p>
 *
 * <p>
 * The test suite focuses on:
 * <ul>
 * <li>Retrieving user collections</li>
 * <li>Retrieving publications available for a collection</li>
 * <li>Delegating publication assignment operations and returning the updated entity</li>
 * <li>Validation of invalid input arguments</li>
 * </ul>
 * </p>
 *
 * <p>
 * Dependencies are isolated using Mockito mocks in order to verify only
 * the controller orchestration responsibilities.
 * </p>
 */
class ToAddPublicationToAListOfMineControllerTest {

    @Test
    void shouldReturnUserCollections() {
        // Arrange
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        PublicationSampleService publicationService = mock(PublicationSampleService.class);
        US018PublicationSampleMapper publicationSampleMapper = mock(US018PublicationSampleMapper.class);
        UserId userId = mock(UserId.class);

        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationService, publicationSampleMapper);

        List<CollectionOfPublications> collections = new ArrayList<>();
        when(collectionRepo.getUserCollections(userId)).thenReturn(collections);

        // Act
        List<US018CollectionOfPublicationsDTO> result = controller.getUserCollections();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAvailablePublicationsUsingServiceMethod() {
        // Arrange
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        PublicationSampleService publicationService = mock(PublicationSampleService.class);
        US018PublicationSampleMapper publicationSampleMapper = mock(US018PublicationSampleMapper.class);
        UserId userId = mock(UserId.class);

        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationService, publicationSampleMapper);

        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        PublicationSample publication = mock(PublicationSample.class);
        US018PublicationSampleDTO dto = mock(US018PublicationSampleDTO.class);

        when(publicationService.findAvailableForCollection(userId, collectionId))
                .thenReturn(List.of(publication));
        when(publicationSampleMapper.toDTOs(List.of(publication)))
                .thenReturn(List.of(dto));

        // Act
        List<US018PublicationSampleDTO> result = controller.getPublicationsForCollection(collectionId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void shouldThrowExceptionWhenCollectionIdIsNull() {
        // Arrange
        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        mock(UserId.class),
                        mock(ICollectionOfPublicationsRepository.class),
                        mock(PublicationSampleService.class),
                        mock(US018PublicationSampleMapper.class));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.getPublicationsForCollection(null));
    }

    @Test
    void shouldAssignPublicationsToCollectionAndReturnSample() {
        // Arrange
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        PublicationSampleService publicationService = mock(PublicationSampleService.class);
        US018PublicationSampleMapper publicationSampleMapper = mock(US018PublicationSampleMapper.class);
        UserId userId = mock(UserId.class);

        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationService, publicationSampleMapper);

        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        PublicationSampleId publicationId = mock(PublicationSampleId.class);

        // Mock the expected returned PublicationSample
        PublicationSample expectedSample = mock(PublicationSample.class);

        when(publicationService.assignPublicationToCollection(collectionId, publicationId, userId))
                .thenReturn(expectedSample);

        // Act
        PublicationSample result = controller.addPublicationToCollection(collectionId, publicationId, userId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedSample, result);
    }

    @Test
    void shouldThrowExceptionWhenCollectionIdIsNullInAdd() {
        // Arrange
        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        mock(UserId.class),
                        mock(ICollectionOfPublicationsRepository.class),
                        mock(PublicationSampleService.class),
                        mock(US018PublicationSampleMapper.class));

        PublicationSampleId publicationId = mock(PublicationSampleId.class);
        UserId userId = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToCollection(null, publicationId, userId));
    }

    @Test
    void shouldThrowExceptionWhenPublicationIdIsNull() {
        // Arrange
        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        mock(UserId.class),
                        mock(ICollectionOfPublicationsRepository.class),
                        mock(PublicationSampleService.class),
                        mock(US018PublicationSampleMapper.class));

        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        UserId userId = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToCollection(collectionId, null, userId));
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNullInAdd() {
        // Arrange
        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        mock(UserId.class),
                        mock(ICollectionOfPublicationsRepository.class),
                        mock(PublicationSampleService.class),
                        mock(US018PublicationSampleMapper.class));

        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        PublicationSampleId publicationId = mock(PublicationSampleId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToCollection(collectionId, publicationId, null));
    }

    @Test
    void shouldReturnMappedCollectionsWithContent() {
        // Arrange
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        PublicationSampleService publicationService = mock(PublicationSampleService.class);
        US018PublicationSampleMapper publicationSampleMapper = mock(US018PublicationSampleMapper.class);
        UserId userId = mock(UserId.class);

        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationService, publicationSampleMapper);

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(collection.id()).thenReturn(mock(CollectionOfPublicationsId.class));
        when(collection.getCollectionName()).thenReturn("C1");

        when(collectionRepo.getUserCollections(userId)).thenReturn(List.of(collection));

        // Act
        List<US018CollectionOfPublicationsDTO> result = controller.getUserCollections();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnNullWhenServiceFailsToAssign() {
        // Arrange
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        PublicationSampleService publicationService = mock(PublicationSampleService.class);
        US018PublicationSampleMapper publicationSampleMapper = mock(US018PublicationSampleMapper.class);
        UserId userId = mock(UserId.class);

        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationService, publicationSampleMapper);

        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        PublicationSampleId pubId = mock(PublicationSampleId.class);

        // Simulate a failure to find/save the entity returning null
        when(publicationService.assignPublicationToCollection(collectionId, pubId, userId))
                .thenReturn(null);

        // Act
        PublicationSample result = controller.addPublicationToCollection(collectionId, pubId, userId);

        // Assert
        assertNull(result);
    }
}