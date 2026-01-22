package TOPSECRET.domain;

import TOPSECRET.domain.Enum.UserRole;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String _userName;
    private Contact _contact;
    private Profile _profile;
    private UserRole _userRole;
    private Library _library;
    private List<CollectionOfPublications> _collectionOfPublications;

    //Constructor
    public User(String userName, Contact contact, Profile profile, UserRole userRole) {

        //Considered that the username can't be null
        //nor can it be composed of fewer than 5 digits
        if (userName == null) {
            throw new IllegalArgumentException("Username can't be null.");
        }

        //trim() - Java method to remove spaces
        //length() - Java method to count the length of the provided argument
        if (userName.replace(" ", "").length() < 5) {
            throw new IllegalArgumentException("Username must contain at least 5 characters.");
        }

        if (userRole == null) {
            throw new IllegalArgumentException("UserRole can't be null.");
        }

        this._userName = userName;
        this._contact = contact;
        this._profile = profile;
        this._userRole = userRole;
        this._library = new Library(new ArrayList<>(),userName + "'s Library",this);
        this._collectionOfPublications = new ArrayList<>();
    }

    // Empty Constructor
    public User() {
    }

    // Getters (não sei se necessário)
    public String getUserName() {
        return _userName;
    }

    public Contact getContact() {
        return _contact;
    }

    public Profile getProfile() {
        return _profile;
    }

    // Method: full user information
    public String getUserInfo() {
        return "User: " + _userName + ", Contact: " + _contact + ", Profile: " + _profile;
    }

    // PRIVATE LIST MANAGEMENT
    // Adds a private list to the user
    public boolean addPrivateList(CollectionOfPublications list) {

        if (list == null) return false;

        if (_collectionOfPublications.contains(list)) return false;

        _collectionOfPublications.add(list);
        return true;
    }

    // Checks if the user owns a given private list
    public boolean ownsPrivateList(CollectionOfPublications list) {
        return _collectionOfPublications.contains(list);
    }

    // Returns a copy of private lists
    public List<CollectionOfPublications> getPrivateLists() {
        return new ArrayList<>(_collectionOfPublications);
    }


    // US018
    // Returns the user's library
    public Library getLibrary() {
        return _library;
    }

    // US0018
    // Adds a collection to the user
    public boolean addCollection(CollectionOfPublications collection) {

        if (collection == null) return false;

        if (_collectionOfPublications.contains(collection)) return false;

        _collectionOfPublications.add(collection);
        return true;
    }

    // US0018
    // Checks if the user owns a given collection
    public boolean ownsCollection(CollectionOfPublications collection) {
        return _collectionOfPublications.contains(collection);
    }

    // US0018
    // Checks if a publication exists in the user's library
    public boolean hasPublicationInLibrary(Publication publication) {
        return _library.containsPublication(publication);
    }

}

