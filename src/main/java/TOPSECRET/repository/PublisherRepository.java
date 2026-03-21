package TOPSECRET.repository;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Publisher;
import java.util.List;

/**
 * Contract for repositories responsible for managing {@link Publisher} instances.
 * <p>
 * A Publisher is uniquely identified by its VAT number. This interface defines
 * the operations required to create, store, and check the existence of Publisher
 * objects, independently of the underlying storage mechanism (e.g., in-memory, database).
 * </p>
 *
 * <p>
 * Implementations must enforce uniqueness of Publisher instances and throw
 * appropriate exceptions when invalid or duplicate entries are provided.
 * </p>
 */
public interface PublisherRepository {

    /**
     * Creates a new {@link Publisher} for the given data,
     * stores it in the repository, and returns it.
     *
     * @param name the publisher's name
     * @param vatNumber the publisher's VAT number
     * @param contact the publisher's contact information
     * @return the newly created Publisher
     * @throws IllegalArgumentException if the created Publisher already exists
     */
    Publisher createAndAdd(String name, String vatNumber, Contact contact);

    /**
     * Checks whether a given {@link Publisher} already exists in the repository.
     *
     * @param publisher the Publisher to check
     * @return true if the Publisher already exists, false otherwise
     */
    boolean exists(Publisher publisher);

    /**
     * Adds a {@link Publisher} to the repository, throwing an exception if it already exists.
     *
     * @param publisher the Publisher instance to be added
     * @throws IllegalArgumentException if the publisher already exists or is null
     */
    void add(Publisher publisher);

    List<Publisher> getAllPublishers();

    Publisher findByVatNumber(String vatNumber);
}

