package TOPSECRET.domain.Publisher;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;

import java.util.List;
import java.util.Optional;

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
public interface IPublisherRepository {

    /**
     * Creates a new {@link Publisher} from the given data,
     * stores it in the repository, and returns it.
     *
     * @param name the publisher's name
     * @param vatNumber the publisher's VAT number
     * @param contact the publisher's contact information
     * @return the newly created Publisher
     * @throws IllegalArgumentException if a Publisher with the same VAT number already exists
     */
    Publisher createAndAdd(String name, String vatNumber, Contact contact);


    /**
     * Returns all {@link Publisher} instances currently stored in the repository.
     *
     * @return an unmodifiable list of all Publishers; never {@code null}, but may be empty
     */
    List<Publisher> getAllPublishers();

    /**
     * Finds and returns the {@link Publisher} with the given identifier.
     *
     * @param vatNumber the {@link PublisherID} to search for
     * @return an {@link Optional} containing the Publisher if found; empty otherwise
     */
    Optional<Publisher> findByVatNumber(PublisherID vatNumber);
}