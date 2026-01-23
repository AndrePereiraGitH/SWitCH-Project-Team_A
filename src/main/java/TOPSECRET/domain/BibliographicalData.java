package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Genres;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class BibliographicalData {

    // Attributes
    private String _title;
    private List<Author> _author;
    private Locale _language;
    private Publisher _publisher;
    private LocalDate _publishDate;
    private List<Genre> _genre;
    private int _numberOfPages;
    private String _chunksOfText;

    // Constructor
    public BibliographicalData(String title, List<Author> authors, Locale language, Publisher publisher, LocalDate publishDate,
                               List<Genre> genre, int numberOfPages, String chunksOfText) {

        // Validations

        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Authors list cannot be empty");
        }

        if (genre == null || genre.isEmpty()) {
            throw new IllegalArgumentException("Genres list cannot be empty");
        }

        if (numberOfPages <= 0) {
            throw new IllegalArgumentException("Number of pages must be positive");
        }

        this._title = title;
        this._author = List.copyOf(authors);
        this._language = language;
        this._publisher = publisher;
        this._publishDate = publishDate;
        this._genre = List.copyOf(genre);
        this._numberOfPages = numberOfPages;
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
        return _language;
    }

    public Publisher getPublisher() {
        return _publisher;
    }

    public LocalDate getPublishDate() {
        return _publishDate;
    }

    public List<Genre> getGenres() {
        return _genre;
    }

    public int getNumberOfPages() {
        return _numberOfPages;
    }

    public String getChunksOfText() {
        return _chunksOfText;
    }

}
