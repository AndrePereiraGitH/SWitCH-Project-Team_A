package TOPSECRET.domain;

import TOPSECRET.domain.Enum.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private String _userName;
    private Contact _contact;
    private Profile _profile;
    private UserRole _userRole;

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
    }

    // Empty Constructor
    public User() {
    }

    // Getters
    public String getUserName() {
        return _userName;
    }

    public Contact getContact() {
        return _contact;
    }

    public Profile getProfile() {
        return _profile;
    }

    public UserRole getUserRole() {
        return _userRole;
    }

    // Method: full user information
    public String getUserInfo() {
        return "User: " + _userName + ", Role: " + _userRole + ", Contact: " + _contact + ", Profile: " + _profile;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(_userName, user._userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_userName);
    }


}

