package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value object that represents the identifier of an Appraisal Entity.
 * Ensures the ID is not null or blank.
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppraisalEntityId implements DomainID {

    private String ap_id;

    /**
     * Creates an AppraisalEntityId with the given string.
     *
     * @param apId the identifier value
     * @throws IllegalArgumentException if the id is null or blank
     */
    public AppraisalEntityId(String apId) {

        if (apId == null || apId.isBlank()) {
            throw new IllegalArgumentException("ID can't be null or blank");
        }

        this.ap_id = apId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (object instanceof AppraisalEntityId other) {
            return Objects.equals(this.ap_id, other.ap_id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ap_id);
    }

    /**
     * Returns the string representation of the id.
     */
    public String getId() {
        return ap_id;
    }
}