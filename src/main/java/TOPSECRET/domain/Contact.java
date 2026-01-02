package TOPSECRET.domain;

public class Contact {

    private String _phoneNum;
    private String _extension;
    private String _email; // TODO change to Email type
    private Location _location; // TODO change to Address type

    // Constructor
    public Contact(String phoneNum, String extension, String email, Location location) {

        if (extension == null) {
            throw new IllegalArgumentException("Extension can´t be null");
        }

        this._phoneNum = phoneNum;
        this._extension = extension;
        this._email = email;
        this._location = location;
    }

    // Empty Constructor
    public Contact () {

    }

    // Getters (não sei se necessário)
    public String getPhoneNum() {
        return _phoneNum;
    }

    public String getExtension() {
        return _extension;
    }

    public String getEmail() {
        return _email;
    }

    public Location getAddress() {
        return _location;
    }

    // Method: Get full phone number (extension + number)
    public String getFullPhone() {

        return _extension + " " + _phoneNum;
    }

    // Method: Get full contact information
    public String getFullContactInfo() {

        return "Full Contact - " +
                "Phone number: " + _extension + " " + _phoneNum +
                ", e-mail: " + _email +
                ", address: " + _location;
    }

}
