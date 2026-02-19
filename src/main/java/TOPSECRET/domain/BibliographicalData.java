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

    // Attributes
    private final String _title;
    private final List<Author> _author;
    private final Locale _originalLanguage;
//    private final Publisher _publisher;                 //It became an attribute of the Edition class
    private final LocalDate _publishDate;
    private final List<Genre> _genre;
//    private final int _numberOfPages;                   //It became an attribute of the PhysicalAttributes class
    private final String _chunksOfText;

    // Constructor
    //public BibliographicalData(String title, List<Author> authors, Locale originalLanguage, Publisher publisher, LocalDate publishDate,
    //List<Genre> genre, int numberOfPages, String chunksOfText) {
    public BibliographicalData(String title, List<Author> authors, Locale originalLanguage, LocalDate publishDate,
                List<Genre> genre, String chunksOfText) {
        // Validations

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }

        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Authors list cannot be empty");
        }

        if (genre == null || genre.isEmpty()) {
            throw new IllegalArgumentException("Genres list cannot be empty");
        }

//        if (numberOfPages <= 0) {
//            throw new IllegalArgumentException("Number of pages must be positive");
//        }

        this._title = title;
        this._author = List.copyOf(authors);
        this._originalLanguage = originalLanguage;
//        this._publisher = publisher;
        this._publishDate = publishDate;
        this._genre = List.copyOf(genre);
//        this._numberOfPages = numberOfPages;
        this._chunksOfText = chunksOfText;
    }

    // Getters
    public String getTitle() {
        return _title;
    }

    public List<Author> getAuthors() {
        return _author;
    }

    public Locale getOriginalLanguage() {
        return _originalLanguage;
    }

//    public Publisher getPublisher() {
//        return _publisher;
//    }

    public LocalDate getPublishDate() {
        return _publishDate;
    }

    public List<Genre> getGenres() {
        return _genre;
    }

//    public int getNumberOfPages() {
//        return _numberOfPages;
//    }

    public String getChunksOfText() {
        return _chunksOfText;
    }

    //US021 Start
    public boolean hasGenre(Genre genre) {
        if (genre == null) return false;
        return _genre.contains(genre);
    }
    //US021 End

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BibliographicalData)) return false;

        BibliographicalData that = (BibliographicalData) o;

        return Objects.equals(_title, that._title) &&
                Objects.equals(_author, that._author) &&
                Objects.equals(_originalLanguage, that._originalLanguage) &&
                Objects.equals(_publishDate, that._publishDate) &&
                Objects.equals(_genre, that._genre) &&
                Objects.equals(_chunksOfText, that._chunksOfText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_title, _author, _originalLanguage,
                _publishDate, _genre, _chunksOfText);
    }
}
