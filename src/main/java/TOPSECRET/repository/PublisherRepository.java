package TOPSECRET.repository;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Publisher;
import TOPSECRET.domain.PublisherFactory;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link Publisher} entities in memory.
 *
 * <p>This class acts as a collection manager that handles the creation,
 * storage, and retrieval of {@link Publisher} objects.</p>
 *
 * <p>Publishers are uniquely identified by their VAT number.
 * The repository prevents duplicate publishers from being added.</p>
 */
public class PublisherRepository {

    private List<Publisher> _publishers;
    private PublisherFactory _publisherFactory;

    /**
     * Constructs a new {@code PublisherRepository} with the given factory.
     * Initializes an empty internal list of publishers.
     *
     * @param publisherFactory the factory responsible for creating {@link Publisher} instances;
     *                         must not be {@code null}
     * @throws IllegalArgumentException if {@code publisherFactory} is {@code null}
     */
    public PublisherRepository(PublisherFactory publisherFactory) {
        if (publisherFactory == null) {
            throw new IllegalArgumentException("PublisherFactory cannot be null");
        }
        _publishers = new ArrayList<>();
        _publisherFactory = publisherFactory;
    }

    /**
     * Adds a {@link Publisher} directly to the repository.
     * <p>
     * <b>Note:</b> This method is intended for testing purposes only
     * and should not be used in production code.
     * </p>
     *
     * @param publisher the publisher to add; should not be {@code null}
     */
    void add(Publisher publisher) {
        _publishers.add(publisher);
    }

    /**
     * Creates and adds a new {@link Publisher} to the repository.
     *
     * <p>The publisher is created using the {@link PublisherFactory}.
     * If a publisher with the same identity already isExistent
     * (based on {@link Publisher#equals(Object)}), the operation is rejected.</p>
     *
     * @param name      the publisher's name
     * @param vatNumber the publisher's VAT number; must be unique within the repository
     * @param contact   the publisher's contact information
     * @return the newly created and registered {@link Publisher}
     * @throws InstanceAlreadyExistsException if a publisher with the same identity already isExistent
     */
    public Publisher createAndAdd(String name, String vatNumber, Contact contact) throws InstanceAlreadyExistsException {

        Publisher publisher = _publisherFactory.create(name, vatNumber, contact);

        if (isExistent(publisher)) {
            throw new InstanceAlreadyExistsException("This publisher already isExistent");
        }
        _publishers.add(publisher);

        return publisher;
    }

    /**
     * Returns an unmodifiable copy of all publishers stored in the repository,
     * preventing external modification of the internal list.
     *
     * @return an immutable list of all {@link Publisher} instances; never {@code null}
     */
    public List<Publisher> getAllPublishers() {
        return List.copyOf(_publishers);
    }

    /**
     * Finds a publisher by its VAT number.
     *
     * @param vatNumber the VAT number to search for; must not be {@code null}
     * @return the matching {@link Publisher}, or {@code null} if none is found
     */
    public Publisher findByVatNumber(String vatNumber) {
        for (Publisher publisher : _publishers) {
            if (publisher.getVatNumber().equals(vatNumber)) {
                return publisher;
            }
        }
        return null;
    }

    /**
     * Checks whether a given {@link Publisher} already isExistent in the repository.
     *
     * <p>Existence is determined using {@link Publisher#equals(Object)},
     * which is based on the VAT number.</p>
     *
     * @param publisher the publisher to check; must not be {@code null}
     * @return {@code true} if an equal publisher is already present; {@code false} otherwise
     */
    private boolean isExistent(Publisher publisher) {
        for (Publisher p : _publishers) {
            if (p.equals(publisher)) {
                return true;
            }
        }
        return false;
    }
}