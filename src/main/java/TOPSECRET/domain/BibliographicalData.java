package TOPSECRET.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class BibliographicalData {

    // Attributes
    private String _title;
    private List<Author> _author;
    private Locale _originalLanguage;
//    private Publisher _publisher;                 //It became an attribute of the Edition class
    private LocalDate _publishDate;
    private List<Genre> _genre;
//    private int _numberOfPages;                   //It became an attribute of the PhysicalAttributes class
    private String _chunksOfText;

    // Constructor
    //public BibliographicalData(String title, List<Author> authors, Locale originalLanguage, Publisher publisher, LocalDate publishDate,
    //List<Genre> genre, int numberOfPages, String chunksOfText) {
    public BibliographicalData(String title, List<Author> authors, Locale originalLanguage, LocalDate publishDate,
                List<Genre> genre, String chunksOfText) {
        // Validations

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

    public Locale getLanguage() {
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
        return _genre.contains(genre);
    }
    //US021 End
}
