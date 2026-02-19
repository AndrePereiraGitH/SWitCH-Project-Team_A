package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a collection of publications created by a user.
 *
 * A collection has a name, genre, and can be private or public.
 * Collections are created private by default (US017) but can be made public (US019).
 */

public class CollectionOfPublications {

    // Attributes
    private String _collectionName;
    private Genre _collectionGenre;
    private List<Publication> _collectionOfPublications;
    // private List<PublicationSample> _collectionOfPublications;
    private String _ownerUserName;
    private boolean _isPrivate;

    /**
     * Creates a new collection of publications.
     *
     * @param collectionName Name of the collection
     * @param collectionGenre Genre of the collection
     * @param user Owner of the collection
     * @throws IllegalArgumentException if any parameter is null or invalid
     */

    // Constructor
    public CollectionOfPublications(String collectionName, Genre collectionGenre, User user) {

        if (collectionName == null || collectionName.isBlank()) {
            throw new IllegalArgumentException("Collection name cannot be null or empty.");
        }

        if (collectionGenre == null) {
            throw new IllegalArgumentException("Collection genre cannot be null.");
        }

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        this._collectionName = collectionName;
        this._collectionGenre = collectionGenre;
        this._isPrivate = true;
        this._ownerUserName = user.getUserName();
        this._collectionOfPublications = new ArrayList<>();
    }


    // Getters
    public String getCollectionName() {
        return _collectionName;
    }

    public boolean getIsPrivate() { return _isPrivate; }

    public Genre getCollectionGenre() { return _collectionGenre; }

    public String getOwner() { return _ownerUserName; }

    /**
     * Returns a copy of the publications list.
     * Modifications to the returned list will not affect the collection.
     */
    // public List<PublicationSample> getPublications() {
    public List<Publication> getPublications() {
        return new ArrayList<>(_collectionOfPublications);
    }


    /**
     * US019 - to publicly share a collection of publications
     *
     *
     */
    public void set_isPrivate(boolean _isPrivate) {
        this._isPrivate = _isPrivate;
    }

    /**
     * US018 - Adds a publication to the collection.
     * Duplicates are not allowed.
     *
     * @param publication Publication to add
     * @return true if added, false if null or already exists
     */
    // public boolean addPublication(PublicationSample publication) {
    public boolean addPublication (Publication publication) {

        if (publication == null) return false;

        if (_collectionOfPublications.contains(publication)) {
            return false;
        }

        _collectionOfPublications.add(publication);
        return true;
    }

    /**
     * Checks if a publication exists in the collection.
     *
     * @param publication Publication to check
     * @return true if exists, false otherwise
     */
    // public boolean containsPublication(PublicationSample publication) {
    public boolean containsPublication (Publication publication) {
        return _collectionOfPublications.contains(publication);
    }

    // It allows two CollectionOfPublications objects to be compared by their content,
    // instead of by memory reference.
    // This is necessary to support unit tests that use assertEquals(expected, result).
    // Without overriding equals(), Java would only compare object references,
    // and two objects with the same data would be considered different.
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (!(obj instanceof CollectionOfPublications)) return false;

        CollectionOfPublications other = (CollectionOfPublications) obj;

        return _collectionName.equalsIgnoreCase(other._collectionName)
                && _collectionGenre.equals(other._collectionGenre)
                && _isPrivate == other._isPrivate
                && _collectionOfPublications.equals(other._collectionOfPublications);
    }

    // This method generates a hash code consistent with equals().
    // If two objects are considered equal by equals(), they must return the same hash code.
    @Override
    public int hashCode() {
        return (_collectionName.toLowerCase()
                + _collectionGenre.hashCode()
                + _isPrivate
                + _collectionOfPublications.hashCode()).hashCode();
    }

}
