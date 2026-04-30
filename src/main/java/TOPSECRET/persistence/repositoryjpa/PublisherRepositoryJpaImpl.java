package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.persistence.repositoryjpa.assembler.PublisherAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublisherDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IPublisherRepositoryJpa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JPA implementation of the {@link IPublisherRepository} interface.
 *
 * <p>This repository is responsible for persisting and retrieving {@link Publisher}
 * domain objects using a Spring Data JPA repository. It converts between
 * domain objects and persistence data models through {@link PublisherAssembler}.</p>
 */
@Repository
public class PublisherRepositoryJpaImpl implements IPublisherRepository {

    private final IPublisherRepositoryJpa _jpa;
    private final PublisherAssembler _assembler;

    /**
     * Constructs a {@code PublisherRepositoryJpaImpl} with the required dependencies.
     *
     * @param jpa      the Spring Data JPA repository; must not be {@code null}
     * @param assembler the assembler used to convert between domain and persistence objects; must not be {@code null}
     */
    public PublisherRepositoryJpaImpl(IPublisherRepositoryJpa jpa, PublisherAssembler assembler) {
        _jpa = Objects.requireNonNull(jpa, "IPublisherRepositoryJpa cannot be null");
        _assembler = Objects.requireNonNull(assembler, "PublisherAssembler cannot be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Publisher save(Publisher publisher) {
        Objects.requireNonNull(publisher, "Publisher cannot be null");

        if (existsById(publisher.id())) {
            throw new IllegalArgumentException("Publisher already exists");
        }

        PublisherDM dm = _assembler.toDM(publisher);
        PublisherDM savedDM = _jpa.save(dm);
        return _assembler.toDomain(savedDM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(PublisherID id) {
        Objects.requireNonNull(id, "PublisherID cannot be null");
        return _jpa.existsById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Publisher> findByVatNumber(PublisherID id) {
        Objects.requireNonNull(id, "PublisherID cannot be null");
        return _jpa.findById(id).map(_assembler::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Publisher> findAll() {
        return _jpa.findAll().stream()
                .map(_assembler::toDomain)
                .toList();
    }
}