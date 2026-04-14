package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;

import java.util.Objects;
import java.util.UUID;


/**
 * Value object that represents the identifier of an Appraisal Entity.
 * Ensures the ID is not null or blank.
 */
public class AppraisalEntityId implements DomainID {

    private final String ap_id;

    /**
     * Creates an AppraisalEntityId with the given string.
     *
     * @param apId the identifier value
     * @throws IllegalArgumentException if the auctionId is null or blank
     */
    public AppraisalEntityId(String apId) {

        if (apId == null || apId.isBlank()) {
            throw new IllegalArgumentException("ID can't be null or blank");
        }

        this.ap_id = apId;
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

        if (object instanceof AppraisalEntityId) {

            AppraisalEntityId exApEnId = (AppraisalEntityId) object;

            if (this.ap_id.equals(exApEnId.ap_id))
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
        return Objects.hash(ap_id);
    }

    /**
     * Returns the string representation of the auctionId.
     *
     * @return auctionId as string
     */
    public String getId() {
        return ap_id;
    }
}