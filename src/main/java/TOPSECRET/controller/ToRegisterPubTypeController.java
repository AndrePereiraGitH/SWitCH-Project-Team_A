package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.services.PublicationTypeService;

/**
 * Controller responsible for handling the registration of new PublicationType entities.
 * Acts as an intermediary between the presentation layer and the service layer.
 */
public class ToRegisterPubTypeController {

    /**
     * Service used to manage PublicationType operations.
     */
    private final PublicationTypeService service;

    /**
     * Constructs a ToRegisterPubTypeController with the specified service.
     *
     * @param service the PublicationTypeService used to create and persist publication types
     * @throws IllegalArgumentException if the provided service is {@code null}
     */
    public ToRegisterPubTypeController(PublicationTypeService service) throws IllegalArgumentException{
        if (service == null) {
            throw new IllegalArgumentException("Service must not be null");
        }
        this.service = service;
    }

    /**
     * Registers a new PublicationType with the given media format.
     *
     * @param mediaFormat the format of the publication (e.g., digital, print)
     * @return the created and saved PublicationType instance
     */
    public PublicationType registerPubType(String mediaFormat)
    {
        return service.createAndSave(mediaFormat);
    }
}
