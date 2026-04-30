package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.GenreId;


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
    private List<PublicationTypeId> _publicationTypeId;
    private List<GenreId> _genreId;


    /**
     * Creates a new AppraisalEntity.
     *
     * @param name name of the entity
     * @param contact contact information
     * @param publicationTypeId list of publication types
     * @param genreId list of genres
     * @throws IllegalArgumentException if any argument is invalid
     */
    protected AppraisalEntity(String name,
                              Contact contact,
                              List<PublicationTypeId> publicationTypeId,
                              List<GenreId> genreId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("AppraisalEntity name can't be null or empty.");
        }
        if (contact == null) {
            throw new IllegalArgumentException("Contact can't be null.");
        }
        if (publicationTypeId == null || publicationTypeId.isEmpty()) {
            throw new IllegalArgumentException("At least one PublicationType is required.");
        }
        if (genreId == null || genreId.isEmpty()) {
            throw new IllegalArgumentException("At least one Genre is required.");
        }
        this._apEnId = new AppraisalEntityId(UUID.randomUUID().toString());
        this._name = name;
        this._contact = contact;
        this._publicationTypeId = publicationTypeId;
        this._genreId = genreId;
    }


    // Getters
    @Override
    public AppraisalEntityId id () {
        return _apEnId;
    }

    public String getName() {
        return _name;
    }

    public Contact getContact() {
        return _contact;
    }

    public List<PublicationTypeId> getPublicationTypeId() {
        return _publicationTypeId;
    }

    public List<GenreId> getGenreId() {
        return _genreId;
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
                "identity:" + _apEnId +
                "name:" + _name + '\'' +
                ", contact:" + _contact +
                ", publication type:" + _publicationTypeId +
                ", genre:" + _genreId +
                '}';
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(_apEnId);
    }

    @Override
    public boolean sameAs(Object obj) {
        if (!(obj instanceof AppraisalEntity)) {
            return false;
        }

        AppraisalEntity newObj = (AppraisalEntity) obj;

        return  this._name.equals(newObj._name) &&
                this._contact.equals(newObj._contact) &&
                this._genreId.equals(newObj._genreId) &&
                this._publicationTypeId.equals(newObj._publicationTypeId);

    }
}

