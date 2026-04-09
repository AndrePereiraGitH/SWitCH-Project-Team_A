package TOPSECRET.controller;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.UserId;

import javax.management.InstanceAlreadyExistsException;


/**
 * Application controller responsible for handling the use case of registering
 * a new {@link PublicationSample} in the system.
 *
 * <p>This controller follows an orchestration approach, delegating
 * the creation and persistence of publication samples to the
 * {@link IPublicationSampleRepository}.</p>
 *
 * <p>It does not contain domain logic or invariant enforcement beyond basic
 * dependency validation.</p>
 *
 * <p><b>Note:</b> This implementation assumes that all required identifiers
 * (e.g., {@link UserId}, {@link BibliographicalDataID}, {@link EditionID})
 * are already validated and provided by the caller.</p>
 */

public class US010ToRegisterNewPublicationCtrl {

    private final IPublicationSampleRepository _publicationRepository;

    public US010ToRegisterNewPublicationCtrl(IPublicationSampleRepository publicationRepository) {

        if (publicationRepository == null) {
            throw new IllegalArgumentException("PublicationRepository cannot be left blank");

        }

        this._publicationRepository = publicationRepository;
    }

    /**
     * Registers a new {@link PublicationSample} in the system.
     *
     * <p>This method delegates the creation and persistence process to the
     * {@link IPublicationSampleRepository#createAndAdd} method.</p>
     *
     * @param bibliographicalData1 the identifier of the bibliographical data
     * @param user1                the identifier of the user registering the publication
     * @param publicationStatus1   the status of the publication
     * @param _edition             the identifier of the edition
     * @param condition            the condition of the publication
     * @return {@code true} if the publication was successfully created
     * @throws InstanceAlreadyExistsException if a publication with the same identity already exists
     * @throws IllegalArgumentException       if any argument is invalid (propagated from repository)
     */

    public boolean registerNewPublication(
            BibliographicalDataID bibliographicalData1,
            UserId user1, PublicationStatus publicationStatus1, EditionID _edition, Condition condition

    ) throws InstanceAlreadyExistsException {

        PublicationSample newPublication = _publicationRepository.createAndAdd(bibliographicalData1, user1,
                publicationStatus1, _edition, condition);

        return true;
    }
}
