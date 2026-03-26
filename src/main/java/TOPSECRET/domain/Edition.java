package TOPSECRET.domain;


import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.ValueObjects.PhysicalAttributes;

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
    private final PhysicalAttributes _physicalAttributes;
    private final PublicationType _publicationType;
    private final Publisher _publisher;
    private final Locale _editionLanguage;
    private final LocalDate _editionDate;

    protected Edition(String editionNumber, Code code, PhysicalAttributes physicalAttributes, PublicationType publicationType,
                      Publisher publisher, Locale editionLanguage, LocalDate editionDate) {
        if (editionNumber == null || editionNumber.isBlank()) {
            throw new IllegalArgumentException("Edition Number cannot be blank");
        }

        if (physicalAttributes == null || publicationType == null || publisher == null) {
            throw new IllegalArgumentException("Physical Attributes, publication type and/or publisher cannot be null");
        }
        if (editionLanguage == null) {
            throw new IllegalArgumentException("Edition language cannot be null");
        }
        if (editionDate == null) {
            throw new IllegalArgumentException("Edition date cannot be null");
        }
        validateCode(code, publicationType, editionDate);

        this._editionNumber = editionNumber;
        this._code = code;
        this._physicalAttributes = physicalAttributes;
        this._publicationType = publicationType;
        this._publisher = publisher;
        this._editionLanguage = editionLanguage;
        this._editionDate = editionDate;
    }

    private static void validateCode(Code code, PublicationType publicationType, LocalDate date) {
        int year = date.getYear();

        if (publicationType.getMediaFormat().equals("Book")) {
            if (year >= 2007) {
                if (code.getIsbn13() == null || code.getIsbn13().isBlank()) {
                    throw new IllegalArgumentException(
                            "Books published in 2007 or later must have an ISBN-13 code.");
                }
            } else if (year >= 1970) {
                if (code.getIsbn10() == null || code.getIsbn10().isBlank()) {
                    throw new IllegalArgumentException(
                            "Books published between 1970 and 2006 must have an ISBN-10 code.");
                }
            }
            // Before 1970: no code required, nothing to validate

        } else if (publicationType.getMediaFormat().equals("Magazine")) {
            if (year >= 1976) {
                if (code.getIssn() == null || code.getIssn().isBlank()) {
                    throw new IllegalArgumentException(
                            "Magazines published in 1976 or later must have an ISSN code.");
                }
            }
            // Before 1976: no code required
        }
    }


    public PublicationType getPublicationType() {
        return _publicationType;
    }

    public Code getCode() {
        return _code;
    }

    public Publisher getPublisher() {
        return _publisher;
    }

    public String getEditionNumber() {
        return _editionNumber;
    }

    public Locale getEditionLanguage() {
        return _editionLanguage;
    }

    public LocalDate getEditionDate() {
        return _editionDate;
    }

    /**
     * Compares this edition with another object for equality.
     * <p>
     * Two {@code Edition} objects are considered equal if all their
     * fields are equal.
     *
     * @param o the object to compare with
     * @return {@code true} if the objects are of the same class and
     * all fields are equal; {@code false} otherwise
     */

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Edition edition = (Edition) o;
        return Objects.equals(_editionNumber, edition._editionNumber)
                && Objects.equals(_code, edition._code)
                && Objects.equals(_physicalAttributes, edition._physicalAttributes)
                && Objects.equals(_publicationType, edition._publicationType)
                && Objects.equals(_publisher, edition._publisher)
                && Objects.equals(_editionLanguage, edition._editionLanguage)
                && Objects.equals(_editionDate, edition._editionDate);
    }
}
