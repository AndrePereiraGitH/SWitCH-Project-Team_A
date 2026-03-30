//package TOPSECRET.domain.PublicationSample;
//
//import TOPSECRET.domain.BibliographicalData.BibliographicalData;
//import TOPSECRET.domain.Edition.Edition;
//import TOPSECRET.domain.Enum.Condition;
//import TOPSECRET.domain.Enum.PublicationStatus;
//import TOPSECRET.domain.User;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedConstruction;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class PublicationSampleFactoryTest {
//
//    @Test
//    void create_shouldReturnValidPublicationSample() {
//
//        // Arrange
//
//        BibliographicalData bdDouble = mock(BibliographicalData.class);
//        User userDouble = mock(User.class);
//        Edition editionDouble = mock(Edition.class);
//
//        PublicationStatus status = PublicationStatus.READ;
//        Condition condition = Condition.NEW;
//
//        try(MockedConstruction<PublicationSample> mocked =
//                mockConstruction(PublicationSample.class,
//                        (mock, context) -> {
//                })) {
//
//            PublicationSampleFactory factory = new PublicationSampleFactory();
//
//
//            // Act
//            PublicationSample result = factory.create(
//                    bdDouble,
//                    userDouble,
//                    status,
//                    editionDouble,
//                    condition
//            );
//
//            // Assert
//            assertNotNull(result);
//            assertEquals(1, mocked.constructed().size());
//
//            PublicationSample constructedPublicationSample = mocked.constructed().get(0);
//            assertSame(result, constructedPublicationSample);
//
//        }
//    }
//}