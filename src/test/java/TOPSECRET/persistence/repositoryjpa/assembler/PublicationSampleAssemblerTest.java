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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublicationSampleAssembler}.
 *
 * <p>Covers both conversion directions:</p>
 * <ul>
 *   <li><b>toDM</b> — verifies that a {@link PublicationSample} domain object is correctly
 *       mapped to a {@link PublicationSampleDM} persistence entity.</li>
 *   <li><b>toDomain</b> — verifies that a {@link PublicationSampleDM} persistence entity is
 *       correctly reconstructed into a {@link PublicationSample} domain object.</li>
 * </ul>
 *
 * <p>Dependencies are isolated using Mockito. {@link PublicationSample} is mocked
 * with stubbed getters to avoid coupling the assembler tests to domain construction logic.</p>
 */
@ExtendWith(MockitoExtension.class)
class PublicationSampleAssemblerTest {

    private PublicationSampleId pubSampleIdDouble;
    private BibliographicalDataID bdIdDouble;
    private UserId userIdDouble;
    private EditionID editionIdDouble;
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
        bdIdDouble = mock(BibliographicalDataID.class);
        userIdDouble = mock(UserId.class);
        editionIdDouble = mock(EditionID.class);
        libraryIdDouble = mock(LibraryId.class);
        collectionOfPublicationsIdDouble = mock(CollectionOfPublicationsId.class);
        status = PublicationStatus.READ;
        condition = Condition.NEW;
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {
        assertThrows(NullPointerException.class, () -> new PublicationSampleAssembler(null));
    }

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
    }

    @Test
    void toDM_shouldThrowNullPointerException_whenPublicationSampleIsNull() {
        assertThrows(NullPointerException.class, () -> assembler.toDM(null));
    }

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

        PublicationSample pbSampleDouble = mock(PublicationSample.class);
        when(factoryDouble.create(
                pubSampleIdDouble, bdIdDouble, userIdDouble,
                status, editionIdDouble, condition)
        ).thenReturn(pbSampleDouble);

        // Act
        PublicationSample result = assembler.toDomain(dm);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDomain_shouldThrowNullPointerException_whenPublicationSampleDMIsNull() {
        assertThrows(NullPointerException.class, () -> assembler.toDomain(null));
    }

    @Test
    void toDomainList_shouldReturnAListOfDomainObjects() {
        // ARRANGE
        PublicationSampleDM dm = mock(PublicationSampleDM.class);

        when(dm.getPubSampleId()).thenReturn(pubSampleIdDouble);
        when(dm.getBdId()).thenReturn(bdIdDouble);
        when(dm.getOwnerId()).thenReturn(userIdDouble);
        when(dm.getEditionId()).thenReturn(editionIdDouble);
        when(dm.getPublicationStatus()).thenReturn(status);
        when(dm.getCondition()).thenReturn(condition);

        PublicationSample publicationSample = mock(PublicationSample.class);

        when(factoryDouble.create(
                pubSampleIdDouble,
                bdIdDouble,
                userIdDouble,
                status,
                editionIdDouble,
                condition)
        ).thenReturn(publicationSample);

        // ACT
        var result = assembler.toDomainList(java.util.List.of(dm));

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void toDomainList_shouldReturnCorrectDomainObject() {
        // ARRANGE
        PublicationSampleDM dm = mock(PublicationSampleDM.class);

        when(dm.getPubSampleId()).thenReturn(pubSampleIdDouble);
        when(dm.getBdId()).thenReturn(bdIdDouble);
        when(dm.getOwnerId()).thenReturn(userIdDouble);
        when(dm.getEditionId()).thenReturn(editionIdDouble);
        when(dm.getPublicationStatus()).thenReturn(status);
        when(dm.getCondition()).thenReturn(condition);

        PublicationSample publicationSample = mock(PublicationSample.class);

        when(factoryDouble.create(
                pubSampleIdDouble,
                bdIdDouble,
                userIdDouble,
                status,
                editionIdDouble,
                condition)
        ).thenReturn(publicationSample);

        // ACT
        var result = assembler.toDomainList(java.util.List.of(dm));

        // ASSERT
        assertSame(publicationSample, result.get(0));
    }

    @Test
    void toDomainList_shouldReturnEmptyListWhenInputIsEmpty() {
        // ACT
        var result = assembler.toDomainList(java.util.List.of());

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void toDomainList_shouldThrowNullPointerExceptionWhenInputIsNull() {
        // ACT & ASSERT
        assertThrows(NullPointerException.class,
                () -> assembler.toDomainList(null));
    }
}