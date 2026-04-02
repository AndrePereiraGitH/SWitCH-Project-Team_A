package TOPSECRET.dto;

import TOPSECRET.domain.Publisher.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) used in the context of US027 to represent
 * the essential information of a Publisher that must be returned to the user.
 *
 * <p>This DTO provides a simplified, presentation‑friendly view of a Publisher,
 * containing only its name and VAT number, in String form.
 * It is used to decouple the domain model from the data exposed externally,
 * ensuring that only the required fields are transferred.</p>
 *
 * <p>The class is immutable: all fields are {@code final} and no setters are provided.
 * Instances can be created directly or through the static factory methods
 * {@link #toDTO(Publisher)} and {@link #toDTOs(List)}.</p>
 */

public class PublisherDTO_US027 {

    private final String _name;
    private final String _vatNumber;

    // ======= Constructor =============================================================================================

    /**
     * Creates a new {@code PublisherDTO_US027} with the given values.
     *
     * @param name      the name of the Publisher
     * @param vatNumber the VAT number of the Publisher
     */

    public PublisherDTO_US027(String name, String vatNumber) {
        this._name = name;
        this._vatNumber = vatNumber;
    }


    // ======= Getters =================================================================================================

    /**
     * Returns the name of the Publisher.
     *
     * @return the Publisher's name
     */

    public String getName() {
        return _name;
    }

    /**
     * Returns the VAT number of the Publisher.
     *
     * @return the Publisher's VAT number
     */

    public String getVatNumber() {
        return _vatNumber;
    }


    // ======== Equals and HashCode ====================================================================================

    /**
     * Indicates whether this DTO is equal to another object.
     * Two DTOs are considered equal if all their fields match.
     *
     * @param o the object to compare with
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublisherDTO_US027 that = (PublisherDTO_US027) o;
        return Objects.equals(_name, that._name) && Objects.equals(_vatNumber, that._vatNumber);
    }

    /**
     * Computes the hash code for this DTO based on all its fields.
     *
     * @return the hash code value
     */

    @Override
    public int hashCode() {
        return Objects.hash(_name, _vatNumber);
    }


    // toDTO(s) ========================================================================================================

    /**
     * Converts a {@link Publisher} domain object into a {@code PublisherDTO_US027}.
     *
     * <p>This method is used when preparing the data to be returned to the user
     * in the context of US027. Only the relevant fields are extracted from the
     * domain object.</p>
     *
     * @param publisher the Publisher to convert; must not be {@code null}
     * @return a new {@code PublisherDTO_US027} containing the Publisher's data
     * @throws NullPointerException if {@code publisher} is {@code null}
     */

    public static PublisherDTO_US027 toDTO(Publisher publisher) {

        if  (publisher == null) {
            throw new NullPointerException("Publisher can not be null");
        }
        return new PublisherDTO_US027(
                publisher.getName(),
                publisher.getVatNumber()
        );
    }

    /**
     * Converts a list of {@link Publisher} objects into an immutable list of
     * {@code PublisherDTO_US027} instances.
     *
     * <p>This method validates that the list is not {@code null} and that it does
     * not contain {@code null} elements. It is typically used when returning the
     * full list of registered Publishers to the user in US027.</p>
     *
     * @param listOfAllPublishers the list of Publisher objects to convert; must not be {@code null}
     * @return an immutable list of {@code PublisherDTO_US027} objects
     * @throws NullPointerException     if {@code publishers} is {@code null}
     * @throws IllegalArgumentException if any element in the list is {@code null}
     */

    public static List<PublisherDTO_US027> toDTOs(List<Publisher> listOfAllPublishers) {

        if  (listOfAllPublishers == null) {
            throw new NullPointerException("List of publishers can not be null");
        }

        List<PublisherDTO_US027> listOfPublishersDTO = new ArrayList<>();

        for (Publisher publisher : listOfAllPublishers) {
            if (publisher == null) {
                throw new IllegalArgumentException("List of publishers must not contain null elements");
            }
            listOfPublishersDTO.add(toDTO(publisher));
        }

        return List.copyOf(listOfPublishersDTO);
    }
}
