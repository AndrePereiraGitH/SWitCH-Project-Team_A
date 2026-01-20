package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Genre;

public class AppraisalEntity {

//    private String _name;
//    private Contact _contact;
//    private PublicationType _publicationType;  // ToDo change to PublicationType
//    private Genre _genre;
//
//    // Construtor
//    public AppraisalEntity(String name, Contact contact, PublicationType publicationType, Genre genre) {
//
//        if (name == null || name.isBlank()) {
//            throw new IllegalArgumentException("AppraisalEntity name can't be null or empty.");
//        }
//
//        if (contact == null) {
//            throw new IllegalArgumentException("Contact can't be null.");
//        }
//
//
//        if (publicationType == null) {
//            throw new IllegalArgumentException("PublicationType can't be null.");
//        }
//
//        if (genre == null) {
//            throw new IllegalArgumentException("Genre can't be null.");
//        }
//
//        this._name = name;
//        this._contact = contact;
//        this._publicationType = publicationType;
//        this._genre = genre;
//    }
//
//    // Getters
//    public String getName() {
//        return _name;
//    }
//
//    public Contact getContact() {
//        return _contact;
//    }
//
//    public PublicationType getPublicationType() {
//        return _publicationType;
//    }
//
//    public Genre getGenre() {
//        return _genre;
//    }
//
//    public String AppraisalEntityFullInformation () {
//
//        return "AppraisalEntity{" +
//                "name:'" + _name + '\'' +
//                ", contact:" + _contact +
//                ", publicationType:" + _publicationType +
//                ", genre:" + _genre +
//                '}';
//    }
//
//    //  This method overrides Object.equals(Object obj).
//    //  The parameter type must be Object (not AppraisalEntity), otherwise the method would not
//    //  correctly override the original equals() method.
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (!(obj instanceof AppraisalEntity)) return false;
//
//        AppraisalEntity newObj = (AppraisalEntity) obj;
//
//        // Two AppraisalEntity objects are considered equal if they have the same name and contact.
//        // PublicationType and Genre are not used to define the entity identity.
//        return _name.equalsIgnoreCase(newObj._name) &&
//                _contact.equals(newObj._contact);
//    }
//
//    // This method generates a hash code consistent with equals().
//    // If two objects are considered equal, they must return the same hash code.
//    // This is required for correct behavior in hash-based collections.
//    @Override
//    public int hashCode() {
//        return (_name.toLowerCase() + _contact.hashCode()).hashCode();
//    }
}

