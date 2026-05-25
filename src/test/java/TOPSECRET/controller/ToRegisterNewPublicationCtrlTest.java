package TOPSECRET.controller;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.PublicationSampleService;
import org.junit.jupiter.api.Test;

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
 *   <li>Exception propagation from the service layer</li>
 *   <li>Validation of null input parameters (guard clauses)</li>
 * </ul>
 * </p>
 *
 * <p>Test doubles (Mockito mocks) are used to isolate the controller from
 * its dependency on {@link PublicationSampleService}.</p>
 */

class ToRegisterNewPublicationCtrlTest {

    // Doubles
    private final BibliographicalDataId anyBiblioDataId = mock(BibliographicalDataId.class);
    private final UserId anyUserId = mock(UserId.class);
    private final EditionId anyEditionId = mock(EditionId.class);
    private final Condition anyCondition = mock(Condition.class);
    private final PublicationSampleService publicationSampleService = mock(PublicationSampleService.class);
    private final PublicationSample publicationSample = mock(PublicationSample.class);

    // Actual controller class under test
    private final ToRegisterNewPublicationCtrl anyController =
            new ToRegisterNewPublicationCtrl(publicationSampleService);

    @Test
    void shouldThrowWhenNullRepo() {

        // Act + assert
        assertThrows(IllegalArgumentException.class,
                () -> new ToRegisterNewPublicationCtrl(null));
    }

    @Test
    void shouldThrowWhenBibliographicalDataIdIsNull() {

        // Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                anyController.register(
                        null,
                        anyUserId,
                        anyEditionId,
                        anyCondition
                )
        );
    }

    @Test
    void shouldThrowWhenUserIdIsNull() {

        // Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                anyController.register(
                        anyBiblioDataId,
                        null,
                        anyEditionId,
                        anyCondition
                )
        );
    }


    @Test
    void shouldThrowWhenEditionIdIsNull() {

        // Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                anyController.register(
                        anyBiblioDataId,
                        anyUserId,
                        null,
                        anyCondition
                )
        );
    }

    @Test
    void shouldThrowWhenConditionIsNull() {

        // Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                anyController.register(
                        anyBiblioDataId,
                        anyUserId,
                        anyEditionId,
                        null
                )
        );
    }

    @Test
    void shouldRegisterPublicationSuccessfully() {

        // Arrange
        when(publicationSampleService.createAndSave(
                anyBiblioDataId,
                anyUserId,
                anyEditionId,
                anyCondition
        )).thenReturn(publicationSample);

        // Act
        PublicationSample result = anyController.register(
                anyBiblioDataId,
                anyUserId,
                anyEditionId,
                anyCondition
        );

        // Assert
        assertNotNull(result);
        assertEquals(publicationSample, result);
    }

    @Test
    void shouldThrowWhenPublicationAlreadyExists() {

        // Arrange
        when(publicationSampleService.createAndSave(
                anyBiblioDataId,
                anyUserId,
                anyEditionId,
                anyCondition
        )).thenThrow(new IllegalArgumentException(
                "PublicationSample already exists in the repository")
        );

        // Act + assert
        assertThrows(IllegalArgumentException.class, () ->
                anyController.register(
                        anyBiblioDataId,
                        anyUserId,
                        anyEditionId,
                        anyCondition
                )
        );
    }
}