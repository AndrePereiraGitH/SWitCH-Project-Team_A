/*
package TOPSECRET.domain;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.Publication;

import java.util.List;

public class CollectionOfPublications {

    // Attributes
    private String _collectionName;
    private Genre _collectionGenre;
    private Library _privateLibrary;
    private List<Publication> _collectionOfPublications;
    private boolean _isPrivate;

    // Constructor
    public CollectionOfPublications(String collectionName,
                                    Genre collectionGenre,
                                    Library privateLibrary,
                                    List<Publication> collectionOfPublications) {

        if (collectionName == null || collectionName.isBlank()) {
            throw new IllegalArgumentException("Collection name cannot be null or blank");
        }
        if (collectionGenre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }
        if (privateLibrary == null) {
            throw new IllegalArgumentException("Library cannot be null");
        }
        if (collectionOfPublications == null) {
            throw new IllegalArgumentException("Publications list cannot be null");
        }

        this._collectionName = collectionName;
        this._collectionGenre = collectionGenre;
        this._privateLibrary = privateLibrary;
        this._collectionOfPublications = collectionOfPublications;
        this._isPrivate = true;
    }

    // Getters
    public String getCollectionName() {
        return _collectionName;
    }

    public Genre getCollectionGenre() {
        return _collectionGenre;
    }

    public Library getPrivateLibrary() {
        return _privateLibrary;
    }

    public List<Publication> getCollectionOfPublications() {
        return _collectionOfPublications;
    }

    public boolean isPrivate() {
        return _isPrivate;
    }
}