package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.PublicationSample.PublicationSample;

import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) used in US015 – "To put for sale a publication in my library in a direct sale".
 * <p>
 * This DTO provides a simplified view of a {@link PublicationSample} domain object.
 * It extracts only the information required by the UI to display the list of publications that a user may
 * choose to put for sale in a direct sale listing.
 * <p>
 * The DTO contains only primitive types, Strings, and simple collections, ensuring that the presentation
 * layer remains decoupled from the domain model.
 */
public class PublicationSampleDTO_US015 {

    private final String _title;
    private final List<String> _authorsName;
    private final String _code;
    private final String _mediaFormat;

    // =================================================================================================================
    // ================ CONSTRUCTOR ====================================================================================
    // =================================================================================================================

    /**
     * Constructs a DTO containing the relevant information extracted from a {@link PublicationSample}.
     *
     * @param title           the title of the publication
     * @param authorsName     list of author names
     * @param code            edition code (ISBN-13 characters, ISBN-10 characters, ISSN)
     * @param mediaFormat     media format (book, magazine, etc.)
     */
    public PublicationSampleDTO_US015(String title, List<String> authorsName, String code, String mediaFormat) {
        this._title = title;
        this._authorsName = List.copyOf(authorsName);
        this._code = code;
        this._mediaFormat = mediaFormat;
    }


    // =================================================================================================================
    // ================ BUSINESS METHODS ===============================================================================
    // =================================================================================================================

    /**
     * Converts the given domain objects into a {@link PublicationSampleDTO_US015}.
     * <p>
     * This method assembles a DTO using information retrieved from multiple
     * aggregates: {@link BibliographicalData}, {@link Author}, and {@link Edition}.
     * </p>
     *
     * @param bd       the bibliographical data associated with the publication;
     *                 must not be {@code null}
     * @param authors  the list of authors referenced by the bibliographical data;
     *                 must not be {@code null}
     * @param edition  the edition information for the publication;
     *                 must not be {@code null}
     * @return a DTO containing the publication’s title, author names, code,
     *         and media format; never {@code null}
     *
     * @throws IllegalArgumentException if any of the arguments is {@code null}
     */
    public static PublicationSampleDTO_US015 toDTO(BibliographicalData bd, List<Author> authors,  Edition edition) {

        if  (bd == null) {
            throw new IllegalArgumentException("Bibliographical data can not be null");
        }
        if  (authors == null) {
            throw new IllegalArgumentException("Authors can not be null");
        }
        if  (edition == null) {
            throw new IllegalArgumentException("Edition can not be null");
        }

        return new PublicationSampleDTO_US015(
                bd.getTitle(),
                authors.stream().map(Author::getName).toList(),
                edition.getCode().toString(),
                edition.getPublicationType().getMediaFormat()
        );
    }

    // =================================================================================================================
    // ================ GETTERS ========================================================================================
    // =================================================================================================================

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
        return List.copyOf(_authorsName);
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
     * Returns the media format (book, magazine, etc.) of the edition.
     *
     * @return the media format
     */
    public String getMediaFormat() {
        return _mediaFormat;
    }


    // =================================================================================================================
    // ================ EQUALS AND HASHCODE ============================================================================
    // =================================================================================================================

    /**
     * Indicates whether this DTO is equal to another object.
     * <p>
     * Two {@code PublicationSampleDTO_US015} instances are considered equal if they contain
     * the same core identifying fields: title, authors, code and media format.
     * These fields uniquely represent a publication  sample in the context of US015 and are sufficient to determine
     * logical equality for comparison and UI display purposes.
     *
     * @param o the object to compare with this DTO
     * @return {@code true} if the objects are logically equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublicationSampleDTO_US015 that = (PublicationSampleDTO_US015) o;
        return Objects.equals(_title, that._title) && Objects.equals(_authorsName, that._authorsName)
                && Objects.equals(_code, that._code) && Objects.equals(_mediaFormat, that._mediaFormat);
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
        return Objects.hash(_title, _authorsName, _code, _mediaFormat);
    }
}
