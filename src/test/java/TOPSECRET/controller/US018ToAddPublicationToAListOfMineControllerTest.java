package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US018CollectionOfPublicationsDTO;
import TOPSECRET.dto.US018PublicationSampleDTO;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US018ToAddPublicationToAListOfMineControllerTest {

    @Test
    void shouldReturnUserCollections() {

        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        IPublicationSampleRepository publicationRepo = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalRepo = mock(IBibliographicalDataRepository.class);
        UserId userId = mock(UserId.class);

        US018ToAddPublicationToAListOfMineController controller =
                new US018ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationRepo, bibliographicalRepo);

        List<CollectionOfPublications> collections = new ArrayList<>();

        when(collectionRepo.getUserCollections(userId)).thenReturn(collections);

        List<US018CollectionOfPublicationsDTO> result =
                controller.getUserCollections();

        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnAvailablePublications() {

        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        IPublicationSampleRepository publicationRepo = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalRepo = mock(IBibliographicalDataRepository.class);
        UserId userId = mock(UserId.class);

        US018ToAddPublicationToAListOfMineController controller =
                new US018ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationRepo, bibliographicalRepo);

        CollectionOfPublicationsID collectionId = mock(CollectionOfPublicationsID.class);

        PublicationSample publication = mock(PublicationSample.class);
        PublicationSampleId id = mock(PublicationSampleId.class);
        BibliographicalDataID bId = mock(BibliographicalDataID.class);
        BibliographicalData data = mock(BibliographicalData.class);

        when(publication.id()).thenReturn(id);
        when(publication.getBibliographicalDataID()).thenReturn(bId);

        when(bibliographicalRepo.findBibliographicalDataById(bId))
                .thenReturn(Optional.of(data));

        when(data.getTitle()).thenReturn("Title");

        when(publicationRepo.getAllPublicationsInUserLibrary(userId))
                .thenReturn(List.of(publication));

        when(publicationRepo.getPublicationsByCollectionID(collectionId))
                .thenReturn(new ArrayList<>());

        List<US018PublicationSampleDTO> result =
                controller.getPublicationsForCollection(collectionId);

        assertEquals(1, result.size());
        assertEquals("Title", result.get(0).title());
    }

    @Test
    void shouldThrowExceptionWhenCollectionIdIsNull() {

        US018ToAddPublicationToAListOfMineController controller =
                new US018ToAddPublicationToAListOfMineController(
                        mock(UserId.class),
                        mock(ICollectionOfPublicationsRepository.class),
                        mock(IPublicationSampleRepository.class),
                        mock(IBibliographicalDataRepository.class));

        assertThrows(IllegalArgumentException.class,
                () -> controller.getPublicationsForCollection(null));
    }

    @Test
    void shouldAssignPublicationsToCollection() {

        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        IPublicationSampleRepository publicationRepo = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalRepo = mock(IBibliographicalDataRepository.class);
        UserId userId = mock(UserId.class);

        US018ToAddPublicationToAListOfMineController controller =
                new US018ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationRepo, bibliographicalRepo);

        CollectionOfPublicationsID collectionId = mock(CollectionOfPublicationsID.class);
        PublicationSampleId publicationId = mock(PublicationSampleId.class);

        when(publicationRepo.assignPublicationsToCollection(
                eq(collectionId), anyList()))
                .thenReturn(true);

        boolean result =
                controller.addPublicationToCollection(
                        collectionId,
                        List.of(publicationId)
                );

        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenCollectionIdIsNullInAdd() {

        US018ToAddPublicationToAListOfMineController controller =
                new US018ToAddPublicationToAListOfMineController(
                        mock(UserId.class),
                        mock(ICollectionOfPublicationsRepository.class),
                        mock(IPublicationSampleRepository.class),
                        mock(IBibliographicalDataRepository.class));

        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToCollection(null, new ArrayList<>()));
    }

    @Test
    void shouldThrowExceptionWhenPublicationListIsNull() {

        US018ToAddPublicationToAListOfMineController controller =
                new US018ToAddPublicationToAListOfMineController(
                        mock(UserId.class),
                        mock(ICollectionOfPublicationsRepository.class),
                        mock(IPublicationSampleRepository.class),
                        mock(IBibliographicalDataRepository.class));

        CollectionOfPublicationsID collectionId = mock(CollectionOfPublicationsID.class);

        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToCollection(collectionId, null));
    }

    @Test
    void shouldThrowExceptionWhenPublicationListIsEmpty() {

        US018ToAddPublicationToAListOfMineController controller =
                new US018ToAddPublicationToAListOfMineController(
                        mock(UserId.class),
                        mock(ICollectionOfPublicationsRepository.class),
                        mock(IPublicationSampleRepository.class),
                        mock(IBibliographicalDataRepository.class));

        CollectionOfPublicationsID collectionId = mock(CollectionOfPublicationsID.class);

        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToCollection(collectionId, new ArrayList<>()));
    }

    @Test
    void shouldReturnMappedCollectionsWithContent() {

        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        IPublicationSampleRepository publicationRepo = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalRepo = mock(IBibliographicalDataRepository.class);
        UserId userId = mock(UserId.class);

        US018ToAddPublicationToAListOfMineController controller =
                new US018ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationRepo, bibliographicalRepo
                );

        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        when(collection.id()).thenReturn(mock(CollectionOfPublicationsID.class));
        when(collection.getCollectionName()).thenReturn("C1");

        when(collectionRepo.getUserCollections(userId))
                .thenReturn(List.of(collection));

        List<US018CollectionOfPublicationsDTO> result =
                controller.getUserCollections();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnFalseWhenRepositoryFails() {

        ICollectionOfPublicationsRepository collectionRepo = mock(ICollectionOfPublicationsRepository.class);
        IPublicationSampleRepository publicationRepo = mock(IPublicationSampleRepository.class);
        IBibliographicalDataRepository bibliographicalRepo = mock(IBibliographicalDataRepository.class);
        UserId userId = mock(UserId.class);

        US018ToAddPublicationToAListOfMineController controller =
                new US018ToAddPublicationToAListOfMineController(
                        userId, collectionRepo, publicationRepo, bibliographicalRepo
                );

        CollectionOfPublicationsID collectionId = mock(CollectionOfPublicationsID.class);
        PublicationSampleId pubId = mock(PublicationSampleId.class);

        when(publicationRepo.assignPublicationsToCollection(collectionId, List.of(pubId)))
                .thenReturn(false);

        boolean result =
                controller.addPublicationToCollection(collectionId, List.of(pubId));

        assertFalse(result);
    }
}