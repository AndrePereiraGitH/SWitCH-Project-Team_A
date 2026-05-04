package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        EditionID that = (EditionID) o;
        return Objects.equals(editionID, that.editionID);
    }

    public String getID(){return editionID;}

}
