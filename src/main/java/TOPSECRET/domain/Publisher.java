package TOPSECRET.domain;

public class Publisher {
    private String _name;
    private String _vatNumber;
    private Address _officialAddress; //discuss the need to have an address here
    private Contact _contact;

    public Publisher(String name, String vatNumber, Address address, Contact contact) {

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
        _officialAddress = address;
    }

    // Review what methods we will want in each class

    public String getName() {
        return _name;
    }
//
//    public String getVatNumber() {
//        return vatNumber;
//    }
//
//    public Address getAddress() {
//        return address;
//    }
//
}
