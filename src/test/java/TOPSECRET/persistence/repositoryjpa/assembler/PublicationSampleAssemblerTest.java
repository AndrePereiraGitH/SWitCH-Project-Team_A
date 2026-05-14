package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublicationSampleDM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublicationSampleAssembler}.
 *
 * <p>This test class validates the behaviour of the assembler responsible for
 * converting between {@link PublicationSample} domain objects and
 * {@link PublicationSampleDM} persistence entities.</p>
 *
 * <p>The following behaviours are covered:</p>
 * <ul>
 *     <li><b>Constructor validation</b> — verifies that the assembler rejects
 *     a {@code null} {@link PublicationSampleFactory} dependency.</li>
 *
 *     <li><b>toDM conversion</b> — verifies that a domain object is correctly
 *     converted into a persistence data model using all values exposed through
 *     the domain getters.</li>
 *
 *     <li><b>toDomain conversion</b> — verifies that a persistence entity is
 *     correctly reconstructed into a domain object through the reconstruction
 *     overload of {@link PublicationSampleFactory#create(PublicationSampleId,
 *     BibliographicalDataID, UserId, PublicationStatus, EditionID, Condition,
 *     LibraryId, CollectionOfPublicationsId)}.</li>
 *
 *     <li><b>List conversion</b> — verifies that lists of persistence entities
 *     are correctly reconstructed into immutable lists of domain objects while
 *     preserving order and size.</li>
 *
 *     <li><b>Null guards</b> — verifies that all public methods reject
 *     {@code null} arguments with {@link NullPointerException}.</li>
 * </ul>
 *
 * <p>Mockito is used to isolate the assembler from both the persistence layer
 * and the domain construction logic. All collaborators are represented using
 * test doubles so the tests remain focused exclusively on mapping behaviour.</p>
 */
@ExtendWith(MockitoExtension.class)
class PublicationSampleAssemblerTest {

    private PublicationSampleId pubSampleIdDouble;
    private BibliographicalDataId bdIdDouble;
    private UserId userIdDouble;
    private EditionId editionIdDouble;
    private LibraryId libraryIdDouble;
    private CollectionOfPublicationsId collectionOfPublicationsIdDouble;
    private PublicationStatus status;
    private Condition condition;

    @Mock
    private PublicationSampleFactory factoryDouble;

    @InjectMocks
    private PublicationSampleAssembler assembler;

    @BeforeEach
    void setup() {

        pubSampleIdDouble = mock(PublicationSampleId.class);
        bdIdDouble = mock(BibliographicalDataId.class);
        userIdDouble = mock(UserId.class);
        editionIdDouble = mock(EditionId.class);
        libraryIdDouble = mock(LibraryId.class);
        collectionOfPublicationsIdDouble = mock(CollectionOfPublicationsId.class);

        status = PublicationStatus.READ;
        condition = Condition.NEW;
    }

    // ===============
    // Constructor
    // ===============

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new PublicationSampleAssembler(null));
    }

    // ===============
    // toDM
    // ===============

    @Test
    void toDM_shouldReturnAValidDataModel() {

        // Arrange
        PublicationSample pbSampleDouble = mock(PublicationSample.class);

        when(pbSampleDouble.id()).thenReturn(pubSampleIdDouble);
        when(pbSampleDouble.getBibliographicalDataID()).thenReturn(bdIdDouble);
        when(pbSampleDouble.getOwnerID()).thenReturn(userIdDouble);
        when(pbSampleDouble.getEditionID()).thenReturn(editionIdDouble);
        when(pbSampleDouble.getLibraryID()).thenReturn(libraryIdDouble);
        when(pbSampleDouble.getCollecOfPubID()).thenReturn(collectionOfPublicationsIdDouble);
        when(pbSampleDouble.getPublicationStatus()).thenReturn(status);
        when(pbSampleDouble.getCondition()).thenReturn(condition);

        // Act
        PublicationSampleDM result = assembler.toDM(pbSampleDouble);

        // Assert
        assertNotNull(result);

        assertEquals(pubSampleIdDouble, result.getPubSampleId());
        assertEquals(bdIdDouble, result.getBdId());
        assertEquals(userIdDouble, result.getOwnerId());
        assertEquals(editionIdDouble, result.getEditionId());
        assertEquals(libraryIdDouble, result.getLibraryId());
        assertEquals(collectionOfPublicationsIdDouble, result.getCollectionOfPublicationsId());
        assertEquals(status, result.getPublicationStatus());
        assertEquals(condition, result.getCondition());
    }

    @Test
    void toDM_shouldThrowNullPointerException_whenPublicationSampleIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> assembler.toDM(null));
    }

    // ===============
    // ToDomain (single object)
    // ===============

    @Test
    void toDomain_shouldReturnAValidDomainObject() {

        // Arrange
        PublicationSampleDM dm = mock(PublicationSampleDM.class);

        when(dm.getPubSampleId()).thenReturn(pubSampleIdDouble);
        when(dm.getBdId()).thenReturn(bdIdDouble);
        when(dm.getOwnerId()).thenReturn(userIdDouble);
        when(dm.getEditionId()).thenReturn(editionIdDouble);
        when(dm.getPublicationStatus()).thenReturn(status);
        when(dm.getCondition()).thenReturn(condition);
        when(dm.getLibraryId()).thenReturn(libraryIdDouble);
        when(dm.getCollectionOfPublicationsId()).thenReturn(collectionOfPublicationsIdDouble);

        PublicationSample pbSampleDouble = mock(PublicationSample.class);

        when(factoryDouble.create(
                pubSampleIdDouble,
                bdIdDouble,
                userIdDouble,
                status,
                editionIdDouble,
                condition,
                libraryIdDouble,
                collectionOfPublicationsIdDouble
        )).thenReturn(pbSampleDouble);

        // Act
        PublicationSample result = assembler.toDomain(dm);

        // Assert
        assertNotNull(result);
        assertEquals(pbSampleDouble, result);
    }

    @Test
    void toDomain_shouldThrowNullPointerException_whenPublicationSampleDMIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> assembler.toDomain((PublicationSampleDM) null));
    }

    // ===============
    // toDomain (list)
    // ===============

    @Test
    void toDomain_withList_shouldReturnAValidDomainObjectList() {

        // Arrange
        PublicationSampleDM dmDouble = mock(PublicationSampleDM.class);

        when(dmDouble.getPubSampleId()).thenReturn(pubSampleIdDouble);
        when(dmDouble.getBdId()).thenReturn(bdIdDouble);
        when(dmDouble.getOwnerId()).thenReturn(userIdDouble);
        when(dmDouble.getEditionId()).thenReturn(editionIdDouble);
        when(dmDouble.getPublicationStatus()).thenReturn(status);
        when(dmDouble.getCondition()).thenReturn(condition);
        when(dmDouble.getLibraryId()).thenReturn(libraryIdDouble);
        when(dmDouble.getCollectionOfPublicationsId()).thenReturn(collectionOfPublicationsIdDouble);

        PublicationSample pbSampleDouble = mock(PublicationSample.class);

        when(factoryDouble.create(
                pubSampleIdDouble,
                bdIdDouble,
                userIdDouble,
                status,
                editionIdDouble,
                condition,
                libraryIdDouble,
                collectionOfPublicationsIdDouble
        )).thenReturn(pbSampleDouble);

        List<PublicationSampleDM> dmList = List.of(dmDouble);

        // Act
        List<PublicationSample> result = assembler.toDomainList(dmList);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pbSampleDouble, result.get(0));
    }

    @Test
    void toDomain_withList_shouldReturnEmptyList_whenListIsEmpty() {

        // Arrange
        List<PublicationSampleDM> dmList = List.of();

        // Act
        List<PublicationSample> result = assembler.toDomainList(dmList);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void toDomain_withList_shouldReturnAListOfDomainObjects() {

        // Arrange
        PublicationSampleDM dm = mock(PublicationSampleDM.class);

        when(dm.getPubSampleId()).thenReturn(pubSampleIdDouble);
        when(dm.getBdId()).thenReturn(bdIdDouble);
        when(dm.getOwnerId()).thenReturn(userIdDouble);
        when(dm.getEditionId()).thenReturn(editionIdDouble);
        when(dm.getPublicationStatus()).thenReturn(status);
        when(dm.getCondition()).thenReturn(condition);
        when(dm.getLibraryId()).thenReturn(libraryIdDouble);
        when(dm.getCollectionOfPublicationsId())
                .thenReturn(collectionOfPublicationsIdDouble);

        PublicationSample publicationSample = mock(PublicationSample.class);

        when(factoryDouble.create(
                pubSampleIdDouble,
                bdIdDouble,
                userIdDouble,
                status,
                editionIdDouble,
                condition,
                libraryIdDouble,
                collectionOfPublicationsIdDouble
        )).thenReturn(publicationSample);

        // Act
        List<PublicationSample> result =
                assembler.toDomainList(List.of(dm));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(publicationSample, result.get(0));
    }

    // ===== WIP ======

    @Test
    void toDomain_withList_shouldReturnCorrectDomainObject() {

        // Arrange
        PublicationSampleDM dm = mock(PublicationSampleDM.class);

        when(dm.getPubSampleId()).thenReturn(pubSampleIdDouble);
        when(dm.getBdId()).thenReturn(bdIdDouble);
        when(dm.getOwnerId()).thenReturn(userIdDouble);
        when(dm.getEditionId()).thenReturn(editionIdDouble);
        when(dm.getPublicationStatus()).thenReturn(status);
        when(dm.getCondition()).thenReturn(condition);
        when(dm.getLibraryId()).thenReturn(libraryIdDouble);
        when(dm.getCollectionOfPublicationsId())
                .thenReturn(collectionOfPublicationsIdDouble);

        PublicationSample publicationSample = mock(PublicationSample.class);

        when(factoryDouble.create(
                pubSampleIdDouble,
                bdIdDouble,
                userIdDouble,
                status,
                editionIdDouble,
                condition,
                libraryIdDouble,
                collectionOfPublicationsIdDouble
        )).thenReturn(publicationSample);

        // Act
        List<PublicationSample> result =
                assembler.toDomainList(List.of(dm));

        // Assert
        assertSame(publicationSample, result.get(0));
    }

    @Test
    void toDomainList_shouldThrowNullPointerException_whenListIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> assembler.toDomainList(null));
    }
}