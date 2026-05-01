package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.PublisherID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of {@link IPublisherRepository}.
 *
 * <p>Publishers are stored in an {@link ArrayList} and are uniquely identified
 * by their ID. Duplicate publishers are rejected on insertion.</p>
 */
public class PublisherRepositoryMem implements IPublisherRepository {

    private final List<Publisher> _publishers = new ArrayList<>();

    /** {@inheritDoc} */
    @Override
    public Publisher save(Publisher publisher) {
        if (existsById(publisher.id())) {
            throw new IllegalArgumentException("Publisher already exists");
        }
        _publishers.add(publisher);
        return publisher;
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsById(PublisherID id) {
        for (Publisher p : _publishers) {
            if (p.id().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Publisher> findByVatNumber(PublisherID id) {
        for (Publisher p : _publishers) {
            if (p.id().equals(id)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    /** {@inheritDoc} */
    @Override
    public List<Publisher> findAll() {
        return List.copyOf(_publishers);
    }
}