package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;

import java.util.Objects;


/**
 * Value object that represents the identifier of a Publication Type.
 * Ensures the ID is not null or blank.
 */
public class PublicationTypeId implements DomainID {

    private final String pbType_id;


    /**
     * Creates a PublicationTypeId with the given string.
     *
     * @param pbTypeId the identifier value
     * @throws IllegalArgumentException if the auctionId is null or blank
     */
    public PublicationTypeId(String pbTypeId) {

        if (pbTypeId == null || pbTypeId.isBlank()) {
            throw new IllegalArgumentException("ID can't be null or blank");
        }

        this.pbType_id = pbTypeId;
    }


    /**
     * Compares this object with another for equality.
     *
     * @param object the object to compare
     * @return true if both have the same auctionId, false otherwise
     */
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

    /**
     * Returns the hash code of the auctionId.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(pbType_id);
    }

    /**
     * Returns the string representation of the auctionId.
     *
     * @return auctionId as string
     */
    public String getId() {
        return pbType_id;
    }


}
