package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;


/**
 * Value object that represents the identifier of a Publication Type.
 * Ensures the ID is not null or blank.
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicationTypeId implements DomainID {

    private String pbType_id;

    /**
     * Creates a PublicationTypeId with the given string.
     *
     * @param pbTypeId the identifier value
     * @throws IllegalArgumentException if the id is null or blank
     */
    public PublicationTypeId(String pbTypeId) {

        if (pbTypeId == null || pbTypeId.isBlank()) {
            throw new IllegalArgumentException("ID can't be null or blank");
        }

        this.pbType_id = pbTypeId;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object instanceof PublicationTypeId) {

            PublicationTypeId exPubTypeId = (PublicationTypeId) object;

            if (this.pbType_id.equals(exPubTypeId.pbType_id))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pbType_id);
    }

    /**
     * Returns the string representation of the id.
     * @return id as string
     */
    public String getId() {
        return pbType_id;
    }


}
