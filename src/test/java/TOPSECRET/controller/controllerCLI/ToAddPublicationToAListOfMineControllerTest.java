package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US018CollectionOfPublicationsDTO;
import TOPSECRET.dto.US018PublicationSampleDTO;
import TOPSECRET.dto.US018PublicationSampleMapper;

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
 *     <li>Retrieving user collections</li>
 *     <li>Retrieving publications available for a collection</li>
 *     <li>Delegating publication assignment operations</li>
 *     <li>Validation of invalid input arguments</li>
 * </ul>
 * </p>
 *
 * <p>
 * Dependencies are isolated using Mockito mocks in order to verify only
 * the controller orchestration responsibilities.
 * </p>
 *
 * <p>
 * The controller itself contains no business logic and is expected only
 * to coordinate repository access, mapper transformations, and delegation
 * of domain operations.
 * </p>
 */
class ToAddPublicationToAListOfMineControllerTest {

    @Test
    void shouldReturnUserCollections() {

        // ARRANGE
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        IPublicationSampleRepository publicationRepo = mock(IPublicationSampleRepository.class);
        US018PublicationSampleMapper publicationSampleMapper = mock(US018PublicationSampleMapper.class);
        UserId userId = mock(UserId.class);

        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationRepo, publicationSampleMapper);

        List<CollectionOfPublications> collections = new ArrayList<>();

        when(collectionRepo.findAllByOwnerId(userId)).thenReturn(collections);

        // ACT
        List<US018CollectionOfPublicationsDTO> result =
                controller.getUserCollections();

        // ASSERT
        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnAvailablePublicationsUsingRepositoryMethod() {

        // ARRANGE
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        IPublicationSampleRepository publicationRepo = mock(IPublicationSampleRepository.class);
        US018PublicationSampleMapper publicationSampleMapper = mock(US018PublicationSampleMapper.class);
        UserId userId = mock(UserId.class);

        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationRepo, publicationSampleMapper);

        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);

        PublicationSample publication = mock(PublicationSample.class);
        PublicationSampleId pubId = mock(PublicationSampleId.class);
        US018PublicationSampleDTO dto = mock(US018PublicationSampleDTO.class);

        when(publication.id()).thenReturn(pubId);

        when(publicationRepo.findAvailableForCollection(userId, collectionId))
                .thenReturn(List.of(publication));

        when(publicationSampleMapper.toDTOs(List.of(publication)))
                .thenReturn(List.of(dto));

        // ACT
        List<US018PublicationSampleDTO> result =
                controller.getPublicationsForCollection(collectionId);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void shouldThrowExceptionWhenCollectionIdIsNull() {

        // ARRANGE
        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        mock(UserId.class),
                        mock(ICollectionOfPublicationsRepository.class),
                        mock(IPublicationSampleRepository.class),
                        mock(US018PublicationSampleMapper.class));

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> controller.getPublicationsForCollection(null));
    }

    @Test
    void shouldAssignPublicationsToCollection() {

        // ARRANGE
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        IPublicationSampleRepository publicationRepo = mock(IPublicationSampleRepository.class);
        US018PublicationSampleMapper publicationSampleMapper = mock(US018PublicationSampleMapper.class);
        UserId userId = mock(UserId.class);

        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationRepo, publicationSampleMapper);

        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        PublicationSampleId publicationId = mock(PublicationSampleId.class);

        when(publicationRepo.assignPublicationsToCollection(
                eq(collectionId), anyList()))
                .thenReturn(true);

        // ACT
        boolean result =
                controller.addPublicationToCollection(
                        collectionId,
                        List.of(publicationId)
                );

        // ASSERT
        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenCollectionIdIsNullInAdd() {

        // ARRANGE
        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        mock(UserId.class),
                        mock(ICollectionOfPublicationsRepository.class),
                        mock(IPublicationSampleRepository.class),
                        mock(US018PublicationSampleMapper.class));

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToCollection(null, new ArrayList<>()));
    }

    @Test
    void shouldThrowExceptionWhenPublicationListIsNull() {

        // ARRANGE
        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        mock(UserId.class),
                        mock(ICollectionOfPublicationsRepository.class),
                        mock(IPublicationSampleRepository.class),
                        mock(US018PublicationSampleMapper.class));

        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToCollection(collectionId, null));
    }

    @Test
    void shouldThrowExceptionWhenPublicationListIsEmpty() {

        // ARRANGE
        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        mock(UserId.class),
                        mock(ICollectionOfPublicationsRepository.class),
                        mock(IPublicationSampleRepository.class),
                        mock(US018PublicationSampleMapper.class));

        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToCollection(collectionId, new ArrayList<>()));
    }

    @Test
    void shouldReturnMappedCollectionsWithContent() {

        // ARRANGE
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        IPublicationSampleRepository publicationRepo = mock(IPublicationSampleRepository.class);
        US018PublicationSampleMapper publicationSampleMapper = mock(US018PublicationSampleMapper.class);
        UserId userId = mock(UserId.class);

        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationRepo, publicationSampleMapper
                );

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(collection.id()).thenReturn(mock(CollectionOfPublicationsId.class));
        when(collection.getCollectionName()).thenReturn("C1");

        when(collectionRepo.findAllByOwnerId(userId))
                .thenReturn(List.of(collection));

        // ACT
        List<US018CollectionOfPublicationsDTO> result =
                controller.getUserCollections();

        // ASSERT
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnFalseWhenRepositoryFails() {

        // ARRANGE
        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        IPublicationSampleRepository publicationRepo = mock(IPublicationSampleRepository.class);
        US018PublicationSampleMapper publicationSampleMapper = mock(US018PublicationSampleMapper.class);
        UserId userId = mock(UserId.class);

        ToAddPublicationToAListOfMineController controller =
                new ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationRepo, publicationSampleMapper
                );

        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);
        PublicationSampleId pubId = mock(PublicationSampleId.class);

        when(publicationRepo.assignPublicationsToCollection(collectionId, List.of(pubId)))
                .thenReturn(false);

        // ACT
        boolean result =
                controller.addPublicationToCollection(collectionId, List.of(pubId));

        // ASSERT
        assertFalse(result);
    }
}