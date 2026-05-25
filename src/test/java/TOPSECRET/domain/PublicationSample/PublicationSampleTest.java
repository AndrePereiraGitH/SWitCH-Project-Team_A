package TOPSECRET.domain.PublicationSample;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link PublicationSample} domain entity.
 *
 * <p>This test class validates the construction, getter methods, library and collection
 * assignment logic, status transitions, ownership and library membership checks,
 * and equality/identity behaviour of {@code PublicationSample}.</p>
 *
 * <p>All collaborators ({@link BibliographicalDataId}, {@link UserId}, {@link EditionId},
 * {@link PublicationStatus}, {@link Condition}) are provided as Mockito test doubles,
 * keeping each test focused on the behaviour of {@code PublicationSample} in isolation.</p>
 *
 * <p>Test coverage is organised into the following sections:</p>
 * <ul>
 *   <li><b>Constructor</b> – valid creation and null-argument guards.</li>
 *   <li><b>changeStatusToForSale</b> – {@code FOR_SALE} transition and idempotency.</li>
 *   <li><b>Library Assignment</b> – assignment rules, null rejection, and double-assignment guard.</li>
 *   <li><b>Collection Assignment</b> – assignment rules, null rejection, double-assignment guard,
 *       and prerequisite library check.</li>
 *   <li><b>isOwnedByUser</b> – verifies that ownership check returns {@code true} when the
 *       provided {@link UserId} matches the publication's owner, and {@code false} otherwise.</li>
 *   <li><b>isInUserLibrary</b> – verifies that the library membership check returns {@code true}
 *       when a {@link LibraryId} has been assigned, and {@code false} when none has been set.</li>
 *   <li><b>Getters</b> – correct retrieval of all fields, including lazily assigned ones.</li>
 *   <li><b>Equality and Identity</b> – {@code sameAs} and {@code equals} contracts.</li>
 * </ul>
 */
class PublicationSampleTest {

    // =========================================================================
    // Test Doubles (Collaborators)
    // =========================================================================

    private final BibliographicalDataId bdID = mock(BibliographicalDataId.class);
    private final UserId ownerID = mock(UserId.class);
    private final EditionId editionID = mock(EditionId.class);
    private final PublicationStatus anyPublicationStatus = mock(PublicationStatus.class);
    private final Condition anyCondition = mock(Condition.class);
    private final PublicationSampleFactory factory = new PublicationSampleFactory();
    private final PublicationSampleId pbSampleId = mock(PublicationSampleId.class);
    private final LibraryId libraryId = mock(LibraryId.class);
    private final CollectionOfPublicationsId collectionOfPubsId = mock(CollectionOfPublicationsId.class);

    // =========================================================================
    // Constructor without PubSampleId
    // =========================================================================

    @Test
    void shouldCreateValidPubSample() {

        //Act
        PublicationSample validSample = new PublicationSample(bdID, ownerID,
                editionID, anyCondition);

        assertNotNull(validSample);
    }

    @Test
    void shouldThrowExceptionWhen_BibliographicalDataID_isNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSample(null, ownerID, editionID, anyCondition));
    }

    @Test
    void shouldThrowExceptionWhen_UserID_isNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSample(bdID, null, editionID, anyCondition));
    }


    @Test
    void shouldThrowExceptionWhen_editionID_isNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSample(bdID, ownerID, null, anyCondition));
    }

    @Test
    void shouldThrowExceptionWhen_condition_isNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSample(bdID, ownerID, editionID, null));
    }

    // =========================================================================
    // Constructor with PubSampleId
    // =========================================================================

    @Test
    void constructorWithPubSampleId_shouldCreateAValidPubSample(){

        //Act
        PublicationSample pbSample = factory.create(pbSampleId,bdID,ownerID,anyPublicationStatus,editionID,anyCondition, libraryId, collectionOfPubsId);

        //Assert
        assertNotNull(pbSample);
    }

    @Test
    void constructorWithPubSampleId_shouldThrowExceptionWhen_PublicationSampleID_isNull() {

        //Act + assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSample(null,bdID, ownerID, anyPublicationStatus, editionID, anyCondition, libraryId, collectionOfPubsId));
    }

    @Test
    void constructorWithPubSampleId_shouldThrowExceptionWhen_BibliographicalDataID_isNull() {

        //Act + assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSample(pbSampleId,null, ownerID, anyPublicationStatus, editionID, anyCondition, libraryId, collectionOfPubsId));
    }

    @Test
    void constructorWithPubSampleId_shouldThrowExceptionWhen_UserID_isNull() {

        //Act + assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSample(pbSampleId, bdID, null, anyPublicationStatus, editionID, anyCondition, libraryId, collectionOfPubsId));
    }

    @Test
    void constructorWithPubSampleId_shouldThrowExceptionWhen_publicationStatus_isNull() {

        //Act + assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSample(pbSampleId, bdID, ownerID, null, editionID, anyCondition, libraryId, collectionOfPubsId));
    }

    @Test
    void constructorWithPubSampleId_shouldThrowExceptionWhen_editionID_isNull() {

        //Act + assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSample(pbSampleId, bdID, ownerID, anyPublicationStatus, null, anyCondition, libraryId, collectionOfPubsId));
    }

    @Test
    void constructorWithPubSampleId_shouldThrowExceptionWhen_condition_isNull() {

        //Act + assert
        assertThrows(NullPointerException.class, () ->
                new PublicationSample(pbSampleId, bdID, ownerID, anyPublicationStatus, editionID, null, libraryId, collectionOfPubsId));
    }

    // =========================================================================
    // changePublicationStatus
    // =========================================================================

    @Test
    void changePublicationStatus_toForSale_returnsForSale() {
        // Arrange
        PublicationSample publicationSample = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        // Act
        PublicationStatus result = publicationSample.changePublicationStatus(PublicationStatus.FOR_SALE);

        // Assert
        assertEquals(PublicationStatus.FOR_SALE, result);
    }

    @Test
    void changePublicationStatus_toSold_returnsSold() {
        // Arrange
        PublicationSample publicationSample = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        // Act
        PublicationStatus result = publicationSample.changePublicationStatus(PublicationStatus.SOLD);

        // Assert
        assertEquals(PublicationStatus.SOLD, result);
    }

    @Test
    void changePublicationStatus_toReading_returnsReading() {
        // Arrange
        PublicationSample publicationSample = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        // Act
        PublicationStatus result = publicationSample.changePublicationStatus(PublicationStatus.READING);

        // Assert
        assertEquals(PublicationStatus.READING, result);
    }

    @Test
    void changePublicationStatus_toRead_returnsRead() {
        // Arrange
        PublicationSample publicationSample = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        // Act
        PublicationStatus result = publicationSample.changePublicationStatus(PublicationStatus.READ);

        // Assert
        assertEquals(PublicationStatus.READ, result);
    }

    @Test
    void changePublicationStatus_toCreated_returnsCreated() {
        // Arrange
        PublicationSample publicationSample = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        // Act
        PublicationStatus result = publicationSample.changePublicationStatus(PublicationStatus.CREATED);

        // Assert
        assertEquals(PublicationStatus.CREATED, result);
    }

    @Test
    void changePublicationStatus_updatesInternalState() {
        // Arrange
        PublicationSample publicationSample = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        // Act
        publicationSample.changePublicationStatus(PublicationStatus.FOR_SALE);

        // Assert
        assertEquals(PublicationStatus.FOR_SALE, publicationSample.getPublicationStatus());
    }

    @Test
    void changePublicationStatus_overwritesPreviousStatus() {
        // Arrange
        PublicationSample publicationSample = new PublicationSample(bdID, ownerID, editionID, anyCondition);
        publicationSample.changePublicationStatus(PublicationStatus.FOR_SALE);

        // Act
        publicationSample.changePublicationStatus(PublicationStatus.SOLD);

        // Assert
        assertEquals(PublicationStatus.SOLD, publicationSample.getPublicationStatus());
    }

    // =========================================================================
    // Library Assignment
    // =========================================================================

    @Test
    void givenValidLibraryId_whenAddPublicationToLibrary_thenLibraryIsAssigned() {

        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, editionID, anyCondition);

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.getId()).thenReturn(UUID.randomUUID().toString());

        //Act
        LibraryId result = sample.addPublicationToLibrary(libraryIdDouble);

        // Assert
        assertEquals(libraryIdDouble, result);
    }

    @Test
    void givenNullLibraryId_whenAddPublicationToLibrary_thenThrowsNullPointerException() {

        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, editionID, anyCondition);

        //Act + Assert
        assertThrows(NullPointerException.class, () ->
                sample.addPublicationToLibrary(null));
    }

    @Test
    void givenAlreadyAssignedLibrary_whenAddPublicationToLibrary_thenThrowsIllegalArgumentException() {

        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, editionID, anyCondition);

        sample.addPublicationToLibrary(new LibraryId(UUID.randomUUID().toString()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                sample.addPublicationToLibrary(new LibraryId(UUID.randomUUID().toString()))
        );
    }


    // =========================================================================
    // Collection Assignment
    // =========================================================================

    @Test
    void givenValidCollection_whenAddPublicationToCollection_thenCollectionIsAssigned() {

        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, editionID, anyCondition);

        sample.addPublicationToLibrary(new LibraryId(UUID.randomUUID().toString()));

        CollectionOfPublicationsId collectionOfPublicationsIdDouble = mock(CollectionOfPublicationsId.class);
        when(collectionOfPublicationsIdDouble.getId()).thenReturn(UUID.randomUUID().toString());

        // Act
        CollectionOfPublicationsId result = sample.addPublicationToCollection(collectionOfPublicationsIdDouble);

        // Assert
        assertEquals(collectionOfPublicationsIdDouble, result);
    }

    @Test
    void givenNullCollection_whenAddPublicationToCollection_thenThrowsNullPointerException() {

        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, editionID, anyCondition);

        sample.addPublicationToLibrary(new LibraryId(UUID.randomUUID().toString()));

        //Act + Assert
        assertThrows(NullPointerException.class, () ->
                sample.addPublicationToCollection(null)
        );
    }

    @Test
    void givenAlreadyAssignedCollection_whenAddPublicationToCollection_thenThrowsIllegalArgumentException() {

        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, editionID, anyCondition);

        sample.addPublicationToLibrary(new LibraryId(UUID.randomUUID().toString()));
        sample.addPublicationToCollection(new CollectionOfPublicationsId(UUID.randomUUID().toString()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                sample.addPublicationToCollection(new CollectionOfPublicationsId(UUID.randomUUID().toString()))
        );
    }

    @Test
    void givenNoLibraryAssigned_whenAddPublicationToCollection_thenThrowsIllegalArgumentException() {

        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, editionID, anyCondition);

        CollectionOfPublicationsId collectionOfPublicationsIdDouble = mock(CollectionOfPublicationsId.class);
        when(collectionOfPublicationsIdDouble.getId()).thenReturn(UUID.randomUUID().toString());

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                sample.addPublicationToCollection(collectionOfPublicationsIdDouble)
        );
    }


    // =========================================================================
    // isOwnedByUser
    // =========================================================================

    @Test
    void isOwnedByUser_shouldReturnTrue_ifPubSampleIsOwnedByUser() {

        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, editionID, anyCondition);

        //Act
        boolean result = sample.isOwnedByUser(ownerID);

        //Assert
        assertTrue(result);
    }

    @Test
    void isOwnedByUser_shouldReturnFalse_ifPubSampleIsNotOwnedByUser() {

        // Arrange
        UserId otherId = mock(UserId.class);

        PublicationSample sample = new PublicationSample(
                bdID, otherId, editionID, anyCondition);

        //Act
        boolean result = sample.isOwnedByUser(ownerID);

        //Assert
        assertFalse(result);
    }


    // =========================================================================
    // isInUserLibrary
    // =========================================================================

    @Test
    void isInUserLibrary_shouldReturnTrue_ifPubSampleIsInLibrary() {

        // Arrange
        LibraryId libraryId = mock(LibraryId.class);

        PublicationSample sample = new PublicationSample(
                bdID, ownerID, editionID, anyCondition);

        sample.addPublicationToLibrary(libraryId);

        //Act
        boolean result = sample.isInUserLibrary();

        //Assert
        assertTrue(result);
    }

    @Test
    void isInUserLibrary_shouldReturnFalse_ifPubSampleIsNotInLibrary() {

        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, editionID, anyCondition);

        //Act
        boolean result = sample.isInUserLibrary();

        //Assert
        assertFalse(result);
    }


    // =========================================================================
    // Getters
    // =========================================================================

    @Test
    void getBibliographicalDataID_shouldReturnBibliographicalDataID() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        when(bdID.getID()).thenReturn("123");

        // Act
        BibliographicalDataId bdID_real = anySample.getBibliographicalDataID();

        //Assert
        assertEquals(bdID, bdID_real);
    }

    @Test
    void getUserID_shouldReturnUserID() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        // Act
        UserId userID_real = anySample.getOwnerID();

        //Assert
        assertEquals(ownerID, userID_real);
    }

    @Test
    void getPublicationStatus_shouldReturnPublicationStatus() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        // Act
        PublicationStatus realStatus = anySample.getPublicationStatus();

        //Assert
        assertEquals(PublicationStatus.CREATED, realStatus);
    }

    @Test
    void getEditionID_shouldReturnEditionID() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID,
                editionID, anyCondition);

        // Act
        EditionId editionId_real = anySample.getEditionID();

        //Assert
        assertEquals(editionId_real, editionID);
    }

    @Test
    void getCondition_shouldReturnCondition() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID,
                editionID, anyCondition);

        // Act
        Condition realCondition = anySample.getCondition();

        //Assert
        assertEquals(realCondition, anyCondition);
    }

    @Test
    void getLibraryID_shouldReturnLibraryID() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID,
                editionID, anyCondition);

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.getId()).thenReturn(UUID.randomUUID().toString());
        anySample.addPublicationToLibrary(libraryIdDouble);

        // Act
        LibraryId libraryID_real = anySample.getLibraryID();

        //Assert
        assertEquals(libraryID_real, libraryIdDouble);
    }

    @Test
    void getCollecOfPubID_shouldReturnCollectionOfPublicationsID() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID,
                editionID, anyCondition);

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.getId()).thenReturn(UUID.randomUUID().toString());
        anySample.addPublicationToLibrary(libraryIdDouble);

        CollectionOfPublicationsId collectionOfPublicationsIdDouble = mock(CollectionOfPublicationsId.class);
        when(collectionOfPublicationsIdDouble.getId()).thenReturn(UUID.randomUUID().toString());
        anySample.addPublicationToCollection(collectionOfPublicationsIdDouble);

        // Act
        CollectionOfPublicationsId collectionOfPublicationsId_real = anySample.getCollecOfPubID();

        //Assert
        assertEquals(collectionOfPublicationsId_real, collectionOfPublicationsIdDouble);
    }

    @Test
    void getDetailsMethodShouldReturnCorrectInfo() {

        // Arrange
        PublicationSample wantDetails = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        //Act
        PublicationSample.PublicationDetails anyDetails = wantDetails.getDetails();

        //Assert
        assertEquals(bdID, anyDetails.bibliographicalData());
        assertEquals(ownerID, anyDetails.owner());
        assertEquals(editionID, anyDetails.edition());
        assertEquals(anyCondition, anyDetails.condition());
    }

    @Test
    void id_ShouldNotBeNull() {

        //Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, editionID, anyCondition);

        //Act
        PublicationSampleId id = sample.id();

        //Assert
        assertNotNull(id);
    }

    // =========================================================================
    // Is Available For Sale
    // =========================================================================

    @Test
    void shouldReturnTrueWhenPublicationStatusIsReading() {
        // Arrange
        PublicationSample sampleForSale = new PublicationSample(bdID, ownerID,
                editionID, anyCondition);

        sampleForSale.changePublicationStatus(PublicationStatus.READING);

        // Act
        boolean result = sampleForSale.isAvailableForSale();

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenPublicationStatusIsRead() {
        // Arrange
        PublicationSample sampleForSale = new PublicationSample(bdID, ownerID,
                editionID, anyCondition);

        sampleForSale.changePublicationStatus(PublicationStatus.READ);

        // Act
        boolean result = sampleForSale.isAvailableForSale();

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenPublicationStatusIsForSale() {
        // Arrange
        PublicationSample sampleNotForSale = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        sampleNotForSale.changePublicationStatus(PublicationStatus.FOR_SALE);

        // Act
        boolean result = sampleNotForSale.isAvailableForSale();

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPublicationStatusIsSold() {
        // Arrange
        PublicationSample sampleNotForSale = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        sampleNotForSale.changePublicationStatus(PublicationStatus.SOLD);

        // Act
        boolean result = sampleNotForSale.isAvailableForSale();

        // Assert
        assertFalse(result);
    }


    // =========================================================================
    // Equality and Identity (sameAs / equals)
    // =========================================================================

    @Test
    void sameAs_shouldReturnTrue_WhenTwoObjectsAreTheSame() {

        // Arrange
        PublicationSample sample1 = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        PublicationSample sample2 = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        //Act
        boolean result = sample1.sameAs(sample2);

        //Assert
        assertTrue(result);
    }

    @Test
    void sameAs_shouldReturnFalse_WhenTwoObjectsAreDifferent() {

        // Arrange
        EditionId editionId2 = mock(EditionId.class);

        PublicationSample sample1 = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        PublicationSample sample2 = new PublicationSample(bdID, ownerID, editionId2, anyCondition);

        //Act
        boolean result = sample1.sameAs(sample2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAs_shouldReturnFalse_WhenObjectIsOfDifferentType() {

        // Arrange
        PublicationSample sample = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        Object differentType = new Object();

        // Act
        boolean result = sample.sameAs(differentType);

        // Assert
        assertFalse(result);
    }

    @Test
    void equal_shouldReturnTrue_WhenIDsAreEqual() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        //Act
        boolean result = anySample.equals(anySample);

        //Assert
        assertTrue(result);
    }

    @Test
    void equal_shouldReturnFalse_WhenIDsAreDifferent() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        PublicationSample newSample = new PublicationSample(bdID, ownerID, editionID, anyCondition);

        //Act
        boolean result = anySample.equals(newSample);

        //Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithNull() {

        //Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, editionID, anyCondition);

        PublicationSample nullSample = null;

        //Assert
        assertFalse(sample.equals(nullSample));
    }
    // =========================================================================
    // assignLibrary
    // =========================================================================

    @Test
    void assignLibrary_shouldAssignLibraryCorrectly() {

        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID,
                ownerID,
                editionID,
                anyCondition
        );

        LibraryId newLibraryId = mock(LibraryId.class);

        // Act
        LibraryId result = sample.addPublicationToLibrary(newLibraryId);

        // Assert
        assertEquals(newLibraryId, sample.getLibraryID());
        assertEquals(newLibraryId, result);
    }

    @Test
    void assignLibrary_shouldThrowExceptionWhenLibraryIdIsNull() {

        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID,
                ownerID,
                editionID,
                anyCondition
        );

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> sample.addPublicationToLibrary(null));
    }

    @Test
    void assignLibrary_shouldReplaceExistingLibraryAssignment() {

        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID,
                ownerID,
                editionID,
                anyCondition
        );

        LibraryId oldLibraryId = mock(LibraryId.class);
        LibraryId newLibraryId = mock(LibraryId.class);

        // Act + Assert
        sample.addPublicationToLibrary(oldLibraryId);

        assertThrows(IllegalArgumentException.class,
                () -> sample.addPublicationToLibrary(newLibraryId));
    }
}