package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.repository.PublicationSampleRepositoryMem;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US010ToRegisterNewPublicationCtrl}.
 *
 * <p>This test suite verifies the behavior of the controller responsible
 * for registering a new {@link PublicationSample} in the system.</p>
 *
 * <p>The controller depends on {@link PublicationSampleRepositoryMem}, which
 * is mocked to isolate the controller logic and verify the interaction
 * between the controller and the repository.</p>
 */


class US010ToRegisterNewPublicationCtrlTest {


    //Mocks
    private final BibliographicalData anyBiblioData = mock(BibliographicalData.class);
    private final User anyUser = mock(User.class);
    private final PublicationStatus anyPublicationStatus = mock(PublicationStatus.class);
    private final Edition anyEdition = mock(Edition.class);
    private final Condition anyCondition = mock(Condition.class);
    private final PublicationSampleRepositoryMem anyPublicationSampleRepositoryMem = mock(PublicationSampleRepositoryMem.class);
    private final PublicationSample anyPublicationSample = mock(PublicationSample.class);

    //Actual controller class under test
    private final US010ToRegisterNewPublicationCtrl anyController = new US010ToRegisterNewPublicationCtrl
            (anyPublicationSampleRepositoryMem);

    @Test
    void shouldThrowWhenNullRepo() {

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> new US010ToRegisterNewPublicationCtrl(null));
    }

    @Test
    void shouldCallRepoToCreatePubSample() throws InstanceAlreadyExistsException {

        //Arrange
        when(anyPublicationSampleRepositoryMem.createAndAdd(anyBiblioData, anyUser, anyPublicationStatus,
                anyEdition, anyCondition)).thenReturn(anyPublicationSample);

        //Act
        anyController.createAndAddPublication(anyBiblioData, anyUser, anyPublicationStatus, anyEdition, anyCondition);

        //Assert
        verify(anyPublicationSampleRepositoryMem).createAndAdd(anyBiblioData, anyUser, anyPublicationStatus,
                anyEdition, anyCondition);

    }

    @Test
    void shouldReturnPubSampleFromRepo() throws InstanceAlreadyExistsException {

        //Arrange
        when(anyPublicationSampleRepositoryMem.createAndAdd(
                anyBiblioData,
                anyUser,
                anyPublicationStatus,
                anyEdition,
                anyCondition
        )).thenReturn(anyPublicationSample);

        //Act
        PublicationSample result = anyController.createAndAddPublication(
                anyBiblioData,
                anyUser,
                anyPublicationStatus,
                anyEdition,
                anyCondition);

        //Assert
        assertEquals(anyPublicationSample, result);
    }

    /**
     * Verifies that the controller propagates the exception thrown by the repository.
     *
     * <p>If the {@link PublicationSampleRepositoryMem} detects that the publication
     * being registered already exists, it throws an {@link InstanceAlreadyExistsException}.
     * Since the controller does not handle this exception internally, it must allow
     * the exception to continue towards the caller.</p>
     *
     * <p>This test configures the mocked repository to throw the exception and
     * verifies that calling the controller method results in the same exception
     * being thrown.</p>
     */

    @Test
    void shouldThrowExceptionWhenRepositoryThrows() throws InstanceAlreadyExistsException {

        //Arrange
        when(anyPublicationSampleRepositoryMem.createAndAdd(
                anyBiblioData,
                anyUser,
                anyPublicationStatus,
                anyEdition,
                anyCondition)).thenThrow
                (InstanceAlreadyExistsException.class);

        //Act + assert
        assertThrows(InstanceAlreadyExistsException.class, ()
                -> anyController.createAndAddPublication(anyBiblioData, anyUser, anyPublicationStatus, anyEdition,
                anyCondition));
    }

}