package TOPSECRET.domain;

import java.util.Objects;

public class Publisher {
    private String _name;
    private String _vatNumber;
    private Contact _contact;

    protected Publisher(String name, String vatNumber, Contact contact) {

        // Validator internal to the class Publisher itself
        if (name.isBlank()) {
            throw new IllegalArgumentException("Not a valid name");
        }
        _name = name;

        if (vatNumber.isBlank()) {
            throw new IllegalArgumentException("Not a valid vatNumber");
        }
        _vatNumber = vatNumber;

        _contact = contact;
    }

    // Review what methods we will want in each class

    public String getName() {
        return _name;
    }

     public String getVatNumber() {
         return _vatNumber;
     }

    public Contact getContact() {
        return _contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Publisher that = (Publisher) o;
        return Objects.equals(this.getVatNumber(), that.getVatNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getVatNumber());
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
