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


class US010ToRegisterNewPublicationCtrlTest {


    //Doubles
    private final BibliographicalDataID anyBiblioDataId = mock(BibliographicalDataID.class);
    private final UserId anyUserId = mock(UserId.class);
    private final PublicationStatus anyPublicationStatus = mock(PublicationStatus.class);
    private final EditionID anyEditionId = mock(EditionID.class);
    private final Condition anyCondition = mock(Condition.class);
    private final IPublicationSampleRepository anyPubRepoMem = mock(IPublicationSampleRepository.class);
    private final PublicationSample anyPublicationSample = mock(PublicationSample.class);

    //Actual controller class under test
    private final US010ToRegisterNewPublicationCtrl anyController = new US010ToRegisterNewPublicationCtrl
            (anyPubRepoMem);

    @Test
    void shouldThrowWhenNullRepo() {

        //Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> new US010ToRegisterNewPublicationCtrl(null));
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