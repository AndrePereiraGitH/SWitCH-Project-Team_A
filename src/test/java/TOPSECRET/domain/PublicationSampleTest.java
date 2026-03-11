package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    //Mocks for collaborators
    private final BibliographicalData anyBiblioData = mock(BibliographicalData.class);
    private final User anyOwner = mock(User.class);
    private final Edition anyEdition = mock(Edition.class);
    private final PublicationStatus anyPublicationStatus = mock(PublicationStatus.class);
    private final Condition anyCondition = mock(Condition.class);


    @Test
    void shouldCreateValidPubSampleWithMocks() {

        //Act
        PublicationSample validSample = new PublicationSample(anyBiblioData, anyOwner, anyPublicationStatus,
                anyEdition, anyCondition);
    }

    @Test
    void shouldThrowExceptionWhenAFieldIsNull() {
        //Arrange
        BibliographicalData biblioDataNull = null;

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                new PublicationSample(biblioDataNull, anyOwner, anyPublicationStatus, anyEdition, anyCondition));
    }

    @Test
    void shouldFetchDependenciesCorrectly() {

        //Arrange
        PublicationSample anySample = new PublicationSample(anyBiblioData, anyOwner, anyPublicationStatus,
                anyEdition, anyCondition);

        //Assert
        assertEquals(anyBiblioData, anySample.getBibliographicalData());
        assertEquals(anyOwner, anySample.getOwner());
        assertEquals(anyEdition, anySample.getEdition());
        assertEquals(anyPublicationStatus, anySample.getPublicationStatus());
        assertEquals(anyCondition, anySample.getCondition());
    }

    @Test
    void shouldChangeStatusToForSaleCorrectly() {

        //Arrange, SUT here is the method being tested, PubSample and PublicationStatus
        PublicationSample sampleRead = new PublicationSample(anyBiblioData, anyOwner, PublicationStatus.READ,
                anyEdition, anyCondition);

        //Act
        boolean forSale = sampleRead.changeStatusToForSale();

        //Assert
        assertTrue(forSale);
    }

    @Test
    void shouldReturnFalseIfSampleAlreadyForSale() {

        //Arrange, SUT same as before but different outcome
        PublicationSample sampleForSale = new PublicationSample(anyBiblioData, anyOwner, PublicationStatus.FOR_SALE,
                anyEdition, anyCondition);

        //Act
        boolean alreadyForSale = sampleForSale.changeStatusToForSale();

        //Assert
        assertFalse(alreadyForSale);
    }

    @Test
    void getDetailsMethodShouldReturnCorrectInfo() {

        // Arrange, SUT here would be the PublicationSample and the getDetails method
        PublicationSample wantDetails = new PublicationSample(anyBiblioData, anyOwner,
                anyPublicationStatus, anyEdition, anyCondition);

        //Act
        PublicationSample.PublicationDetails anyDetails = wantDetails.getDetails();

        //Assert
        assertEquals(anyBiblioData, anyDetails.bibliographicalData());
        assertEquals(anyOwner, anyDetails.owner());
        assertEquals(anyPublicationStatus, anyDetails.publicationStatus());
        assertEquals(anyEdition, anyDetails.edition());
        assertEquals(anyCondition, anyDetails.condition());
    }

    @Test
    void shouldReturnTrueWhenSameObject() {

        // Arrange
        PublicationSample sameSample = new PublicationSample(anyBiblioData, anyOwner,
                anyPublicationStatus, anyEdition, anyCondition);

        //Assert
        assertEquals(sameSample, sameSample);
    }

    @Test
    void shouldReturnFalseWhenDifferentObject() {

        //Is it valid to use an actual attribute as the differing field here? In this case Condition
        //Because the SUT now could be considered PublicationSample and Condition instead of just Condition...

        //Arrange
        PublicationSample anySample = new PublicationSample(anyBiblioData, anyOwner, anyPublicationStatus,
                anyEdition, anyCondition);

        PublicationSample newSample = new PublicationSample(anyBiblioData, anyOwner, anyPublicationStatus,
                anyEdition, Condition.NEW);

        //Assert
        assertNotEquals(anySample, newSample);
    }

    @Test
    void hashCodeShouldBeConsistent() {

        // Arrange
        PublicationSample anySample =
                new PublicationSample(anyBiblioData, anyOwner,
                        anyPublicationStatus, anyEdition, anyCondition);

        int hashA = anySample.hashCode();
        int hashB = anySample.hashCode();

        //Assert
        assertEquals(hashA, hashB);
    }

    @Test
    void differentPublicationSamplesShouldHaveDifferentHashCodes() {

        //Arrange
        BibliographicalData otherBd = mock(BibliographicalData.class);

        PublicationSample goldSample = new PublicationSample(
                anyBiblioData, anyOwner, anyPublicationStatus, anyEdition, anyCondition);

        PublicationSample silverSample = new PublicationSample(
                otherBd, anyOwner, anyPublicationStatus, anyEdition, anyCondition);

        //Act + assert
        assertNotEquals(goldSample.hashCode(), silverSample.hashCode());
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithNull() {

        //Arrange
        PublicationSample sample = new PublicationSample(
                anyBiblioData, anyOwner, anyPublicationStatus, anyEdition, anyCondition);

        PublicationSample nullSample = null;

        //Assert
        assertFalse(sample.equals(nullSample));
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithDifferentType() {

        //Arrange
        PublicationSample sample = new PublicationSample(
                anyBiblioData, anyOwner, anyPublicationStatus, anyEdition, anyCondition);

        //Assert
        assertFalse(sample.equals("not a publication"));
    }

    @Test
    void shouldReturnTrueWhenBibliographicalDataHasGenre() {

        //Arrange + act
        Genre genre = mock(Genre.class);
        when(anyBiblioData.hasGenre(genre)).thenReturn(true);

        PublicationSample sample = new PublicationSample(
                anyBiblioData, anyOwner, anyPublicationStatus, anyEdition, anyCondition);

        //Assert
        assertTrue(sample.hasGenre(genre));
    }

    @Test
    void shouldReturnFalseWhenBibliographicalDataDoesNotHaveGenre() {

        //Arrange + act
        Genre genre = mock(Genre.class);
        when(anyBiblioData.hasGenre(genre)).thenReturn(false);

        PublicationSample sample = new PublicationSample(
                anyBiblioData, anyOwner, anyPublicationStatus, anyEdition, anyCondition);

        //Assert
        assertFalse(sample.hasGenre(genre));
    }
}