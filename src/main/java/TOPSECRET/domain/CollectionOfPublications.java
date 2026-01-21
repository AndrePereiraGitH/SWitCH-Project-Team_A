package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Genres;

import java.util.ArrayList;
import java.util.List;


// Represents a user's private list of publications.
// A PrivateList groups publications according to a specific purpose or genre.
public class CollectionOfPublications {

    private String _name;
    private Genres _genre;
    private List<Publication> _publications;

    // Constructor
    public CollectionOfPublications(String name, Genres genre) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("PrivateList name cannot be null or empty.");
        }

        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null.");
        }

        this._name = name;
        this._genre = genre;
        this._publications = new ArrayList<>();
    }


    // GETTERS
    public String getName() {
        return _name;
    }

    public Genres getGenre() {
        return _genre;
    }

    // Returns a copy of the publications list
    public List<Publication> getPublications() {
        return new ArrayList<>(_publications);
    }


    //  Adds a publication to the private list.
    //  Returns true if the publication was added successfully.
    //  Returns false if the publication already exists in the list.
    public boolean addPublication (Publication publication) {

        if (publication == null) return false;

        if (_publications.contains(publication)) {
            return false;
        }

        _publications.add(publication);
        return true;
    }

    // Validade if a publication exists in the private list.
    public boolean containsPublication (Publication publication) {
        return _publications.contains(publication);
    }

    // Removes a publication from the private list.
    public boolean removePublication(Publication publication) {
        return _publications.remove(publication);
    }

//    public boolean isPrivate() {
//        return _isPrivate;
//    }
}

