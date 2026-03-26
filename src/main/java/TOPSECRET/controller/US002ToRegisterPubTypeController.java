package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.repository.IPublicationTypeRepository;

/**
 * Controller responsible for handling the use case of registering a new {@link PublicationType}.
 * <p>
 * This controller coordinates the interaction between the application
 * layer and the {@link IPublicationTypeRepository}, delegating:
 * <ul>
 *     <li>Creation of a {@link PublicationType}</li>
 *     <li>Validation and storage in the repository</li>
 * </ul>
 */

public class US002ToRegisterPubTypeController {

    /**
     * Repository responsible for managing publication types.
     */
    private IPublicationTypeRepository _pubTypeRepo;

    /**
     * Constructs the controller with a given {@link IPublicationTypeRepository}.
     *
     * @param pubTypeRepo the repository used to create and store publication types
     */
    public US002ToRegisterPubTypeController(IPublicationTypeRepository pubTypeRepo)
    {
        this._pubTypeRepo = pubTypeRepo;
    }


    /**
     * Registers a new {@link PublicationType} in the repository.
     * <p>
     * This method:
     * <ol>
     *     <li>Creates a new publication type using the repository</li>
     *     <li>Adds the publication type to the repository</li>
     * </ol>
     *
     * @param mediaFormat   the media format of the publication (e.g., BOOK, MAGAZINE)

     * @return the registered {@link PublicationType}
     * @throws IllegalArgumentException if the publication type is null or already exists
     */
    public PublicationType RegisterPubType(String mediaFormat)
    {
        PublicationType pubType = _pubTypeRepo.create(mediaFormat);
        _pubTypeRepo.addToList(pubType);

        return pubType;
    }
}
