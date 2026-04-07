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
 * assignment logic, status transitions, and equality/identity behaviour of
 * {@code PublicationSample}.</p>
 *
 * <p>All collaborators ({@link BibliographicalDataID}, {@link UserId}, {@link EditionID},
 * {@link PublicationStatus}, {@link Condition}) are provided as Mockito test doubles,
 * keeping each test focused on the behaviour of {@code PublicationSample} in isolation.</p>
 *
 * <p>Test coverage is organised into the following sections:</p>
 * <ul>
 *   <li><b>Constructor</b> – valid creation and null-argument guards.</li>
 *   <li><b>Getters</b> – correct retrieval of all fields, including lazily assigned ones.</li>
 *   <li><b>Library Assignment</b> – assignment rules, null rejection, and double-assignment guard.</li>
 *   <li><b>Collection Assignment</b> – assignment rules, null rejection, double-assignment guard,
 *       and prerequisite library check.</li>
 *   <li><b>Status Change</b> – {@code FOR_SALE} transition and idempotency.</li>
 *   <li><b>Equality and Identity</b> – {@code sameAs} and {@code equals} contracts.</li>
 * </ul>
 */


class PublicationSampleTest {

    // =========================================================================
    // Test Doubles (Collaborators)
    // =========================================================================

    private final BibliographicalDataID bdID = mock(BibliographicalDataID.class);
    private final UserId ownerID = mock(UserId.class);
    private final EditionID editionID = mock(EditionID.class);
    private final PublicationStatus anyPublicationStatus = mock(PublicationStatus.class);
    private final Condition anyCondition = mock(Condition.class);


    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void shouldCreateValidPubSample() {

        //Act
        PublicationSample validSample = new PublicationSample(bdID, ownerID, anyPublicationStatus,
                editionID, anyCondition);

        assertNotNull(validSample);
    }

    @Test
    void shouldThrowExceptionWhen_BibliographicalDataID_isNull() {
        //Arrange

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSample(null, ownerID, anyPublicationStatus, editionID, anyCondition));
    }

    @Test
    void shouldThrowExceptionWhen_UserID_isNull() {
        //Arrange

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSample(bdID, null, anyPublicationStatus, editionID, anyCondition));
    }

    @Test
    void shouldThrowExceptionWhen_publicationStatus_isNull() {
        //Arrange

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSample(bdID, ownerID, null, editionID, anyCondition));
    }

    @Test
    void shouldThrowExceptionWhen_editionID_isNull() {
        //Arrange

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSample(bdID, ownerID, anyPublicationStatus, null, anyCondition));
    }

    @Test
    void shouldThrowExceptionWhen_condition_isNull() {
        //Arrange

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSample(bdID, ownerID, anyPublicationStatus, editionID, null));
    }


    // =========================================================================
    // Getters
    // =========================================================================

    @Test
    void getBibliographicalDataID_shouldReturnBibliographicalDataID() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID, anyPublicationStatus,
                editionID, anyCondition);

        when(bdID.getID()).thenReturn("123");

        // Act
        BibliographicalDataID bdID_real = anySample.getBibliographicalDataID();

        //Assert
        assertEquals(bdID,bdID_real);
    }

    @Test
    void getUserID_shouldReturnUserID() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID, anyPublicationStatus,
                editionID, anyCondition);


        // Act
        UserId userID_real = anySample.getOwnerID();

        //Assert
        assertEquals(ownerID,userID_real);
    }

    @Test
    void getPublicationStatus_shouldReturnPublicationStatus() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID, anyPublicationStatus,
                editionID, anyCondition);


        // Act
        PublicationStatus realStatus = anySample.getPublicationStatus();

        //Assert
        assertEquals(anyPublicationStatus,realStatus);
    }

    @Test
    void getEditionID_shouldReturnEditionID() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID, anyPublicationStatus,
                editionID, anyCondition);


        // Act
        EditionID editionID_real = anySample.getEditionID();

        //Assert
        assertEquals(editionID_real,editionID);
    }

    @Test
    void getCondition_shouldReturnCondition() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID, anyPublicationStatus,
                editionID, anyCondition);


        // Act
        Condition realCondition = anySample.getCondition();

        //Assert
        assertEquals(realCondition,anyCondition);
    }

    @Test
    void getLibraryID_shouldReturnLibraryID() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID, anyPublicationStatus,
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
        PublicationSample anySample = new PublicationSample(bdID, ownerID, anyPublicationStatus,
                editionID, anyCondition);

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.getId()).thenReturn(UUID.randomUUID().toString());
        anySample.addPublicationToLibrary(libraryIdDouble);

        CollectionOfPublicationsID collectionOfPublicationsIdDouble = mock(CollectionOfPublicationsID.class);
        when(collectionOfPublicationsIdDouble.getId()).thenReturn(UUID.randomUUID().toString());
        anySample.addPublicationToCollection(collectionOfPublicationsIdDouble);


        // Act
        CollectionOfPublicationsID collectionOfPublicationsID_real = anySample.getCollecOfPubID();

        //Assert
        assertEquals(collectionOfPublicationsID_real, collectionOfPublicationsIdDouble);
    }

    @Test
    void getDetailsMethodShouldReturnCorrectInfo() {

        // Arrange, SUT here would be the PublicationSample and the getDetails method
        PublicationSample wantDetails = new PublicationSample(bdID, ownerID,
                anyPublicationStatus, editionID, anyCondition);

        //Act
        PublicationSample.PublicationDetails anyDetails = wantDetails.getDetails();

        //Assert
        assertEquals(bdID, anyDetails.bibliographicalData());
        assertEquals(ownerID, anyDetails.owner());
        assertEquals(anyPublicationStatus, anyDetails.publicationStatus());
        assertEquals(editionID, anyDetails.edition());
        assertEquals(anyCondition, anyDetails.condition());
    }

    @Test
    void id_ShouldNotBeNull(){
        //Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, anyPublicationStatus, editionID, anyCondition);

        //Act
        PublicationSampleId id = sample.id();

        //Assert
        assertNotNull(id);
    }


    // =========================================================================
    // Library Assignment
    // =========================================================================

    @Test
    void givenValidLibraryId_whenAddPublicationToLibrary_thenLibraryIsAssigned() {
        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, anyPublicationStatus, editionID, anyCondition);

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.getId()).thenReturn(UUID.randomUUID().toString());

        //Act
        LibraryId result = sample.addPublicationToLibrary(libraryIdDouble);

        // Assert
        assertEquals(libraryIdDouble, result);
    }

    @Test
    void givenNullLibraryId_whenAddPublicationToLibrary_thenThrowsNullPointerException() {
        // Act
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, anyPublicationStatus, editionID, anyCondition);

        LibraryId libraryIdDouble = mock(LibraryId.class);

        //Act + Assert
        assertThrows(NullPointerException.class, () ->
                sample.addPublicationToLibrary(null));
    }

    @Test
    void givenAlreadyAssignedLibrary_whenAddPublicationToLibrary_thenThrowsIllegalArgumentException() {
        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, anyPublicationStatus, editionID, anyCondition);

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.getId()).thenReturn(UUID.randomUUID().toString());
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
                bdID, ownerID, anyPublicationStatus, editionID, anyCondition);

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.getId()).thenReturn(UUID.randomUUID().toString());
        sample.addPublicationToLibrary(new LibraryId(UUID.randomUUID().toString()));

        CollectionOfPublicationsID collectionOfPublicationsIdDouble = mock(CollectionOfPublicationsID.class);
        when(collectionOfPublicationsIdDouble.getId()).thenReturn(UUID.randomUUID().toString());

        // Act
        CollectionOfPublicationsID result = sample.addPublicationToCollection(collectionOfPublicationsIdDouble);

        // Assert
        assertEquals(collectionOfPublicationsIdDouble, result);
    }

    @Test
    void givenNullCollection_whenAddPublicationToCollection_thenThrowsNullPointerException() {
        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, anyPublicationStatus, editionID, anyCondition);

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.getId()).thenReturn(UUID.randomUUID().toString());
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
                bdID, ownerID, anyPublicationStatus, editionID, anyCondition);

        LibraryId libraryIdDouble = mock(LibraryId.class);
        when(libraryIdDouble.getId()).thenReturn(UUID.randomUUID().toString());
        sample.addPublicationToLibrary(new LibraryId(UUID.randomUUID().toString()));

        CollectionOfPublicationsID collectionOfPublicationsIdDouble = mock(CollectionOfPublicationsID.class);
        when(collectionOfPublicationsIdDouble.getId()).thenReturn(UUID.randomUUID().toString());

        sample.addPublicationToCollection(new CollectionOfPublicationsID(UUID.randomUUID().toString()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                sample.addPublicationToCollection(new CollectionOfPublicationsID(UUID.randomUUID().toString()))
        );
    }

    @Test
    void givenNoLibraryAssigned_whenAddPublicationToCollection_thenThrowsIllegalArgumentException() {
        // Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, anyPublicationStatus, editionID, anyCondition);

        CollectionOfPublicationsID collectionOfPublicationsIdDouble = mock(CollectionOfPublicationsID.class);
        when(collectionOfPublicationsIdDouble.getId()).thenReturn(UUID.randomUUID().toString());

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                sample.addPublicationToCollection(collectionOfPublicationsIdDouble)
        );
    }


    // =========================================================================
    // Status Change
    // =========================================================================

    @Test
    void shouldChangeStatusToForSaleCorrectly() {

        //Arrange
        PublicationSample sampleRead = new PublicationSample(bdID, ownerID, PublicationStatus.READ,
                editionID, anyCondition);

        //Act
        boolean forSale = sampleRead.changeStatusToForSale();

        //Assert
        assertTrue(forSale);
    }

    @Test
    void shouldReturnFalseIfSampleAlreadyForSale() {


        PublicationSample sampleForSale = new PublicationSample(bdID, ownerID, PublicationStatus.FOR_SALE,
                editionID, anyCondition);

        //Act
        boolean alreadyForSale = sampleForSale.changeStatusToForSale();

        //Assert
        assertFalse(alreadyForSale);
    }


    // =========================================================================
    // Equality and Identity (sameAs / equals)
    // =========================================================================

    @Test
    void sameAs_shouldReturnTrue_WhenTwoObjectsAreTheSame() {

        // Arrange

        PublicationSample sample1 = new PublicationSample(bdID, ownerID,
                anyPublicationStatus, editionID, anyCondition);

        PublicationSample sample2 = new PublicationSample(bdID, ownerID,
                anyPublicationStatus, editionID, anyCondition);


        //Act
        boolean result = sample1.sameAs(sample2);

        //Assert
        assertTrue(result);
    }

    @Test
    void sameAs_shouldReturnFalse_WhenTwoObjectsAreDifferent() {

        // Arrange
        EditionID editionID2 = mock(EditionID.class);

        PublicationSample sample1 = new PublicationSample(bdID, ownerID,
                anyPublicationStatus, editionID, anyCondition);

        PublicationSample sample2 = new PublicationSample(bdID, ownerID,
                anyPublicationStatus, editionID2, anyCondition);


        //Act
        boolean result = sample1.sameAs(sample2);

        //Assert
        assertFalse(result);
    }

    @Test
    void sameAs_shouldReturnFalse_WhenObjectIsOfDifferentType() {

        // Arrange
        PublicationSample sample = new PublicationSample(bdID, ownerID,
                anyPublicationStatus, editionID, anyCondition);

        Object differentType = new Object();

        // Act
        boolean result = sample.sameAs(differentType);

        // Assert
        assertFalse(result);

    }

    @Test
    void equal_shouldReturnTrue_WhenIDsAreEqual() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID, anyPublicationStatus,
                editionID, anyCondition);

        //Act
        boolean result = anySample.equals(anySample);

        //Assert
        assertTrue(result);
    }

    @Test
    void equal_shouldReturnFalse_WhenIDsAreDifferent() {

        //Arrange
        PublicationSample anySample = new PublicationSample(bdID, ownerID, anyPublicationStatus,
                editionID, anyCondition);

        PublicationSample newSample = new PublicationSample(bdID, ownerID, anyPublicationStatus,
                editionID, anyCondition);

        //Act
        boolean result = anySample.equals(newSample);

        //Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithNull() {

        //Arrange
        PublicationSample sample = new PublicationSample(
                bdID, ownerID, anyPublicationStatus, editionID, anyCondition);

        PublicationSample nullSample = null;

        //Assert
        assertFalse(sample.equals(nullSample));
    }


//
//    @Test
//    void shouldReturnTrueWhenBibliographicalDataHasGenre() {
//
//        //Arrange + act
//        Genre genre = mock(Genre.class);
//        when(anyBiblioData.hasGenre(genre)).thenReturn(true);
//
//        PublicationSample sample = new PublicationSample(
//                anyBiblioData, anyOwner, anyPublicationStatus, anyEdition, anyCondition);
//
//        //Assert
//        assertTrue(sample.hasGenre(genre));
//    }
//
//    @Test
//    void shouldReturnFalseWhenBibliographicalDataDoesNotHaveGenre() {
//
//        //Arrange + act
//        Genre genre = mock(Genre.class);
//        when(anyBiblioData.hasGenre(genre)).thenReturn(false);
//
//        PublicationSample sample = new PublicationSample(
//                anyBiblioData, anyOwner, anyPublicationStatus, anyEdition, anyCondition);
//
//        //Assert
//        assertFalse(sample.hasGenre(genre));
//    }
}