package TOPSECRET.domain.PublicationSample;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublicationSampleFactory}.
 *
 * <p>This test class validates the behaviour of the factory responsible for
 * creating and reconstructing {@link PublicationSample} domain entities.</p>
 *
 * <p>The factory exposes two distinct creation paths:</p>
 * <ul>
 *   <li>
 *       <b>Creation of new domain objects</b> —
 *       verifies that the factory correctly creates a new
 *       {@link PublicationSample} using the constructor that internally
 *       generates a new {@link PublicationSampleId}.
 *   </li>
 *
 *   <li>
 *       <b>Reconstruction of persisted domain objects</b> —
 *       verifies that the factory correctly rebuilds an existing
 *       {@link PublicationSample} using a previously persisted identity and
 *       persistence-related associations such as {@link LibraryId} and
 *       {@link CollectionOfPublicationsId}.
 *   </li>
 * </ul>
 *
 * <p>{@link MockedConstruction} is used to intercept constructor invocations
 * of {@link PublicationSample}, allowing the tests to isolate the factory
 * behaviour from the internal implementation details of the domain entity.</p>
 *
 * <p>The tests confirm that:</p>
 * <ul>
 *   <li><b>New object</b> — {@link PublicationSampleFactory#create(BibliographicalDataId, UserId, EditionId, Condition)}:
 *       identity is generated internally by the domain object.</li>
 *   <li><b>Reconstruction</b> — {@link PublicationSampleFactory#create(PublicationSampleId, BibliographicalDataId, UserId, PublicationStatus, EditionId, Condition, LibraryId, CollectionOfPublicationsId)}:
 *       a pre-existing identity is supplied, restoring a previously persisted object.</li>
 * </ul>
 *
 * <p>{@link MockedConstruction} is used to intercept {@link PublicationSample} construction,
 * isolating the factory from the domain entity's internal logic and confirming
 * that exactly one instance is created per factory call.</p>
 */
class PublicationSampleFactoryTest {

    private PublicationSampleId pbSampleIdDouble;
    private BibliographicalDataId bdIdDouble;
    private UserId userIdDouble;
    private EditionId editionIdDouble;
    private PublicationStatus status;
    private Condition condition;
    private LibraryId libraryId;
    private CollectionOfPublicationsId collectionId;


    @BeforeEach
    void setup(){
        pbSampleIdDouble = mock(PublicationSampleId.class);
        bdIdDouble = mock(BibliographicalDataId.class);
        userIdDouble = mock(UserId.class);
        editionIdDouble = mock(EditionId.class);
        status = PublicationStatus.READ;
        condition = Condition.NEW;
        libraryId = mock(LibraryId.class);
        collectionId = mock(CollectionOfPublicationsId.class);

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
                    condition,
                    libraryId,
                    collectionId
            );

            // Assert
            assertNotNull(result);
            assertEquals(1, mocked.constructed().size());

            PublicationSample constructedPublicationSample = mocked.constructed().get(0);
            assertSame(result, constructedPublicationSample);
        }
    }
}