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
     * Checks whether a given {@link Publisher} already exists in the repository.
     *
     * @param publisher the Publisher to check
     * @return {@code true} if the Publisher already exists, {@code false} otherwise
     */
    boolean exists(Publisher publisher);

    /**
     * Returns all {@link Publisher} instances currently stored in the repository.
     *
     * @return an unmodifiable list of all Publishers; never {@code null}, but may be empty
     */
    List<Publisher> getAllPublishers();

    /**
     * Finds and returns the {@link Publisher} with the given VAT number.
     *
     * @param vatNumber the VAT number to search for
     * @return the Publisher with the matching VAT number, or {@code null} if not found
     */
    Publisher findByVatNumber(String vatNumber);
}