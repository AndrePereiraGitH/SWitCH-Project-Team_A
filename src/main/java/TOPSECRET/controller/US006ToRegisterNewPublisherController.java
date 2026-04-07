package TOPSECRET.controller;


import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Publisher.IPublisherRepository;

import javax.management.InstanceAlreadyExistsException;

/**
 * Controller responsible for handling the use case
 * of registering a new publisher (US006).
 *
 * <p>This class acts as an intermediary between the UI layer
 * and the domain/repository layers.</p>
 */
public class   US006ToRegisterNewPublisherController {

    private IPublisherRepository _publisherRepository;
    private final PublisherFactory _publisherFactory;

    /**
     * Constructs a new {@code US006ToRegisterNewPublisherController} with the given repository.
     *
     * @param publisherRepository the repository used to store publishers; must not be {@code null}
     * @throws NullPointerException if {@code publisherRepository} is {@code null}
     */
    public US006ToRegisterNewPublisherController(IPublisherRepository publisherRepository,
                                                 PublisherFactory publisherFactory) {

        if(publisherRepository == null)
            throw new NullPointerException("Repository can not be null");
        if (publisherFactory == null)
                throw new NullPointerException("Factory cannot be null");


        _publisherRepository = publisherRepository;
        _publisherFactory = publisherFactory;
    }

    /*
     * Registers a new publisher in the system by delegating creation to the repository.
     *
     * @param name      the publisher's name; must not be blank
     * @param vatNumber the publisher's VAT number; must not be blank
     * @param contact   the publisher's contact information
     * @return {@code true} if the publisher was successfully created and registered
     * @throws InstanceAlreadyExistsException if a publisher with the same identity already exists
     */
    public boolean registerPublisher(String name, String vatNumber, Contact contact)
            throws InstanceAlreadyExistsException {

        Publisher publisher = _publisherFactory.create(name, vatNumber, contact);

        if (_publisherRepository.exists(publisher)) {
            throw new InstanceAlreadyExistsException("Publisher already exists: " + vatNumber);
        }

        _publisherRepository.save(publisher);
        return true;
    }
}
