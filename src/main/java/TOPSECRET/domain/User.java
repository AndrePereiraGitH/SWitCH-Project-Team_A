package TOPSECRET.domain;

public class User {

    private String _userName;
    private Contact _contact;
    private String _profile; // TODO change to Profile type

    //Constructor
    public User (String _userName, Contact _contact, String _profile){

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

    public String getProfile() {
        return _profile;
    }

    // Method: full user information
    public String getUserInfo() {
        return "User: " + _userName + ", Contact: " + _contact + ", Profile: " + _profile;
    }

}
