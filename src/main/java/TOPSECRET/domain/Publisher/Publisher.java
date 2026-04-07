package TOPSECRET.domain.Publisher;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;

import java.util.Objects;

/**
 * Represents a publisher aggregate root in the system.
 *
 * <p>A publisher is uniquely identified by its {@link PublisherID}, which is derived
 * from its VAT number. Instances are immutable after creation.</p>
 */

public class Publisher implements AggregateRoot<PublisherID> {
    private final PublisherID _publisherID;
    private final String _name;
    private final String _vatNumber;
    private final Contact _contact;

    /**
     * Constructs a new {@code Publisher} with the given name, VAT number, and contact.
     *
     * @param name      the publisher's name; must not be blank
     * @param vatNumber the publisher's VAT number; must not be blank
     * @param contact   the publisher's contact information
     * @throws IllegalArgumentException if {@code name} or {@code vatNumber} is {@code null} or blank
     */
    protected Publisher(String name, String vatNumber, Contact contact) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Not a valid name");
        }
        _name = name;

        if (vatNumber == null || vatNumber.isBlank()) {
            throw new IllegalArgumentException("Not a valid vatNumber");
        }
        _publisherID = new PublisherID(vatNumber);
        _vatNumber = vatNumber;

        _contact = contact;
    }

    public String getName() {
        return _name;
    }

     public String getVatNumber() {
         return _vatNumber;
     }

    public Contact getContact() {
        return _contact;
    }

    /**
     * Returns the unique identifier of this {@code Publisher}.
     *
     * @return the {@link PublisherID} identifying this publisher; never {@code null}
     */

    @Override
    public PublisherID id() {
        return _publisherID;
    }

    /**
     * Determines whether this {@code Publisher} represents the same business entity
     * as another object, based on name and contact equality.
     *
     * @param o the object to compare with
     * @return {@code true} if {@code o} is a {@code Publisher} with the same name and contact;
     *         {@code false} otherwise
     */

    @Override
    public boolean sameAs(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Publisher that = (Publisher) o;
        return Objects.equals(_vatNumber, that._vatNumber);
    }

    /**
     * Indicates whether some other object is equal to this {@code Publisher}.
     *
     * <p>Two publishers are considered equal if and only if they share the same
     * {@link PublisherID}.</p>
     *
     * @param o the reference object with which to compare
     * @return {@code true} if {@code o} is a {@code Publisher} with the same identifier;
     *         {@code false} otherwise
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Publisher that = (Publisher) o;
        return Objects.equals(_publisherID, that._publisherID);
    }

    /**
     * Returns a hash code value for this {@code Publisher}, based solely on its identifier.
     *
     * @return the hash code of this publisher's {@link PublisherID}
     */

    @Override
    public int hashCode() {
        return Objects.hash(_publisherID);
    }

    /**
     * Retrieves the details of this Publisher.
     *
     * <p>This method is used in the context of US027 –
     * "To get the list of items on direct sale of a given Publisher".
     * It provides a {@link PublisherDetails} object containing the key
     * information required to identify and describe the Publisher,
     * namely its name, VAT number, and contact information.</p>
     *
     * @return a new {@link PublisherDetails} instance with the Publisher's name, VAT number, and contact.
     */

    public Publisher.PublisherDetails getPublisherDetails() {
        return new Publisher.PublisherDetails(
                _name,
                _vatNumber,
                _contact
        );
    }

    /**
     * Immutable data structure representing the details of a Publisher.
     *
     * <p>This record encapsulates the Publisher's name, VAT number,
     * and contact information. It is used to safely and clearly transport
     * Publisher-related data, particularly within the scope of US027.</p>
     *
     * @param name      the Publisher's name
     * @param vatNumber the Publisher's VAT number
     * @param contact   the Publisher's contact information
     */

    public record PublisherDetails(
            String name,
            String vatNumber,
            Contact contact
    ) {}
}
