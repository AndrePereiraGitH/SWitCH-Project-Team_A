package TOPSECRET.domain;

import TOPSECRET.domain.Genre.Genre;

import java.util.List;

/**
 * Domain entity representing an Appraisal Entity.
 * An AppraisalEntity contains:
 * - A name
 * - Contact information
 * - A list of supported PublicationTypes (1..*)
 * - A list of supported Genres (1..*)
 * Two AppraisalEntity objects are considered equal if they share
 * the same name and contact information.
 */

public class AppraisalEntity {

    private String _name;
    private Contact _contact;
    private List<PublicationType> _publicationTypes;
    private List<Genre> _genres;

    /**
     * Creates a new AppraisalEntity.
     * Validates that:
     * - Name is not null or blank
     * - Contact is not null
     * - At least one PublicationType exists
     * - At least one Genre exists
     * @param name Name of the Appraisal Entity
     * @param contact Contact information
     * @param publicationTypes List of supported PublicationTypes (1..*)
     * @param genres List of supported Genres (1..*)
     */
    protected AppraisalEntity(String name, Contact contact, List<PublicationType> publicationTypes, List<Genre> genres) {

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

        this._name = name;
        this._contact = contact;
        this._publicationTypes = publicationTypes;
        this._genres = genres;
    }

    // Getters
    /**
     * Returns the name of the Appraisal Entity.
     * @return AppraisalEntity name
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns the contact information of the Appraisal Entity.
     * @return Contact object
     */
    public Contact getContact() {
        return _contact;
    }

    /**
     * Returns the list of supported PublicationTypes.
     * @return List of PublicationType
     */
    public List<PublicationType> getPublicationTypes() {
        return _publicationTypes;
    }

    /**
     * Returns the list of supported Genres.
     * @return List of Genre
     */
    public List<Genre> getGenres() {
        return _genres;
    }


    /**
     * Returns a formatted String containing the full information
     * of the AppraisalEntity.
     * @return Formatted String representation
     */
    public String AppraisalEntityFullInformation () {

        return "AppraisalEntity{" +
                "name:'" + _name + '\'' +
                ", contact:" + _contact +
                ", publicationType:" + _publicationTypes +
                ", genre:" + _genres +
                '}';
    }


    /**
     * Compares two AppraisalEntity objects.
     * Two entities are considered equal if:
     * - They have the same name (case-insensitive)
     * - They have the same contact
     * @param obj Object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AppraisalEntity)) return false;

        AppraisalEntity newObj = (AppraisalEntity) obj;

        // Two AppraisalEntity objects are considered equal if they have the same name and contact.
        // PublicationType and Genre are not used to define the entity identity.
        return _name.equalsIgnoreCase(newObj._name) &&
                _contact.equals(newObj._contact);
    }

    /**
     * Generates a hash code consistent with equals().
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return (_name.toLowerCase() + _contact.hashCode()).hashCode();
    }
}

