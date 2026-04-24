package TOPSECRET.controller;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ToRegisterNewPublicationCtrl}.
 *
 * <p>This test suite validates the observable behavior of the controller,
 * ensuring correct handling of publication registration requests.</p>
 *
 * <p>The tests follow a black-box approach, focusing on:
 * <ul>
 *   <li>Successful publication registration</li>
 *   <li>Exception propagation from the repository layer</li>
 *   <li>Validation of null input parameters (guard clauses)</li>
 * </ul>
 * </p>
 *
 * <p>Test doubles (Mockito mocks) are used to isolate the controller from
 * its dependency on {@link IPublicationSampleRepository}.</p>
 */

class ToRegisterNewPublicationCtrlTest {


    //Doubles
    private final BibliographicalDataID anyBiblioDataId = mock(BibliographicalDataID.class);
    private final UserId anyUserId = mock(UserId.class);
    private final PublicationStatus anyPublicationStatus = mock(PublicationStatus.class);
    private final EditionID anyEditionId = mock(EditionID.class);
    private final Condition anyCondition = mock(Condition.class);
    private final IPublicationSampleRepository anyPubRepoMem = mock(IPublicationSampleRepository.class);
    private final PublicationSample anyPublicationSample = mock(PublicationSample.class);

    //Actual controller class under test
    private final ToRegisterNewPublicationCtrl anyController = new ToRegisterNewPublicationCtrl
            (anyPubRepoMem);

    @Test
    void shouldThrowWhenNullRepo() {

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> new ToRegisterNewPublicationCtrl(null));
    }

    @Test
    void shouldThrowWhenBibliographicalDataIdIsNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                anyController.registerNewPublication(
                        null,
                        anyUserId,
                        anyPublicationStatus,
                        anyEditionId,
                        anyCondition
                )
        );
    }

    @Test
    void shouldThrowWhenUserIdIsNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                anyController.registerNewPublication(
                        anyBiblioDataId,
                        null,
                        anyPublicationStatus,
                        anyEditionId,
                        anyCondition
                )
        );
    }

    @Test
    void shouldThrowWhenPublicationStatusIsNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                anyController.registerNewPublication(
                        anyBiblioDataId,
                        anyUserId,
                        null,
                        anyEditionId,
                        anyCondition
                )
        );
    }

    @Test
    void shouldThrowWhenEditionIdIsNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                anyController.registerNewPublication(
                        anyBiblioDataId,
                        anyUserId,
                        anyPublicationStatus,
                        null,
                        anyCondition
                )
        );
    }

    @Test
    void shouldThrowWhenConditionIsNull() {

        //Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                anyController.registerNewPublication(
                        anyBiblioDataId,
                        anyUserId,
                        anyPublicationStatus,
                        anyEditionId,
                        null
                )
        );
    }

    @Test
    void shouldRegisterPublicationSuccessfully() throws Exception {
        //Arrange
        when(anyPubRepoMem.createAndAdd(anyBiblioDataId,
                anyUserId,
                anyPublicationStatus,
                anyEditionId,
                anyCondition)).thenReturn(anyPublicationSample);

        //Act
        boolean result = anyController.registerNewPublication(
                anyBiblioDataId,
                anyUserId,
                anyPublicationStatus,
                anyEditionId,
                anyCondition
        );

        //Assert
        assertTrue(result);
    }

    @Test
    void shouldThrowWhenPublicationAlreadyExists() throws Exception {
        //Arrange
        when(anyPubRepoMem.createAndAdd(
                anyBiblioDataId,
                anyUserId,
                anyPublicationStatus,
                anyEditionId,
                anyCondition
        )).thenThrow(new InstanceAlreadyExistsException
                ("PublicationSample already exists in the repository")
        );

        //Act + assert
        assertThrows(InstanceAlreadyExistsException.class, () ->
                anyController.registerNewPublication(
                        anyBiblioDataId,
                        anyUserId,
                        anyPublicationStatus,
                        anyEditionId,
                        anyCondition
                )
        );
    }
}