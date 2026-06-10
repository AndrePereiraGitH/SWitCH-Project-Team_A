package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.PublisherService;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceNotFoundException;
import java.util.Objects;

/**
 * Controller responsible for handling the use case
 * of registering a new publisher (US006) via the CLI.
 *
 * <p>This class acts as an intermediary between the CLI UI layer
 * and the application service layer.</p>
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
     * @param contact   the publisher's contact information; must not be null
     * @param userId    the identifier of the user performing the registration; must not be null
     * @throws IllegalArgumentException if any business validation fails or the user is not found
     */
    public void registerPublisher(String name, String vatNumber, Contact contact, UserId userId)
            throws AccessDeniedException, InstanceNotFoundException {
        Objects.requireNonNull(contact, "Contact information is required");
        Objects.requireNonNull(userId, "A valid User ID must be provided");

        _publisherService.createAndSave(name, vatNumber, contact, userId);
    }
}