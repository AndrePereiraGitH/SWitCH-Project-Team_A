package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.AppraisalEntityId;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing an Appraisal Entity.
 *
 * <p>
 * An AppraisalEntity contains a name, contact information,
 * a list of supported PublicationTypes and a list of supported Genres.
 * </p>
 *
 * <p>
 * Validation rules:
 * <ul>
 *     <li>Name must not be null or blank</li>
 *     <li>Contact must not be null</li>
 *     <li>At least one PublicationType is required</li>
 *     <li>At least one Genre is required</li>
 * </ul>
 * </p>
 *
 * <p>
 * Identity is defined by {@link AppraisalEntityId}.
 * </p>
 */
public class AppraisalEntity implements AggregateRoot<AppraisalEntityId> {

    AppraisalEntityId _apEnId;
    private String _name;
    private Contact _contact;
    private List<PublicationType> _publicationTypes;
    private List<Genre> _genres;


    /**
     * Creates a new AppraisalEntity.
     *
     * @param name name of the entity
     * @param contact contact information
     * @param publicationTypes list of publication types
     * @param genres list of genres
     * @throws IllegalArgumentException if any argument is invalid
     */
    protected AppraisalEntity(String name,
                              Contact contact,
                              List<PublicationType> publicationTypes,
                              List<Genre> genres) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("AppraisalEntity name can't be null or empty.");
        }

        if (contact == null) {
            throw new IllegalArgumentException("Contact can't be null.");
        }

        if (publicationTypes == null || publicationTypes.isEmpty()) {
            throw new IllegalArgumentException("At least one PublicationType is required.");
        }

        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("At least one Genre is required.");
        }

        this._apEnId = AppraisalEntityId.generate();
        this._name = name;
        this._contact = contact;
        this._publicationTypes = publicationTypes;
        this._genres = genres;
    }


    // Getters
    public AppraisalEntityId getId () {
        return _apEnId;
    }

    public String getName() {
        return _name;
    }

    public Contact getContact() {
        return _contact;
    }

    public List<PublicationType> getPublicationTypes() {
        return _publicationTypes;
    }

    public List<Genre> getGenres() {
        return _genres;
    }

    public AppraisalEntityId identity(){
        return _apEnId;
    }


    /**
     * Returns a string representation of the entity.
     *
     * @return formatted string with entity data
     */
    @Override
    public String toString () {

        return "AppraisalEntity{" +
                "identidade:" + _apEnId +
                "name:" + _name + '\'' +
                ", contact:" + _contact +
                ", publicationType:" + _publicationTypes +
                ", genre:" + _genres +
                '}';
    }


    /**
     * Compares this entity with another based on identifier.
     *
     * @param obj object to compare
     * @return true if both entities have the same id, false otherwise
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj instanceof AppraisalEntity) {
            AppraisalEntity newObj = (AppraisalEntity) obj;

            if(this._apEnId.equals(newObj._apEnId))
                return true;
        }
        return false;
    }

    /**
     * Returns the hash code based on the identifier.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(_apEnId);
    }

    /**
     * Returns the entity id (DDD contract).
     *
     * @return AppraisalEntityId
     */
    @Override
    public AppraisalEntityId id() {
        return _apEnId;
    }

    /**
     * Compares entities based on business identity.
     *
     * @param obj object to compare
     * @return true if all attributes are equal, false otherwise
     */
    @Override
    public boolean sameAs(Object obj) {
        if (obj instanceof AppraisalEntity){
            AppraisalEntity newObj = (AppraisalEntity) obj;

            if (
                    this._name.equals(newObj._name) &&
                    this._contact.equals(newObj._contact) &&
                    this._genres.equals(newObj._genres) &&
                    this._publicationTypes.equals(newObj._publicationTypes)
            )
                return true;

        }
        return false;
    }
}

