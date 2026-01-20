package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String _userName;
    private Contact _contact;
    private Profile _profile;
    private Library _library;
    private List<CollectionOfPublications> _collectionOfPublications;

    //Constructor
    public User(String _userName, Contact _contact, Profile _profile) {

        //Considered that the username can't be null
        //nor can it be composed of fewer than 5 digits
        if (_userName == null) {
            throw new IllegalArgumentException("Username can't be null.");
        }

        //trim() - Java method to remove spaces
        //length() - Java method to count the length of the provided argument
        if (_userName.replace(" ", "").length() < 5) {
            throw new IllegalArgumentException("Username must contain at least 5 characters.");
        }

        this._userName = _userName;
        this._contact = _contact;
        this._profile = _profile;
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

    //    LIBRARY MANAGEMENT
    //    Adds a publication to the user's library
    public boolean addPublicationToLibrary(Publication publication) {

        if (publication == null) return false;

        return _library.addPublication(publication);
    }

    // Checks if a publication exists in the user's library
    public boolean hasPublicationInLibrary(Publication publication) {
        return _library.containsPublication(publication);
    }

    // Returns user's library
    public Library getLibrary() {
        return _library;
    }

}

