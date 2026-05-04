package TOPSECRET.domain.PublicationType;


import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;

import java.util.Objects;
import java.util.UUID;

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

        if( mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("Media format can't be null or empty.");
        }

        this.pub_id = new PublicationTypeId(UUID.randomUUID().toString());
        this._mediaFormat = mediaFormat;
    }

    /**
     * Recreates a PublicationType from DB.
     *
     * @param id the publication type id
     * @param mediaFormat the publication format
     * @throws IllegalArgumentException if mediaFormat or id are null or blank
     */
    protected PublicationType (PublicationTypeId id, String mediaFormat) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        if (mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("Media format can't be null or empty.");
        }

        this.pub_id = id;
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

   @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        PublicationType that = (PublicationType) obj;
        return  pub_id.equals(that.pub_id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(pub_id);
    }

    @Override
    public boolean sameAs(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PublicationType)) {
            return false;
        }

        PublicationType other = (PublicationType) obj;

        return this._mediaFormat.trim()
                .equalsIgnoreCase(other._mediaFormat.trim());
    }
}
