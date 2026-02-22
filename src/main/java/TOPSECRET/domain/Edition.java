package TOPSECRET.domain;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;


/**
 * Represents a specific edition of a publication.
 * <p>
 * An {@code Edition} is uniquely defined by its edition number,
 * identification code, physical attributes, publication type,
 * publisher, language and publication date.
 * <p>
 * All fields are mandatory and validated upon construction.
 */

public class Edition {

    private final String _editionNumber;
    private final Code _code;
    private final PhysicalAttributes _physicalAtributes;
    private final PublicationType _publicationType;
    private final Publisher _publisher;
    private final Locale _editionLanguage;
    private final LocalDate _editionDate;

    public Edition (String editionNumber, Code code, PhysicalAttributes physicalAttributes, PublicationType publicationType, Publisher publisher, Locale editionLanguage, LocalDate editionDate){
        if (editionNumber == null || editionNumber.isBlank()) {
            throw new IllegalArgumentException(" Edition Number cannot be blank");
        }
        if (code == null) {
            throw new IllegalArgumentException(" Code cannot be null");
        }
        if (physicalAttributes == null || publicationType == null || publisher == null) {
            throw new IllegalArgumentException(" Physical Attributes, publication type and/or publisher cannot be null");
        }
        if (editionLanguage == null) {
            throw new IllegalArgumentException(" Edition language cannot be null");
        }
        if (editionDate == null) {
            throw new IllegalArgumentException(" Edition date cannot be null");
        }

        this._editionNumber = editionNumber;
        this._code = code;
        this._physicalAtributes = physicalAttributes;
        this._publicationType = publicationType;
        this._publisher = publisher;
        this._editionLanguage = editionLanguage;
        this._editionDate = editionDate;
    }


    public PublicationType getPublicationType() {
        return _publicationType;
    }

    public Code getCode() {
        return _code;
    }

    /**
     * Compares this edition with another object for equality.
     * <p>
     * Two {@code Edition} objects are considered equal if all their
     * fields are equal.
     *
     * @param o the object to compare with
     * @return {@code true} if the objects are of the same class and
     *         all fields are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Edition edition = (Edition) o;
        return Objects.equals(_editionNumber, edition._editionNumber)
                && Objects.equals(_code, edition._code)
                && Objects.equals(_physicalAtributes, edition._physicalAtributes)
                && Objects.equals(_publicationType, edition._publicationType)
                && Objects.equals(_publisher, edition._publisher)
                && Objects.equals(_editionLanguage, edition._editionLanguage)
                && Objects.equals(_editionDate, edition._editionDate);
    }

    // Getters

    public Publisher getPublisher() {
        return _publisher;
    }
}
