package TOPSECRET.repository;

import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.ValueObjects.PublisherID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of {@link IPublisherRepository}.
 *
 * <p>Publishers are stored in an {@link ArrayList} and are uniquely identified
 * by their VAT number. Duplicate publishers are rejected on insertion.</p>
 */
public class PublisherRepositoryMem implements IPublisherRepository {

    private final List<Publisher> _publishers;
    private final PublisherFactory _publisherFactory;

    /**
     * Constructs a new {@code PublisherRepositoryMem} with the given factory.
     * Initializes an empty internal list of publishers.
     *
     * @param publisherFactory the factory used to create {@link Publisher} instances;
     *                         must not be {@code null}
     * @throws IllegalArgumentException if {@code publisherFactory} is {@code null}
     */
    public PublisherRepositoryMem(PublisherFactory publisherFactory) {
        if (publisherFactory == null) {
            throw new IllegalArgumentException("PublisherFactory cannot be null");
        }
        _publishers = new ArrayList<>();
        _publisherFactory = publisherFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Publisher createAndAdd(String name, String vatNumber, Contact contact) {
        Publisher publisher = _publisherFactory.create(name, vatNumber, contact);

        if (exists(publisher)) {
            throw new IllegalArgumentException("Publisher already exists");
        }
        _publishers.add(publisher);

        return publisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Publisher> getAllPublishers() {
        return List.copyOf(_publishers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Publisher> findByVatNumber(PublisherID vatNumber) {
        return _publishers.stream()
                .filter(p -> p.id().equals(vatNumber))
                .findFirst();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(Publisher publisher) {
        for (Publisher p : _publishers) {
            if (p.equals(publisher)) {
                return true;
            }
        }
        return false;
    }
}