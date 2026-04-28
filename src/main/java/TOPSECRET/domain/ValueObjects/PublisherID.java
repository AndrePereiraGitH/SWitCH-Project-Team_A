package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Value Object representing the unique identifier of a Publisher aggregate.
 *
 * <p>Implements the {@link DomainID} marker interface as required by the DDD-layered structure.
 * Identity is based solely on the underlying {@code String} value, making two instances with
 * the same value equal regardless of reference.</p>
 *
 * <p>Instances of this class are immutable. Once created, the ID value cannot be changed.</p>
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublisherID implements DomainID {

    private String _publisherID;

    /**
     * Constructs a new {@code PublisherID} with the given value.
     *
     * @param publisherID the string value for this ID; must not be {@code null} or blank
     * @throws IllegalArgumentException if {@code publisherID} is {@code null} or blank
     */
    public PublisherID(String publisherID) {
        if (publisherID == null || publisherID.isBlank()) {
            throw new IllegalArgumentException("PublisherId must be a non empty String");
        }
        this._publisherID = publisherID;
    }


    public String getId() {
        return _publisherID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublisherID that = (PublisherID) o;
        return Objects.equals(_publisherID, that._publisherID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_publisherID);
    }

    public String getPublisherId() {
        return _publisherID;
    }
}
