package TOPSECRET.domain.PublicationSample;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationSampleFactoryTest {

    @Test
    void create_shouldReturnValidPublicationSample() {

        // Arrange

        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);
        UserId userIdDouble = mock(UserId.class);
        EditionID editionIdDouble = mock(EditionID.class);

        PublicationStatus status = PublicationStatus.READ;
        Condition condition = Condition.NEW;

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
}