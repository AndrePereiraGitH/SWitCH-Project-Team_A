package TOPSECRET.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents the literary information of a publication (book, magazine, others).
 * It represents the literary work as an immutable concept, i.e, what is found in the catalog.
 * <p>
 * This class stores immutable metadata such as title, authors, original language,
 * publication date, genres and additional text fragments. It enforces basic validation
 * rules to guarantee that essential fields are always present.
 * </p>
 *
 * <p>
 * Instances of this class are immutable: all collections are defensively copied and
 * no setters are provided.
 * </p>
 */


public class BibliographicalData {

    private final String _title;
    private final List<Author> _authors;
    private final Locale _originalLanguage;
    private final LocalDate _publicationDate;
    private final List<Genre> _genres;
    private final String _chunksOfText;

    // Constructor =====================================================================================================

    /**
     * Creates a new {@code BibliographicalData} instance with the provided metadata.
     *
     * @param title            the title of the work; must not be {@code null} or blank
     * @param authors          the list of authors; must not be {@code null} or empty
     * @param originalLanguage the original language of the work; may be {@code null} if unknown
     * @param publicationDate      the publication date; may be {@code null} if unknown
     * @param genres            the list of genres associated with the work; must not be {@code null} or empty
     * @param chunksOfText     additional text fragments or notes; may be {@code null}
     *
     * @throws IllegalArgumentException if the title is null/blank, if the authors list is null/empty,
     *                                  or if the genres list is null/empty
     */

    public BibliographicalData(String title, List<Author> authors, Locale originalLanguage, LocalDate publicationDate,
                List<Genre> genres, String chunksOfText) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }

        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Authors list cannot be empty");
        }

        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Genres list cannot be empty");
        }

        this._title = title;
        this._authors = List.copyOf(authors);
        this._originalLanguage = originalLanguage;
        this._publicationDate = publicationDate;
        this._genres = List.copyOf(genres);
        this._chunksOfText = chunksOfText;
    }

    // Getters =========================================================================================================

    /**
     * Returns the title of the work.
     *
     * @return the non-null title
     */

    public String getTitle() {
        return _title;
    }

    /**
     * Returns the list of authors of the work.
     *
     * @return a non-null, non-empty list of authors
     */

    public List<Author> getAuthors() {
        return _authors;
    }

    /**
     * Returns the original language of the work.
     *
     * @return the original language, or {@code null} if unknown
     */

    public Locale getOriginalLanguage() {
        return _originalLanguage;
    }

    /**
     * Returns the publication date of the work.
     *
     * @return the publication date, or {@code null} if unknown
     */

    public LocalDate getPublicationDate() {
        return _publicationDate;
    }

    /**
     * Returns the list of genres associated with the work.
     *
     * @return a non-null, non-empty list of genres
     */

    public List<Genre> getGenres() {
        return _genres;
    }

    /**
     * Returns additional text fragments or notes associated with the work.
     *
     * @return the text fragments, or {@code null} if none were provided
     */

    public String getChunksOfText() {
        return _chunksOfText;
    }

    // Equals and HashCode =============================================================================================

    /**
     * Compares this instance with another object for logical equality.
     * Two instances are considered equal if all their fields are equal.
     *
     * @param o the object to compare with
     * @return {@code true} if both objects represent the same bibliographical data;
     *         {@code false} otherwise
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BibliographicalData)) return false;

        BibliographicalData that = (BibliographicalData) o;

        return Objects.equals(_title, that._title) &&
                Objects.equals(_authors, that._authors) &&
                Objects.equals(_originalLanguage, that._originalLanguage) &&
                Objects.equals(_publicationDate, that._publicationDate) &&
                Objects.equals(_genres, that._genres) &&
                Objects.equals(_chunksOfText, that._chunksOfText);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code of this instance
     */

    @Override
    public int hashCode() {
        return Objects.hash(_title, _authors, _originalLanguage, _publicationDate, _genres, _chunksOfText);
    }

    //US021 Start ======================================================================================================

    /**
     * Indicates whether this work is associated with the specified genre.
     *
     * @param genres the genre to check; may be {@code null}
     * @return {@code true} if the genre is present, {@code false} otherwise
     */

    public boolean hasGenre(Genre genres) {
        if (genres == null) return false;
        return _genres.contains(genres);
    }
    //US021 End ========================================================================================================

    public BibliographicalData.BibliographicalDataDetails getBibliographicalDataDetails () {
        return new BibliographicalData.BibliographicalDataDetails(
                _title,
                _authors,
                _originalLanguage,
                _publicationDate,
                _genres,
                _chunksOfText
        );
    }

    public record BibliographicalDataDetails(
            String title,
            List<Author> authors,
            Locale originalLanguage,
            LocalDate publicationDate,
            List<Genre> genres,
            String chunksOfText
    ) {}
}
