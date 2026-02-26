package TOPSECRET.controller;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.repository.PublicationTypeRepository;

/**
 * Controller responsible for handling the use case of registering
 * a new {@link PublicationType}.
 * <p>
 * This controller coordinates the interaction between the application
 * layer and the {@link PublicationTypeRepository}, delegating:
 * <ul>
 *     <li>Creation of a {@link PublicationType}</li>
 *     <li>Validation and storage in the repository</li>
 * </ul>
 */

public class US002ToRegisterPubTypeController {

    /**
     * Repository responsible for managing publication types.
     */
    private PublicationTypeRepository _pubTypeRepo;

    /**
     * Constructs the controller with a given {@link PublicationTypeRepository}.
     *
     * @param pubTypeRepo the repository used to create and store publication types
     */
    public US002ToRegisterPubTypeController(PublicationTypeRepository pubTypeRepo)
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
     * @param typeOfBinding the binding type of the publication (e.g., HARDBACK, PAPERBACK)
     * @param details       additional descriptive details about the publication type
     * @return the registered {@link PublicationType}
     * @throws IllegalArgumentException if the publication type is null or already exists
     */
    public PublicationType RegisterPubType(MediaFormat mediaFormat, TypeOfBinding typeOfBinding, String details)
    {
        PublicationType pubType = _pubTypeRepo.create(mediaFormat, typeOfBinding, details);
        _pubTypeRepo.addToList(pubType);

        return pubType;
    }
}
