package TOPSECRET.domain.Publisher;

import TOPSECRET.domain.ValueObjects.PublisherID;

import java.util.List;
import java.util.Optional;

/**
 * Contract for repositories responsible for managing {@link Publisher} instances.
 */
public interface IPublisherRepository {

    /**
     * Persists the given {@link Publisher} and returns the saved instance.
     *
     * @param publisher the publisher to save; must not be {@code null}
     * @return the persisted Publisher
     * @throws IllegalArgumentException if a Publisher with the same ID already exists
     */
    Publisher save(Publisher publisher);

    /**
     * Returns whether a Publisher with the given ID exists.
     *
     * @param id the {@link PublisherID} to check
     * @return {@code true} if a Publisher with that ID exists; {@code false} otherwise
     */
    boolean existsById (PublisherID id);

    /**
     * Finds and returns the {@link Publisher} with the given ID.
     *
     * @param id the {@link PublisherID} to search for
     * @return an {@link Optional} containing the Publisher if found; empty otherwise
     */
    Optional<Publisher> findById(PublisherID id);

    /**
     * Returns all {@link Publisher} instances currently stored in the repository.
     *
     * @return a list of all Publishers; never {@code null}, but may be empty
     */
    List<Publisher> findAll();
}