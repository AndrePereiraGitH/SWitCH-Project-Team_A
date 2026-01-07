package TOPSECRET.domain;

public class Publisher {
    private String _name;
    private String _vatNumber;
    private Contact _contact;

    public Publisher(String name, String vatNumber, Contact contact) {

        // Validator internal to the class Publisher itself
        if (name.isBlank()) {
            throw new IllegalArgumentException("Not a valid name");
        }
        _name = name;

        if (vatNumber.isBlank()) {
            throw new IllegalArgumentException("Not a valid vatNumber");
        }
        _vatNumber = vatNumber;

        _contact = contact;
    }

    // Review what methods we will want in each class

    public String getName() {
        return _name;
    }

//    public String get_vatNumber() {
//        return _vatNumber;
//    }
//
//    public Contact get_contact() {
//        return _contact;
//    }
}
