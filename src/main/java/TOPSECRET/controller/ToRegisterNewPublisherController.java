package TOPSECRET.controller;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.services.PublisherService;

/**
 * Controller responsible for handling the use case
 * of registering a new publisher (US006).
 *
 * <p>This class acts as an intermediary between the UI layer
 * and the service layer.</p>
 */
public class ToRegisterNewPublisherController {

    private final PublisherService _publisherService;

    /**
     * Constructs a new {@code ToRegisterNewPublisherController} with the given service.
     *
     * @param publisherService the service used to create and persist publishers; must not be {@code null}
     * @throws IllegalArgumentException if {@code publisherService} is {@code null}
     */
    public ToRegisterNewPublisherController(PublisherService publisherService) {
        if (publisherService == null) {
            throw new IllegalArgumentException("PublisherService cannot be null");
        }
        _publisherService = publisherService;
    }

    /**
     * Registers a new publisher in the system by delegating to the service layer.
     *
     * @param name      the publisher's name; must not be blank
     * @param vatNumber the publisher's VAT number; must not be blank
     * @param contact   the publisher's contact information
     * @return {@code true} if the publisher was successfully created and registered
     * @throws IllegalArgumentException if a publisher with the same identity already exists
     */
    public boolean registerPublisher(String name, String vatNumber, Contact contact) {
        _publisherService.createAndSave(name, vatNumber, contact);
        return true;
    }
}