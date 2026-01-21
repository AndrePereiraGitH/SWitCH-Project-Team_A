package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Genres;

import java.util.ArrayList;
import java.util.List;


// Represents a user's private list of publications.
// A PrivateList groups publications according to a specific purpose or genre.
public class CollectionOfPublications {

    private String _collectionName;
    private Genre _collectionGenre;
    private Library _privateLibrary;
    private List<Publication> _collectionOfPublications;
    private boolean _isPrivate;

    // Constructor
    public CollectionOfPublications(String collectionName, Genre collectionGenre, Library privateLibrary, boolean isPrivate) {

        if (collectionName == null || collectionName.isBlank()) {
            throw new IllegalArgumentException("Collection name cannot be null or empty.");
        }

        if (collectionGenre == null) {
            throw new IllegalArgumentException("Collection genre cannot be null.");
        }

        if (privateLibrary == null) {
            throw new IllegalArgumentException("Library cannot be null.");
        }

        this._collectionName = collectionName;
        this._collectionGenre = collectionGenre;
        this._privateLibrary = privateLibrary;
        this._isPrivate = isPrivate;
        this._collectionOfPublications = new ArrayList<>();
    }


    // GETTERS
    public String getCollectionName() {
        return _collectionName;
    }

    public Genre getCollectionGenre() {
        return _collectionGenre;
    }

    public Library getPrivateLibrary() {
        return _privateLibrary;
    }

    // Returns a copy of the publications list
    public List<Publication> getPublications() {
        return new ArrayList<>(_collectionOfPublications);
    }


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
                && _privateLibrary.equals(other._privateLibrary)
                && _isPrivate == other._isPrivate
                && _collectionOfPublications.equals(other._collectionOfPublications);
    }

    // This method generates a hash code consistent with equals().
    // If two objects are considered equal by equals(), they must return the same hash code.
    @Override
    public int hashCode() {
        return (_collectionName.toLowerCase()
                + _collectionGenre.hashCode()
                + _privateLibrary.hashCode()
                + _isPrivate
                + _collectionOfPublications.hashCode()).hashCode();
    }

}

