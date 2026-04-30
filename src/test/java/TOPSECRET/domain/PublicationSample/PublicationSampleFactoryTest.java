package TOPSECRET.domain.PublicationSample;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublicationSampleFactory}.
 *
 * <p>Verifies both creation paths exposed by the factory:</p>
 * <ul>
 *   <li><b>New object</b> — {@link PublicationSampleFactory#create(BibliographicalDataID, UserId, PublicationStatus, EditionID, Condition)}:
 *       identity is generated internally by the domain object.</li>
 *   <li><b>Reconstruction</b> — {@link PublicationSampleFactory#create(PublicationSampleId, BibliographicalDataID, UserId, PublicationStatus, EditionID, Condition)}:
 *       a pre-existing identity is supplied, restoring a previously persisted object.</li>
 * </ul>
 *
 * <p>{@link MockedConstruction} is used to intercept {@link PublicationSample} construction,
 * isolating the factory from the domain entity's internal logic and confirming
 * that exactly one instance is created per factory call.</p>
 */
class PublicationSampleFactoryTest {

    private PublicationSampleId pbSampleIdDouble;
    private BibliographicalDataID bdIdDouble;
    private UserId userIdDouble;
    private EditionID editionIdDouble;
    private PublicationStatus status;
    private Condition condition;


    @BeforeEach
    void setup(){
        pbSampleIdDouble = mock(PublicationSampleId.class);
        bdIdDouble = mock(BibliographicalDataID.class);
        userIdDouble = mock(UserId.class);
        editionIdDouble = mock(EditionID.class);
        status = PublicationStatus.READ;
        condition = Condition.NEW;
    }

    @Test
    void create_shouldReturnValidPublicationSample() {

        // Arrange
        try(MockedConstruction<PublicationSample> mocked =
                mockConstruction(PublicationSample.class,
                        (mock, context) -> {
                })) {

            PublicationSampleFactory factory = new PublicationSampleFactory();


            // Act
            PublicationSample result = factory.create(
                    bdIdDouble,
                    userIdDouble,
                    status,
                    editionIdDouble,
                    condition
            );

            // Assert
            assertNotNull(result);
            assertEquals(1, mocked.constructed().size());

            PublicationSample constructedPublicationSample = mocked.constructed().get(0);
            assertSame(result, constructedPublicationSample);

        }
    }

    @Test
    void createWithPubSampleId_shouldReturnValidPublicationSample() {

        //Arrange
        try(MockedConstruction<PublicationSample> mocked =
                    mockConstruction(PublicationSample.class,
                            (mock, context) -> {
                            })) {

            PublicationSampleFactory factory = new PublicationSampleFactory();


            // Act
            PublicationSample result = factory.create(
                    pbSampleIdDouble,
                    bdIdDouble,
                    userIdDouble,
                    status,
                    editionIdDouble,
                    condition
            );

            // Assert
            assertNotNull(result);
            assertEquals(1, mocked.constructed().size());

            PublicationSample constructedPublicationSample = mocked.constructed().get(0);
            assertSame(result, constructedPublicationSample);
        }
    }
}