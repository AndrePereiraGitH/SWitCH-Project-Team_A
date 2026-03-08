package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link PublicationSample} using mocked dependencies.
 *
 * <p>This test suite verifies the behavior of {@link PublicationSample}
 * while isolating it from the internal logic of its dependencies
 * ({@link BibliographicalData}, {@link User}, {@link Edition}, etc.).</p>
 *
 * <p>Mockito is used to create mock objects so that only the logic
 * inside {@link PublicationSample} is tested.</p>
 */

class PublicationSampleTest {

    //Mocks
    private final BibliographicalData anyBiblioData = mock(BibliographicalData.class);
    private final User anyOwner = mock(User.class);
    private final Edition anyEdition = mock(Edition.class);
    private final PublicationStatus anyPublicationStatus = PublicationStatus.READING;
    private final Condition anyCondition = Condition.GOOD;

    //Class/object under test
    private final PublicationSample anySample =
            new PublicationSample(anyBiblioData, anyOwner, anyPublicationStatus, anyEdition, anyCondition);


    @Test
    void shouldCreateValidPubSampleWithMocks() {

        //Act
        PublicationSample validSample = new PublicationSample(anyBiblioData, anyOwner, anyPublicationStatus,
                anyEdition, anyCondition);
    }

    @Test
    void shouldFetchDependenciesCorrectly() {

        //Assert
        assertEquals(anyBiblioData, anySample.getBibliographicalData());
        assertEquals(anyOwner, anySample.getOwner());
        assertEquals(anyEdition, anySample.getEdition());
        assertEquals(anyPublicationStatus, anySample.getPublicationStatus());
        assertEquals(anyCondition, anySample.getCondition());
    }

    @Test
    void shouldChangeStatusToForSaleCorrectly() {

        //Act
        anySample.changeStatusToForSale();

        //Assert
        assertEquals(PublicationStatus.FOR_SALE, anySample.getPublicationStatus());
    }

    @Test
    void getDetailsMethodShouldReturnCorrectInfo() {

        //Act
        PublicationSample.PublicationDetails anyDetails = anySample.getDetails();

        //Assert
        assertEquals(anyBiblioData, anyDetails.bibliographicalData());
        assertEquals(anyOwner, anyDetails.owner());
        assertEquals(anyPublicationStatus, anyDetails.publicationStatus());
        assertEquals(anyEdition, anyDetails.edition());
        assertEquals(anyCondition, anyDetails.condition());
    }

    @Test
    void shouldReturnTrueWhenSameObject() {

        //Assert
        assertEquals(anySample, anySample);
    }

    @Test
    void shouldReturnFalseWhenDifferentObject() {

        //Arrange
        PublicationSample otherSample = new PublicationSample(anyBiblioData, anyOwner, anyPublicationStatus,
                anyEdition, Condition.NEW);

        //Assert
        assertNotEquals(anySample, otherSample);
    }

    @Test
    void hashCodeShouldBeConsistent() {

        int hashA = anySample.hashCode();
        int hashB = anySample.hashCode();

        //Assert
        assertNotEquals(anyBiblioData.hashCode(), anySample.hashCode());
        assertEquals(hashA, hashB);
    }
}