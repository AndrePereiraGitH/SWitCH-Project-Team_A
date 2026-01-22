package TOPSECRET.controller;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Publisher;
import TOPSECRET.repository.PublisherRepository;
import TOPSECRET.domain.User;

public class US006ToRegisterNewPublisherController {

    private PublisherRepository _publisherRepository;
    private User admin;

    public US006ToRegisterNewPublisherController(PublisherRepository publisherRepository, User admin) {

        this._publisherRepository = publisherRepository;
        this.admin = admin;

    }
    public Publisher createPublisher(String name, String vatNumber, Contact contact){

        Publisher publisher = _publisherRepository.add(name, vatNumber,contact);

        return publisher;
    }

}
