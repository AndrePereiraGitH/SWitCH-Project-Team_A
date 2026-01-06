package TOPSECRET.domain;

public class Publisher {
    private String name;
    private String vatNumber;
    private Contact contact;

    public Publisher(String name, String vatNumber, Address address, Contact contact) {

        // Validates attributes that are String
        if (Validator.isNullOrBlank(name)) {
            throw new IllegalArgumentException("Not a valid name");
        }
        this.name = name;

        if (Validator.isNullOrBlank(vatNumber)) {
            throw new IllegalArgumentException("Not a valid vatNumber");
        }
        this.vatNumber = vatNumber;

        this.contact = contact;
    }

    // Review what methods we will want in each class

    public String getName() {
        return name;
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
