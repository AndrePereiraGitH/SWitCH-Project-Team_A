package TOPSECRET.persistence.repositoryjpa.datamodel;


import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class PublicationSampleDMTest {

    private PublicationSampleId pubSampleId;
    private BibliographicalDataID bdId;
    private UserId ownerId;
    private EditionID editionId;
    private LibraryId libraryId;
    private CollectionOfPublicationsID collectionOfPublicationsId;
    private PublicationStatus publicationStatus;
    private Condition condition;

    @BeforeEach
    void setup(){
        pubSampleId = mock(PublicationSampleId.class);
        bdId = mock(BibliographicalDataID.class);
        ownerId = mock(UserId.class);
        editionId = mock(EditionID.class);
        libraryId = mock(LibraryId.class);
        collectionOfPublicationsId = mock(CollectionOfPublicationsID.class);
        publicationStatus = PublicationStatus.READ;
        condition = Condition.LIKE_NEW;
    }


    @Test
    void constructorShouldInitializeEveryFieldSuccessfully() {

        //Act
        PublicationSampleDM pSampleDM =
                new PublicationSampleDM(pubSampleId,bdId,ownerId,editionId,libraryId,collectionOfPublicationsId,publicationStatus,condition);

        //Assert
        assertNotNull(pSampleDM);

    }

    @Test
    void shouldThrowException_WhenPubSampleIdIsNull() {

        //Assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleDM(null, bdId, ownerId, editionId, libraryId,
                        collectionOfPublicationsId, publicationStatus, condition));
    }

    @Test
    void shouldThrowException_WhenBdIdIsNull() {

        //Assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleDM(pubSampleId, null, ownerId, editionId, libraryId,
                        collectionOfPublicationsId, publicationStatus, condition));
    }

    @Test
    void shouldThrowException_WhenOwnerIdIsNull() {

        //Assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleDM(pubSampleId, bdId, null, editionId, libraryId,
                        collectionOfPublicationsId, publicationStatus, condition));
    }

    @Test
    void shouldThrowException_WhenEditionIdIsNull() {

        //Assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleDM(pubSampleId, bdId, ownerId, null, libraryId,
                        collectionOfPublicationsId, publicationStatus, condition));
    }

    @Test
    void shouldThrowException_WhenLibraryIdIsNull() {

        //Assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleDM(pubSampleId, bdId, ownerId, editionId, null,
                        collectionOfPublicationsId, publicationStatus, condition));
    }

    @Test
    void shouldThrowException_WhenCollectionOfPublicationsIdIsNull() {

        //Assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleDM(pubSampleId, bdId, ownerId, editionId, libraryId,
                        null, publicationStatus, condition));
    }

    @Test
    void shouldThrowException_WhenPublicationStatusIsNull() {

        //Assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleDM(pubSampleId, bdId, ownerId, editionId, libraryId,
                        collectionOfPublicationsId, null, condition));
    }

    @Test
    void shouldThrowException_WhenConditionIsNull() {

        //Assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSampleDM(pubSampleId, bdId, ownerId, editionId, libraryId,
                        collectionOfPublicationsId, publicationStatus, null));
    }

}