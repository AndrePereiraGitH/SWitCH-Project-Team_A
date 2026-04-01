package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationSample.PublicationSample;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) used in US027 – "To get the list of items on direct sale of a given publication".
 * <p>
 * This DTO provides a simplified view of a {@link PublicationSample} domain object.
 * It extracts only the information required by the UI to display the list of publications that a user may
 * choose to put for sale in a direct sale listing.
 * <p>
 * The DTO contains only primitive types, Strings, and simple collections, ensuring that the presentation
 * layer remains decoupled from the domain model.
 */

public class PublicationSampleDTO_US027 {

    private final String _title;
    private final List<String> _authorsName;
    private final List<String> _genres;
    private final String _ownerUsername;
    private final String _pubStatus;
    private final String _editionNumber;
    private final String _code;
    private final String _mediaFormat;
    private final String _editionLanguage;
    private final String _publisherName;
    private final String _condition;

    // Constructor =====================================================================================================

    /**
     * Constructs a DTO containing the relevant information extracted from a {@link PublicationSample}.
     *
     * @param title           the title of the publication
     * @param authorsName     list of author names
     * @param genres          list of genre types
     * @param ownerUsername   username of the publication's owner
     * @param pubStatus       publication status (for sale, sold, reading, etc.)
     * @param editionNumber   edition number
     * @param code            edition code (ISBN-13 characters, ISBN-10 characters, ISSN)
     * @param mediaFormat     media format (book, magazine, etc.)
     * @param editionLanguage language of the edition
     * @param publisherName   publisher name
     * @param condition       condition description (like new, new, good, etc.)
     */

    public PublicationSampleDTO_US027(String title, List<String> authorsName, List<String> genres, String ownerUsername,
                                      String pubStatus, String editionNumber, String code, String mediaFormat,
                                      String editionLanguage, String publisherName, String condition) {
        this._title = title;
        this._authorsName = authorsName;
        this._genres = genres;
        this._ownerUsername = ownerUsername;
        this._pubStatus = pubStatus;
        this._editionNumber = editionNumber;
        this._code = code;
        this._mediaFormat = mediaFormat;
        this._editionLanguage = editionLanguage;
        this._publisherName = publisherName;
        this._condition = condition;
    }

    // Getters =========================================================================================================

    /**
     * Returns the title of the publication.
     *
     * @return the publication title
     */

    public String getTitle() {
        return _title;
    }

    /**
     * Returns the list of author names associated with the publication.
     * <p>
     * This list contains only the authors' display names, extracted from the
     * corresponding domain objects, and is intended for UI presentation.
     *
     * @return a list of author names
     */

    public List<String> getAuthorsName() {
        return _authorsName;
    }

    /**
     * Returns the list of genre types associated with the publication.
     *
     * @return a list of genre types
     */

    public List<String> getGenres() {
        return _genres;
    }

    /**
     * Returns the username of the owner of the publication.
     *
     * @return the owner's username
     */

    public String getOwnerUsername(){
        return _ownerUsername;
    }

    /**
     * Returns the publication status.
     * <p>
     * The value corresponds to the string representation of the domain
     * {@code PublicationStatus} and is suitable for UI display.
     *
     * @return the publication status
     */

    public String getPublicationStatus() {
        return _pubStatus;
    }

    /**
     * Returns the edition number of the publication.
     *
     * @return the edition number
     */

    public String getEditionNumber() {
        return _editionNumber;
    }

    /**
     * Returns the code (ISBN, ISSN) of the edition.
     *
     * @return the code
     */

    public String getCode() {
        return _code;
    }

    /**
     * Returns the media format (book, maganize, etc.) of the edition.
     *
     * @return the media format
     */

    public String getMediaFormat() {
        return _mediaFormat;
    }

    /**
     * Returns the language of the edition.
     *
     * @return the edition language
     */

    public String getEditionLanguage() {
        return _editionLanguage;
    }

    /**
     * Returns the publisher of the edition.
     *
     * @return the publisher's name
     */

    public String getPublisherName() {
        return _publisherName;
    }

    /**
     * Returns the condition description of the publication.
     *
     * @return the condition description
     */

    public String getCondition() {
        return _condition;
    }


    // Equals and HashCode =============================================================================================

    /**
     * Indicates whether this DTO is equal to another object.
     * <p>
     * Two {@code PublicationSampleDTO_US027} instances are considered equal if they contain
     * the same core identifying fields: title, authors, owner username, publication status,
     * edition number, code and condition. These fields uniquely represent a publication
     * sample in the context of US027 and are sufficient to determine logical equality
     * for comparison and UI display purposes.
     *
     * @param o the object to compare with this DTO
     * @return {@code true} if the objects are logically equal, {@code false} otherwise
     */

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublicationSampleDTO_US027 that = (PublicationSampleDTO_US027) o;
        return Objects.equals(_title, that._title) && Objects.equals(_authorsName, that._authorsName)
                && Objects.equals(_ownerUsername, that._ownerUsername) && Objects.equals(_pubStatus, that._pubStatus)
                && Objects.equals(_editionNumber, that._editionNumber) && Objects.equals(_code, that._code)
                && Objects.equals(_condition, that._condition);
    }

    /**
     * Computes a hash code consistent with {@link #equals(Object)}.
     * <p>
     * The hash code is derived from the same identifying fields used in the equality
     * comparison. This ensures that DTO instances considered equal will always produce
     * the same hash code, maintaining the general contract of {@code hashCode()} and
     * allowing this DTO to be safely used in hash‑based collections.
     *
     * @return a hash code value for this DTO
     */

    @Override
    public int hashCode() {
        return Objects.hash(_title, _authorsName, _ownerUsername, _pubStatus, _editionNumber, _code, _condition);
    }


    // toDTO(s) ===========================================================================================================

    /**
     * Converts a {@link PublicationSample} domain object into a {@link PublicationSampleDTO_US027}.
     * <p>
     * This method extracts only the fields required by US027 and transforms domain‑specific types
     * (e.g., enums, value objects) into Strings or simple values suitable for the UI layer.
     *
     * @param publicationSample the domain object to convert
     * @return a DTO containing the relevant publication information
     */

//    public static PublicationSampleDTO_US027 toDTO(PublicationSample publicationSample) {
//
//        if  (publicationSample == null) {
//            throw new NullPointerException("Publication sample can not be null");
//        }
//        return new PublicationSampleDTO_US027(
//                publicationSample.getBibliographicalData().getTitle(),
//                publicationSample.getBibliographicalData().getAuthors().stream()
//                        .map(a -> a.getName())
//                        .toList(),
//                publicationSample.getBibliographicalData().getGenres().stream()
//                        .map(Genre::getGenreType)
//                        .toList(),
//                publicationSample.getOwnerId().getUserName(),
//                publicationSample.getPublicationStatus().toString(),
//                publicationSample.getEdition().getEditionNumber(),
//                publicationSample.getEdition().getCode().toString(),
//                publicationSample.getEdition().getPublicationType().getMediaFormat().toString(),
//                publicationSample.getEdition().getEditionLanguage().toString(),
//                publicationSample.getEdition().getPublisher().getName(),
//                publicationSample.getCondition().toString()
//        );
//    }

    /**
     * Converts a list of {@link PublicationSample} entities into a list of
     * {@link PublicationSampleDTO_US027} objects.
     *
     * <p>The method accepts an empty list and returns an empty list. However,
     * it does not allow {@code null} as the input list, nor does it allow
     * {@code null} elements inside the list. If either occurs, an
     * {@link IllegalArgumentException} is thrown.</p>
     *
     * <p>Each non-null element in the list is converted using
//     * {@link #toDTO(PublicationSample)}, and the order of elements is preserved.</p>
     *
     * @param listOfItems the list of publication samples to convert; must not be {@code null}
     * @return a list of DTOs corresponding to the given publication samples; never {@code null}
     * @throws IllegalArgumentException if {@code listOfItems} is {@code null} or contains {@code null} elements
     */

//    public static List<PublicationSampleDTO_US027> toDTOs(List<PublicationSample> listOfItems) {
//
//        if  (listOfItems == null) {
//            throw new NullPointerException("List of items can not be null");
//        }
//
//        List<PublicationSampleDTO_US027> listOfPublicationSampleDTO = new ArrayList<>();
//
//        for (PublicationSample pubSample : listOfItems) {
//            if (pubSample == null) {
//                throw new IllegalArgumentException("List of items must not contain null elements");
//            }
//            listOfPublicationSampleDTO.add(toDTO(pubSample));
//        }
//
//        return List.copyOf(listOfPublicationSampleDTO);
//    }
}
