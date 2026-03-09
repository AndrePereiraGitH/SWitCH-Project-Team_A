package TOPSECRET.controller;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Publisher;
import TOPSECRET.repository.PublisherRepository;
import TOPSECRET.domain.User;

/**
 * Controller responsible for handling the use case
 * of registering a new Publisher (US006).
 *
 * This class acts as an intermediary between the UI layer
 * and the domain/repository layers.
 */

public class   US006ToRegisterNewPublisherController {

    // Repository used to store and retrieve Publisher entities
    private PublisherRepository _publisherRepository;

    // Admin user performing the registration action
    private User admin;

    /**
     * Constructor that initializes the controller with
     * the required dependencies.
     *
     */
    public US006ToRegisterNewPublisherController(PublisherRepository publisherRepository, User admin) {

        // Store the repository for later use
        this._publisherRepository = publisherRepository;

        // Store the admin user (could be used for authorization or auditing)
        this.admin = admin;

    }

    /**
     * Creates and registers a new Publisher in the system.
     *
     * @param name the publisher's name
     * @param vatNumber the publisher's VAT number
     * @param contact the publisher's contact information
     * @return the newly created Publisher
     */
    public Publisher createPublisher(String name, String vatNumber, Contact contact){

        // Delegate the creation and persistence of the Publisher
        // to the PublisherRepository
        Publisher publisher = _publisherRepository.createAndAdd(name, vatNumber,contact);

        // Return the created Publisher to the caller
        return publisher;
    }

}
