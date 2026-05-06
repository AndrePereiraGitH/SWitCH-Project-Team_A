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

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {
        // Act + Assert
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
        // Act + Assert
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
        // Act + Assert
        assertThrows(NullPointerException.class, () -> assembler.toDomain((PublicationSampleDM) null));
    }

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

        PublicationSample pbSampleDouble = mock(PublicationSample.class);
        when(factoryDouble.create(
                pubSampleIdDouble, bdIdDouble, userIdDouble,
                status, editionIdDouble, condition)
        ).thenReturn(pbSampleDouble);

        List<PublicationSampleDM> dmList = List.of(dmDouble);

        // Act
        List<PublicationSample> result = assembler.toDomain(dmList);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void toDomain_withList_shouldThrowNullPointerException_whenListIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> assembler.toDomain((List<PublicationSampleDM>) null));
    }

    @Test
    void toDomain_withList_shouldReturnEmptyList_whenListIsEmpty() {
        // Arrange
        List<PublicationSampleDM> dmList = List.of();

        // Act
        List<PublicationSample> result = assembler.toDomain(dmList);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}