package TOPSECRET.domain;

import TOPSECRET.domain.Enum.UserRole;

public class User {

    private String _userName;
    private Contact _contact;
    private Profile _profile; // TODO change to Profile type
    private UserRole _userRole;

    //Constructor
    public User (String _userName, Contact _contact, Profile _profile, UserRole _userRole) {

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

        //Considered that the user role can't be null
        if (_userRole == null) {
            throw new IllegalArgumentException("User role can't be null.");
        }

        this._userName = _userName;
        this._contact = _contact;
        this._profile = _profile;
        this._userRole = _userRole;
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

    public UserRole getUserRole() { return _userRole; }

    // Method: full user information
    public String getUserInfo() {
        return "User: " + _userName + ", Contact: " + _contact + ", Profile: " + _profile + ", UserRole: " + _userRole;}
}