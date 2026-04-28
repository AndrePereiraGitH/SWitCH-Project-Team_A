package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublicationSampleDM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
class PublicationSampleAssemblerTest {

    private PublicationSampleId pubSampleIdDouble;
    private BibliographicalDataID bdIdDouble;
    private UserId userIdDouble;
    private EditionID editionIdDouble;
    private LibraryId libraryIdDouble;
    private CollectionOfPublicationsID collectionOfPublicationsIdDouble;
    private PublicationStatus status;
    private Condition condition;
    private PublicationSampleFactory factoryDouble;
    private PublicationSampleAssembler assembler;

    @BeforeEach
    void setup() {
        pubSampleIdDouble = mock(PublicationSampleId.class);
        bdIdDouble = mock(BibliographicalDataID.class);
        userIdDouble = mock(UserId.class);
        editionIdDouble = mock(EditionID.class);
        libraryIdDouble = mock(LibraryId.class);
        collectionOfPublicationsIdDouble = mock(CollectionOfPublicationsID.class);
        status = PublicationStatus.READ;
        condition = Condition.NEW;

        factoryDouble = mock(PublicationSampleFactory.class);
        assembler = new PublicationSampleAssembler(factoryDouble);
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
    void toDomain_shouldReturnAValidDomainObject() {
        // Arrange
        PublicationSampleDM dm = mock(PublicationSampleDM.class);
        when(dm.getPubSampleId()).thenReturn(pubSampleIdDouble);
        when(dm.getBdId()).thenReturn(bdIdDouble);
        when(dm.getOwnerId()).thenReturn(userIdDouble);
        when(dm.getEditionId()).thenReturn(editionIdDouble);
        when(dm.getLibraryId()).thenReturn(libraryIdDouble);
        when(dm.getCollectionOfPublicationsId()).thenReturn(collectionOfPublicationsIdDouble);
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
}