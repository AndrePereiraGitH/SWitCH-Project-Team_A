package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a collection of publications created by a user.

 * In the context of US017, this class is responsible for representing
 * a private collection with a name and an associated genre.

 * Some attributes (such as library and owner information) are used
 * by other user stories and are not required for the creation logic
 * of US017 itself.
 */

public class CollectionOfPublications {

    // Attributes
    private String _collectionName;
    private Genre _collectionGenre;
    // Library where this collection is stored.
    // This attribute is NOT required for US017,
    // but is used in later user stories involving library management.
//    private Library _privateLibrary; REMOVED
    private List<Publication> _collectionOfPublications;    // TODO change to PublicationSample
    // Username of the collection owner.
    // Ownership validation is handled at repository level (US017),
    // but this attribute supports other user stories.(US20)
    private User _owner;
    private boolean _isPrivate;

    // Constructor
    public CollectionOfPublications(String collectionName, Genre collectionGenre, boolean isPrivate, User user) {

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
        this._isPrivate = isPrivate;
        this._owner = user;
        this._collectionOfPublications = new ArrayList<>();
    }


    // Getters
    public String getCollectionName() {
        return _collectionName;
    }

    public boolean getIsPrivate() { return _isPrivate; }

    public Genre getCollectionGenre() { return _collectionGenre; }

    public User getOwner() { return _owner; }

    // Returns a copy of the publications list
    public List<Publication> getPublications() {
        return new ArrayList<>(_collectionOfPublications);
    }


    // _______________
    // Used in US018
    // _______________
    //  Adds a publication to the private list.
    //  Returns true if the publication was added successfully.
    //  Returns false if the publication already exists in the list.
    public boolean addPublication (Publication publication) {

        if (publication == null) return false;

        if (_collectionOfPublications.contains(publication)) {
            return false;
        }

        _collectionOfPublications.add(publication);
        return true;
    }

    // Validade if a publication exists in the private list.
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
                && Objects.equals(_owner, other._owner)
                && _isPrivate == other._isPrivate
                && _collectionOfPublications.equals(other._collectionOfPublications);
    }

    // This method generates a hash code consistent with equals().
    // If two objects are considered equal by equals(), they must return the same hash code.
    @Override
    public int hashCode() {
        return Objects.hash(
                _collectionName.toLowerCase(),
                _collectionGenre,
                _owner,
                _isPrivate,
                _collectionOfPublications
        );
    }


    /**
     * US019 - to publicly share a collection of publications
     *
     *
     */
    public void set_isPrivate(boolean _isPrivate) {
        this._isPrivate = _isPrivate;
    }

}
