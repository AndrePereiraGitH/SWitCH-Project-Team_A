package TOPSECRET.domain.Edition;


import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.Publisher;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PhysicalAttributes;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;


/**
 * Represents a specific edition of a publication.
 * <p>
 * An {@code Edition} is uniquely defined by its edition number,
 * identification code, physical attributes, publication type,
 * publisher, language and publication date.
 * <p>
 * All fields are mandatory and validated upon construction.
 */
public class Edition implements AggregateRoot<EditionID> {

    private final EditionID _editionID;
    private final String _editionNumber;
    private final Code _code;
    private final PhysicalAttributes _physicalAttributes;
    private final PublicationType _publicationType;
    private final Publisher _publisher;
    private final Locale _editionLanguage;
    private final LocalDate _editionDate;
    private final TypeOfBinding _typeOfBinding;

    /**
     * Creates a new {@code Edition} instance with the specified attributes.
     * <p>
     * All arguments are mandatory and validated during construction.
     * The edition number must not be blank, and the remaining attributes
     * must not be null. The identification code is validated according
     * to the publication type and edition date.
     *
     * @param editionNumber the edition number that identifies this edition
     * @param code the identification code of the edition (ISBN/ISSN)
     * @param physicalAttributes the physical attributes of the edition
     * @param publicationType the publication type of the edition
     * @param publisher the publisher responsible for the edition
     * @param editionLanguage the language of the edition
     * @param editionDate the publication date of the edition
     * @param typeOfBinding the type of binding of the edition
     *
     * @throws IllegalArgumentException if:
     * <ul>
     *   <li>the edition number is null or blank</li>
     *   <li>any mandatory argument is null</li>
     *   <li>the identification code is invalid for the given publication type and date</li>
     * </ul>
     */
    protected Edition(String editionNumber, Code code, PhysicalAttributes physicalAttributes, PublicationType publicationType,
                      Publisher publisher, Locale editionLanguage, LocalDate editionDate, TypeOfBinding typeOfBinding) {
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
        if (typeOfBinding == null) {
            throw new IllegalArgumentException("TypeOfBinding cannot be null");
        }
        validateCode(code, publicationType, editionDate);

        this._editionID = new EditionID(UUID.randomUUID().toString());
        this._editionNumber = editionNumber;
        this._code = code;
        this._physicalAttributes = physicalAttributes;
        this._publicationType = publicationType;
        this._publisher = publisher;
        this._editionLanguage = editionLanguage;
        this._editionDate = editionDate;
        this._typeOfBinding = typeOfBinding;
    }

    /**
     * Validates the identification code according to the publication type
     * and publication year.
     * <p>
     * For books:
     * <ul>
     *   <li>published in 2007 or later, an ISBN-13 is required;</li>
     *   <li>published between 1970 and 2006, an ISBN-10 is required;</li>
     *   <li>published before 1970, no code is required.</li>
     * </ul>
     *
     * For magazines:
     * <ul>
     *   <li>published in 1976 or later, an ISSN is required;</li>
     *   <li>published before 1976, no code is required.</li>
     * </ul>
     *
     * @param code the code to validate
     * @param publicationType the type of publication
     * @param date the publication date used to determine the applicable rules
     * @throws IllegalArgumentException if the required code for the given
     *                                  publication type and year is missing
     */
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

    public TypeOfBinding getTypeOfBinding() {
        return _typeOfBinding;
    }

    /**
     * Compares two editions by value (all attributes except identity).
     *
     * @param o object to compare
     * @return true if all attributes are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Edition that = (Edition) o;
        return Objects.equals(_editionID, that._editionID);
    }

    /**
     * Returns the unique identifier of this edition.
     *
     * @return the edition ID
     */

    @Override
    public EditionID id(){
        return _editionID;
    }

    /**
     * Checks if two Edition objects share the same identity.
     *
     * @param o the object to compare
     * @return {@code true} if both have the same EditionID; {@code false} otherwise
     */

    @Override
    public boolean sameAs(Object o){
        if(o == null || getClass() != o.getClass()) return false;
        Edition edition = (Edition) o;
        return Objects.equals(_editionNumber, edition._editionNumber)
                && Objects.equals(_code, edition._code)
                && Objects.equals(_physicalAttributes, edition._physicalAttributes)
                && Objects.equals(_publicationType, edition._publicationType)
                && Objects.equals(_publisher, edition._publisher)
                && Objects.equals(_editionLanguage, edition._editionLanguage)
                && Objects.equals(_editionDate, edition._editionDate)
                && Objects.equals(_typeOfBinding, edition._typeOfBinding);
    }
}
