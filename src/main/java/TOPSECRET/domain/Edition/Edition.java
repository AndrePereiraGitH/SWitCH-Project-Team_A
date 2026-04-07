package TOPSECRET.domain.Edition;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.*;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;


/**
 * Represents a specific edition of a publication.
 * <p>
 * An {@code Edition} is uniquely defined by its edition number,
 * identification code, physical attributes, publication type ID,
 * publisher ID, media format, language and publication date.
 * <p>
 * This entity references other aggregates only by their identifiers,
 * following Domain-Driven Design principles.
 */
public class Edition implements AggregateRoot<EditionID> {

    /** Constant representing a book media format. */
    public static final String BOOK = "Book";

    /** Constant representing a magazine media format. */
    public static final String MAGAZINE = "Magazine";

    /** Unique identifier of the edition. */
    private final EditionID _editionID;

    /** Edition number (e.g., "1st Edition"). */
    private final String _editionNumber;

    /** Identification code (ISBN/ISSN). */
    private final Code _code;

    /** Physical characteristics of the edition. */
    private final PhysicalAttributes _physicalAttributes;

    /** Identifier of the publication type. */
    private final PublicationTypeId _publicationTypeId;

    /** Identifier of the publisher. */
    private final PublisherID _publisherID;

    /** Media format of the publication (Book or Magazine). */
    private final String _mediaFormat;

    /** Language of the edition. */
    private final Locale _editionLanguage;

    /** Publication date of the edition. */
    private final LocalDate _editionDate;

    /** Type of binding of the edition. */
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
     * @param publicationTypeId the publication type of the edition
     * @param publisherID the publisher responsible for the edition
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
    protected Edition(String editionNumber,
                      Code code,
                      PhysicalAttributes physicalAttributes,
                      PublicationTypeId publicationTypeId,
                      PublisherID publisherID,
                      String mediaFormat,
                      Locale editionLanguage,
                      LocalDate editionDate,
                      TypeOfBinding typeOfBinding) {

        if (editionNumber == null || editionNumber.isBlank()) {
            throw new IllegalArgumentException("Edition Number cannot be blank");
        }
        if (physicalAttributes == null || publicationTypeId == null || publisherID == null) {
            throw new IllegalArgumentException("Physical Attributes, publication type ID and/or publisher ID cannot be null");
        }
        if (mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("Media format cannot be null or blank");
        }
        if (!mediaFormat.equals(BOOK) && !mediaFormat.equals(MAGAZINE)) {
            throw new IllegalArgumentException("Invalid media format");
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

        validateEditionCode(code, mediaFormat, editionDate);

        this._editionID = new EditionID(UUID.randomUUID().toString());
        this._editionNumber = editionNumber;
        this._code = code;
        this._physicalAttributes = physicalAttributes;
        this._publicationTypeId = publicationTypeId;
        this._publisherID = publisherID;
        this._mediaFormat = mediaFormat;
        this._editionLanguage = editionLanguage;
        this._editionDate = editionDate;
        this._typeOfBinding = typeOfBinding;
    }

    /**
     * Validates the identification code according to media format and publication year.
     *
     * @param code the code to validate
     * @param mediaFormat the media format
     * @param date the publication date
     */
    private static void validateEditionCode(Code code, String mediaFormat, LocalDate date) {
        int year = date.getYear();

        if (mediaFormat.equals(BOOK)) {
            if (year >= 2007) {
                if (code == null || code.getIsbn13() == null || code.getIsbn13().isBlank()) {
                    throw new IllegalArgumentException("Books published in 2007 or later must have an ISBN-13 code.");
                }
            } else if (year >= 1970) {
                if (code == null || code.getIsbn10() == null || code.getIsbn10().isBlank()) {
                    throw new IllegalArgumentException("Books published between 1970 and 2006 must have an ISBN-10 code.");
                }
            }
        } else if (mediaFormat.equals(MAGAZINE)) {
            if (year >= 1976) {
                if (code == null || code.getIssn() == null || code.getIssn().isBlank()) {
                    throw new IllegalArgumentException("Magazines published in 1976 or later must have an ISSN code.");
                }
            }
        }
    }


    /** @return the edition number */
    public String getEditionNumber() { return _editionNumber; }

    /** @return the identification code */
    public Code getCode() { return _code; }

    /** @return the physical attributes */
    public PhysicalAttributes getPhysicalAttributes() { return _physicalAttributes; }

    /** @return the publication type ID */
    public PublicationTypeId getPublicationTypeID() { return _publicationTypeId; }

    /** @return the publisher ID */
    public PublisherID getPublisherID() { return _publisherID; }

    /** @return the media format */
    public String getMediaFormat() { return _mediaFormat; }

    /** @return the edition language */
    public Locale getEditionLanguage() { return _editionLanguage; }

    /** @return the publication date */
    public LocalDate getEditionDate() { return _editionDate; }

    /** @return the type of binding */
    public TypeOfBinding getTypeOfBinding() { return _typeOfBinding; }

    /**
     * Compares two editions by value (all attributes except identity).
     *
     * @param o object to compare
     * @return true if all attributes are equal
     */
    @Override
    public boolean sameAs(Object o) {
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
    public boolean equals(Object o){
        if(o == null || getClass() != o.getClass()) return false;
        Edition edition = (Edition) o;
        return Objects.equals(_editionNumber, edition._editionNumber)
                && Objects.equals(_code, edition._code)
                && Objects.equals(_physicalAttributes, edition._physicalAttributes)
                && Objects.equals(_publicationTypeId, edition._publicationTypeId)
                && Objects.equals(_publisherID, edition._publisherID)
                && Objects.equals(_editionLanguage, edition._editionLanguage)
                && Objects.equals(_editionDate, edition._editionDate)
                && Objects.equals(_typeOfBinding, edition._typeOfBinding);
    }
}
