package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.GenreID;


import java.util.List;
import java.util.Objects;

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

    private final AppraisalEntityId _apEnId;
    private String _name;
    private Contact _contact;
    private List<PublicationTypeId> _publicationTypeId;
    private List<GenreID> _genreId;


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
                              List<GenreID> genreId) {

        validate(name, contact, publicationTypeId, genreId);

        this._apEnId = AppraisalEntityId.generate();
        this._name = name;
        this._contact = contact;
        this._publicationTypeId = publicationTypeId;
        this._genreId = genreId;
    }

    /**
     * Creates a new AppraisalEntity.
     *
     * @param id appraisal entity id
     * @param name name of the entity
     * @param contact contact information
     * @param publicationTypeId list of publication types
     * @param genreId list of genres
     * @throws IllegalArgumentException if any argument is invalid
     */
    protected AppraisalEntity(AppraisalEntityId id,
                              String name,
                              Contact contact,
                              List<PublicationTypeId> publicationTypeId,
                              List<GenreID> genreId) {

        if (id == null) {
            throw new IllegalArgumentException("AppraisalEntity id can't be null.");
        }
        validate(name, contact, publicationTypeId, genreId);

        this._apEnId = id;
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

    public List<PublicationTypeId> getPublicationTypeIds() {
        return List.copyOf(_publicationTypeId);
    }

    public List<GenreID> getGenreIds() {
        return List.copyOf(_genreId);
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

        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AppraisalEntity)) {
            return false;
        }

        AppraisalEntity other = (AppraisalEntity) obj;

        return Objects.equals(this._apEnId, other._apEnId);

    }

    @Override
    public int hashCode() {
        return Objects.hash(_apEnId);
    }

    @Override
    public boolean sameAs(Object obj) {

        if (!(obj instanceof AppraisalEntity)) return false;

        AppraisalEntity other = (AppraisalEntity) obj;

        return Objects.equals(this._name, other._name) &&
                Objects.equals(this._contact, other._contact) &&
                Objects.equals(this._genreId, other._genreId) &&
                Objects.equals(this._publicationTypeId, other._publicationTypeId);

    }

    /**
     * Updates the state of this AppraisalEntity.
     *
     * <p>
     * Applies new values while enforcing all domain invariants.
     * This method follows a fluent style and returns the updated instance.
     * </p>
     *
     * @param name the new name of the entity
     * @param contact the new contact information
     * @param publicationTypes updated list of publication types
     * @param genres updated list of genres
     * @return the updated AppraisalEntity instance
     * @throws IllegalArgumentException if any argument is invalid
     */
    public AppraisalEntity update(String name,
                                  Contact contact,
                                  List<PublicationTypeId> publicationTypes,
                                  List<GenreID> genres
    ) {
        validate(name, contact, publicationTypes, genres);

        this._name = name;
        this._contact = contact;
        this._publicationTypeId = publicationTypes;
        this._genreId = genres;
        return this;
    }

    /**
     * Validates the invariants of an AppraisalEntity.
     *
     * @param name entity name
     * @param contact contact information
     * @param publicationTypes publication types list
     * @param genres genres list
     * @throws IllegalArgumentException if any argument is invalid
     */
    private void validate( String name,
                           Contact contact,
                           List<PublicationTypeId> publicationTypes,
                           List<GenreID> genres
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
        if (publicationTypes == null || publicationTypes.isEmpty()) {
            throw new IllegalArgumentException("Publication types cannot be empty");
        }
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Genres cannot be empty");
        }

    }
}

