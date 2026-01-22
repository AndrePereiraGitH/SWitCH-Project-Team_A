package TOPSECRET.domain;

import java.util.Objects;

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

     public String getVatNumber() {
         return _vatNumber;
     }
//
//    public Contact get_contact() {
//        return _contact;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Publisher that = (Publisher) o;
        return Objects.equals(this.getVatNumber(), that.getVatNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getVatNumber());
    }
}
