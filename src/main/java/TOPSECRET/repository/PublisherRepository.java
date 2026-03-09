package TOPSECRET.repository;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Publisher;
import TOPSECRET.domain.PublisherFactory;

import java.util.ArrayList;
import  java.util.List;

/**
 * Repository responsible for managing {@link Publisher} entities in memory.
 *
 * <p>This class acts as a collection manager that handles the creation,
 * storage, and retrieval of {@link Publisher} objects.</p>
 *
 * <p>Publishers are uniquely identified by their VAT number.
 * The repository prevents duplicate publishers from being added.</p>
 */

public class   PublisherRepository {

    // Internal list that stores all registered publishers
    private List<Publisher> publishers;

    /** Factory responsible for creating {@link Publisher} instances. */
    private PublisherFactory publisherFactory;


    /**
     * Default constructor that initializes the repository
     * with an empty list of publishers and a new {@link PublisherFactory}.
     */
    public PublisherRepository() {
        this(new ArrayList<>(), new PublisherFactory());
}

    /**
     * Constructor that allows dependency injection.
     *
     * <p>This constructor is mainly used for testing, allowing
     * a custom list and factory to be provided.</p>
     *
     * @param publishers the list used to store publishers
     * @param publisherFactory the factory responsible for creating publishers
     * @throws IllegalArgumentException if publishers or publisherFactory are null
     */
public PublisherRepository(List<Publisher> publishers, PublisherFactory publisherFactory) {

    if (publishers == null) {
        throw new IllegalArgumentException("Publisher list cannot be null");
    }

    if (publisherFactory == null) {
        throw new IllegalArgumentException("PublisherFactory cannot be null");
    }

    this.publishers = publishers;
    this.publisherFactory = publisherFactory;
}

    /**
     * Creates and adds a new {@link Publisher} to the repository.
     *
     * <p>The publisher is created using the {@link PublisherFactory}.
     * If a publisher with the same identity already exists
     * (based on {@link Publisher#equals(Object)}), the operation is rejected.</p>
     *
     * @param name the publisher's name
     * @param vatNumber the publisher's VAT number (must be unique)
     * @param contact the publisher's contact information
     * @return the created Publisher, or null if a Publisher
     *         with the same VAT number already exists
     */
    public Publisher  createAndAdd(String name, String vatNumber, Contact contact) {

        // Create a new Publisher instance with the given data
        Publisher publisher = publisherFactory.create( name,  vatNumber,  contact);

        // Check if a publisher with the same identity already exists
        if (exists(publisher)) {
            // Prevent duplicate publishers from being added
            return null;
        }

        // Store the publisher in the repository
        publishers.add(publisher);

        // Return the newly created publisher
        return publisher;
    }

    /**
     * Returns all publishers stored in the repository.
     *
     * A read-only copy is returned to prevent external code
     * from modifying the internal list.
     */
    public List<Publisher> getAllPublishers() {
        return List.copyOf(publishers); // safe read-only copy
    }

    /**
     * Finds a publisher by its VAT number.
     *
     * @param vatNumber the VAT number to search for
     * @return the matching Publisher, or null if not found
     */
    public Publisher findByVatNumber(String vatNumber) {

        // Iterate through the stored publishers
        for (Publisher p : publishers) {

            // Compare VAT numbers to find a match
            if (p.getVatNumber().equals(vatNumber)) {
                return p;
            }
        }
        // No publisher found with the given VAT number
        return null; // or throw exception, depending on your style
    }

    /**
     * Checks whether a given Publisher already exists in the repository.
     *
     * Existence is determined using the Publisher.equals() method,
     * which should define what makes two publishers equal
     * (typically the VAT number).
     */

    private boolean exists(Publisher publisher) {

        // Compare the new publisher against existing ones
        for (Publisher p : publishers) {
            if (p.equals(publisher)) {
                return true;
            }
        }

        // No matching publisher found
        return false;
    }
}
