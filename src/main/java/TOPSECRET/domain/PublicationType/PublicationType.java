package TOPSECRET.domain.PublicationType;


import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;

import java.util.Objects;

/**
 * Domain entity representing a Publication Type.
 *
 * <p>
 * A PublicationType defines the format of a publication
 * (e.g., Book, Magazine, AudioBook).
 * </p>
 *
 * <p>
 * Validation rules:
 * <ul>
 *     <li>Media format must not be null or blank</li>
 * </ul>
 * </p>
 *
 * <p>
 * Identity is defined by {@link PublicationTypeId}.
 * </p>
 */
public class PublicationType implements AggregateRoot<PublicationTypeId>  {

    private PublicationTypeId pub_id;
    private String _mediaFormat;


    /**
     * Creates a new PublicationType.
     *
     * @param mediaFormat the publication format
     * @throws IllegalArgumentException if mediaFormat is null or blank
     */
    protected PublicationType (String mediaFormat) {

        if( mediaFormat == null || mediaFormat.isBlank() || mediaFormat.isEmpty()) {
            throw new IllegalArgumentException("Media format can't be null or empty.");
        }

        this.pub_id = PublicationTypeId.generate();
        this._mediaFormat = mediaFormat;
    }

    // --------------------------------- US004 starts ---------------------------------


    public PublicationTypeId id() {
        return pub_id;
    }

    public String getMediaFormat(){
        return _mediaFormat;
    }


    // --------------------------------- US004 ends ---------------------------------

    public PublicationTypeId identity(){
        return pub_id;
    }

    /**
     * Compares this entity with another based on identifier.
     *
     * @param obj object to compare
     * @return true if both have the same id, false otherwise
     */
   @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        PublicationType that = (PublicationType) obj;
        return  pub_id.equals(that.pub_id);
    }

    /**
     * Returns the hash code based on the identifier.
     *
     * @return hash code value
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(pub_id);
    }

    /**
     * Compares entities based on business identity.
     *
     * @param obj object to compare
     * @return true if both have the same media format, false otherwise
     */
    @Override
    public boolean sameAs(Object obj) {
        if (obj instanceof PublicationType){
            PublicationType newObj = (PublicationType) obj;

            if (
                    this._mediaFormat.equals(newObj._mediaFormat)
            )
                return true;

        }
        return false;
    }
}
