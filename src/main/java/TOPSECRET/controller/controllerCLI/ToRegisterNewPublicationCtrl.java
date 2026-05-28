package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.PublicationSampleService;

import javax.management.InstanceAlreadyExistsException;


/**
 * Application controller responsible for handling the use case of registering
 * a new {@link PublicationSample} in the system.
 *
 * <p>This controller follows an orchestration approach, delegating
 * the creation and persistence of publication samples to the
 * {@link PublicationSampleService}.</p>
 *
 * <p>It does not contain domain logic or invariant enforcement beyond basic
 * dependency validation.</p>
 *
 * <p><b>Note:</b> Validation, creation, and persistence responsibilities are
 * delegated to the service layer.</p>
 */

public class ToRegisterNewPublicationCtrl {

    private final PublicationSampleService _publicationSampleService;

    public ToRegisterNewPublicationCtrl(PublicationSampleService publicationSampleService) {

        if (publicationSampleService == null) {
            throw new IllegalArgumentException("PublicationSampleService cannot be left null");

        }

        _publicationSampleService = publicationSampleService;
    }


    /**
     * Creates and persists a new {@link PublicationSample}.
     *
     * @param bibliographicalDataId the bibliographical data identifier; must not be {@code null}
     * @param user                the user identifier; must not be {@code null}
     * @param edition             the edition identifier; must not be {@code null}
     * @param condition           the physical condition of the publication; must not be {@code null}
     * @return the newly created and persisted {@link PublicationSample}
     * @throws IllegalArgumentException if any argument is {@code null} or invalid
     */
    public PublicationSample register(
            BibliographicalDataId bibliographicalDataId,
            UserId user, EditionId edition, Condition condition

    ) throws InstanceAlreadyExistsException {

        if (bibliographicalDataId == null) {
            throw new IllegalArgumentException("BibliographicalData cannot be left null");
        }

        if (user == null) {
            throw new IllegalArgumentException("User Id cannot be left blank");
        }
        if (edition == null) {
            throw new IllegalArgumentException("Edition Id cannot be left null");
        }
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be left null");
        }

        return _publicationSampleService.createAndSave(bibliographicalDataId, user, edition, condition);
    }
}
