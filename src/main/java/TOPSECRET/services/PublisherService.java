package TOPSECRET.services;

import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.ValueObjects.Contact;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Application service responsible for orchestrating the creation and persistence
 * of {@link Publisher} aggregates.
 *
 * <p>Acts as the intermediary between the controller layer and the domain/persistence layers,
 * delegating creation to {@link PublisherFactory} and persistence to
 * {@link IPublisherRepository}.</p>
 *
 * <p>This service does not enforce domain invariants directly — those are the
 * responsibility of the factory and the domain object. It is responsible for
 * null-guarding its own inputs and coordinating the creation and save flow.</p>
 */
@Service
public class PublisherService {

    private final PublisherFactory _factory;
    private final IPublisherRepository _repository;

    /**
     * Constructs a {@code PublisherService} with the required dependencies.
     *
     * @param factory    the factory used to create {@link Publisher} domain objects; must not be {@code null}
     * @param repository the repository used to persist and retrieve publishers; must not be {@code null}
     */
    public PublisherService(PublisherFactory factory, IPublisherRepository repository) {
        _factory = Objects.requireNonNull(factory, "PublisherFactory cannot be null");
        _repository = Objects.requireNonNull(repository, "IPublisherRepository cannot be null");
    }

    /**
     * Creates a new {@link Publisher} and persists it to the repository.
     *
     * <p>Delegates creation to {@link PublisherFactory#create} and
     * persistence to {@link IPublisherRepository#save}.
     * All parameters are mandatory — passing {@code null} for any argument
     * will throw a {@link NullPointerException}.</p>
     *
     * @param name      the publisher's name; must not be {@code null}
     * @param vatNumber the publisher's VAT number; must not be {@code null}
     * @param contact   the publisher's contact information; must not be {@code null}
     * @return the saved {@link Publisher} with its persisted identity
     * @throws NullPointerException     if any parameter is {@code null}
     * @throws IllegalArgumentException if a publisher with the same identity already exists
     */
    @Transactional
    public Publisher createAndSave(String name, String vatNumber, Contact contact) {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(vatNumber, "VatNumber cannot be null");
        Objects.requireNonNull(contact, "Contact cannot be null");

        Publisher publisher = _factory.create(name, vatNumber, contact);
        return _repository.save(publisher);
    }
}