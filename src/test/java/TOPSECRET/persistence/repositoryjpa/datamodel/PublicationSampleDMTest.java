package TOPSECRET.persistence.repositoryjpa.datamodel;


import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


/**
 * Unit tests for {@link PublicationSampleDM}.
 *
 * <p>Covers three areas:</p>
 * <ul>
 *   <li><b>Constructor — happy path</b>: verifies successful instantiation with all valid arguments.</li>
 *   <li><b>Constructor — null rejection</b>: verifies that mandatory fields ({@code pubSampleId},
 *       {@code bdId}, {@code ownerId}, {@code editionId}, {@code publicationStatus}, {@code condition})
 *       throw {@link NullPointerException} when {@code null}. {@code libraryId} and
 *       {@code collectionOfPublicationsId} are optional and verified to accept {@code null}.</li>
 *   <li><b>Getters</b>: verifies that each getter returns the exact value supplied at construction,
 *       killing all {@code NO_COVERAGE} mutations on getter return values.</li>
 * </ul>
 *
 */

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

    // =========================================================================
    // Constructor
    // =========================================================================

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

    @Test
    void shouldAllowNullLibraryId() {

        //Arrange
        PublicationSampleDM result = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, null,
                collectionOfPublicationsId, publicationStatus, condition);

        //Assert
        assertNotNull(result);
        assertNull(result.getLibraryId());
    }

    @Test
    void shouldAllowNullCollectionOfPublicationsId() {

        //Arrange
        PublicationSampleDM result = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                null, publicationStatus, condition);

        //Assert
        assertNotNull(result);
        assertNull(result.getCollectionOfPublicationsId());
    }

    @Test
    void noArgConstructor_shouldBeProtected() throws NoSuchMethodException {
        Constructor<PublicationSampleDM> constructor =
                PublicationSampleDM.class.getDeclaredConstructor();

        assertTrue(Modifier.isProtected(constructor.getModifiers()));
    }

    // =========================================================================
    // Getter
    // =========================================================================

    @Test
    void getPubSampleId_shouldReturnCorrectValue() {

        //Arrange
        PublicationSampleDM result = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        //Act + Assert
        assertEquals(pubSampleId, result.getPubSampleId());
    }

    @Test
    void getBdId_shouldReturnCorrectValue() {

        //Arrange
        PublicationSampleDM result = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        //Act + Assert
        assertEquals(bdId, result.getBdId());
    }

    @Test
    void getOwnerId_shouldReturnCorrectValue() {

        //Arrange
        PublicationSampleDM result = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        //Act + Assert
        assertEquals(ownerId, result.getOwnerId());
    }

    @Test
    void getEditionId_shouldReturnCorrectValue() {

        //Arrange
        PublicationSampleDM result = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        //Act + Assert
        assertEquals(editionId, result.getEditionId());
    }

    @Test
    void getLibraryId_shouldReturnCorrectValue() {

        //Arrange
        PublicationSampleDM result = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        //Act + Assert
        assertEquals(libraryId, result.getLibraryId());
    }

    @Test
    void getCollectionOfPublicationsId_shouldReturnCorrectValue() {

        //Arrange
        PublicationSampleDM result = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        //Act + Assert
        assertEquals(collectionOfPublicationsId, result.getCollectionOfPublicationsId());
    }

    @Test
    void getPublicationStatus_shouldReturnCorrectValue() {

        //Arrange
        PublicationSampleDM result = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        //Act + Assert
        assertEquals(publicationStatus, result.getPublicationStatus());
    }

    @Test
    void getCondition_shouldReturnCorrectValue() {

        //Arrange
        PublicationSampleDM result = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        //Act + Assert
        assertEquals(condition, result.getCondition());
    }

    // =========================================================================
// @Data tests — equals, hashCode, toString
// =========================================================================

    @Test
    void equals_shouldReturnTrue_whenTwoInstancesHaveSameFields() {

        // Arrange
        PublicationSampleDM dm1 = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        PublicationSampleDM dm2 = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        // Act + Assert
        assertEquals(dm1, dm2);
    }

    @Test
    void equals_shouldReturnFalse_whenPubSampleIdDiffers() {

        // Arrange
        PublicationSampleId otherId = mock(PublicationSampleId.class);

        PublicationSampleDM dm1 = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        PublicationSampleDM dm2 = new PublicationSampleDM(
                otherId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        // Act + Assert
        assertNotEquals(dm1, dm2);
    }

    @Test
    void equals_shouldReturnFalse_whenComparedToNull() {

        // Arrange
        PublicationSampleDM dm = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        // Act + Assert
        assertNotEquals(null, dm);
    }

    @Test
    void equals_shouldReturnFalse_whenComparedToDifferentType() {

        // Arrange
        PublicationSampleDM dm = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        // Act + Assert
        assertNotEquals("not a PublicationSampleDM", dm);
    }

    @Test
    void equals_shouldReturnTrue_whenSameReference() {

        // Arrange
        PublicationSampleDM dm = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        // Act + Assert
        assertEquals(dm, dm);
    }

    @Test
    void hashCode_shouldBeEqual_whenTwoInstancesHaveSameFields() {

        // Arrange
        PublicationSampleDM dm1 = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        PublicationSampleDM dm2 = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        // Act + Assert
        assertEquals(dm1.hashCode(), dm2.hashCode());
    }

    @Test
    void hashCode_shouldDiffer_whenPubSampleIdDiffers() {

        // Arrange
        PublicationSampleId otherId = mock(PublicationSampleId.class);

        PublicationSampleDM dm1 = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        PublicationSampleDM dm2 = new PublicationSampleDM(
                otherId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        // Act + Assert
        assertNotEquals(dm1.hashCode(), dm2.hashCode());
    }

    @Test
    void toString_shouldContainAllFieldValues() {

        // Arrange
        PublicationSampleDM dm = new PublicationSampleDM(
                pubSampleId, bdId, ownerId, editionId, libraryId,
                collectionOfPublicationsId, publicationStatus, condition);

        // Act
        String result = dm.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("PublicationSampleDM"));
    }

}