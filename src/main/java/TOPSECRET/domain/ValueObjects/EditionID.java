package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.util.Objects;
/**
 * Value Object that represents the unique identifier of an Edition.
 *
 * <p>
 * This identifier is immutable and ensures that each Edition can be
 * uniquely distinguished within the system.
 * </p>
 *
 * <p>
 * Two EditionID instances are considered equal if they contain the same value.
 * </p>
 */

@Embeddable
@NoArgsConstructor
public class EditionID implements DomainID {

    private String editionID;

    /**
     * Creates a new EditionID.
     *
     * @param editionID the identifier value
     * @throws IllegalArgumentException if the id is null or blank
     */
    public EditionID(String editionID){
        if(editionID == null || editionID.isBlank()){
            throw new IllegalArgumentException("ID cannot be null or blank");
        }

        this.editionID = editionID;
    }

    /**
     * Compares this EditionID with another object.
     *
     * @param o the object to compare
     * @return true if both identifiers are equal, false otherwise
     */
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        EditionID that = (EditionID) o;
        return Objects.equals(editionID, that.editionID);
    }

    /**
     * Returns the identifier value.
     *
     * @return the edition ID as a String
     */
    public String getID(){return editionID;}


}
